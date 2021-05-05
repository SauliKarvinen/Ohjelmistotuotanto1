/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.palvelutJaLaitteet;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vuoto.luokkafilet.Palvelu;
import vuoto.luokkafilet.Toimitila;
import vuoto.tietokanta.DBAccess;

/**
 * FXML Controller class
 *
 * @author sauli
 */
public class LisaaUusiPalveluController implements Initializable {

    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/palvelutJaLaitteet/LisaaUusiPalvelu.fxml";
    @FXML
    private TextField txtToimipiste;
    @FXML
    private Label lblToimipisteValinta;
    @FXML
    private Button btnPoistaLasku;
    @FXML
    private TextField txtKuvaus;
    @FXML
    private TextField txtHinta;
    private Palvelu palvelu;
    private DBAccess tietokanta = new DBAccess();
    @FXML
    private TextField txtToimitila;
    private Toimitila toimitila;

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
        
        boolean palveluLisatty = true;
        String kuvaus = txtKuvaus.getText();
        int hintaPvm = Integer.valueOf(txtHinta.getText());     
        palvelu = new Palvelu(hintaPvm, kuvaus);
        
        try {
            tietokanta.lisaaPalvelu(palvelu);
            //tietokanta.lisaaTilanPalvelu(palvelu, toimitila);
        } catch (SQLException ex) {
            palveluLisatty = false;
            heitaVirheNaytolle("Virhe lisätessä palvelua tietokantaan");
            Logger.getLogger(LisaaUusiPalveluController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(palveluLisatty) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Palvelu lisääminen");
                a.setHeaderText("Palvelu lisätty!");
                a.showAndWait();
                
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.close();
            }
        }
    }

    public void asetaToimitila(Toimitila t) {
        
        if(t != null) {
            toimitila = t;
            txtToimitila.setText(t.getTilanNimi());
        }
    }
    
    /**
     * Heittää virheilmoituksen näytölle
     * @param virhe Virheilmoitus
     */
    private void heitaVirheNaytolle(String virhe) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Palvelut ja Laitteet");
        a.setHeaderText(virhe);
        //a = muotoileIlmoitus(a);
        a.showAndWait();
    }
    
}
