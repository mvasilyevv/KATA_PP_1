package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final String DB_NAME = "user";
    private final Connection connection = Util.getConnection();
    public UserDaoJDBCImpl() {

    }
    public void createUsersTable() {
        String query = """
                   CREATE TABLE IF NOT EXISTS %s (
                   id INT NOT NULL AUTO_INCREMENT,
                   name VARCHAR(45) NOT NULL,
                   lastName VARCHAR(45) NOT NULL,
                   age INT(2) NOT NULL,
                   PRIMARY KEY (ID)
                );
                """.formatted(DB_NAME);
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.executeQuery();
            System.out.println("Таблица данных создана");
        } catch (SQLException e) {
            System.err.printf("Ошибка при создании таблицы: %s\n", e.getLocalizedMessage());
        }
    }

    public void dropUsersTable() {
        String query = "DROP TABLE IF EXISTS %s".formatted(DB_NAME);
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.execute();
            System.out.println("База данный удалена");
        } catch (SQLException e) {
            System.err.printf("Ошибка при удалении таблицы: %s\n", e.getLocalizedMessage());
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String query = "INSERT INTO %s (name, lastName, age) VALUES (?, ?, ?)".formatted(DB_NAME);
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3,age);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.printf("Запись {%s, %s, %d} была успешно добавлена\n", name, lastName, age);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.printf("Ошибка при сохранении объекта в БД: %s\n", e.getLocalizedMessage());
        }
    }

    public void removeUserById(long id) {
        String query = "DELETE FROM %s WHERE id = %d".formatted(DB_NAME, id);
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
            System.out.printf("Запись с id = %d была удалена", id);
        } catch (SQLException e) {
            System.err.printf("Ошибка при удалении строки по id %d: %s",id, e.getLocalizedMessage());
        }
    }

    public List<User> getAllUsers() {
        String query = "SELECT * FROM %s".formatted(DB_NAME);
        List<User> userList = new ArrayList<>();
        int count = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getNString("name"),
                        resultSet.getNString("lastName"),
                        resultSet.getByte("age")
                );
                userList.add(user);
                count++;
            }
            System.out.printf("Количество полученных записей: %d\n", count);
        } catch (SQLException e) {
            System.err.printf("Ошибка при получении данных из БД: %s\n", e.getLocalizedMessage());
        }
        return userList;
    }

    public void cleanUsersTable() {
        String query = "DELETE FROM %s".formatted(DB_NAME);
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
            System.out.println("Все записи из таблицы были удалена");
        } catch (SQLException e) {
            System.err.printf("Ошибка при удалении строк: %s\n", e.getLocalizedMessage());
        }
    }
}
