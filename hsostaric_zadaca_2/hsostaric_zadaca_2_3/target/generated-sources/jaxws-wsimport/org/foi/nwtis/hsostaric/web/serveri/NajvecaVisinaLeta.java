
package org.foi.nwtis.hsostaric.web.serveri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for najvecaVisinaLeta complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="najvecaVisinaLeta">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="korisnik" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lozinka" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="icao24" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zaVrijeme" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "najvecaVisinaLeta", propOrder = {
    "korisnik",
    "lozinka",
    "icao24",
    "zaVrijeme"
})
public class NajvecaVisinaLeta {

    protected String korisnik;
    protected String lozinka;
    protected String icao24;
    protected long zaVrijeme;

    /**
     * Gets the value of the korisnik property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKorisnik() {
        return korisnik;
    }

    /**
     * Sets the value of the korisnik property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKorisnik(String value) {
        this.korisnik = value;
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
     * Gets the value of the icao24 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIcao24() {
        return icao24;
    }

    /**
     * Sets the value of the icao24 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIcao24(String value) {
        this.icao24 = value;
    }

    /**
     * Gets the value of the zaVrijeme property.
     * 
     */
    public long getZaVrijeme() {
        return zaVrijeme;
    }

    /**
     * Sets the value of the zaVrijeme property.
     * 
     */
    public void setZaVrijeme(long value) {
        this.zaVrijeme = value;
    }

}
