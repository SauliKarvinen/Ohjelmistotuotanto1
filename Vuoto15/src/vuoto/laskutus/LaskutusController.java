/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.laskutus;

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
import vuoto.luokkafilet.Asiakas;
import vuoto.luokkafilet.Lasku;
import vuoto.luokkafilet.LaskuOlio;
import vuoto.luokkafilet.Toimipiste;
import vuoto.luokkafilet.LaskutTauluOlio;
import vuoto.tietokanta.DBAccess;


/**
 * FXML Controller class
 *
 * @author marko
 */
public class LaskutusController implements Initializable {
    // Tietokanta yht.
    private DBAccess tietokanta = new DBAccess();
    private Asiakas asiakas;
    
    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/laskutus/Laskutus.fxml";
    
    // FXML
    @FXML
    private Label lblToimipisteValinta;
    @FXML
    private TextField txtAktiivinenToimipiste;
    @FXML
    private Button btnLisaaLasku;
    @FXML
    private Button btnPoistaLasku;
    @FXML
    private Toimipiste toimipiste;
    @FXML
    private ComboBox<String> cbValitseAsiakas;
    private String valittuAsiakas;
    @FXML
    private Button btnMuokkaaLasku;
    
     /*** TABLEVIEW **/   
    @FXML
    private TableView<Lasku> tblLaskut;
    @FXML
    private TableColumn<Lasku, Integer> colLaskunNro;
    @FXML
    private TableColumn<Lasku, String> colTyyppi;
    @FXML
    private TableColumn<Lasku, Integer> colHinta;
    @FXML
    private TableColumn<Lasku, Integer> colVarausNro;
    @FXML
    private ObservableList<Lasku> listLaskut;
    private LaskuOlio laskuOlio;
    private ObservableList<LaskuOlio> laskut = null;
    private Lasku valLasku;
    
     @FXML
    private void LisaaLaskuPainettu(ActionEvent event) {
        // Open panel - UusiLasku
        UusiLaskuController controller = (UusiLaskuController) siirryNakymaan(UusiLaskuController.fxmlString, "Uusi Lasku", event);
        //controller.asetaToimipiste(toimipiste);
        
    }

    @FXML
    private void PoistaLaskuPainettu(ActionEvent event) {
        // Open panel - UusiLasku
    //    PoistaLaskuController controller = (PoistaLaskuController) siirryNakymaan(PoistaLaskuController.fxmlString, "Poista Lasku", event);
    
            
        boolean laskuPoistettu = true;

        if (laskuOlio == null) {

            heitaVirheNaytolle("Valitse poistettava lasku");

        } else {
            boolean okPainettu = heitaVahvistusNaytolle("Poistetaanko Lasku " + laskuOlio.getVarausId() + "?", "Laskun poistaminen");

            if (okPainettu) {
                try {
                    tietokanta.poistaLasku(laskuOlio.getVarausId());
                } catch (SQLException ex) {
                    laskuPoistettu = false;
                    heitaVirheNaytolle("Virhe poistettaessa laskua");
                    Logger.getLogger(LaskutusController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (laskuPoistettu) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Laskun poistaminen");
                a.setHeaderText("Lasku poistettu");
                a.showAndWait();
                try {
                    populateTableViewLaskut(valittuAsiakas);
                } catch (SQLException ex) {
                    Logger.getLogger(LaskutusController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @FXML
    private void MuokkaaLaskuPainettu(ActionEvent event) {
        // Open panel - UusiLasku
        
        if(valLasku == null) {
            heitaVirheNaytolle("Valitse lasku");
        } else {
            MuokkaaLaskuController controller = (MuokkaaLaskuController) avaaUusiIkkuna(MuokkaaLaskuController.fxmlString, "Laskun muokkaus");
            controller.taytaLaskunTiedot(valLasku);
            controller.asetaController(this);
        }
    }
    
    @FXML
    private void cbValitseAsiakasValittu(ActionEvent event) {
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Aktiivinen toimipiste (eli yksi keskuksista)
        txtAktiivinenToimipiste.setFocusTraversable(false);
        txtAktiivinenToimipiste.setText(valittuToimipiste);

        
        // ToDo - lista VAIN TP:n asiakkaista!!!!!!
        // Lista KAIKISTA asiakkaista. 
        paivitaAsiakasValikko();
        // Tämä asettaa aina cbValitseToimipiste -valikosta / comboboxista valitun asian muutujaan valittuToimipiste (String)
        cbValitseAsiakas.getSelectionModel().selectedItemProperty().addListener((s1, s2, s3) -> {
            
            valittuAsiakas = s3;
            
            // TableView aktivointi
        try {
            // Aktivoi TblView
            populateTableViewLaskut(valittuAsiakas);
            } catch (SQLException ex) {
                heitaVirheNaytolle("TableView:n aktivoinnissa virhe (Laskut).");
                Logger.getLogger(LaskutusController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
        
        tblLaskut.getSelectionModel().selectedItemProperty().addListener((s1, s2, s3) -> {
            
            if(s3 != s2) {
                valLasku = s3;
            }
        });
    }    

    
    
    /**
     * Method to populate TableView: Laskut
     * Käytetään Laskun (DBAccess) metodia, 
     *  haeAsiakkaanLaskut() 
     *  Luetaan -> observableArrayList(listLaskut)
     *  Alustetaan -> setCellValueFactory määritykset sarakkeille.
     * 
     * */
    void populateTableViewLaskut(String nimi) throws SQLException {
       // alustetaan lista
       listLaskut = FXCollections.observableArrayList();
       
       // Haetaan asiakkaan Varaukset (Ei vielä, nyt KAIKKI varaukset)
       Asiakas as = new Asiakas();
       as.setYrityksenNimi(nimi);
       listLaskut = tietokanta.haeAsiakkaanLaskut(as);
        // set propertyTab to TableView
       colLaskunNro.setCellValueFactory(new PropertyValueFactory<>("laskuNro"));
       colTyyppi.setCellValueFactory(new PropertyValueFactory<>("laskuntyyppi"));
       colHinta.setCellValueFactory(new PropertyValueFactory<>("hinta"));
       colVarausNro.setCellValueFactory(new PropertyValueFactory<>("varausId"));
       
       tblLaskut.setItems(listLaskut);
    }
    
    
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

    /** LISTA ASIAKKAISTA
     * 
     * Hakee tietokannasta toimipisteet ja lisää ne cbValitseToimipiste -listaan
     */
    private void paivitaAsiakasValikko() {
        //Palauttaa listan tietokannan toimipisteistä
        ObservableList<Asiakas> asiakkaat = tietokanta.haeKaikkiAsiakkaat();

        // Lisää toimipisteet valikkoon
        for(Asiakas a: asiakkaat) {
            cbValitseAsiakas.getItems().add(a.getYrityksenNimi());
        }
    }
   
    
    /**
     * Heittää virheilmoituksen näytölle
     * @param virhe Virheilmoitus
     */
    private void heitaVirheNaytolle(String virhe) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Laskutus");
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
            Logger.getLogger(LaskutusController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return controller;
   
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
            Logger.getLogger(LaskutusController.class.getName()).log(Level.SEVERE, null, ex);
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
     * Heittää Confirmation -ikkunan näytölle
     * @param viesti Ikkunan header-teksti
     * @param title Ikkunan otsikko
     * @return Jos painetaan OK = true, muuten false
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
        
        String alert_css = getClass().getResource("/vuoto/stylesheets/sauli_alert.css").toExternalForm();
        DialogPane dialog = a.getDialogPane();
        dialog.getStylesheets().add(alert_css);
        dialog.getStyleClass().add("alert");
        ((Button)a.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Takaisin");
        
        return a;
    }
}
