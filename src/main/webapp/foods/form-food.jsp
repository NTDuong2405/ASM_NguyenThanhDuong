<%@ page import="entity.Categories" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String status = (String) request.getAttribute("status");
    ArrayList<Categories> categories = (ArrayList<Categories>) request.getAttribute("categories");
%>
<html>
<head>
    <title>Register</title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

</head>
<style>
    body{
        background-image: url("/Asset/images.jpg");

    }
    #abc{
        width: 100%;
        margin: 0 auto;
    }
    .logo img{
        width: 100%;
        position: relative;
        top: 20%
    }
    label{
        font-size: 14px;
        font-weight: bold;
    }
</style>
<body>
<div class="container">
    <h4 style="text-align: center"> Welcome To My Restaurant</h4>
    <div class="row">
        <div class="col-8">
            <div id="abc">
                <form method="post" action="/foods/form-food.jsp">
                    <%--        Tên món ăn--%>
                    <div class="form-group">
                        <label for="name">Tên món ăn </label>
                        <input type="text" class="form-control" id="name" placeholder="Nhập tên món ăn" name="name">
                    </div>
                    <%--    Danh mục--%>
                    <div class="form-group">
                        <label for="categoryId">Danh mục</label>
                        <select class="form-control" id="categoryId" name="categoryId">
                            <option value="1">Đồ nướng</option>
                            <option value="2">Món luộc</option>
                            <option value="3">Món chay</option>
                            <option value="4">Đồ uống</option>
                        </select>
                    </div>
                    <%--    Mô tả--%>
                    <div class="form-group">
                        <label for="description">Mô tả </label>
                        <textarea class="form-control" id="description" name="description" placeholder="Mô tả" rows="3"></textarea>
                    </div>
                    <div class="form-group">
                        <label for="thumbnail">Link ảnh món ăn</label>
                        <input type="input" class="form-control" id="thumbnail" placeholder="Nhập link ảnh" name="thumbnail">
                    </div>
                        <button type="button" id="upload_widget" class="btn btn-primary">Upload files</button>

                        <div class="form-group">
                        <label for="price">Giá</label>
                        <input type="number" class="form-control" id="price" placeholder="Nhập giá tiền" name="price">
                    </div>
                    <button type="submit" class="btn btn-primary">Tạo món ăn</button>
                </form>
            </div>
        </div>
        <div class="col-4 logo">
            <img src="/Asset/logo.jpg" alt="">
        </div>
    </div>
</div>
</body>
</html>


<script src="https://upload-widget.cloudinary.com/global/all.js" type="text/javascript"></script>

<script type="text/javascript">
    var myWidget = cloudinary.createUploadWidget({
            cloudName: 'ddjqq7pe9',
            uploadPreset: 'ml_default'
        }, (error, result) => {
            if (!error && result && result.event === "success") {
                console.log('Done! Here is the image info: ', result.info);
            }
        }
    )

    document.getElementById("upload_widget").addEventListener("click", function () {
        myWidget.open();
    }, false);
</script>
