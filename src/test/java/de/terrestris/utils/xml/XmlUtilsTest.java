package de.terrestris.utils.xml;

import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLOutputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.stax2.XMLStreamWriter2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.stream.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class XmlUtilsTest {

    private XMLInputFactory inFactory;

    private XMLOutputFactory outFactory;

    @Before
    public void setUp() {
        inFactory = XMLInputFactory2.newFactory();
        outFactory = XMLOutputFactory2.newFactory();
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
        outFactory.setProperty(XMLOutputFactory2.IS_REPAIRING_NAMESPACES, true);
        writer.setDefaultNamespace("https://terrestris.de");
        writer.writeStartDocument();
        writer.writeStartElement("https://terrestris.de", "rootElem");
        XmlUtils.writeSimpleElement(writer, "https://terrestris.de", "subElem", "the text");
        writer.writeEndElement();
        writer.close();
        String result = new String(out.toByteArray());
        Assert.assertEquals("<?xml version='1.0' encoding='UTF-8'?><rootElem><subElem>the text</subElem></rootElem>", result);
    }

    @Test
    public void testCopySubtree() throws XMLStreamException {
        InputStream in = getClass().getResourceAsStream("copytest.xml");
        XMLStreamReader2 reader = (XMLStreamReader2) inFactory.createXMLStreamReader(in);
        XmlUtils.skipToElement(reader, "targetElem");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        outFactory.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, true);
        outFactory.setProperty(XMLOutputFactory2.P_AUTOMATIC_NS_PREFIX, true);
        XMLStreamWriter2 writer = (XMLStreamWriter2) outFactory.createXMLStreamWriter(out);
        XmlUtils.copySubtree(reader, writer);
        writer.close();
        String expected = "<targetElem xmlns=\"http://terrestris.de/1\">\n"
                + "            <subElemWithText xmlns=\"http://terrestris.de/1\">some text</subElemWithText>\n"
                + "            <subElemWithAttr xmlns=\"http://terrestris.de/1\" attr=\"123\"/>\n"
                + "            <subElemDifferentNamespace xmlns=\"http://terrestris.de/2\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:href=\"http://www.terrestris.de\"/>\n"
                + "        </targetElem>";
        String result = new String(out.toByteArray(), StandardCharsets.UTF_8);
        Assert.assertEquals(expected, result);
    }

}
