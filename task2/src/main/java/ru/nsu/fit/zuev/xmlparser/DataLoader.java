package ru.nsu.fit.zuev.xmlparser;

import generated.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.fit.zuev.xmlparser.dao.*;

import java.io.IOException;
import java.sql.SQLException;

public class DataLoader {
    private static Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);
    private static final int MAX_NODES = 10000;

    public void insert(String filename, String insertType) throws SQLException, IOException {
        DBManager.init();
        long totalTime = 0;
        int tags = 0;
        int nodes = 0;
        DAOFactory<Node> factory = new NodeDAOFactory();
        DAO<Node> dao = factory.getDao(insertType);
        try(XMLParser parser = new XMLParser(filename)){
            Node node = parser.getNextNode();
            while (node != null && tags < MAX_NODES) {
                long start = System.currentTimeMillis();
                dao.insert(node);
                long end = System.currentTimeMillis();
                totalTime += end - start;
                nodes += 1;
                tags += node.getTag().size();
                node = parser.getNextNode();
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        if (dao instanceof BatchNodeDAO) {
            BatchNodeDAO batchNodeDAO = (BatchNodeDAO) dao;
            long start = System.currentTimeMillis();
            batchNodeDAO.flushBatch();
            long end = System.currentTimeMillis();
            totalTime += end - start;
        }

        LOGGER.info(String.format(insertType + " INSERT TIME = %s (rows/sec)", (nodes + tags) / (totalTime / 1000.0)));
        DBManager.closeConnection();
    }
}
