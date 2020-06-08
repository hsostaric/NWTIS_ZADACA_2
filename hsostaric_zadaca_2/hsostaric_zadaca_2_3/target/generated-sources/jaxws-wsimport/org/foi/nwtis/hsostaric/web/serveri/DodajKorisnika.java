
package org.foi.nwtis.hsostaric.web.serveri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dodajKorisnika complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dodajKorisnika">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="noviKorisnik" type="{http://serveri.web.hsostaric.nwtis.foi.org/}korisnik" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dodajKorisnika", propOrder = {
    "noviKorisnik"
})
public class DodajKorisnika {

    protected Korisnik noviKorisnik;

    /**
     * Gets the value of the noviKorisnik property.
     * 
     * @return
     *     possible object is
     *     {@link Korisnik }
     *     
     */
    public Korisnik getNoviKorisnik() {
        return noviKorisnik;
    }

    /**
     * Sets the value of the noviKorisnik property.
     * 
     * @param value
     *     allowed object is
     *     {@link Korisnik }
     *     
     */
    public void setNoviKorisnik(Korisnik value) {
        this.noviKorisnik = value;
    }

}
