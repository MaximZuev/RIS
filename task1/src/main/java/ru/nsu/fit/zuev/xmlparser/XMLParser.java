package ru.nsu.fit.zuev.xmlparser;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.util.HashMap;
import java.util.Map;

public class XMLParser {
    private static Logger LOGGER = LoggerFactory.getLogger(XMLParser.class);
    private static QName USER = new QName("user");
    private static QName CHANGE_SET = new QName("changeset");
    private static QName KEY = new QName("k");

    public static void parse(BZip2CompressorInputStream in, long length) throws XMLStreamException {
        Map<String, Map<String, Integer>> changeStat = new HashMap<>();
        Map<String, Integer> keyStat = new HashMap<>();

        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = xmlInputFactory.createXMLEventReader(in);

        long start = System.currentTimeMillis();

        while (reader.hasNext() && in.getBytesRead() < length) {
            XMLEvent nextEvent = reader.nextEvent();
            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                switch (startElement.getName().getLocalPart()) {
                    case "node" -> {
                        var attrUser = startElement.getAttributeByName(USER);
                        if (attrUser == null) {
                            continue;
                        }
                        var attrChangeSet = startElement.getAttributeByName(CHANGE_SET);
                        if (attrChangeSet == null) {
                            continue;
                        }
                        String user = attrUser.getValue();
                        String changeSet = attrChangeSet.getValue();
                        var userStat = changeStat.computeIfAbsent(user, k -> new HashMap<>());
                        userStat.merge(changeSet, 1, Integer::sum);
                    }
                    case "tag" -> {
                        var attrKey = startElement.getAttributeByName(KEY);
                        if (attrKey == null) {
                            continue;
                        }
                        String key = attrKey.getValue();
                        keyStat.merge(key, 1, Integer::sum);
                    }
                }

            }
        }
        long time = System.currentTimeMillis() - start;
        LOGGER.info("Spend time: " + time);
        LOGGER.info("Speed: " + in.getBytesRead() / time * 1000.0 / 1024 / 1024);

        changeStat.entrySet().stream().sorted((a, b) -> {
            int cmp = Integer.compare(b.getValue().size(), a.getValue().size());
            return cmp == 0 ? a.getKey().compareTo(b.getKey()) : cmp;
        }).forEach(e -> System.out.printf("%s\t%s\t%s\n",
                e.getKey(),
                e.getValue().size(),
                e.getValue()
                        .values()
                        .stream()
                        .mapToInt(Integer::intValue)
                        .sum()
        ));
        keyStat.entrySet().stream().sorted((a, b) -> {
            int cmp = Integer.compare(b.getValue(), a.getValue());
            return cmp == 0 ? a.getKey().compareTo(b.getKey()) : cmp;
        }).forEach(e -> System.out.printf("%s\t%s\n",
                e.getKey(),
                e.getValue()
        ));
    }
}
