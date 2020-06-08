/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.hsostaric.ws.klijenti;

import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.hsostaric.web.serveri.Aerodrom;
import org.foi.nwtis.hsostaric.web.serveri.AvionLeti;
import org.foi.nwtis.hsostaric.web.serveri.Zadaca2;
import org.foi.nwtis.hsostaric.web.serveri.Zadaca2_Service;

/**
 * Klasa koja predstavlja klijenta za dohvat podataka iz prve aplikacije
 * (servisa).
 *
 * @author Hrvoje-PC
 */
public class Zadaca2_1WS {

    /**
     * Metoda koja dohvaća aerodrome korisnika iz prve aplikacije.s
     *
     * @param korisnik
     * @param lozinka
     * @return
     */
    public List<Aerodrom> dajAerodrome(String korisnik, String lozinka) {
        List<Aerodrom> aerodromi = null;

        try {
            Zadaca2_Service service = new org.foi.nwtis.hsostaric.web.serveri.Zadaca2_Service();
            Zadaca2 port = service.getZadaca2Port();
            aerodromi = port.dajAerodromeKorisnika(korisnik, lozinka);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return aerodromi;
    }

    /**
     * Metoda koja autentificira korisnika.zahtjev se šalje na SOAP web servis.
     * Ispravnost podataka je uvjetovana rezultatom ovog odgovora.
     *
     * @param korisnickoIme
     * @param lozinka
     * @return
     */
    public Boolean prijavaKorisnika(String korisnickoIme, String lozinka) {
        Boolean prijavljen = false;
        try {
            Zadaca2_Service service = new org.foi.nwtis.hsostaric.web.serveri.Zadaca2_Service();
            Zadaca2 port = service.getZadaca2Port();
            prijavljen = port.provjeraKorisnika(korisnickoIme, lozinka);
        } catch (Exception ex) {
            System.out.println("Greska: " + ex);
        }
        return prijavljen;
    }

    /**
     * Metoda koja vraća popis poletjelih aviona sa zadanog aerodroma za
     * određeni interval.Podatke dohvaća sa SOAP web servisa.
     * @param korisnickoIme
     * @param lozinka
     * @param ICAO
     * @param od
     * @param kraj
     * @return kolekcija aviona
     */
    public List<AvionLeti> poletjeliAvioniAerodrom(String korisnickoIme,
            String lozinka, String ICAO,
            long od, long kraj) {
        List<AvionLeti> lista = new ArrayList<>();
        try {
            Zadaca2_Service service = new org.foi.nwtis.hsostaric.web.serveri.Zadaca2_Service();
            Zadaca2 port = service.getZadaca2Port();
            lista = port.poletjeliAvioniAerodrom(korisnickoIme, lozinka, ICAO, od, kraj);
            if (lista == null
                    || lista.isEmpty()) {
                lista = new ArrayList<>();
            }
        } catch (Exception ex) {
            System.out.println("Greška: " + ex);
        }
        return lista;
    }
}
