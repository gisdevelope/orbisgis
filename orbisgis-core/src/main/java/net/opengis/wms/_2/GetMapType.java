//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.02.08 at 01:23:12 PM CET 
//


package net.opengis.wms._2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetMapType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetMapType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/wms/2.0}MapRequestType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/wms/2.0}Exceptions" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetMapType", propOrder = {
    "exceptions"
})
public class GetMapType
    extends MapRequestType
{

    @XmlElement(name = "Exceptions")
    protected ExceptionFormatType exceptions;

    /**
     * Gets the value of the exceptions property.
     * 
     * @return
     *     possible object is
     *     {@link ExceptionFormatType }
     *     
     */
    public ExceptionFormatType getExceptions() {
        return exceptions;
    }

    /**
     * Sets the value of the exceptions property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExceptionFormatType }
     *     
     */
    public void setExceptions(ExceptionFormatType value) {
        this.exceptions = value;
    }

}