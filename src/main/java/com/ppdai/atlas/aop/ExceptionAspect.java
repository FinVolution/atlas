package com.ppdai.atlas.aop;

import com.ppdai.atlas.common.exception.AtlasServiceException;
import com.ppdai.atlas.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Global Exception handler
 */
@Aspect
@Component
@Slf4j
@Order(5)
public class ExceptionAspect {

    @Around("com.ppdai.atlas.aop.ResourcePointCuts.webController()")
    public Object handleException(ProceedingJoinPoint apiMethod) {


        try {
            return apiMethod.proceed();
        } catch (AtlasServiceException e) {
            log.error(e.getMessage(), e);
            return Response.error(e);
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(), throwable);
            return Response.error(throwable);
        }
    }
}
