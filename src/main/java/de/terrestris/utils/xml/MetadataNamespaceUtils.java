package de.terrestris.utils.xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * This class includes some common namespaces related to ISO metadata files as constants, as well as
 * utility functions to set the prefixes and write the namespace bindings.
 */
public class MetadataNamespaceUtils {

  public static final String GMD = "http://www.isotc211.org/2005/gmd";

  public static final String GCO = "http://www.isotc211.org/2005/gco";

  public static final String GMX = "http://www.isotc211.org/2005/gmx";

  public static final String XLINK = "http://www.w3.org/1999/xlink";

  public static final String GML = "http://www.opengis.net/gml/3.2";

  public static final String SRV = "http://www.isotc211.org/2005/srv";

  /**
   * Set the prefixes for the ISO metadata namespaces.
   * @param writer the writer to set the bindings to
   * @throws XMLStreamException if anything goes wrong
   */
  public static void setNamespaceBindings(XMLStreamWriter writer) throws XMLStreamException {
    writer.setPrefix("gmd", GMD);
    writer.setPrefix("gco", GCO);
    writer.setPrefix("gmx", GMX);
    writer.setPrefix("xlink", XLINK);
    writer.setPrefix("gml", GML);
    writer.setPrefix("srv", SRV);
  }

  /**
   * Writes the namespace bindings for the ISO metadata namespaces.
   * @param writer the writer to write the bindings to
   * @throws XMLStreamException in case you're not on a start element
   */
  public static void writeNamespaceBindings(XMLStreamWriter writer) throws XMLStreamException {
    writer.writeNamespace("gmd", GMD);
    writer.writeNamespace("gco", GCO);
    writer.writeNamespace("gmx", GMX);
    writer.writeNamespace("xlink", XLINK);
    writer.writeNamespace("gml", GML);
    writer.writeNamespace("srv", SRV);
  }

}
