/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.laskutus;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static vuoto.aloitus.VuotoMainController.valittuToimipiste;
import vuoto.luokkafilet.Lasku;
import vuoto.luokkafilet.Toimipiste;
import vuoto.luokkafilet.Asiakas;
import vuoto.luokkafilet.Toimitila;
import vuoto.tietokanta.DBAccess;


/**
 * FXML Controller class
 *
 * @author marko
 */
public class UusiLaskuController implements Initializable {
    // Tietokanta yht.
    private final DBAccess tietokanta = new DBAccess();
    
    private int asiakkaanID = 0;
    private Toimitila toimitila;
    private int laskutettavaTotal = 0;     // Laskua YHTEENSÄ
    private String tilat = "";
    private String palvelut = "";
    private String laitteet = "";

    
    //Listat tilatuista xxx, id:t, käytetään nimien selvitykseen.
    private List<Integer> ttilat = new LinkedList<Integer>();
    private List<Integer> tpalvelut = new LinkedList<Integer>();
    private List<Integer> tlaitteet = new LinkedList<Integer>();

    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/laskutus/UusiLasku.fxml";
    @FXML
    private Label lblToimipisteValinta;
    @FXML
    private TextField txtToimipiste;
    @FXML
    private Button btnTakaisin;
    @FXML
    private Toimipiste toimipiste;
    @FXML
    private ComboBox<String> cbAsiakas;
    
    private String valittuAsiakas;
    @FXML
    private Button btnVahvistaLasku;
    @FXML
    private TextArea txfAsiakas;
    @FXML
    private TextArea txfVuokKiinteisto;
    @FXML
    private TextArea txfPalvelut;
    @FXML
    private TextArea txfLaitteet;
    @FXML
    private TextField txtLasku;
    @FXML
    private CheckBox cbEmail;
    @FXML
    private CheckBox cbPaperi;
    
    private String laskunTyyppi = "email";
    
    Lasku lasku = new Lasku();
    

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
        
        paivitaAsiakasValikko();
        // Tämä asettaa aina cbValitseToimipiste -valikosta / comboboxista valitun asian muutujaan valittuToimipiste (String)
        cbAsiakas.getSelectionModel().selectedItemProperty().addListener((s1, s2, s3) -> {
            valittuAsiakas = s3;
            paivitaAsiakasKentat();
        });
        
        
    }    
    @FXML
    private void btnPoistaLaskuPainettu(ActionEvent event) {
        // Open panel - PoistaLasku
        PoistaLaskuController controller = (PoistaLaskuController) siirryNakymaan("PoistaLasku.fxml", "Poista Lasku", event);
        //controller.asetaToimipiste(toimipiste);
    }
    @FXML
    private void btnMuokkaaLaskuPainettu(ActionEvent event) {
        // Open panel - UusiLasku
        MuokkaaLaskuController controller = (MuokkaaLaskuController) siirryNakymaan("MuokkaaLasku.fxml", "Muokkaa Laskua", event);
    }

    @FXML
    private void btnTakaisinLaskutukseen(ActionEvent event) {
         // Opens panel - Laskutuksen hallinta.
        LaskutusController controller = (LaskutusController) siirryNakymaan("Laskutus.fxml", "Laskutuksen hallinta", event);
    }

    @FXML
    private void VahvistaLaskuPainettu(ActionEvent event) {
        // Opens panel - Laskun lähetys/tulostus.
        TaytettyLaskuController controller = (TaytettyLaskuController) siirryNakymaan("TaytettyLaskuPohja.fxml", "Lähetä Lasku", event);
    }
    
    @FXML
    private void cbEmailValittu(ActionEvent event) {
        if (cbEmail.isSelected()){
            cbPaperi.setSelected(false);
            laskunTyyppi="email";
            lasku.setLaskunTyyppi(laskunTyyppi);
            
            
        }
        
    }

    @FXML
    private void cbPaperiValittu(ActionEvent event) {
        if (cbPaperi.isSelected()){
            cbEmail.setSelected(false);
            laskunTyyppi = "paperi";
            lasku.setLaskunTyyppi(laskunTyyppi);
        }
        
    }
    
    public void asetaToimipiste(Toimipiste toimipiste) {
        
        if (toimipiste != null) {
            this.toimipiste = toimipiste;
            txtToimipiste.setText(toimipiste.getToimipistenimi());
        }
    }
 
           
    private void paivitaAsiakasKentat(){
        ObservableList<Asiakas> asiakasL = FXCollections.observableArrayList();
        int asId = 0;
        asiakasL = tietokanta.haeAsiakas(valittuAsiakas);
        
        for(Asiakas a: asiakasL) {
            txfAsiakas.setText(
                    a.getYrityksenNimi() + "\n" + 
                    a.getEtunimi() + " " + a.getSukunimi() + "\n" +
                    a.getLahiosoite() + " " + a.getPostinumero());
                    asiakkaanID = a.getAsiakasId();
                    asId =  a.getAsiakasId();
            }
     
    // hae asiakkaan vuokraamat kiinteistöt
    // hakee id:llä varukset, ja palauttaa nimet:
        tilat = tietokanta.haeAsiakkaanToimitilaVaraukset(valittuAsiakas);
        txfVuokKiinteisto.setText(String.valueOf(tilat));
        
    // Haetaan asiakkaan palvelut
        String palvelut = tietokanta.haeAsiakkaanPalvelut(valittuAsiakas);
        txfPalvelut.setText(palvelut);
        
    // Haetaan asiakkaan Laitteet
        String laitteet = tietokanta.haeAsiakkaanLaitteet(valittuAsiakas);
        txfLaitteet.setText(laitteet);
        
    
    ///////////// L A S K E T A A N   L O P P U S U M M A  ///////////////////
    // Lasketaan hinta
        int hintaTmp = 0;   // Välihinta
        int hintaFinal = 0; // Lopullinen hinta
        int laskutettava = 0;     // LaskuaKiinteistöistä
        int laskutettavaPalvelut = 0;     // Laskua Palveluista
        int laskutettavaLaitteet = 0;     // Laskua Laitteista
        
        laskutettava=tietokanta.haeLaskutettava(valittuAsiakas);
        laskutettavaPalvelut=tietokanta.haeLaskutettavaPalvelut(valittuAsiakas);
        //laskutettavaLaitteet=tietokanta.haeLaskutettavaLaitteet(valittuAsiakas);
        
        // Lopullinen hinta:
        laskutettavaTotal=laskutettava+laskutettavaPalvelut+laskutettavaLaitteet;
        txtLasku.setText(String.valueOf(laskutettavaTotal));
        lasku.setHinta(laskutettavaTotal);
    
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
