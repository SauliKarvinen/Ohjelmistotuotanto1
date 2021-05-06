/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.tietokanta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import vuoto.luokkafilet.Toimipiste;
import vuoto.luokkafilet.Asiakas;
import vuoto.luokkafilet.Laite;
import vuoto.luokkafilet.Lasku;
import vuoto.luokkafilet.Palvelu;
import vuoto.luokkafilet.Taulut;
import vuoto.luokkafilet.Toimitila;
import vuoto.luokkafilet.Varaus;
import vuoto.luokkafilet.VarausOlio;



/**
 * Database connection to opskure database
 *
 * @author marko
 */
public class DBAccess {

    // Statementit ja resultset luotu jo tässä alusssa
    private Connection conn = null;
    private Statement stmt = null;
    private PreparedStatement ps = null;
    private ResultSet results = null;
    private final String URL = "jdbc:mariadb://maria.westeurope.cloudapp.azure.com";
    //private String localUrl = "jdbc:mariadb://localhost:3306"; // Paikallisella koneella

    /**
     * Parametriton konstruktori
     */
    public DBAccess() {

    }

    /**
     * Creates connection to database.
     *
     * @param dbURL
     * @param user
     * @param password
     * @throws SQLException
     * @throws ClassNotFoundException
     */

    public DBAccess(String dbURL, String user, String password) throws SQLException, ClassNotFoundException {
        conn = DriverManager.getConnection(dbURL, user, password);

    }


    /**
     * Luo yhteyden tietokantaan
     * @return Tietokantayhteys (Connection)
     */
    public Connection yhdista() {

        try {

            conn = DriverManager.getConnection(URL, "opiskelija", "opiskelija1");
            System.out.println("\t>>> Yhteys muodostettu");
            kaytaTietokantaa();

        } catch (SQLException e) { // tietokantaan ei saada yhteyttä
            conn = null;

            heitaVirhe("Virhe yhdistettäessä tietokantaan");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, e);
        }

        return conn;
    }

    /**
     * Katkaisee yhteyden tietokantaan
     */
    public void katkaiseYhteys() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa tietokantaa");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("\t>>> Yhteys suljettu");
        }
    }

    /**
     * Ilmoittaa tietokannalle että käytetään tätä kyseistä tietokantaa
     */
    private void kaytaTietokantaa() {
        try {
            stmt = conn.createStatement();
            stmt.executeQuery("USE Karelia_Ohjelmistotuotanto_R19");
            System.out.println("\t>>>Käytetään tietokantaa Karelia_Ohjelmistotuotanto_R19");

        } catch (SQLException ex) {
            heitaVirhe("Virhe yrittäessä käyttää tietokantaa");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /** T O I M I P I S T E E T
     * Lisää toimipisteen tietokantaan
     * @param t Lisättävä toimipiste
     */
    public void lisaaToimipiste(Toimipiste t) {

        try {
            yhdista();
            ps = conn.prepareStatement("INSERT INTO Toimipisteet (lahiosoite, Postinumero, toimipistenimi, kuvaus) "
                    + "VALUES (?, ?, ?, ?);");
            ps.setString(1, t.getLahiosoite());
            ps.setString(2, t.getPostinumero());
            ps.setString(3, t.getToimipistenimi());
            ps.setString(4, t.getKuvaus());

            ps.execute();

        } catch (SQLException e) {
            heitaVirhe("Virhe lisätessä toimipistettä");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                ps.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyä");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
            katkaiseYhteys();
        }
    }

    /**
     * Palauttaa ObservableListin kaikista toimipisteistä
     * @return ObservableList toimipisteet
     */
    public ObservableList<Toimipiste> haeKaikkiToimipisteet() {
        
        ObservableList<Toimipiste> toimipisteet = FXCollections.observableArrayList();

        try {
            yhdista();
            ps = conn.prepareStatement("SELECT toimipisteId, lahiosoite, postinumero, toimipisteNimi, kuvaus FROM Toimipisteet;");
            results = ps.executeQuery();

            while (results.next()) {
                toimipisteet.add(new Toimipiste(results.getInt("toimipisteId"), results.getString("lahiosoite"), results.getString("postinumero"), results.getString("toimipisteNimi"), results.getString("kuvaus")));
            }

        } catch (SQLException ex) {
            heitaVirhe("Virhe haettaessa toimipisteitä");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ps.close();
                results.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyä (haeKaikkiToimipisteet)");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            katkaiseYhteys();
        }
        
        return toimipisteet;
        
    }

    /**
     * Hakee toimipisteen syötetyllä nimellä ja palauttaa sen
     * @param nimi
     * @return 
     */
    public Toimipiste haeToimipisteNimella(String nimi) {
        Toimipiste t = null;
        
        try {
            yhdista();
            ps = conn.prepareStatement("SELECT * FROM Toimipisteet WHERE toimipisteNimi = (?) LIMIT 1;");
            ps.setString(1, nimi);

            results = ps.executeQuery();

            results.next();
            t = new Toimipiste(results.getInt("toimipisteId"), results.getString("lahiosoite"), results.getString("postinumero"), results.getString("toimipisteNimi"), results.getString("kuvaus"));

        } catch (SQLException ex) {
            heitaVirhe("Virhe haettaessa toimipistettä nimellä");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            katkaiseYhteys();
            
            try {
                ps.close();
                results.close();
            } catch (SQLException ex) { 
                heitaVirhe("Virhe suljettaessa kyselyä (haeToimipisteNimella)");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return t;
    }

    public ObservableList<VarausOlio> haeTiedotVarausIkkunaan(String tilanNimi) {
        
        ObservableList<VarausOlio> varaukset = FXCollections.observableArrayList();
        int id = 0;
        LocalDate alku = null;
        LocalDate loppu = null;
        String asiakas = "";
        String tila = "";
        String toimipiste = "";
        
        try {
            yhdista();
            
            ps = conn.prepareStatement("SELECT v.varausId, v.vuokraAlku, v.vuokraLoppu, a.etunimi, a.sukunimi, tt.tilanNimi, tp.toimipisteNimi " +
                    "FROM Varaus v, Asiakas a, Tilat tt, Toimipisteet tp " +
                    "WHERE tt.tilanNimi = ? AND v.tilaId = tt.tilaId AND tt.toimipisteId = tp.toimipisteId AND v.asiakasId = a.asiakasId;");
            ps.setString(1, tilanNimi);
            
            results = ps.executeQuery();
            
            while(results.next()) {
                id = results.getInt("varausId");
                alku = results.getDate("vuokraAlku").toLocalDate();
                loppu = results.getDate("vuokraLoppu").toLocalDate();
                asiakas = results.getString("etunimi") + " " + results.getString("sukunimi");
                tila = results.getString("tilanNimi");
                toimipiste = results.getString("toimipisteNimi");
                varaukset.add(new VarausOlio(id, alku, loppu, asiakas, tila, toimipiste));
                
            }
        } catch (SQLException ex) {
            heitaVirhe("Virhe hakiessa tietoja varausikkunaan");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            katkaiseYhteys();
            try {
                ps.close();
                results.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return varaukset;
    }
    
    public ObservableList<VarausOlio> haeTiedotVarausIkkunaanAikavalilta(String tilanNimi, LocalDate alkuPaiva, LocalDate loppuPaiva) {
        
        ObservableList<VarausOlio> varaukset = FXCollections.observableArrayList();
        int id = 0;
        LocalDate alku = null;
        LocalDate loppu = null;
        String asiakas = "";
        String tila = "";
        String toimipiste = "";
        
        try {
            yhdista();
            
            ps = conn.prepareStatement("SELECT v.varausId, v.vuokraAlku, v.vuokraLoppu, a.etunimi, a.sukunimi, tt.tilanNimi, tp.toimipisteNimi " +
                    "FROM Varaus v, Asiakas a, Tilat tt, Toimipisteet tp " +
                    "WHERE tt.tilanNimi = ? AND v.tilaId = tt.tilaId AND tt.toimipisteId = tp.toimipisteId AND v.asiakasId = a.asiakasId "
                    + "AND v.vuokraAlku >= ? AND v.vuokraLoppu <= ?;");
            ps.setString(1, tilanNimi);
            ps.setDate(2, Date.valueOf(alkuPaiva));
            ps.setDate(3, Date.valueOf(loppuPaiva));
            
            results = ps.executeQuery();
            
            while(results.next()) {
                id = results.getInt("varausId");
                alku = results.getDate("vuokraAlku").toLocalDate();
                loppu = results.getDate("vuokraLoppu").toLocalDate();
                asiakas = results.getString("etunimi") + " " + results.getString("sukunimi");
                tila = results.getString("tilanNimi");
                toimipiste = results.getString("toimipisteNimi");
                varaukset.add(new VarausOlio(id, alku, loppu, asiakas, tila, toimipiste));
                
            }
        } catch (SQLException ex) {
            heitaVirhe("Virhe hakiessa tietoja varausikkunaan");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            katkaiseYhteys();
            try {
                ps.close();
                results.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return varaukset;
        
    }
    
    /**
     * Poistaa toimipisteen
     * @param t Poistettava toimipiste
     * @throws SQLException 
     */
    public void poistaToimipiste(Toimipiste t) throws SQLException {
        
        try {
            yhdista();
            ps = conn.prepareStatement("DELETE FROM Toimipisteet WHERE toimipisteId = ?");
            ps.setInt(1, t.getToimipisteID());
            ps.execute();
        } catch (SQLException ex) {        
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } finally {
            katkaiseYhteys();
            
            try {
                ps.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyä (poistaToimipiste)");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
      
    /**
     * Lisää uuden toimitilan tietokantaan
     * @param t Toimitila
     * @throws SQLException SQL Virhe
     */
    public void lisaaToimitila(Toimitila t) throws SQLException {
        
        try {
            yhdista();
            
            // Lisää uusi kiinteistö -näkymässä käyttäjä syöttää toimitilan nimen. Tässä haetaan toimipisteen nimellä toimipisteen id-numero (Viimeinen parametri)
            ps = conn.prepareStatement("INSERT INTO Tilat " +
                    "(lahiosoite, postinumero, postitoimipaikka, huonekoko, hintaPvm, huoneistonTila, kuvaus, tilanNimi, toimipisteId) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            
            ps.setString(1, t.getLahiosoite());
            ps.setString(2, t.getPostinumero());
            ps.setString(3, t.getPostitoimipaikka());
            ps.setInt(4, t.getHuonekoko());
            ps.setInt(5, t.getHintaPvm());
            ps.setInt(6, t.getHuoneistonTila());
            ps.setString(7, t.getKuvaus());
            ps.setString(8, t.getTilanNimi());
            ps.setInt(9, t.getToimipisteId());
            
            
            
            ps.executeQuery();
            
        } catch (SQLException ex) {
            heitaVirhe("Virhe lisätettä toimitilaa tietokantaan");
            throw ex;

        } finally {
            katkaiseYhteys();
            try {
                ps.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyä (lisaaToimitila)");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            
        }
        
        
    }
    
    /**
     * Hakee kaikki toimitilat tietokannasta ja palauttaa ObservableList listan
     * @return ObservableList toimitiloista
     */
    public ObservableList<Toimitila> haeKaikkiToimitilat(){
        
        ObservableList<Toimitila> toimitilat = FXCollections.observableArrayList();
     
        try {
            yhdista();
            stmt = conn.createStatement();
            
            results = stmt.executeQuery("SELECT * FROM Tilat;");
 
            while(results.next()) {
                toimitilat.add(new Toimitila(results.getInt("tilaId"), results.getString("lahiosoite"), results.getString("postinumero"), results.getString("postitoimipaikka"), results.getInt("huonekoko"), results.getInt("hintaPvm"), results.getInt("huoneistonTila"), results.getString("kuvaus"), results.getString("tilanNimi"), results.getInt("toimipisteId")));
            }
        } catch (SQLException ex) {
            heitaVirhe("Virhe hakiessa kaikkia toimitiloja tietokannasta");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
                results.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyä (haeKaikkiToimitilat)");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            katkaiseYhteys();
        }
        
        return toimitilat;
        
    }
    
    /**
     * Hakee halutun toimipisteen toimitilat
     * @param toimipiste Haluttu toimipiste
     * @return ObservableList toimitiloista
     */
    public ObservableList<Toimitila> haeToimitilatToimipisteesta(String toimipiste) {
        
        ObservableList<Toimitila> toimitilat = FXCollections.observableArrayList();
        
        try {
            yhdista();
            ps = conn.prepareStatement("SELECT * FROM Tilat WHERE toimipisteId = (SELECT toimipisteId FROM Toimipisteet WHERE toimipisteNimi = (?))");
            ps.setString(1, toimipiste);
            
            results = ps.executeQuery();
            
            while(results.next()) {
                toimitilat.add(new Toimitila(results.getInt("tilaId"), results.getString("lahiosoite"), results.getString("postinumero"), results.getString("postitoimipaikka"), results.getInt("huonekoko"), results.getInt("hintaPvm"), results.getInt("huoneistonTila"), results.getString("kuvaus"), results.getString("tilanNimi"), results.getInt("toimipisteId")));
            }
        } catch (SQLException ex) {
            heitaVirhe("Virhe haettaessa toimitiloja toimipisteestä " + toimipiste);
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            katkaiseYhteys();
            try {
                ps.close();
                results.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyä (haeToimitilatToimipisteestä)");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
     
        }
        
        return toimitilat;
    }

    /**
     * Poistaa toimitilan
     * @param t Poistettava toimitila
     * @throws SQLException SQL virhe
     */
    public void poistaToimitila(Toimitila t) throws SQLException {
        
        try {
            yhdista();
            ps = conn.prepareStatement("DELETE FROM Tilat WHERE tilaId = ?");
            ps.setInt(1, t.getTilaId());
            ps.execute();
        } catch (SQLException ex) {        
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } finally {
            katkaiseYhteys();
            
            try {
                ps.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyä (poistaToimitila)");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

 

    /**  A S I A K K U U D E T
     * */
    
    
    /**
     * Lisää UUSI ASIAKAS tietokantaan
     * @param a Lisättävä asiakas
     */
    public void lisaaAsiakas(Asiakas a) {

        try {
            yhdista();
            ps = conn.prepareStatement("INSERT INTO Asiakas (etunimi, sukunimi, lahiosoite, postinumero, puhelinnumero, sahkoposti, yrityksenNimi) "
                    + "VALUES (?, ?, ?, ?, ?, ? ,?);");
            ps.setString(1, a.getEtunimi());
            ps.setString(2, a.getSukunimi());
            ps.setString(3, a.getLahiosoite());
            ps.setString(4, a.getPostinumero());
            ps.setString(5, a.getPuhelinnumero());
            ps.setString(6, a.getSahkoposti());
            ps.setString(7, a.getYrityksenNimi());
            

            ps.execute();

        } catch (SQLException e) {
            heitaVirhe("Virhe lisätessä asiakasta");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                ps.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyä");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
            katkaiseYhteys();
        }
    }
    
     /**
     * Hakee KAIKKI ASIAKKAAT tietokannasta ja palauttaa ObservableList listan
     * @return ObservableList asiakkaista.
     */
    public ObservableList<Asiakas> haeKaikkiAsiakkaat(){
        
        ObservableList<Asiakas> asiakkaat = FXCollections.observableArrayList();
     
        try {
            yhdista();
            stmt = conn.createStatement();
            
            results = stmt.executeQuery("SELECT * FROM Asiakas;");
 
            while(results.next()) {
                asiakkaat.add(new Asiakas(
                        results.getInt("asiakasId"), 
                        results.getString("etunimi"), 
                        results.getString("sukunimi"), 
                        results.getString("lahiosoite"), 
                        results.getString("postinumero"), 
                        results.getString("puhelinnumero"), 
                        results.getString("sahkoposti"), 
                        results.getString("yrityksenNimi")));
            }
        } catch (SQLException ex) {
            heitaVirhe("Virhe hakiessa kaikkia asiakkaita tietokannasta");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
                results.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            katkaiseYhteys();
        }
        
        return asiakkaat;
        
    }

     /**
     * Hakee yrityksen/ASIAKKAAN tiedot tietokannasta ja palauttaa ObservableList listan
     * @param a Haettava yritys
     * @return ObservableList asiakkaista.
     */
    public ObservableList<Asiakas> haeAsiakas(String a){
        
        ObservableList<Asiakas> asiakas = FXCollections.observableArrayList();
        int asiakasId = 0;
        String etunimi = "";
        String sukunimi = "";
        String lahiosoite = "";
        String postinumero = "";
        String puhelinnumero = "";
        String sahkoposti = "";
        String yrityksenNimi = "";
                
        try {
            yhdista();
            stmt = conn.createStatement();
            
            ps = conn.prepareStatement("SELECT * FROM Asiakas WHERE yrityksenNimi = ?;");
            ps.setString(1, a);
            
            results = ps.executeQuery();
            while(results.next()) {
                asiakasId = results.getInt("asiakasId"); 
                etunimi = results.getString("etunimi");
                sukunimi = results.getString("sukunimi");
                lahiosoite = results.getString("lahiosoite"); 
                postinumero = results.getString("postinumero"); 
                puhelinnumero = results.getString("puhelinnumero"); 
                sahkoposti = results.getString("sahkoposti"); 
                yrityksenNimi = results.getString("yrityksenNimi");
                
                asiakas.add(new Asiakas(asiakasId, etunimi, sukunimi, lahiosoite, postinumero, puhelinnumero, sahkoposti, yrityksenNimi));
            }
            
        } catch (SQLException ex) {
            heitaVirhe("Virhe hakiessa asiakasta tietokannasta");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
                ps.close();
                results.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            katkaiseYhteys();
        }
        
        return asiakas;
        
    }

    /** L A S K U T U S * *
     **/
    
    
    /**
     * Lisää UUSI LASKU tietokantaan
     * @param la Lisättävä lasku
     */
    
    public void lisaaUusiLasku(Lasku la) {

        try {
            yhdista();
            ps = conn.prepareStatement("INSERT INTO Lasku (tyyppi, hinta, varausId) "
                    + "VALUES (? ,?, ?);");
            ps.setString(1, la.getLaskunTyyppi());
            ps.setInt(2, la.getHinta());
            ps.setInt(3, la.getVarausId());
            
            ps.execute();

        } catch (SQLException e) {
            heitaVirhe("Virhe lisätessä laskua");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                ps.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyä");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
            katkaiseYhteys();
        }
    }


    /** V A R A U K S E T
     * Lisää varauksen tietokantaan
     * @param v Lisättävä varaus
     */
    public void lisaaVaraus(Varaus v) {

        
        try {
            yhdista();
            ps = conn.prepareStatement("INSERT INTO Varaus (varausId, vuokraAlku, vuokraLoppu, tilaId, asiakasId, palveluvarausId, laitevarausId) "

                    + "VALUES (?, ?, ?, ?, ?, null, null);");
            ps.setInt(1, v.getVarausId());
            ps.setDate(2,   Date.valueOf(v.getVuokraAlku()));
            ps.setDate(3,   Date.valueOf(v.getVuokraLoppu()));
            ps.setInt(4, v.getTilaId());
            ps.setInt(5, v.getAsiakasId());
//            ps.setInt(6, v.getPalveluvarausId());
//            ps.setInt(7, v.getLaitevarausId());


            ps.execute();

        } catch (SQLException e) {
            heitaVirhe("Virhe lisätessä varausta");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                ps.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyä");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
            katkaiseYhteys();
        }
    }
    
    /**
     * Palauttaa lisätyn varauksen ID:n
     * @param v Haettava varaus
     * @return Varauksen ID
     */
    public int haeLisattyVarausId(Varaus v) {
        
        int palautettavaId = 0;
        
        try {
            yhdista();
            
            ps = conn.prepareStatement("SELECT varausId " +
                    "FROM Varaus WHERE vuokraAlku = ? AND vuokraLoppu = ? AND tilaId = ? AND asiakasId = ? LIMIT 1");
            
            ps.setDate(1, Date.valueOf(v.getVuokraAlku()));
            ps.setDate(2, Date.valueOf(v.getVuokraLoppu()));
            ps.setInt(3, v.getTilaId());
            ps.setInt(4, v.getAsiakasId());
            
            results = ps.executeQuery();
            
            results.next();
            
            palautettavaId = results.getInt("varausId");
            
        } catch (SQLException ex) {
            heitaVirhe("Virhe hakiessa varauksen ID:tä");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            katkaiseYhteys();
            try {
                ps.close();
                results.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyä (haeLisättyVarausId)");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return palautettavaId;
    }
    
    /**
     * Hakee kaikki varaukset
     * @return ObservableList varaukset
     */
    public ObservableList<Varaus> haeKaikkiVaraukset() {
        
        ObservableList<Varaus> varaukset = FXCollections.observableArrayList();
        int varausId = 0;
        LocalDate vuokraAlku = null;
        LocalDate vuokraLoppu = null;
        int tilaId = 0;
        int asiakasId = 0;
        int palveluvarausId = 0;
        int laitevarausId = 0;
        
        try {
            yhdista();
            ps = conn.prepareStatement("SELECT * FROM Varaus;");
            
            results = ps.executeQuery();

            while(results.next()) {
                varausId = results.getInt("varausId");
                vuokraAlku = results.getDate("vuokraAlku").toLocalDate();
            //    System.out.println("Vuokran alku: "+vuokraAlku);
                vuokraLoppu = results.getDate("vuokraLoppu").toLocalDate();
                tilaId = results.getInt("tilaId");
                asiakasId = results.getInt("asiakasId");
                palveluvarausId = results.getInt("palveluvarausId");
                laitevarausId = results.getInt("laitevarausId");
                                
                varaukset.add(new Varaus(varausId, vuokraAlku, vuokraLoppu, tilaId, asiakasId, palveluvarausId, laitevarausId));
            }
        } catch (SQLException ex) {
            heitaVirhe("Virhe haettaessa varauksia");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            katkaiseYhteys();
            try {
                ps.close();
                results.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyä (haeKaikkiVaraukset)");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return varaukset;
    }
    
    /**
     * Hakee varaukset toimitilasta
     * @param t Toimitila
     * @return ObservableList varaukset
     */
    public ObservableList<Varaus> haeVarauksetToimitilasta(Toimitila t) {
    
        ObservableList<Varaus> varaukset = FXCollections.observableArrayList();
        int varausId = 0;
        LocalDate vuokraAlku = null;
        LocalDate vuokraLoppu = null;
        int tilaId = 0;
        int asiakasId = 0;
        int palveluvarausId = 0;
        int laitevarausId = 0;
        
        try {
            yhdista();
            ps = conn.prepareStatement("SELECT * FROM Varaus WHERE tilaId = ?");
            ps.setInt(1, t.getTilaId());
            
            results = ps.executeQuery();
            
            while(results.next()) {
                varausId = results.getInt("varausId");
                vuokraAlku = results.getDate("vuokraAlku").toLocalDate();
                vuokraLoppu = results.getDate("vuokraLoppu").toLocalDate();
                tilaId = results.getInt("tilaId");
                asiakasId = results.getInt("asiakasId");
                palveluvarausId = results.getInt("palveluvarausId");
                laitevarausId = results.getInt("laitevarausId");
                
                varaukset.add(new Varaus(varausId, vuokraAlku, vuokraLoppu, tilaId, asiakasId, palveluvarausId, laitevarausId));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            katkaiseYhteys();
            try {
                ps.close();
                results.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyä (haeVarauksetToimitilasta)");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return varaukset;
    }

    /**
     * Hakee varaukset halutulta aikaväliltä
     * @param alku Alkupäivä
     * @param loppu Loppupäivä
     * @return ObservableList varaukset
     */
    public ObservableList<Varaus> haeVarauksetAikavalilta(LocalDate alku, LocalDate loppu) {
        
        ObservableList<Varaus> varaukset = FXCollections.observableArrayList();
        int varausId = 0;
        LocalDate vuokraAlku = null;
        LocalDate vuokraLoppu = null;
        int tilaId = 0;
        int asiakasId = 0;
        int palveluvarausId = 0;
        int laitevarausId = 0;
        
        try {
            yhdista();
            ps = conn.prepareStatement("SELECT * FROM Varaus WHERE varausAlku >= ? AND varausLoppu <= ?");
            ps.setDate(1, Date.valueOf(alku));
            ps.setDate(2, Date.valueOf(loppu));
            
            results = ps.executeQuery();
            
            while(results.next()) {
                varausId = results.getInt("varausId");
                vuokraAlku = results.getDate("vuokraAlku").toLocalDate();
                vuokraLoppu = results.getDate("vuokraLoppu").toLocalDate();
                tilaId = results.getInt("tilaId");
                asiakasId = results.getInt("asiakasId");
                palveluvarausId = results.getInt("palveluvarausId");
                laitevarausId = results.getInt("laitevarausId");
                
                varaukset.add(new Varaus(varausId, vuokraAlku, vuokraLoppu, tilaId, asiakasId, palveluvarausId, laitevarausId));
            }
        } catch (SQLException ex) {
            heitaVirhe("Virhe haettaessa varauksia aikaväliltä");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            katkaiseYhteys();
            try {
                ps.close();
                results.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyitä (haeVarauksetAikavalilta)");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        return varaukset;
    }

    public ObservableList<String> haeVarauksenPalvelut(int varausId) {
        
        ObservableList<String> palvelut = FXCollections.observableArrayList();
        
        try {
            yhdista();
            ps = conn.prepareStatement("SELECT p.kuvaus FROM Palvelut p, Varauspalvelut vp WHERE p.palveluId = vp.palveluId AND vp.varausId = ?");
                    
            ps.setInt(1, varausId);
            
            results = ps.executeQuery();
            
            while(results.next()) {
                palvelut.add(results.getString("kuvaus"));
            }
        } catch (SQLException ex) {
            heitaVirhe("Virhe haettaessa varauksen palveluita");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            katkaiseYhteys();
            try {
                ps.close();
                results.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return palvelut;
    }
    
    public ObservableList<String> haeVarauksenLaitteet(int varausId) {
        
        ObservableList<String> laitteet = FXCollections.observableArrayList();
        
        try {
            yhdista();
            ps = conn.prepareStatement("SELECT l.kuvaus FROM Laitteet l, Varauslaitteet vl WHERE l.laiteId = vl.laiteId AND vl.varausId = ?");
                 
            ps.setInt(1, varausId);
            
            results = ps.executeQuery();
            
            while(results.next()) {
                laitteet.add(results.getString("kuvaus"));
            }
        } catch (SQLException ex) {
            heitaVirhe("Virhe haettaessa varauksen laitteita");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            katkaiseYhteys();
            try {
                ps.close();
                results.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return laitteet;
    }
    
    // KESKEN
    public void lisaaVarauksenPalvelut(Palvelu p, int varausId) {
    
        try {
            
            //ps = conn.prepareStatement("INSERT INTO Varauspalvelut (palveluvarausId, palveluId) VALUES (?, ?)");
            ps = conn.prepareStatement("INSERT INTO Varauspalvelut (palveluId, varausId) VALUES (?, (SELECT varausId FROM Varaus WHERE varausId = ?))");
            
            ps.setInt(1, p.getPalveluId());
            ps.setInt(2, varausId);

            ps.execute();
        } catch (SQLException ex) {
            heitaVirhe("Virhe lisätessä varauksen palveluita");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            
            try {
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    // KESKEN
    public void lisaaVarauksenLaitteet(Laite l, int varausId) {
        
        try {
          
            //ps = conn.prepareStatement("INSERT INTO Varauslaitteet (laitevarausId, laiteId) VALUES (?, ?)");
            ps = conn.prepareStatement("INSERT INTO Varauslaitteet (laiteId, varausId) VALUES (?, (SELECT varausId FROM Varaus WHERE varausId = ?))");
            
            ps.setInt(1, l.getLaiteId());
            ps.setInt(2, varausId);

            ps.execute();
        } catch (SQLException ex) {
            heitaVirhe("Virhe lisätessä varauksen palveluita");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            
            try {
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Lisää palvelun kuuluvaksi tiettyyn tilaan
     * @param p Lisättävä palvelu
     * @param t Tila johon palvelu liitetään
     */
    public void lisaaTilanPalvelu(Palvelu p, Toimitila t) {
        
        try {
            yhdista();
            
            // Sisäkkäisessä haussa haetaan palveluId tietokannasta, koska sitä ei ole vielä määritetty parametrina syötettävälle oliolle. Id on tietokannassa luotu auto incrementilla
            ps = conn.prepareStatement("INSERT INTO Tilanpalvelut (tila, palveluId, tilaId) VALUES (?, (SELECT palveluId FROM Palvelut WHERE kuvaus = ? AND hintaPvm = ? LIMIT 1), ?)");
            ps.setInt(1, 1);
            ps.setString(2, p.getKuvaus());
            ps.setInt(3, p.getHintaPvm());
            ps.setInt(4, t.getTilaId());
            
            ps.execute();
            
        } catch (SQLException ex) {
            heitaVirhe("Virhe lisätessä tilan palvelua");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            katkaiseYhteys();
            try {
                ps.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyä (lisaaTilanPalvelu)");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Lisää laitteen kuuluvaksi tiettyyn tilaan
     * @param l Lisättävä laite
     * @param t Tila johon laite liitetään
     */
    public void lisaaTilanLaite(Laite l, Toimitila t) {
        
        try {
            yhdista();
            // Sisäkkäisessä haussa haetaan laiteId tietokannasta, koska sitä ei ole vielä määritetty parametrina syötettävälle oliolle. Id on tietokannassa luotu auto incrementilla
            ps = conn.prepareStatement("INSERT INTO Tilanlaitteet (tila, laiteId, tilaId) VALUES (?, (SELECT laiteId FROM Laitteet WHERE kuvaus = ? AND hintaPvm = ? LIMIT 1), ?)");
            ps.setInt(1, 1);
            ps.setString(2, l.getKuvaus());
            ps.setInt(3, l.getHintaPvm());
            ps.setInt(4, t.getTilaId());
            
            ps.execute();
            
        } catch (SQLException ex) {
            heitaVirhe("Virhe lisätessä tilan laitetta");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            katkaiseYhteys();
            try {
                ps.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyä (lisaaTilanLaite)");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Palauttaa viimeisimmän Varauspalvelut -taulun palveluvarausId:n
     * @return palveluvarausId
     */
    public int haeVarauksenPalvelutId() {
       
        int palveluvarausId = 0;
        
        try {
            yhdista();
            stmt = conn.createStatement();
            results = stmt.executeQuery("SELECT MAX(palveluvarausId) AS 'palveluvarausId' FROM Varauspalvelut LIMIT 1;");
            results.next();
            palveluvarausId = results.getInt("palveluvarausId");
            
        } catch (SQLException ex) {
            heitaVirhe("Virhe hakiessa VarauksenPalvelutId:tä");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            katkaiseYhteys();
            try {
                stmt.close();
                results.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyitä (haeVarauksenPalvelutId)");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return palveluvarausId;
    }
    
    /**
     * Palauttaa viimeisimmän Varauslaitteet -taulun laitevarausId:n
     * @return laitevarausId
     */
    public int haeVarauksenLaitteetId() {
        
        int laitevarausId = 0;
        
        try {
            yhdista();
            stmt = conn.createStatement();
            results = stmt.executeQuery("SELECT MAX(laitevarausId) AS 'laitevarausId' FROM Varauslaitteet LIMIT 1;");
            results.next();
            laitevarausId = results.getInt("laitevarausId");
            
        } catch (SQLException ex) {
            heitaVirhe("Virhe hakiessa VarauksenLaitteetId:tä");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            katkaiseYhteys();
            try {
                stmt.close();
                results.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyitä (haeVarauksenLaitteetId)");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return laitevarausId;
    }
    /**
     * Hakee halutun toimitilan palvelut
     * @param t Toimipiste
     */
    public ObservableList<Palvelu> haePalvelutToimitilasta(Toimitila t) {
        
        ObservableList<Palvelu> palvelut = FXCollections.observableArrayList();
        try {
            yhdista();
            ps = conn.prepareStatement("SELECT p.palveluId, p.hintaPvm, p.kuvaus FROM Palvelut p, Tilanpalvelut tp WHERE tp.tilaId = ? AND p.palveluId = tp.palveluId;");
            ps.setInt(1, t.getTilaId());
            results = ps.executeQuery();
            
            while(results.next()) {
                palvelut.add(new Palvelu(results.getInt("palveluId"), results.getInt("hintaPvm"), results.getString("kuvaus")));
            }
        } catch (SQLException ex) {
            heitaVirhe("Virhe haettaessa palveluita");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            katkaiseYhteys();
            try {
                ps.close();
                results.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyitä (haePalvelutToimitilasta)");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return palvelut;
    }
    
    /**
     * Hakee kaikki palvelut
     * @return Kaikki palvelut
     */
    public ObservableList<Palvelu> haeKaikkiPalvelut() {
        
        ObservableList<Palvelu> palvelut = FXCollections.observableArrayList();
        try {
            yhdista();
            stmt = conn.createStatement();
            results = stmt.executeQuery("SELECT * FROM Palvelut");
            
            while(results.next()) {
                palvelut.add(new Palvelu(results.getInt("palveluId"), results.getInt("hintaPvm"), results.getString("kuvaus")));
            }
        } catch (SQLException ex) {
            heitaVirhe("Virhe haettaessa palveluita");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            katkaiseYhteys();
            try {
                stmt.close();
                results.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyitä (haeKaikkiPalvelut)");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return palvelut;
    }
    /**
     * Hakee halutun toimitilan laitteet
     * @param t Toimitila
     */
    public ObservableList<Laite> haeLaitteetToimitilasta(Toimitila t) {
        
        ObservableList<Laite> laitteet = FXCollections.observableArrayList();
        try {
            yhdista();
            ps = conn.prepareStatement("SELECT l.laiteId, l.kuvaus, l.hintaPvm FROM Laitteet l, Tilanlaitteet tl WHERE tl.tilaId = ? AND l.laiteId = tl.laiteId;");
            ps.setInt(1, t.getTilaId());
            results = ps.executeQuery();
            
            while(results.next()) {
                laitteet.add(new Laite(results.getInt("laiteId"), results.getString("kuvaus"),results.getInt("hintaPvm")));
            }
        } catch (SQLException ex) {
            heitaVirhe("Virhe haettaessa laitteita");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            katkaiseYhteys();
            try {
                ps.close();
                results.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyitä (haeLaitteetToimitilasta)");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return laitteet;
    }
    
    /**
     * Hakee kaikki laitteet
     * @return Kaikki laitteet
     */
    public ObservableList<Laite> haeKaikkiLaitteet() {
        
        ObservableList<Laite> laitteet = FXCollections.observableArrayList();
        try {
            yhdista();
            stmt = conn.createStatement();
            
            results = stmt.executeQuery("SELECT * FROM Laitteet;");
            
            while(results.next()) {
                laitteet.add(new Laite(results.getInt("laiteId"), results.getString("kuvaus"),results.getInt("hintaPvm")));
            }
        } catch (SQLException ex) {
            heitaVirhe("Virhe haettaessa laitteita");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            katkaiseYhteys();
            try {
                stmt.close();
                results.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyitä (haeKaikkiLaitteet)");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return laitteet;
    }
    
    /**
     * Lisää palvelun tietokantaan
     * @param p Lisättävä palvelu
     */
    public void lisaaPalvelu(Palvelu p) throws SQLException {
        
        try {
            yhdista();
            ps = conn.prepareStatement("INSERT INTO Palvelut (hintaPvm, kuvaus) VALUES (?, ?);");
            ps.setInt(1, p.getHintaPvm());
            ps.setString(2, p.getKuvaus());
            
            System.out.println("Lisätään palvelu");
            ps.execute();
            System.out.println("Palvelu lisätty");
            
        } catch (SQLException ex) {
            //heitaVirhe("Virhe lisätessä palvelua");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } finally {
            katkaiseYhteys();
            try {
                ps.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyä (lisaaPalvelu)");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Lisää laitteen tietokantaan
     * @param l Lisättävä laite
     */
    public void lisaaLaite(Laite l) throws SQLException {
        
        try {
            yhdista();
            
            ps = conn.prepareStatement("INSERT INTO Laitteet (kuvaus, hintaPvm) VALUES (?, ?)");
            ps.setString(1, l.getKuvaus());
            ps.setInt(2, l.getHintaPvm());
            
            System.out.println("lisätään laite");
            ps.execute();
            System.out.println("Laite lisätty");
            
        } catch (SQLException ex) {
            heitaVirhe("Virhe lisätessä laitetta");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } finally {
            katkaiseYhteys();
            try {
                ps.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyä (lisaaLaite)");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Hakee laskut
     */
    public ObservableList<Lasku> haeKaikkiLaskut() {
        
        ObservableList<Lasku> laskut = FXCollections.observableArrayList();
        try {
            yhdista();
            ps = conn.prepareStatement("SELECT * FROM Lasku;");
            results = ps.executeQuery();
            
            while(results.next()) {
                laskut.add(new Lasku(results.getInt("laiteId"), results.getString("laskuntyyppi"),results.getInt("hinta"),results.getInt("varausId")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            katkaiseYhteys();
            try {
                ps.close();
                results.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyitä (haeKaikkiLaskut)");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return laskut;
    }    
    
    /**
     * Hakee ASIAKKAAN laskut //KESKEN
     * @param a String Haettavan yrityksen nimi
     * @return laskut ObservableList
     */
    public ObservableList<Lasku> haeAsiakkaanLaskut(Asiakas a) {
        
        ObservableList<Lasku> laskut = FXCollections.observableArrayList();
        try {
            yhdista();
            
            
            ps = conn.prepareStatement("SELECT l.laskuNro, l.varausId , a.yrityksenNimi, t.tilanNimi, l.laskuntyyppi FROM Lasku l, Asiakas a, Tilat t \n" +
                                " WHERE a.yrityksenNimi= ? LIMIT 1;;");
            ps.setString(1, a.getYrityksenNimi());
            results = ps.executeQuery();
            
            while(results.next()) {
                laskut.add(new Lasku(results.getInt("laiteId"), results.getString("laskuntyyppi"),results.getInt("hinta"),results.getInt("varausId")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            katkaiseYhteys();
            try {
                ps.close();
                results.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyitä (haeKaikkiLaskut)");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return laskut;
    }
    
    
    
    /**
     * Heittää virheen näytölle
     * @param viesti Virheilmoitus
     */
    private void heitaVirhe(String viesti) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Tietokanta");
        a.setHeaderText(viesti);
        a.showAndWait();
    }
   
    
    
     /**
     * TauluOlio
     * @return ObservableList laskun tiedot.
     */
    
    
    public ObservableList<Taulut> haeLaskut(){
        
        ObservableList<Taulut> tiedot = FXCollections.observableArrayList();
        int laskunNro = 0;
        String laskunTyyppi = "";
        String yritysNimi = "";
        String toimitila = "";
        int varausId = 0;
            
        try {
            yhdista();
            stmt = conn.createStatement();
 
            // Tiedot laskuista
            results = stmt.executeQuery("SELECT * FROM Lasku;");
            while(results.next()) {
                laskunNro = results.getInt("laskunNro");
                laskunTyyppi = results.getString("laskunTyyppi");
                
                 // tiedot.add(new Taulut(laskunNro, laskunTyyppi));
            }
            
            // Tiedot Asiakkaasta
            results = null;
            results = stmt.executeQuery("SELECT * FROM Asiakas;");
            while(results.next()) {
                yritysNimi = results.getString("yritysNimi"); 
                laskunTyyppi = results.getString("laskunTyyppi");
                        
                // laskut.add(new Taulut(laskunNro, laskunTyyppi));
            }
        
        } catch (SQLException ex) {
            heitaVirhe("Virhe hakiessa kaikkia varauksia tietokannasta");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            katkaiseYhteys();
        }
        
        return tiedot;
        
    }

   /**
     * Hakee kaikki varaukset
     * @return ObservableList varaukset
     */
    public ObservableList<Varaus> haeAsiakkaanVaraukset(int asId) {
        
        ObservableList<Varaus> varaukset = FXCollections.observableArrayList();
        int varausId = 0;
        LocalDate vuokraAlku = null;
        LocalDate vuokraLoppu = null;
        int tilaId = 0;
        int asiakasId = 0;
        int palveluvarausId = 0;
        int laitevarausId = 0;
        
        try {
            yhdista();
            ps = conn.prepareStatement("SELECT * FROM Varaus WHERE asiakasId =?;");
            
            results = ps.executeQuery();

            while(results.next()) {
                varausId = results.getInt("varausId");
                vuokraAlku = results.getDate("vuokraAlku").toLocalDate();
            //    System.out.println("Vuokran alku: "+vuokraAlku);
                vuokraLoppu = results.getDate("vuokraLoppu").toLocalDate();
                tilaId = results.getInt("tilaId");
                asiakasId = results.getInt("asiakasId");
                palveluvarausId = results.getInt("palveluvarausId");
                laitevarausId = results.getInt("laitevarausId");
                                
                varaukset.add(new Varaus(varausId, vuokraAlku, vuokraLoppu, tilaId, asiakasId, palveluvarausId, laitevarausId));
            }
        } catch (SQLException ex) {
            heitaVirhe("Virhe haettaessa varauksia");
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            katkaiseYhteys();
            try {
                ps.close();
                results.close();
            } catch (SQLException ex) {
                heitaVirhe("Virhe suljettaessa kyselyä (haeKaikkiVaraukset)");
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return varaukset;
    }
    
    
    
    
    
    
    
}
