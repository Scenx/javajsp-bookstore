package com.scen.bookstore.web.filter;

import com.scen.bookstore.domain.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限过滤
 *
 * @author Scen
 * @date 2017/12/2
 */
@WebFilter(filterName = "RoleAdminFilter", urlPatterns = "/admin/*")
public class RoleAdminFilter implements Filter {
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
        //判断当前用户权限
        if (user != null) {
            if ("admin".equals(user.getRole())) {
                //放行
                chain.doFilter(req, resp);
            } else {
                response.getWriter().print("权限不足，请使用管理员登录");
                response.setHeader("refresh", "2;url=" + request.getContextPath() + "/index.jsp");
                return;
            }
        } else {
            response.sendRedirect(request.getContextPath()+"/login.jsp");
        }
    }

        @Override
        public void destroy () {

        }
    }

