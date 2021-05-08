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
import vuoto.aloitus.VuotoMainController;
import vuoto.luokkafilet.Palvelu;
import vuoto.luokkafilet.Toimitila;
import vuoto.tietokanta.DBAccess;

/**
 * FXML Controller class
 *
 * @author sauli
 */
public class MuokkaaPalveluaController implements Initializable {

    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/palvelutJaLaitteet/MuokkaaPalvelua.fxml";
    @FXML
    private TextField txtToimipiste;
    @FXML
    private Label lblToimipisteValinta;
    @FXML
    private TextField txtPalveluID;
    @FXML
    private TextField txtKuvaus;
    @FXML
    private TextField txtHinta;
    private Palvelu palvelu;
    private Toimitila toimitila;
    private DBAccess tietokanta = new DBAccess();
    private PalvelutJaLaitteetController controller;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        txtToimipiste.setText(VuotoMainController.valittuToimipiste);
    }    

    @FXML
    private void btnTakaisinPainettu(ActionEvent event) {
        
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void btnLisaaPainettu(ActionEvent event) {
        
        Palvelu paivitettavaPalvelu = null;
        
        int palveluId = Integer.valueOf(txtPalveluID.getText());
        int hintaPvm = Integer.valueOf(txtHinta.getText());
        String kuvaus = txtKuvaus.getText();
        
        paivitettavaPalvelu = new Palvelu(palveluId, hintaPvm, kuvaus);
        
        if(paivitettavaPalvelu.equals(palvelu)) {
            heitaVirheNaytolle("Muuta tietoja päivittääksesi palvelu");
        } else {
            try {
                tietokanta.paivitaPalvelu(paivitettavaPalvelu);
                
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Palvelun päivittäminen");
                a.setHeaderText("Palvelu päivitetty!");
                a.showAndWait();
                
                controller.paivitaPalvelut();
                
            } catch (SQLException ex) {
                Logger.getLogger(MuokkaaPalveluaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void asetaPalvelu(Palvelu p) {
        
        if(p != null) {
            palvelu = p;
            txtPalveluID.setText(String.valueOf(p.getPalveluId()));
            txtKuvaus.setText(p.getKuvaus());
            txtHinta.setText(String.valueOf(p.getHintaPvm()));
            
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
