/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.asiakkuudet;

//import vuoto.palvelutJaLaitteet.PalvelutJaLaitteetController;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import vuoto.aloitus.VuotoMainController;
import vuoto.luokkafilet.Asiakas;
import vuoto.luokkafilet.Toimipiste;
import vuoto.luokkafilet.Toimitila;
import vuoto.tietokanta.DBAccess;

/**
 * FXML Controller class
 *
 * @author marko
 */
public class AsiakkuudetController implements Initializable {

    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/asiakkuudet/Asiakkuudet.fxml";
    @FXML
    private TableView<Asiakas> tbvAsiakkaat;
    @FXML
    private TableColumn<Asiakas, Integer> colAsiakasId;
    @FXML
    private TableColumn<Asiakas, String> colEtunimi;
    @FXML
    private TableColumn<Asiakas, String> colSukunimi;
    @FXML
    private TableColumn<Asiakas, String> colLahiosoite;
    @FXML
    private TableColumn<Asiakas, String> colPostinumero;
    @FXML
    private TableColumn<Asiakas, String> colPuhelinnumero;
    @FXML
    private TableColumn<Asiakas, String> colSahkoposti;
    @FXML
    private TableColumn<Asiakas, String> colYritys;
    private DBAccess tietokanta = new DBAccess();
    private Toimipiste valittuToimipiste;
    private Toimitila valittuToimitila;
    @FXML
    private ComboBox<Toimitila> cbToimitila;
    @FXML
    private ComboBox<String> cbToimipiste;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    


    private void paivitaTableview() {
        
        //ObservableList<Asiakas> asiakkaat = tietokanta.haeas
        
        colAsiakasId.setCellValueFactory(new PropertyValueFactory<>("asiakasId"));
        colEtunimi.setCellValueFactory(new PropertyValueFactory<>("etunimi"));
        colSukunimi.setCellValueFactory(new PropertyValueFactory<>("sukunimi"));
        colLahiosoite.setCellValueFactory(new PropertyValueFactory<>("lahiosoite"));
        colPostinumero.setCellValueFactory(new PropertyValueFactory<>("postinumero"));
        colPuhelinnumero.setCellValueFactory(new PropertyValueFactory<>("puhelinnumero"));
        colSahkoposti.setCellValueFactory(new PropertyValueFactory<>("sahkoposti"));
        colYritys.setCellValueFactory(new PropertyValueFactory<>("yrityksenNimi"));
        

    }
    
    private void paivitaToimitilatValikko() {
        
        //ObservableList<Toimitila> toimitilat = tietokanta.
    }
    
    private void paivitaToimipisteetValikko() {
        
    }
    
    private void heitaVirheNaytolle(String virhe) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Asiakkuudet");
        a.setHeaderText(virhe);
        a.showAndWait();
    }


    @FXML
    private void btnEtusivullePainettu(ActionEvent event) {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(VuotoMainController.fxmlString));
        Parent root = null;
        
        try {
            root = loader.load();
        } catch (IOException ex) {
            heitaVirheNaytolle("Virhe luotaessa näkymää VuotoMain.fxml");
            Logger.getLogger(AsiakkuudetController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    private void btnLisaaAsiakasPainettu(ActionEvent event) {
        
        avaaUusiIkkuna(LisaaUusiAsiakasController.fxmlString, "Lisää asiakas");
        
    }

    @FXML
    private void btnMuokkaaAsiakastaPainettu(ActionEvent event) {
        
        MuokkaaAsiakastaController controller = (MuokkaaAsiakastaController) avaaUusiIkkuna(MuokkaaAsiakastaController.fxmlString, "Asiakkaan muokkaus");
    }

    @FXML
    private void btnPoistaAsiakasPainettu(ActionEvent event) {
        
        boolean okPainettu = heitaVahvistusNaytolle("Poistetaanko palvelu " + "*tähän asiakas*" + "?", "Palvelun poistaminen");
        
        // if(okPainettu) niin poista tiedot.........
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
            Logger.getLogger(AsiakkuudetController.class.getName()).log(Level.SEVERE, null, ex);
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
