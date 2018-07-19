package com.zhangmiaoxin.www.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CharsetFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest hreq = (HttpServletRequest) req;
        HttpServletResponse hresp = (HttpServletResponse) resp;
        hreq.setCharacterEncoding("utf-8");
        hresp.setCharacterEncoding("utf-8");
        chain.doFilter(hreq, hresp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
