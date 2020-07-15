package com.ppdai.atlas.aop;


import com.ppdai.atlas.entity.AuditLogEntity;
import com.ppdai.atlas.service.AuditLogService;
import com.ppdai.auth.common.identity.Identity;
import com.ppdai.auth.utils.PauthTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
@Order(4)
public class AuditLogAspect {
 // 1

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private PauthTokenUtil pauthTokenUtil;

    @Before("ResourcePointCuts.webController()")
    public void doSthBefore(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String methodName = method.getName();
        String returnType = method.getReturnType().getName();

        //可以获取请求request，进而获得对请求流程的控制
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //获取请求URL
        String url = request.getRequestURL().toString();
        //获取请求方法 POST,PUT,GET,DELETE
        String httpMethod = request.getMethod();

        if ("GET".equalsIgnoreCase(httpMethod)) {
            return;
        }

        Object[] args = joinPoint.getArgs();
        StringBuilder paraStr = new StringBuilder();
        for (Object o : args) {
            if (o == null) {
                continue;
            }
            paraStr.append(o.getClass().getName()).append(": ").append(o).append(";");
        }

        String remoteAddr = request.getRemoteAddr();

        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setHttpUri(url);
        auditLogEntity.setHttpMethod(httpMethod);
        auditLogEntity.setClientIp(remoteAddr);
        auditLogEntity.setClassMethodArgs(paraStr.toString());
        auditLogEntity.setClassMethod(methodName);
        auditLogEntity.setClassMethodReturn(returnType);

        Identity tokenInfo = pauthTokenUtil.getTokenInfo(request);
        String userName = tokenInfo.getName();

        auditLogEntity.setUserName(userName);

        auditLogService.addAuditLog(auditLogEntity);

    }

}
