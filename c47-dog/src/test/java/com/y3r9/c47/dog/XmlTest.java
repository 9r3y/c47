package com.y3r9.c47.dog;

import java.io.ByteArrayInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.HexDump;
import org.junit.Test;

import com.sun.org.apache.xerces.internal.impl.io.MalformedByteSequenceException;

/**
 * The class XmlTest.
 *
 * @version 1.0
 */
public final class XmlTest {

    @Test
    public void test() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<register xmlns=\"http://www.netis.com.cn/decoder/register\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "    xsi:schemaLocation=\"http://www.netis.com.cn/decoder/register http://www.netis.com.cn/decoder/register/decoder.register.xsd\">\n" +
                "\n" +
                "    <property>\n" +
                "        <description>通用服务器平台（eUSP）</description>\n" +
                "    </property>\n" +
                "    \n" +
                "    <categories>\n" +
                "        <category>行业公共.银行.通用服务器平台（eUSP）</category>\n" +
                "    </categories>\n" +
                "    \n" +
                "    <decoders>\n" +
                "        <decoder>\n" +
                "            <name>EUSP</name>\n" +
                "            <constructor>EuspDecoder</constructor>\n" +
                "            <description>China Mingsheng Banking Crop. eBIS Universal Server Platform</description>\n" +
                "        </decoder>\n" +
                "    </decoders>\n" +
                "\n" +
                "</register>aaa";
        XMLInputFactory factory = XMLInputFactory.newFactory();
        factory.setProperty("javax.xml.stream.supportDTD", false);
        factory.setProperty("javax.xml.stream.isValidating", false);
        factory.setProperty("javax.xml.stream.isCoalescing", true);

        byte[] data = xml.getBytes("UTF-8");
        HexDump.dump(data, 0, System.out, 0);
        int size = data.length;
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        XMLStreamReader reader = factory.createXMLStreamReader(stream, "utf-8");

        String rootElmentName = "register";
        String lastChildName = null;
        try {
            while (reader.hasNext()) {
                reader.next();

                if (reader.isStartElement()) {
                    lastChildName = reader.getLocalName();
                    final int offset = reader.getLocation().getCharacterOffset();
                    System.out.println(lastChildName);

                } else if (reader.isCharacters()) {

                } else if (reader.isEndElement()) {
                    final int offset = reader.getLocation().getCharacterOffset();
                    // parse text and clear
                    System.out.println("");

                }
            }
        } catch (Exception e) {
            final int offset = reader.getLocation().getCharacterOffset();
            e.printStackTrace();
        }



    }
}
