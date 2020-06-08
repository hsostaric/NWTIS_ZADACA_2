/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.hsostaric.web.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.hsostaric.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.Lokacija;

/**
 * Klasa koja služi za komuniciranje s bazom podataka za potrebu aerodroma.
 *
 * @author Hrvoje-PC
 */
public class AirportDAO {

    Connection connection;
    Statement statement;
    ResultSet resultSet;
    private BP_Konfiguracija bpk;

    public AirportDAO(BP_Konfiguracija bpk) {
        this.bpk = bpk;
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
     * Metoda koja dohvaća aerodrome koji se nalazi u tablici MYAIRPORTS.Dohvaća
     * aerodrome koje prate korisnici.
     *
     * @return kolekcija aerodroma
     */
    public List<Aerodrom> dohvatiAerodromekorisnika() {
        List<Aerodrom> aerodromiKorisnika = new ArrayList<>();
        Aerodrom aerodrom = null;
        try {
            connection = dajKonenkciju();
            statement = connection.createStatement();
            String sql = "SELECT DISTINCT IDENT FROM MYAIRPORTS";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                aerodrom = new Aerodrom();
                aerodrom.setIcao(resultSet.
                        getString("IDENT"));
                aerodromiKorisnika.add(aerodrom);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aerodromiKorisnika;
    }

    /**
     * Provjerava je li aerodrom za određen datum pohranjen u MyAirportsLog
     *
     * @param aerodrom aerodrom koji se provjerava
     * @param date datum za koji se provjerava let
     * @return stanje preuzetog aerodroma
     */
    public boolean provjeriPreuzetostPodatka(Aerodrom aerodrom, Date date) {
        int broj = 0;
        try {
            connection = dajKonenkciju(); 
            String sql = "SELECT COUNT(*) as broj FROM MYAIRPORTSLOG WHERE IDENT=?"
                    + "AND FLIGHTDATE =?";
            
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, aerodrom.getIcao());
            ps.setDate(2, new java.sql.Date(date.getTime()));
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                broj = resultSet.getInt("broj");
                break;
            }
            resultSet.close();
            ps.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return broj > 0;
    }

    /**
     * Metoda koja oblikuje datum u format yyyy-MM-dd.
     */
    private Date oblikujDatum(Date datum) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String format = formatter.format(datum);
        Date dat = new Date();
        try {
            dat = formatter.parse(format);
        } catch (ParseException ex) {
            System.out.println("Greska: " + ex);
        }
        return dat;
    }

    /**
     * Metoda koja evidentira aerodrom da je obrađen.
     *
     * @param aerodrom aerodrom koji se pohranjuje
     * @param datum datum koji se pohranjuje i evidentira
     * @return stanje evidentiranog aerodroma
     */
    public boolean evidentirajAerodrom(Aerodrom aerodrom, Date datum) {
        boolean unos = false;
        int broj = 0;
        try {
            connection = dajKonenkciju();
            String sql = "INSERT INTO MYAIRPORTSLOG (IDENT, FLIGHTDATE, STORED) VALUES(?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, aerodrom.getIcao());
            ps.setDate(2, new java.sql.Date(datum.getTime()));
            ps.setTimestamp(3, trenutnoVrijeme());
            broj = ps.executeUpdate();
            ps.close();
            connection.close();
            unos = (broj > 0);
            return unos;
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Greška: " + ex);
        }
        return unos;
    }

    /**
     * Metoda koja vraća trenutno vrijeme u Timestamp formatu.
     */
    private Timestamp trenutnoVrijeme() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Date dat;
        String datum = formatter.format(date);
        try {
            dat = formatter.parse(datum);
            return new Timestamp(dat.getTime());
        } catch (ParseException ex) {
            System.out.println("Greška: " + ex);
        }
        return null;
    }

    /**
     * Meotda koja vraća kolekciju aerodroma prema nazivu.
     *
     * @param naziv naziv za koji se pretražuje baza podataka
     * @return kolekcija vraćenih aerodroma
     */
    public List<Aerodrom> dajAerodromiPremaNazivu(String naziv) {
        Aerodrom aerodrom = null;
        List<Aerodrom> popisAerodroma = new ArrayList<>();
        try {
            connection = dajKonenkciju();
            statement = connection.createStatement();
            String sql = "SELECT *FROM AIRPORTS WHERE NAME LIKE '" + naziv.trim() + "'";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String icao = resultSet.getString("IDENT");
                String nazivAerodroma = resultSet.getString("NAME");
                String drzava = resultSet.getString("ISO_COUNTRY");
                Lokacija lokacija = urediLokaciju(resultSet.getString("COORDINATES"));
                aerodrom = new Aerodrom(icao, nazivAerodroma, drzava, lokacija);
                popisAerodroma.add(aerodrom);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Greška: " + ex);
        }
        return popisAerodroma;
    }

    /**
     * Metoda koja obrađuje dohvaćene geo kordinate iz baze podataka i
     * pohranjuje ih u objekt.
     */
    private Lokacija urediLokaciju(String lokacija) {
        String polje[] = lokacija.split(",");
        String longitude = polje[0].trim();
        String latidude = polje[1]
                .trim();
        Lokacija lok = new Lokacija(latidude, longitude);
        return lok;
    }

    /**
     * Metoda koja dohvaćuje aerodroma koji su iz tražene države.
     *
     * @param drzava država za koju se pretražuje
     * @return kolekcija vraćenih podataka
     */
    public List<Aerodrom> dohvatiAerodromeDrzave(String drzava) {
        List<Aerodrom> listaAerodroma = new ArrayList<>();
        Aerodrom aerodrom = null;
        try {
            connection = dajKonenkciju();
            statement = connection.createStatement();
            String sql = "SELECT *FROM AIRPORTS WHERE ISO_COUNTRY='" + drzava + "'";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String icao = resultSet.getString("IDENT");
                String nazivAerodroma = resultSet.getString("NAME");
                String isoCountry = resultSet.getString("ISO_COUNTRY");
                Lokacija lokacija = urediLokaciju(resultSet.getString("COORDINATES"));
                aerodrom = new Aerodrom(icao, nazivAerodroma, isoCountry, lokacija);
                listaAerodroma.add(aerodrom);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Greska: " + ex);
        }
        return listaAerodroma;
    }

    /**
     * Metoda koja dohvaća sve aerodroma za traženog korisnika.
     *
     * @param korime korisničko ime traženog korisnika
     * @return kolekcija traženih aerodroma
     */
    public List<Aerodrom> dohvatiAerodromeKorisnika(String korime) {
        List<Aerodrom> aerodromi = new ArrayList<>();
        Aerodrom aerodrom = null;
        try {
            connection = dajKonenkciju();
            statement = connection.createStatement();
            String sql = "SELECT  AIRPORTS.IDENT, AIRPORTS.\"NAME\", AIRPORTS.ISO_COUNTRY, AIRPORTS.COORDINATES FROM "
                    + "AIRPORTS,MYAIRPORTS WHERE MYAIRPORTS.IDENT=AIRPORTS.IDENT and MYAIRPORTS.USERNAME='" + korime + "'";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String icao = resultSet.getString("IDENT");
                String nazivAerodroma = resultSet.getString("NAME");
                String isoCountry = resultSet.getString("ISO_COUNTRY");
                Lokacija lokacija = urediLokaciju(resultSet.getString("COORDINATES"));
                aerodrom = new Aerodrom(icao, nazivAerodroma, isoCountry, lokacija);
                aerodromi.add(aerodrom);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Greska: " + ex);
        }
        return aerodromi;
    }

    /**
     * Metoda koja dohvaća aerodrom iz kolekcije za traženog korisnika.
     *
     * @param korisnickoIme korisničko ime za koji se pretražuje
     * @param icaoKod kod traženog aerodroma
     * @return traženi aerodrom
     */
    public Aerodrom dohvatiAerodromIzMojihAerodroma(String korisnickoIme, String icaoKod) {
        Aerodrom aerodrom = null;
        try {
            connection = dajKonenkciju();
            statement = connection.createStatement();
            String sql = "SELECT  AIRPORTS.IDENT, AIRPORTS.\"NAME\", AIRPORTS.ISO_COUNTRY, AIRPORTS.COORDINATES FROM "
                    + "AIRPORTS,MYAIRPORTS WHERE MYAIRPORTS.IDENT=AIRPORTS.IDENT AND MYAIRPORTS.USERNAME='" + korisnickoIme + "' "
                    + "AND MYAIRPORTS.IDENT='" + icaoKod + "'";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String icao = resultSet.getString("IDENT");
                String nazivAerodroma = resultSet.getString("NAME");
                String isoCountry = resultSet.getString("ISO_COUNTRY");
                Lokacija lokacija = urediLokaciju(resultSet.getString("COORDINATES"));
                aerodrom = new Aerodrom(icao, nazivAerodroma, isoCountry, lokacija);
                break;
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Greska: " + ex);
        }
        return aerodrom;
    }

    /**
     * Metoda koja dodaje aerodrom u korisnikovu kolekciju aerodroma.
     *
     * @param ident kod aerodroma
     * @param korisnickoIme korisničko ime korisnika za koji se aerodrom
     * evidentira
     * @return status je li dodan ili nije
     */
    public boolean dodajAerodromUMojuKolekciju(String ident, String korisnickoIme) {
        Boolean dodan = false;
        Aerodrom aerodrom;
        aerodrom = dohvatiAerodromPremaIdentu(ident);
        if (aerodrom != null) {
            try {
                connection = dajKonenkciju();
                statement = connection.createStatement();
                String sql = "INSERT INTO MYAIRPORTS(ID, IDENT, USERNAME, STORED) VALUES"
                        + "(DEFAULT, '" + aerodrom.getIcao() + "','" + korisnickoIme + "', '" + trenutnoVrijeme() + "')";
                int broj = statement.executeUpdate(sql);
                dodan = (broj == 1);
                return dodan;
            } catch (ClassNotFoundException | SQLException ex) {
                System.out.println("Greska: " + ex);
            }
        }
        return dodan;
    }

    /**
     * Metoda koja vraća aerodrorm iz baze podataka prema identifikatoru.
     */
    private Aerodrom dohvatiAerodromPremaIdentu(String ident) {
        Aerodrom aerodrom = null;
        try {
            connection = dajKonenkciju();
            statement = connection.createStatement();
            String sql = "SELECT *FROM AIRPORTS Where IDENT='" + ident + "'";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String icao = resultSet.getString("IDENT");
                String nazivAerodroma = resultSet.getString("NAME");
                String isoCountry = resultSet.getString("ISO_COUNTRY");
                Lokacija lokacija = urediLokaciju(resultSet.getString("COORDINATES"));
                aerodrom = new Aerodrom(icao, nazivAerodroma, isoCountry, lokacija);
                break;
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Greška " + ex);
        }
        return aerodrom;
    }

    /**
     * Metoda koja vraća popis zrakoplova koji su poletjeli sa traženog
     * aerodorma u zadanom intervalu.
     *
     * @param ICAO kod aerodroma
     * @param od početak intervala u sekundama
     * @param kraj završni interval u sekundama
     * @return kolekcija trakoplova.
     */
    public List<AvionLeti> poletjeliAvioniAerodrom(String ICAO, long od, long kraj) {
        List<AvionLeti> avioni = new ArrayList<>();
        AvionLeti avion = null;
        try {
            connection = dajKonenkciju();
            String sql = "Select *from AIRPLANES WHERE FIRSTSEEN BETWEEN ? AND ? AND ESTDEPARTUREAIRPORT = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, od);
            ps.setLong(2, kraj);
            ps.setString(3, ICAO);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                avion = new AvionLeti();
                avion = dajAvion();
                avioni.add(avion);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Greška: " + ex);
        }
        return avioni;
    }

    /**
     * Metoda koja sastavlja objekt aviona.
     */
    private AvionLeti dajAvion() throws SQLException {
        AvionLeti avion = new AvionLeti();
        avion.setIcao24(resultSet.
                getString("icao24"));
        avion.setFirstSeen(resultSet.
                getInt("firstSeen"));
        avion.setEstDepartureAirport(resultSet
                .getString("estDepartureAirport"));
        avion.setLastSeen(resultSet.
                getInt("lastSeen"));
        avion.setEstArrivalAirport(resultSet.
                getString("estArrivalAirport"));
        avion.setCallsign(resultSet.getString("callsign"));
        avion.setEstDepartureAirportHorizDistance(resultSet
                .getInt("estDepartureAirportHorizDistance"));
        avion.setEstDepartureAirportVertDistance(resultSet
                .getInt("estDepartureAirportVertDistance"));
        avion.setEstArrivalAirportHorizDistance(resultSet
                .getInt("estArrivalAirportHorizDistance"));
        avion.setEstArrivalAirportVertDistance(resultSet
                .getInt("estArrivalAirportVertDistance"));
        avion.setDepartureAirportCandidatesCount(resultSet
                .getInt("departureAirportCandidatesCount"));
        avion.setArrivalAirportCandidatesCount(resultSet
                .getInt("arrivalAirportCandidatesCount"));
        return avion;
    }

}
