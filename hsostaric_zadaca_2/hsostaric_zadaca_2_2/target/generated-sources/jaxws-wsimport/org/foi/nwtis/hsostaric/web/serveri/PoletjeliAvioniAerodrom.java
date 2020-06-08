
package org.foi.nwtis.hsostaric.web.serveri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for poletjeliAvioniAerodrom complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="poletjeliAvioniAerodrom">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="korisnickoIme" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lozinka" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ICAO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="od" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="do" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "poletjeliAvioniAerodrom", propOrder = {
    "korisnickoIme",
    "lozinka",
    "icao",
    "od",
    "_do"
})
public class PoletjeliAvioniAerodrom {

    protected String korisnickoIme;
    protected String lozinka;
    @XmlElement(name = "ICAO")
    protected String icao;
    protected long od;
    @XmlElement(name = "do")
    protected long _do;

    /**
     * Gets the value of the korisnickoIme property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    /**
     * Sets the value of the korisnickoIme property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKorisnickoIme(String value) {
        this.korisnickoIme = value;
    }

    /**
     * Gets the value of the lozinka property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLozinka() {
        return lozinka;
    }

    /**
     * Sets the value of the lozinka property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLozinka(String value) {
        this.lozinka = value;
    }

    /**
     * Gets the value of the icao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getICAO() {
        return icao;
    }

    /**
     * Sets the value of the icao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setICAO(String value) {
        this.icao = value;
    }

    /**
     * Gets the value of the od property.
     * 
     */
    public long getOd() {
        return od;
    }

    /**
     * Sets the value of the od property.
     * 
     */
    public void setOd(long value) {
        this.od = value;
    }

    /**
     * Gets the value of the do property.
     * 
     */
    public long getDo() {
        return _do;
    }

    /**
     * Sets the value of the do property.
     * 
     */
    public void setDo(long value) {
        this._do = value;
    }

}
