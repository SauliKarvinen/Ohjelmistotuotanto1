/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.toimipisteidenHallinta;

import java.io.IOException;
import java.net.URL;
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
import javafx.stage.Stage;

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

    
    /**
     * Suoritetaan SQL-komento, jolla muutetaan toimipisteen tietoja
     * 
     * Palataan Toimipisteet paneelille
     * 
     * @param event 
     */
     @FXML
    private void LisaaMuokkaaToimipistePainettu(ActionEvent event) {
        // Opens panel - Toimipisteiden hallinta.
        ToimipisteetController controller = (ToimipisteetController) siirryNakymaan("Toimipisteet.fxml", "Toimipisteiden hallinta", event);
        //controller.asetaToimipiste(toimipiste);

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

   
   
}
