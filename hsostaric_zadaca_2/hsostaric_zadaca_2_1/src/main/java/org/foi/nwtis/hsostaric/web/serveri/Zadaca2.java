/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.hsostaric.web.serveri;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.servlet.ServletContext;
import org.foi.nwtis.hsostaric.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.hsostaric.web.podaci.AirportDAO;
import org.foi.nwtis.hsostaric.web.podaci.KorisnikDAO;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Korisnik;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.LetPozicija;

/**
 *
 * @author Hrvoje Šoštarić
 */
@WebService(serviceName = "Zadaca2")
public class Zadaca2 {

    @Inject
    ServletContext context;

    /**
     * Metoda za dodavanje korisnika u bazu podataka
     *
     * @param noviKorisnik
     * @return
     */
    @WebMethod(operationName = "dodajKorisnika")
    public Boolean dodajKorisnika(@WebParam(name = "noviKorisnik") Korisnik noviKorisnik) {
        if ((noviKorisnik.getIme().isEmpty() || noviKorisnik.getIme() == null)
                || (noviKorisnik.getPrezime()
                        .isEmpty()
                || noviKorisnik.getPrezime() == null)
                || (noviKorisnik.getKorIme().isEmpty()
                || noviKorisnik.getKorIme() == null)
                || (noviKorisnik.getLozinka()
                        .isEmpty()
                || noviKorisnik.getLozinka() == null)
                || (noviKorisnik.getEmailAdresa()
                        .isEmpty()
                || noviKorisnik.getEmailAdresa() == null)) {
            return false;
        }
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        KorisnikDAO korisnikDAO = new KorisnikDAO(bpk);
        return korisnikDAO.dodajKorisnika(noviKorisnik);
    }

 
    /**
     * Metoda koja vraća aerodrome korisnika.
     *
     * @param korisnickoIme 
     * @param lozinka
     * @return
     */
    @WebMethod(operationName = "dajAerodromeKorisnika")
    public List<Aerodrom> dajAerodromeKorisnika(@WebParam(name = "korisnickoIme") String korisnickoIme,
            @WebParam(name = "lozinka") String lozinka) {
        List<Aerodrom> aerodromi = new ArrayList<>();
        boolean autentificiran = false;
        if (korisnickoIme != null
                && lozinka != null
                && !korisnickoIme.isEmpty()
                && !lozinka.isEmpty()) {
            autentificiran = provjeraKorisnika(korisnickoIme, lozinka);
            if (autentificiran == true) {
                BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
                AirportDAO airportDAO = new AirportDAO(bpk);
                aerodromi = airportDAO.dohvatiAerodromeKorisnika(korisnickoIme);
            }
        }
        return aerodromi;

    }

    /**
     * Metoda koja vraća traženi aerodrom iz vlastitih aerodroma
     *
     * @param korisnickoIme
     * @param lozinka
     * @param icao
     * @return
     */
    @WebMethod(operationName = "dajAerodromizKolekcijeVlastitih")
    public Aerodrom dajAerodromizVlastitih(@WebParam(name = "korisnickoIme") String korisnickoIme,
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "oznaka") String icao) {
        Aerodrom aerodrom = null;
        boolean autentificiran = false;
        autentificiran = provjeraKorisnika(korisnickoIme, lozinka);
        if (autentificiran == true) {
            BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
            AirportDAO airportDAO = new AirportDAO(bpk);
            aerodrom = airportDAO.dohvatiAerodromIzMojihAerodroma(korisnickoIme, icao);
        }
        return aerodrom;

    }

    /**
     * Metoda koja provjerava postoji li zadani aerodrom u kolekciji aerodroma
     * korisnika
     *
     * @param korisnickoIme
     * @param lozinka
     * @param icao
     * @return
     */
    @WebMethod(operationName = "provjeriAerodromUMojojKolekciji")
    public Boolean provjeriPostojanostAerdromauMojojKolekciji(@WebParam(name = "korisnickoIme") String korisnickoIme,
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "ident") String icao) {
        List<Aerodrom> aerodromi = new ArrayList<>();
        boolean postoji = false;
        boolean autentificiran = false;
        autentificiran = provjeraKorisnika(korisnickoIme, lozinka);
        if (autentificiran == true) {
            aerodromi = dajAerodromeKorisnika(korisnickoIme, lozinka);
            if (!aerodromi.isEmpty() 
                    || aerodromi != null) {
                for (Aerodrom a : aerodromi) {
                    if (a.getIcao()
                            .compareTo(icao) == 0) {
                        postoji = true;
                        break;
                    }
                }
            }
        }
        return postoji;
    }

    /**
     * Metoda koja vraća aerodrome prema nazivu.
     *
     * @param korisnickoIme
     * @param lozinka
     * @param naziv
     * @return
     */
    @WebMethod(operationName = "dajAerodromeNaziv")
    public List<Aerodrom> dajAerodromeNaziv(@WebParam(name = "korisnickoIme") String korisnickoIme,
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "naziv") String naziv) {
        System.out.println("Naziv iz soapa: "+naziv+"\n korisnik:"+korisnickoIme+"\n"+"lozinka:"+lozinka);
        List<Aerodrom> aerodromi = new ArrayList<>();
        boolean autentificiran = provjeraKorisnika(korisnickoIme, lozinka);
        if (autentificiran == true) {
            System.out.println("Idem po upit");
            BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
            AirportDAO airportDAO = new AirportDAO(bpk);
            aerodromi = airportDAO.dajAerodromiPremaNazivu(naziv);
        }
        return aerodromi;
    }

    /**
     * Metoda koja služi za autentifikaciju korisnika.
     *
     * @param korisnickoIme
     * @param lozinka
     * @return
     */
    @WebMethod(operationName = "provjeraKorisnika")
    public Boolean provjeraKorisnika(@WebParam(name = "korisnickoIme") String korisnickoIme,
            @WebParam(name = "lozinka") String lozinka) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        KorisnikDAO korisnikDAO = new KorisnikDAO(bpk);
        return (korisnikDAO.autentificirajKorisnika(korisnickoIme, lozinka)
                != null);

    }

    /**
     * Metoda koja služi za ¸dohvat svih korisnika iz baze podataka.
     *
     * @param korisnickoIme
     * @param lozinka
     * @return
     */
    @WebMethod(operationName = "vratiKorisnikeIzBazePodataka")
    public List<Korisnik> vratiKorisnikeIzBazePodataka(@WebParam(name = "korisnickoIme") String korisnickoIme,
            @WebParam(name = "lozinka") String lozinka) {
        boolean autentificiran = false;
        List<Korisnik> popisKorisnka = new ArrayList<>();
        autentificiran = provjeraKorisnika(korisnickoIme, lozinka);
        if (autentificiran == true) {
            BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
            KorisnikDAO korisnikDAO = new KorisnikDAO(bpk);
            popisKorisnka = korisnikDAO.dohvatiSveKorisnike();
        }
        return popisKorisnka;

    }

    /**
     * Metoda koja služi za ¸dohvat svih korisnika iz baze podataka koji imaju
     * svoje aerodrome
     *
     * @param korisnickoIme
     * @param lozinka
     * @return
     */
    @WebMethod(operationName = "vratiKorisnikeSaAerodromima")
    public List<Korisnik> vratiKorisnikeSaAerodromima(@WebParam(name = "korisnickoIme") String korisnickoIme,
            @WebParam(name = "lozinka") String lozinka) {
        boolean autentificiran = false;
        List<Korisnik> popisKorisnka = new ArrayList<>();
        autentificiran = provjeraKorisnika(korisnickoIme, lozinka);
        if (autentificiran == true) {
            BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
            KorisnikDAO korisnikDAO = new KorisnikDAO(bpk);
            popisKorisnka = korisnikDAO.dohvatiKorisnikeSAerodromima();
        }
        return popisKorisnka;
    }

    /**
     * Metoda koja vraća sve aerodrome za pojedinu državu
     *
     * @param korisnickoIme
     * @param lozinka
     * @param drzava
     * @return
     */
    @WebMethod(operationName = "vratiAerodromePremaDrzavi")
    public List<Aerodrom> vratiAerodromeDrzave(@WebParam(name = "korisnickoIme") String korisnickoIme,
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "drzava") String drzava) {
        boolean autentificiran = false;
        AirportDAO airportDAO;
        List<Aerodrom> aerodromi = new ArrayList<>();
        if (drzava != null
                || !drzava.isEmpty()) {
            autentificiran = provjeraKorisnika(korisnickoIme, lozinka);
            if (autentificiran == true) {
                BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
                airportDAO = new AirportDAO(bpk);
                aerodromi = airportDAO.dohvatiAerodromeDrzave(drzava);
            }
        }
        return aerodromi;
    }

    /**
     *Metoda koja dodaje aerodrom u korisnikovu kolekciju zrakoplova.
     * @param korisnickoIme
     * @param lozinka
     * @param ident
     * @return uspješnost operacije
     */
    @WebMethod(operationName = "dodajAerodromUVlastiteAerodrome")
    public Boolean dodajAerodromUVlastite(@WebParam(name = "korisnickoIme") String korisnickoIme,
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "ident") String ident) {
        Boolean dodan = false;
        Boolean postoji=false;
        postoji= provjeriPostojanostAerdromauMojojKolekciji(korisnickoIme, lozinka, ident);
        boolean autentificiran = false;
        AirportDAO airportDAO;
        if(postoji==false){
        autentificiran = provjeraKorisnika(korisnickoIme, lozinka);
        if (autentificiran == true) {
            BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
            airportDAO = new AirportDAO(bpk);
            dodan = airportDAO.dodajAerodromUMojuKolekciju(ident, korisnickoIme);
        }}
        return dodan;
    }

    /**
     * Metoda koja služi za dohvat aviona poletjelih sa Aerodroma za određeni
     * termin
     *
     * @param korisnickoIme
     * @param lozinka
     * @param ICAO
     * @param od
     * @param kraj
     * @return
     */
    @WebMethod(operationName = "poletjeliAvioniAerodrom")
    public List<AvionLeti> poletjeliAvioniAerodrom(@WebParam(name = "korisnickoIme") String korisnickoIme,
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "ICAO") String ICAO,
            @WebParam(name = "od") long od, @WebParam(name = "do") long kraj) {
        boolean autentificiran = false;
        List<AvionLeti> popisAviona = new ArrayList<>();
        autentificiran = provjeraKorisnika(korisnickoIme, lozinka);
        if (autentificiran == true) {
            BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
            AirportDAO airportDAO = new AirportDAO(bpk);
            popisAviona = airportDAO.poletjeliAvioniAerodrom(ICAO, od, kraj);
            if (popisAviona == null) {
                return popisAviona;
            }
        }
        return popisAviona;
    }

}
