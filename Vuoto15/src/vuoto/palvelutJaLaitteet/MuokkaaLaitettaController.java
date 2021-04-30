/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.palvelutJaLaitteet;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sauli
 */
public class MuokkaaLaitettaController implements Initializable {

    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/palvelutJaLaitteet/MuokkaaLaitetta.fxml";
    /** Valittu toimipiste*/
    @FXML
    private TextField txtToimipiste;
    
    @FXML
    private Label lblToimipisteValinta;
    /** Nappi tietojen poistamiselle*/
    @FXML
    private Button btnPoistaLasku;
    /** Laitteen ID*/
    @FXML
    private TextField txtLaiteID;
    /** Laitteen kuvaus*/
    @FXML
    private TextField txtKuvaus;
    /** Laitteen hinta*/
    @FXML
    private TextField txtHinta;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    /**
     * Sulkee tämän ikkunan
     * @param event "Takaisin" -napin painallus
     */
    @FXML
    private void btnTakaisinPainettu(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    
    @FXML
    private void btnLisaaPainettu(ActionEvent event) {
    }
    
}
