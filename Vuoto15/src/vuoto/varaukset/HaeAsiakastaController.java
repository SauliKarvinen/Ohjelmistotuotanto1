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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import vuoto.luokkafilet.Asiakas;
import vuoto.tietokanta.DBAccess;

/**
 * FXML Controller class
 *
 * @author sauli
 */
public class HaeAsiakastaController implements Initializable {

    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/varaukset/HaeAsiakasta.fxml";
    @FXML
    private TextField txtToimipiste;
    @FXML
    private Label lblToimipisteValinta;
    @FXML
    private Button btnPoistaLasku;
    @FXML
    private TableView<Asiakas> tbvAsiakkaat;
    private DBAccess tietokanta = new DBAccess();
    @FXML
    private TableColumn<Asiakas, Integer> colAsiakasId;
    @FXML
    private TableColumn<Asiakas, String> colYritys;
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
    private UusiVarausController uusivarauscontroller;
    private Asiakas asiakas;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        paivitaAsiakasTableview();
        
        // Kuunnellaan tableviewta asiakkaan valinnasta
        tbvAsiakkaat.getSelectionModel().selectedItemProperty().addListener((s1, s2, s3) -> {
            asiakas = s3;
        });
    }    
    
    /**
     * Päivittää asiakkaat tableviewn
     */
    public void paivitaAsiakasTableview() {
        
        ObservableList<Asiakas> asiakkaat = tietokanta.haeKaikkiAsiakkaat();
        
        colAsiakasId.setMinWidth(100);
        colAsiakasId.setCellValueFactory(new PropertyValueFactory<>("asiakasId"));
        colYritys.setMinWidth(100);
        colYritys.setCellValueFactory(new PropertyValueFactory<>("yrtiys"));
        colEtunimi.setMinWidth(100);
        colEtunimi.setCellValueFactory(new PropertyValueFactory<>("etunimi"));
        colSukunimi.setMinWidth(100);
        colSukunimi.setCellValueFactory(new PropertyValueFactory<>("sukunimi"));
        colLahiosoite.setMinWidth(100);
        colLahiosoite.setCellValueFactory(new PropertyValueFactory<>("lahiosoite"));
        colPostinumero.setMinWidth(100);
        colPostinumero.setCellValueFactory(new PropertyValueFactory<>("postinumero"));
        colPuhelinnumero.setMinWidth(100);
        colPuhelinnumero.setCellValueFactory(new PropertyValueFactory<>("puhelinnumero"));
        colSahkoposti.setMinWidth(100);
        colSahkoposti.setCellValueFactory(new PropertyValueFactory<>("sahkoposti"));
        
        tbvAsiakkaat.setItems(asiakkaat);
    }

    /**
     * Palauttaa näkymän takaisin edelliseen
     * @param event 
     */
    @FXML
    private void btnTakaisinPainettu(ActionEvent event) {
        
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Valitsee listasta valitun asiakkaan ja lähettää sen UusiVaraus näkymään
     * @param event 
     */
    @FXML
    private void btnLisaaPainettu(ActionEvent event) {
        
        uusivarauscontroller.asetaAsiakas(asiakas);
        
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
        
    }

    // Määrittää UusiVaraus näkymän kontrollerin
    public void setUusiVarausController(UusiVarausController controller) {
        
        if(controller != null) {
            uusivarauscontroller = controller;
        }
    }
    
   
    
}
