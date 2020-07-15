package com.ppdai.atlas.config;

import com.ppdai.auth.common.identity.Identity;
import com.ppdai.auth.utils.PauthTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class UserAuditorAware implements AuditorAware<String> {

    public static final String DEFAULT_SYSTEM_NAME = "system";

    private PauthTokenUtil pauthTokenUtil;

    public UserAuditorAware(PauthTokenUtil pauthTokenUtil) {
        this.pauthTokenUtil = pauthTokenUtil;
    }

    @Override
    public String getCurrentAuditor() {
        String userName = DEFAULT_SYSTEM_NAME;

        try {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                HttpServletRequest request = requestAttributes.getRequest();

                Identity tokenInfo = pauthTokenUtil.getTokenInfo(request);
                userName = tokenInfo.getName();
            }
        } catch (Exception e) {
            log.error("Not able to read the user name by servlet requests. Probably it's a system call. ex=" + e.getMessage(), e);
        }

        return userName;
    }

}
