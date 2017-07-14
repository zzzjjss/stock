package com.uf.filter;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.uf.bean.Result;

/**
 * Servlet Filter implementation class CustomerFilter
 */
@WebFilter("/CustomerFilter")
public class CustomerFilter implements Filter {

    /**
     * Default constructor.
     */
    public CustomerFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	     HttpServletRequest  httpRequest=(HttpServletRequest)request;
	     HttpServletResponse httpResponse = (HttpServletResponse)response;
	     Object manager=httpRequest.getSession().getAttribute("customer");
        String requestType = httpRequest.getHeader("X-Requested-With");
        Result result=new Result();
        if(manager==null){
          if (requestType != null && requestType.equals("XMLHttpRequest")) {
            result.setResult(Result.RESULT_NO_LOGIN);
            result.setMes("请登录");
            Gson gson=new Gson();
            Writer writer=httpResponse.getWriter();
            writer.write(gson.toJson(result));
            writer.flush();
            return;
          }else{
            String contextName=httpRequest.getServletContext().getContextPath();
            httpResponse.sendRedirect(contextName);
            httpResponse.flushBuffer();
            return;
          }
        }
	     chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
