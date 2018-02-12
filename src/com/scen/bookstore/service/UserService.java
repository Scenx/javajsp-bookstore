package com.scen.bookstore.service;

import com.scen.bookstore.dao.UserDao;
import com.scen.bookstore.domain.User;
import com.scen.bookstore.exception.UserException;
import com.scen.bookstore.util.SendJMail;

import java.sql.SQLException;

/**
 * @author Scen
 * @date 2017/11/20
 */
public class UserService {

    UserDao ud = new UserDao();

    /**
     * 用户注册
     * @param user
     * @throws UserException
     */
    public void regist(User user) throws UserException {
        try {
            //用户注册
            ud.addUser(user);

            String emailMsg = "注册成功，请<a href='http://localhost:8080/u?method=active&activeCode=" + user.getActiveCode() + "'>激活</a>后登录";
            SendJMail.sendMail(user.getEmail(), emailMsg);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserException("注册失败！");
        }
    }

    /**
     * 激活用户
     * @param activeCode
     * @throws UserException
     */
    public void activeUser(String activeCode) throws UserException {
        //根据激活码查找用户
        try {
            User user = ud.findUserByActiveCode(activeCode);
            if (user != null) {
                //激活用户
                ud.activeCode(activeCode);
                return;
            }
            throw new UserException("激活失败!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserException("激活失败!");
        }
    }

    /**
     * 登录
     * @param username
     * @param password
     * @return
     * @throws UserException
     */
    public User login(String username, String password) throws UserException {
        User user = null;
        try {
            user = ud.findUserByUserNameAndPassword(username, password);
            if (user == null) {
                throw new UserException("用户名或密码错误!");
            }
            if (user.getState() == 0) {
                throw new UserException("用户未激活!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserException("用户名或密码错误!");
        }
        return user;
    }

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     * @throws UserException
     */
    public User findUserById(String id) throws UserException {
        try {
            return ud.findUserById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserException("用户查找失败！");
        }
    }

    /**
     * 修改用户信息
     * @param user
     * @throws UserException
     */
    public void modifyUser(User user) throws UserException {
        try {
            ud.modifyUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserException("修改用户信息失败");
        }
    }

}
