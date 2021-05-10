/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.laskutus;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import vuoto.luokkafilet.Asiakas;
import vuoto.luokkafilet.Toimitila;
import vuoto.luokkafilet.Lasku;
import vuoto.tietokanta.DBAccess;

/**
 * FXML Controller class
 *
 * @author marko
 */
public class TaytettyLaskuController implements Initializable {
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/laskutus/TaytettyLaskuPohja.fxml";
    // Tietokanta yht.
    private final DBAccess tietokanta = new DBAccess();
    private int asiakkaanID = 0;
    private Toimitila toimitila;
    private String valittuAsiakas;
    
    Lasku lasku = new Lasku();
    
    @FXML
    private TextField txtPaivamaara;
    @FXML
    private TextField txtYritysJaHenkilo;
    @FXML
    private TextField txtNotes;
    @FXML
    private TextArea txtMaksajanTiedot;
    @FXML
    private TextArea txtKuluErittely;
    @FXML
    private TextField txtEraPvm;
    @FXML
    private TextField txtLoppuSumma;
    @FXML
    private Button butSend;
    @FXML
    private Button butTakaisin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<Asiakas> asiakasL = FXCollections.observableArrayList();
        asiakasL = tietokanta.haeAsiakas(valittuAsiakas);
        int asId = 0;
        
        for(Asiakas a: asiakasL) {
            txtYritysJaHenkilo.setText(
                    a.getYrityksenNimi() + "\n" + 
                    a.getEtunimi() + " " + a.getSukunimi() + "\n" +
                    a.getLahiosoite() + " " + a.getPostinumero());
                    asiakkaanID = a.getAsiakasId();
                    asId =  a.getAsiakasId();
            }
        txtYritysJaHenkilo.setFocusTraversable(false);
       
        
    
        
    
    }    

    @FXML
    private void butSendPainettu(ActionEvent event) {
        // luetaan asiakkaan toive laskun tyypistä,
        // email, nappissa LÄHETÄ
        // posti, napissa TULOSTA
        
        //TO DO
        
    }

    @FXML
    private void butTakaisinPainettu(ActionEvent event) {
         // Opens panel - Laskutuksen hallinta.
        LaskutusController controller = (LaskutusController) siirryNakymaan("Laskutus.fxml", "Laskutuksen hallinta", event);
    }
    
        /**
     * Heittää virheilmoituksen näytölle
     * @param virhe Virheilmoitus
     */
    private void heitaVirheNaytolle(String virhe) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Vuokrattavat kiinteistöt");
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
            Logger.getLogger(UusiLaskuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return controller;
   
    } 
    
}
