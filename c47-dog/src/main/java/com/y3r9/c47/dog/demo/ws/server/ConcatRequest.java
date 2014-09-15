
package com.y3r9.c47.dog.demo.ws.server;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="s1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="s2" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "s1",
    "s2"
})
@XmlRootElement(name = "concatRequest")
public class ConcatRequest {

    @XmlElement(required = true)
    protected String s1;
    @XmlElement(required = true)
    protected String s2;

    /**
     * 获取s1属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getS1() {
        return s1;
    }

    /**
     * 设置s1属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setS1(String value) {
        this.s1 = value;
    }

    /**
     * 获取s2属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getS2() {
        return s2;
    }

    /**
     * 设置s2属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setS2(String value) {
        this.s2 = value;
    }

}
