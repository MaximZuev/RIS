package ru.nsu.fit.zuev.xmlparser.dao;

import generated.Node;
import generated.Tag;
import ru.nsu.fit.zuev.xmlparser.DBManager;

import java.sql.SQLException;

public class QueryNodeDAO extends DAO<Node> {
    @Override
    public void insert(Node obj) throws SQLException {
        DBManager.getConnection().createStatement().executeUpdate(String.format(
                "INSERT INTO Nodes VALUES (%s, %s, %s, $$%s$$, %s, %s, %s, $$%s$$)",
                obj.getId(), obj.getLat(), obj.getLon(), obj.getUser(), obj.getUid(), obj.getVersion(), obj.getChangeset(), obj.getTimestamp())
        );
        for (Tag tag : obj.getTag()) {
            DBManager.getConnection().createStatement().executeUpdate(String.format(
                    "INSERT INTO Tags VALUES (%s, $$%s$$, $$%s$$)",
                    obj.getId(), tag.getK(), tag.getV())
            );
        }
    }
}