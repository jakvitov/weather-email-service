package cz.jakvitov.wes.config.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

/**
 * Interceptor to add and remove traceIds from the thread contex - so they are available in the logs
 */

public class TraceIdInterceptior implements HandlerInterceptor {

    Logger logger = LogManager.getLogger(TraceIdInterceptior.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceId = request.getParameter("TRACE_ID");
        if (traceId == null){
            traceId = UUID.randomUUID().toString();
            logger.warn("Request missing TRACE_ID. Generating: " + traceId);
        }
        ThreadContext.put("TRACE_ID", traceId);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ThreadContext.clearAll();
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
