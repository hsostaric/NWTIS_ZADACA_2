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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.foi.nwtis.hsostaric.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.AvionLeti;

/**
 * Klasa koja služi za komuniciranje s bazom podataka za potrebu aviona.
 *
 * @author Hrvoje-PC
 */
public class AirplaneDAO {

    Connection connection;
    Statement statement;
    ResultSet resultSet;
    private Aerodrom aerodrom;
    private BP_Konfiguracija bpk;

    public AirplaneDAO(BP_Konfiguracija bpk) {
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
     * Metoda koja unosi avion u bazu podataka.
     *
     * @param avion objekt koji sadrži podatke o avionima za bazu podataka
     * @param datum
     * @return uspješnost operacije unosa
     */
    public boolean UnesiAvion(AvionLeti avion, Date datum) {
        boolean uneseno = false;
        int broj = 0;
        try {
            connection = dajKonenkciju();
            String sql = "INSERT INTO AIRPLANES ( icao24 , firstSeen , estDepartureAirport, lastSeen , estArrivalAirport , callsign ,"
                    + " estDepartureAirportHorizDistance , estDepartureAirportVertDistance , estArrivalAirportHorizDistance ,"
                    + " estArrivalAirportVertDistance , departureAirportCandidatesCount , arrivalAirportCandidatesCount , STORED) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement pstate = connection.prepareStatement(sql);
            unosAviona(pstate, avion, datum);
            broj = pstate.executeUpdate();
            uneseno = (broj == 1);
            pstate.close();
            connection.close();
            return uneseno;
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Greška kod unosa aviona: " + ex);
        }
        return uneseno;
    }

    /**
     * Metoda koja pridružuje vrijednosti pripremljenim statementima za upit
     * unosa aviona.
     */
    private void unosAviona(PreparedStatement ps, AvionLeti avion, Date datum) throws SQLException {
        ps.setString(1, avion.getIcao24());
        ps.setInt(2, avion.getFirstSeen());
        ps.setString(3, avion.getEstDepartureAirport());
        ps.setInt(4, avion.getLastSeen());
        ps.setString(5, avion.getEstArrivalAirport());
        ps.setString(6, avion.getCallsign());
        ps.setInt(7, avion.getEstDepartureAirportHorizDistance());
        ps.setInt(8, avion.getEstDepartureAirportVertDistance());
        ps.setInt(9, avion.getEstArrivalAirportHorizDistance());
        ps.setInt(10, avion.getEstArrivalAirportVertDistance());
        ps.setInt(11, avion.getDepartureAirportCandidatesCount());
        ps.setInt(12, avion.getArrivalAirportCandidatesCount());
        ps.setTimestamp(13, trenutnoVrijeme());

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
}
