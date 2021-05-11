/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.laskutus;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.sound.midi.ControllerEventListener;
import vuoto.luokkafilet.Asiakas;
import vuoto.luokkafilet.Toimitila;
import vuoto.luokkafilet.Lasku;
import vuoto.tietokanta.DBAccess;

/**
 * FXML Controller class
 *
 * @author marko
 */
public class TaytettyLaskuController implements Initializable {
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/laskutus/TaytettyLaskuPohja.fxml";
    // Tietokanta yht.
    private final DBAccess tietokanta = new DBAccess();
    private int asiakkaanID = 0;
    private Asiakas asiakas = new Asiakas();
    
    private String AsiakasTxt = "";
    private String PalvelutTxt = "";
    private String LaitteetTxt = "";
    private String LoppuSummaTxt = "";
    private String LaskuTyyppi = "";
    private String KulutTxt = "";
    private String AsiaKulutTxt = "";
    
    // Laskun päiväys
    Lasku lasku = new Lasku();
    LocalDate day = LocalDate.now();
    DateTimeFormatter muuta = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    String now = day.format(muuta);
    // Laskun eräpäivä
    LocalDate ep = LocalDate.now();
    LocalDate ep2 = ep.plusDays(14);
    String ePaiva = ep2.format(muuta);
    
    
    @FXML
    private TextField txtPaivamaara;
    @FXML
    private TextField txtYritysJaHenkilo;
    @FXML
    private TextField txtNotes;
    @FXML
    private TextArea txtMaksajanTiedot;
    @FXML
    private TextArea txtKuluErittely;
    @FXML
    private TextField txtEraPvm;
    @FXML
    private TextField txtLoppuSumma;
    @FXML
    private Button butSend;
    @FXML
    private Button butTakaisin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        paivitaKentat();
        
    }    

    @FXML
    private void butSendPainettu(ActionEvent event) {
        // luetaan asiakkaan toive laskun tyypistä,
        // email, nappissa LÄHETÄ
        
        // posti, napissa TULOSTA
        
        //TO DO
        
    }

    @FXML
    private void butTakaisinPainettu(ActionEvent event) {
         // Opens panel - Laskutuksen hallinta.
        UusiLaskuController controller = (UusiLaskuController) siirryNakymaan("UusiLasku.fxml", "Laskutuksen hallinta", event);
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

    public void asetaKulutTxt() {
       StringBuilder pal_las = new StringBuilder("");
       pal_las.append(this.PalvelutTxt);
        System.out.println("PalvelutTxt"+PalvelutTxt);
       pal_las.append(this.LaitteetTxt);
       
       txtNotes.setText(pal_las.toString());
    }

    public void asetaAsiakasTxt(String AsiakasTxt) {
        
        this.AsiakasTxt = AsiakasTxt;
        txtYritysJaHenkilo.setText(AsiakasTxt);
        
    }
    
    public void asetaPalvelutTxt(String PalvelutTxt) {
        
        this.PalvelutTxt = PalvelutTxt;
    }

    public void asetaLaitteetTxt(String LaitteetTxt) {
        
        this.LaitteetTxt = LaitteetTxt;
    }

    public void asetaLoppuSummaTxt(String LoppuSummaTxt) {
        
        this.LoppuSummaTxt = LoppuSummaTxt;
        txtLoppuSumma.setText(LoppuSummaTxt);
        
    }

    public void asetaLaskunTyyppi(String LaskuTyyppi) {
        
        this.LaskuTyyppi = LaskuTyyppi;
    }  

    public void lisaaLaskunTulostusController(UusiLaskuController controller) {
        
        if(controller != null) {
           // this.controller = controller;
        }
    }    

    public void paivitaKentat() {
        // Postitus osoite
        txtYritysJaHenkilo.setText(AsiakasTxt); // Toimii
        //Maksajan tiedot
        txtMaksajanTiedot.setText(AsiakasTxt);  // Ei toimi?!
        // Listaus palveluista ja laitteista
        txtKuluErittely.setText(AsiaKulutTxt);  // Ei toimi?!
        asetaKulutTxt();                        // Ei toimi?!
        
        //Laskun päiväys
        txtPaivamaara.setText(now);             // Toimii
        //ERäpäivä
        txtEraPvm.setText(ePaiva);              // Toimii
    }
}
