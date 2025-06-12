package service;

import dao.AnimalDAO;
import dao.UserDAO;
import model.Animal;
import model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList; // Needed for returning empty list on findAll errors if no try-catch

public class AnimalService {
    private AnimalDAO animalDAO;
    private UserDAO userDAO;
    private Connection connection;

    public AnimalService(Connection connection) {
        this.connection = connection;
        this.animalDAO = new AnimalDAO(connection);
        this.userDAO = new UserDAO(connection);
    }

    public void registerAnimal(Animal animal) throws SQLException {
        connection.setAutoCommit(false);
        animalDAO.save(animal);
        connection.commit();
        System.out.println("Animal " + animal.getNickName() + " registered!");
    }

    public Animal getAnimalById(int id) {
        return animalDAO.findById(id);
    }

    public List<Animal> getAllAnimalsLazy() {
        return animalDAO.findAllLazyLoading();
    }

    public List<Animal> getAllAnimalsEager() {
        return animalDAO.findAllEagerLoading();
    }

    public void updateAnimal(Animal animal) throws SQLException {
        connection.setAutoCommit(false);
        animalDAO.update(animal);
        connection.commit();
        System.out.println("Animal " + animal.getNickName() +" updated!");
    }

    public void deleteAnimal(int id) throws SQLException {
        connection.setAutoCommit(false);
        Animal animal = animalDAO.findById(id);
        animalDAO.delete(id);
        connection.commit();
        System.out.println("Animal " + animal.getNickName() + " deleted!");
    }

    public void userLikesAnimal(int userId, int animalId) throws SQLException {
        connection.setAutoCommit(false);
        User user = userDAO.findById(userId);
        Animal animal = animalDAO.findById(animalId);

        animal.like(user);
        animalDAO.update(animal);
        connection.commit();
        System.out.println("User " + user.getName() + " liked " + animal.getNickName() + "!");
    }

    public void userUnlikesAnimal(int userId, int animalId) throws SQLException {
        connection.setAutoCommit(false);
        User user = userDAO.findById(userId);
        Animal animal = animalDAO.findById(animalId);

        animal.unlike(user);
        animalDAO.update(animal);
        connection.commit();

        System.out.println("User " + user.getName() + " unliked " + animal.getNickName() + "!");
    }
}