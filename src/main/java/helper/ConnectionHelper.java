package helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {
    private static final String DATABASE_URL_FORMAT = "jdbc:mysql://%s:%d/%s?useUnicode=true&contentEncoding=UTF-8";
    private static final String DATABASE_SERVER = "localhost";
    private static final int DATABASE_PORT = 8080;
    private static final String DATABASE_NAME = "asm_ntd";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PWD = "";
    private static Connection cnn;

    public static Connection getConnection() {
        try {
//            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            if (cnn == null || cnn.isClosed()) {
                cnn = DriverManager.getConnection(String.format(DATABASE_URL_FORMAT, DATABASE_SERVER,
                        DATABASE_PORT, DATABASE_NAME,
                        DATABASE_USER, DATABASE_PWD));
                System.out.println("Connection Success");
            }
        } catch (SQLException throwables) {
            System.out.println("Connection Failed!");
            System.err.println(throwables.getMessage());
        }
        return cnn;
    }

}
