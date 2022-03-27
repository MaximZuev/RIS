package ru.nsu.fit.zuev.xmlparser;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

import generated.Node;

import java.io.FileInputStream;
import java.io.IOException;

public class XMLParser implements AutoCloseable {
    private XMLStreamReader reader;
    private final Unmarshaller nodeUnmarshaller;
    private static final Logger LOGGER = LoggerFactory.getLogger(XMLParser.class);
    private static final String NODE = "node";
    public static final String TARGET_NAMESPACE = "http://openstreetmap.org/osm/0.6";

    public XMLParser(String filename) throws XMLStreamException, IOException, JAXBException {
        BZip2CompressorInputStream in = new BZip2CompressorInputStream(new FileInputStream(filename));
        XMLInputFactory FACTORY = XMLInputFactory.newInstance();
        reader = FACTORY.createXMLStreamReader(in);
        reader = new XsiTypeReader(reader);
        JAXBContext jc = JAXBContext.newInstance(Node.class);
        nodeUnmarshaller = jc.createUnmarshaller();
    }

    public Node getNextNode(){
        try {
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamReader.START_ELEMENT && reader.getLocalName().equals(NODE)) {
                    return nodeUnmarshaller.unmarshal(reader, Node.class).getValue();
                }
            }
        } catch (XMLStreamException | JAXBException e){
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    /*public void findAllElementsWithAttribute(LinkedHashMap<String, Integer> items, int event, String element, String attributeName) throws JAXBException {
        if (event == XMLEvent.START_ELEMENT && element.equals(reader.getLocalName())){
            //Node item = nodeUnmarshaller.unmarshal(reader, Node.class).getValue();
            //items.put(item, items.get(item) + 1);
            int attributesAmount = reader.getAttributeCount();
            for (int i = 0; i < attributesAmount; ++i) {
                if (attributeName.equals(reader.getAttributeLocalName(i))) {
                    String item = reader.getAttributeValue(i);
                    if (items.containsKey(item)) {
                        items.put(item, items.get(item) + 1);
                    } else {
                        items.put(item, 1);
                    }
                }
            }
        }
    }*/

    @Override
    public void close() throws Exception {
        if (reader != null) {
            reader.close();
        }
    }

    private static class XsiTypeReader extends StreamReaderDelegate {

        public XsiTypeReader(XMLStreamReader reader) {
            super(reader);
        }

        @Override
        public String getAttributeNamespace(int index) {
            return super.getAttributeNamespace(index);
        }

        @Override
        public String getNamespaceURI() {
            return TARGET_NAMESPACE;
        }
    }
}
