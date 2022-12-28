package jm.task.core.jdbc.util;
import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.*;
import java.util.Properties;

public class Util {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/UsersDB";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "12345678";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);
            System.out.println("Драйвер установлен");
        } catch (SQLException e) {
            System.out.println("Ошибка в установке драйвера");
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Соединение с БД установлено");
        } catch (SQLException e) {
            System.out.println("Соединение с БД не установлено");
        }
        return connection;
    }

    public static SessionFactory SessionFabric() {
        try {
            Properties prop = new Properties();
            prop.setProperty("hibernate.connection.url", DB_URL);
            prop.setProperty("hibernate.connection.username", DB_USERNAME);
            prop.setProperty("hibernate.connection.password", DB_PASSWORD);
            prop.setProperty("dialect", "org.hibernate.dialect.MySQLDialect");

            return new Configuration()
                    .addProperties(prop)
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();
        }
        catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
