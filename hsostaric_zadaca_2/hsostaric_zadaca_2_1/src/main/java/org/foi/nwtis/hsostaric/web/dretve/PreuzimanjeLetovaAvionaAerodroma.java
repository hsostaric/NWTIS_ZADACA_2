package org.foi.nwtis.hsostaric.web.dretve;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.hsostaric.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.hsostaric.web.podaci.AirplaneDAO;
import org.foi.nwtis.hsostaric.web.podaci.AirportDAO;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.AvionLeti;

/**
 * Dretva koja periodički preuzima podatke o letovima iz aerodroma korisnika
 *
 * @author Hrvoje Šoštarić
 */
public class PreuzimanjeLetovaAvionaAerodroma extends Thread {

    Boolean krajPreuzimanja;
    BP_Konfiguracija konf;
    OSKlijent oSKlijent;
    String korisnickoIme;
    String lozinka;
    Date pocetniDatum;
    Date zavrsniDatum;
    int ciklusPodatakaAerodroma;
    int pauzaIzmedjuAerodroma;
    List<Aerodrom> aerodromiKorisnika;
    AirportDAO aerodromiDAO;
    List<AvionLeti> listaDolazaka;
    AirplaneDAO airplaneDAO;
    long dan = 86400000;


    public PreuzimanjeLetovaAvionaAerodroma(BP_Konfiguracija konf) {
        this.konf = konf;
    }

    @Override
    public void interrupt() {
        krajPreuzimanja = true;
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        if (pocetniDatum.compareTo(dajDanasnjiDatum()) <= 0) {
            aerodromiKorisnika = new ArrayList<>();
            aerodromiDAO = new AirportDAO(konf);
            if (pocetniDatum.compareTo(zavrsniDatum) < 0) {
                while (krajPreuzimanja == false) {
                    aerodromiKorisnika = aerodromiDAO.dohvatiAerodromekorisnika();
                    for (Aerodrom a : aerodromiKorisnika) {
                        preuzmiPolaskeAviona(a);
                    }
                    azurirajPocetniDatumPreuzimanja();
                    spavajDan();
                }
            }
        }
    }

    /**
     * Metoda koja dretvu stavlja u stanje spavaj 1 dan ukoliko je krajnje
     * vrijeme manje jednako današnjem danu
     */
    private void spavajDan() {
        if (pocetniDatum.compareTo(zavrsniDatum) == 0
                && zavrsniDatum.compareTo(dajDanasnjiDatum()) >= 0) {
            try {
                sleep(dan);
            } catch (InterruptedException ex) {
                System.out.println("Greska: " + ex);
            }
        }
    }

    /**
     * Metoda koja preuzima letove za primljeni aerodrom
     *
     * @param aerodrom aerodrom za koji se preuzimaju letovi
     */
    public synchronized void preuzmiPolaskeAviona(Aerodrom aerodrom) {
        boolean preuzet = false;
        preuzet = aerodromiDAO.provjeriPreuzetostPodatka(aerodrom, pocetniDatum);
        if (preuzet == false) {
            Date krajnjiDatum = uvecajDatumZaDan(pocetniDatum);
            listaDolazaka = oSKlijent.getDepartures(aerodrom.getIcao(), pocetniDatum
                    .getTime() / 1000, krajnjiDatum
                            .getTime() / 1000);
            try {
                sleep(pauzaIzmedjuAerodroma);
            } catch (InterruptedException ex) {
                System.out.println("Greska: " + ex);
            }
            if (listaDolazaka.size() > 0) {
                pohraniAvione();
                listaDolazaka.clear();
                evidentirajAerodrom(aerodrom);
                try {
                    sleep(ciklusPodatakaAerodroma);
                } catch (InterruptedException ex) {
                    System.out.println("Greška:" + ex);
                }
            }
        }
    }

    /**
     * Metoda koja poziva metodu za evidentiranje aerodroma nakon uspješnog
     * preuzimanja i pohrane podataka
     *
     * @param aerodrom aerodrom koji se evidentira
     */
    public synchronized void evidentirajAerodrom(Aerodrom aerodrom) {
        aerodromiDAO.evidentirajAerodrom(aerodrom, pocetniDatum);

    }

    /**
     * Metoda koja poziva metodu iz baze podataka za pohranu aviona.
     */
    private synchronized void pohraniAvione() {
        for (AvionLeti avion : listaDolazaka) {
            if (avion.getEstArrivalAirport() != null) {
                airplaneDAO = new AirplaneDAO(konf);
                airplaneDAO.UnesiAvion(avion, pocetniDatum);

            }
        }
    }

    /**
     * Metoda koja ažurira dan za preuzimanje letova.
     */
    private synchronized void azurirajPocetniDatumPreuzimanja() {
        Calendar c = Calendar.getInstance();
        c.setTime(pocetniDatum);
        c.add(Calendar.DATE, 1);
        pocetniDatum = c.getTime();

    }

    @Override
    public synchronized void start() {
        krajPreuzimanja = vratiUvjet();
        korisnickoIme = vratiPostavku("OpenSkyNetwork.korisnik");
        lozinka = vratiPostavku("OpenSkyNetwork.lozinka");
        ciklusPodatakaAerodroma = Integer.
                parseInt(vratiPostavku("preuzimanje.ciklus"));
        pauzaIzmedjuAerodroma = Integer.
                parseInt(vratiPostavku("preuzimanje.pauza"));
        pocetniDatum = pretvoriStringuDate(vratiPostavku("preuzimanje.pocetak"));
        zavrsniDatum = pretvoriStringuDate(vratiPostavku("preuzimanje.kraj"));
        oSKlijent = new OSKlijent(korisnickoIme, lozinka);
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Metoda koja provjerava uvjet iz konfiguracijske datoteke i vraća određen
     * rezultat.
     */
    private synchronized Boolean vratiUvjet() {
        String uvjet = konf.getKonfig()
                .dajPostavku("preuzimanje.status")
                .trim();
        return uvjet.equals("true");
    }

    /**
     * Metoda koja za primljeni ključ vraća postavku iz konfiguracijske datoteke
     *
     * @param kljuc ključ za koji se vraća vrijednost
     * @return
     */
    public synchronized String vratiPostavku(String kljuc) {
        return konf.getKonfig()
                .dajPostavku(kljuc);
    }

    /**
     * Metoda koja vraća današnji datum u formatu dd.MM.yyyy .
     */
    private synchronized Date dajDanasnjiDatum() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        String datum = dateFormat.format(date);
        return pretvoriStringuDate(datum);
    }

    /**
     * Metoda koja pretvara primljeni string u format Date.
     * @param strDatum primljeni datum u obliku string
     * @return  konvertirani datum iz stringa
     */
    public synchronized Date pretvoriStringuDate(String strDatum) {
        Date datum = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            datum = sdf.parse(strDatum);
        } catch (ParseException ex) {
            System.out.println("Greška: " + ex);
        }
        return datum;
    }

    /**
     * Metoda koja uvećava primljeni datum za jedan dan.
     */
    private synchronized Date uvecajDatumZaDan(Date datum) {
        Calendar c = Calendar.getInstance();
        c.setTime(datum);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }
}
