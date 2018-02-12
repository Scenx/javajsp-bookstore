package com.scen.bookstore.web.servlet;

import com.scen.bookstore.domain.Order;
import com.scen.bookstore.domain.OrderItem;
import com.scen.bookstore.domain.Product;
import com.scen.bookstore.domain.User;
import com.scen.bookstore.service.OrderService;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Scen
 * @date 2017/12/5
 */
@WebServlet(name = "OrderServlet", urlPatterns = "/order")
public class OrderServlet extends BaseServlet {
    /**
     * 创建订单
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void createOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1、封装Order对象
        Order order = new Order();
        try {
            BeanUtils.populate(order, request.getParameterMap());
            order.setId(UUID.randomUUID().toString());
            //把session对象中的用户信息保存到order对象中
            order.setUser((User) request.getSession().getAttribute("user"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //2、获取session对象中的购物车数据
        Map<Product, String> cart = (Map<Product, String>) request.getSession().getAttribute("cart");

        //3、遍历购物车中的数据，添加到orderItem对象中，同时把多个orderItem添加到list集合中
        List<OrderItem> list = new ArrayList<OrderItem>();
        for (Product p : cart.keySet()) {
            OrderItem oi = new OrderItem();
            //把Order对象添加到OrderItem中
            oi.setOrder(order);
            //把购物车中的商品对象添加到OrderItem中
            oi.setP(p);
            //购物车中的商品数量
            oi.setBuynum(Integer.parseInt(cart.get(p)));
            //把每个定单项添加到集合中
            list.add(oi);
        }

        //4、把集合放入到Order对象 中
        order.setOrderItems(list);

        //调用 业务逻辑
        OrderService os = new OrderService();
        os.addOrder(order);

        //
        request.setAttribute("orderid", order.getId());
        request.setAttribute("money", order.getMoney());
        request.getRequestDispatcher("/user/pay.jsp").forward(request, response);
    }

    /**
     * 根据用户id查询订单
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void findOrderByUserId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");

        OrderService os = new OrderService();
        List<Order> orders = os.findOrdersByUserId(user.getId());

        request.setAttribute("orders", orders);
        request.setAttribute("count", orders.size());
        request.getRequestDispatcher("/user/orderlist.jsp").forward(request, response);
    }

    /**
     * 根据订单id删除订单
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void delOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderid = request.getParameter("orderid");
        OrderService os = new OrderService();
        os.deleteOrder(orderid);
        User user = (User) request.getSession().getAttribute("user");
        if ("admin".equals(user.getRole())) {
            findOrderByManyCondition(request, response);
        } else {
            findOrderByUserId(request, response);
        }
    }

    /**
     * 多条件查询订单
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void findOrderByManyCondition(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderid = request.getParameter("orderid");
        String receiverName = request.getParameter("receiverName");
        if (orderid == null || receiverName == null) {
            orderid = "";
            receiverName = "";
        }
        OrderService os = new OrderService();
        List<Order> orderList = os.searchOrders(orderid, receiverName);
        request.setAttribute("orders", orderList);
        request.getRequestDispatcher("/admin/orders/list.jsp").forward(request, response);
    }

    /**
     * 支付订单页面
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void payOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderid = request.getParameter("orderid");
        String money = request.getParameter("money");
        request.setAttribute("orderid", orderid);
        request.setAttribute("money", money);
        request.getRequestDispatcher("/user/pay.jsp").forward(request, response);
    }

    /**
     * 根据id查询详情
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void viewOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderid = request.getParameter("orderid");
        OrderService os = new OrderService();
        Order order = os.findOrdersByOrderId(orderid);
        User user = (User) request.getSession().getAttribute("user");
        request.setAttribute("order", order);
        if ("admin".equals(user.getRole())) {
            request.getRequestDispatcher("/admin/orders/view.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/user/orderInfo.jsp").forward(request, response);
        }

    }

}
