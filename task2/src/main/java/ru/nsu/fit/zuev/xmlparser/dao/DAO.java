package ru.nsu.fit.zuev.xmlparser.dao;

import java.sql.SQLException;
import java.util.ArrayList;

public abstract class DAO<T> {
    public abstract void insert(T obj) throws SQLException;
    //public abstract T getById(int id);
    //public abstract void update(T obj);
    //public abstract void delete(T obj);
    //public abstract ArrayList<T> getAll();
}
