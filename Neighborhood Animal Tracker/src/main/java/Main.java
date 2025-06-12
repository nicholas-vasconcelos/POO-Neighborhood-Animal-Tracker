import enums.Color;
import enums.Size;
import enums.StreetName;
import enums.TailLength;
import model.Capybara;
import model.Cat;
import model.Dog;
import model.Report;
import model.User;
import service.AnimalService;
import service.ReportService;
import service.UserService;
import factory.ConnectionFactory;
import utils.ConsoleUtils;

import java.sql.Connection;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Connection connection = null;
        Scanner scanner = new Scanner(System.in);

        try {
            connection = ConnectionFactory.getConnection();
            System.out.printf("Database connection established.%n");

            // Initialize Service Layer
            UserService userService = new UserService(connection);
            AnimalService animalService = new AnimalService(connection);
            ReportService reportService = new ReportService(connection);

            System.out.printf("%n--- Initial Data Setup ---%n");
            try {

                // Register Users
                System.out.printf("Registering users...%n");

                User user1 = new User(0, "Alice Johnson", "alice@example.com");
                User user2 = new User(0, "Bob Williams", "bob@example.com");
                User user3 = new User(0, "Charlie Davis", "charlie@example.com");
                User user4 = new User(0, "Diana Green", "diana@example.com");
                User user5 = new User(0, "Eve Black", "eve@example.com");
                User user6 = new User(0, "Frank White", "frank@example.com");
                User user7 = new User(0, "Grace Hall", "grace@example.com");
                User user8 = new User(0, "Henry King", "henry@example.com");
                User user9 = new User(0, "Ivy Lee", "ivy@example.com");
                User user10 = new User(0, "Jack Turner", "jack@example.com");
                User user11 = new User(0, "Olivia Bell", "olivia@example.com");
                User user12 = new User(0, "Peter Clark", "peter@example.com");
                User user13 = new User(0, "Quinn Adams", "quinn@example.com");
                User user14 = new User(0, "Rachel Hill", "rachel@example.com");
                User user15 = new User(0, "Sam Taylor", "sam@example.com");

                userService.registerUser(user1);
                userService.registerUser(user2);
                userService.registerUser(user3);
                userService.registerUser(user4);
                userService.registerUser(user5);
                userService.registerUser(user6);
                userService.registerUser(user7);
                userService.registerUser(user8);
                userService.registerUser(user9);
                userService.registerUser(user10);
                userService.registerUser(user11);
                userService.registerUser(user12);
                userService.registerUser(user13);
                userService.registerUser(user14);
                userService.registerUser(user15);
                System.out.printf("Users registered.%n");

                System.out.printf("Registering animals...%n");
                Cat fluffy = new Cat(0, "Fluffy", Color.WHITE, Size.SMALL, "Persian", true, true, true, true);
                Cat whiskers = new Cat(0, "Whiskers", Color.BLACK, Size.MEDIUM, "Domestic", false, true, true, false);
                Cat mittens = new Cat(0, "Mittens", Color.GOLDEN, Size.SMALL, "Ragdoll", true, false, true, true);
                Dog buddy = new Dog(0, "Buddy", Color.GOLDEN, Size.LARGE, "Golden Retriever", TailLength.MEDIUM, false, true, true, false);
                Dog max = new Dog(0, "Max", Color.BROWN, Size.MEDIUM, "Labrador", TailLength.LONG, false, true, true, true);
                Dog bella = new Dog(0, "Bella", Color.BLACK, Size.SMALL, "Pug", TailLength.SHORT, true, true, true, false);
                Dog fido = new Dog(0, "Fido", Color.MIXED, Size.LARGE, "German Shepherd", TailLength.LONG, false, false, true, true);
                Capybara gary = new Capybara(0, "Gary", Color.GRAY, Size.LARGE, 5, true);
                Capybara squeaky = new Capybara(0, "Squeaky", Color.BROWN, Size.MEDIUM, 3, true);
                Capybara bubbles = new Capybara(0, "Bubbles", Color.GRAY, Size.LARGE, 7, true);
                Cat daisy = new Cat(0, "Daisy", Color.WHITE, Size.SMALL, "Siamese", false, true, true, false);
                Dog cooper = new Dog(0, "Cooper", Color.BROWN, Size.MEDIUM, "Beagle", TailLength.MEDIUM, true, true, true, true);
                Cat felix = new Cat(0, "Felix", Color.BLACK, Size.MEDIUM, "Bombay", true, true, true, true);
                Dog zoe = new Dog(0, "Zoe", Color.BLACK, Size.SMALL, "Terrier", TailLength.SHORT, false, true, true, false);
                Capybara river = new Capybara(0, "River", Color.BROWN, Size.LARGE, 6, true);
                Capybara coco = new Capybara(0, "Coco", Color.BROWN, Size.SMALL, 2, true);
                Dog bingo = new Dog(0, "Bingo", Color.WHITE, Size.MEDIUM, "Dalmatian", TailLength.MEDIUM, false, true, true, false);
                Cat luna = new Cat(0, "Luna", Color.GRAY, Size.SMALL, "Russian Blue", true, true, true, true);

                animalService.registerAnimal(fluffy);
                animalService.registerAnimal(whiskers);
                animalService.registerAnimal(mittens);
                animalService.registerAnimal(buddy);
                animalService.registerAnimal(max);
                animalService.registerAnimal(bella);
                animalService.registerAnimal(fido);
                animalService.registerAnimal(gary);
                animalService.registerAnimal(squeaky);
                animalService.registerAnimal(bubbles);
                animalService.registerAnimal(daisy);
                animalService.registerAnimal(cooper);
                animalService.registerAnimal(felix);
                animalService.registerAnimal(zoe);
                animalService.registerAnimal(river);
                animalService.registerAnimal(coco);
                animalService.registerAnimal(bingo);
                animalService.registerAnimal(luna);
                System.out.printf("Animals registered.%n");


                System.out.printf("Creating reports...%n");
                Report report1 = new Report(0, "Stray dog on main street", StreetName.AVENIDA_DAS_ACACIAS_DA_PENINSULA, user1, buddy);
                Report report2 = new Report(0, "Injured cat near park", StreetName.RUA_ANGELINS_DA_PENINSULA, user2, fluffy);
                Report report3 = new Report(0, "Loud barking at Oak Avenue", StreetName.AVENIDA_DOS_FLAMBOYANTS_DA_PENINSULA, user3, max);
                Report report4 = new Report(0, "Missing capybara poster", StreetName.RUA_AROEIRAS_DA_PENINSULA, user4, gary);
                Report report5 = new Report(0, "Cat stuck in tree at Maple Drive", StreetName.RUA_BAUHINEAS_DA_PENINSULA, user5, mittens);
                Report report6 = new Report(0, "Unusual animal sighting at Elm Street", StreetName.RUA_BROMELIAS_DA_PENINSULA, user6, squeaky);
                Report report7 = new Report(0, "Aggressive dog reported", StreetName.RUA_PAU_BRASIL_DA_PENINSULA, user7, fido);
                Report report8 = new Report(0, "Lost cat looking for home", StreetName.RUA_SAPUCAIAS_DA_PENINSULA, user8, daisy);
                Report report9 = new Report(0, "Friendly dog roaming freely", StreetName.AVENIDA_DAS_ACACIAS_DA_PENINSULA, user9, cooper);
                Report report10 = new Report(0, "Small capybara on the loose", StreetName.RUA_ANGELINS_DA_PENINSULA, user10, coco);

                reportService.createReport(report1);
                reportService.createReport(report2);
                reportService.createReport(report3);
                reportService.createReport(report4);
                reportService.createReport(report5);
                reportService.createReport(report6);
                reportService.createReport(report7);
                reportService.createReport(report8);
                reportService.createReport(report9);
                reportService.createReport(report10);
                System.out.printf("Reports created.%n");

                // Demonstrate Liking Animals
                System.out.printf("Adding animal likes...%n");
                animalService.userLikesAnimal(user1.getId(), fluffy.getId());
                animalService.userLikesAnimal(user2.getId(), fluffy.getId());
                animalService.userLikesAnimal(user3.getId(), fluffy.getId());
                animalService.userLikesAnimal(user4.getId(), fluffy.getId());
                animalService.userLikesAnimal(user5.getId(), fluffy.getId());

                animalService.userLikesAnimal(user1.getId(), buddy.getId());
                animalService.userLikesAnimal(user4.getId(), buddy.getId());
                animalService.userLikesAnimal(user5.getId(), buddy.getId());
                animalService.userLikesAnimal(user6.getId(), buddy.getId());

                animalService.userLikesAnimal(user2.getId(), max.getId());
                animalService.userLikesAnimal(user6.getId(), max.getId());
                animalService.userLikesAnimal(user8.getId(), max.getId());

                animalService.userLikesAnimal(user3.getId(), gary.getId());
                animalService.userLikesAnimal(user7.getId(), gary.getId());
                animalService.userLikesAnimal(user9.getId(), gary.getId());

                animalService.userLikesAnimal(user8.getId(), bubbles.getId());
                animalService.userLikesAnimal(user9.getId(), bubbles.getId());
                animalService.userLikesAnimal(user10.getId(), bubbles.getId());

                animalService.userLikesAnimal(user11.getId(), whiskers.getId());
                animalService.userLikesAnimal(user12.getId(), bella.getId());
                animalService.userLikesAnimal(user13.getId(), cooper.getId());
                animalService.userLikesAnimal(user14.getId(), felix.getId());
                animalService.userLikesAnimal(user15.getId(), zoe.getId());
                animalService.userLikesAnimal(user1.getId(), river.getId());
                animalService.userLikesAnimal(user2.getId(), coco.getId());
                animalService.userLikesAnimal(user3.getId(), bingo.getId());
                animalService.userLikesAnimal(user4.getId(), luna.getId());
                System.out.printf("Animal likes added.%n");

                // Demonstrate Liking Reports
                System.out.printf("Adding report likes...%n");
                reportService.userLikesReport(user1.getId(), report1.getId());
                reportService.userLikesReport(user2.getId(), report1.getId());
                reportService.userLikesReport(user3.getId(), report1.getId());
                reportService.userLikesReport(user4.getId(), report1.getId());

                reportService.userLikesReport(user4.getId(), report2.getId());
                reportService.userLikesReport(user5.getId(), report2.getId());
                reportService.userLikesReport(user6.getId(), report2.getId());

                reportService.userLikesReport(user6.getId(), report3.getId());
                reportService.userLikesReport(user7.getId(), report3.getId());

                reportService.userLikesReport(user8.getId(), report4.getId());
                reportService.userLikesReport(user9.getId(), report5.getId());
                reportService.userLikesReport(user10.getId(), report6.getId());
                reportService.userLikesReport(user11.getId(), report7.getId());
                reportService.userLikesReport(user12.getId(), report8.getId());
                reportService.userLikesReport(user13.getId(), report9.getId());
                reportService.userLikesReport(user14.getId(), report10.getId());
                System.out.printf("Report likes added.%n");

                System.out.printf("Initial data created successfully.%n");
            } catch (Exception e) {
                System.err.printf("Error during initial data setup: %s%n", e.getMessage());
                e.printStackTrace();
            }

            // --- Console Application Loop ---
            int choice = -1;
            do {
                ConsoleUtils.displayMenu();
                System.out.printf("Enter your choice: ");
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.printf("Invalid input. Please enter a number.%n");
                    continue;
                }

                switch (choice) {
                    case 1:
                        ConsoleUtils.displayTopReports(reportService);
                        break;
                    case 2:
                        ConsoleUtils.displayTopAnimals(animalService);
                        break;
                    case 3:
                        ConsoleUtils.displayAnimalProfile(animalService, scanner);
                        break;
                    case 0:
                        System.out.printf("Exiting application. Goodbye!%n");
                        break;
                    default:
                        System.out.printf("Invalid choice. Please try again.%n");
                }
            } while (choice != 0);

        } finally {
            if (connection != null) {
                ConnectionFactory.closeConnection(connection);
                System.out.printf("%nDatabase connection closed.%n");
            }
            scanner.close();
        }
    }
}