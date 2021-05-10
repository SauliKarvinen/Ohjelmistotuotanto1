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
import vuoto.luokkafilet.Toimitila;
import vuoto.tietokanta.DBAccess;

/**
 * FXML Controller class
 *
 * @author marko
 */
public class MuokkaaKiinteistoController implements Initializable {

    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/vuokrattavatKiinteistot/MuokkaaKiinteisto.fxml";
    @FXML
    private Label lblToimipisteValinta;
    @FXML
    private TextField txtToimipiste;
    @FXML
    private Button btnTakaisin;
    @FXML
    private Button btnMuokkaaKiinteisto;
    private Toimitila toimitila;
    private DBAccess tietokanta = new DBAccess();
    private VuokrattavatKiinteistotController controller;
    @FXML
    private TextArea txtKuvaus;
    @FXML
    private TextField txtTilanNimi;
    @FXML
    private TextField txtLahiosoite;
    @FXML
    private TextField txtPostinumero;
    @FXML
    private TextField txtPostitoimipaikka;
    @FXML
    private TextField txtHuoneistonKoko;
    @FXML
    private TextField txtKiinteistoId;
    @FXML
    private TextField txtHintaPvm;

    /**
     * Initializes the controller class.
     */
    
    /**
     * Palataan takaisin kiinteistöt paneeliin
     * @param event 
     */
    @FXML
    private void btnTakaisinKiinteistotSivulle(ActionEvent event) {
        // Opens panel - VuokrattavatKiinteistot.
        siirryNakymaan(VuokrattavatKiinteistotController.fxmlString, "Vuokrattavat Kiinteistot", event);
        
    }

    /**
     * Muokataan tiedot,
     * eli suoritetaan UPDATE SQL komento ja
     * palataan Kiinteistot paneeliin.
     * @param event 
     */
    @FXML
    private void btnMuokkaaKiinteistoPainettu(ActionEvent event) {
        
        int tilaId = Integer.valueOf(txtKiinteistoId.getText());
        String lahiosoite = txtLahiosoite.getText();
        String postinumero = txtPostinumero.getText();
        String postitoimipaikka = txtPostitoimipaikka.getText();
        int huonekoko = Integer.valueOf(txtHuoneistonKoko.getText());
        int hintaPvm = Integer.valueOf(txtHintaPvm.getText());
        int huoneistonTila = 1; // Tämän määrittämiseen ei ole vielä ollut metodia tietokannassa
        String kuvaus = txtKuvaus.getText();
        String tilanNimi = txtTilanNimi.getText();
        int toimipisteId = toimitila.getToimipisteId();

        
        Toimitila paivitettavaToimitila = new Toimitila(tilaId, lahiosoite, postinumero, postitoimipaikka, huonekoko, hintaPvm, huoneistonTila, kuvaus, tilanNimi, toimipisteId);

        // Jos tietoja ei ole muutettu, ilmoittaa ohjelma siitä
        if(paivitettavaToimitila.equals(toimitila)) {
            heitaVirheNaytolle("Muuta tietoja päivittääksesi kiinteistö");
        } else {
            
            try {
                tietokanta.paivitaToimitila(paivitettavaToimitila);
                
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Kiinteistön päivittäminen");
                a.setHeaderText("Kiinteistö päivitetty!");
                a.showAndWait();
                // Opens panel - VuokrattavatKiinteistot.
                siirryNakymaan(VuokrattavatKiinteistotController.fxmlString, "Vuokrattavat Kiinteistot", event);
            } catch (SQLException ex) {
                Logger.getLogger(MuokkaaKiinteistoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        txtToimipiste.setText(VuotoMainController.valittuToimipiste);
    }    
    
        /**
     * Heittää virheilmoituksen näytölle
     * @param virhe Virheilmoitus
     */
    private void heitaVirheNaytolle(String virhe) {
        
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("VuotoMain");
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
            Logger.getLogger(MuokkaaKiinteistoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return controller;
   
    } 
    
    public void asetaToimitila(Toimitila t) {
        
        if(t != null) {
            toimitila = t;
            txtKiinteistoId.setText(String.valueOf(t.getTilaId()));
            txtTilanNimi.setText(t.getTilanNimi());
            txtLahiosoite.setText(t.getLahiosoite());
            txtPostinumero.setText(String.valueOf(t.getPostinumero()));
            txtPostitoimipaikka.setText(t.getPostitoimipaikka());
            txtHuoneistonKoko.setText(String.valueOf(t.getHuonekoko()));
            txtHintaPvm.setText(String.valueOf(t.getHintaPvm()));
            txtKuvaus.setText(t.getKuvaus());
        }
    }
    
    public void asetaController(VuokrattavatKiinteistotController c) {
        
        if(c != null) {
            controller = c;
        }
    }
}
