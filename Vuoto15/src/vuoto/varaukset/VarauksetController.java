/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.varaukset;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import vuoto.aloitus.VuotoMainController;
import static vuoto.aloitus.VuotoMainController.valittuToimipiste;
import vuoto.asiakkuudet.AsiakkuudetController;
import vuoto.laskutus.LaskutusController;
import vuoto.luokkafilet.Toimitila;
import vuoto.luokkafilet.Varaus;
import vuoto.luokkafilet.VarausOlio;
import vuoto.tietokanta.DBAccess;

/**
 * FXML Controller class
 *
 * @author marko
 */
public class VarauksetController implements Initializable {

    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/varaukset/Varaukset.fxml";
    @FXML
    private Label lblToimipisteValinta;
    @FXML
    private TextField txtToimipiste;
    @FXML
    private DatePicker dpAlkupaiva;
    @FXML
    private DatePicker dpLoppupaiva;
    @FXML
    private VBox palvelutIkkuna;
    @FXML
    private VBox laitteetIkkuna;
    private DBAccess tietokanta = new DBAccess();
    private Toimitila valittuToimitila;
    @FXML
    private ComboBox<Toimitila> cbToimitilavalikko;
    @FXML
    private TableColumn<VarausOlio, Integer> colVarausId;
    @FXML
    private TableColumn<VarausOlio, LocalDate> colVarausalku;
    @FXML
    private TableColumn<VarausOlio, LocalDate> colVarausloppu;
    @FXML
    private TableColumn<VarausOlio, String> colAsiakas;
    @FXML
    private TableColumn<VarausOlio, String> colToimitila;
    @FXML
    private TableColumn<VarausOlio, String> colToimipiste;
    @FXML
    private TableView<VarausOlio> tbvVaraukset;
    private boolean toimipisteessaOnToimitiloja = false;
    private ObservableList<VarausOlio> varaukset = null;
    private VarausOlio varausOlio;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        

        // Aktiivinen toimipiste (eli yksi keskuksista)
        txtToimipiste.setFocusTraversable(false);
        txtToimipiste.setText(valittuToimipiste);
        paivitaToimitilavalikko();
        
        if (txtToimipiste.getText().equals("Kaikki toimipisteet")) {
            
            cbToimitilavalikko.getSelectionModel().clearSelection();
            valittuToimitila = null;
        }
        
        paivitaTableview();
        
        // Kuuntelee toimitilan valintaa, asettaa valitun toimitilan ja päivittää Palvelut ja Laitteet ikkunat
        cbToimitilavalikko.getSelectionModel().selectedItemProperty().addListener((s1, s2, s3) -> {
            
            if (s3 != s2) {
                valittuToimitila = s3;

                paivitaTableview();
            }
        });
        
        // Kuuntelee Varaukset tableviewin valintaa
        // Tyhjentää checkbox ikkunat ja täyttää uusilla kun toinen varaus valitaan
        tbvVaraukset.getSelectionModel().selectedItemProperty().addListener((s1, s2, s3) -> {
            palvelutIkkuna.getChildren().clear();
            laitteetIkkuna.getChildren().clear();
            
            // Tietojen poistamisen jälkeen uusin valinta on null. Tällä estetään se että yritettäisiin hakea palveluita ja laitteita joiden varaus on null       
            if (s3 != null) {
                paivitaPalvelut(s3.getVarausId());
                paivitaLaitteet(s3.getVarausId());
                varausOlio = s3;
            }
        });
        
    }    
    
    /**
     * Hakee palvelut valitusta toimitilasta ja luo niistä checkboxit Palvelut -ikkunaan
     */
    private void paivitaPalvelut(int varausId) {
        
        ObservableList<String> palvelut = tietokanta.haeVarauksenPalvelut(varausId);
        
        
        for(String p: palvelut) {
            CheckBox checkbox = new CheckBox();
            checkbox.setText(p);
            palvelutIkkuna.getChildren().add(checkbox);
            checkbox.setSelected(true);
            checkbox.setDisable(true);
            checkbox.setStyle("-fx-opacity: 1"); // palvelutIkkuna on scenebuilderin päästä asetettu 'disabled'. Tämä palauttaa checkboxin tavallisen näköiseksi

        }
        
    }
    
    /**
     * Hakee laitteet valitusta toimitilasta ja luo niistä checkboxit Laitteet -ikkunaan
     */
    private void paivitaLaitteet(int varausId) {
        
        ObservableList<String> laitteet = tietokanta.haeVarauksenLaitteet(varausId);
        
        for(String l: laitteet) {
            CheckBox checkbox = new CheckBox();
            checkbox.setText(l);
            laitteetIkkuna.getChildren().add(checkbox);
            checkbox.setSelected(true);
            checkbox.setDisable(true);
            checkbox.setStyle("-fx-opacity: 1"); // laitteetIkkuna on scenebuilderin päästä asetettu 'disabled'. Tämä palauttaa checkboxin tavallisen näköiseksi
        }
    }

    /**
     * Päivittää toimitilat comboboxiin
     */
    private void paivitaToimitilavalikko() {
        
        ObservableList<Toimitila> toimitilat = null;
                
        if (txtToimipiste.getText().equals("Kaikki toimipisteet")) {

            toimitilat = tietokanta.haeKaikkiToimitilat();
            cbToimitilavalikko.setItems(toimitilat);
        } else {
            toimitilat = tietokanta.haeToimitilatToimipisteesta(valittuToimipiste);
            cbToimitilavalikko.setItems(toimitilat);
            if(toimitilat.size() > 0) {
                toimipisteessaOnToimitiloja = true;
                cbToimitilavalikko.getSelectionModel().selectFirst();
            }
        }
        
        
        if(valittuToimitila == null) {
            valittuToimitila = cbToimitilavalikko.getSelectionModel().getSelectedItem();
        }
    }
    
    /**
     * Päivittää näkymän tableviewn
     */
    public void paivitaTableview() {
        //ObservableList<Varaus> varaukset = tietokanta.haeKaikkiVaraukset();
      
        //varaukset = tietokanta.haeKaikkiVaraukset();
        
        // Jos valikosta ei ole valittu toimitilaa..
        if (valittuToimitila != null) {
            tbvVaraukset.getItems().clear();
            varaukset = tietokanta.haeTiedotVarausIkkunaan(valittuToimitila.getTilanNimi());

        // Jos toimitila on valittu..
        } else {
            
            varaukset = tietokanta.haeKaikkiVarauksetVarausIkkunaan();
        
        }
        
        colVarausId.setCellValueFactory(new PropertyValueFactory<>("varausId"));
        colVarausalku.setCellValueFactory(new PropertyValueFactory<>("alkupaiva"));
        colVarausloppu.setCellValueFactory(new PropertyValueFactory<>("loppupaiva"));
        colAsiakas.setCellValueFactory(new PropertyValueFactory<>("asiakas"));
        colToimitila.setCellValueFactory(new PropertyValueFactory<>("toimitila"));
        colToimipiste.setCellValueFactory(new PropertyValueFactory<>("toimipiste"));

        tbvVaraukset.setItems(varaukset);

    }
    
    /**
     * Heittää virheilmoituksen näytölle
     * @param virhe Virheilmoitus
     */
    private void heitaVirheNaytolle(String virhe) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Varaukset");
        a.setHeaderText(virhe);
        a.showAndWait();
    }

    /**
     * Palauttaa näkymän etusivulle
     * @param event "Etusivulle" -napin painallus
     */
    @FXML
    private void btnEtusivullePainettu(ActionEvent event) {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(VuotoMainController.fxmlString));
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
     * Vaihtaa näkymäksi uuden varauksen luomisen
     * @param event 
     */
    @FXML
    private void btnUusiVarausPainettu(ActionEvent event) {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(UusiVarausController.fxmlString));
        Parent root = null;
        
        try {
            root = loader.load();
        } catch (IOException ex) {
            heitaVirheNaytolle("Virhe luotaessa näkymää UusiVaraus.fxml");
            Logger.getLogger(AsiakkuudetController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    /**
     * Poistaa valitun varauksen
     * @param event "Poista" -napin painallus
     */
    @FXML
    private void btnPoistaVarausPainettu(ActionEvent event) {
        
        boolean varausPoistettu = true;

        if (varausOlio == null) {

            heitaVirheNaytolle("Valitse poistettava varaus");

        } else {
            boolean okPainettu = heitaVahvistusNaytolle("Poistetaanko varaus " + varausOlio.getVarausId() + "?", "Palvelun poistaminen");

            if (okPainettu) {
                try {
                    tietokanta.poistaVaraus(varausOlio.getVarausId());
                } catch (SQLException ex) {
                    varausPoistettu = false;
                    heitaVirheNaytolle("Virhe poistettaessa varausta");
                    Logger.getLogger(VarauksetController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (varausPoistettu) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Varauksen poistaminen");
                a.setHeaderText("Varaus poistettu");
                a.showAndWait();
                paivitaTableview();
            }
        }
    }

    /**
     * Vaihtaa näkymäksi varauksen muokkaamisen näkymän
     * @param event "Muokkaa" -napin painallus
     */
    @FXML
    private void btnMuokkaaVaraustaPainettu(ActionEvent event) {
        
        if (varausOlio != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(MuokkaaVaraustaController.fxmlString));
            Parent root = null;

            try {
                root = loader.load();
            } catch (IOException ex) {
                heitaVirheNaytolle("Virhe luotaessa näkymää MuokkaaVarausta.fxml");
                Logger.getLogger(AsiakkuudetController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Varaus muokattavaVaraus = tietokanta.haeVarausIdNumerolla(varausOlio.getVarausId());
            
            MuokkaaVaraustaController controller = loader.getController();
            controller.asetaToimitila(valittuToimitila);
            controller.asetaMuokattavaVaraus(muokattavaVaraus);
            controller.asetaAsiakas(varausOlio.getAsiakas());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } else {
            heitaVirheNaytolle("Valitse varaus");
        }
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

    /**
     * Hakee varauksen valitulta ajalta ja lisää ne tableviewiin
     * @param event "Hae ajalta" -napin painallus
     */
    @FXML
    private void btnHaeAjaltaPainettu(ActionEvent event) {
            
        if(varaukset.isEmpty()) {
            varaukset = tietokanta.haeTiedotVarausIkkunaan(valittuToimitila.getTilanNimi());
        }
        
        ObservableList<VarausOlio> varauksetAikavalilta = FXCollections.observableArrayList();
        
        for(VarausOlio varaus: varaukset) {
            if(varaus.getAlkupaiva().isAfter(dpAlkupaiva.getValue().minusDays(1)) && varaus.getLoppupaiva().isBefore(dpLoppupaiva.getValue().plusDays(1))) {
                varauksetAikavalilta.add(varaus);
            }
        }
        
        if(varaukset != null) {
            tbvVaraukset.getItems().clear();
            tbvVaraukset.setItems(varauksetAikavalilta);
        }
    }

    /**
     * Nollaa aikavälin valinnan ja palauttaa kaikki tiedot tableviewiin
     * @param event "Palauta" -napin painallus
     */
    @FXML
    private void btnPalautaPainettu(ActionEvent event) {
        
        tbvVaraukset.getItems().clear();
        dpAlkupaiva.setValue(null);
        dpLoppupaiva.setValue(null);
        paivitaTableview();
    }
   
    
}


