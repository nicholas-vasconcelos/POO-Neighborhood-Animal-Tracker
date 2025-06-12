package service;

import dao.ReportDAO;
import dao.UserDAO;
import dao.AnimalDAO;
import model.Report;
import model.User;
import model.Animal;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ReportService {
    final ReportDAO reportDAO;
    final UserDAO userDAO;
    final AnimalDAO animalDAO;
    final Connection connection;

    public ReportService(Connection connection) {
        this.connection = connection;
        this.reportDAO = new ReportDAO(connection);
        this.userDAO = new UserDAO(connection);
        this.animalDAO = new AnimalDAO(connection);
    }

    public void createReport(Report report) throws SQLException {
        connection.setAutoCommit(false);
        reportDAO.save(report);
        connection.commit();
        System.out.println("Animal " + report.getReportedAnimal().getNickName() + " report created!");
    }

    public Report getReportById(int id) {
        return reportDAO.findById(id);
    }

    public List<Report> getAllReportsLazy() {
        return reportDAO.findAllLazyLoading();
    }

    public void updateReport(Report report) throws SQLException {
        connection.setAutoCommit(false);
        reportDAO.update(report);
        connection.commit();
        System.out.println("Report " + report.getId() + " updated!");
    }

    public void deleteReport(int id) throws SQLException {
        connection.setAutoCommit(false);
        reportDAO.delete(id);
        connection.commit();
        System.out.println("Report " + id + " deleted!");
    }

    public void userLikesReport(int userId, int reportId) throws SQLException {
        connection.setAutoCommit(false);
        User user = userDAO.findById(userId);
        Report report = reportDAO.findById(reportId);

        report.like(user);
        reportDAO.update(report);
        connection.commit();

        System.out.println("User " + user.getName() + " liked report " + report.getId() + "!");
    }

    public void userUnlikesReport(int userId, int animalId) throws SQLException {
        connection.setAutoCommit(false);
        User user = userDAO.findById(userId);
        Report report = reportDAO.findById(animalId);

        report.unlike(user);
        reportDAO.update(report);
        connection.commit();

        System.out.println("User " + user.getName() + " unliked " + report.getId() + "!");
    }
}