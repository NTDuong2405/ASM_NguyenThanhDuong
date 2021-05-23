package helper;

import dannotation.Column;
import dannotation.Entity;
import dannotation.Id;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

public class DMigartion {
    public static void main(String[] args) throws SQLException {
        // quét trong package entity.
        Reflections reflections = new Reflections("entity");
        // tìm ra tất cả các class có annotation là @Entity.
        Set<Class<?>> allClasses =
                reflections.getTypesAnnotatedWith(Entity.class);
        for (Class<?> c :
                allClasses) {
            createTable(c);
        }
    }
    private static void createTable(Object obj) {
        Class currentClass = obj.getClass();
        if (!currentClass.isAnnotationPresent(Entity.class)) {
            return;
        }
        Entity currentEntity = (Entity) currentClass.getAnnotation(Entity.class);
        //build sql cmd
        StringBuilder stringCmd = new StringBuilder();
        stringCmd.append(SQLConstant.CREATE_TABLE);
        stringCmd.append(SQLConstant.SPACE);
        stringCmd.append(currentEntity.tableName());
        stringCmd.append(SQLConstant.SPACE);
        stringCmd.append(SQLConstant.OPEN_PARENTHESES);
        Field[] fields = currentClass.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }
            Column currentColumn = field.getAnnotation(Column.class);
            stringCmd.append(currentColumn.columnName());
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(currentColumn.columnType());
            //id checker
            if (field.isAnnotationPresent(Id.class)) {
                System.out.println("here");
                Id currentId = (Id) field.getAnnotation(Id.class);
                stringCmd.append(SQLConstant.SPACE);
                stringCmd.append(SQLConstant.PRIMARY_KEY);
                //auto icreament checker
                if (currentId.autoIncrement()) {
                    stringCmd.append(SQLConstant.SPACE);
                    stringCmd.append(SQLConstant.AUTO_INCREMENT);
                }
            }
            stringCmd.append(SQLConstant.COMMON);
            stringCmd.append(SQLConstant.SPACE);

        }
        stringCmd.setLength(stringCmd.length() - 2);
        stringCmd.append(SQLConstant.CLOSE_PARENTHESES);
        System.out.println(stringCmd.toString());
        Connection connection = ConnectionHelper.getConnection();
        try {
            connection.createStatement().execute(stringCmd.toString());
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }
}
