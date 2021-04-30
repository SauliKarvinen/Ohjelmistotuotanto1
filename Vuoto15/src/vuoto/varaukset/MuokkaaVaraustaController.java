/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.varaukset;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import vuoto.asiakkuudet.AsiakkuudetController;

/**
 * FXML Controller class
 *
 * @author sauli
 */
public class MuokkaaVaraustaController implements Initializable {

    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/varaukset/MuokkaaVarausta.fxml";
    @FXML
    private Label lblToimipisteValinta;
    @FXML
    private TextField txtToimipiste;
    @FXML
    private ComboBox<?> cbToimitilat;
    @FXML
    private ComboBox<?> cbLaitteet;
    @FXML
    private DatePicker dpLopetuspvm;
    @FXML
    private DatePicker dpAloituspvm;
    @FXML
    private VBox palvelutIkkuna;
    @FXML
    private VBox laitteetIkkuna;
    @FXML
    private Button btnLisaaAsiakas1;
    @FXML
    private TextField txtLoppusumma;
    @FXML
    private Button btnTakaisin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void LisaaToimipiste(ActionEvent event) {
    }

    @FXML
    private void btnTakaisinPainettu(ActionEvent event) {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(VarauksetController.fxmlString));
        Parent root = null;
        
        try {
            root = loader.load();
        } catch (IOException ex) {
            heitaVirheNaytolle("Virhe luotaessa näkymää Varaukset.fxml");
            Logger.getLogger(AsiakkuudetController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
    
    /**
     * Heittää virheilmoituksen näytölle
     * @param virhe Virheilmoitus
     */
    private void heitaVirheNaytolle(String virhe) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Varaukset");
        a.setHeaderText(virhe);
        a.showAndWait();
    }
}
