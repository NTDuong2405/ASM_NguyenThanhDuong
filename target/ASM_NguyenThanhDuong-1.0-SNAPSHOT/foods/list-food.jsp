<%@ page import="entity.Food" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 5/18/2021
  Time: 10:12 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    ArrayList<Food> foods = (ArrayList<Food>) request.getAttribute("foods");
%>
<html>
<head>
    <title>List Student</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
    <div class="">
        <h4 class="text-center">Danh sách mòn ăn</h4>
        <a href="/food/create">Create new</a>
        <table class="table">
            <thead class="table-info">
            <tr  class="text-center">
                <th scope="col">Mã món ăn</th>
                <th scope="col">Tên món ăn</th>
                <th scope="col">Danh mục món ăn</th>
                <th scope="col">Mô tả</th>
                <th scope="col">Ảnh đại diện</th>
                <th scope="col">Giá</th>
                <th scope="col">Ngày bắt đầu bán</th>
                <th scope="col">Ngày sửa thông tin</th>
                <th scope="col">Trạng thái</th>
                <th scope="col">Thao tác</th>
            </tr>
            </thead>
            <tbody>
            <%
                for (int i = 0; i < foods.size(); i++) {
                    Food food = foods.get(i);
            %>
            <tr>
                <td scope="col" style="text-align: center"><%=food.getId()%>
                </td>
                <td scope="col" style="text-align: center"><%=food.getFoodName()%>
                </td>
                <td scope="col" style="text-align: center"><%=food.getCategoryId()%>
                </td>
                <td scope="col" style="text-align: center"><%=food.getDescription()%>
                </td>
                <td scope="col" style="text-align: center">
                    <div style="width: 100px; height: 100px; background-repeat: no-repeat; background-position: center; background-size: cover; background-image: url('<%=food.getThumbnail()%>')"></div>
                </td>
                <td scope="col" style="text-align: center"><%=food.getPrice()%>
                </td>
                <td scope="col" style="text-align: center"><%=food.toStatus(food.getStatus())%>
                </td>
                <td scope="col" style="text-align: center">
                    <div class="btn-group">
                        <form action="/food/edit" method="get" >
                            <input type="hidden" name="id" value="<%=food.getId()%>">
                            <button type="submit" class="btn btn-success">Sửa</button>
                        </form>
                        <form action="/food/delete" method="get" >
                            <input type="hidden" name="id" value="<%=food.getId()%>">
                            <button type="submit" class="btn btn-success">Sửa</button>
                        </form>
                        <a href="/food/edit?id=<%=food.getId()%>" class="btn btn-success">Sửa</a>
                        <a href="/food/delete?id=<%=food.getId()%>" class="btn btn-danger">Xóa</a>
                    </div>
                </td>
            </tr>
            <%}%>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
