/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.hsostaric.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;

/**
 * Klasa koja predstavlja zrno za pregled aviona.
 *
 * @author Hrvoje-PC
 */
@Named(value = "pregledAviona")
@SessionScoped
public class PregledAviona implements Serializable {

    @Inject
    PrijavaKorisnika prijavaKorisnika;
    @Inject
    PregledAerodroma pregledAerodroma;
    @Getter
    @Setter
    List listaAviona = new ArrayList<>();
    @Getter
    @Setter
    String korisnik = "";
    @Getter
    @Setter
    String lozinka = "";
    @Getter
    @Setter
    long pocetak;
    @Getter
    @Setter
    long kraj;
    @Getter
    @Setter
    String icao;

    public PregledAviona() {
    }

    /**
     * Metoda koja pretvara sekunde prvog i zadnjeg vremena u format datuma.
     * @param sekunde
     * @return 
     */
    public String pretvoriSekundeUdatum(long sekunde) {
        Date date = new Date(sekunde * 1000L);
        SimpleDateFormat datumPredlozak = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return datumPredlozak
                .format(date);
    }
}
