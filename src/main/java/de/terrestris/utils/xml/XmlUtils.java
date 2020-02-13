package de.terrestris.utils.xml;

import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.stax2.XMLStreamWriter2;
import org.w3c.dom.Node;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Utilities to work with StAX streams and DOM documents.
 */
public class XmlUtils {

    private XmlUtils() {
        // prevent instantiation
    }

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
            if (!reader.hasNext()) {
                break;
            }
        }
    }

    /**
     * Writes a simple element with text content.
     *
     * @param writer    the XMLStreamWriter
     * @param namespace the namespace
     * @param localName the local name
     * @param content   the text content
     * @throws XMLStreamException if anything goes wrong
     */
    public static void writeSimpleElement(XMLStreamWriter writer, String namespace, String localName, String content)
            throws XMLStreamException {
        writer.writeStartElement(namespace, localName);
        writer.writeCharacters(content);
        writer.writeEndElement();
    }

    /**
     * Copy a subtree into the given writer. Needs API version 2 in order to use #copyEventFromReader.
     * The reader must be on a START_ELEMENT event and will be on the corresponding END_ELEMENT event after copying.
     * Please note that the start element occurring in the subtree will most likely lead to broken output.
     *
     * @param reader the reader to read from
     * @param writer the writer to write to
     * @throws XMLStreamException if anything goes wrong
     */
    public static void copySubtree(XMLStreamReader2 reader, XMLStreamWriter2 writer) throws XMLStreamException {
        String localName = reader.getLocalName();
        String ns = reader.getNamespaceURI();
        while (!(reader.isEndElement() && reader.getLocalName().equals(localName) && reader.getNamespaceURI().equals(ns))) {
            writer.copyEventFromReader(reader, true);
            reader.next();
        }
    }

    /**
     * Use the default transformer to serialize a DOM node to string.
     *
     * @param node the DOM node to serialize
     * @return the string with the serialized XML
     * @throws TransformerException if the transformation failed
     * @throws IOException if the transformation failed
     */
    public static String domToString(Node node) throws TransformerException, IOException {
        TransformerFactory factory = TransformerFactory.newDefaultInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            transformer.transform(new DOMSource(node), new StreamResult(bos));
            return new String(bos.toByteArray(), StandardCharsets.UTF_8);
        }
    }

}
