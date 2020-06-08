/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.hsostaric.rest.klijenti;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Klijentska klasa generirana za svrhu Web service clienta.
 *
 * @author Hrvoje-PC
 */
public class Zadaca2_2RS {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/hsostaric_zadaca_2_2/webresources";
    private String korisnik;
    private String lozinka;

    public Zadaca2_2RS() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("aerodromi");
    }

    /**
     * Konstruktor preko kojeg šaljemo podatke za korisničko ime i lozinku.
     *
     * @param korisnik
     * @param lozinka
     */
    public Zadaca2_2RS(String korisnik, String lozinka) {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("aerodromi");
        this.korisnik = korisnik;
        this.lozinka = lozinka;
    }

    /**
     * Metoda za slanje i primanje zahtjeva za aerodrome prijavljenog korisnika.
     *
     * @param <T>
     * @param responseType
     * @return
     */
    public <T> T dajAerodomeKorisnika(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .header("korisnik", korisnik)
                .header("lozinka", lozinka)
                .get(responseType);
    }

    /**
     * Metoda za dodavanje aerodroma korisniku u njegovu kolekciju.POST metodom
     * se šalje identifikator aerodroma zajedno sa korisničkim imenom i
     * lozinkom.
     *
     * @param requestEntity
     * @return
     */
    public Response dodajAerodromKorisniku(Object requestEntity) throws ClientErrorException {
        return webTarget.request(MediaType.APPLICATION_JSON)
                .header("korisnik", korisnik).
                header("lozinka", lozinka).
                post(Entity.entity(requestEntity, MediaType.APPLICATION_JSON), Response.class);
    }

    /**
     * Metoda kojom dohvaćamo aerodrome iz određene države ili prema nazivu.
     *
     * @param <T>
     * @param responseType
     * @param drzava
     * @param naziv
     * @return
     */
    public <T> T dajAerodrome(Class<T> responseType, String drzava, String naziv) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("svi");
        if (!drzava.equals("")) {
            resource = resource.queryParam("drzava", drzava);
        }
        if (!naziv.equals("")) {
            resource = resource.queryParam("naziv", naziv);

        }
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).header("korisnik", korisnik)
                .header("lozinka", lozinka)
                .get(responseType);
    }

    public void close() {
        client.close();
    }

}
