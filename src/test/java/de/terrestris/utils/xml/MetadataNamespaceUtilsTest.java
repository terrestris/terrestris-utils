package de.terrestris.utils.xml;

import org.codehaus.stax2.XMLOutputFactory2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static de.terrestris.utils.xml.MetadataNamespaceUtils.*;

public class MetadataNamespaceUtilsTest {

  private XMLOutputFactory outFactory;

  @Before
  public void setUp() {
    outFactory = XMLOutputFactory2.newFactory();
  }

  @Test
  public void testWriteNamespaces() throws XMLStreamException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    XMLStreamWriter writer = outFactory.createXMLStreamWriter(out);
    setNamespaceBindings(writer);
    writer.writeStartDocument();
    writer.writeStartElement(GMD, "MD_Metadata");
    writeNamespaceBindings(writer);
    writer.writeEndElement();
    writer.close();
    String result = out.toString(StandardCharsets.UTF_8);
    Assert.assertEquals("<?xml version='1.0' encoding='UTF-8'?><gmd:MD_Metadata xmlns:gmd=\"http://www.isotc211.org/2005/gmd\" xmlns:gco=\"http://www.isotc211.org/2005/gco\" xmlns:gmx=\"http://www.isotc211.org/2005/gmx\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:gml=\"http://www.opengis.net/gml/3.2\" xmlns:srv=\"http://www.isotc211.org/2005/srv\"/>", result);
  }

}
