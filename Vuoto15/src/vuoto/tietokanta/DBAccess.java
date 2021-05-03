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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import vuoto.luokkafilet.Toimipiste;
import vuoto.luokkafilet.Asiakas;
import vuoto.luokkafilet.Lasku;
import vuoto.luokkafilet.Toimitila;
import vuoto.luokkafilet.Varaus;


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
     * Creates connection to Opskure-database.
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
                stmt.close();
                results.close();
            } catch (SQLException ex) {
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
        }
        
        return t;
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
            } catch (SQLException ex) {
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
            } catch (SQLException ex) {
                Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            katkaiseYhteys();
        }
        
        return asiakkaat;
        
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
                    + "VALUES (?, ?, ?, ?);");
            ps.setInt(1, v.getVarausId());
//            ps.setLocalDate(2,   v.getVuokraAlku());
//            ps.setLocalDate(3,   v.getVuokraLoppu());
            ps.setInt(4, v.getTilaId());
            ps.setInt(5, v.getAsiakasId());
            ps.setInt(6, v.getPalveluvarausId());
            ps.setInt(7, v.getLaitevarausId());

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
     * Heittää virheen näytölle
     * @param viesti Virheilmoitus
     */
    private void heitaVirhe(String viesti) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Tietokanta");
        a.setHeaderText(viesti);
        a.showAndWait();
    }
}
