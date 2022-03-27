package ru.nsu.fit.zuev.xmlparser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.sql.SQLException;

public class Main {
    private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws ParseException, SQLException, IOException {
        if (args.length < 1) {
            throw new IllegalArgumentException("Wrong argument count");
        }

        LOGGER.info("start");

        Options options = new Options();
        options.addOption("i", "in", true, "Xml input");
        DefaultParser parser = new DefaultParser();
        CommandLine cmdLine = parser.parse(options, args);
        String inPath = cmdLine.getOptionValue("i");

        DataLoader dataLoader = new DataLoader();
        dataLoader.insert(inPath, "QUERY");
        dataLoader.insert(inPath, "PREPARED_STATEMENT");
        dataLoader.insert(inPath, "BATCH");

        LOGGER.info("end");
    }
}
