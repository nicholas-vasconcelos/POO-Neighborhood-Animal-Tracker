package dao;

import model.User;
import model.Animal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserAnimalLikesDAO {

    private Connection connection;
    private UserDAO userDAO;
    private AnimalDAO animalDAO;

    public UserAnimalLikesDAO(Connection connection) {
        this.connection = connection;
        this.userDAO = new UserDAO(connection);
        this.animalDAO = new AnimalDAO(connection);
    }

    public void addLike(int userId, int animalId) {
        String sql = "INSERT INTO UserAnimalLikes (user_id, animal_id) " +
                     "VALUES (?, ?)";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, userId);
            pstm.setInt(2, animalId);
            pstm.executeUpdate();
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed") || e.getSQLState().startsWith("23")) {
                System.out.println("User " + userId + " already liked animal " + animalId);
            } else {
                throw new RuntimeException("Error adding like: UserID=" + userId + ", AnimalID=" + animalId, e);
            }
        }
    }

    public void removeLike(int userId, int animalId) {
        String sql = "DELETE FROM UserAnimalLikes " +
                     "WHERE user_id = ? AND animal_id = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, userId);
            pstm.setInt(2, animalId);
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error removing like: UserID=" + userId + ", AnimalID=" + animalId, e);
        }
    }

    public boolean isLiked(int userId, int animalId) {
        String sql = "SELECT COUNT(*) " +
                     "FROM UserAnimalLikes " +
                     "WHERE user_id = ? AND animal_id = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, userId);
            pstm.setInt(2, animalId);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking like status: UserID=" + userId + ", AnimalID=" + animalId, e);
        }
        return false;
    }

    public List<Animal> getLikedAnimalsForUser(int userId) {
        List<Animal> likedAnimals = new ArrayList<>();
        String sql = "SELECT animal_id " +
                     "FROM UserAnimalLikes " +
                     "WHERE user_id = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, userId);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    int animalId = rs.getInt("animal_id");
                    Animal animal = animalDAO.findById(animalId);
                    if (animal != null) {
                        likedAnimals.add(animal);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting liked animals for UserID: " + userId, e);
        }
        return likedAnimals;
    }

    public List<User> getUsersWhoLikedAnimal(int animalId) {
        List<User> likedUsers = new ArrayList<>();
        String sql = "SELECT user_id " +
                     "FROM UserAnimalLikes " +
                     "WHERE animal_id = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, animalId);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("user_id");
                    User user = userDAO.findById(userId);
                    if (user != null) {
                        likedUsers.add(user);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting users who liked AnimalID: " + animalId, e);
        }
        return likedUsers;
    }

    public int countLikesForAnimal(int animalId) {
        String sql = "SELECT COUNT(*) " +
                     "FROM UserAnimalLikes " +
                     "WHERE animal_id = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, animalId);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error counting likes for AnimalID: " + animalId, e);
        }
        return 0;
    }
}