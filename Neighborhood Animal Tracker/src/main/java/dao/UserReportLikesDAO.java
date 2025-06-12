package dao;

import model.User;
import model.Report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserReportLikesDAO {

    private Connection connection;
    private UserDAO userDAO;
    private ReportDAO reportDAO;

    public UserReportLikesDAO(Connection connection) {
        this.connection = connection;
        this.userDAO = new UserDAO(connection);
        this.reportDAO = new ReportDAO(connection);
    }

    public void addLike(int userId, int reportId) {
        String sql = "INSERT INTO ReportLikes (user_id, report_id) " +
                     "VALUES (?, ?)";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, userId);
            pstm.setInt(2, reportId);
            pstm.executeUpdate();
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed") || e.getSQLState().startsWith("23")) {
                System.out.println("User " + userId + " already liked report " + reportId);
            } else {
                throw new RuntimeException("Error adding like: UserID=" + userId + ", ReportID=" + reportId, e);
            }
        }
    }

    public void removeLike(int userId, int reportId) {
        String sql = "DELETE FROM ReportLikes " +
                     "WHERE user_id = ? AND report_id = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, userId);
            pstm.setInt(2, reportId);
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error removing like: UserID=" + userId + ", ReportID=" + reportId, e);
        }
    }

    public boolean isLiked(int userId, int reportId) {
        String sql = "SELECT COUNT(*) FROM ReportLikes " +
                     "WHERE user_id = ? AND report_id = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, userId);
            pstm.setInt(2, reportId);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking like status: UserID=" + userId + ", ReportID=" + reportId, e);
        }
        return false;
    }

    public List<Report> getLikedReportsForUser(int userId) {
        List<Report> likedReports = new ArrayList<>();
        String sql = "SELECT report_id " +
                     "FROM ReportLikes " +
                     "WHERE user_id = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, userId);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    int reportId = rs.getInt("report_id");
                    Report report = reportDAO.findById(reportId);
                    if (report != null) {
                        likedReports.add(report);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting liked reports for UserID: " + userId, e);
        }
        return likedReports;
    }

    public List<User> getUsersWhoLikedReport(int reportId) {
        List<User> likedUsers = new ArrayList<>();
        String sql = "SELECT user_id " +
                     "FROM ReportLikes " +
                     "WHERE report_id = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, reportId);
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
            throw new RuntimeException("Error getting users who liked ReportID: " + reportId, e);
        }
        return likedUsers;
    }

    public int countLikesForReport(int reportId) {
        String sql = "SELECT COUNT(*) FROM ReportLikes WHERE report_id = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, reportId);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error counting likes for ReportID: " + reportId, e);
        }
        return 0;
    }
}