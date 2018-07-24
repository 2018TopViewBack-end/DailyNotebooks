package com.zhangmiaoxin.www.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class LoginFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest hreq = (HttpServletRequest) req;
        HttpServletResponse hresp = (HttpServletResponse) resp;

        String uriEnd=hreq.getRequestURI();
        if(uriEnd.endsWith("login.jsp")||uriEnd.endsWith("register.jsp")
                ||uriEnd.endsWith("Login")||uriEnd.endsWith("Register")){
            chain.doFilter(hreq,hresp);
            return;
        }

        if (null == hreq.getSession().getAttribute("user")) {
            hresp.sendRedirect("login.jsp");
            return;
        } else {
            chain.doFilter(hreq, hresp);
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
