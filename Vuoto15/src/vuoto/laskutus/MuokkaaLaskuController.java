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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static vuoto.aloitus.VuotoMainController.valittuToimipiste;
import vuoto.tietokanta.DBAccess;
import vuoto.luokkafilet.Lasku;
import vuoto.luokkafilet.LaskuOlio;


/**
 * FXML Controller class
 *
 * @author marko
 */
public class MuokkaaLaskuController implements Initializable {
    
    private Lasku lasku;
    private DBAccess tietokanta = new DBAccess();
    private LaskutusController controller;
    private String valittuAsiakas;
    

    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/laskutus/MuokkaaLasku.fxml";
    @FXML
    private Label lblToimipisteValinta;
    @FXML
    private TextField txtToimipiste;
    private TextArea txtAsiakas;
    @FXML
    private TextField txtSumma;
    @FXML
    private TextArea txtLaskunNro;
    @FXML
    private TextArea txtVarausNro;
    @FXML
    private Button btTakaisin;
    @FXML
    private Button btMuokkaaLasku;
    @FXML
    private TextArea txtLaskTyyppi;

    @FXML
    private void btnTakaisinPainettu(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void MuokkaaLaskuPainettu(ActionEvent event) {
        Lasku updLasku = null;
        
        int laskuNro = lasku.getLaskuNro();
        int varausNro = lasku.getVarausId();
        String tyyppi = lasku.getLaskuntyyppi();
        int hinta = lasku.getHinta();
        
        updLasku = new Lasku(laskuNro,tyyppi,hinta, varausNro);
        // Tarkastaa että tietoja on muutettu
        if(updLasku.equals(lasku)) {
            heitaVirheNaytolle("Muuta tietoja päivittääksesi laskua");
        } else {
            try {
                tietokanta.muokkaaLasku(updLasku);
                
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Laskun päivitys");
                a.setHeaderText("Lasku päivitetty!");
                a.showAndWait();
                
                controller.populateTableViewLaskut(valittuAsiakas);
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.close();
                
            } catch (SQLException ex) {
                Logger.getLogger(MuokkaaLaskuController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtToimipiste.setText(valittuToimipiste);
        // TODO
        
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
            Logger.getLogger(MuokkaaLaskuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return controller;
   
    }
    
        public void taytaLaskunTiedot(Lasku la) {
        
        if(la != null) {
            lasku = la;
            txtToimipiste.setText(valittuToimipiste);
            txtLaskTyyppi.setText(la.getLaskuntyyppi());
            txtLaskunNro.setText(String.valueOf(la.getLaskuNro()));
            txtVarausNro.setText(String.valueOf(la.getVarausId()));
            txtSumma.setText(String.valueOf(la.getHinta()));
            
        }
    }
     
    /**
     * Asettaa AsiakkuudetController luokan controllerin
     * @param c controller
     */
    public void asetaController(LaskutusController c) {
       
        if(c != null) {
            controller = c;
        }
    }
    
     public void asetaValittuAsiakas(String valittuAsiakas) {
        
        this.valittuAsiakas = valittuAsiakas;
   
    }

    
        /**
     * Heittää virheilmoituksen näytölle
     * @param virhe Virheilmoitus
     */
    private void heitaVirheNaytolle(String virhe) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Laskun muokkaus");
        a.setHeaderText(virhe);
        a.showAndWait();
    }
}
