/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.varaukset;

import vuoto.palvelutJaLaitteet.PalvelutJaLaitteetController;
import vuoto.aloitus.VuotoMainController;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import vuoto.asiakkuudet.LisaaUusiAsiakasController;
import vuoto.luokkafilet.Asiakas;
import vuoto.luokkafilet.Palvelu;
import vuoto.luokkafilet.Toimipiste;
import vuoto.luokkafilet.Toimitila;
import vuoto.tietokanta.DBAccess;
import vuoto.vuokrattavatKiinteistot.UusiKiinteistoController;

/**
 * FXML Controller class
 *
 * @author sauli
 */
public class UusiVarausController implements Initializable {

    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/varaukset/UusiVaraus.fxml";
    @FXML
    private Label lblToimipisteValinta;
    @FXML
    private TextField txtToimipiste;
    @FXML
    private VBox palvelutIkkuna;
    @FXML
    private VBox laitteetIkkuna;
    @FXML
    private Button btnLisaaAsiakas1;
    @FXML
    private TextField txtLoppusumma;
    @FXML
    private Button btnTakaisin;
    @FXML
    private DatePicker dpLopetuspvm;
    @FXML
    private DatePicker dpAloituspvm;
    private DBAccess tietokanta = new DBAccess();
    Toimipiste toimipiste = null;
    @FXML
    private TextField txtToimitila;
    @FXML
    private Button btnLisaaToimitila;
    @FXML
    private TextField txtAsiakas;
    private Toimitila valittuToimitila;
    private Asiakas valittuAsiakas;
    private Palvelu palvelu;
    private List<Palvelu> palvelut = new LinkedList<Palvelu>();
    private Map<Integer, LinkedHashSet<Palvelu>> palvelutMap = new HashMap<>();


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        maaritaToimipiste(); // Tämä metodi tossa heti alapuolella
        
        txtToimitila.textProperty().addListener((s1, s2, s3) -> {
            // haePalvelutToimitilaId:llä
            
        });
        
        listaPalveluista();
        
        
        // Hae palvelut HashMapiin, avain on Toimitilan nimi ja arvo on LinkedList toimitilan palveluista
        // Nopeuttaa ohjelmaa kun ei aina tarvitse tehdä uutta tietokantahakua kun toimitila vaihdetaan
        
        
        
    }    
    
    // Kokeilumetodi. Täytyy muuttaa lopulliseen versioon
    private List listaPalveluista() {
        
        palvelu = new Palvelu(1, 55, "Ruokailu");
        palvelut.add(palvelu);
        palvelu = new Palvelu(2, 250, "Konsultointi");
        palvelut.add(palvelu);
        palvelu = new Palvelu(3, 70, "Muut palvelut");
        palvelut.add(palvelu);
        
        return palvelut;
    }
    
    private Map toimitilanPalvelut(int toimitilaId, LinkedHashSet<Palvelu> paivitettyLista) {

        palvelutMap.putIfAbsent(toimitilaId, new LinkedHashSet<>());
        LinkedHashSet<Palvelu> paivitettavaLista = palvelutMap.get(toimitilaId);
        paivitettavaLista.addAll(paivitettyLista);
        
        
        return palvelutMap;
        
        // Muistiinpanoja itselle:
        // Kun valitaan toimitila -> tehdään tietokantahaku (palvelut joissa toimipisteId == valitun toimipisteen Id)  
        // Hakutulokset (ObservableList) lisätään HashMapiin ja avaimeksi annetaan toimitilan Id. 
        // Toimitila ID:llä voi hakea nyt HashMapista ID:tä vastaavat palvelut
        // LinkedHashSet jotta uudet lisätyt palvelut on helppo päivittää listaan (poistaa automaattisesti duplikaatit)
    }
    
    private void paivitaPalvelut() {
        
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

    @FXML
    private void LisaaToimipiste(ActionEvent event) {
    }

    @FXML
    private void btnTakaisinPainettu(ActionEvent event) {
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(VarauksetController.fxmlString));
            Parent vuoto = (Parent) fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();       
            stage.setTitle("Varaukset");
            stage.setScene(new Scene(vuoto));
            stage.show();
        } catch (IOException ex) {
            heitaVirheNaytolle("Virhe luotaessa näkymää Varaukset");
            Logger.getLogger(VuotoMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
    public Alert muotoileIlmoitus(Alert a) {
        
        String alert_css = getClass().getResource("/stylesheets/sauli_alert.css").toExternalForm();
        DialogPane dialog = a.getDialogPane();
        dialog.getStylesheets().add(alert_css);
        dialog.getStyleClass().add("alert");
        ((Button)a.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Takaisin");
        
        return a;
    }

    @FXML
    private void btnHaeToimitilaaPainettu(ActionEvent event) {
        
        HaeToimitilaaController controller = (HaeToimitilaaController) avaaUusiIkkuna(HaeToimitilaaController.fxmlString, "Hae toimitila");
        controller.setUusiVarausController(this);
    }

    @FXML
    private void btnHaeAsiakastaPainettu(ActionEvent event) {
        
        HaeAsiakastaController controller = (HaeAsiakastaController) avaaUusiIkkuna(HaeAsiakastaController.fxmlString, "Hae asiakas");
        controller.setUusiVarausController(this);
    }

    @FXML
    private void btnLisaaUusiAsiakasPainettu(ActionEvent event) {
        
        avaaUusiIkkuna(LisaaUusiAsiakasController.fxmlString, "Lisää asiakas");
    }
    
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

    @FXML
    private void btnLisaaToimitilaPainettu(ActionEvent event) {
        
        UusiKiinteistoController controller = (UusiKiinteistoController) avaaUusiIkkuna(UusiKiinteistoController.fxmlString, "Lisaa Toimitila");
    }
    
    public void asetaToimitila(Toimitila t) {
        if(t != null) {
            valittuToimitila = t;
            txtToimitila.setText(t.getTilanNimi());
        }
    }
    
    public void asetaAsiakas(Asiakas a) {
        if(a != null) {
            valittuAsiakas = a;
            txtAsiakas.setText(a.getEtunimi() + " " + a.getSukunimi());
        }
    }
    
}
