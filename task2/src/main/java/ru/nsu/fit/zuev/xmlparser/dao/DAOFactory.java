package ru.nsu.fit.zuev.xmlparser.dao;

import java.sql.SQLException;

public abstract class DAOFactory<T> {
    abstract public DAO<T> getDao(String type) throws SQLException;
}
