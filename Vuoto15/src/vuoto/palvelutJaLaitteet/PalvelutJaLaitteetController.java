/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.palvelutJaLaitteet;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
    private ObservableList<Laite> laitteet = FXCollections.observableArrayList();
    private ObservableList<Palvelu> palvelut = FXCollections.observableArrayList();
    private boolean toimipisteessaOnToimitiloja = false;
    @FXML
    private TableColumn<Palvelu, Integer> colPalveluId;
    @FXML
    private TableColumn<Palvelu, String> colPalveluKuvaus;
    @FXML
    private TableColumn<Palvelu, Integer> colPalveluHintaPvm;
    @FXML
    private TableColumn<Laite, Integer> colLaiteId;
    @FXML
    private TableColumn<Laite, String> colLaiteKuvaus;
    @FXML
    private TableColumn<Laite, Integer> colLaiteHintaPvm;
    private Palvelu valittuPalvelu;
    private Laite valittuLaite;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Näytetään aktiivinen Toimipiste (eli työpaikka 8))
        txtToimipiste.setFocusTraversable(false);
        txtToimipiste.setText(valittuToimipiste);
        
        paivitaToimitilavalikko();
        paivitaPalvelut();
        paivitaLaitteet();
        
        // Kuuntelee toimitilavalikon valintaa ja asettaa toimitilan
        cbToimitilavalikko.getSelectionModel().selectedItemProperty().addListener((s1, s2, s3) -> {
            
            if (s3 != s2) {

                valittuToimitila = s3;
                paivitaPalvelut();
                paivitaLaitteet();
                
            }
        });
        
        tbviewPalvelut.getSelectionModel().selectedItemProperty().addListener((s1, s2, s3) -> {
            
            if(s3 != s2) {
                valittuPalvelu = s3;
            }
        });
        
        tbviewLaitteet.getSelectionModel().selectedItemProperty().addListener((s1, s2, s3) -> {
            
            if(s3 != s2) {
                valittuLaite = s3;
            }
        });

    }
    
    public void paivitaPalvelut() {

        if(txtToimipiste.getText().equals("Kaikki toimipisteet")) {
            palvelut = tietokanta.haeKaikkiPalvelut();
            cbToimitilavalikko.setValue(null);
            cbToimitilavalikko.setDisable(true);
        } else {
            if(toimipisteessaOnToimitiloja) {
                palvelut = tietokanta.haePalvelutToimitilasta(valittuToimitila);
            }
        }
        colPalveluId.setCellValueFactory(new PropertyValueFactory<>("palveluId"));
        colPalveluKuvaus.setCellValueFactory(new PropertyValueFactory<>("kuvaus"));
        colPalveluHintaPvm.setCellValueFactory(new PropertyValueFactory<>("hintaPvm"));
        tbviewPalvelut.setItems(palvelut);

    }

    public void paivitaLaitteet() {

         if(txtToimipiste.getText().equals("Kaikki toimipisteet")) {
            laitteet = tietokanta.haeKaikkiLaitteet();
        } else {
            if(toimipisteessaOnToimitiloja) {
                laitteet = tietokanta.haeLaitteetToimitilasta(valittuToimitila);
            }
        }

        colLaiteId.setCellValueFactory(new PropertyValueFactory<>("laiteId"));
        colLaiteKuvaus.setCellValueFactory(new PropertyValueFactory<>("kuvaus"));
        colLaiteHintaPvm.setCellValueFactory(new PropertyValueFactory<>("hintaPvm"));
        tbviewLaitteet.setItems(laitteet);

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
            if (toimitilat.size() > 0) {
                toimipisteessaOnToimitiloja = true;
            }
        }

        cbToimitilavalikko.getSelectionModel().selectFirst();
        if (valittuToimitila == null) {
            valittuToimitila = cbToimitilavalikko.getSelectionModel().getSelectedItem();
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
        //a = muotoileIlmoitus(a);
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
        
        if(valittuToimitila == null) {
            heitaVirheNaytolle("Valitse toimitila");
        } else {

            LisaaUusiPalveluController controller = (LisaaUusiPalveluController) avaaUusiIkkuna(LisaaUusiPalveluController.fxmlString, "Lisää uusi palvelu");
            controller.asetaToimitila(valittuToimitila);
            controller.lisaaPalvelutJaLaitteetController(this);
        }
    }

    /**
     * Avaa uuden pop-up ikkunan Laitteen lisäämiselle
     * @param event "Lisää palvelu" - painettu
     */
    @FXML
    private void btnLisaaUusiLaitePainettu(ActionEvent event) {
        
        if(valittuToimitila == null) {
            heitaVirheNaytolle("Valitse toimitila");
        } else {
            LisaaUusiLaiteController controller = (LisaaUusiLaiteController) avaaUusiIkkuna(LisaaUusiLaiteController.fxmlString, "Lisää uusi laite");
            controller.asetaToimitila(valittuToimitila);
            controller.lisaaPalvelutJaLaitteetController(this);
        }
    }   
  
    @FXML
    private void btnMuokkaaPalveluaPainettu(ActionEvent event) {
        
        if(valittuPalvelu == null) {
            heitaVirheNaytolle("Valitse muokattava palvelu");
        } else {
            MuokkaaPalveluaController controller = (MuokkaaPalveluaController) avaaUusiIkkuna(MuokkaaPalveluaController.fxmlString, "Palvelun muokkaus");
            controller.asetaToimitila(valittuToimitila);
            controller.asetaPalvelu(valittuPalvelu);
            controller.asetaController(this);
        }
    }

    /**
     * Poistaa listasta valitun palvelun
     * @param event "Poista" -napin painallus
     */
    @FXML
    private void btnPoistaPalveluPainettu(ActionEvent event) {
        
        if(valittuPalvelu == null) {
            heitaVirheNaytolle("Valitse poistettava palvelu");
        
        } else {
            // Kysyy vahvistuksen
            boolean okPainettu = heitaVahvistusNaytolle("Poistetaanko palvelu " + valittuPalvelu.getKuvaus() + "?", "Palvelun poistaminen");
            
            if(okPainettu) {
                try {
                    tietokanta.poistaPalvelu(valittuPalvelu.getPalveluId());
                    // Ilmoittaa jos poistaminen onnistui
                    heitaIlmoitusNaytolle("Palvelu poistettu", "Palvelun poistaminen");
                    
                    paivitaPalvelut();
                    
                } catch (SQLException ex) {
                    Logger.getLogger(PalvelutJaLaitteetController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }      
    }

    /**
     * Vaihtaa näkymän muokattavat laitteet -kikkunaan
     * @param event "Muokkaa" -napin painallus
     */
    @FXML
    private void btnMuokkaaLaitettaPainettu(ActionEvent event) {
        
        if(valittuLaite == null) {
            heitaVirheNaytolle("Valitse muokattava laite");
        } else {
            MuokkaaLaitettaController controller = (MuokkaaLaitettaController) avaaUusiIkkuna(MuokkaaLaitettaController.fxmlString, "Laitteen muokkaus");
            controller.asetaToimitila(valittuToimitila);
            controller.asetaLaite(valittuLaite);
            controller.asetaController(this);
        }
    }

    /**
     * Poistaa listasta valitun laitteen
     * @param event "Poista" -napin painallus
     */
    @FXML
    private void btnPoistaLaitePainettu(ActionEvent event) {
        
        if(valittuLaite == null) {
            heitaVirheNaytolle("Valitse poistettava laite");
        
        } else {
            // Kysyy vahvistuksen
            boolean okPainettu = heitaVahvistusNaytolle("Poistetaanko laite " + valittuLaite.getKuvaus() + "?", "Laitteen poistaminen");
            
            if(okPainettu) {
                try {
                    tietokanta.poistaLaite(valittuLaite.getLaiteId());
                    //Ilmoittaa jos poisto onnistui
                    heitaIlmoitusNaytolle("Laite poistettu", "Laitteen poistaminen");
                    
                    paivitaLaitteet();
          
                } catch (SQLException ex) {
                    Logger.getLogger(PalvelutJaLaitteetController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }  
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
    
    /**
     * Heittää ilmoituksen näytölle
     * @param teksti Ilmoituksen teksti
     * @param title Ilmoitus-ikkunan otsikko
     */
    public void heitaIlmoitusNaytolle(String teksti, String title) {
        
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(teksti);
        a.showAndWait();
    }
    
}
