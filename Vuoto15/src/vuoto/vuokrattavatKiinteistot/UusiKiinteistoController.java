/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.vuokrattavatKiinteistot;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vuoto.aloitus.VuotoMainController;
import vuoto.luokkafilet.Toimipiste;
import vuoto.luokkafilet.Toimitila;
import vuoto.tietokanta.DBAccess;

/**
 * FXML Controller class
 *
 * @author marko
 */
public class UusiKiinteistoController implements Initializable {

    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/vuokrattavatKiinteistot/UusiKiinteisto.fxml";
    @FXML
    private Label lblToimipisteValinta;
    @FXML
    private TextField txtToimipiste;
    @FXML
    private Button btnTakaisin;
    @FXML
    private Button btnLisaaKiinteisto;
    @FXML
    private TextArea txtKuvaus;
    @FXML
    private TextField txtKiinteistonNimi;
    @FXML
    private TextField txtLahiosoite;
    @FXML
    private TextField txtPostinumero;
    @FXML
    private TextField txtPostitoimipaikka;
    @FXML
    private TextField txtHuoneistonKoko;
    private Toimitila toimitila;
    private DBAccess tietokanta = new DBAccess();
    @FXML
    private TextField txtHinta;
    private Toimipiste toimipiste;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        maaritaToimipiste();
    }    
    
    /**
     * Määrittää toimipisteen jonka tietoja haetaan
     * Määrittää Toimipiste -luokan olion ja asettaa toimipisteen nimen txtToimipiste kenttään
     */
    private void maaritaToimipiste() {
        
       
        // Aloitusvalikossa valittu toimipisteen nimi
        String valikostaValittu = VuotoMainController.valittuToimipiste;
        
        // Estetään ettei mene tietokantaan hakua WHERE toimipisteNimi = 'Kaikki toimipisteet'
        if(valikostaValittu.equals("Kaikki toimipisteet")) {
            // Käsitellään kaikkia toimipisteitä
            
        } else {        
            toimipiste = tietokanta.haeToimipisteNimella(valikostaValittu); // tietokannan haeToimipisteNimella(nimi) palauttaa toimipiste -olion. Nimenä se aloitusikkunassa valittu toimipisteen nimi.
        }
        
        txtToimipiste.setText(valikostaValittu); // Asetetaan näkymän toimipiste -tekstikenttään aloitusnäkymässä valittu valinta
        txtToimipiste.setFocusTraversable(false); // Tää ohjelma hölmösti "kohdistaa" näkymän tohon tekstikenttään ja mustamaalaa sen tekstin kun siihen lisätään tässä sisältöä. Tällä saa sen pois.
        
    }
    

    /**
     * Lisää syötetyn kiinteistön tietokantaan
     * @param event Lisää kiinteistö -napin painallus
     */
    @FXML
    private void btnLisaaKiinteistoPainettu(ActionEvent event) {

        // Tekstikenttien tiedoista tehdään toimitila-olio ja lisätään se tietokantaan
        String nimi = txtKiinteistonNimi.getText();
        String lahiosoite = txtLahiosoite.getText();
        String postinumero = txtPostinumero.getText();
        String postitoimipaikka = txtPostitoimipaikka.getText();
        int huonekoko = Integer.valueOf(txtHuoneistonKoko.getText());
        int hinta = Integer.valueOf(txtHinta.getText());
        String kuvaus = txtKuvaus.getText();
        int toimipisteId = toimipiste.getToimipisteID();
        
        boolean toimitilaLisatty = true;
        int tila = 1;
        
        toimitila = new Toimitila(lahiosoite, postinumero, postitoimipaikka, huonekoko, hinta, tila, kuvaus, nimi, toimipisteId);
        
        try {
            tietokanta.lisaaToimitila(toimitila);
            
        } catch (SQLException ex) {
            toimitilaLisatty = false;
            Logger.getLogger(UusiKiinteistoController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            
            // Jos toimipiste meni tietokantaan ilman ongelmia niin heitetään siitä ilmoitus
            if (toimitilaLisatty) {
                Alert ok = new Alert(Alert.AlertType.INFORMATION);
                ok.setTitle("Kiinteistön lisääminen");
                ok.setHeaderText("Kiinteistö " + toimitila + " lisätty!");
                ok.showAndWait();
                // Paluu - VuokrattavatKiinteistot.
                VuokrattavatKiinteistotController controller = (VuokrattavatKiinteistotController) siirryNakymaan(VuokrattavatKiinteistotController.fxmlString, "Vuokrattavat Kiinteistot", event);
                //controller.asetaToimipiste(toimipiste);
            }
        }
        
        
        
    }

    
    @FXML
    private void btnTakaisinKiinteistotSivulle(ActionEvent event) {
    // Opens panel - VuokrattavatKiinteistot.
    VuokrattavatKiinteistotController controller = (VuokrattavatKiinteistotController)siirryNakymaan(VuokrattavatKiinteistotController.fxmlString, "Vuokrattavat Kiinteistot", event);
    //controller.asetaToimipiste(toimipiste);
    }
   
    /**
     * Heittää virheilmoituksen näytölle
     * @param virhe Virheilmoitus
     */
    private void heitaVirheNaytolle(String virhe) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Vuokrattavat kiinteistöt");
        a.setHeaderText(virhe);
        a.showAndWait();
    }
    
    
     /**
     * Muuttaa ikkunan näkymäksi aloitusikkunasta valitun näkymän
     * @param fxml fxml-tiedoston nimi
     * @param title Ikkunan otsikko
     * @param event Lähde mistä valinta tuli
     * @return Valitun näkymän Controller -luokan olio
     */
    private Object siirryNakymaan(String fxml, String title, ActionEvent event) {
        
        Object controller = null; // Palautettava controller olio
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
            Parent vuoto = (Parent) fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            controller = fxmlLoader.getController();
            stage.setTitle(title);
            stage.setScene(new Scene(vuoto));
            stage.show();
        } catch (IOException ex) {
            heitaVirheNaytolle("Virhe luotaessa näkymää " + title);
            Logger.getLogger(UusiKiinteistoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return controller;
   
    } 

    
    
}
