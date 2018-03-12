package com.scen.bookstore.web.filter;

import com.scen.bookstore.domain.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Scen
 * @date 2017/12/2
 */
@WebFilter(filterName = "RoleUserFilter",urlPatterns = "/user/*")
public class RoleUserFilter implements Filter {
    @Override
    public void init(FilterConfig config) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        //强转
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        //处理业务
        //从session中把用户对象得到
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.getWriter().print("请先登录!");
            response.setHeader("refresh", "2;url=" + request.getContextPath() + "/login.jsp");
            return;
        } else {
            chain.doFilter(req, resp);
        }
    }

    @Override
    public void destroy() {

    }
}
