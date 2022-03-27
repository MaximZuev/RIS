package ru.nsu.fit.zuev.xmlparser;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    public static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/osm";
    public static final String DATABASE_USERNAME = "postgres";
    public static final String DATABASE_PASSWORD = "postgres";

    private static Connection connection;

    public static void init() throws IOException, SQLException {
        connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        scriptRunner.setRemoveCRs(true);
        InputStream inputStream = Resources.getResourceAsStream("initDB.sql");
        scriptRunner.runScript(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        inputStream.close();
        DBManager.getConnection().setAutoCommit(true);
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
