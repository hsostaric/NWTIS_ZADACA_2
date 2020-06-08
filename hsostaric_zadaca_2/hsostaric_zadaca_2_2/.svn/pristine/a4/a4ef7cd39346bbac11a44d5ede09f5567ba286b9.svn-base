/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.hsostaric.rest.serveri;

import java.util.ArrayList;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Odgovor;
import org.foi.nwtis.rest.podaci.Lokacija;

/**
 * REST Web Service
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
     * Retrieves representation of an instance of
     * org.foi.nwtis.hsostaric.rest.serveri.Zadaca2RestAerodromi
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
        java.util.List<Aerodrom> aerodromi = new ArrayList<>();
        aerodromi.add(new Aerodrom("LDAZ", "Zagreb", "HR", new Lokacija("0.0", "0.0")));
        aerodromi.add(new Aerodrom(
                "LOWW", "Vienna", "AT", new Lokacija("0.0", "0.0")));
        aerodromi.add(new Aerodrom(
                "EDDF", "Frankfurt/M", "AT", new Lokacija("0.0", "0.0")));
        Odgovor odgovor = new Odgovor();
        odgovor.setStatus("10");
        odgovor.setPoruka("OK");
        odgovor.setOdgovor(aerodromi.toArray());
        return Response
                .ok(odgovor)
                .status(200)
                .build();
    }


  
    /**
     * PUT method for updating or creating an instance of Zadaca2RestAerodromi
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(Response content) {
    }
}
