package dao;

import java.util.ArrayList;

public interface BaseDAO<T> {

    public void save(T object); // For saving an object
    public T findById(int id); // For findinb by ID
    public ArrayList<T> findAllLazyLoading();
    public ArrayList<T> findAllEagerLoading();
    public void update(T object); // For updating an object
    public void delete(int id); // For deleting an object by ID
}