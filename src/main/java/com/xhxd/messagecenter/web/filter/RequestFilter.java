package com.xhxd.messagecenter.web.filter;


import com.xhxd.messagecenter.common.Constants;
import com.xhxd.messagecenter.common.util.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Order(9)
@WebFilter(filterName="RequestFilter",urlPatterns={"/api/*"})
@Slf4j
public class RequestFilter implements Filter, EnvironmentAware {

    private final static String SECURITY_MSG = "[client_key] or [client_secret] is incorrect";

    private static final String ENV_CLIENT = "message.client.";

    private RelaxedPropertyResolver propertyResolver;
    private static final String CLIENT_KEY = "key";
    private static final String CLIENT_SECRET = "secret";


    private String serverKey;
    private String serverSecret;

    @Override
    public void init(FilterConfig filterConfig){
        log.info("RequestFilter init......");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Map<String,String> requestSecurityValue = this.getHeaderMap(request);
        if(!checkVerifyCode(requestSecurityValue)){
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED,SECURITY_MSG);
            return ;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        log.info("RequestFilter destroy.....");
    }


    /**
     * 推送校验
     * @param headerMap
     * @return
     */
    private boolean checkVerifyCode(Map<String,String> headerMap){
        String clientKey = headerMap.get(Constants.SecurityConstants.CLIENT_KEY);
        String clientSecret = headerMap.get(Constants.SecurityConstants.CLIENT_SECRET);

        if(!StringUtils.endsWithIgnoreCase(serverKey,clientKey)){
            return false;
        }
        if(!StringUtils.endsWithIgnoreCase(Md5Utils.md5(serverSecret),clientSecret)){
            return false;
        }
        return true;
    }

    /**
     * 获得用户验证信息
     * @param request
     * @return
     */
    private Map<String,String> getHeaderMap(HttpServletRequest request){

        Map<String,String> headerMap = new HashMap<>(2);
        String key = request.getHeader(Constants.SecurityConstants.CLIENT_KEY);
        String secret = request.getHeader(Constants.SecurityConstants.CLIENT_SECRET);

        headerMap.put(Constants.SecurityConstants.CLIENT_KEY,key);
        headerMap.put(Constants.SecurityConstants.CLIENT_SECRET,secret);
        return headerMap;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, ENV_CLIENT);

        serverKey = propertyResolver.getProperty(RequestFilter.CLIENT_KEY);
        serverSecret = propertyResolver.getProperty(RequestFilter.CLIENT_SECRET);
    }
}
