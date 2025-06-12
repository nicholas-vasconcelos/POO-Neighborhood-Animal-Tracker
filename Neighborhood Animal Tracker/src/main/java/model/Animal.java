package model;

import interfaces.Likeable;
import enums.Color;
import enums.Size;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class Animal implements Likeable {

    protected int id;
    protected String nickName;
    protected Color color;
    protected Size size;
    protected Set<User> likedBy;

    public Animal(int id, String nickName, Color color, Size size) {
        this.id = id;
        this.nickName = nickName;
        this.color = color;
        this.size = size;
        this.likedBy = new HashSet<>();
    }


    // Getter and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
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
        Animal animal = (Animal) obj;
        return id == animal.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void setLikedBy(Set<User> users) {
        for (User user: users) {
            this.like(user);
        }
    }
}
