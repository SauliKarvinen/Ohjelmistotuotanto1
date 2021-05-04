/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.palvelutJaLaitteet;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import vuoto.aloitus.VuotoMainController;
import static vuoto.aloitus.VuotoMainController.valittuToimipiste;
import vuoto.laskutus.LaskutusController;
import vuoto.luokkafilet.Laite;
import vuoto.luokkafilet.Palvelu;
import vuoto.luokkafilet.Toimitila;
import vuoto.tietokanta.DBAccess;

/**
 * FXML Controller class
 *
 * @author marko
 */
public class PalvelutJaLaitteetController implements Initializable {

    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/palvelutJaLaitteet/PalvelutJaLaitteet.fxml";
    @FXML
    private Label lblToimipisteValinta;
    @FXML
    private TextField txtToimipiste;
    @FXML
    private TableView<Palvelu> tbviewPalvelut;
    @FXML
    private TableView<Laite> tbviewLaitteet;
    @FXML
    private Button btnMuokkaaPalvelua;
    @FXML
    private Button btnPoistaPalvelu;
    @FXML
    private Button btnMuokkaaLaitetta;
    @FXML
    private Button btnPoistaLaite;
    @FXML
    private ComboBox<Toimitila> cbToimitilavalikko;
    private Toimitila valittuToimitila;
    private DBAccess tietokanta = new DBAccess();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Näytetään aktiivinen Toimipiste (eli työpaikka 8))
        txtToimipiste.setFocusTraversable(false);
        txtToimipiste.setText(valittuToimipiste);
        
        paivitaToimitilavalikko();
        
        // Kuuntelee toimitilavalikon valintaa ja asettaa toimitilan
        cbToimitilavalikko.getSelectionModel().selectedItemProperty().addListener((s1, s2, s3) -> {
            
            if (s3 != s2) {
                valittuToimitila = s3;
                
            }
        });
        
        
        
//      // Asettaa Muokkaa ja Poista napit aktiiviseksi vasta kun on valittu jotain
//        tbviewPalvelut.getSelectionModel().selectedItemProperty().addListener((s1, s2, s3) -> {
//            if(s3 != null) {
//                btnMuokkaaPalvelua.setDisable(false);
//                btnPoistaPalvelu.setDisable(false);
//                palvelu = s3;
//            }
//        });
    }    
    
    /**
     * Lisää toimitilat toimitilat-valikkoon
     */
    private void paivitaToimitilavalikko() {
        
        ObservableList<Toimitila> toimitilat = null;
                
        if (txtToimipiste.getText().equals("Kaikki toimipisteet")) {
            toimitilat = tietokanta.haeKaikkiToimitilat();
            cbToimitilavalikko.setItems(toimitilat);
        } else {
            toimitilat = tietokanta.haeToimitilatToimipisteesta(valittuToimipiste);
            cbToimitilavalikko.setItems(toimitilat);
        }
    }

    /**
     * Palauttaa näkymän etusivulle
     * @param event Etusivulle -napin painallus
     */
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
    
    /**
     * Heittää virheilmoituksen näytölle
     * @param virhe Virheilmoitus
     */
    private void heitaVirheNaytolle(String virhe) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Palvelut ja Laitteet");
        a.setHeaderText(virhe);
        a = muotoileIlmoitus(a);
        a.showAndWait();
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
     * Avaa uuden pop-up ikkunan Palvelun lisäämiselle
     * @param event "Lisää palvelu" -painettu
     */
    @FXML
    private void btnLisaaUusiPalveluPainettu(ActionEvent event) {
        
        LisaaUusiPalveluController controller = (LisaaUusiPalveluController) avaaUusiIkkuna(LisaaUusiPalveluController.fxmlString, "Lisää uusi palvelu");
    }

    /**
     * Avaa uuden pop-up ikkunan Laitteen lisäämiselle
     * @param event "Lisää palvelu" - painettu
     */
    @FXML
    private void btnLisaaUusiLaitePainettu(ActionEvent event) {
        
        LisaaUusiLaiteController controller = (LisaaUusiLaiteController) avaaUusiIkkuna(LisaaUusiLaiteController.fxmlString, "Lisää uusi laite");  
    }
  
    @FXML
    private void btnMuokkaaPalveluaPainettu(ActionEvent event) {
        
        MuokkaaPalveluaController controller = (MuokkaaPalveluaController) avaaUusiIkkuna(MuokkaaPalveluaController.fxmlString, "Palvelun muokkaus");
    }

    @FXML
    private void btnPoistaPalveluPainettu(ActionEvent event) {
        
        boolean okPainettu = heitaVahvistusNaytolle("Poistetaanko palvelu " + "*tähän palvelu*" + "?", "Palvelun poistaminen");
        
        // if(okPainettu) niin poista tiedot.........
        
    }

    @FXML
    private void btnMuokkaaLaitettaPainettu(ActionEvent event) {
        
        MuokkaaLaitettaController controller = (MuokkaaLaitettaController) avaaUusiIkkuna(MuokkaaLaitettaController.fxmlString, "Laitteen muokkaus");
    }

    @FXML
    private void btnPoistaLaitePainettu(ActionEvent event) {
        
        boolean okPainettu = heitaVahvistusNaytolle("Poistetaanko laite " + "*tähän laite*" + "?", "Laitteen poistaminen");
        // if(okPainettu) niin poista tiedot.........
    }
    
    /**
     * Näyttää uuden ikkunan
     * @param fxml avattava .fxml tiedosto
     * @param title uuden ikkunan otsikko
     * @return avattavan .fxml näkymän Controller
     */
    private Object avaaUusiIkkuna(String fxml, String title) {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));  
        Parent root = null;
        Object controller = null;
        
        try {
            root = loader.load();
        } catch (IOException ex) {
            heitaVirheNaytolle("Virhe luotaessa näkymää '" + title + "'");
            Logger.getLogger(PalvelutJaLaitteetController.class.getName()).log(Level.SEVERE, null, ex);
        }
        controller = loader.getController();
        
        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.show();
        
        return controller;
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
