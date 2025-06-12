package model;

import java.util.Objects;

public class User {

    private int id;
    private String name;
    private String condominium;

    public User(int id, String name, String condominium) {
        this.id = id;
        this.name = name;
        this.condominium = condominium;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCondominium() {
        return condominium;
    }

    public void setCondominium(String condominium) {
        this.condominium = condominium;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
