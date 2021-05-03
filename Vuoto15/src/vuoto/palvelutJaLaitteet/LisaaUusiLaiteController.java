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
public class LisaaUusiLaiteController implements Initializable {

    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/palvelutJaLaitteet/LisaaUusiLaite.fxml";
    @FXML
    private TextField txtToimipiste;
    @FXML
    private Label lblToimipisteValinta;
    @FXML
    private Button btnPoistaLasku;
    @FXML
    private TextField txtLaiteID;
    @FXML
    private TextField txtKuvaus;
    @FXML
    private TextField txtHinta;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnTakaisinPainettu(ActionEvent event) {
        
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void btnLisaaPainettu(ActionEvent event) {
    }
    
}
