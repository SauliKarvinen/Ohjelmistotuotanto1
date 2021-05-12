/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.asiakkuudet;


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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vuoto.luokkafilet.Asiakas;
import vuoto.tietokanta.DBAccess;


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
    private Asiakas asiakas;
    private DBAccess tietokanta = new DBAccess();
    private AsiakkuudetController controller;

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

    /**
     * Päivittää asiakkaan
     * @param event Tallenna -napin painallus
     */
    @FXML
    private void btnTallennaPainettu(ActionEvent event) {
        
        Asiakas paivitettyAsiakas = null;
        
        int asiakasId = asiakas.getAsiakasId();
        String etunimi = txtEtunimi.getText();
        String sukunimi = txtSukunimi.getText();
        String lahiosoite = txtLahiosoite.getText();
        String postinumero = txtPostinumero.getText();
        String puhelinnumero = txtPuhelinnumero.getText();
        String sahkoposti = txtSahkoposti.getText();
        String yrityksenNimi = txtYrityksenNimi.getText();
        
        paivitettyAsiakas = new Asiakas(asiakasId, etunimi, sukunimi, lahiosoite, postinumero, puhelinnumero, sahkoposti, yrityksenNimi);
        
        // Tarkastaa että tietoja on muutettu
        if(paivitettyAsiakas.equals(asiakas)) {
            heitaVirheNaytolle("Muuta tietoja päivittääksesi asiakas");
        } else {
            try {
                tietokanta.muokkaaAsiakas(paivitettyAsiakas);
                
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Asiakkaan päivitys");
                a.setHeaderText("Asiakas päivitetty!");
                a.showAndWait();
                
                controller.paivitaTableview();
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.close();
                
            } catch (SQLException ex) {
                Logger.getLogger(MuokkaaAsiakastaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void taytaAsiakkaanTiedot(Asiakas a) {
        
        if(a != null) {
            asiakas = a;
            txtAsiakasID.setText(String.valueOf(a.getAsiakasId()));     
            txtYrityksenNimi.setText(a.getYrityksenNimi());
            txtEtunimi.setText(a.getEtunimi());
            txtSukunimi.setText(a.getSukunimi());
            txtLahiosoite.setText(a.getLahiosoite());
            txtPostinumero.setText(a.getPostinumero());
            txtPuhelinnumero.setText(a.getPuhelinnumero());
            txtSahkoposti.setText(a.getSahkoposti());

        }
    }
    
    /**
     * Asettaa AsiakkuudetController luokan controllerin
     * @param c controller
     */
    public void asetaController(AsiakkuudetController c) {
       
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
