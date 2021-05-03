/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.asiakkuudet;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sauli
 */
public class MuokkaaAsiakastaController implements Initializable {

    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/asiakkuudet/MuokkaaAsiakasta.fxml";
    @FXML
    private TextField txtAsiakasID;
    @FXML
    private TextField txtYrityksenNimi;
    @FXML
    private TextField txtEtunimi;
    @FXML
    private TextField txtSukunimi;
    @FXML
    private TextField txtLahiosoite;
    @FXML
    private TextField txtPostinumero;
    @FXML
    private TextField txtPuhelinnumero;
    @FXML
    private TextField txtSahkoposti;
    

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
    private void btnTallennaPainettu(ActionEvent event) {
    }
}
