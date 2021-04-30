/*******************************************************************************
* 
* VUOTO 
* OHJELMISTOTUOTANTO 1
* R19
* VERSIONUMERO: v1.4.1 (päivitetty 29.4.2021)
* Korvaa version v1.4!
* 
********************************************************************************/
package vuoto.aloitus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author marko
 */
public class Vuoto extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(VuotoMainController.fxmlString));
        
        Scene scene = new Scene(root);
        stage.setTitle("VuoTo - VuokraToimistot");
        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
              
        // Saulin kommentti
        
    }
    
}
