/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.toimipisteidenHallinta;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import vuoto.aloitus.VuotoMainController;
import static vuoto.aloitus.VuotoMainController.valittuToimipiste;
import vuoto.luokkafilet.Toimipiste;
import vuoto.tietokanta.DBAccess;


/**
 * FXML Controller class
 *
 * @author marko
 */
public class ToimipisteetController implements Initializable {
    
    private DBAccess tietokanta = new DBAccess();
    
    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/toimipisteidenHallinta/Toimipisteet.fxml";
    @FXML
    private Label lblToimipisteValinta;
    @FXML
    private TextField txtToimipiste;
    @FXML
    private Button btnLisaaToimipiste;
    @FXML
    private Button btnPoistaToimipiste;
    @FXML
    private Button btnMuokkaaToimipiste;

     @FXML
    private void LisaaToimipistePainettu(ActionEvent event) {
     // Opens panel - Uusi toimipiste.
    UusiToimipisteController controller = (UusiToimipisteController)siirryNakymaan(UusiToimipisteController.fxmlString, "Uusi Toimipiste", event);
    //controller.asetaToimipiste(toimipiste);   
    }

    @FXML
    private void PoistaToimipistePainettu(ActionEvent event) {
    PoistaToimipisteController controller = (PoistaToimipisteController)siirryNakymaan(PoistaToimipisteController.fxmlString, "Poista Toimipiste", event);
    }

    @FXML
    private void MuokkaaToimipistePainettu(ActionEvent event) {
    MuokkaaToimipisteController controller = (MuokkaaToimipisteController)siirryNakymaan(MuokkaaToimipisteController.fxmlString, "Muokkaa Toimipiste", event);
    
    }
   
     /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Aktiivinen toimipiste (eli yksi keskuksista)
        txtToimipiste.setFocusTraversable(false);
        txtToimipiste.setText(valittuToimipiste);
        
        // TableView aktivointi
        try {
            // Aktivoi TblView
            populateTableViewToimipisteet();
        } catch (SQLException ex) {
            heitaVirheNaytolle("TableView:n aktivoinnissa virhe (Toimipisteet).");
            Logger.getLogger(ToimipisteetController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 

    
    /*** TABLEVIEW **/
     @FXML
    private TableView<Toimipiste> tblToimipisteet;
    @FXML
    private TableColumn<Toimipiste, String> colToimipisteenId;
    @FXML
    private TableColumn<Toimipiste, String> colToimipisteenNimi;
    @FXML
    private TableColumn<Toimipiste, String> colLahiosoite;
    @FXML
    private TableColumn<Toimipiste, String> colPostinumero;
    @FXML
    private TableColumn<Toimipiste, String> colKuvaus;
    @FXML
    private ObservableList<Toimipiste> listTPs;
    
    /**
     * Method to populate TableView: COURSE
     * Käytetään Toimipisteiden (DBAccess) metodia, 
     *  haeKaikkiToimipisteet() 
     *  Luetaan -> observableArrayList(listTPs)
     *  Alustetaan -> setCellValueFactory määritykset sarakkeille.
     * 
     * !!!!! Nimi ei näy !!
     * */
    
    
    private void populateTableViewToimipisteet() throws SQLException {
        // alustetaan lista
        listTPs = FXCollections.observableArrayList();
       
        // Haetaan toimipisteet...         
        listTPs = tietokanta.haeKaikkiToimipisteet();
        
        // set propertyTab to TableView
       colToimipisteenId.setCellValueFactory(new PropertyValueFactory<>("toimipisteID"));
       colLahiosoite.setCellValueFactory(new PropertyValueFactory<>("lahiosoite"));
       colPostinumero.setCellValueFactory(new PropertyValueFactory<>("postinumero"));
       colToimipisteenNimi.setCellValueFactory(new PropertyValueFactory<>("toimipisteNimi"));
       colKuvaus.setCellValueFactory(new PropertyValueFactory<>("kuvaus"));

        tblToimipisteet.setItems(listTPs);
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
            Logger.getLogger(ToimipisteetController.class.getName()).log(Level.SEVERE, null, ex);
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
        a.setTitle("Toimipisteiden hallinta");
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
            Logger.getLogger(ToimipisteetController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return controller;
   
    } 
   
}
