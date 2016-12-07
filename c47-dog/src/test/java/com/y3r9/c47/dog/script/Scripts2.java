package com.y3r9.c47.dog.script;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

/**
 * The class Scripts2.
 *
 * @version 1.0
 */
public final class Scripts2 {

    @Test
    public void convertDcdDoc()
            throws IOException, ParserConfigurationException, TransformerConfigurationException,
            InstantiationException, IllegalAccessException, ClassNotFoundException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
        transformer.setOutputProperty(OutputKeys.INDENT, "true");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
        final DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
        final LSSerializer writer = impl.createLSSerializer();
        writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
        writer.getDomConfig().setParameter("xml-declaration", true);

        Path folder = Paths.get("D:\\APP\\netis\\dcd\\dcd-doc\\src\\main\\resources\\register");
        Path toFolder = Paths.get("D:\\TMP\\register");
        try (Stream<Path> stream = Files.list(folder)) {
            stream.forEach(file -> {
                try {
                    final Document doc = builder.parse(file.toFile());
                    final Element register = doc.getDocumentElement();
                    final Node property = findChildNode(register, "property");
                    final Node metaHelp = findChildNode(register, "meta-help");
                    final Node name = findChildNode(register, "name");
                    if (name == null) {
                        return;
                    }
                    final String protoName = name.getTextContent().trim().toLowerCase();

                    if (property != null) {
                        register.removeChild(property);
                    }
                    if (metaHelp != null) {
                        register.removeChild(metaHelp);
                    }

                    Element protocol = doc.createElement("protocol");
                    register.appendChild(protocol);
                    protocol.setAttribute("id", protoName);
                    protocol.setAttribute("baseProtocol", "");
                    if (property != null) {
                        final NodeList attrs = property.getChildNodes();
                        List<Node> toRemoves = new ArrayList<Node>();
                        int attrCnt = 0;
                        for (int i = 0; i < attrs.getLength(); i++) {
                            Node attr = attrs.item(i);
                            if (attr.getNodeType() != Node.ELEMENT_NODE) {
                                continue;
                            }
                            attrCnt++;
                            if (StringUtils.isEmpty(
                                    attr.getAttributes().getNamedItem("key").getNodeValue())) {
                                toRemoves.add(attr);
                            }
                        }
                        if (toRemoves.size() < attrCnt) {
                            for (Node toRemove : toRemoves) {
                                property.removeChild(toRemove);
                            }
                            protocol.appendChild(property);
                        }
                    }
                    if (metaHelp != null && metaHelp.getChildNodes().getLength() > 0) {
                        protocol.appendChild(metaHelp);
                    }
                    Element recordField = doc.createElement("recordField");
                    recordField.setAttribute("allItem", "true");
                    if (protoName.endsWith("xml")) {
                        recordField.setAttribute("template", "xpath");
                    } else if (protoName.endsWith("keyvalue xml")) {
                        recordField.setAttribute("template", "kvxpath");
                    } else if (protoName.endsWith("json")) {
                        recordField.setAttribute("template", "json");
                    } else if (protoName.endsWith("jsoup")) {
                        recordField.setAttribute("template", "jsoup");
                    }
                    protocol.appendChild(recordField);

                    final Path toFile = toFolder.resolve(file.getFileName());

                    // final String out = writer.writeToString(doc);
                    // Files.write(toFile, out.getBytes(StandardCharsets.UTF_8));

                    // DOMSource source = new DOMSource(doc);
                    // StreamResult result = new StreamResult(Files.newBufferedWriter(
                    // toFile, StandardOpenOption.CREATE));
                    // transformer.transform(source, result);

                    LSOutput lsOutput = impl.createLSOutput();
                    lsOutput.setEncoding("UTF-8");
                    lsOutput.setByteStream(Files.newOutputStream(toFile, StandardOpenOption.CREATE,
                            StandardOpenOption.TRUNCATE_EXISTING));
                    writer.write(doc, lsOutput);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * Find child node node.
     *
     * @param node the node
     * @param childName the child name
     * @return the node
     */
    private Node findChildNode(final Node node, final String childName) {
        NodeList nl = node.getChildNodes();
        Node result = null;
        for (int i = 0; i < nl.getLength(); i++) {
            Node item = nl.item(i);
            if (childName.equals(nl.item(i).getNodeName())) {
                result = item;
                break;
            }
        }
        return result;
    }
}
