package ru.nsu.fit.zuev.xmlparser.dao;

import generated.Node;
import generated.Tag;
import ru.nsu.fit.zuev.xmlparser.DBManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PreparedStatementNodeDAO extends DAO<Node> {
    private static final String INSERT_NODE = "INSERT INTO Nodes VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_TAG = "INSERT INTO Tags VALUES (?, ?, ?)";
    private final PreparedStatement node_statement = DBManager.getConnection().prepareStatement(INSERT_NODE);
    private final PreparedStatement tag_statement = DBManager.getConnection().prepareStatement(INSERT_TAG);

    public PreparedStatementNodeDAO() throws SQLException {
    }

    @Override
    public void insert(Node obj) throws SQLException {
        node_statement.setLong(1, obj.getId().longValue());
        node_statement.setDouble(2, obj.getLat());
        node_statement.setDouble(3, obj.getLon());
        node_statement.setString(4, obj.getUser());
        node_statement.setLong(5, obj.getUid().longValue());
        node_statement.setLong(6, obj.getVersion().longValue());
        node_statement.setLong(7, obj.getChangeset().longValue());
        node_statement.setTimestamp(8, new Timestamp(obj.getTimestamp().toGregorianCalendar().getTimeInMillis()));
        node_statement.executeUpdate();

        for (Tag tag : obj.getTag()) {
            tag_statement.setLong(1, obj.getId().longValue());
            tag_statement.setString(2, tag.getK());
            tag_statement.setString(3, tag.getV());
            tag_statement.executeUpdate();
        }
    }
}