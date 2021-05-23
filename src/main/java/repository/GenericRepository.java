package repository;

import dannotation.Column;
import dannotation.Entity;
import dannotation.Id;
import helper.ConnectionHelper;
import helper.SQLConstant;

import java.lang.reflect.Field;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GenericRepository<T> {
    private Class<T> clazz;

    public GenericRepository(Class<T> clazz) {
        if (!clazz.isAnnotationPresent(Entity.class)) {
            System.err.printf("Class %s không được đăng ký làm việc với database.", clazz.getSimpleName());
            return;
        }
        this.clazz = clazz;
    }

    // save thong tin
    public boolean save(T obj) {
        try {
            // Lấy ra giá trị của annotation @Entity vì cần những thông tin liên quan đến tableName.
            Entity entityInfor = clazz.getAnnotation(Entity.class);
            // Build lên câu query string.
            StringBuilder strQuery = new StringBuilder();
            // Build chuỗi chứa giá trị các trường tương ứng.
            StringBuilder fieldValues = new StringBuilder();
            fieldValues.append(SQLConstant.OPEN_PARENTHESES);
            // Xây dựng câu lệnh insert theo tên bảng, theo tên các field cùa đối tượng truyền vào.
            strQuery.append(SQLConstant.INSERT_INTO); // insert into
            strQuery.append(SQLConstant.SPACE); //
            strQuery.append(entityInfor.tableName()); // giangvien
            strQuery.append(SQLConstant.SPACE); //
            strQuery.append(SQLConstant.OPEN_PARENTHESES); // (
            for (Field field : clazz.getDeclaredFields()) {
                // check xem trường có phải là @Column không.
                if (!field.isAnnotationPresent(Column.class)) {
                    // bỏ qua trong trường hợp không được đánh là @Column.
                    continue;
                }
                // cần set bằng true để có thể set, get giá trị của field trong một object nào đó.
                field.setAccessible(true);
                // lấy thông tin column để check tên trường, kiểu giá trị của trường.
                // Không lấy danh sách column theo tên field mà lấy theo annotation đặt tại field đó.
                Column columnInfor = field.getAnnotation(Column.class);
                // check xem trường có phải là id không.
                if (field.isAnnotationPresent(Id.class)) {
                    // lấy thông tin id.
                    Id idInfor = field.getAnnotation(Id.class);
                    if (idInfor.autoIncrement()) {
                        // trường hợp đây là trường tự tăng, thì next sang trường tiếp theo.
                        continue;
                    }
                }
                strQuery.append(columnInfor.columnName()); // nối tên trường.
                strQuery.append(SQLConstant.COMMON); //,
                strQuery.append(SQLConstant.SPACE); //
                // nhanh trí, xử lý luôn phần value, tránh sử dụng 2 vòng lặp.
                // check kiểu của trường, nếu là string thì thêm dấu '
                if (field.getType().getSimpleName().equals(String.class.getSimpleName()) ||
                        field.getType().getSimpleName().equals(Date.class.getSimpleName())) {
                    fieldValues.append(SQLConstant.QUOTE);
                }
                // lấy ra thông tin giá trị của trường đó tại obj truyền vào.
                fieldValues.append(field.get(obj)); // field.setAccessible(true);
                // check kiểu của trường, nếu là string thì thêm dấu '
                if (field.getType().getSimpleName().equals(String.class.getSimpleName()) ||
                        field.getType().getSimpleName().equals(Date.class.getSimpleName())) {
                    fieldValues.append(SQLConstant.QUOTE);
                }
                fieldValues.append(SQLConstant.COMMON); //,
                fieldValues.append(SQLConstant.SPACE); //
            }
            strQuery.setLength(strQuery.length() - 2); // trường hợp là field cuối cùng thì bỏ dấu , và khoảng trắng đi.
            fieldValues.setLength(fieldValues.length() - 2);
            strQuery.append(SQLConstant.CLOSE_PARENTHESES); // )
            fieldValues.append(SQLConstant.CLOSE_PARENTHESES); // )
            strQuery.append(SQLConstant.SPACE);
            strQuery.append(SQLConstant.VALUES); // values
            strQuery.append(SQLConstant.SPACE);
            strQuery.append(fieldValues); // nối giá trị các trường vào.
            System.out.println("lệnh insert \n");
            System.out.println(strQuery.toString());
            return ConnectionHelper.getConnection().createStatement().execute(strQuery.toString());
        } catch (IllegalAccessException | SQLException e) {
            System.err.printf("Có lỗi xảy ra trong quá trình làm việc với database. Error %s.\n", e.getMessage());
        }
        return true;
    }

    // find All
    public ArrayList<T> findAll() {
        ArrayList<T> result = new ArrayList<>(); // khởi tạo một danh sách rỗng.
        Entity entityInfor = clazz.getAnnotation(Entity.class);
        StringBuilder stringQuery = new StringBuilder();
        stringQuery.append(SQLConstant.SELECT_ASTERISK); // select *
        stringQuery.append(SQLConstant.SPACE);
        stringQuery.append(SQLConstant.FROM); // from
        stringQuery.append(SQLConstant.SPACE);
        stringQuery.append(entityInfor.tableName()); // tableName
        stringQuery.append(SQLConstant.SPACE);
        stringQuery.append(SQLConstant.ORDER_BY);
        stringQuery.append(SQLConstant.SPACE);
        for (Field field1 : clazz.getDeclaredFields()) {
            // check xem trường có phải là @Column không.
            if (!field1.isAnnotationPresent(Column.class)) {
                // bỏ qua trong trường hợp không được đánh là @Column.
                continue;
            }
            // cần set bằng true để có thể set, get giá trị của field trong một object nào đó.
            field1.setAccessible(true);
            Column columnInfor = field1.getAnnotation(Column.class);
            if (columnInfor.columnName().equals("ngayCapNhat")) {
                // lấy thông tin id.
                stringQuery.append(columnInfor.columnName()); // nối tên trường.
                stringQuery.append(SQLConstant.SPACE); //
                stringQuery.append(SQLConstant.DESC); //
            }
        }
        stringQuery.append(SQLConstant.SPACE);
        stringQuery.append(SQLConstant.LIMIT);
        stringQuery.append(SQLConstant.SPACE);
        stringQuery.append(SQLConstant.MARK_QUESTION);
        stringQuery.append(SQLConstant.COMMON);
        stringQuery.append(SQLConstant.MARK_QUESTION);
        System.out.println(stringQuery.toString());
        try {
            PreparedStatement preparedStatement = ConnectionHelper.getConnection().prepareStatement(stringQuery.toString());
            // thực thi câu lệnh select * from.
            // trả về ResultSet (nó thêm thằng con trỏ)
            ResultSet resultSet = preparedStatement.executeQuery();
            Field[] fields = clazz.getDeclaredFields(); //
            while (resultSet.next()) { // trỏ đến các bản ghi cho đến khi trả về false.
                T obj = clazz.newInstance(); // khởi tạo ra đối tượng cụ thể của class T.
                for (Field field : fields) {
                    // check nếu không là @Column
                    if (!field.isAnnotationPresent(Column.class)) {
                        continue;
                    }
                    field.setAccessible(true);
                    // lấy thông tin column để check tên trường, kiểu giá trị của trường.
                    Column columnInfor = field.getAnnotation(Column.class);
                    // tuỳ thuộc vào kiểu dữ liệu của trường, lấy giá trị ra theo các hàm khác nhau.
                    // phải bổ sung các kiểu dữ liệu cần thiết.
                    switch (field.getType().getSimpleName()) {
                        case SQLConstant.PRIMITIVE_INT:
                            // set giá trị của trường đó cho đối tượng mới tạo ở trên.
                            field.set(obj, resultSet.getInt(columnInfor.columnName()));
                            break;
                        case SQLConstant.PRIMITIVE_STRING:
                            field.set(obj, resultSet.getString(columnInfor.columnName()));
                            break;
                        case SQLConstant.PRIMITIVE_DOUBLE:
                            field.set(obj, resultSet.getDouble(columnInfor.columnName()));
                            break;
                        case SQLConstant.PRIMITIVE_DATE:
                            field.set(obj, resultSet.getDate(columnInfor.columnName()));
                            break;
                    }
                }
                // đối tượng obj kiểu T đã có đầy đủ giá trị.
                // add vào trong danh sách trả về.
                System.out.println(stringQuery.toString());
                result.add(obj);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            System.err.printf("Có lỗi xảy ra trong quá trình làm việc với database. Error %s.\n", e.getMessage());
        }
        return result;
    }

    // get count
    public int getCount() {
        ArrayList<T> list = new ArrayList();
        String stringQuery = "SELECT count(maSinhVien) FROM sinhvien";
        int count = 0;
        try {
            PreparedStatement preparedStatement = ConnectionHelper.getConnection().prepareStatement(stringQuery.toString());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    //find by Id
    public T findById(String id) {
        Entity entity = clazz.getAnnotation(Entity.class);
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder fieldValues = new StringBuilder();
        stringBuilder.append(SQLConstant.SELECT_ASTERISK);
        stringBuilder.append(SQLConstant.SPACE);
        stringBuilder.append(SQLConstant.FROM);
        stringBuilder.append(SQLConstant.SPACE);
        stringBuilder.append(entity.tableName());
        stringBuilder.append(SQLConstant.SPACE);
        stringBuilder.append(SQLConstant.WHERE);
        stringBuilder.append(SQLConstant.SPACE);
        for (Field field : clazz.getDeclaredFields()) {
            // check xem trường có phải là @Column không.
            if (!field.isAnnotationPresent(Column.class)) {
                // bỏ qua trong trường hợp không được đánh là @Column.
                continue;
            }
            // cần set bằng true để có thể set, get giá trị của field trong một object nào đó.
            field.setAccessible(true);

            Column columnInfor = field.getAnnotation(Column.class);
            if (field.isAnnotationPresent(Id.class)) {
                // lấy thông tin id.
                stringBuilder.append(columnInfor.columnName()); // nối tên trường.
                stringBuilder.append(SQLConstant.SPACE); //
                stringBuilder.append(SQLConstant.EQUAL); //
                stringBuilder.append(SQLConstant.SPACE); //
                // nhanh trí, xử lý luôn phần value, tránh sử dụng 2 vòng lặp.
                // check kiểu của trường, nếu là string thì thêm dấu '
                if (field.getType().getSimpleName().equals(String.class.getSimpleName())) {
                    fieldValues.append(SQLConstant.QUOTE);
                }
                // lấy ra thông tin giá trị của trường đó tại obj truyền vào.
                fieldValues.append(id); // field.setAccessible(true);
                // check kiểu của trường, nếu là string thì thêm dấu '
                if (field.getType().getSimpleName().equals(String.class.getSimpleName())) {
                    fieldValues.append(SQLConstant.QUOTE);
                }
                fieldValues.append(SQLConstant.SPACE); //
                stringBuilder.append(fieldValues); //
            }
        }
        try {
            PreparedStatement preparedStatement = ConnectionHelper.getConnection().prepareStatement(stringBuilder.toString());
            // thực thi câu lệnh select * from.
            // trả về ResultSet (nó thêm thằng con trỏ)
            ResultSet resultSet = preparedStatement.executeQuery();
            Field[] fields = clazz.getDeclaredFields(); //
            while (resultSet.next()) { // trỏ đến các bản ghi cho đến khi trả về false.
                T obj = clazz.newInstance(); // khởi tạo ra đối tượng cụ thể của class T.
                for (Field field : fields) {
                    // check nếu không là @Column
                    if (!field.isAnnotationPresent(Column.class)) {
                        continue;
                    }
                    field.setAccessible(true);
                    // lấy thông tin column để check tên trường, kiểu giá trị của trường.
                    Column columnInfor = field.getAnnotation(Column.class);
                    // tuỳ thuộc vào kiểu dữ liệu của trường, lấy giá trị ra theo các hàm khác nhau.
                    // phải bổ sung các kiểu dữ liệu cần thiết.
                    switch (field.getType().getSimpleName()) {
                        case SQLConstant.PRIMITIVE_INT:
                            // set giá trị của trường đó cho đối tượng mới tạo ở trên.
                            field.set(obj, resultSet.getInt(columnInfor.columnName()));
                            break;
                        case SQLConstant.PRIMITIVE_STRING:
                            field.set(obj, resultSet.getString(columnInfor.columnName()));
                            break;
                        case SQLConstant.PRIMITIVE_DOUBLE:
                            field.set(obj, resultSet.getDouble(columnInfor.columnName()));
                            break;
                        case SQLConstant.PRIMITIVE_DATE:
                            field.set(obj, resultSet.getDate(columnInfor.columnName()));
                            break;
                    }
                }
                // đối tượng obj kiểu T đã có đầy đủ giá trị.
                // add vào trong danh sách trả về.

                return obj;
            }
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            System.err.printf("Có lỗi xảy ra trong quá trình làm việc với database. Error %s.\n", e.getMessage());
        }
        System.out.println(stringBuilder.toString());
        return null;
    }

    //update
    public T update(T obj) {
        //update {table_name} SET column1 = value 1, column2 = value 2 where id = {id}
        //not allow to update id
        try {

            String tableName = clazz.getAnnotation(Entity.class).tableName();
            StringBuilder stringCmd = new StringBuilder();
            stringCmd.append(SQLConstant.UPDATE);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(tableName);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.SET);
            stringCmd.append(SQLConstant.SPACE);
            Field[] fields = clazz.getDeclaredFields();
            //id information
            String idName = "";
            String idValue = "";
            String idType = "";
            for (Field field : fields) {
                if (!field.isAnnotationPresent(Column.class)) {
                    continue;
                }

                field.setAccessible(true);
                Column columnInformation = field.getDeclaredAnnotation(Column.class);
                String columnName = columnInformation.columnName();
                String columnType = columnInformation.columnType();
                Object value = field.get(obj);
                if (field.isAnnotationPresent(Id.class)) {
                    //dont update id
                    //but get id information
                    idName = columnName;
                    idValue = value.toString();
                    idType = columnType;
                    continue;
                }
                stringCmd.append(columnName);
                stringCmd.append(SQLConstant.SPACE);
                stringCmd.append(SQLConstant.EQUAL);
                stringCmd.append(SQLConstant.SPACE);
                if (!columnType.equals(SQLConstant.PRIMITIVE_INT)) {
                    stringCmd.append(SQLConstant.QUOTE);
                }
                stringCmd.append(value);
                if (!columnType.equals(SQLConstant.PRIMITIVE_INT)) {
                    stringCmd.append(SQLConstant.QUOTE);
                }
                stringCmd.append(SQLConstant.COMMON);
                stringCmd.append(SQLConstant.SPACE);
            }
            stringCmd.setLength(stringCmd.length() - 2);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.WHERE);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(idName);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.EQUAL);
            stringCmd.append(SQLConstant.SPACE);
            if (!idType.equals(SQLConstant.PRIMITIVE_INT)) {
                stringCmd.append(SQLConstant.QUOTE);
            }
            stringCmd.append(idValue);
            if (!idType.equals(SQLConstant.PRIMITIVE_INT)) {
                stringCmd.append(SQLConstant.QUOTE);
            }
            ConnectionHelper.getConnection().createStatement().execute(stringCmd.toString());
            return obj;
        } catch ( IllegalAccessException | SQLException error) {
            System.out.printf("Update  failed error: %s \n", error.getMessage());
        }
        return null;
    }


    //delete
    public boolean delete(Object id) {
        //delete from {tableName} where id = id
        try {
            String tableName = clazz.getAnnotation(Entity.class).tableName();
            StringBuilder stringCmd = new StringBuilder();
            stringCmd.append(SQLConstant.DELETE);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.FROM);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(tableName);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.WHERE);
            //id information
            String idName = "";
            String idType = "";
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(Column.class)) {
                    continue;
                }

                field.setAccessible(true);
                Column columnInformation = field.getDeclaredAnnotation(Column.class);
                String columnName = columnInformation.columnName();
                String columnType = columnInformation.columnType();
                if (field.isAnnotationPresent(Id.class)) {
                    //dont update id
                    //but get id information
                    idName = columnName;
                    idType = columnType;
                    break;
                }
            }
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(idName);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.EQUAL);
            stringCmd.append(SQLConstant.SPACE);
            if (!idType.equals(SQLConstant.PRIMITIVE_INT)) {
                stringCmd.append(SQLConstant.QUOTE);
            }
            stringCmd.append(id);
            if (!idType.equals(SQLConstant.PRIMITIVE_INT)) {
                stringCmd.append(SQLConstant.QUOTE);
            }
            ConnectionHelper.getConnection().createStatement().execute(stringCmd.toString());
            return true;
        } catch (SQLException error) {
            System.out.printf("Delete failed  error: %s \n", error.getMessage());
        }
        return false;
    }

    //find by status !=0

    public ArrayList<T> findByStatus() {
        ArrayList<T> listObj = new ArrayList<T>();
        Entity entity = clazz.getAnnotation(Entity.class);
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder fieldValues = new StringBuilder();
        stringBuilder.append(SQLConstant.SELECT_ASTERISK);
        stringBuilder.append(SQLConstant.SPACE);
        stringBuilder.append(SQLConstant.FROM);
        stringBuilder.append(SQLConstant.SPACE);
        stringBuilder.append(entity.tableName());
        stringBuilder.append(SQLConstant.SPACE);
        stringBuilder.append(SQLConstant.WHERE);
        stringBuilder.append(SQLConstant.SPACE);
        for (Field field : clazz.getDeclaredFields()) {
            // check xem trường có phải là @Column không.
            if (!field.isAnnotationPresent(Column.class)) {
                // bỏ qua trong trường hợp không được đánh là @Column.
                continue;
            }
            field.setAccessible(true);
            Column columnInfor = field.getAnnotation(Column.class);
            if (columnInfor.columnName().equals(SQLConstant.STATUS)) {
                stringBuilder.append(columnInfor.columnName()); // nối tên trường.
                stringBuilder.append(SQLConstant.SPACE); //
                stringBuilder.append(SQLConstant.NOT_EQUAL); //
                stringBuilder.append(SQLConstant.SPACE); //
                fieldValues.append(0); // field.setAccessible(true);
                stringBuilder.append(fieldValues); //
            }
        }
        try {
            PreparedStatement preparedStatement = ConnectionHelper.getConnection().prepareStatement(stringBuilder.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                T obj = clazz.newInstance();
                for (Field field : clazz.getDeclaredFields()) {
                    if (!field.isAnnotationPresent(Column.class)) {
                        continue;
                    }
                    field.setAccessible(true);
                    Column columnInfor = field.getAnnotation(Column.class);

                    switch (columnInfor.columnType()) {
                        case SQLConstant.PRIMITIVE_INT:
                            // set giá trị của trường đó cho đối tượng mới tạo ở trên.
                            field.set(obj, resultSet.getInt(columnInfor.columnName()));
                            break;
                        case SQLConstant.VARCHAR_250:
                        case SQLConstant.VARCHAR_50:
                            field.set(obj, resultSet.getString(columnInfor.columnName()));
                            break;
                        case SQLConstant.PRIMITIVE_DOUBLE:
                            field.set(obj, resultSet.getDouble(columnInfor.columnName()));
                            break;
                        default:
                            break;
                    }
                }
                listObj.add(obj);
            }
        } catch ( InstantiationException | IllegalAccessException | SQLException error) {
            System.err.printf("Find all error %s\n", error.getMessage());
        }
        return listObj;
    }
}
