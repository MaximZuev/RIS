package ru.nsu.fit.zuev.xmlparser.dao;

import generated.Node;

import java.sql.SQLException;

public class NodeDAOFactory extends DAOFactory<Node> {
    @Override
    public DAO<Node> getDao(String type) throws SQLException {
        if (type.equals("QUERY")) return new QueryNodeDAO();
        if (type.equals("PREPARED_STATEMENT")) return new PreparedStatementNodeDAO();
        if (type.equals("BATCH")) return new BatchNodeDAO();
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
