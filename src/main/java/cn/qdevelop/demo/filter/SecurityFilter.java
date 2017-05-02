package cn.qdevelop.demo.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * 以svr开头的接口调用，将均走安全验证
 * @author janson
 *
 */
@WebFilter(urlPatterns="/svr/*")
public class SecurityFilter  implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
//		Random r = new Random();
//		if(r.nextInt(5)==3){
//			((HttpServletResponse)response).setStatus(401);//返回页面未授权
//			return;
//		}
		
		chain.doFilter(request,response);
	}

	@Override
	public void destroy() {
		
	}

}
