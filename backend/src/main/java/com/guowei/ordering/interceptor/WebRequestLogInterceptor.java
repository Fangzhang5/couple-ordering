package com.guowei.ordering.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class WebRequestLogInterceptor implements HandlerInterceptor {

    private static final String START_TIME_ATTRIBUTE = "requestStartTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(START_TIME_ATTRIBUTE, System.currentTimeMillis());

        log.info("Request started: {} {}{} -> {}",
                request.getMethod(),
                request.getRequestURI(),
                formatQueryString(request),
                formatHandler(handler));

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception exception) {
        long duration = getDuration(request);

        if (exception == null) {
            log.info("Request completed: {} {}{} -> {}, status={}, duration={}ms",
                    request.getMethod(),
                    request.getRequestURI(),
                    formatQueryString(request),
                    formatHandler(handler),
                    response.getStatus(),
                    duration);
            return;
        }

        log.error("Request failed: {} {}{} -> {}, status={}, duration={}ms",
                request.getMethod(),
                request.getRequestURI(),
                formatQueryString(request),
                formatHandler(handler),
                response.getStatus(),
                duration,
                exception);
    }

    private String formatHandler(Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            return handlerMethod.getBeanType().getSimpleName() + "#" + handlerMethod.getMethod().getName();
        }

        return handler.getClass().getSimpleName();
    }

    private String formatQueryString(HttpServletRequest request) {
        String queryString = request.getQueryString();
        return queryString == null ? "" : "?" + queryString;
    }

    private long getDuration(HttpServletRequest request) {
        Object startTime = request.getAttribute(START_TIME_ATTRIBUTE);
        if (startTime instanceof Long startTimeMillis) {
            return System.currentTimeMillis() - startTimeMillis;
        }

        return 0;
    }
}
