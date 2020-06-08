/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.hsostaric.filteri;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.inject.Inject;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.foi.nwtis.hsostaric.web.zrna.PrijavaKorisnika;

/**
 * Klasa koja služi za kontrolu pristupa da neprijavljeni korisnici nemaju pravo
 * pristupa stranicama koje im nisu namjenjene i obrnuto za prijavljene
 * korisnike.
 *
 * @author Hrvoje-PC
 */
@WebFilter(filterName = "KontrolaPristupa", urlPatterns = {"/*"}, dispatcherTypes = {DispatcherType.REQUEST})
public class KontrolaPristupa implements Filter {

    private static final boolean debug = true;

    private FilterConfig filterConfig = null;

    @Inject
    PrijavaKorisnika prijavaKorisnika;

    public KontrolaPristupa() {
    }

    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {

    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String url = req.getRequestURI();
        if (prijavaKorisnika.getPrijavljen() == false
                || prijavaKorisnika == null) {
            if (url.indexOf("odjavaKorisnika.xhtml") >= 0
                    || url.indexOf("dodavanjeAerodroma.xhtml") >= 0
                    || url.indexOf("pregledAerodroma.xhtml") >= 0
                    || url.indexOf("pregledAviona.xhtml") >= 0) {
                resp.sendRedirect(req.getContextPath()
                        + "/prijavaKorisnika.xhtml");
            } else {
                chain.doFilter(request, response);
            }
        } else {
            filterPrijavljenogKorisnika(url, resp, req, chain, request, response);
        }
    }
/**
 *Metoda za kontrolu pristupa prijavljenih korisnika.
 */
    private void filterPrijavljenogKorisnika(String url, HttpServletResponse resp,
            HttpServletRequest req, FilterChain chain, ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        if (url.indexOf("prijavaKorisnika.xhtml") >= 0) {
            resp.sendRedirect(req.getContextPath() + "/index.xhtml");
        } else if (url.indexOf("odjavaKorisnika.xhtml") >= 0) {
            ocistiParametre();
            resp.sendRedirect(req.getContextPath() + "/prijavaKorisnika.xhtml");
        } else {
            chain.doFilter(request, response);
        }
    }

    private void ocistiParametre() {
        prijavaKorisnika.setKorisnickoIme("");
        prijavaKorisnika.setLozinka("");
        prijavaKorisnika.setPrijavljen(false);
    }

    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("KontrolaPristupa:Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("KontrolaPristupa()");
        }
        StringBuffer sb = new StringBuffer("KontrolaPristupa(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

}
