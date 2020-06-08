/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.hsostaric.web.slusaci;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.hsostaric.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.hsostaric.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.hsostaric.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.hsostaric.web.dretve.PreuzimanjeLetovaAvionaAerodroma;

/**
 * Klasa koja prestavlja slušač pokretanja aplikacije. Preuzima konfiguraciju
 * baze podatka i pokreće dretvu za preuzimanje podataka za avione.
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {

    PreuzimanjeLetovaAvionaAerodroma plaa = null;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        String datoteke = sc.getInitParameter("konfiguracija");
        String putanja = sc.getRealPath("WEB-INF") + File.separator + datoteke;
        try {
            BP_Konfiguracija konf = new BP_Konfiguracija(putanja);
            sc.setAttribute("BP_Konfig", konf);
            plaa = new PreuzimanjeLetovaAvionaAerodroma(konf);
            plaa.start();
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Aplikacija je pokrenuta");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (plaa != null) {
            plaa.interrupt();
            System.out.println("Gasim dretvu");
        }
        System.out.println("Aplikacija je zaustavljena.");
    }
}
