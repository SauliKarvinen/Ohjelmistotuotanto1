/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.varaukset;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
 * @author sauli
 */
public class HaeToimitilaaController implements Initializable {

    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/varaukset/HaeToimitilaa.fxml";
    @FXML
    private TextField txtToimipiste;
    @FXML
    private Label lblToimipisteValinta;
    private Toimipiste toimipiste;
    private DBAccess tietokanta = new DBAccess();
    @FXML
    private TableView<Toimitila> tbvToimitilat;
    private Toimitila toimitila;
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
    @FXML
    private Button btnTakaisin;
    @FXML
    private Button btnValitse;
    private UusiVarausController uusivarauscontroller;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        maaritaToimipiste();
        paivitaTableview();
        
        
        
        tbvToimitilat.getSelectionModel().selectedItemProperty().addListener((s1, s2, s3) -> {
            
            toimitila = s3;
        });
        
        
        
    }    
    
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

    @FXML
    private void btnTakaisinPainettu(ActionEvent event) {
        
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void btnValitsePainettu(ActionEvent event) {

        uusivarauscontroller.asetaToimitila(toimitila);
        
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
    
    public void setUusiVarausController(UusiVarausController controller) {
        if(controller != null) {
            uusivarauscontroller = controller;
        }
    }
    
}
