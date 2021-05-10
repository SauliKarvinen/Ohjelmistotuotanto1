/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.laskutus;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import static javax.management.Query.value;
import static vuoto.aloitus.VuotoMainController.valittuToimipiste;
import vuoto.luokkafilet.Lasku;
import vuoto.luokkafilet.Toimipiste;
import vuoto.luokkafilet.Asiakas;
import vuoto.luokkafilet.Toimitila;
import vuoto.luokkafilet.Varaus;
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
    @FXML
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
            paivitaAsiakasKentat();
        });
        
        
        
        // TABLEVIEW VARAUKSET aktivointi
        try {
            // Aktivoi TblView
            populateTableViewVaraukset();
        } catch (SQLException ex) {
            heitaVirheNaytolle("TableView:n aktivoinnissa virhe (Toimipisteet).");
            Logger.getLogger(UusiLaskuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    private void btnPoistaLaskuPainettu(ActionEvent event) {
        // Open panel - PoistaLasku
        PoistaLaskuController controller = (PoistaLaskuController) siirryNakymaan("PoistaLasku.fxml", "Poista Lasku", event);
        //controller.asetaToimipiste(toimipiste);
    }

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
        TaytettyLaskuController controller = (TaytettyLaskuController) siirryNakymaan("TaytettyLaskuPohja.fxml", "Laskutuksen hallinta", event);
    }
    
    public void asetaToimipiste(Toimipiste toimipiste) {
        
        if (toimipiste != null) {
            this.toimipiste = toimipiste;
            txtToimipiste.setText(toimipiste.getToimipistenimi());
        }
    }
 
    /*** TABLEVIEW **/   
    private ObservableList<Varaus> listVaraukset;
    
    @FXML
    private TableView<Varaus> tblVaraukset;
    @FXML
    private TableColumn<Varaus, String> colVarausId;
    @FXML
    private TableColumn<Varaus, String> colAsiakas;
    @FXML
    private TableColumn<Varaus, String> colVuokrattava;
    @FXML
    private TableColumn<Varaus, String> colVarausAlku;
    @FXML
    private TableColumn<Varaus, String> colVarausLoppu;
    @FXML
    private TableColumn<Varaus, String> colPalvelut;
    @FXML
    private TableColumn<Varaus, String> colLaitteet;
    
    
    
   /**
     * Method to populate TableView: Varaukset
     * Käytetään Varauksen (DBAccess) metodia, 
     *  haeKaikkiVaraukset() 
     *  Luetaan -> observableArrayList(listVaraus)
     *  Alustetaan -> setCellValueFactory määritykset sarakkeille.
     * 
     * */
    private void populateTableViewVaraukset() throws SQLException {
        // alustetaan lista
        listVaraukset = FXCollections.observableArrayList();
       
        // Haetaan asiakkaan Varaukset (Ei vielä, nyt KAIKKI varaukset)
        listVaraukset = tietokanta.haeKaikkiVaraukset();
        
        // set propertyTab to TableView
       colVarausId.setCellValueFactory(new PropertyValueFactory<>("varausId"));
       colVarausAlku.setCellValueFactory(new PropertyValueFactory<>("vuokraAlku"));
       colVarausLoppu.setCellValueFactory(new PropertyValueFactory<>("vuokraLoppu"));
       colVuokrattava.setCellValueFactory(new PropertyValueFactory<>("tilaId"));
       colAsiakas.setCellValueFactory(new PropertyValueFactory<>("asiakasId"));
       colPalvelut.setCellValueFactory(new PropertyValueFactory<>("palveluvarausId"));
       colLaitteet.setCellValueFactory(new PropertyValueFactory<>("laitevarausId"));

       tblVaraukset.setItems(listVaraukset);
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
        ObservableList<Varaus> varaukset = FXCollections.observableArrayList();
        List<Integer> listVaraukset = new ArrayList<Integer>();
        
    // Palauttaa vain ID:t
        // varaukset = tietokanta.haeAsiakkaanVaraukset(asiakkaanID);
        
    // String versio: hakee id:llä varukset, ja palauttaa nimet:
        String tilat = tietokanta.haeAsiakkaanToimitilaVaraukset(valittuAsiakas);
        
        for(Varaus v: varaukset){
            // System.out.println(v.getVarausId());
            // System.out.println("Varauksissa");
            listVaraukset.add(v.getTilaId());
            }
        
        /* Toimi, mutta vain ID:n palautus
            for(Varaus v: listVaraukset){
            ttilat.add(v.getTilaId());
            tpalvelut.add(v.getPalveluvarausId());
            tlaitteet.add(v.getLaitevarausId());
            }   
        */    
                
        txfVuokKiinteisto.setText(String.valueOf(tilat));
        
        // txfVuokKiinteisto.setText(String.valueOf(listVaraukset));
        txfPalvelut.setText("Vuokrattavan kiinteistön palvelut.");
        
        String palvelut = tietokanta.haeAsiakkaanPalvelut(valittuAsiakas);
        
        txfLaitteet.setText("Vuokrattavan kiinteistön laitteet.");
        
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
    
    /**  // KESKEN
     * Asiakkaan vuokraamat tilat
     * @param a asiakasId
     * @return tilat String, lista tiloista
     * 
     */
    private String haeVuokratutTilat(int a){
       ObservableList<Varaus> kaikki = FXCollections.observableArrayList();
       ObservableList<Varaus> vainIDt = FXCollections.observableArrayList();

       kaikki = tietokanta.haeKaikkiVaraukset();
        
       for (Varaus va: kaikki){
           // TODO
           // vainIDt.add(va.getTilaId());            
       }
       String tmp = ""; 
       return tmp; 
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
