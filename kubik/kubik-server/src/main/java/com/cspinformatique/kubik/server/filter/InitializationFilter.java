package com.cspinformatique.kubik.server.filter;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.cspinformatique.kubik.server.domain.company.service.CompanyService;

@Component
public class InitializationFilter implements Filter {
	@Resource
	private CompanyService companyService;

	private boolean initialized;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (!initialized && request instanceof HttpServletRequest) {
			String url = ((HttpServletRequest) request).getRequestURL().toString();
			if (!url.endsWith("/initialization") && !url.endsWith("favicon.ico") && !url.contains("/css/")
					&& !url.contains("/js/") && !url.contains("/libs/") && !url.endsWith("/logo")
					&& !url.endsWith("/user/new")&& !url.endsWith("/company/new")) {
				initialized = companyService.findComapny().isPresent();

				if (!initialized)
					((HttpServletResponse) response).sendRedirect("/initialization");
			}
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}
}
