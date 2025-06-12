package utils;

import model.Animal;
import model.Capybara;
import model.Cat;
import model.Dog;
import model.Report;
import model.User;
import service.AnimalService;
import service.ReportService;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ConsoleUtils {

    public static void displayMenu() {
        System.out.printf("%n--- ANIMAL TRACKER CONSOLE ---%n");
        System.out.printf("1. See Top Reports%n");
        System.out.printf("2. See Top Animals%n");
        System.out.printf("3. View Animal Profile%n");
        System.out.printf("0. Exit%n");
        System.out.printf("------------------------------%n");
    }

    public static void displayTopReports(ReportService reportService) {
        System.out.printf("%n--- Top Reports ---%n");
        try {
            List<Report> reports = reportService.getAllReportsLazy();
            if (reports.isEmpty()) {
                System.out.printf("No reports found.%n");
                return;
            }

            reports.sort(Comparator.comparingInt(Report::getLikeCount).reversed());

            int count = 0;
            for (Report report : reports) {
                System.out.printf("Report ID: %d%n", report.getId());
                System.out.printf("  Description: %s%n", report.getDescription());
                System.out.printf("  Street: %s%n", report.getStreet().toString());
                System.out.printf("  Reported by: %s%n", (report.getCreatedBy() != null ? report.getCreatedBy().getName() : "N/A"));
                System.out.printf("  Reported Animal: %s (ID: %d)%n",
                        (report.getReportedAnimal() != null ? report.getReportedAnimal().getNickName() : "N/A"),
                        (report.getReportedAnimal() != null ? report.getReportedAnimal().getId() : 0));
                System.out.printf("  Likes: %d%n", report.getLikeCount());
                System.out.printf("---%n");
                count++;
                if (count >= 5) break;
            }
        } catch (Exception e) {
            System.err.printf("Error displaying top reports: %s%n", e.getMessage());
        }
    }

    public static void displayTopAnimals(AnimalService animalService) {
        System.out.printf("%n--- Top Animals ---%n");
        try {
            List<Animal> animals = animalService.getAllAnimalsEager();
            if (animals.isEmpty()) {
                System.out.printf("No animals found.%n");
                return;
            }

            animals.sort(Comparator.comparingInt(Animal::getLikeCount).reversed());

            int count = 0;
            for (Animal animal : animals) {
                System.out.printf("Animal ID: %d%n", animal.getId());
                System.out.printf("  Nickname: %s%n", animal.getNickName());
                System.out.printf("  Species: %s%n", animal.getClass().getSimpleName());
                System.out.printf("  Color: %s%n", animal.getColor());
                System.out.printf("  Size: %s%n", animal.getSize());
                System.out.printf("  Likes: %d%n", animal.getLikeCount());
                System.out.printf("---%n");
                count++;
                if (count >= 5) break;
            }
        } catch (Exception e) {
            System.err.printf("Error displaying top animals: %s%n", e.getMessage());
        }
    }

    public static void displayAnimalProfile(AnimalService animalService, Scanner scanner) {
        System.out.printf("%n--- Animal Profile ---%n");
        System.out.printf("Enter Animal ID: ");
        int animalId;
        try {
            animalId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.printf("Invalid ID. Please enter a number.%n");
            return;
        }

        try {
            Animal animal = animalService.getAnimalById(animalId);
            if (animal == null) {
                System.out.printf("Animal with ID %d not found.%n", animalId);
                return;
            }

            System.out.printf("%n--- Profile for %s (ID: %d) ---%n", animal.getNickName(), animal.getId());
            System.out.printf("Species: %s%n", animal.getClass().getSimpleName());
            System.out.printf("Color: %s%n", animal.getColor());
            System.out.printf("Size: %s%n", animal.getSize());

            if (animal instanceof Cat cat) {
                System.out.printf("  Breed: %s%n", cat.getBreed());
                System.out.printf("  Scratches People: %b%n", cat.isScratchesPeople());
                System.out.printf("  Likes being pet: %b%n", cat.isLikesBeingPet());
                System.out.printf("  Is Castrated: %b%n", cat.isCastrated());
                System.out.printf("  Is Vaccinated: %b%n", cat.isVaccinated());
            } else if (animal instanceof Dog dog) {
                System.out.printf("  Breed: %s%n", dog.getBreed());
                System.out.printf("  Tail Length: %s%n", dog.getTailLength());
                System.out.printf("  Likes to chase cars: %b%n", dog.isChasesCars());
                System.out.printf("  Likes being pet: %b%n", dog.isLikesBeingPet());
                System.out.printf("  Is Castrated: %b%n", dog.isCastrated());
                System.out.printf("  Is Vacinated: %b%n", dog.isVaccinated());
            } else if (animal instanceof Capybara capybara) {
                System.out.printf("  Social group size): %d%n", capybara.getSocialGroupSize());
                System.out.printf("  Likes eating grass: %b%n", capybara.isEatsGrass());
            }

            System.out.printf("Liked by %d users:%n", animal.getLikeCount());
            if (animal.getLikeCount() > 0) {
                for (User user : animal.getLikedByUsers()) {
                    System.out.printf("  - %s (ID: %d)%n", user.getName(), user.getId());
                }
            } else {
                System.out.printf("  No likes yet.%n");
            }
            System.out.printf("----------------------------%n");

        } catch (Exception e) {
            System.err.printf("Error viewing animal profile: %s%n", e.getMessage());
        }
    }
}