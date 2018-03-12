package com.scen.bookstore.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;

import com.scen.bookstore.domain.Order;
import com.scen.bookstore.domain.OrderItem;
import com.scen.bookstore.util.ManagerThreadLocal;

/**
 * @author Scen
 * @date 2017/11/21
 */
public class OrderItemDao {
    /**
     * 添加订单项
     * @param order
     * @throws SQLException
     */
    public void addOrderItem(Order order) throws SQLException {
        //得到所有定单项的集合
        List<OrderItem> orderItems = order.getOrderItems();
        QueryRunner qr = new QueryRunner();
        Object[][] params = new Object[orderItems.size()][];

        for (int i = 0; i < params.length; i++) {
            //数组中的第一个参数代表主单id， 第二个参数：商品id 第三个参数 ：商品的购买数量
            params[i] = new Object[]{order.getId(), orderItems.get(i).getP().getId(), orderItems.get(i).getBuynum()};
        }
        qr.batch(ManagerThreadLocal.getConnection(), "INSERT INTO orderitem VALUES(?,?,?)", params);
    }
}
