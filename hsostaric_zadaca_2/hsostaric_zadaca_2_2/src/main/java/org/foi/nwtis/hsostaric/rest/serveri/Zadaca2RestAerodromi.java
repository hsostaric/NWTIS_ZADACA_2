/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.hsostaric.rest.serveri;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.foi.nwtis.hsostaric.web.serveri.Aerodrom;

import org.foi.nwtis.hsostaric.ws.klijenti.Zadaca2_1WS;

import org.foi.nwtis.podaci.Odgovor;
import org.foi.nwtis.podaci.OdgovorAerodrom;

/**
 * Klasa koja predstavlja REST Web Service.
 *
 * @author Hrvoje Šoštarić
 */
@Path("aerodromi")
public class Zadaca2RestAerodromi {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Zadaca2RestAerodromi
     */
    public Zadaca2RestAerodromi() {
    }

    /**
     * Vraća aerodrome koje prati korisnik Retrieves representation of an
     * instance of org.foi.nwtis.hsostaric.rest.serveri.Zadaca2RestAerodromi
     *
     * @param korisnik
     * @param lozinka
     * @return an instance of Response
     */
    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajAerodomeKorisnika(
            @HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka) {
        Zadaca2_1WS zadaca2_1WS = new Zadaca2_1WS();
        List<Aerodrom> lista = zadaca2_1WS.dajAerodrome(korisnik, lozinka);
        if (lista == null
                || lista.isEmpty()) {
            lista = new ArrayList<>();
            return vratiOdgovor("40", "Operacija nije u redu ili korisnik nema aerodorma", lista);
        }
        return vratiOdgovor("10", "OK", lista);
    }

    /**
     * Vraća aerodrome koje prati korisnik.
     *
     * @param korisnik
     * @param lozinka
     * @param icao
     * @return an instance of Response
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response dodajAerodromKorisniku(
            @HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka,
            @JsonProperty("icao") String icao) {
        String icaoOcisceni;
        Boolean dodano = false;
        System.out.println(icao);
        icaoOcisceni = icao.
                substring(icao
                        .indexOf(":") + 1,
                        icao.lastIndexOf("}"))
                .trim()
                .replaceAll("\"", "");
        Zadaca2_1WS zadaca2_1WS = new Zadaca2_1WS();
        dodano = zadaca2_1WS.dodajAerodromKorisniku(korisnik, lozinka, icaoOcisceni);
        if (dodano == false) {
            return vratiOdgovor("40", "Došlo je do problema prilikom dodavanja aerodroma u listu",
                    new ArrayList());
        }
        return vratiOdgovor("10", "OK", new ArrayList());
    }
    
    /**
     * Vraća odgovor sa statusom, porukom i podacima koje primi.
     *
     * @param status
     * @param poruka
     * @param kolekcija
     * @return an instance of Response
     */
    public Response vratiOdgovor(String status, String poruka, List kolekcija) {
        Odgovor odgovor = new Odgovor();
        odgovor.setOdgovor(kolekcija.toArray());
        odgovor.setStatus(status);
        odgovor.setPoruka(poruka);
        return Response
                .ok(odgovor)
                .status(200)
                .build();
    }

    /**
     * Vraća aerodrome prema nazivu ili državi.
     *
     * @param korisnik
     * @param lozinka
     * @param naziv
     * @param drzava
     * @return an instance of Response
     */
    @Path("/svi")
    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajAerodrome(
            @HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka,
            @QueryParam("naziv") String naziv,
            @QueryParam("drzava") String drzava) {

        List<Aerodrom> lista = null;
        Zadaca2_1WS zadaca2_1WS = new Zadaca2_1WS();
        if (naziv != null && !naziv.isEmpty()) {
            lista = zadaca2_1WS.dajAerodromeNaziv(korisnik, lozinka, naziv);
        } else if (drzava != null && !drzava.isEmpty()) {
            lista = zadaca2_1WS.vratiAerodromeDrzave(korisnik, lozinka, drzava);
        } else {
            lista = zadaca2_1WS.dajAerodromeNaziv(korisnik, lozinka, "%");
        }
        if (lista == null || 
                lista.isEmpty()) {
        return vratiOdgovor("40", "Došlo je do problema prilikom pretraživanja/filtriranja sadrzaja",
                    new ArrayList());
        }
        return vratiOdgovor("10", "OK", lista);
    }

    /**
     * Vraća aerodrome koje prati korisnik Retrieves representation of an
     * instance of org.foi.nwtis.hsostaric.rest.serveri.Zadaca2RestAerodromi.
     *
     * @param korisnik
     * @param lozinka
     * @param icao
     * @return an instance of Response
     */
    @Path("{icao}")
    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response vratiAerodromKorisnika(
            @HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka,
            @PathParam("icao") String icao) {
        Zadaca2_1WS zadaca2_1WS = new Zadaca2_1WS();
        Aerodrom izabraniAerodrom = null;
        izabraniAerodrom= zadaca2_1WS.vratiAerodromKorisnika(korisnik, lozinka, icao);
        if (izabraniAerodrom == null) {
            return vratiOdgovor("40", "Operacija nije u redu ili korisnik nema aerodorma", new ArrayList() );
        }
        List<Aerodrom> listaAerodroma= new ArrayList<>();
        listaAerodroma.add(izabraniAerodrom);
        return vratiOdgovor("10", "OK",listaAerodroma);
    }
    
 

}
