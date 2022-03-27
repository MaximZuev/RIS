package ru.nsu.fit.zuev.xmlparser;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.io.*;

public class Main {
    private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws ParseException {
        if (args.length < 1) {
            throw new IllegalArgumentException("Wrong argument count");
        }

        LOGGER.info("start");

        Options options = new Options();
        options.addOption("i", "in", true, "Xml input");
        options.addOption("o", "out", true, "Xml output");
        options.addOption("l", "length", true, "Read N bytes");
        DefaultParser parser = new DefaultParser();
        var cmdLine = parser.parse(options, args);
        String inPath = cmdLine.getOptionValue("i");
        String outPath = cmdLine.getOptionValue("o");
        long length = Long.parseLong(cmdLine.getOptionValue("l", "" + Long.MAX_VALUE));

        try (var in = new BZip2CompressorInputStream(new BufferedInputStream(new FileInputStream(inPath)))) {
            LOGGER.info("Reading xml from: " + inPath);
            if (outPath != null) {
                try (var out = new FileOutputStream(outPath)) {
                    IOUtils.copyRange(in, length, out);
                    LOGGER.info("Writing xml to: " + outPath);
                }
            } else {
                XMLParser.parse(in, length);
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("File not found", e);
        } catch (IOException e) {
            LOGGER.error("File reading error", e);
        } catch (XMLStreamException e) {
            LOGGER.error("XML reading error", e);
        }
        LOGGER.info("end");
    }
}
