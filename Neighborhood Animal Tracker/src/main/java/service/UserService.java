package service;

import dao.UserDAO;
import model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserService {
    final UserDAO userDAO;
    final Connection connection;

    public UserService(Connection connection) {
        this.connection = connection;
        this.userDAO = new UserDAO(connection);
    }

    public void registerUser(User user) throws SQLException {
        connection.setAutoCommit(false);
        userDAO.save(user);
        connection.commit();
        System.out.println("User " + user.getName() + " created!");
    }

    public void updateUser(User user) throws SQLException {
        connection.setAutoCommit(false);
        userDAO.update(user);
        connection.commit();
        System.out.println("Report " + user.getName() + " updated!");
    }

    public User getUserById(int id) {
        return userDAO.findById(id);
    }

    public List<User> getAllUsers() {
        return userDAO.findAllLazyLoading();
    }

    public void deleteUser(int id) throws SQLException {
        connection.setAutoCommit(false);
        User user = userDAO.findById(id);
        userDAO.delete(id);
        connection.commit();
        System.out.println("User " + user.getName() + " deleted!");
    }
}