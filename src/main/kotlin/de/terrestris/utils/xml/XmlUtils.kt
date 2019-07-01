package de.terrestris.utils.xml

import org.codehaus.stax2.XMLStreamReader2
import org.codehaus.stax2.XMLStreamWriter2

import javax.xml.stream.XMLStreamReader
import javax.xml.stream.XMLStreamWriter

/**
 * Utilities to work with StAX streams.
 */
object XmlUtils {

    /**
     * Skip the stream to the next element with the given local name. Precondition can be anything, postcondition will be
     * START_ELEMENT.
     *
     * @param reader    the XML stream reader
     * @param localName the desired local name
     */
    fun skipToElement(reader: XMLStreamReader, localName: String) {
        while (!(reader.isStartElement && reader.localName == localName)) {
            reader.next()
        }
    }

    /**
     * Skips the stream to the first occurrence of any of the given local names. Precondition can be anything, postcondition
     * will be START_ELEMENT.
     *
     * @param reader     the XML stream reader
     * @param localNames the local names
     */
    fun skipToOneOf(reader: XMLStreamReader, vararg localNames: String) {
        val list = listOf(*localNames)
        while (!(reader.isStartElement && list.contains(reader.localName))) {
            reader.next()
            if (!reader.hasNext()) {
                break
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
     */
    fun writeSimpleElement(writer: XMLStreamWriter, namespace: String, localName: String, content: String) {
        writer.writeStartElement(namespace, localName)
        writer.writeCharacters(content)
        writer.writeEndElement()
    }

    /**
     * Copy a subtree into the given writer. Needs API version 2 in order to use #copyEventFromReader.
     * The reader must be on a START_ELEMENT event and will be on the corresponding END_ELEMENT event after copying.
     * Please note that the start element occurring in the subtree will most likely lead to broken output.
     *
     * @param reader the reader to read from
     * @param writer the writer to write to
     */
    fun copySubtree(reader: XMLStreamReader2, writer: XMLStreamWriter2) {
        val localName = reader.localName
        val ns = reader.namespaceURI
        while (!(reader.isEndElement && reader.localName == localName && reader.namespaceURI == ns)) {
            writer.copyEventFromReader(reader, true)
            reader.next()
        }
    }

}
