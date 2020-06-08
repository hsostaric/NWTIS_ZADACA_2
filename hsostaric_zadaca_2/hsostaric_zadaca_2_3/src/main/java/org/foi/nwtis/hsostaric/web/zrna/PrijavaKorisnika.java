package org.foi.nwtis.hsostaric.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.hsostaric.ws.klijenti.Zadaca2_1WS;

/**
 * Zrno koje predstavlja zrno za prijavu korisnika. Sadrži pohranjene podatke
 * prijavljenog korisnika u sjednici.
 *
 * @author Hrvoje-PC
 */
@Named(value = "prijavaKorisnika")
@SessionScoped
public class PrijavaKorisnika implements Serializable {

    @Getter
    @Setter
    private String korisnickoIme = "";
    @Getter
    @Setter
    private String lozinka = "";
    @Getter
    @Setter
    private Boolean prijavljen = false;
    private Zadaca2_1WS klijentWebServisa;

    public PrijavaKorisnika() {
    }

    /**
     * Metoda koja provjerava autentičnost koja korisnikove podatke za
     * autentifikaciju šalje prema provme servisu, te ovisno o odgovoru obavlja
     * određenu radnju.
     *
     * @return
     */
    public String prijaviKorisnika() {
        String rezultat = "";
        klijentWebServisa = new Zadaca2_1WS();
        prijavljen = klijentWebServisa.prijavaKorisnika(korisnickoIme, lozinka);
        if (prijavljen == true) {
            rezultat = "uspjesnaPrijava";
        } else {
            ocistiVarijable();
        }
        return rezultat;
    }

    /**
     * Metoda koja čisti podatke sjednice prijavljenog korisnika.
     */
    private void ocistiVarijable() {
        this.korisnickoIme = "";
        this.lozinka = "";
        this.prijavljen = false;
    }

}
