//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.04.25 at 03:03:22 PM CEST 
//

package org.orbisgis.core.geocognition.persistence;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref=&quot;{}geocognition-node&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "geocognitionNode" })
@XmlRootElement(name = "geocognition-document")
public class GeocognitionDocument {

	@XmlElement(name = "geocognition-node", required = true)
	protected GeocognitionNode geocognitionNode;

	/**
	 * Gets the value of the geocognitionNode property.
	 * 
	 * @return possible object is {@link GeocognitionNode }
	 * 
	 */
	public GeocognitionNode getGeocognitionNode() {
		return geocognitionNode;
	}

	/**
	 * Sets the value of the geocognitionNode property.
	 * 
	 * @param value
	 *            allowed object is {@link GeocognitionNode }
	 * 
	 */
	public void setGeocognitionNode(GeocognitionNode value) {
		this.geocognitionNode = value;
	}

}