/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.hsostaric.web.zrna;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.annotation.ApplicationMap;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.hsostaric.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.hsostaric.rest.klijenti.Zadaca2_2RS;
import org.foi.nwtis.hsostaric.web.serveri.AvionLeti;
import org.foi.nwtis.hsostaric.ws.klijenti.Zadaca2_1WS;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.OdgovorAerodrom;
import org.foi.nwtis.rest.klijenti.LIQKlijent;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.Lokacija;
import org.foi.nwtis.rest.podaci.MeteoPodaci;

/**
 * Klasa koja predstavlja zrno za pregled aerodroma.
 *
 * @author Hrvoje-PC
 */
@Named(value = "pregledAerodroma")
@SessionScoped
public class PregledAerodroma implements Serializable {

    @Inject
    PrijavaKorisnika prijavaKorisnika;

    @Inject
    @ApplicationMap
    private Map<String, Object> applicationMap;

    @Inject
    FacesContext facesContext;

    @Getter
    @Setter
    List<Aerodrom> aerodromiKorisnika = new ArrayList<>();
    @Getter
    @Setter
    String datumPocetka = "";
    @Getter
    @Setter
    String datumKraja = "";
    @Getter
    @Setter
    String korisnik = "";
    @Getter
    @Setter
    String lozinka = "";
    @Getter
    @Setter
    Lokacija lokacijaServisa = new Lokacija();
    @Getter
    @Setter
    Lokacija lokacija = new Lokacija();
    @Getter
    @Setter
    String nazivAerodroma = "";
    @Setter
    @Getter
    LIQKlijent liqKlijent;

    @Getter
    @Setter
    MeteoPodaci meteoPodaci = new MeteoPodaci();
    @Getter
    @Setter
    long brojSekundiOd;
    @Getter
    @Setter
    long brojSekundiDo;
    private BP_Konfiguracija bpk;

    @Getter
    @Setter
    private OWMKlijent owmk;

    @Getter
    @Setter
    String icaoOdabrani;

    @Getter
    @Setter
    List<AvionLeti> avionLeti = new ArrayList<>();

    public PregledAerodroma() {

    }

    /**
     * Metoda koja preuzima podatke prijavljenog korisnika u lokalne varijable.
     */
    private void preuzmiPodatkeKorisnika() {
        korisnik = prijavaKorisnika.getKorisnickoIme();
        lozinka = prijavaKorisnika.getLozinka();
    }

    /**
     * Metoda koja preuzima aerodrome prijavljenog korisnika.
     */
    public void preuzmiAerodromeKorisnika() {
        preuzmiPodatkeKorisnika();
        Zadaca2_2RS zrs = new Zadaca2_2RS(korisnik, lozinka);
        OdgovorAerodrom odgovor = zrs.dajAerodomeKorisnika(OdgovorAerodrom.class);
        aerodromiKorisnika = new ArrayList(Arrays.
                asList(odgovor
                        .getOdgovor()));
        if (aerodromiKorisnika == null
                || aerodromiKorisnika.isEmpty()) {
            aerodromiKorisnika = new ArrayList();
        }

    }

    /**
     * Metoda koja dohvaća geolikacijske podatke za odabrani aerodrom.
     *
     * @param icao
     * @return
     */
    public String dajGeolokacijskePodatke(String icao) {
        podaciIzBazePodataka(icao);
        podaciSaServisa();
        return "";
    }

    /**
     * Postavlja podatke lokacije iz baze podataka u lokalne varijable.
     */
    private void podaciIzBazePodataka(String icao) {
        for (Aerodrom aerodrom : aerodromiKorisnika) {
            if (aerodrom.getIcao()
                    .equals(icao)) {
                nazivAerodroma = aerodrom.getNaziv();
                lokacija.setLatitude(aerodrom
                        .getLokacija()
                        .getLatitude());
                lokacija.setLongitude(aerodrom
                        .getLokacija()
                        .getLongitude());
                break;
            }
        }
    }

    /**
     * Metoda koja dohvaća geolokacijske podatke sa gotovih servisa za usporedbu
     * podataka iz baze podataka.
     */
    private void podaciSaServisa() {
        bpk = (BP_Konfiguracija) applicationMap.get("BP_Konfig");
        String apiKey = bpk.getKonfig()
                .dajPostavku("LocationIQ.token");
        if (apiKey != null
                && !apiKey.equals("")) {
            liqKlijent = new LIQKlijent(apiKey);
            lokacijaServisa = liqKlijent.getGeoLocation(nazivAerodroma);
        }
        String apiKeyOWM = bpk.getKonfig()
                .dajPostavku("OpenWeatherMap.apikey");
        if (apiKeyOWM != null
                && !apiKeyOWM.equals("")) {
            owmk = new OWMKlijent(apiKeyOWM);
            meteoPodaci = owmk.getRealTimeWeather(
                    lokacija.getLatitude(),
                    lokacija.getLongitude());

        }
    }

    /**
     * Metoda koja preuzima avione za odabrani aerodrom i odabrani
     * interval.Ukoliko postoje podaci tada se u novom prozoru prikazuju podaci.
     *
     * @param icao
     * @return
     */
    public String preuzmiAvione(String icao) {
        icaoOdabrani = icao;
        if (datumPocetka != null
                && datumPocetka != null) {
            if (!datumPocetka.equals("")
                    && !datumKraja.equals("")) {
                if (provjeriFormat(datumPocetka) == true
                        && provjeriFormat(datumKraja) == true) {
                    postaviVarijable();
                    dohvatiLetove();
                    if (avionLeti.size() > 0) {
                        ExternalContext externalContext = facesContext.getExternalContext();
                        try {
                            externalContext.redirect("pregledAviona.xhtml");
                        } catch (IOException ex) {
                            System.out.println("Greska: " + ex);
                        }
                    }
                }
            }
        }
        return "";
    }

    /**
     * Metoda koja dohvaća letove sa prvog web servisa ukoliko su svi podaci
     * ispravni.
     */
    private void dohvatiLetove() {
        if (korisnik != null
                && lozinka != null
                && icaoOdabrani != null
                && brojSekundiOd > 0
                && brojSekundiDo > 0) {

            Zadaca2_1WS zadaca2_1WS = new Zadaca2_1WS();
            avionLeti = zadaca2_1WS.poletjeliAvioniAerodrom(korisnik, lozinka,
                    icaoOdabrani, brojSekundiOd, brojSekundiDo);
            if (avionLeti == null
                    || avionLeti.isEmpty()) {
                avionLeti = new ArrayList();
            }
        }
    }

    /**
     * Metoda koja postavlja potrebne varijable u ispravan oblik.
     */
    private void postaviVarijable() {
        try {
            Date od = new SimpleDateFormat("dd.MM.yyyy HH:mm")
                    .parse(datumPocetka);
            Date kraj = new SimpleDateFormat("dd.MM.yyyy HH:mm").
                    parse(datumKraja);
            brojSekundiOd = od.getTime() / 1000;
            brojSekundiDo = kraj.getTime() / 1000;
        } catch (ParseException ex) {
            System.out.println("Greska: " + ex);
        }
    }

    /**
     * Metoda koja provjerava je li ulazni format datuma u ispravnome obliku.
     * @param dateStr
     * @return 
     */
    public boolean provjeriFormat(String dateStr) {
        try {
            Date date = new SimpleDateFormat("dd.MM.yyyy HH:mm")
                    .parse(dateStr);
        } catch (ParseException ex) {
            return false;
        }
        return true;
    }
}
