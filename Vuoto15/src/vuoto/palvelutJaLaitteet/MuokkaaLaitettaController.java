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
import vuoto.luokkafilet.Laite;
import vuoto.luokkafilet.Toimitila;
import vuoto.tietokanta.DBAccess;

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
    private Laite laite;
    private Toimitila toimitila;
    private PalvelutJaLaitteetController controller;
    private DBAccess tietokanta = new DBAccess();

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
        
        Laite paivitettavaLaite = null;
        
        int laiteId = Integer.valueOf(txtLaiteID.getText());
        String kuvaus = txtKuvaus.getText();
        int hintaPvm = Integer.valueOf(txtHinta.getText());
        
        
        paivitettavaLaite = new Laite(laiteId, kuvaus, hintaPvm);
        
        if(paivitettavaLaite.equals(laite)) {
            heitaVirheNaytolle("Muuta tietoja päivittääksesi laite");
        } else {
            try {
                tietokanta.paivitaLaite(paivitettavaLaite);
                
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Laitten päivittäminen");
                a.setHeaderText("Laite päivitetty!");
                a.showAndWait();
                
                controller.paivitaLaitteet();
                
            } catch (SQLException ex) {
                Logger.getLogger(MuokkaaLaitettaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void asetaLaite(Laite l) {
        
        if(l != null) {
            laite = l;
            txtLaiteID.setText(String.valueOf(l.getLaiteId()));
            txtKuvaus.setText(l.getKuvaus());
            txtHinta.setText(String.valueOf(l.getHintaPvm()));
            
        }
    }
    
    public void asetaToimitila(Toimitila t) {
        
        if(toimitila != null) {
            toimitila = t;
        }
    }
    
    public void asetaController(PalvelutJaLaitteetController c) {
        
        if(c != null) {
            controller = c;
        }
    }
    
    private void heitaVirheNaytolle(String virhe) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Palvelut ja Laitteet");
        a.setHeaderText(virhe);
        //a = muotoileIlmoitus(a);
        a.showAndWait();
    }
    
}
