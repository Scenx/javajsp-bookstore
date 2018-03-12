<%@ page language="java" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<HTML>
<HEAD>
    <meta http-equiv="Content-Language" content="zh-cn">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="${pageContext.request.contextPath}/admin/css/Style.css"
          rel="stylesheet" type="text/css"/>
    <script language="javascript"
            src="${pageContext.request.contextPath}/admin/js/public.js"></script>
    <script>
        function addProduct() {
            window.location.href = "${pageContext.request.contextPath}/admin/products/add.jsp";
        }

        //提示用户是否删除
        function delBook(id, name) {
            if (confirm("是否确定删除" + name + "？")) {
                location.href = "${pageContext.request.contextPath}/product?method=delProduct&id=" + id;
            }
        }

        //全选
        function checkAll() {
            //得到ckAll元素，并得到他的选中状态
            var flag = document.getElementById("ckAll").checked;
            //得到所有ids复选框元素
            var ids = document.getElementsByName("ids");
            //循环给每一个复选框赋值
            for (var i = 0; i < ids.length; i++) {
                ids[i].checked = flag;
            }
        }

        //批量删除
        function delAllBooks() {

            var form2 = document.forms[1];
            var ids = document.getElementsByName("ids");
            var num = 0;
            for (var i = 0; i < ids.length; i++) {
                if (ids[i].checked) {
                    num++;
                }
            }
            if (confirm("是否确定删除这" + num + "条图书？")) {
                form2.submit();
            }

        }

    </script>
</HEAD>
<body>
<br>

<table cellSpacing="1" cellPadding="0" width="100%" align="center"
       bgColor="#f5fafe" border="0">
    <TBODY>
    <tr>
        <td class="ta_01" align="center" bgColor="#afd1f3"><strong>查
            询 条 件</strong>
        </td>
    </tr>
    <tr>
        <td>
            <form id="Form1" name="Form1"
                  action="${pageContext.request.contextPath}/product?method=findProductByManyCondition"
                  method="post">
                <table cellpadding="0" cellspacing="0" border="0" width="100%">
                    <tr>
                        <td height="22" align="center" bgColor="#f5fafe" class="ta_01">
                            商品编号
                        </td>
                        <td class="ta_01" bgColor="#ffffff"><input type="text"
                                                                   name="id" size="15" value="" id="Form1_userName"
                                                                   class="bg"/>
                        </td>
                        <td height="22" align="center" bgColor="#f5fafe" class="ta_01">
                            类别：
                        </td>
                        <td class="ta_01" bgColor="#ffffff"><select name="category"
                                                                    id="category">
                            <option value="" selected="selected">--选择商品类加--</option>
                            <option value="文学">文学</option>
                            <option value="生活">生活</option>
                            <option value="计算机">计算机</option>
                            <option value="外语">外语</option>
                            <option value="经营">经营</option>
                            <option value="励志">励志</option>
                            <option value="社科">社科</option>
                            <option value="学术">学术</option>
                            <option value="少儿">少儿</option>
                            <option value="艺术">艺术</option>
                            <option value="原版">原版</option>
                            <option value="科技">科技</option>
                            <option value="考试">考试</option>
                            <option value="生活百科">生活百科</option>
                        </select></td>
                    </tr>

                    <tr>
                        <td height="22" align="center" bgColor="#f5fafe" class="ta_01">
                            商品名称：
                        </td>
                        <td class="ta_01" bgColor="#ffffff"><input type="text"
                                                                   name="name" size="15" value="" id="Form1_userName"
                                                                   class="bg"/>
                        </td>
                        <td height="22" align="center" bgColor="#f5fafe" class="ta_01">
                            价格区间(元)：
                        </td>
                        <td class="ta_01" bgColor="#ffffff"><input type="text"
                                                                   name="minprice" size="10" value=""/>- <input
                                type="text"
                                name="maxprice" size="10" value=""/></td>
                    </tr>

                    <tr>
                        <td width="100" height="22" align="center" bgColor="#f5fafe"
                            class="ta_01"></td>
                        <td class="ta_01" bgColor="#ffffff"><font face="宋体"
                                                                  color="red"> &nbsp;</font>
                        </td>
                        <td align="right" bgColor="#ffffff" class="ta_01"><br>
                            <br></td>
                        <td align="right" bgColor="#ffffff" class="ta_01">
                            <button type="submit" id="search" name="search"
                                    value="&#26597;&#35810;" class="button_view">
                                &#26597;&#35810;
                            </button> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input
                                type="reset" name="reset" value="&#37325;&#32622;"
                                class="button_view"/>
                        </td>
                    </tr>
                </table>
            </form>
        </td>

    </tr>
    <tr>
        <td class="ta_01" align="center" bgColor="#afd1f3"><strong>商品列表</strong>
        </TD>
    </tr>
    <tr>
        <td class="ta_01" align="right">
            <button type="button" id="delAll" name="add" value="批量删除"
                    class="button_add" onclick="delAllBooks()">批量删除
            </button>
            <button type="button" id="add" name="add" value="&#28155;&#21152;"
                    class="button_add" onclick="addProduct()">&#28155;&#21152;
            </button>
        </td>
    </tr>
    <tr>
        <td class="ta_01" align="center" bgColor="#f5fafe">
            <form id="Form2" name="Form2" action="${pageContext.request.contextPath}/product?method=delAllProducts" method="post">
                <table cellspacing="0" cellpadding="1" rules="all"
                       bordercolor="gray" border="1" id="DataGrid1"
                       style="BORDER-RIGHT: gray 1px solid; BORDER-TOP: gray 1px solid; BORDER-LEFT: gray 1px solid; WIDTH: 100%; WORD-BREAK: break-all; BORDER-BOTTOM: gray 1px solid; BORDER-COLLAPSE: collapse; BACKGROUND-COLOR: #f5fafe; WORD-WRAP: break-word">
                    <tr
                            style="FONT-WEIGHT: bold; FONT-SIZE: 12pt; HEIGHT: 25px; BACKGROUND-COLOR: #afd1f3">
                        <td align="center" width="10%"><input type="checkbox" id="ckAll" onclick="checkAll() "/>全选</td>
                        <td align="center" width="24%">商品编号</td>
                        <td align="center" width="18%">商品名称</td>
                        <td align="center" width="9%">商品价格</td>
                        <td align="center" width="9%">商品数量</td>
                        <td width="8%" align="center">商品类别</td>
                        <td width="8%" align="center">编辑</td>

                        <td width="8%" align="center">删除</td>
                    </tr>
                    <c:forEach items="${products}" var="p">


                        <tr onmouseover="this.style.backgroundColor = 'white'"
                            onmouseout="this.style.backgroundColor = '#F5FAFE';">
                            <td style="CURSOR: hand; HEIGHT: 22px" align="center"
                                width="23"><input type="checkbox" name="ids" value="${p.id}"/>
                            </td>
                            <td style="CURSOR: hand; HEIGHT: 22px" align="center"
                                width="23">${p.id}
                            </td>
                            <td style="CURSOR: hand; HEIGHT: 22px" align="center"
                                width="18%">${p.name}
                            </td>
                            <td style="CURSOR: hand; HEIGHT: 22px" align="center"
                                width="8%">${p.price}
                            </td>
                            <td style="CURSOR: hand; HEIGHT: 22px" align="center"
                                width="8%">${p.pnum}
                            </td>
                            <td style="CURSOR: hand; HEIGHT: 22px" align="center">${p.category}</td>
                            <td align="center" style="HEIGHT: 22px" width="7%"><a
                                    href="${pageContext.request.contextPath}/product?method=findProductById&id=${p.id}">
                                <img
                                        src="${pageContext.request.contextPath}/admin/images/i_edit.gif"
                                        border="0" style="CURSOR: hand"> </a>
                            </td>

                            <td align="center" style="HEIGHT: 22px" width="7%"><a
                                    href="javascript:delBook('${p.id}','${p.name}')">
                                <img
                                        src="${pageContext.request.contextPath}/admin/images/i_del.gif"
                                        width="16" height="16" border="0" style="CURSOR: hand">
                            </a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </form>
        </td>
    </tr>
    </TBODY>
</table>

</body>
</HTML>

