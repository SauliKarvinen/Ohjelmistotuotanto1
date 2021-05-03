/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.vuokrattavatKiinteistot;

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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import vuoto.aloitus.VuotoMainController;
import vuoto.luokkafilet.Toimipiste;
import vuoto.luokkafilet.Toimitila;
import vuoto.tietokanta.DBAccess;

/**
 * FXML Controller class
 *
 * @author marko
 */
public class VuokrattavatKiinteistotController implements Initializable {

    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/vuokrattavatKiinteistot/VuokrattavatKiinteistot.fxml";
    private Toimipiste toimipiste = null;
    
    @FXML
    private Label lblToimipisteValinta;
    @FXML
    private TextField txtToimipiste;
    @FXML
    private Button btnEtusivulle;
    @FXML
    private Button btnLisaaKiinteisto;
    @FXML
    private Button btnPoistaKiinteisto;
    @FXML
    private Button btnMuokkaaKiinteisto;
    private DBAccess tietokanta = new DBAccess();
    @FXML
    private TableView<Toimitila> tbvToimitilat;
    @FXML
    private TableColumn<Toimitila, Integer> tbcTilaId;
    @FXML
    private TableColumn<Toimitila, String> tbcLahiosoite;
    @FXML
    private TableColumn<Toimitila, String> tbcPostinumero;
    @FXML
    private TableColumn<Toimitila, Integer> tbcHuonekoko;
    @FXML
    private TableColumn<Toimitila, Integer> tbcHintaPvm;
    @FXML
    private TableColumn<Toimitila, String> tbcKuvaus;
    @FXML
    private TableColumn<Toimitila, String> tbcToimitilaNimi;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        maaritaToimipiste();
        paivitaTableview();
     
    
    } 
    
    /**
     * Määrittää toimipisteen jonka tietoja haetaan
     * Määrittää Toimipiste -luokan olion ja asettaa toimipisteen nimen txtToimipiste kenttään
     */
    private void maaritaToimipiste() {
        
       
        // Aloitusvalikossa valittu toimipisteen nimi
        String valikostaValittu = VuotoMainController.valittuToimipiste;
        
        // Estetään ettei mene tietokantaan hakua WHERE toimipisteNimi = 'Kaikki toimipisteet'
        if(valikostaValittu.equals("Kaikki toimipisteet")) {
            // Käsitellään kaikkia toimipisteitä
            
        } else {        
            toimipiste = tietokanta.haeToimipisteNimella(valikostaValittu); // tietokannan haeToimipisteNimella(nimi) palauttaa toimipiste -olion. Nimenä se aloitusikkunassa valittu toimipisteen nimi.
        }
        
        txtToimipiste.setText(valikostaValittu); // Asetetaan näkymän toimipiste -tekstikenttään aloitusnäkymässä valittu valinta
        txtToimipiste.setFocusTraversable(false); // Tää ohjelma hölmösti "kohdistaa" näkymän tohon tekstikenttään ja mustamaalaa sen tekstin kun siihen lisätään tässä sisältöä. Tällä saa sen pois.
        
    }

    /**
     * Päivittää sivun tableviewiin tietokannan toimitilat
     */
    private void paivitaTableview() {

        ObservableList toimitilat = null;
        
        // Jos alussa valittu kaikki toimipisteet - hakee kaikki toimitilat. Muuten hakee valitun toimipisteen toimitilat
        if(txtToimipiste.getText().equals("Kaikki toimipisteet")) {
            toimitilat = tietokanta.haeKaikkiToimitilat();
        } else {
            toimitilat = tietokanta.haeToimitilatToimipisteesta(txtToimipiste.getText());
        }
        
        tbcTilaId.setMinWidth(100);
        tbcTilaId.setCellValueFactory(new PropertyValueFactory<>("tilaId"));
        tbcLahiosoite.setMinWidth(100);
        tbcLahiosoite.setCellValueFactory(new PropertyValueFactory<>("lahiosoite"));
        tbcPostinumero.setMinWidth(100);
        tbcPostinumero.setCellValueFactory(new PropertyValueFactory<>("postinumero"));
        tbcHuonekoko.setMinWidth(100);
        tbcHuonekoko.setCellValueFactory(new PropertyValueFactory<>("huonekoko"));
        tbcHintaPvm.setMinWidth(100);
        tbcHintaPvm.setCellValueFactory(new PropertyValueFactory<>("hintaPvm"));
        tbcKuvaus.setMinWidth(100);
        tbcKuvaus.setCellValueFactory(new PropertyValueFactory<>("kuvaus"));
        tbcToimitilaNimi.setMinWidth(100);
        tbcToimitilaNimi.setCellValueFactory(new PropertyValueFactory<>("tilanNimi"));
        
        tbvToimitilat.setItems(toimitilat);

    }

    /**
     * Palauttaa näkymän takaisin etusivulle
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
            Logger.getLogger(VuokrattavatKiinteistotController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        
    }
    
     @FXML
    private void btnLisaaKiinteistoPainettu(ActionEvent event) {
    // Opens panel - PalvelutJaLaitteet.
    UusiKiinteistoController controller = (UusiKiinteistoController)siirryNakymaan(UusiKiinteistoController.fxmlString, "Uusi Kiinteistö", event);
    //controller.asetaToimipiste(toimipiste);
    }
        
    
    @FXML
    private void btnPoistaKiinteistoPainettu(ActionEvent event) {
        // Opens panel - PalvelutJaLaitteet.
        PoistaKiinteistoController controller = (PoistaKiinteistoController)siirryNakymaan(PoistaKiinteistoController.fxmlString, "Poista Kiinteistö", event);
        //controller.asetaToimipiste(toimipiste);
    }

    @FXML
    private void btnMuokkaaKiinteistoPainettu(ActionEvent event) {
        // Opens panel - PalvelutJaLaitteet.
        MuokkaaKiinteistoController controller = (MuokkaaKiinteistoController)siirryNakymaan(MuokkaaKiinteistoController.fxmlString, "Muokkaa Kiinteistöä", event);
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
            Logger.getLogger(VuokrattavatKiinteistotController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return controller;
   
    } 

}
