/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.hsostaric.ws.klijenti;

import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.hsostaric.web.serveri.Aerodrom;
import org.foi.nwtis.hsostaric.web.serveri.Zadaca2;
import org.foi.nwtis.hsostaric.web.serveri.Zadaca2_Service;

/**
 * Klasa koja predstavlja klijenta za poziv metoda SOAP web servisa prvog
 * projekta.
 *
 * @author Hrvoje-PC
 */
public class Zadaca2_1WS {

    /**
     * Metoda koja vraća popis aerodorma korisnika prema korisničkome imenu i
     * lozinci
     *
     * @param korisnik
     * @param lozinka
     * @return
     */
    public List<Aerodrom> dajAerodrome(String korisnik, String lozinka) {
        List<Aerodrom> aerodromi = null;

        try {
            Zadaca2_Service service = new Zadaca2_Service();
            Zadaca2 port = service.getZadaca2Port();
            aerodromi = port.dajAerodromeKorisnika(korisnik, lozinka);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return aerodromi;
    }

    /**
     *Metoda koja dohvaća aerodrome prema nazivu iz prvog projekta(servisa).
     * @param korisnik
     * @param lozinka
     * @param naziv
     * @return 
     */
    public List<Aerodrom> dajAerodromeNaziv(String korisnik, String lozinka, String naziv) {
        List<Aerodrom> lista = new ArrayList<>();
        try { // Call Web Service Operation
            Zadaca2_Service service = new Zadaca2_Service();
            Zadaca2 port = service.getZadaca2Port();
            System.out.println("Drugi naziv: " + naziv);
            lista = port.dajAerodromeNaziv(korisnik, lozinka, naziv);
        } catch (Exception ex) {
            System.out.println("Greška: " + ex);
        }
        return lista;
    }

    /**
     *Metoda koja dohvaća aerodrome države iz prvog servisa.
     * @param korisnik
     * @param lozinka
     * @param naziv
     * @return 
     */
    public List<Aerodrom> vratiAerodromeDrzave(String korisnik, String lozinka, String naziv) {
        List<Aerodrom> lista = new ArrayList<>();
        try { // Call Web Service Operation
            org.foi.nwtis.hsostaric.web.serveri.Zadaca2_Service service = new org.foi.nwtis.hsostaric.web.serveri.Zadaca2_Service();
            org.foi.nwtis.hsostaric.web.serveri.Zadaca2 port = service.getZadaca2Port();
            lista = port.vratiAerodromePremaDrzavi(korisnik, lozinka, naziv);
        } catch (Exception ex) {
            System.out.println("Greska: " + ex);
        }

        return lista;
    }
    /**
     *Metoda koja dodaje aerodrom korisniku.Zahtjev se šalje POST metodom prema prvom servisu.
     * @param korisnickoIme
     * @param lozinka
     * @param icao
     * @return ispravnost operacije
     */
    public Boolean dodajAerodromKorisniku(String korisnickoIme, String lozinka, String icao) {
        Boolean result = false;
        try { // Call Web Service Operation
            Zadaca2_Service service = new org.foi.nwtis.hsostaric.web.serveri.Zadaca2_Service();
            Zadaca2 port = service.getZadaca2Port();
            result = port.dodajAerodromUVlastiteAerodrome(korisnickoIme, lozinka, icao);
        } catch (Exception ex) {
            System.out.println("Greska: " + ex);
            result = false;
        }
        return result;
    }

    /**
     * Metoda koja dohvaća informacije o aerodromu prema zadanom identifikatoru.
     * @param korisnik
     * @param lozinka
     * @param icao
     * @return 
     */
    public Aerodrom vratiAerodromKorisnika(String korisnik, String lozinka, String icao) {
        Aerodrom aerodrom = null;
        try { // Call Web Service Operation
            Zadaca2_Service service = new Zadaca2_Service();
            Zadaca2 port = service.getZadaca2Port();
            aerodrom = port.dajAerodromizKolekcijeVlastitih(korisnik, lozinka, icao);
        } catch (Exception ex) {
            System.out.println("Greska: " + ex);
        }
        return aerodrom;
    }
}
