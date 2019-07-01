package de.terrestris.utils.xml

import org.codehaus.stax2.XMLInputFactory2
import org.codehaus.stax2.XMLOutputFactory2
import org.codehaus.stax2.XMLStreamReader2
import org.codehaus.stax2.XMLStreamWriter2
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLOutputFactory

class XmlUtilsTest {

    private var inFactory: XMLInputFactory? = null

    private var outFactory: XMLOutputFactory? = null

    @Before
    fun setUp() {
        inFactory = XMLInputFactory2.newInstance()
        outFactory = XMLOutputFactory2.newInstance()
    }

    @Test
    fun testSkipToElement() {
        val `in` = javaClass.getResourceAsStream("test.xml")
        val reader = inFactory!!.createXMLStreamReader(`in`)
        XmlUtils.skipToElement(reader, "targetElem")
        Assert.assertTrue(reader.isStartElement)
        Assert.assertEquals("targetElem", reader.localName)
    }

    @Test
    fun testSkipToOneOf() {
        val `in` = javaClass.getResourceAsStream("test.xml")
        val reader = inFactory!!.createXMLStreamReader(`in`)
        XmlUtils.skipToOneOf(reader, "otherElem", "targetElem")
        Assert.assertTrue(reader.isStartElement)
        Assert.assertEquals("otherElem", reader.localName)
    }

    @Test
    fun testSkipToOneOfNotFound() {
        val `in` = javaClass.getResourceAsStream("test.xml")
        val reader = inFactory!!.createXMLStreamReader(`in`)
        XmlUtils.skipToOneOf(reader, "notFound")
        Assert.assertFalse(reader.hasNext())
    }

    @Test
    fun testWriteSimpleElem() {
        val out = ByteArrayOutputStream()
        val writer = outFactory!!.createXMLStreamWriter(out)
        outFactory!!.setProperty(XMLOutputFactory2.IS_REPAIRING_NAMESPACES, true)
        writer.setDefaultNamespace("https://terrestris.de")
        writer.writeStartDocument()
        writer.writeStartElement("https://terrestris.de", "rootElem")
        XmlUtils.writeSimpleElement(writer, "https://terrestris.de", "subElem", "the text")
        writer.writeEndElement()
        writer.close()
        val result = String(out.toByteArray())
        Assert.assertEquals("<?xml version='1.0' encoding='UTF-8'?><rootElem><subElem>the text</subElem></rootElem>", result)
    }

    @Test
    fun testCopySubtree() {
        val `in` = javaClass.getResourceAsStream("copytest.xml")
        val reader = inFactory!!.createXMLStreamReader(`in`) as XMLStreamReader2
        XmlUtils.skipToElement(reader, "targetElem")
        val out = ByteArrayOutputStream()
        outFactory!!.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, true)
        outFactory!!.setProperty(XMLOutputFactory2.P_AUTOMATIC_NS_PREFIX, true)
        val writer = outFactory!!.createXMLStreamWriter(out) as XMLStreamWriter2
        XmlUtils.copySubtree(reader, writer)
        writer.close()
        val expected = ("<targetElem xmlns=\"http://terrestris.de/1\">\n"
                + "            <subElemWithText xmlns=\"http://terrestris.de/1\">some text</subElemWithText>\n"
                + "            <subElemWithAttr xmlns=\"http://terrestris.de/1\" attr=\"123\"/>\n"
                + "            <subElemDifferentNamespace xmlns=\"http://terrestris.de/2\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:href=\"http://www.terrestris.de\"/>\n"
                + "        </targetElem>")
        val result = String(out.toByteArray(), StandardCharsets.UTF_8)
        Assert.assertEquals(expected, result)
    }

}
