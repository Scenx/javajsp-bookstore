package com.scen.bookstore.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.scen.bookstore.domain.Order;
import com.scen.bookstore.domain.OrderItem;
import com.scen.bookstore.domain.Product;
import com.scen.bookstore.util.C3P0Util;
import com.scen.bookstore.util.ManagerThreadLocal;

/**
 * @author Scen
 * @date 2017/11/21
 */
public class ProductDao {
    /**
     * 修改商品数量
     *
     * @param order
     * @throws SQLException
     */
    public void updateProductNum(Order order) throws SQLException {
        List<OrderItem> orderItems = order.getOrderItems();
        QueryRunner qr = new QueryRunner();

        Object[][] params = new Object[orderItems.size()][];
        for (int i = 0; i < params.length; i++) {
            params[i] = new Object[]{orderItems.get(i).getBuynum(), orderItems.get(i).getP().getId()};
        }
        qr.batch(ManagerThreadLocal.getConnection(), "UPDATE products SET pnum=pnum-? WHERE id=?", params);
    }

    /**
     * 添加图书信息
     *
     * @param product
     * @throws SQLException
     */
    public void addBook(Product product) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
        qr.update("INSERT INTO products VALUES(?,?,?,?,?,?,?)", product.getId(), product.getName(), product.getPrice(), product.getPnum(), product.getCategory(), product.getDescription(), product.getImg_url());
    }

    /**
     * 根据id查询图书
     * @param id
     * @return
     * @throws SQLException
     */
    public Product findProductById(String id) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
        return qr.query("select * from products where id=?", new BeanHandler<Product>(Product.class), id);
    }

    /**
     * 修改图书信息
     *
     * @param product
     * @throws SQLException
     */
    public void updateBook(Product product) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
        qr.update("update products set name=?,price=?,pnum=?,category=?,description=?,img_url=? where id=?",
                product.getName(), product.getPrice(), product.getPnum(), product.getCategory(), product.getDescription(), product.getImg_url(), product.getId());


    }

    /**
     * 根据id删除图书
     *
     * @param id
     * @throws SQLException
     */
    public void delBook(String id) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
        qr.update("delete from products where id=?", id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @throws SQLException
     */
    public void deleAllBooks(String[] ids) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
        Object[][] params = new Object[ids.length][];
        for (int i = 0; i < params.length; i++) {
            //循环给每个一维数组中的元素赋值，值是id
            params[i] = new Object[]{ids[i]};
        }
        qr.batch("delete from products where id=?", params);
    }

    /**
     * 多条件查询图书
     *
     * @param id
     * @param category
     * @param name
     * @param minprice
     * @param maxprice
     * @return
     * @throws SQLException
     */
    public List<Product> searchBooks(String id, String category, String name,
                                     String minprice, String maxprice) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
        String sql = "select * from products where 1=1";
        List list = new ArrayList();
        if (!"".equals(id.trim())) {
            //  不能在这写%   %'1002'%
            sql += " and id like ?";
            // '%1002%'
            list.add("%" + id.trim() + "%");
        }

        if (!"".equals(category.trim())) {
            sql += " and category=?";
            list.add(category.trim());
        }

        if (!"".equals(name.trim())) {
            sql += " and name like ?";
            list.add("%" + name.trim() + "%");
        }

        if (!"".equals(minprice.trim())) {
            sql += " and price>?";
            list.add(minprice.trim());
        }
        if (!"".equals(maxprice.trim())) {
            sql += " and price< ?";
            list.add(maxprice.trim());
        }

        return qr.query(sql, new BeanListHandler<Product>(Product.class), list.toArray());
    }

    /**
     * 得到总记录数
     *
     * @return
     * @throws SQLException
     */
    public int count(String category) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
        String sql = "select count(*) from products";
        //如果category不是空，就把条件加上
        if (!"".equals(category)) {
            sql += " where category='" + category + "'";
        }
        long l = (Long) qr.query(sql, new ScalarHandler(1));
        return (int) l;
    }

    /**
     * 查找分页数据
     *
     * @param currentPage
     * @param pageSize
     * @return
     * @throws SQLException
     */
    public List<Product> findBooks(int currentPage, int pageSize, String category) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());

        String sql = "select * from products where 1=1";
        List list = new ArrayList();
        if (!"".equals(category)) {
            sql += " and category=?";
            list.add(category);
        }
        sql += " limit ?,?";

        // select * from products where 1=1 and category=? limit ?,?;
        list.add((currentPage - 1) * pageSize);
        list.add(pageSize);

        return qr.query(sql, new BeanListHandler<Product>(Product.class), list.toArray());
    }
}
