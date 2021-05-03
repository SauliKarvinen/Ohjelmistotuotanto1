/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.asiakkuudet;

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
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vuoto.luokkafilet.Asiakas;
import vuoto.tietokanta.DBAccess;

/**
 * FXML Controller class
 *
 * @author sauli
 */
public class LisaaUusiAsiakasController implements Initializable {

    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/asiakkuudet/LisaaUusiAsiakas.fxml";
    
    // DB Access
    private DBAccess tietokanta = new DBAccess();
    
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

 

    @FXML
    private void btnLisaaAsiakasPainettu(ActionEvent event) {
        // alustetaan muuttujat tietokantaa varten. 
        Asiakas uusiClient = new Asiakas();
        
        // Formin kentät -> olion muuttujiin.
        uusiClient.setEtunimi(txtEtunimi.getText());
        uusiClient.setSukunimi(txtSukunimi.getText());
        uusiClient.setLahiosoite(txtLahiosoite.getText());
        uusiClient.setPostinumero(txtPostinumero.getText());
        uusiClient.setPuhelinnumero(txtPuhelinnumero.getText());
        uusiClient.setSahkoposti(txtSahkoposti.getText());
        uusiClient.setYrityksenNimi(txtYrityksenNimi.getText());    
        
        // DB yhteys
        tietokanta.lisaaAsiakas(uusiClient);
        
        // ja palataan - Toimipisteiden hallinta.
        AsiakkuudetController controller = (AsiakkuudetController) siirryNakymaan(AsiakkuudetController.fxmlString, "Asiakkaiden hallinta", event);
        
    }

    /**
     * Sulkee tämän ikkunan
     * @param event "Takaisin" - napin painallus
     */
    @FXML
    private void btnTakaisinPainettu(ActionEvent event) {
        
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    

    
    /**
     * Heittää vahvistusilmoituksen näytölle
     * @param viesti Ilmoituksen header-teksti
     * @param title Ilmoituksen otsikko
     * @return Palauttaa OK = true, CANCEL = false
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
        
        String alert_css = getClass().getResource("/stylesheets/sauli_alert.css").toExternalForm();
        DialogPane dialog = a.getDialogPane();
        dialog.getStylesheets().add(alert_css);
        dialog.getStyleClass().add("alert");
        ((Button)a.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Takaisin");
        
        return a;
    }
    
    
    /**
     * Heittää virheilmoituksen näytölle
     * @param virhe Virheilmoitus
     */
    private void heitaVirheNaytolle(String virhe) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Virhe toimipisteen lisäämisessä!");
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
            Logger.getLogger(LisaaUusiAsiakasController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return controller;
    }   
    
}
