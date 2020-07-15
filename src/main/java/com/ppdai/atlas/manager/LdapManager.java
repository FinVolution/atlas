package com.ppdai.atlas.manager;

import com.google.common.collect.Lists;
import com.ppdai.atlas.dto.UserDto;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;

import static java.util.Comparator.comparing;


@Slf4j
@Component
@ConfigurationProperties("ldap")
public class LdapManager {
    private static final String INITIAL_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
    private static final String SECURITY_AUTHENTICATION = "simple";
    private static final String[] LDAP_RETURNING_ATTRIBUTES = new String[] {
            "cn", "department", "employeeID", "mail", "mobile", "sAMAccountName", "createTimeStamp", "modifyTimeStamp"};

    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss.'0Z'");
    @Setter
    private String url;
    @Setter
    private String username;
    @Setter
    private String password;


    /**
     * 查询有效的用户
     *
     * @param dateTime
     * @return
     * @throws Exception
     */
    public void queryActiveUserAfter(Date dateTime, Consumer<UserDto> consumer) throws Exception {
        queryUser("OU=拍拍贷总公司", format.format(dateTime), consumer);
    }

    /**
     * 查询禁用的用户
     *
     * @param dateTime
     * @return
     * @throws Exception
     */
    public void queryDisabledUserAfter(Date dateTime, Consumer<UserDto> consumer) throws Exception {
        queryUser("OU=Disable", format.format(dateTime), userDto -> {
            userDto.setActive(false);
            consumer.accept(userDto);
        });
    }

    public Optional<UserDto> queryActivedByName(String username) throws Exception {
        return queryByName("OU=Disable", username).map(userDto -> {
            userDto.setActive(false);
            return userDto;
        });
    }

    public Optional<UserDto> queryDisabledByName(String username) throws Exception {
        return queryByName("OU=拍拍贷总公司", username);
    }

    private Optional<UserDto> queryByName(String searchName, String username) throws Exception {
        LdapContext context = initLdapContext();
        SearchControls searchCtls = new SearchControls();
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchCtls.setReturningAttributes(LDAP_RETURNING_ATTRIBUTES);
        String searchFilter = "(&(objectClass=top)(objectClass=person)(objectClass=organizationalPerson)(objectClass=user)(sAMAccountName=" + username + "))";
        NamingEnumeration<SearchResult> result = context.search(searchName, searchFilter, searchCtls);
        UserDto ldapUser = null;
        if (result != null && result.hasMoreElements()) {
            SearchResult sr = result.next();
            ldapUser = transUserInfo(sr.getAttributes());
        }
        return Optional.ofNullable(ldapUser);
    }

    private void queryUser(String searchName, String dateTime, Consumer<UserDto> consumer) throws Exception {
        LdapContext context = initLdapContext();
        SearchControls searchCtls = new SearchControls();
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchCtls.setReturningAttributes(LDAP_RETURNING_ATTRIBUTES);

        String searchFilter = "(&(objectClass=top)(objectClass=person)(objectClass=organizationalPerson)(objectClass=user)(modifyTimeStamp>=" + dateTime + "))";
        searchCtls.setTimeLimit(5000);

        // 分页查询
        int pageSize = 1000;
        byte[] cookie;
        Control[] ctls = new Control[]{new PagedResultsControl(pageSize, Control.CRITICAL)};
        context.setRequestControls(ctls);
        do {
            NamingEnumeration<SearchResult> result = context.search(searchName, searchFilter, searchCtls);
            List<UserDto> results = Lists.newArrayList();
            while (result != null && result.hasMoreElements()) {
                SearchResult sr = result.next();
                UserDto ldapUser = transUserInfo(sr.getAttributes());
                results.add(ldapUser);
            }
            results.sort(comparing(UserDto::getLdapUpdateTime));
            results.forEach(consumer);
            cookie = parseControls(context.getResponseControls());
            context.setRequestControls(new Control[]{new PagedResultsControl(pageSize, cookie, Control.CRITICAL)});
        } while (cookie != null && cookie.length != 0);
        context.close();
    }

    private UserDto transUserInfo(Attributes attributes) {
        UserDto ldapUser = new UserDto();
        ldapUser.setExtensions(new HashMap<>());
        ldapUser.setUserName(parseString(attributes, "sAMAccountName"));
        ldapUser.setRealName(parseString(attributes, "cn"));
        ldapUser.setWorkNumber(parseString(attributes, "employeeID"));
        ldapUser.setLdapInsertTime(parseDate(attributes, "createTimeStamp"));
        ldapUser.setLdapUpdateTime(parseDate(attributes, "modifyTimeStamp"));
        ldapUser.getExtensions().put("department", parseString(attributes, "department"));
        ldapUser.getExtensions().put("mobile", parseString(attributes, "mobile"));
        ldapUser.setEmail(parseString(attributes, "mail"));
        return ldapUser;
    }

    private LdapContext initLdapContext() throws Exception {
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_AUTHENTICATION, SECURITY_AUTHENTICATION);
        env.put(Context.SECURITY_PRINCIPAL, "corp\\" + username);
        env.put(Context.SECURITY_CREDENTIALS, password);
        return new InitialLdapContext(env, null);
    }

    private byte[] parseControls(Control[] controls) {
        byte[] cookie = null;
        if (controls != null) {
            for (Control control : controls) {
                if (control instanceof PagedResultsResponseControl) {
                    PagedResultsResponseControl prrc = (PagedResultsResponseControl) control;
                    cookie = prrc.getCookie();
                }
            }
        }
        return cookie == null ? new byte[0] : cookie;
    }

    private Date parseDate(Attributes attributes, String key) {
        String str = parseString(attributes, key);
        try {
            return format.parse(str);
        } catch (Exception e) {
            log.warn("can not parse '{}' date : '{}'", key, str);
        }
        Calendar c = Calendar.getInstance();
        c.set(1980, 1, 1);
        return c.getTime();
    }

    private String parseString(Attributes attributes, String key) {
        if (attributes.get(key) == null) {
            return "";
        }
        return attributes.get(key).toString().replaceAll("^" + key + ":", "").replaceAll("^ *", "").replaceAll(" *$", "");
    }
}