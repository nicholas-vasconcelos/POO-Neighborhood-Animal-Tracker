package factory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public static Connection getConnection() {
        try {
            // Database configuration from slides
            String sgbd = "mysql";
            String endereco = "localhost";
            String bd = "animaltracker";
            String usuario = "root";
            String senha = "admin";

            // Establish connection
            Connection connection = DriverManager.getConnection(
                    "jdbc:" + sgbd + "://" + endereco + "/" + bd, usuario, senha);

            System.out.println("Connected to DB");
            return connection;
        } catch (SQLException e) {

            System.out.println("Error connecting to DB");
            throw new RuntimeException(e);
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.printf("Error closing database connection: %s%n", e.getMessage());
                e.printStackTrace();
            }
        }
    }
}