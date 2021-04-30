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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static vuoto.aloitus.VuotoMainController.valittuToimipiste;
import vuoto.luokkafilet.Lasku;
import vuoto.luokkafilet.Toimipiste;
import vuoto.luokkafilet.Asiakas;
import vuoto.tietokanta.DBAccess;

/**
 * FXML Controller class
 *
 * @author marko
 */
public class UusiLaskuController implements Initializable {
    // Tietokanta yht.
    private final DBAccess tietokanta = new DBAccess();
    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/laskutus/UusiLasku.fxml";
    @FXML
    private Label lblToimipisteValinta;
    @FXML
    private TextField txtToimipiste;
    @FXML
    private Button btnTakaisin;
    private Lasku uusiLasku;
    private Toimipiste toimipiste;
    @FXML
    private ComboBox<String> cbAsiakas;
    private String valittuAsiakas;
    @FXML
    private Button btnVahvistaLasku;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     * 
     * Asettaa aktiivisen toimipisteen
     * Sekä hakee listan (laskutettavista) asiakkaista
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Aktiivinen toimipiste (eli yksi keskuksista)
        txtToimipiste.setFocusTraversable(false);
        txtToimipiste.setText(valittuToimipiste);
        
        // Lista KAIKISTA asiakkaista. 
        
        // ToDo - lista TP:n asiakkaista!!!!!!
        
        paivitaAsiakasValikko();
        // Tämä asettaa aina cbValitseToimipiste -valikosta / comboboxista valitun asian muutujaan valittuToimipiste (String)
        cbAsiakas.getSelectionModel().selectedItemProperty().addListener((s1, s2, s3) -> {
            
            valittuAsiakas = s3;
        });
        
    /*    
        // TABLEVIEW VARAUKSET aktivointi
        try {
            // Aktivoi TblView
            populateTableViewVaraukset();
        } catch (SQLException ex) {
            heitaVirheNaytolle("TableView:n aktivoinnissa virhe (Toimipisteet).");
            Logger.getLogger(ToimipisteetController.class.getName()).log(Level.SEVERE, null, ex);
        }
    */
    }    

    private void btnPoistaLaskuPainettu(ActionEvent event) {
        // Open panel - PoistaLasku
        PoistaLaskuController controller = (PoistaLaskuController) siirryNakymaan("PoistaLasku.fxml", "Poista Lasku", event);
        //controller.asetaToimipiste(toimipiste);
    }

    private void btnMuokkaaLaskuPainettu(ActionEvent event) {
        // Open panel - UusiLasku
        MuokkaaLaskuController controller = (MuokkaaLaskuController) siirryNakymaan("MuokkaaLasku.fxml", "Muokkaa Laskua", event);
        //controller.asetaToimipiste(toimipiste);
    }

    @FXML
    private void btnTakaisinLaskutukseen(ActionEvent event) {
         // Opens panel - Laskutuksen hallinta.
        LaskutusController controller = (LaskutusController) siirryNakymaan("Laskutus.fxml", "Laskutuksen hallinta", event);
        // controller.asetaToimipiste(toimipiste);
    
    }

    @FXML
    private void VahvistaLaskuPainettu(ActionEvent event) {
    }
    
    public void asetaToimipiste(Toimipiste toimipiste) {
        
        if (toimipiste != null) {
            this.toimipiste = toimipiste;
            txtToimipiste.setText(toimipiste.getToimipistenimi());
        }
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
            cbAsiakas.getItems().add(a.getYrityksenNimi());
        }
    }
    
    
    private void btnEtusivullePainettu(ActionEvent event) {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("VuotoMain.fxml"));
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
