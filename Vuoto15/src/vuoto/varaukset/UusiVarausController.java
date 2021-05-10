/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.varaukset;

import vuoto.palvelutJaLaitteet.PalvelutJaLaitteetController;
import vuoto.aloitus.VuotoMainController;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import vuoto.asiakkuudet.LisaaUusiAsiakasController;
import vuoto.luokkafilet.Asiakas;
import vuoto.luokkafilet.Laite;
import vuoto.luokkafilet.Palvelu;
import vuoto.luokkafilet.Toimipiste;
import vuoto.luokkafilet.Toimitila;
import vuoto.luokkafilet.VaratutPaivat;
import vuoto.luokkafilet.Varaus;
import vuoto.tietokanta.DBAccess;
import vuoto.vuokrattavatKiinteistot.UusiKiinteistoController;

/**
 * FXML Controller class
 *
 * @author sauli
 */
public class UusiVarausController implements Initializable {

    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/varaukset/UusiVaraus.fxml";
    @FXML
    private Label lblToimipisteValinta;
    @FXML
    private TextField txtToimipiste;
    @FXML
    private VBox palvelutIkkuna;
    @FXML
    private VBox laitteetIkkuna;
    @FXML
    private TextField txtLoppusumma;
    @FXML
    private Button btnTakaisin;
    @FXML
    private DatePicker dpLopetuspvm;
    @FXML
    private DatePicker dpAloituspvm;
    private DBAccess tietokanta = new DBAccess();
    Toimipiste toimipiste = null;
    @FXML
    private TextField txtToimitila;
    @FXML
    private Button btnLisaaToimitila;
    @FXML
    private TextField txtAsiakas;
    private Toimitila valittuToimitila;
    private Asiakas valittuAsiakas;
    private Palvelu palvelu;
    private Laite laite;
    private Varaus varaus;
    private List<Palvelu> valitutPalvelut = new LinkedList<>();
    private List<Laite> valitutLaitteet = new LinkedList<>();
    private List<String> palveluCheckboxit = new LinkedList<>();
    private List<String> laiteCheckboxit = new LinkedList<>();
    private ObservableList<Palvelu> palvelut = null;
    private ObservableList<Laite> laitteet = null;
//    private List<Palvelu> palvelut = new LinkedList<Palvelu>();
//    private List<Laite> laitteet = new LinkedList<Laite>();
   // private Map<Integer, LinkedHashSet<Palvelu>> palvelutMap = new HashMap<>();


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        maaritaToimipiste(); // Tämä metodi tossa heti alapuolella
        
        // Kun toimitilaa muutetaan, suoritetaan listenerin sisällä olevat asiat
        txtToimitila.textProperty().addListener((s1, s2, s3) -> {
            
            if (s3 != s2) {
                palvelutIkkuna.getChildren().clear(); // Poistaa palveluiden checkboxit ikkunasta
                laitteetIkkuna.getChildren().clear(); // Poistaa laitteiden checkboxit ikkunasta
                valitutLaitteet.clear(); // Tyhjentää valittujen laitteiden listan
                valitutPalvelut.clear(); // Tyhjentää valittujen palveluiden listan
                paivitaPalvelut(); // Päivittää uudet palvelut
                paivitaLaitteet(); // Päivittää uudet laitteet
                haeVaratutPaivat(); // Lisää varatut päivät kalenteriin
            }
        });
    }    

    /**
     * Hakee palvelut valitusta toimitilasta ja luo niistä checkboxit Palvelut -ikkunaan
     */
    private void paivitaPalvelut() {
        
        //Jos palvelut -listalla on dataa, lista tyhjennetään kun halutaan päivittää uudet tiedot
        if(palvelut != null) {
            palvelut.clear();
        }
        palvelut = tietokanta.haePalvelutToimitilasta(valittuToimitila);
        
        // Lisää checkboxeja ikkunaan jokaiselle laitteelle
        for(Palvelu p: palvelut) {
            CheckBox checkbox = new CheckBox();
            checkbox.setText(p.getKuvaus());
            palvelutIkkuna.getChildren().add(checkbox);
            
            // Pitää kirjaa valituista palveluista ja laitteista
            // Lisää checkboxille listenerin. Jos checkbox valitaan, sisältö lisätään listaan
            // Jos checkboxia painetaan uudestaan, sisältö poistuu listasta
            checkbox.selectedProperty().addListener((s1, s2, s3) -> {
                
                if(s3) {
                    palveluCheckboxit.add(checkbox.getText());

                } else {
                    palveluCheckboxit.remove(checkbox.getText());

                }
            });
        }
    }
    
    /**
     * Hakee laitteet valitusta toimitilasta ja luo niistä checkboxit Laitteet -ikkunaan
     */
    private void paivitaLaitteet() {
        
        //Jos palvelut -listalla on dataa, lista tyhjennetään kun halutaan päivittää uudet tiedot
        if(laitteet != null) {
            laitteet.clear();
        }
        laitteet = tietokanta.haeLaitteetToimitilasta(valittuToimitila);
        
        // Lisää checkboxeja ikkunaan jokaiselle laitteelle
        for(Laite l: laitteet) {
            CheckBox checkbox = new CheckBox();
            checkbox.setText(l.getKuvaus());
            laitteetIkkuna.getChildren().add(checkbox);
            
            // Pitää kirjaa valituista palveluista ja laitteista
            // Lisää checkboxille listenerin. Jos checkbox valitaan, sisältö lisätään listaan
            // Jos checkboxia painetaan uudestaan, sisältö poistuu listasta
            checkbox.selectedProperty().addListener((s1, s2, s3) -> {
                
                if(s3) {
                    laiteCheckboxit.add(checkbox.getText());
                } else {
                    laiteCheckboxit.remove(checkbox.getText());
                }
            });
        }
    }
    
    /**
     * Määrittää toimipisteen jonka tietoja haetaan
     * Määrittää Toimipiste -luokan olion ja asettaa toimipisteen nimen txtToimipiste kenttään
     */
    private void maaritaToimipiste() {
        
        // Aloitusvalikossa valittu toimipisteen nimi
        String valikostaValittu = VuotoMainController.valittuToimipiste;
        
        // Estetään ettei mene tietokantaan hakua WHERE toimipisteNimi = 'Kaikki toimipisteet'
        if(valikostaValittu.equals("Kaikki toimipisteet")) {
            // Käsitellään kaikkia toimipisteitä
            
        } else {        
            toimipiste = tietokanta.haeToimipisteNimella(valikostaValittu); // tietokannan haeToimipisteNimella(nimi) palauttaa toimipiste -olion. Nimenä se aloitusikkunassa valittu toimipisteen nimi.
        }
        
        txtToimipiste.setText(valikostaValittu); // Asetetaan näkymän toimipiste -tekstikenttään aloitusnäkymässä valittu valinta
        txtToimipiste.setFocusTraversable(false); // Tää ohjelma hölmösti "kohdistaa" näkymän tohon tekstikenttään ja mustamaalaa sen tekstin kun siihen lisätään tässä sisältöä. Tällä saa sen pois.
        
    }

    /**
     * Palauttaa näkymän edelliseen näkymään
     * @param event "Takaisin" -napin painallus
     */
    @FXML
    private void btnTakaisinPainettu(ActionEvent event) {
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(VarauksetController.fxmlString));
            Parent vuoto = (Parent) fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();       
            stage.setTitle("Varaukset");
            stage.setScene(new Scene(vuoto));
            stage.show();
        } catch (IOException ex) {
            heitaVirheNaytolle("Virhe luotaessa näkymää Varaukset");
            Logger.getLogger(VuotoMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Heittää virheilmoituksen näytölle
     * @param virhe Virheilmoitus
     */
    private void heitaVirheNaytolle(String virhe) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Palvelut ja Laitteet");
        a.setHeaderText(virhe);
        //a = muotoileIlmoitus(a);
        a.showAndWait();
    }
    
    /**
     * Muotoilee ilmoitusta omilla css-määrityksillä
     * @param a Muokattava ikkuna
     * @return Muokattu ikkuna
     */
    public Alert muotoileIlmoitus(Alert a) {
        
        String alert_css = getClass().getResource("/stylesheets/sauli_alert.css").toExternalForm();
        DialogPane dialog = a.getDialogPane();
        dialog.getStylesheets().add(alert_css);
        dialog.getStyleClass().add("alert");
        ((Button)a.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Takaisin");
        
        return a;
    }

    /**
     * Avaa uuden ikkunan Toimitilan valintaa varten
     * @param event "Hae toimitilaa" -napin painallus
     */
    @FXML
    private void btnHaeToimitilaaPainettu(ActionEvent event) {
        
        HaeToimitilaaController controller = (HaeToimitilaaController) avaaUusiIkkuna(HaeToimitilaaController.fxmlString, "Hae toimitila");
        controller.setUusiVarausController(this);
    }

    /**
     * Avaa uuden ikkunan Asiakkaan valintaa varten
     * @param event "Hae asiakasta" -napin painallus
     */
    @FXML
    private void btnHaeAsiakastaPainettu(ActionEvent event) {
        
        HaeAsiakastaController controller = (HaeAsiakastaController) avaaUusiIkkuna(HaeAsiakastaController.fxmlString, "Hae asiakas");
        controller.setUusiVarausController(this);
    }

    // TÄMÄ OMINAISUUS LISÄTÄÄN JOS AIKAA JÄÄ
    @FXML
    private void btnLisaaUusiAsiakasPainettu(ActionEvent event) {
        
        avaaUusiIkkuna(LisaaUusiAsiakasController.fxmlString, "Lisää asiakas");
    }
    
    private Object avaaUusiIkkuna(String fxml, String title) {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));  
        Parent root = null;
        Object controller = null;
        
        try {
            root = loader.load();
        } catch (IOException ex) {
            heitaVirheNaytolle("Virhe luotaessa näkymää '" + title + "'");
            Logger.getLogger(PalvelutJaLaitteetController.class.getName()).log(Level.SEVERE, null, ex);
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

    // TÄMÄ OMINAISUUS LISÄTÄÄN JOS AIKAA JÄÄ
    @FXML
    private void btnLisaaToimitilaPainettu(ActionEvent event) {
        
        UusiKiinteistoController controller = (UusiKiinteistoController) avaaUusiIkkuna(UusiKiinteistoController.fxmlString, "Lisaa Toimitila");
    }
    
    /**
     * Asettaa varauksen toimitilan
     * @param t Toimitila
     */
    public void asetaToimitila(Toimitila t) {
        if(t != null) {
            valittuToimitila = t;
            txtToimitila.setText(t.getTilanNimi());
        }
    }
    
    /**
     * Asettaa varauksen asiakkaan
     * @param a Asiakas
     */
    public void asetaAsiakas(Asiakas a) {
        if(a != null) {
            valittuAsiakas = a;
            txtAsiakas.setText(a.getEtunimi() + " " + a.getSukunimi());
        }
    }

    /**
     * Lisää uuden varauksen
     * @param event "Luo varaus" -napin painallus
     */
    @FXML
    private void btnLisaaVarausPainettu(ActionEvent event) {
        
        varaus = new Varaus();
        int palveluvarausId = tietokanta.haeVarauksenPalvelutId() + 1;
        int laitevarausId = tietokanta.haeVarauksenLaitteetId() + 1;
          
        
        
        if(valittuAsiakas != null) {
            varaus.setAsiakasId(valittuAsiakas.getAsiakasId());
            varaus.setTilaId(valittuToimitila.getTilaId());
        }
        
        varaus.setVuokraAlku(dpAloituspvm.getValue());
        varaus.setVuokraLoppu(dpLopetuspvm.getValue());

        int lisattyVarausId = 0;
        
        // Varmistaa että kaikki kentät on täytetty
        if (txtToimitila.getText().isEmpty() || txtAsiakas.getText().isEmpty() || dpAloituspvm.getValue() == null || dpLopetuspvm.getValue() == null) {
            
            heitaVirheNaytolle("Täytä kaikki kentät!");
            
        } else {
            tietokanta.lisaaVaraus(varaus);
            lisattyVarausId = tietokanta.haeLisattyVarausId(varaus);

            if (palveluCheckboxit.size() > 0) {
                varaus.setPalveluvarausId(palveluvarausId);
                lisaaPalvelut(lisattyVarausId);
            }

            if (laiteCheckboxit.size() > 0) {
                varaus.setLaitevarausId(laitevarausId);
                lisaaLaitteet(lisattyVarausId);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Uusi varaus");
            alert.setHeaderText("Varaus lisätty");
            alert.showAndWait();
            System.out.println(varaus);
        }
    }
    
    /**
     * Lisää valitut palvelut varaukselle tietokantaan
     * @param varausId Varauksen id
     */
    public void lisaaPalvelut(int varausId) {
         
          for(String p: palveluCheckboxit) {
              for(Palvelu lisattava: palvelut) {
                  if(lisattava.getKuvaus().equals(p)) {            
                      tietokanta.yhdista();
                      tietokanta.lisaaVarauksenPalvelut(lisattava, varausId);
                      tietokanta.katkaiseYhteys();
                  }
              }
          } 
    }
    
    /**
     * Lisää valitut laitteet varaukselle tietokantaan
     * @param varausId Varauksen id
     */
    public void lisaaLaitteet(int varausId) {

          for(String l: laiteCheckboxit) {
              
              for(Laite lisattava: laitteet) {
                  if(lisattava.getKuvaus().equals(l)) {
                      tietokanta.yhdista();
                      tietokanta.lisaaVarauksenLaitteet(lisattava, varausId);
                      tietokanta.katkaiseYhteys();
                  }
              }
          } 
    }
    
    /**
     * Haetaan varatut päivämäärät toimitilasta ja heitetään lista varaaPaivatKalenterista() metodille
     */
    public void haeVaratutPaivat() {
        
        LinkedList<VaratutPaivat> varatutpaivat = tietokanta.haeVaratutPaivatToimitilasta(valittuToimitila.getTilaId());
        varaaPaivatKalenterista(varatutpaivat);

    }
    
    /**
     * Varaa päivät kalenterista ettei niitä voi valita
     * @param list Lista varatuista päivistä
     */
    public void varaaPaivatKalenterista(LinkedList<VaratutPaivat> list) {
        
        // Datepickerin "solut"
        final Callback<DatePicker, DateCell> dayCellFactory = 
                // Palauttaa päivitetyt solut
                new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate kalenterinPaiva, boolean empty) {
                        super.updateItem(kalenterinPaiva, empty);
                        
                        // Listassa on päivämääräpareja. Kalenterista varataan päivät jotka ovat näillä väleillä.
                        // Myös nykyistä päivämäärää edeltävät päivät varataan kalenterista ettei niitä voi varata
                        for(VaratutPaivat d : list) {
                            if(kalenterinPaiva.isAfter(d.getAloitusPvm().minusDays(1)) && kalenterinPaiva.isBefore(d.getLoppuPvm().plusDays(1)) || kalenterinPaiva.isBefore(LocalDate.now())) {
                                setDisable(true);
                                setStyle("-fx-background-color:#ff3333;"); // Vähän näkyvämpi väri kuin tuo perus 'disabled'
                                
                            }
                        }
                    }
                };
            }
                    
                };
        
        // Päivitetään näkymän datepickerit 
        dpAloituspvm.setDayCellFactory(dayCellFactory);
        dpLopetuspvm.setDayCellFactory(dayCellFactory);
        
    }
    
}



