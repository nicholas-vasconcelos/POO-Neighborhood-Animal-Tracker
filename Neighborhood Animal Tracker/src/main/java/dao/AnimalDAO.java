package dao;

import model.Animal;
import model.Cat;
import model.Dog;
import model.Capybara;
import model.User;
import enums.Color;
import enums.Size;
import enums.TailLength;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AnimalDAO implements BaseDAO<Animal> {

    private Connection connection;
    private UserDAO userDAO;

    public AnimalDAO(Connection connection) {
        this.connection = connection;
        this.userDAO = new UserDAO(connection);
    }

    public void save(Animal animal) {
        String sqlAnimals = "INSERT INTO Animals (nick_name, color, size) " +
                            "VALUES (?, ?, ?)";

        try (PreparedStatement pstmAnimals = connection.prepareStatement(
                sqlAnimals,
                Statement.RETURN_GENERATED_KEYS
        )) {
            pstmAnimals.setString(1, animal.getNickName());
            pstmAnimals.setString(2, animal.getColor().name());
            pstmAnimals.setString(3, animal.getSize().name());

            pstmAnimals.execute();

            try (ResultSet generatedKeys = pstmAnimals.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int animalId = generatedKeys.getInt(1);
                    animal.setId(animalId);
                } else {
                    throw new SQLException("Failed to get Animal ID.");
                }
            }

            if (animal instanceof Cat cat) {
                String sqlCat = "INSERT INTO Cats (id, breed, scratches_people, likes_being_pet, is_vaccinated, is_castrated) " +
                                "VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pstmCat = connection.prepareStatement(sqlCat)) {
                    pstmCat.setInt(1, cat.getId());
                    pstmCat.setString(2, cat.getBreed());
                    pstmCat.setBoolean(3, cat.isScratchesPeople());
                    pstmCat.setBoolean(4, cat.isLikesBeingPet());
                    pstmCat.setBoolean(5, cat.isVaccinated());
                    pstmCat.setBoolean(6, cat.isCastrated());
                    pstmCat.execute();
                }
            } else if (animal instanceof Dog dog) {
                String sqlDog = "INSERT INTO Dogs (id, breed, tail_length, chases_cars, likes_being_pet, is_vaccinated, is_castrated) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pstmDog = connection.prepareStatement(sqlDog)) {
                    pstmDog.setInt(1, dog.getId());
                    pstmDog.setString(2, dog.getBreed());
                    pstmDog.setString(3, dog.getTailLength().name());
                    pstmDog.setBoolean(4, dog.isChasesCars());
                    pstmDog.setBoolean(5, dog.isLikesBeingPet());
                    pstmDog.setBoolean(6, dog.isVaccinated());
                    pstmDog.setBoolean(7, dog.isCastrated());
                    pstmDog.execute();
                }
            } else if (animal instanceof Capybara capybara) {
                String sqlCapybara = "INSERT INTO Capybaras (id, social_group_size, eats_grass) " +
                                     "VALUES (?, ?, ?)";
                try (PreparedStatement pstmCapybara = connection.prepareStatement(sqlCapybara)) {
                    pstmCapybara.setInt(1, capybara.getId());
                    pstmCapybara.setInt(2, capybara.getSocialGroupSize());
                    pstmCapybara.setBoolean(3, capybara.isEatsGrass());
                    pstmCapybara.execute();
                }
            } else {
                throw new IllegalArgumentException("Unknown Animal subtype: " + animal.getClass().getName());
            }

            saveLikedByUsers(animal);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Animal findById(int id) {
        Animal animal = null;
        String sqlAnimalBase = "SELECT id, nick_name, color, size " +
                               "FROM Animals WHERE id = ?";

        try (PreparedStatement pstmBase = connection.prepareStatement(sqlAnimalBase)) {
            pstmBase.setInt(1, id);
            try (ResultSet rsBase = pstmBase.executeQuery()) {
                if (rsBase.next()) {
                    String nickName = rsBase.getString("nick_name");
                    Color color = Color.valueOf(rsBase.getString("color"));
                    Size size = Size.valueOf(rsBase.getString("size"));

                    Cat cat = loadCatData(id, nickName, color, size);
                    if (cat != null) {
                        animal = cat;
                    } else {
                        Dog dog = loadDogData(id, nickName, color, size);
                        if (dog != null) {
                            animal = dog;
                        } else {
                            Capybara capybara = loadCapybaraData(id, nickName, color, size);
                            if (capybara != null) {
                                animal = capybara;
                            }
                        }
                    }

                    if (animal != null) {
                        animal.setLikedBy(loadLikedByUsers(animal.getId()));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return animal;
    }

    private Cat loadCatData(int id, String nickName, Color color, Size size) throws SQLException {
        String sqlCat = "SELECT breed, scratches_people, likes_being_pet, is_vaccinated, is_castrated " +
                        "FROM Cats " +
                        "WHERE id = ?";
        try (PreparedStatement pstmCat = connection.prepareStatement(sqlCat)) {
            pstmCat.setInt(1, id);
            try (ResultSet rsCat = pstmCat.executeQuery()) {
                if (rsCat.next()) {
                    String breed = rsCat.getString("breed");
                    boolean scratchesPeople = rsCat.getBoolean("scratches_people");
                    boolean likesBeingPet = rsCat.getBoolean("likes_being_pet");
                    boolean vaccinated = rsCat.getBoolean("is_vaccinated");
                    boolean castrated = rsCat.getBoolean("is_castrated");
                    return new Cat(id, nickName, color, size, breed, scratchesPeople, likesBeingPet, vaccinated, castrated);
                }
            }
        }
        return null;
    }

    private Dog loadDogData(int id, String nickName, Color color, Size size) throws SQLException {
        String sqlDog = "SELECT breed, tail_length, chases_cars, likes_being_pet, is_vaccinated, is_castrated " +
                        "FROM Dogs " +
                        "WHERE id = ?";
        try (PreparedStatement pstmDog = connection.prepareStatement(sqlDog)) {
            pstmDog.setInt(1, id);
            try (ResultSet rsDog = pstmDog.executeQuery()) {
                if (rsDog.next()) {
                    String breed = rsDog.getString("breed");
                    TailLength tailLength = TailLength.valueOf(rsDog.getString("tail_length"));
                    boolean chasesCars = rsDog.getBoolean("chases_cars");
                    boolean likesBeingPet = rsDog.getBoolean("likes_being_pet");
                    boolean vaccinated = rsDog.getBoolean("is_vaccinated");
                    boolean castrated = rsDog.getBoolean("is_castrated");
                    return new Dog(id, nickName, color, size, breed, tailLength, chasesCars, likesBeingPet, vaccinated, castrated);
                }
            }
        }
        return null;
    }

    private Capybara loadCapybaraData(int id, String nickName, Color color, Size size) throws SQLException {
        String sqlCapybara = "SELECT social_group_size, eats_grass " +
                             "FROM Capybaras" +
                             "WHERE id = ?";
        try (PreparedStatement pstmCapybara = connection.prepareStatement(sqlCapybara)) {
            pstmCapybara.setInt(1, id);
            try (ResultSet rsCapybara = pstmCapybara.executeQuery()) {
                if (rsCapybara.next()) {
                    int socialGroupSize = rsCapybara.getInt("social_group_size");
                    boolean eatsGrass = rsCapybara.getBoolean("eats_grass");
                    return new Capybara(id, nickName, color, size, socialGroupSize, eatsGrass);
                }
            }
        }
        return null;
    }

    private Set<User> loadLikedByUsers(int animalId) throws SQLException {
        Set<User> likedByUsers = new HashSet<>();
        String sql = "SELECT user_id " +
                     "FROM AnimalLikes " +
                     "WHERE animal_id = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, animalId);
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

    public ArrayList<Animal> findAllLazyLoading() {
        ArrayList<Animal> animals = new ArrayList<>();
        String sqlAnimalBase = "SELECT id, nick_name, color, size " +
                               "FROM Animals";

        try (PreparedStatement pstmBase = connection.prepareStatement(sqlAnimalBase);
             ResultSet rsBase = pstmBase.executeQuery()) {
            while (rsBase.next()) {
                int id = rsBase.getInt("id");
                String nickName = rsBase.getString("nick_name");
                Color color = Color.valueOf(rsBase.getString("color"));
                Size size = Size.valueOf(rsBase.getString("size"));

                Cat cat = loadCatData(id, nickName, color, size);
                if (cat != null) {
                    animals.add(cat);
                } else {
                    Dog dog = loadDogData(id, nickName, color, size);
                    if (dog != null) {
                        animals.add(dog);
                    } else {
                        Capybara capybara = loadCapybaraData(id, nickName, color, size);
                        if (capybara != null) {
                            animals.add(capybara);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return animals;
    }

    public ArrayList<Animal> findAllEagerLoading() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT A.id, A.nick_name, A.color, A.size, ");
        sql.append("C.breed AS cat_breed, C.scratches_people, C.likes_being_pet AS cat_likes_being_pet, C.is_vaccinated AS cat_is_vaccinated, C.is_castrated AS cat_is_castrated, ");
        sql.append("D.breed AS dog_breed, D.tail_length, D.chases_cars, D.likes_being_pet AS dog_likes_being_pet, D.is_vaccinated AS dog_is_vaccinated, D.is_castrated AS dog_is_castrated, ");
        sql.append("CAP.social_group_size, CAP.eats_grass, ");
        sql.append("UAL.user_id ");
        sql.append("FROM Animals A ");
        sql.append("LEFT JOIN Cats C ON A.id = C.id ");
        sql.append("LEFT JOIN Dogs D ON A.id = D.id ");
        sql.append("LEFT JOIN Capybaras CAP ON A.id = CAP.id ");
        sql.append("LEFT JOIN AnimalLikes UAL ON A.id = UAL.animal_id ");

        ArrayList<Animal> animals = new ArrayList<>();
        java.util.Map<Integer, Animal> animalMap = new java.util.HashMap<>();

        try (PreparedStatement pstm = connection.prepareStatement(sql.toString());
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");

                Animal currentAnimal = animalMap.get(id);

                if (currentAnimal == null) {
                    String nickName = rs.getString("nick_name");
                    Color color = Color.valueOf(rs.getString("color"));
                    Size size = Size.valueOf(rs.getString("size"));

                    if (rs.getString("cat_breed") != null) {
                        currentAnimal = new Cat(
                                id, nickName, color, size,
                                rs.getString("cat_breed"),
                                rs.getBoolean("scratches_people"),
                                rs.getBoolean("cat_likes_being_pet"),
                                rs.getBoolean("cat_is_vaccinated"),
                                rs.getBoolean("cat_is_castrated")
                        );
                    } else if (rs.getString("dog_breed") != null) {
                        currentAnimal = new Dog(
                                id, nickName, color, size,
                                rs.getString("dog_breed"),
                                TailLength.valueOf(rs.getString("tail_length")),
                                rs.getBoolean("chases_cars"),
                                rs.getBoolean("dog_likes_being_pet"),
                                rs.getBoolean("dog_is_vaccinated"),
                                rs.getBoolean("dog_is_castrated")
                        );
                    } else if (rs.getObject("social_group_size") != null) {
                        currentAnimal = new Capybara(
                                id, nickName, color, size,
                                rs.getInt("social_group_size"),
                                rs.getBoolean("eats_grass")
                        );
                    }
                    if (currentAnimal != null) {
                        animalMap.put(id, currentAnimal);
                    }
                }

                if (currentAnimal != null) {
                    int userId = rs.getInt("user_id");
                    if (!rs.wasNull()) {
                        User user = userDAO.findById(userId);
                        if (user != null) {
                            currentAnimal.like(user);
                        }
                    }
                }
            }
            animals.addAll(animalMap.values());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return animals;
    }

    public void update(Animal animal) {
        String sqlAnimals = "UPDATE Animals SET nick_name = ?, color = ?, size = ? " +
                            "WHERE id = ?";
        try (PreparedStatement pstmAnimals = connection.prepareStatement(sqlAnimals)) {
            pstmAnimals.setString(1, animal.getNickName());
            pstmAnimals.setString(2, animal.getColor().name());
            pstmAnimals.setString(3, animal.getSize().name());
            pstmAnimals.setInt(4, animal.getId());
            pstmAnimals.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            if (animal instanceof Cat cat) {
                String sqlCat = "UPDATE Cats SET breed = ?, scratches_people = ?, likes_being_pet = ?, is_vaccinated = ?, is_castrated = ? " +
                                "WHERE id = ?";
                try (PreparedStatement pstmCat = connection.prepareStatement(sqlCat)) {
                    pstmCat.setString(1, cat.getBreed());
                    pstmCat.setBoolean(2, cat.isScratchesPeople());
                    pstmCat.setBoolean(3, cat.isLikesBeingPet());
                    pstmCat.setBoolean(4, cat.isVaccinated());
                    pstmCat.setBoolean(5, cat.isCastrated());
                    pstmCat.setInt(6, cat.getId());
                    pstmCat.executeUpdate();
                }
            } else if (animal instanceof Dog dog) {
                String sqlDog = "UPDATE Dogs SET breed = ?, tail_length = ?, chases_cars = ?, likes_being_pet = ?, is_vaccinated = ?, is_castrated = ? " +
                                "WHERE id = ?";
                try (PreparedStatement pstmDog = connection.prepareStatement(sqlDog)) {
                    pstmDog.setString(1, dog.getBreed());
                    pstmDog.setString(2, dog.getTailLength().name());
                    pstmDog.setBoolean(3, dog.isChasesCars());
                    pstmDog.setBoolean(4, dog.isLikesBeingPet());
                    pstmDog.setBoolean(5, dog.isVaccinated());
                    pstmDog.setBoolean(6, dog.isCastrated());
                    pstmDog.setInt(7, dog.getId());
                    pstmDog.executeUpdate();
                }
            } else if (animal instanceof Capybara capybara) {
                String sqlCapybara = "UPDATE Capybaras SET social_group_size = ?, eats_grass = ? " +
                                     "WHERE id = ?";
                try (PreparedStatement pstmCapybara = connection.prepareStatement(sqlCapybara)) {
                    pstmCapybara.setInt(1, capybara.getSocialGroupSize());
                    pstmCapybara.setBoolean(2, capybara.isEatsGrass());
                    pstmCapybara.setInt(3, capybara.getId());
                    pstmCapybara.executeUpdate();
                }
            } else {
                throw new IllegalArgumentException("Unknown Animal subtype: " + animal.getClass().getName());
            }

            deleteLikedByUsers(animal.getId());
            saveLikedByUsers(animal);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    private void saveLikedByUsers(Animal animal) throws SQLException {
        if (animal.getLikedByUsers() != null && !animal.getLikedByUsers().isEmpty()) {
            String sql = "INSERT INTO AnimalLikes (user_id, animal_id) " +
                         "VALUES (?, ?)";
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                for (User user : animal.getLikedByUsers()) {
                    pstm.setInt(1, user.getId());
                    pstm.setInt(2, animal.getId());
                    pstm.addBatch();
                }
                pstm.executeBatch();
            }
        }
    }

    private void deleteLikedByUsers(int animalId) throws SQLException {
        String sql = "DELETE FROM AnimalLikes " +
                     "WHERE animal_id = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, animalId);
            pstm.executeUpdate();
        }
    }
}