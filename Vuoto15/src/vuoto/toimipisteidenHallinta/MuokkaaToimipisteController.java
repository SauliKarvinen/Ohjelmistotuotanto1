/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.toimipisteidenHallinta;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vuoto.luokkafilet.Toimipiste;
import vuoto.tietokanta.DBAccess;

/**
 * FXML Controller class
 *
 * @author marko
 */
public class MuokkaaToimipisteController implements Initializable {

    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/toimipisteidenHallinta/MuokkaaToimipiste.fxml";
    @FXML
    private Button btnTakaisin;
    @FXML
    private Button btnMuokkaaToimipiste;
    @FXML
    private TextArea txtKuvaus;
    @FXML
    private TextField txtToimipistenimi;
    @FXML
    private TextField txtLahiosoite;
    @FXML
    private TextField txtPostinumero;
    private Toimipiste toimipiste;
    private DBAccess tietokanta = new DBAccess();

    
    /**
     * Suoritetaan SQL-komento, jolla muutetaan toimipisteen tietoja
     * 
     * Palataan Toimipisteet paneelille
     * 
     * @param event 
     */
     @FXML
    private void LisaaMuokkaaToimipistePainettu(ActionEvent event) {
        
        Toimipiste muokattuToimipiste = null;
        
        int toimipisteId = toimipiste.getToimipisteID();
        String lahiosoite = txtLahiosoite.getText();
        String postinumero = txtPostinumero.getText();
        String toimipisteNimi = txtToimipistenimi.getText();
        String kuvaus = txtKuvaus.getText();
        
        muokattuToimipiste = new Toimipiste(toimipisteId, lahiosoite, postinumero, toimipisteNimi, kuvaus);
        
        if(muokattuToimipiste.equals(toimipiste)) {
            heitaVirheNaytolle("Muuta tietoja päivittääksesi toimipiste");
        } else {
            
            try {
                tietokanta.paivitaToimipiste(muokattuToimipiste);
                
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Toimipisteen muokkaus");
                a.setHeaderText("Toimipiste päivitetty!");
                a.showAndWait();
                ToimipisteetController controller = (ToimipisteetController) siirryNakymaan("Toimipisteet.fxml", "Toimipisteiden hallinta", event);
                
            } catch (SQLException ex) {
                Logger.getLogger(MuokkaaToimipisteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * Palataan Toimipisteet paneelille
     * 
     * @param event 
     */
    @FXML
    private void TakaisinToimipisteisiin(ActionEvent event) {
                // Opens panel - Toimipisteiden hallinta.
        ToimipisteetController controller = (ToimipisteetController) siirryNakymaan("Toimipisteet.fxml", "Toimipisteiden hallinta", event);
        //controller.asetaToimipiste(toimipiste);

    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
            Logger.getLogger(MuokkaaToimipisteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return controller;
   
    } 
    
    /**
     * Asettaa muokattavan toimipisteen tiedot kenttiin
     * @param t Muokattava toimipiste
     */
    public void asetaToimipiste(Toimipiste t) {
        
        if(t != null) {
            toimipiste = t;
            txtToimipistenimi.setText(t.getToimipistenimi());
            txtLahiosoite.setText(t.getLahiosoite());
            txtPostinumero.setText(t.getPostinumero());
            txtKuvaus.setText(t.getKuvaus());
            
        }
    }

   
   
}
