package cn.qdevelop.demo.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * 自定义自己的拦截器，用来控制自己的请求
 * 监控/app/api/下拦截所有的请求
 * @author janson
 *
 */
@WebFilter(urlPatterns="/app/api/*")
public class MyFilter implements Filter{

	
	/**
	 * 应用起来初始化一次
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println(((HttpServletRequest)request).getRequestURI());
		chain.doFilter(request,response);
	}

	public void destroy() {
		
	}

}
