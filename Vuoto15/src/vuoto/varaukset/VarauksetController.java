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
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import vuoto.aloitus.VuotoMainController;
import static vuoto.aloitus.VuotoMainController.valittuToimipiste;
import vuoto.asiakkuudet.AsiakkuudetController;
import vuoto.laskutus.LaskutusController;
import vuoto.tietokanta.DBAccess;

/**
 * FXML Controller class
 *
 * @author marko
 */
public class VarauksetController implements Initializable {

    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/varaukset/Varaukset.fxml";
    @FXML
    private Label lblToimipisteValinta;
    @FXML
    private TextField txtToimipiste;
    @FXML
    private DatePicker dpAlkupaiva;
    @FXML
    private DatePicker dpLoppupaiva;
    @FXML
    private VBox palvelutIkkuna;
    @FXML
    private VBox laitteetIkkuna;
    private DBAccess tietokanta = new DBAccess();
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Aktiivinen toimipiste (eli yksi keskuksista)
        txtToimipiste.setFocusTraversable(false);
        txtToimipiste.setText(valittuToimipiste);
        
    }    
    
    public void paivitaPalvelut() {
        
        
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

     @FXML
    private void btnEtusivullePainettu(ActionEvent event) {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(VuotoMainController.fxmlString));
        Parent root = null;
        
        try {
            root = loader.load();
        } catch (IOException ex) {
            heitaVirheNaytolle("Virhe palatessa takaisin aloitusnäkymään");
            Logger.getLogger(LaskutusController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        
    }

    @FXML
    private void btnUusiVarausPainettu(ActionEvent event) {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(UusiVarausController.fxmlString));
        Parent root = null;
        
        try {
            root = loader.load();
        } catch (IOException ex) {
            heitaVirheNaytolle("Virhe luotaessa näkymää UusiVaraus.fxml");
            Logger.getLogger(AsiakkuudetController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    private void btnPoistaVarausPainettu(ActionEvent event) {
        
        boolean okPainettu = heitaVahvistusNaytolle("Poistetaanko palvelu " + "*tähän varaus*" + "?", "Palvelun poistaminen");
        
        // if(okPainettu) niin poista tiedot.........
    }

    @FXML
    private void btnMuokkaaVaraustaPainettu(ActionEvent event) {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(MuokkaaVaraustaController.fxmlString));
        Parent root = null;
        
        try {
            root = loader.load();
        } catch (IOException ex) {
            heitaVirheNaytolle("Virhe luotaessa näkymää MuokkaaVarausta.fxml");
            Logger.getLogger(AsiakkuudetController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
    
    /**
     * Heittää Confirmation -ikkunan näytölle
     * @param viesti Ikkunan header-teksti
     * @param title Ikkunan otsikko
     * @return Jos painetaan OK = true, muuten false
     */
    private boolean heitaVahvistusNaytolle(String viesti, String title) {
        
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle(title);
        a.setHeaderText(viesti);
        a = muotoileIlmoitus(a);
        a.showAndWait();
        if(a.getResult() == ButtonType.OK) {
            return true;
        }
        return false;
    }
    
    /**
     * Muotoilee ilmoituksen ikkunan
     * @param a Alkuperäinen ilmoitus
     * @return Palautetaan muotoiltu ilmoitus
     */
    public Alert muotoileIlmoitus(Alert a) {
        
        String alert_css = getClass().getResource("/vuoto/stylesheets/sauli_alert.css").toExternalForm();
        DialogPane dialog = a.getDialogPane();
        dialog.getStylesheets().add(alert_css);
        dialog.getStyleClass().add("alert");
        ((Button)a.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Takaisin");
        
        return a;
    }
   
    
}
