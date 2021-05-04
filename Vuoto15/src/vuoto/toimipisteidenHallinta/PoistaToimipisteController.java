/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.toimipisteidenHallinta;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import vuoto.luokkafilet.Toimipiste;
import vuoto.tietokanta.DBAccess;

/**
 * FXML Controller class
 *
 * @author marko
 */
public class PoistaToimipisteController implements Initializable {

    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/toimipisteidenHallinta/PoistaToimipiste.fxml";
    @FXML
    private Button btnPoistaToimipiste;
    @FXML
    private Button btnTakaisinToimipisteisiin;
    private ComboBox<Toimipiste> cbToimipiste;
    private DBAccess tietokanta = new DBAccess();
    @FXML
    private ComboBox<Toimipiste> cbToimipisteet;
    private Toimipiste toimipiste;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        haeToimipisteet();
        
        // valikosta valittu toimipiste = toimipiste
        cbToimipisteet.getSelectionModel().selectedItemProperty().addListener((s1, s2, s3) -> {
            toimipiste = s3;
        });
        
    } 
    
    /**
     * Lisää toimipisteet valikkoon
     */
    private void haeToimipisteet() {
        
        ObservableList<Toimipiste> toimipisteet = tietokanta.haeKaikkiToimipisteet();
        
        cbToimipisteet.getItems().addAll(toimipisteet);
    }

    /**
     * Poistetaan valittu toimipiste 
     * (SQL)
     * ja palataan toimipiste näkymään.
     * 
     * @param event 
     */
    @FXML
    private void PoistaToimipistePainettu(ActionEvent event) {
        
        boolean toimipistePoistettu = true;
        
        // Opens panel - Toimipisteiden hallinta.
        if(toimipiste == null) {
            heitaVirheNaytolle("Valitse poistettava toimipiste");
        } else {
            
            try {
                tietokanta.poistaToimipiste(toimipiste);
            } catch (SQLException ex) {
                toimipistePoistettu = false;
                Logger.getLogger(PoistaToimipisteController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if(toimipistePoistettu) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Toimipisteen poistaminen");
                    alert.setHeaderText("Toimipiste " + toimipiste.getToimipistenimi() + " poistettu");
                    alert.showAndWait();
                    ToimipisteetController controller = (ToimipisteetController) siirryNakymaan(ToimipisteetController.fxmlString, "Toimipisteiden hallinta", event);
                    //controller.asetaToimipiste(toimipiste);
                }
            }
            
        }
    }

    /**
     * Palataan toimipiste näkymään.
     * @param event 
     */
    @FXML
    private void btnTakaisinToimipisteisiinPainettu(ActionEvent event) {
       // Opens panel - Toimipisteiden hallinta.
        ToimipisteetController controller = (ToimipisteetController) siirryNakymaan(ToimipisteetController.fxmlString, "Toimipisteiden hallinta", event);
        //controller.asetaToimipiste(toimipiste); 
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
            Logger.getLogger(PoistaToimipisteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return controller;
   
    } 

    
   
}
