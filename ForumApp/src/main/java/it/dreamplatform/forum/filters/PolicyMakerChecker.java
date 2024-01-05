package it.dreamplatform.forum.filters;

import it.dreamplatform.forum.bean.UserBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class PolicyMakerChecker implements Filter {
    /**
     * Default constructor.
     */
    public PolicyMakerChecker() {
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
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession s = req.getSession();
        if (s.isNew() || s.getAttribute("user") == null) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED); //No authenticated user
            return;
        } else if (((UserBean) s.getAttribute("user")).getPolicyMakerID() == null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN); //Not authorized
            return;
        }
        // pass the request along the filter chain
        chain.doFilter(request, response);
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }
}
