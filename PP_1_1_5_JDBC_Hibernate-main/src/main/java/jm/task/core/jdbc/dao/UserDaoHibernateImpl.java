package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.*;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
   private final SessionFactory factory = Util.SessionFabric();

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        try(Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            String query = """
                   CREATE TABLE IF NOT EXISTS user (
                   id INT NOT NULL AUTO_INCREMENT,
                   name VARCHAR(45) NOT NULL,
                   lastName VARCHAR(45) NOT NULL,
                   age INT(2) NOT NULL,
                   PRIMARY KEY (ID)
                );
                """;
        session.createSQLQuery(query).executeUpdate();
        transaction.commit();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = factory.openSession()) {
            String query = "DROP TABLE IF EXISTS user";
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery(query).executeUpdate();
            transaction.commit();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            User newUser = new User(name, lastName, age);
            transaction = session.beginTransaction();
            session.save(newUser);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("delete User where id = %d".formatted(id)).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        try(Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            @SuppressWarnings("unchecked")
            List<User> userList = session.createQuery("from User").getResultList();
            transaction.commit();
            return userList;
        }
    }

    @Override
    public void cleanUsersTable() {
        try(Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("delete User").executeUpdate();
            transaction.commit();
        }
    }
}
