package com.yilan.aop;

import com.yilan.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Slf4j
@Aspect
@Component
public class ControllerAspect {

    @Pointcut("execution(public * com.yilan.controller..*.*(..))")
    public void anyController() {

    }

    @Around("anyController()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        MDC.put("sessionTokenId", UUID.randomUUID().toString());
        log.info("========请求开始===========");
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;

        //断言
        assert sra != null;
        HttpServletRequest request = sra.getRequest();
        //获取请求相关信息
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();

        //获取调用方法信息
        Signature signature = pjp.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        Object[] args = pjp.getArgs();

        //参数
        String params = "";
        //获取请求参数集合并进行遍历拼接
        if (args.length > 0) {
            if (StringUtils.equals(method, "POST")) {
                Object object = args[0];
                params = JsonMapper.toNormalJson(object);
            } else if (StringUtils.equals(method, "GET")) {
                params = queryString;
            }
        }

        //打印输出内容
        log.info("Request => ClassMethod: {}#{}() URI:{}, method:{}, URL:{}, params:{}",
                className, methodName, uri, method, url, params);

        //result的值就是被拦截方法的返回值
        try {
            //process方法是调用实际所拦截的controller中的方法，这里的result为调用方法的返回值
            Object result = pjp.proceed();
            long endTime = System.currentTimeMillis();
            //定义请求结束时的返回数据
            log.info("Response => ClassMethod: {}#{}(), URI: {}, HttpMethod: {}, URL: {}, time: {}ms, result: {}",
                    className,methodName,uri,method,url,(endTime - startTime), JsonMapper.toNormalJson(result));
            return result;
        } catch (Throwable e) {
            long endTime = System.currentTimeMillis();

            log.error("Error => ClassMethod: {}#{}(), URI: {}, HttpMethod: {}, URL: {}, time: {}ms",
                    className,methodName,uri,method,url,(endTime - startTime), e);
            throw e;
        } finally {
            MDC.remove("sessionTokenId");
            log.info("========请求结束==========");
        }

    }
}
