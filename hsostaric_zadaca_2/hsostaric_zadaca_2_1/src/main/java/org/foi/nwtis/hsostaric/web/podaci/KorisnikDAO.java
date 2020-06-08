/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.hsostaric.web.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.hsostaric.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.podaci.Korisnik;

/**
 * Klasa koja služi za komuniciranje s bazom podataka za korisnike sustava.
 *
 * @author Hrvoje Šoštarić
 */
public class KorisnikDAO {

    Connection connection;
    Statement statement;
    ResultSet resultSet;
    private BP_Konfiguracija bpk;

    public KorisnikDAO(BP_Konfiguracija bpk) {
        this.bpk = bpk;
    }

    public KorisnikDAO() {
    }

    /**
     * Metoda koja vraća konekciju za bazu podataka. U njoj se dohvaćaju driveri
     * i podaci za uspostavu veze s bazom podataka.
     */
    private Connection dajKonenkciju() throws ClassNotFoundException, SQLException {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        Class.forName(bpk.getDriverDatabase(url));
        return DriverManager.getConnection(url, bpkorisnik, bplozinka);
    }

    /**
     * Metoda koja dohvaća sve korisnike iz baze podataka.
     *
     * @return kolekcija korisnika iz baze podataka.
     */
    public List<Korisnik> dohvatiSveKorisnike() {
        List<Korisnik> popisKorisnika = new ArrayList<>();
        Korisnik korisnik = null;
        try {
            connection = dajKonenkciju();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT *FROM KORISNICI");
            while (resultSet.next()) {
                String korime = resultSet.getString("kor_ime");
                String ime = resultSet.getString("ime");
                String prezime = resultSet.getString("prezime");
                String email = resultSet.getString("EMAIL_ADRESA");
                Timestamp kreiran = resultSet.getTimestamp("datum_kreiranja");
                Timestamp promjena = resultSet.getTimestamp("datum_promjene");
                korisnik = new Korisnik(korime, ime, prezime, "******", email, kreiran, promjena);
                popisKorisnika.add(korisnik);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Greška: " + ex);
        }
        return popisKorisnika;
    }

    /**
     * Metoda koja služi za autentifikaciju korisnika.
     *
     * @param username korisničko ime korisnika.
     * @param password lozinka korisnika.
     * @return korisnik
     */
    public Korisnik autentificirajKorisnika(String username, String password) {
        Korisnik k = null;
        try {
            try (Connection con = dajKonenkciju();
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery("Select *from Korisnici where KOR_IME='" + username + "' AND "
                            + "LOZINKA = '" + password + "'")) {
                while (rs.next()) {
                    String korime = rs.getString("kor_ime");
                    String ime = rs.getString("ime");
                    String prezime = rs.getString("prezime");
                    String email = rs.getString("EMAIL_ADRESA");
                    String lozinka = rs.getString("LOZINKA");
                    Timestamp kreiran = rs.getTimestamp("datum_kreiranja");
                    Timestamp promjena = rs.getTimestamp("datum_promjene");
                    k = new Korisnik(korime, ime, prezime, lozinka, email, kreiran, promjena);
                    return k;
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Greška: " + ex);
        }
        return k;
    }

    /**
     * Metoda koja dodaje korisnika u bazu podataka.
     *
     * @param k korisnik koji se dodaje.
     * @return uspješnost operacije.
     */
    public boolean dodajKorisnika(Korisnik k) {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String upit = "INSERT INTO korisnici (IME, PREZIME, EMAIL_ADRESA, KOR_IME, LOZINKA, DATUM_KREIRANJA, DATUM_PROMJENE) VALUES ("
                + "'" + k.getIme() + "', '" + k.getPrezime()
                + "', '" + k.getEmailAdresa() + "', '" + k.getKorIme() + "', '" + k.getLozinka() + "', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        try (
                Connection con = dajKonenkciju();
                Statement s = con.createStatement()) {
            int brojAzuriranja = s.executeUpdate(upit);
            return brojAzuriranja == 1;
        } catch (SQLException ex) {
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Metoda koja dohvaća korisnike sa aerodromima.
     *
     * @return kolekcija korisnika.
     */
    public List<Korisnik> dohvatiKorisnikeSAerodromima() {
        List<Korisnik> popisKorisnika = new ArrayList<>();
        Korisnik korisnik = null;
        try {
            String sql = dajUpit();
            connection = dajKonenkciju();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String korime = resultSet.getString("kor_ime");
                String ime = resultSet.getString("ime");
                String prezime = resultSet.getString("prezime");
                String email = resultSet.getString("EMAIL_ADRESA");
                String lozinka = resultSet.getString("LOZINKA");
                Timestamp kreiran = resultSet.getTimestamp("datum_kreiranja");
                Timestamp promjena = resultSet.getTimestamp("datum_promjene");
                korisnik = new Korisnik(korime, ime, prezime, lozinka, email, kreiran, promjena);
                popisKorisnika.add(korisnik);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Greška: " + ex);
        }
        return popisKorisnika;
    }

    /**
     * Upit za dohvat korisnika sa aerodromima.
     */
    private String dajUpit() {
        String sql = "SELECT DISTINCT KORISNICI.IME, KORISNICI.PREZIME, KORISNICI.KOR_IME, KORISNICI.LOZINKA, "
                + "KORISNICI.EMAIL_ADRESA, KORISNICI.DATUM_KREIRANJA, KORISNICI.DATUM_PROMJENE\n"
                + " FROM KORISNICI, MYAIRPORTS WHERE KORISNICI.KOR_IME = MYAIRPORTS.USERNAME";
        return sql;
    }
}
