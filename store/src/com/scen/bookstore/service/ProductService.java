package com.scen.bookstore.service;


import java.sql.SQLException;
import java.util.List;

import com.scen.bookstore.dao.ProductDao;

import com.scen.bookstore.domain.PageBean;
import com.scen.bookstore.domain.Product;

/**
 * @author Scen
 * @date 2017/11/21
 */
public class ProductService {
    /**
     * 创建一个Dao对象
     */
    ProductDao productDao = new ProductDao();

    /**
     * 添加图书
     *
     * @param product
     */
    public void addBook(Product product) {
        try {
            productDao.addBook(product);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据商品id查询商品信息
     * @param id
     * @return
     */
    public Product findProductById(String id) {
        try {
            return productDao.findProductById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改商品信息
     * @param product
     */
    public void updateBook(Product product) {
        try {
            productDao.updateBook(product);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据商品id删除商品
     * @param id
     */
    public void deleteBook(String id) {
        try {
            productDao.delBook(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量删除商品
     * @param ids
     */
    public void deleAllBooks(String[] ids) {
        try {
            productDao.deleAllBooks(ids);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 多条件查询商品
     * @param id
     * @param category
     * @param name
     * @param minprice
     * @param maxprice
     * @return
     */
    public List<Product> searchBooks(String id, String category, String name,
                                     String minprice, String maxprice) {
        try {
            return productDao.searchBooks(id, category, name, minprice, maxprice);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 分页查询
     *
     * @param currentPage
     * @param pageSize
     * @param category
     * @return
     */
    public PageBean findBooksPage(int currentPage, int pageSize, String category) {

        try {
            //得到总记录数
            int count = productDao.count(category);
            //求出总页数
            int totalPage = (int) Math.ceil(count * 1.0 / pageSize);
            List<Product> products = productDao.findBooks(currentPage, pageSize, category);

            //把5个变量封装到PageBean中，做为返回值
            PageBean pb = new PageBean();
            pb.setProducts(products);
            pb.setCount(count);
            pb.setCurrentPage(currentPage);
            pb.setPageSize(pageSize);
            pb.setTotalPage(totalPage);
            //在pageBean中添加属性，用于点击上一页或下一页时使用
            pb.setCategory(category);

            return pb;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
