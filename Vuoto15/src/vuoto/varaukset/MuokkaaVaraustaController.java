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
import java.util.LinkedList;
import java.util.List;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import vuoto.aloitus.VuotoMainController;
import vuoto.asiakkuudet.AsiakkuudetController;
import vuoto.luokkafilet.Laite;
import vuoto.luokkafilet.Palvelu;
import vuoto.luokkafilet.Toimitila;
import vuoto.luokkafilet.Varaus;
import vuoto.tietokanta.DBAccess;
import vuoto.vuokrattavatKiinteistot.UusiKiinteistoController;

/**
 * FXML Controller class
 *
 * @author sauli
 */
public class MuokkaaVaraustaController implements Initializable {

    // Jos muutat fxml-tiedoston sijaintia niin muuta tähän uusi sijainti!
    /** fxml-tiedoston sijainti*/
    public static final String fxmlString = "/vuoto/varaukset/MuokkaaVarausta.fxml";
    @FXML
    private Label lblToimipisteValinta;
    @FXML
    private TextField txtToimipiste;
    @FXML
    private ComboBox<String> cbToimitilat;
    @FXML
    private ComboBox<String> cbAsiakas;
    @FXML
    private DatePicker dpLopetuspvm;
    @FXML
    private DatePicker dpAloituspvm;
    @FXML
    private VBox palvelutIkkuna;
    @FXML
    private VBox laitteetIkkuna;
    @FXML
    private TextField txtLoppusumma;
    private Varaus varaus;
    private String toimitilaNimi;
    private String asiakasNimi;
    private DBAccess tietokanta = new DBAccess();
    private ObservableList<Palvelu> palvelut = null;
    private ObservableList<Laite> laitteet = null;
    private ObservableList<String> varauksenPalvelut = null;
    private ObservableList<String> varauksenLaitteet = null;
    private List<String> valitutPalvelut = new LinkedList<>();
    private List<String> valitutLaitteet = new LinkedList<>();
    private Toimitila valittuToimitila;
    private boolean tietojaMuutettu = false;
    private boolean palveluitaMuutettu = false;
    private boolean laitteitaMuutettu = false;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        txtToimipiste.setText(VuotoMainController.valittuToimipiste);
        
    }    
    
    /**
     * Hakee palvelut valitusta toimitilasta ja luo niistä checkboxit Palvelut -ikkunaan
     */
    private void paivitaPalvelut(int varausId) {
        
        palvelut = tietokanta.haePalvelutToimitilasta(valittuToimitila);
        
        for(Palvelu p: palvelut) {
            CheckBox checkbox = new CheckBox();
            checkbox.setText(p.getKuvaus());
            
            if(palveluVarattu(checkbox)) {
                checkbox.setSelected(true);                
                valitutPalvelut.add(p.getKuvaus());
            }

            
            palvelutIkkuna.getChildren().add(checkbox);
            
            checkbox.selectedProperty().addListener((s1, s2, s3) -> {
                if(s3) {
                    valitutPalvelut.add(checkbox.getText());
                    palveluitaMuutettu = true;
                } else {
                    valitutPalvelut.remove(checkbox.getText());
                    palveluitaMuutettu = true;
                }
            });
            

            
            
            
        }
        
    }
    
    /**
     * Hakee laitteet valitusta toimitilasta ja luo niistä checkboxit Laitteet -ikkunaan
     */
    private void paivitaLaitteet(int varausId) {
        
        laitteet = tietokanta.haeLaitteetToimitilasta(valittuToimitila);
        
        for(Laite l: laitteet) {
            CheckBox checkbox = new CheckBox();
            checkbox.setText(l.getKuvaus());    
            
            if(laiteVarattu(checkbox)) {
                checkbox.setSelected(true);
                valitutLaitteet.add(l.getKuvaus());
            }
            
            laitteetIkkuna.getChildren().add(checkbox);
                   
            checkbox.selectedProperty().addListener((s1, s2, s3) -> {
                if(s3) {
                    valitutLaitteet.add(checkbox.getText());
                    laitteitaMuutettu = true;
                } else {
                    valitutLaitteet.remove(checkbox.getText());
                    laitteitaMuutettu = true;
                }
            });
        }
    }
       
    private boolean palveluVarattu(CheckBox checkbox) {

        for(String palvelu: varauksenPalvelut) {
            if(checkbox.getText().equals(palvelu)) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean laiteVarattu(CheckBox checkbox) {
  
        for(String laite: varauksenLaitteet) {
            if(checkbox.getText().equals(laite)) {
                return true;
            }
        }
        
        return false;
    }

    @FXML
    private void btnTakaisinPainettu(ActionEvent event) {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(VarauksetController.fxmlString));
        Parent root = null;
        
        try {
            root = loader.load();
        } catch (IOException ex) {
            heitaVirheNaytolle("Virhe luotaessa näkymää Varaukset.fxml");
            Logger.getLogger(AsiakkuudetController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
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
     * Asettaa muokattavan varauksen
     * @param v 
     */
    public void asetaMuokattavaVaraus(Varaus v) {
        
        if(varaus == null) {
            varaus = v;
            dpAloituspvm.setValue(v.getVuokraAlku());
            dpLopetuspvm.setValue(v.getVuokraLoppu());
            haeVarauksenPalvelut();
            haeVarauksenLaitteet();
            paivitaPalvelut(varaus.getVarausId());
            paivitaLaitteet(varaus.getVarausId());
            
        }
    }
    
    public void asetaToimitila(Toimitila t) {
        
        valittuToimitila = t;
        cbToimitilat.getSelectionModel().select(t.getTilanNimi());
    }
    
    public void asetaAsiakas(String asiakasNimi) {
        
        this.asiakasNimi = asiakasNimi;
        cbAsiakas.getSelectionModel().select(asiakasNimi);
    }
    
    public void haeVarauksenPalvelut() {
        
        varauksenPalvelut = tietokanta.haeVarauksenPalvelut(varaus.getVarausId());
        System.out.println(varauksenPalvelut);
    }
    
    public void haeVarauksenLaitteet() {
        
        varauksenLaitteet = tietokanta.haeVarauksenLaitteet(varaus.getVarausId());
        
    }

    @FXML
    private void btnPaivitaTiedotPainettu(ActionEvent event) {

        Varaus paivitettyVaraus = null;
        boolean paivitetty = false;
        
        LocalDate vuokraAlku = dpAloituspvm.getValue();
        LocalDate vuokraLoppu = dpLopetuspvm.getValue();
        int tilaId = varaus.getTilaId();
        int asiakasId = varaus.getAsiakasId();
        paivitettyVaraus = new Varaus(vuokraAlku, vuokraLoppu, tilaId, asiakasId, 0, 0); // Kaksi viimeistä nollaa palveluvarausId ja laitevarausId ja ne menee tietokantaan Null:ksi

        if(varaus.equals(paivitettyVaraus) && !palveluitaMuutettu && !laitteitaMuutettu) {
            heitaVirheNaytolle("Tietoja ei ole muutettu. Muuta ainakin yhtä tietoa päivittääksesti varaus.");
            
        } else {

            tietojaMuutettu = true;
            
            
            if(tietojaMuutettu) {
                try {
                    if(!paivitettyVaraus.equals(varaus)) {
                        tietokanta.paivitaVaraus(paivitettyVaraus, varaus.getVarausId());                 
                        paivitetty = true;
                    }
                    
                    if(palveluitaMuutettu) {
                        
                        List<Palvelu> paivitetytPalvelut = paivitaVaratutPalvelut();
                        tietokanta.paivitaVarauksenPalvelut(varaus, paivitetytPalvelut);
                        paivitetty = true;
                    }
                    
                    if(laitteitaMuutettu) {
                        List<Laite> paivitetytLaitteet = paivitaVaratutLaitteet();
                        tietokanta.paivitaVarauksenLaitteet(varaus, paivitetytLaitteet);
                        paivitetty = true;
                    }
                    
                } catch (SQLException ex) {
                    paivitetty = false;
                    Logger.getLogger(MuokkaaVaraustaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } 
            
            if(paivitetty) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Varauksen päivitys");
                a.setHeaderText("Varaus päivitetty!");
                a.showAndWait();
                
                siirryNakymaan(VarauksetController.fxmlString, "Varaukset", event);
                
                
                
            }
        }
    }
    
    public List paivitaVaratutPalvelut() {
        
        LinkedList<Palvelu> paivitettyLista = null; 
        paivitettyLista = new LinkedList<>();
        
        for(Palvelu p: palvelut) {
            for(String valittuPalvelu: valitutPalvelut) {
                if(valittuPalvelu.equals(p.getKuvaus())) {
                    paivitettyLista.add(p);
                }
            }
        }
        
        return paivitettyLista;
  
    }
    
    public List paivitaVaratutLaitteet() {
        
        LinkedList<Laite> paivitettyLista = null; 
        paivitettyLista = new LinkedList<>();
        
        for(Laite l: laitteet) {
            for(String valittuLaite: valitutLaitteet) {
                if(valittuLaite.equals(l.getKuvaus())) {
                    paivitettyLista.add(l);
                }
            }
        }
        
        return paivitettyLista;
    }
    
    /**
     * Muuttaa ikkunan näkymäksi valitun näkymän
     * @param fxml fxml-tiedoston nimi
     * @param title Ikkunan otsikko
     * @param event Lähde mistä valinta tuli
     */
    private void siirryNakymaan(String fxml, String title, ActionEvent event) {
        
        Object controller = null; // Palautettava controller olio
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
            Parent vuoto = (Parent) fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(vuoto));
        } catch (IOException ex) {
            heitaVirheNaytolle("Virhe luotaessa näkymää " + title);
            Logger.getLogger(MuokkaaVaraustaController.class.getName()).log(Level.SEVERE, null, ex);
        }

   
    } 

 
}
