package com.scen.bookstore.web.servlet;

import com.scen.bookstore.domain.User;
import com.scen.bookstore.exception.OrderException;
import com.scen.bookstore.exception.UserException;
import com.scen.bookstore.service.OrderService;
import com.scen.bookstore.service.UserService;
import com.scen.bookstore.util.PaymentUtil;
import org.apache.commons.beanutils.BeanUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author Scen
 * @date 2017/12/5
 */
@WebServlet(name = "UserServlet", urlPatterns = "/u")
public class UserServlet extends BaseServlet {
    /**
     * 登录
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserService us = new UserService();
        try {
            String path = "/index.jsp";
            User user = us.login(username, password);
            if ("admin".equals(user.getRole())) {
                path = "/admin/login/home.jsp";
            }
            request.getSession().setAttribute("user", user);
            request.getRequestDispatcher(path).forward(request, response);
        } catch (UserException e) {
            e.printStackTrace();
            request.setAttribute("user_msg", e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    /**
     * 注册
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //处理验证码
        String ckcode = request.getParameter("ckcode");
        String checkcode_session = (String) request.getSession().getAttribute("checkcode_session");
        if (!checkcode_session.equals(ckcode)) {
            //如果两个验证码不一致，跳回注册页面
            request.setAttribute("ckcode_msg", "验证码错误！");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        //获取表单数据
        User user = new User();
        try {
            BeanUtils.populate(user, request.getParameterMap());
            user.setActiveCode(UUID.randomUUID().toString());
            //调用业务逻辑
            UserService us = new UserService();
            us.regist(user);
            //分发转向

            request.getSession().setAttribute("user", user);
            request.getRequestDispatcher("registersuccess.jsp").forward(request, response);
        } catch (UserException e) {
            request.setAttribute("user_msg", e.getMessage());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 注销
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void logOut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //使session销毁
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }

    /**
     * 根据id查询用户信息
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void findUserById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

        UserService us = new UserService();
        try {
            User user = us.findUserById(id);
            request.setAttribute("u", user);
            request.getRequestDispatcher("/user/modifyuserinfo.jsp").forward(request, response);
        } catch (UserException e) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }

    /**
     * 激活用户
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取激活码
        String activeCode = request.getParameter("activeCode");

        UserService us = new UserService();
        try {
            us.activeUser(activeCode);
            request.getRequestDispatcher("/user/activesuccess.jsp").forward(request, response);
        } catch (UserException e) {
            e.printStackTrace();
            //向用户提示失败信息
            response.getWriter().write(e.getMessage());
        }
    }

    /**
     * 支付是否成功
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void callBack(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        // 获得回调所有数据
        String p1_MerId = request.getParameter("p1_MerId");
        String r0_Cmd = request.getParameter("r0_Cmd");
        String r1_Code = request.getParameter("r1_Code");
        String r2_TrxId = request.getParameter("r2_TrxId");
        String r3_Amt = request.getParameter("r3_Amt");
        String r4_Cur = request.getParameter("r4_Cur");
        String r5_Pid = request.getParameter("r5_Pid");
        String r6_Order = request.getParameter("r6_Order");
        String r7_Uid = request.getParameter("r7_Uid");
        String r8_MP = request.getParameter("r8_MP");
        String r9_BType = request.getParameter("r9_BType");
        String rb_BankId = request.getParameter("rb_BankId");
        String ro_BankOrderId = request.getParameter("ro_BankOrderId");
        String rp_PayDate = request.getParameter("rp_PayDate");
        String rq_CardNo = request.getParameter("rq_CardNo");
        String ru_Trxtime = request.getParameter("ru_Trxtime");
        // 身份校验 --- 判断是不是支付公司通知你
        String hmac = request.getParameter("hmac");
        boolean isOK = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
                r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
                r8_MP, r9_BType, "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl");
        if (!isOK) {
            out.print("支付数据有可能被篡改，请联系客服");
        } else {

            if ("1".equals(r1_Code)) {
                if ("2".equals(r9_BType)) {
                    out.print("success");
                }
                //修改订单状态
                OrderService os = new OrderService();
                try {
                    os.modifyOrderState(r6_Order);
                } catch (OrderException e) {
                    System.out.println(e.getMessage());
                }
                response.sendRedirect(request.getContextPath() + "/user/paysuccess.jsp");
            }
        }
    }

    /**
     * 集合中保存所有成语
     */

    private List<String> words = new ArrayList<String>();

    /**
     * 服务器装入时初始化，仅执行一次
     *
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        // 初始化阶段，读取new_words.txt
        // web工程中读取 文件，必须使用绝对磁盘路径
        String path = getServletContext().getRealPath("/WEB-INF/new_words.txt");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void checkImg(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 禁止缓存


        int width = 120;
        int height = 30;

        // 步骤一 绘制一张内存中图片
        BufferedImage bufferedImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        // 步骤二 图片绘制背景颜色 ---通过绘图对象
        // 得到画图对象 --- 画笔
        Graphics graphics = bufferedImage.getGraphics();
        // 绘制任何图形之前 都必须指定一个颜色
        graphics.setColor(getRandColor(200, 250));
        graphics.fillRect(0, 0, width, height);

        // 步骤三 绘制边框
        graphics.setColor(Color.WHITE);
        graphics.drawRect(0, 0, width - 1, height - 1);

        // 步骤四 四个随机数字
        Graphics2D graphics2d = (Graphics2D) graphics;
        // 设置输出字体
        graphics2d.setFont(new Font("宋体", Font.BOLD, 18));
        // 生成随机数
        Random random = new Random();
        int index = random.nextInt(words.size());
        // 获得成语
        String word = words.get(index);

        // 定义x坐标
        int x = 10;
        for (int i = 0; i < word.length(); i++) {
            // 随机颜色
            graphics2d.setColor(new Color(20 + random.nextInt(110), 20 + random
                    .nextInt(110), 20 + random.nextInt(110)));
            // 旋转 -30 --- 30度
            int jiaodu = random.nextInt(60) - 30;
            // 换算弧度
            double theta = jiaodu * Math.PI / 180;

            // 获得字母数字
            char c = word.charAt(i);

            // 将c 输出到图片
            graphics2d.rotate(theta, x, 20);
            graphics2d.drawString(String.valueOf(c), x, 20);
            graphics2d.rotate(-theta, x, 20);
            x += 30;
        }

        // 将验证码内容保存session
        request.getSession().setAttribute("checkcode_session", word);

        // 步骤五 绘制干扰线
        graphics.setColor(getRandColor(160, 200));
        int x1;
        int x2;
        int y1;
        int y2;
        for (int i = 0; i < 30; i++) {
            x1 = random.nextInt(width);
            x2 = random.nextInt(12);
            y1 = random.nextInt(height);
            y2 = random.nextInt(12);
            graphics.drawLine(x1, y1, x1 + x2, x2 + y2);
        }

        // 将上面图片输出到浏览器 ImageIO
        graphics.dispose();// 释放资源
        ImageIO.write(bufferedImage, "jpg", response.getOutputStream());

    }

    /**
     * 取其某一范围的color
     *
     * @param fc int 范围参数1
     * @param bc int 范围参数2
     * @return Color
     */
    private Color getRandColor(int fc, int bc) {
        // 取其随机颜色
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    /**
     * 修改用户数据
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void modifyUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //封装表单数据
        User user = new User();
        try {
            BeanUtils.populate(user, request.getParameterMap());

            UserService us = new UserService();
            us.modifyUser(user);
            response.sendRedirect(request.getContextPath() + "/user/modifyUserInfoSuccess.jsp");
        } catch (Exception e) {
            response.getWriter().write(e.getMessage());
        }
    }

    /**
     * 判断用户跳转前台还是后台
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void myAccount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从session中取出user对象
        User user = (User) request.getSession().getAttribute("user");
        //判断user是否为null
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        } else {
            //普通用户页面
            String path = "/user/myAccount.jsp";
            if ("admin".equals(user.getRole())) {
                //管理员页面
                path = "/admin/login/home.jsp";
            }
            request.getRequestDispatcher(path).forward(request, response);
        }
    }

    /**
     * 在线支付
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void payOnline(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取用户数据
        String orderid = request.getParameter("orderid");
        String money = request.getParameter("money");
        String pd_FrpId = request.getParameter("yh");
        String p0_Cmd = "Buy";
        String p1_MerId = "10001126856";
        String p2_Order = orderid;
        String p3_Amt = money;
        String p4_Cur = "CNY";
        String p5_Pid = "unknow";
        String p6_Pcat = "unknow";
        String p7_Pdesc = "unknow";
        String p8_Url = "http://localhost:8080/bookStore/u?method=callBack";
        String p9_SAF = "1";
        String pa_MP = "unknow";
        String pr_NeedResponse = "1";
        String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl");
        request.setAttribute("pd_FrpId", pd_FrpId);
        request.setAttribute("p0_Cmd", p0_Cmd);
        request.setAttribute("p1_MerId", p1_MerId);
        request.setAttribute("p2_Order", p2_Order);
        request.setAttribute("p3_Amt", p3_Amt);
        request.setAttribute("p4_Cur", p4_Cur);
        request.setAttribute("p5_Pid", p5_Pid);
        request.setAttribute("p6_Pcat", p6_Pcat);
        request.setAttribute("p7_Pdesc", p7_Pdesc);
        request.setAttribute("p8_Url", p8_Url);
        request.setAttribute("p9_SAF", p9_SAF);
        request.setAttribute("pa_MP", pa_MP);
        request.setAttribute("pr_NeedResponse", pr_NeedResponse);
        request.setAttribute("hmac", hmac);

        request.getRequestDispatcher("/user/confirm.jsp").forward(request, response);
    }
}
