package de.terrestris.utils.xml;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.stream.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class XmlUtilsTest {

    private XMLInputFactory inFactory;

    private XMLOutputFactory outFactory;

    @Before
    public void setUp() {
        inFactory = XMLInputFactory.newFactory();
        outFactory = XMLOutputFactory.newFactory();
    }

    @Test
    public void testSkipToElement() throws XMLStreamException {
        InputStream in = getClass().getResourceAsStream("test.xml");
        XMLStreamReader reader = inFactory.createXMLStreamReader(in);
        XmlUtils.skipToElement(reader, "targetElem");
        Assert.assertTrue(reader.isStartElement());
        Assert.assertEquals("targetElem", reader.getLocalName());
    }

    @Test
    public void testSkipToOneOf() throws XMLStreamException {
        InputStream in = getClass().getResourceAsStream("test.xml");
        XMLStreamReader reader = inFactory.createXMLStreamReader(in);
        XmlUtils.skipToOneOf(reader, "otherElem", "targetElem");
        Assert.assertTrue(reader.isStartElement());
        Assert.assertEquals("otherElem", reader.getLocalName());
    }

    @Test
    public void testWriteSimpleElem() throws XMLStreamException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLStreamWriter writer = outFactory.createXMLStreamWriter(out);
        writer.setDefaultNamespace("https://terrestris.de");
        writer.writeStartDocument();
        writer.writeStartElement("https://terrestris.de", "rootElem");
        XmlUtils.writeSimpleElement(writer, "https://terrestris.de", "subElem", "the text");
        writer.writeEndElement();
        String result = new String(out.toByteArray());
        Assert.assertEquals("<?xml version=\"1.0\" ?><rootElem><subElem>the text</subElem></rootElem>", result);
    }

}
