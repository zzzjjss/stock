package com.uf.store.shiro;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uf.store.restful.dto.RestfulResponse;
import com.uf.store.restful.dto.RestfulResponse.ResultCode;

public class StatelessAuthFilter  extends AuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        String token = this.getToken(request);
        if (StringUtils.isEmpty(token)) {
            this.printUnauthorized("token is empty", WebUtils.toHttp(response));
            return false;
        }
        boolean loginSuccess = this.login(new UserToken(token));
        if (!loginSuccess) {
            this.printUnauthorized("use token to login fail", WebUtils.toHttp(response));
        }
        return loginSuccess;
    }

    private String getToken(ServletRequest request) {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        return  httpServletRequest.getHeader("Authorization");
    }

    private void printUnauthorized(String message, HttpServletResponse response) {
    	RestfulResponse result = new RestfulResponse();
        result.setResultCode(ResultCode.FAIL);
        result.setMes(message);
        try {
			ObjectMapper mapper = new ObjectMapper();
			String content =mapper.writeValueAsString(result);
			response.setContentType("application/json");
			response.setContentLength(content.length());
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
            final PrintWriter writer = response.getWriter();
            writer.write(content);
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }

    private boolean login(UserToken token) {
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            return true;
        } catch (AuthenticationException e) {
            return false;
        }
    }

   
}
