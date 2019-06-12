package de.terrestris.utils.xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * Utilities to work with StAX streams.
 */
public class XmlUtils {

    /**
     * Skip the stream to the next element with the given local name. Precondition can be anything, postcondition will be
     * START_ELEMENT.
     *
     * @param reader    the XML stream reader
     * @param localName the desired local name
     * @throws XMLStreamException if anything goes wrong
     */
    public static void skipToElement(XMLStreamReader reader, String localName) throws XMLStreamException {
        while (!(reader.isStartElement() && reader.getLocalName().equals(localName))) {
            reader.next();
        }
    }

    /**
     * Skips the stream to the first occurrence of any of the given local names. Precondition can be anything, postcondition
     * will be START_ELEMENT.
     *
     * @param reader     the XML stream reader
     * @param localNames the local names
     * @throws XMLStreamException if anything goes wrong
     */
    public static void skipToOneOf(XMLStreamReader reader, String... localNames) throws XMLStreamException {
        List<String> list = Arrays.asList(localNames);
        while (!(reader.isStartElement() && list.contains(reader.getLocalName()))) {
            reader.next();
        }
    }

}
