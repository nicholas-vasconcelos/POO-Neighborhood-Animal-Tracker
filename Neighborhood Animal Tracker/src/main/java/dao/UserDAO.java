package dao;

import factory.ConnectionFactory;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserDAO implements BaseDAO<User> {

    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO Users (name, condominium) " +
                     "VALUES (?, ?)";

        try (PreparedStatement pstm = connection.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
        )) {
            pstm.setString(1, user.getName());
            pstm.setString(2, user.getCondominium());

            pstm.execute();

            try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    user.setId(generatedId);
                } else {
                    throw new SQLException("Failed to retrieve auto-generated ID for user: " + user.getName());
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findById(int id) {
        String sql = "SELECT id, name, condominium " +
                     "FROM Users " +
                     "WHERE id = ?";
        User user = null;

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, id);

            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) {
                    int userId = resultSet.getInt("id");
                    String userName = resultSet.getString("name");
                    String userCondominium = resultSet.getString("condominium");
                    user = new User(userId, userName, userCondominium);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public ArrayList<User> findAllLazyLoading() {
        String sql = "SELECT id, name, condominium " +
                     "FROM Users";
        ArrayList<User> users = new ArrayList<>();

        try (PreparedStatement pstm = connection.prepareStatement(sql);
             ResultSet resultSet = pstm.executeQuery()) {

            while (resultSet.next()) {
                int userId = resultSet.getInt("id");
                String userName = resultSet.getString("name");
                String userCondominium = resultSet.getString("condominium");
                users.add(new User(userId, userName, userCondominium));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public ArrayList<User> findAllEagerLoading() {
        //Same as lazy...
        return findAllLazyLoading();
    }

    public void delete(int id) {

        try (PreparedStatement pstmCat = connection.prepareStatement("DELETE FROM Cats WHERE id = ?")) {
            pstmCat.setInt(1, id);
            pstmCat.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (PreparedStatement pstmDog = connection.prepareStatement("DELETE FROM Dogs WHERE id = ?")) {
            pstmDog.setInt(1, id);
            pstmDog.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (PreparedStatement pstmCapybara = connection.prepareStatement("DELETE FROM Capybaras WHERE id = ?")) {
            pstmCapybara.setInt(1, id);
            pstmCapybara.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String sqlAnimal = "DELETE FROM Animals WHERE id = ?";
        try (PreparedStatement pstmAnimal = connection.prepareStatement(sqlAnimal)) {
            pstmAnimal.setInt(1, id);
            pstmAnimal.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void update(User user) {
        String sql = "UPDATE Users SET name = ?, condominium = ? " +
                     "WHERE id = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setString(1, user.getName());
            pstm.setString(2, user.getCondominium());
            pstm.setInt(3, user.getId());
            pstm.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}