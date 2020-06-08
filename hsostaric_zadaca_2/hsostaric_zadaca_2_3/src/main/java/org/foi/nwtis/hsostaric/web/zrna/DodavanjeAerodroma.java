/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.hsostaric.web.zrna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.hsostaric.rest.klijenti.Zadaca2_2RS;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.OdgovorAerodrom;
import org.primefaces.shaded.json.JSONObject;

/**
 * Klasa koja predstavlja zrno za dodavanje aerodroma u korisnikovu kolekciju.
 *
 * @author Hrvoje-PC
 */
@Named(value = "dodavanjeAerodroma")
@SessionScoped
public class DodavanjeAerodroma implements Serializable {

    @Inject
    PrijavaKorisnika prijavaKorisnika;

    @Getter
    @Setter
    String naziv;
    @Getter
    @Setter
    String drzava;
    @Setter
    @Getter
    List<Aerodrom> aerodromi = new ArrayList<>();
    @Setter
    @Getter
    List<Aerodrom> aerodromiKorisnika = new ArrayList<>();
    @Getter
    @Setter
    String icaoOdabrani;
    @Getter
    @Setter
    String korisnik = "";
    @Getter
    @Setter
    String lozinka = "";

    /**
     * Creates a new instance of DodavanjeAerodroma
     */
    public DodavanjeAerodroma() {
    }

    /**
     * Metoda koja preuzima podatke prijavljenog korisnika iz sesije i
     * pohranjuje ih u lokalne varijable.
     */
    public void preuzmiPodatkeKorisnika() {
        korisnik = prijavaKorisnika.getKorisnickoIme();
        lozinka = prijavaKorisnika.getLozinka();
    }

    /**
     * Metoda koja poziva dohvaćanje aerodroma korisnika i odgovor pohranjuje u
     * kolekciju.
     */
    public void preuzmiAerodromeKorisnika() {
        Zadaca2_2RS zrs = new Zadaca2_2RS(korisnik, lozinka);
        OdgovorAerodrom odgovor = zrs.dajAerodomeKorisnika(OdgovorAerodrom.class);
        aerodromiKorisnika = Arrays.asList(odgovor.getOdgovor());
    }

    /**
     * Metoda koja dohvaća aerodrome prema nazivu iz baze podataka.
     *
     * @return
     */
    public String dajAerodromeNaziv() {
        preuzmiPodatkeKorisnika();
        if (naziv.length() >= 3) {
            aerodromi = new ArrayList(dajAerodrome(korisnik, lozinka, "", naziv));
            if (aerodromi == null
                    || aerodromi.isEmpty()) {
                aerodromi = new ArrayList<>();
            } else {
                preuzmiAerodromeKorisnika();
                ukloniAerodromeKorisnikaIzKolekcije();
            }
        }
        return "";
    }

    /**
     * Dohvaća aerodrome iz određene države.
     *
     * @return
     */
    public String dajAerodromeDrzava() {
        preuzmiPodatkeKorisnika();
        if (drzava.length() >= 2) {
            aerodromi = new ArrayList(dajAerodrome(korisnik, lozinka, drzava, ""));
            if (aerodromi == null || aerodromi.isEmpty()) {
                aerodromi = new ArrayList<>();
            } else {
                preuzmiAerodromeKorisnika();
                ukloniAerodromeKorisnikaIzKolekcije();
            }
        }
        return "";
    }

    /**
     * Metoda za dodavanje aerodroma korisniku.Kreira se JSON objekt, koji se u
     * obliku String šalje prema serveru.
     *
     * @param icao
     * @return
     */
    public String dodajAerodromKorisniku(String icao) {
        preuzmiAerodromeKorisnika();
        JSONObject object = new JSONObject();
        object.put("icao", icao);
        Zadaca2_2RS zadaca2_2RS = new Zadaca2_2RS(korisnik, lozinka);
        Response odgovor = zadaca2_2RS.dodajAerodromKorisniku(object.toString());
        OdgovorAerodrom odg = odgovor.readEntity(OdgovorAerodrom.class);
        if (odg.getStatus().equals("10")) {
            System.out.println("Aerodrom je dodan");
        } else {
            System.out.println("Aerodrom nije dodan");
        }
        return "";
    }

    /**
     * Metoda koja uklanja pristigle aerodrome koji se nalaze u kolekciji
     * korisnika.
     */
    public void ukloniAerodromeKorisnikaIzKolekcije() {
        Iterator<Aerodrom> iterator;
        List<Aerodrom> pom = new ArrayList<>();
        if (aerodromi.size() > 0) {
            if (aerodromiKorisnika.size() > 0) {
                for (iterator = aerodromi.iterator(); iterator.hasNext();) {
                    Aerodrom brisi = iterator.next();
                    for (Aerodrom aerodromKorisnika : aerodromiKorisnika) {
                        if (aerodromKorisnika.getIcao().equals(brisi.getIcao())) {
                            iterator.remove();
                        }
                    }
                }
            }
        }
        if (aerodromi == null || aerodromi.isEmpty()) {
            aerodromi = new ArrayList<>();
        }
    }

    /**
     * Metoda koja preuzima iz odgovora aerodrome ovisno prema nazivu ili iz
     * države.
     */
    private List<Aerodrom> dajAerodrome(String korisnik, String lozinka,
            String naziv, String drzava) {
        List<Aerodrom> aerodroms = new ArrayList<>();
        Zadaca2_2RS zadaca2_2RS = new Zadaca2_2RS(korisnik, lozinka);
        OdgovorAerodrom odgAero = zadaca2_2RS.dajAerodrome(OdgovorAerodrom.class, naziv, drzava);
        aerodroms = Arrays.asList(odgAero.getOdgovor());
        return aerodroms;
    }
}
