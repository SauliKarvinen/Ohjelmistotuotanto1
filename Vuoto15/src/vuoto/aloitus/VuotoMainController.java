/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.aloitus;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import vuoto.asiakkuudet.AsiakkuudetController;
import vuoto.laskutus.LaskutusController;
import vuoto.palvelutJaLaitteet.PalvelutJaLaitteetController;
import vuoto.luokkafilet.Toimipiste;
import vuoto.tietokanta.DBAccess;
import vuoto.toimipisteidenHallinta.ToimipisteetController;
import vuoto.varaukset.VarauksetController;
import vuoto.vuokrattavatKiinteistot.VuokrattavatKiinteistotController;

/**
 * FXML Controller class
 *
 * @author marko
 */
public class VuotoMainController implements Initializable {

    // Näkymän sijainnin voi hakea mistä vaan näkymästä tyyliin VuotoMainController.fxmlString
    // Jos muutat fxml-tiedoston sijaintia niin vaihda tähän uusi sijainti!
    /** Näkymän fxml-filen sijainti*/
    public static final String fxmlString = "/vuoto/aloitus/VuotoMain.fxml"; 
    private DBAccess tietokanta = new DBAccess();
    @FXML
    private Label lblToimipisteValinta;
    @FXML
    private Button btnVuokrattavatKiinteistot;
    @FXML
    private Button btnVuokrattavatPalvelut;
    @FXML
    private Button btnToimipisteidenHallinta;
    @FXML
    private Button btnPalvelut;
    @FXML
    private Button btnLaskutus;
    @FXML
    private Button btnAsiakkuudet;
    //private MenuButton mnuBtnValitseToimipiste;
    @FXML
    private ComboBox<String> cbValitseToimipiste;
    
    public static String valittuToimipiste;
    
    private Toimipiste toimipiste;
    
    
    @FXML
    private void OpenVuokrattavatKiinteistot(ActionEvent event) {
        
        // Opens panel - VuokrattavatKiinteistot.
        VuokrattavatKiinteistotController controller = (VuokrattavatKiinteistotController)siirryNakymaan(VuokrattavatKiinteistotController.fxmlString, "Vuokrattavat Kiinteistot", event);
        
         
    }

    @FXML
    private void OpenVuokrattavatPalvelut(ActionEvent event) {
        
        // Opens panel - PalvelutJaLaitteet.
        PalvelutJaLaitteetController controller = (PalvelutJaLaitteetController)siirryNakymaan(PalvelutJaLaitteetController.fxmlString, "Palvelut ja laitteet", event);
        
        
    }

    @FXML
    private void OpenToimipisteidenHallinta(ActionEvent event) {
        
        // Opens panel - Toimipisteiden hallinta.
        ToimipisteetController controller = (ToimipisteetController) siirryNakymaan(ToimipisteetController.fxmlString, "Toimipisteiden hallinta", event);
        

    }

    @FXML
    private void OpenPalvelut(ActionEvent event) {
        
        // Open panel - Varaukset
        VarauksetController controller = (VarauksetController) siirryNakymaan(VarauksetController.fxmlString, "Varaukset", event);
        
        
    }

    @FXML
    private void OpenLaskutus(ActionEvent event) {
        
        // Opens panel - Laskutuksen hallinta.
        LaskutusController controller = (LaskutusController) siirryNakymaan(LaskutusController.fxmlString, "Laskutuksen hallinta", event);
        
    
    }

    @FXML
    private void OpenAsiakkuudet(ActionEvent event) {
        
       // Opens panel - Asiakkaiden hallinta.      
        AsiakkuudetController controller = (AsiakkuudetController) siirryNakymaan(AsiakkuudetController.fxmlString, "Asiakkaiden hallinta", event);
        

    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Lisää toimipistevalikkoon vaihtoehdon Kaikki toimipisteet
        cbValitseToimipiste.getItems().add("Kaikki toimipisteet");
        // Päivittää toimipistevalikon tietokannan toimipisteillä
        paivitaToimipistevalikko();
        
     
        // Tämä asettaa aina cbValitseToimipiste -valikosta / comboboxista valitun asian muutujaan valittuToimipiste (String)
        cbValitseToimipiste.getSelectionModel().selectedItemProperty().addListener((s1, s2, s3) -> {
            
            valittuToimipiste = s3;
        });
  
    }    
  
    /**
     * Hakee tietokannasta toimipisteet ja lisää ne cbValitseToimipiste -listaan
     */
    private void paivitaToimipistevalikko() {
        
//        tietokanta.lisaaToimipiste(new Toimipiste("Kauppakatu 15", "80100", "Joensuu", "Kauppakadun toimitila Joensuussa"));
//        tietokanta.lisaaToimipiste(new Toimipiste("Mannerheimintie 58", "00100", "Helsinki", "Mannerheimintien toimitila Helsingissä"));
//        tietokanta.lisaaToimipiste(new Toimipiste("Särkänniemi 99", "33100", "Tampere", "Särkänniemen toimitila Tampereella"));
        
        //Palauttaa listan tietokannan toimipisteistä
        ObservableList<Toimipiste> toimipisteet = tietokanta.haeKaikkiToimipisteet();

        // Lisää toimipisteet valikkoon
        for(Toimipiste t: toimipisteet) {
            cbValitseToimipiste.getItems().add(t.getToimipistenimi());
        }
    }
    
    /**
     * Heittää virheilmoituksen näytölle
     * @param virhe Virheilmoitus
     */
    private void heitaVirheNaytolle(String virhe) {
        
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("VuotoMain");
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
        
        boolean toimipisteValittu = true;
        Object controller = null; // Palautettava controller olio

        if (valittuToimipiste == null) {
            heitaVirheNaytolle("Valitse toimipiste");
            toimipisteValittu = false;
        }

        if (toimipisteValittu) {

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
                Logger.getLogger(VuotoMainController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return controller;

    }
}
