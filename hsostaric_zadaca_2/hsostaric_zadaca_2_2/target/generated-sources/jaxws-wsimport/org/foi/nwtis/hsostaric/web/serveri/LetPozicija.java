
package org.foi.nwtis.hsostaric.web.serveri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for letPozicija complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="letPozicija">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="baro_altitude" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="latitude" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="longitude" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="on_ground" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="time" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="true_track" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "letPozicija", propOrder = {
    "baroAltitude",
    "latitude",
    "longitude",
    "onGround",
    "time",
    "trueTrack"
})
public class LetPozicija {

    @XmlElement(name = "baro_altitude")
    protected float baroAltitude;
    protected float latitude;
    protected float longitude;
    @XmlElement(name = "on_ground")
    protected boolean onGround;
    protected int time;
    @XmlElement(name = "true_track")
    protected float trueTrack;

    /**
     * Gets the value of the baroAltitude property.
     * 
     */
    public float getBaroAltitude() {
        return baroAltitude;
    }

    /**
     * Sets the value of the baroAltitude property.
     * 
     */
    public void setBaroAltitude(float value) {
        this.baroAltitude = value;
    }

    /**
     * Gets the value of the latitude property.
     * 
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * Sets the value of the latitude property.
     * 
     */
    public void setLatitude(float value) {
        this.latitude = value;
    }

    /**
     * Gets the value of the longitude property.
     * 
     */
    public float getLongitude() {
        return longitude;
    }

    /**
     * Sets the value of the longitude property.
     * 
     */
    public void setLongitude(float value) {
        this.longitude = value;
    }

    /**
     * Gets the value of the onGround property.
     * 
     */
    public boolean isOnGround() {
        return onGround;
    }

    /**
     * Sets the value of the onGround property.
     * 
     */
    public void setOnGround(boolean value) {
        this.onGround = value;
    }

    /**
     * Gets the value of the time property.
     * 
     */
    public int getTime() {
        return time;
    }

    /**
     * Sets the value of the time property.
     * 
     */
    public void setTime(int value) {
        this.time = value;
    }

    /**
     * Gets the value of the trueTrack property.
     * 
     */
    public float getTrueTrack() {
        return trueTrack;
    }

    /**
     * Sets the value of the trueTrack property.
     * 
     */
    public void setTrueTrack(float value) {
        this.trueTrack = value;
    }

}
