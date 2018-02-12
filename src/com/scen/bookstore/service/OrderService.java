package com.scen.bookstore.service;

import java.sql.SQLException;
import java.util.List;

import com.scen.bookstore.dao.OrderDao;
import com.scen.bookstore.dao.OrderItemDao;
import com.scen.bookstore.dao.ProductDao;
import com.scen.bookstore.domain.Order;
import com.scen.bookstore.exception.OrderException;
import com.scen.bookstore.util.ManagerThreadLocal;

/**
 * @author Scen
 * @date 2017/11/21
 */
public class OrderService {
    OrderDao orderDao = new OrderDao();
    OrderItemDao orderItemDao = new OrderItemDao();
    ProductDao productDao = new ProductDao();

    /**
     * 添加订单
     * @param order
     */
    public void addOrder(Order order) {
        try {
            ManagerThreadLocal.startTransacation();
            orderDao.addOrder(order);
            orderItemDao.addOrderItem(order);
            productDao.updateProductNum(order);

            ManagerThreadLocal.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            ManagerThreadLocal.rollback();
        }
    }

    /**
     * 根据用户id查询订单
     * @param id
     * @return
     */
    public List<Order> findOrdersByUserId(int id) {
        try {
            return orderDao.findOrders(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据订单号查询订单详细信息
     * @param orderid
     * @return
     */
    public Order findOrdersByOrderId(String orderid) {
        try {
            return orderDao.findOrdersByOrderId(orderid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改订单状态
     * @param orderid
     * @throws OrderException
     */
    public void modifyOrderState(String orderid) throws OrderException {
        try {
            orderDao.modifyOrderState(orderid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new OrderException("修改失败");
        }
    }


    /**
     * 查询所有订单 包含多条件
     * @param orderid
     * @param receiverName
     * @return
     */
    public List<Order> searchOrders(String orderid, String receiverName) {
        try {
            return orderDao.searchOrders(orderid, receiverName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据id删除订单
     * @param orderid
     */
    public void deleteOrder(String orderid) {
        try {
            orderDao.deleteOrder(orderid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
