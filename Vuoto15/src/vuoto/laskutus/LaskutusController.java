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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import vuoto.aloitus.VuotoMainController;
import static vuoto.aloitus.VuotoMainController.valittuToimipiste;
import vuoto.luokkafilet.Asiakas;
import vuoto.luokkafilet.Lasku;
import vuoto.luokkafilet.Toimipiste;
import vuoto.tietokanta.DBAccess;


/**
 * FXML Controller class
 *
 * @author marko
 */
public class LaskutusController implements Initializable {
    // Tietokanta yht.
    private DBAccess tietokanta = new DBAccess();
    
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
    @FXML
    private String valittuAsiakas;
    @FXML
    private Button btnMuokkaaLasku;
    
    
    
     @FXML
    private void LisaaLaskuPainettu(ActionEvent event) {
        // Open panel - UusiLasku
        UusiLaskuController controller = (UusiLaskuController) siirryNakymaan(UusiLaskuController.fxmlString, "Uusi Lasku", event);
        //controller.asetaToimipiste(toimipiste);
        
    }

    @FXML
    private void PoistaLaskuPainettu(ActionEvent event) {
        // Open panel - UusiLasku
        PoistaLaskuController controller = (PoistaLaskuController) siirryNakymaan(PoistaLaskuController.fxmlString, "Poista Lasku", event);
        //controller.asetaToimipiste(toimipiste);
        
    }

    @FXML
    private void MuokkaaLaskuPainettu(ActionEvent event) {
        // Open panel - UusiLasku
        MuokkaaLaskuController controller = (MuokkaaLaskuController) siirryNakymaan(MuokkaaLaskuController.fxmlString, "Muokkaa Lasku", event);
        //controller.asetaToimipiste(toimipiste);
        
    }
    
    @FXML
    private void cbValitseAsiakasValittu(ActionEvent event) {
        // Hae asiakkaat TableViewhen
        // Todo
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
            
            // populateTableViewLaskut()
        });
        
        // Lista kaikista laskuista...
        // ToDo
        // By TP/All/Asiakas
        
        
    }    

     /*** TABLEVIEW **/   
    private ObservableList<Lasku> listLaskut;
    @FXML
    private TableView<Lasku> tblLaskut;
    @FXML
    private TableColumn<Lasku, Integer> colLaskunNro;
    @FXML
    private TableColumn<Lasku, Integer> colVarausNro;
    @FXML
    private TableColumn<Lasku, Integer> colAsiakas;
    @FXML
    private TableColumn<Lasku, Integer> colToimitila;
    @FXML
    private TableColumn<Lasku, String> colTyyppi;
    
    
    /**
     * Method to populate TableView: Laskut
     * Käytetään Laskun (DBAccess) metodia, 
     *  haeKaikkiLaskut() 
     *  Luetaan -> observableArrayList(listLaskut)
     *  Alustetaan -> setCellValueFactory määritykset sarakkeille.
     * 
     * */
    private void populateTableViewVaraukset() throws SQLException {
        // alustetaan lista
        listLaskut = FXCollections.observableArrayList();
       
        // Haetaan asiakkaan Varaukset (Ei vielä, nyt KAIKKI varaukset)
        listLaskut = tietokanta.haeKaikkiLaskut();
        
        // set propertyTab to TableView
       colLaskunNro.setCellValueFactory(new PropertyValueFactory<>("laskuNro"));
       colVarausNro.setCellValueFactory(new PropertyValueFactory<>("laskuntyyppi"));
       colAsiakas.setCellValueFactory(new PropertyValueFactory<>("yritysNimi"));
       colToimitila.setCellValueFactory(new PropertyValueFactory<>("toimitila"));
       colTyyppi.setCellValueFactory(new PropertyValueFactory<>("varausId"));
       
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
     * TABLEVIEW A S I A K K A A N   L A S K U T
     * 
     */
    
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

    
}
