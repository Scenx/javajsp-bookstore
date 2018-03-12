package com.scen.bookstore.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.scen.bookstore.domain.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.scen.bookstore.domain.Order;
import com.scen.bookstore.domain.OrderItem;
import com.scen.bookstore.domain.Product;
import com.scen.bookstore.util.C3P0Util;
import com.scen.bookstore.util.ManagerThreadLocal;

/**
 * @author Scen
 * @date 2017/11/21
 */
public class OrderDao {
    /**
     * 添加定单
     *
     * @param order
     * @throws SQLException
     */
    public void addOrder(Order order) throws SQLException {
        QueryRunner qr = new QueryRunner();
        qr.update(ManagerThreadLocal.getConnection(),
                "INSERT INTO orders VALUES(?,?,?,?,?,?,?,?)", order.getId(),
                order.getMoney(), order.getReceiverAddress(), order
                        .getReceiverName(), order.getReceiverPhone(), order
                        .getPaystate(), order.getOrdertime(), order.getUser()
                        .getId());
    }

    /**
     * 根据用户id查询所有定单
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public List<Order> findOrders(int id) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
        return qr.query("select * from orders where user_id=?",
                new BeanListHandler<Order>(Order.class), id);
    }

    /**
     * 查询指定用户的定单信息
     *
     * @param orderid
     * @return
     * @throws SQLException
     */
    public Order findOrdersByOrderId(String orderid) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
        //得到一个定单
        Order order = qr.query("select * from orders where id=?", new BeanHandler<Order>(Order.class), orderid);
        //得到当前定单中的所有定单项

        /**
         * 商品编号
         * 购买数量
         * 商品名称
         * 商品价格
         * 商品类别
         * 商品描述
         * 商品图片
         */
        List<OrderItem> orderItems = qr.query("SELECT * FROM orderitem o,products p WHERE p.id=o.product_id AND order_id=?", new ResultSetHandler<List<OrderItem>>() {

            @Override
            public List<OrderItem> handle(ResultSet rs) throws SQLException {
                List<OrderItem> orderItems = new ArrayList<OrderItem>();
                while (rs.next()) {

                    //封装OrderItem对象
                    OrderItem oi = new OrderItem();
                    //购买数量
                    oi.setBuynum(rs.getInt("buynum"));
                    //封装Product对象
                    Product p = new Product();
                    //商品名称
                    p.setName(rs.getString("name"));
                    //商品价格
                    p.setPrice(rs.getDouble("price"));
                    //商品编号
                    p.setId(rs.getString("id"));
                    //商品类别
                    p.setCategory(rs.getString("category"));
                    //商品描述
                    p.setDescription(rs.getString("description"));
                    //商品图片
                    p.setImg_url(rs.getString("img_url"));
                    //把每个p对象封装到OrderItem对象中
                    oi.setP(p);
                    //把每个OrderItem对象封装到集合中
                    orderItems.add(oi);
                }
                return orderItems;
            }

        }, orderid);
        //把所有的定单项orderItems添加到主单对象Order中
        order.setOrderItems(orderItems);
        //查询用户信息
        User u = qr.query("SELECT u.id,username,gender,email,telephone,introduce,registTime FROM user u,orders o WHERE o.user_id=u.id AND o.id=?", new BeanHandler<User>(User.class),orderid);
        //把用户信息放到order中
        order.setUser(u);
        return order;
    }

    /**
     * 修改订单支付状态
     *
     * @param orderid
     * @throws SQLException
     */
    public void modifyOrderState(String orderid) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
        qr.update("update orders set paystate=1 where id=?", orderid);
    }

    /**
     * 多条件查询订单
     * @param orderid
     * @param receiverName
     * @return
     */
    public List<Order> searchOrders(String orderid, String receiverName) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
        String sql = "SELECT * FROM orders o,user u WHERE o.user_id=u.id and 1=1";
        List list = new ArrayList();
        if (!"".equals(orderid.trim())) {
            sql += " and o.id like ?";
            list.add("%" + orderid.trim() + "%");
        }
        if (!"".equals(receiverName.trim())) {
            sql += " and receiverName=?";
            list.add(receiverName.trim());
        }

        return qr.query(sql, new ResultSetHandler<List<Order>>() {
            @Override
            public List<Order> handle(ResultSet rs) throws SQLException {
                List<Order> orders = new ArrayList<Order>();
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getString("id"));
                    order.setMoney(rs.getDouble("money"));
                    order.setReceiverAddress(rs.getString("receiverAddress"));
                    order.setReceiverName(rs.getString("receiverName"));
                    order.setReceiverPhone(rs.getString("receiverPhone"));
                    order.setPaystate(rs.getInt("paystate"));
                    order.setOrdertime(rs.getDate("ordertime"));
                    User u = new User();
                    u.setId(Integer.parseInt(rs.getString("user_id")));
                    u.setUsername(rs.getString("username"));
                    u.setGender(rs.getString("gender"));
                    u.setEmail(rs.getString("email"));
                    u.setTelephone(rs.getString("telephone"));
                    u.setIntroduce(rs.getString("introduce"));
                    order.setUser(u);
                    orders.add(order);
                }
                return orders;
            }
        },list.toArray());
    }

    /**
     * 根据id删除订单
     * @param orderid
     * @throws SQLException
     */
    public void deleteOrder(String orderid) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
        qr.update("DELETE FROM orders WHERE id=?", orderid);
    }
}

