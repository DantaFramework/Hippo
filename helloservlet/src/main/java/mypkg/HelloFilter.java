package mypkg;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.wrappers.SlingHttpServletRequestWrapper;

import org.apache.sling.api.SlingHttpServletResponse;

/**
 * Servlet Filter implementation class 
 */
public class HelloFilter implements Filter {

	static final String DANTA_CONTENT_MODEL = "DANTA_CONTENT_MODEL";

	private ServletContext context;
	
	public void init(FilterConfig fConfig) throws ServletException {
        context = fConfig.getServletContext();
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		request.setAttribute(DANTA_CONTENT_MODEL, "DANTA CONTENT MODEL (added by Filter)");


        RequestDispatcher rd = context
                .getContext("/org.apache.sling.starter-10-webapp")
                .getRequestDispatcher("/template.support.json");

        /*
        RequestDispatcher rd = context
                .getContext("/helloservlet")
                .getRequestDispatcher("/sayhello2");
        */

        // The problem might that the .include needs to send a SlingHttpServletRequest object
        // If this is the case, then, how to generate it from a ServletRequest object?
        //SlingHttpServletRequest slingHttpRequest = (SlingHttpServletRequest) request;
        //GetRequest getRequest = new GetRequest(slingHttpRequest);

        rd.include(request, response);

		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	public void destroy() {
		//we can close resources here
	}

    private static class GetRequest extends SlingHttpServletRequestWrapper {
        public GetRequest(SlingHttpServletRequest wrappedRequest) {
            super(wrappedRequest);
        }
        @Override
        public String getMethod() {
            return "GET";
        }
    }
}
