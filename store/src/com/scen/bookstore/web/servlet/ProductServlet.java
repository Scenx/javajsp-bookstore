package com.scen.bookstore.web.servlet;

import com.scen.bookstore.domain.PageBean;
import com.scen.bookstore.domain.Product;
import com.scen.bookstore.domain.User;
import com.scen.bookstore.service.ProductService;
import com.scen.bookstore.util.UUIDUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Scen
 * @date 2017/12/5
 */
@WebServlet(name = "ProductServlet",urlPatterns = "/product")
public class ProductServlet extends BaseServlet {
    /**
     * 添加购物车
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void addCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        String id = request.getParameter("id");

        ProductService bs = new ProductService();

        Product b = bs.findProductById(id);

        //从session中的购物车取出 来
        HttpSession session = request.getSession();
        Map<Product, String> cart = (Map<Product, String>) session.getAttribute("cart");
        int num = 1;
        //如何是第一次访问，没有购物车对象，我们就创建 一个购物车对象
        if (cart == null) {
            cart = new HashMap<Product, String>();

        }
        //查看当前集合中是否存在b这本书,如果有就把数据取出来加1;
        if (cart.containsKey(b)) {
            num = Integer.parseInt(cart.get(b)) + 1;
        }
        //把图书放入购物车
        cart.put(b, num + "");

        //把cart对象放回到session作用域中
        session.setAttribute("cart", cart);

        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }

    /**
     * 添加商品
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void addProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //创建一个DiskFileItemFactory工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //创建一个ServletFileUpload核心对象
        ServletFileUpload sfu = new ServletFileUpload(factory);
        sfu.setHeaderEncoding("UTF-8");
        //解析request，返回所有表单项
        List<FileItem> fileItems = new ArrayList<FileItem>(0);
        Map<String, String[]> map = new HashMap<String, String[]>();
        try {
            fileItems = sfu.parseRequest(request);

            //迭代fileItems表单项
            for (FileItem fileItem : fileItems) {
                if (fileItem.isFormField()) {
                    String name = fileItem.getFieldName();
                    String value = fileItem.getString("UTF-8");
                    map.put(name, new String[]{value});
                    //普通表单项
                } else {
                    //文件表单项
                    InputStream inputStream = fileItem.getInputStream();
                    String filename = fileItem.getName();
                    String extension = FilenameUtils.getExtension(filename);
                    if (!("jsp".equals(extension) || "exe".equals(extension))) {
                        File storeDirectory = new File(this.getServletContext().getRealPath("/upload"));
                        if (!storeDirectory.exists()) {
                            storeDirectory.mkdirs();
                        }
                        if (filename != null) {
                            filename = FilenameUtils.getName(filename);
                        }
                        String childDirectory = makeChildDirectory(storeDirectory, filename);
                        filename = childDirectory + File.separator + filename;
                        fileItem.write(new File(storeDirectory, filename));
                        fileItem.delete();

                    }
                    map.put(fileItem.getFieldName(), new String[]{filename});


                }
            }
            Product product = new Product();
            BeanUtils.populate(product, map);
            product.setId(UUIDUtil.getUUID());
            //调用业务逻辑
            ProductService ps = new ProductService();
            ps.addBook(product);
            //分发转向
            findProductByManyCondition(request, response);
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加或者修改商品时打散的图片目录
     * @param storeDirectory
     * @param filename
     * @return
     */
    private String makeChildDirectory(File storeDirectory, String filename) {
        int hashcode = filename.hashCode();
        String code = Integer.toHexString(hashcode);
        String childDirectory = code.charAt(0) + File.separator + code.charAt(1);
        File file = new File(storeDirectory, childDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }
        return childDirectory;
    }

    /**
     * 修改购物车商品数量
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void changeNum(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String num = request.getParameter("num");
        //注：只能重写id的hashcode
        Product b = new Product();
        b.setId(id);

        HttpSession session = request.getSession();
        Map<Product, String> cart = (Map<Product, String>) session.getAttribute("cart");
        //如果商品数据为0，就删除对象
        if ("0".equals(num)) {
            cart.remove(b);
        }
        //判断如果找到与id相同的书，
        if (cart.containsKey(b)) {
            cart.put(b, num);
        }

        response.sendRedirect(request.getContextPath() + "/cart.jsp");
    }

    /**
     * 批量删除商品
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void delAllProducts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //得到所有id
        String[] ids = request.getParameterValues("ids");

        //调用删除业务
        ProductService ps = new ProductService();
        ps.deleAllBooks(ids);

        findProductByManyCondition(request,response);
    }

    /**
     * 根据id删除商品
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void delProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

        ProductService ps = new ProductService();
        ps.deleteBook(id);

        findProductByManyCondition(request,response);
    }

    /**
     * 编辑商品
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void editProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //创建一个DiskFileItemFactory工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //创建一个ServletFileUpload核心对象
        ServletFileUpload sfu = new ServletFileUpload(factory);
        sfu.setHeaderEncoding("UTF-8");
        //解析request，返回所有表单项
        List<FileItem> fileItems = new ArrayList<FileItem>(0);
        Map<String, String[]> map = new HashMap<String, String[]>();
        try {
            fileItems = sfu.parseRequest(request);

            //迭代fileItems表单项
            for (FileItem fileItem : fileItems) {
                if (fileItem.isFormField()) {
                    String name = fileItem.getFieldName();
                    String value = fileItem.getString("UTF-8");
                    map.put(name, new String[]{value});
                    //普通表单项
                } else {
                    //文件表单项
                    String filename = fileItem.getName();
                    if ("".equals(filename)) {
                        map.put("img_url", map.get("static_img_url"));
                        map.remove("static_img_url");
                    } else {
                        String extension = FilenameUtils.getExtension(filename);
                        if (!("jsp".equals(extension) || "exe".equals(extension))) {
                            File storeDirectory = new File(this.getServletContext().getRealPath("/upload"));
                            if (!storeDirectory.exists()) {
                                storeDirectory.mkdirs();
                            }

                            filename = FilenameUtils.getName(filename);
                            String childDirectory = makeChildDirectory(storeDirectory, filename);
                            filename = childDirectory + File.separator + filename;
                            fileItem.write(new File(storeDirectory, filename));
                            fileItem.delete();
                            map.remove("static_img_url");
                            map.put(fileItem.getFieldName(), new String[]{filename});
                        }
                    }

                }
            }
            Product product = new Product();
            BeanUtils.populate(product, map);
            //调用业务逻辑
            ProductService ps = new ProductService();
            ps.updateBook(product);
            //分发转向
            findProductByManyCondition(request,response);
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据商品id查询详细信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void findProductById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

        ProductService bs = new ProductService();
        Product book = bs.findProductById(id);

        request.setAttribute("book", book);
        User user = (User) request.getSession().getAttribute("user");
        if (user==null) {
            user = new User();
        }
        if ("admin".equals(user.getRole())) {
            request.getRequestDispatcher("/admin/products/edit.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/product_info.jsp").forward(request, response);
        }
    }

    /**
     * 多条件查询图书
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void findProductByManyCondition(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取表单数据
        String id = request.getParameter("id");
        String category = request.getParameter("category");
        String name = request.getParameter("name");
        String minprice = request.getParameter("minprice");
        String maxprice = request.getParameter("maxprice");
        if (id==null||category==null||name==null||minprice==null|maxprice==null) {
            id = "";
            category = "";
            name = "";
            minprice = "";
            maxprice = "";
        }
        //调用业务逻辑
        ProductService ps = new ProductService();
        List<Product> list = ps.searchBooks(id, category, name, minprice, maxprice);

        //分发转向
        request.setAttribute("products", list);
        request.getRequestDispatcher("/admin/products/list.jsp").forward(request, response);
    }

    /**
     * 分页
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void pagination(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //导航按钮的查询条件
        String category = request.getParameter("category");
        if (category == null) {
            category = "";
        }
        //初始化每页显示的记录数
        int pageSize = 4;
        //当前页
        int currentPage = 1;
        //从上一页或下一页得到的数据
        String currPage = request.getParameter("currentPage");
        if (currPage != null && !"".equals(currPage)) {
            //第一次访问资源时，currPage可能是null
            currentPage = Integer.parseInt(currPage);
        }

        ProductService bs = new ProductService();
        //分页查询，并返回PageBean对象
        PageBean pb = bs.findBooksPage(currentPage, pageSize, category);

        request.setAttribute("pb", pb);
        request.getRequestDispatcher("/product_list.jsp").forward(request, response);
    }

}
