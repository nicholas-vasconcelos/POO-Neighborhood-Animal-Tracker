package dao;

import model.Report;
import model.User;
import model.Animal;
import enums.StreetName;
import interfaces.Likeable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;

public class ReportDAO implements BaseDAO<Report> {

    private Connection connection;
    private UserDAO userDAO;
    private AnimalDAO animalDAO;

    public ReportDAO(Connection connection) {
        this.connection = connection;
        this.userDAO = new UserDAO(connection);
        this.animalDAO = new AnimalDAO(connection);
    }

    public void save(Report report) {
        String sql = "INSERT INTO Reports (description, street_name, created_by_user_id, reported_animal_id) " +
                     "VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, report.getDescription());
            pstm.setString(2, report.getStreet().name());
            pstm.setInt(3, report.getCreatedBy().getId());
            pstm.setInt(4, report.getReportedAnimal().getId());

            pstm.executeUpdate();

            try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    report.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Failed to retrieve auto-generated ID for Report.");
                }
            }
            saveLikedByUsers(report);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Report findById(int id) {
        Report report = null;
        String sql = "SELECT id, description, street_name, created_by_user_id, reported_animal_id " +
                     "FROM Reports " +
                     "WHERE id = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    String description = rs.getString("description");
                    StreetName street = StreetName.valueOf(rs.getString("street_name"));
                    int createdByUserId = rs.getInt("created_by_user_id");
                    int reportedAnimalId = rs.getInt("reported_animal_id");

                    User createdBy = userDAO.findById(createdByUserId);
                    Animal reportedAnimal = animalDAO.findById(reportedAnimalId);

                    report = new Report(id, description, street, createdBy, reportedAnimal);
                    report.setLikedBy(loadLikedByUsers(report.getId()));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return report;
    }

    public ArrayList<Report> findAllLazyLoading() {
        ArrayList<Report> reports = new ArrayList<>();
        String sql = "SELECT id, description, street_name, created_by_user_id, reported_animal_id " +
                     "FROM Reports";
        try (PreparedStatement pstm = connection.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String description = rs.getString("description");
                StreetName street = StreetName.valueOf(rs.getString("street"));
                int createdByUserId = rs.getInt("created_by_user_id");
                int reportedAnimalId = rs.getInt("reported_animal_id");

                User createdBy = userDAO.findById(createdByUserId);
                Animal reportedAnimal = animalDAO.findById(reportedAnimalId);

                reports.add(new Report(id, description, street, createdBy, reportedAnimal));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reports;
    }

    public ArrayList<Report> findAllEagerLoading() {
        ArrayList<Report> reports;
        HashMap<Integer, Report> reportMap = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT R.id, R.description, R.street_name, R.created_by_user_id, R.reported_animal_id, ");
        sql.append("U.id AS user_id, U.name AS user_name, U.email AS user_email, ");
        sql.append("A.id AS animal_id, A.nick_name AS animal_nick_name, A.color AS animal_color, A.size AS animal_size, ");
        sql.append("UAL.user_id AS liker_user_id ");
        sql.append("FROM Reports R ");
        sql.append("JOIN Users U ON R.created_by_user_id = U.id ");
        sql.append("JOIN Animals A ON R.reported_animal_id = A.id ");
        sql.append("LEFT JOIN UserReportLikes UAL ON R.id = UAL.report_id");

        try (PreparedStatement pstm = connection.prepareStatement(sql.toString());
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                int reportId = rs.getInt("id");
                Report currentReport = reportMap.get(reportId);

                if (currentReport == null) {
                    String description = rs.getString("description");
                    StreetName street = StreetName.valueOf(rs.getString("street_name"));

                    int createdByUserId = rs.getInt("created_by_user_id");
                    String createdByUserName = rs.getString("user_name");
                    String createdByUserEmail = rs.getString("user_email");
                    User createdBy = new User(createdByUserId, createdByUserName, createdByUserEmail);

                    int reportedAnimalId = rs.getInt("reported_animal_id");
                    String reportedAnimalNickName = rs.getString("animal_nick_name");
                    Animal reportedAnimal = new Animal(reportedAnimalId, reportedAnimalNickName, null, null) {}; // Anonymous concrete Animal

                    currentReport = new Report(reportId, description, street, createdBy, reportedAnimal);
                    reportMap.put(reportId, currentReport);
                }

                int likerUserId = rs.getInt("liker_user_id");
                if (!rs.wasNull()) {
                    User liker = userDAO.findById(likerUserId);
                    if (liker != null) {
                        currentReport.like(liker);
                    }
                }
            }
            reports = new ArrayList<>(reportMap.values());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reports;
    }

    public void update(Report report) {
        String sql = "UPDATE Reports SET description = ?, street_name = ?, created_by_user_id = ?, reported_animal_id = ? " +
                     "WHERE id = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setString(1, report.getDescription());
            pstm.setString(2, report.getStreet().name());
            pstm.setInt(3, report.getCreatedBy().getId());
            pstm.setInt(4, report.getReportedAnimal().getId());
            pstm.setInt(5, report.getId());
            pstm.executeUpdate();
            deleteLikedByUsers(report.getId());
            saveLikedByUsers(report);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int id) {

        String sql = "DELETE FROM Reports " +
                     "WHERE id = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, id);
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveLikedByUsers(Report report) throws SQLException {
        if (report.getLikedByUsers() != null && !report.getLikedByUsers().isEmpty()) {
            String sql = "INSERT INTO ReportLikes (user_id, report_id) " +
                         "VALUES (?, ?)";
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                for (User user : report.getLikedByUsers()) {
                    pstm.setInt(1, user.getId());
                    pstm.setInt(2, report.getId());
                    pstm.addBatch();
                }
                pstm.executeBatch();
            }
        }
    }

    private void deleteLikedByUsers(int reportId) throws SQLException {
        String sql = "DELETE FROM ReportLikes WHERE report_id = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, reportId);
            pstm.executeUpdate();
        }
    }

    private Set<User> loadLikedByUsers(int reportId) throws SQLException {
        Set<User> likedByUsers = new HashSet<>();
        String sql = "SELECT user_id FROM ReportLikes " +
                     "WHERE report_id = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, reportId);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("user_id");
                    User user = userDAO.findById(userId);
                    if (user != null) {
                        likedByUsers.add(user);
                    }
                }
            }
        }
        return likedByUsers;
    }
}