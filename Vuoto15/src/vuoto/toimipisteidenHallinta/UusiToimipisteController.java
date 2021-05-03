/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.toimipisteidenHallinta;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vuoto.luokkafilet.Toimipiste;
import vuoto.tietokanta.DBAccess;


/**
 * FXML Controller class
 *
 * @author marko
 */
public class UusiToimipisteController implements Initializable {

    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/toimipisteidenHallinta/UusiToimipiste.fxml";
    private DBAccess tietokanta = new DBAccess();
    @FXML
    private Button btnTakaisin;
    @FXML
    private Button btnLisaaToimipiste;
    @FXML
    private TextArea txaKuvaus;
    @FXML
    private TextField txfTPnimi;
    @FXML
    private TextField txfTPlahiosoite;
    @FXML
    private TextField txfPnumero;
    
    private Connection conn;
    
    
    /**
     * Lisätään toimipiste kantaan ja palataan Toimipisteet näkymään.
     * 
     * @param event 
     */
            
    @FXML
    private void LisaaToimipistePainettu(ActionEvent event) throws Exception {
        // alustetaan muuttujat tietokantaa varten. 
        Toimipiste tpiste = new Toimipiste();
        
        // Formin kentät -> olion muuttujiin.
        tpiste.setToimipistenimi(txfTPnimi.getText());
        tpiste.setLahiosoite(txfTPlahiosoite.getText());
        tpiste.setPostinumero(txfPnumero.getText());
        tpiste.setKuvaus(txaKuvaus.getText());
        
        // DB yhteys
        tietokanta.lisaaToimipiste(tpiste);
        
        // ja palataan - Toimipisteiden hallinta.
        ToimipisteetController controller = (ToimipisteetController) siirryNakymaan(ToimipisteetController.fxmlString, "Toimipisteiden hallinta", event);
        //controller.asetaToimipiste(toimipiste);
    }

    
    
    /**
     * Palataan Toimipisteet näkymään.
     * @param event 
     */
    @FXML
    private void btnTakaisinToimipisteisiinPainettu(ActionEvent event) {
        // Opens panel - Toimipisteiden hallinta.
        ToimipisteetController controller = (ToimipisteetController) siirryNakymaan(ToimipisteetController.fxmlString, "Toimipisteiden hallinta", event);
        //controller.asetaToimipiste(toimipiste);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
            Logger.getLogger(UusiToimipisteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return controller;
   
    } 

}
