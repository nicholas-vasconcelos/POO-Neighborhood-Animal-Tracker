package model;

import enums.StreetName;
import interfaces.Likeable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Report implements Likeable {

    private int id;
    private String description;
    private StreetName street;
    private Set<User> likedBy;
    private User createdBy;
    private Animal reportedAnimal;

    public Report(int id, String description, StreetName street,
                  User createdBy, Animal reportedAnimal) {
        this.id = id;
        this.description = description;
        this.street = street;
        this.createdBy = createdBy;
        this.reportedAnimal = reportedAnimal;
        this.likedBy = new HashSet<>();
    }


    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StreetName getStreet() {
        return street;
    }

    public void setStreet(StreetName street) {
        this.street = street;
    }

    public Set<User> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(Set<User> likedBy) {
        this.likedBy = likedBy;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Animal getReportedAnimal() {
        return reportedAnimal;
    }

    public void setReportedAnimal(Animal reportedAnimal) {
        this.reportedAnimal = reportedAnimal;
    }


    // Overrides
    @Override
    public void like(User user) {
        if (user != null) {
            this.likedBy.add(user);
        }
    }

    @Override
    public void unlike(User user) {
        if (user != null) {
            this.likedBy.remove(user);
        }
    }

    @Override
    public int getLikeCount() {
        return this.likedBy.size();
    }

    @Override
    public Set<User> getLikedByUsers() {
        return new HashSet<>(this.likedBy);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Report report = (Report) obj;
        return id == report.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
