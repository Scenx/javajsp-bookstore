package com.scen.bookstore.util;


import com.sun.mail.util.MailSSLSocketFactory;

import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author Scen
 * @date 2017/11/20
 */
public class SendJMail {
    public static boolean sendMail(String email, String emailMsg) {
        // 邮件发送人的邮件地址
        String from = "862351901@qq.com";
        // 邮件接收人的邮件地址
        String to = email;
        //发件人的邮件帐户
        final String username = "862351901@qq.com";
        //发件人的邮件密码
        final String password = "orkidjidccqwbccg";


        //定义Properties对象,设置环境信息
        Properties props = System.getProperties();

        //设置邮件服务器的地址
        // 指定的smtp服务器
        props.setProperty("mail.smtp.host", "smtp.qq.com");
        props.setProperty("mail.smtp.auth", "true");
        //设置发送邮件使用的协议
        props.setProperty("mail.transport.protocol", "smtp");
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        sf.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);
        //创建Session对象,session对象表示整个邮件的环境信息
        Session session = Session.getInstance(props);
        //设置输出调试信息
        session.setDebug(true);
        try {
            //Message的实例对象表示一封电子邮件
            MimeMessage message = new MimeMessage(session);
            //设置发件人的地址
            message.setFrom(new InternetAddress(from));
            //设置主题
            message.setSubject("用户激活");
            //设置邮件的文本内容
            message.setContent(emailMsg, "text/html;charset=utf-8");
            //从session的环境中获取发送邮件的对象
            Transport transport = session.getTransport();
            //连接邮件服务器
            transport.connect("smtp.qq.com", 465, username, password);
            //设置收件人地址,并发送消息
            transport.sendMessage(message, new Address[]{new InternetAddress(to)});
            transport.close();
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
