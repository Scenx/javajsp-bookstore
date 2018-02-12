package com.scen.bookstore.dao;

import com.scen.bookstore.domain.User;
import com.scen.bookstore.util.C3P0Util;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;

/**
 * @author Scen
 * @date 2017/11/20
 */
public class UserDao {
    /**
     * 注册
     *
     * @param user
     * @throws SQLException
     */
    public void addUser(User user) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
        String sql = "INSERT INTO USER(username,PASSWORD,gender,email,telephone,introduce,activecode,state,registtime) "
                + "VALUES(?,?,?,?,?,?,?,?,?)";
        qr.update(sql, user.getUsername(), user.getPassword(),
                user.getGender(), user.getEmail(), user.getTelephone(),
                user.getIntroduce(), user.getActiveCode(), user.getState(),
                user.getRegistTime());

    }

    /**
     * 根据激活码查找用户
     */
    public User findUserByActiveCode(String activeCode) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
        return qr.query("select * from user where activecode=?",
                new BeanHandler<User>(User.class), activeCode);
    }

    /**
     * 修改用户激活状态
     *
     * @param activeCode
     * @throws SQLException
     */
    public void activeCode(String activeCode) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
        qr.update("update user set state=1 where activecode=?", activeCode);
    }

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     * @throws SQLException
     */
    public User findUserByUserNameAndPassword(String username, String password)
            throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
        return qr.query("select * from user where username=? and password=?",
                new BeanHandler<User>(User.class), username, password);
    }

    /**
     * 根据id查找用户
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public User findUserById(String id) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
        return qr.query("select * from user where id=?", new BeanHandler<User>(User.class), id);
    }

    /**
     * 修改用户信息
     *
     * @param user
     * @throws SQLException
     */
    public void modifyUser(User user) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
        qr.update("update user set password=? , gender=?, telephone=? where id=?", user.getPassword(), user.getGender(), user.getTelephone(), user.getId());

    }
}
