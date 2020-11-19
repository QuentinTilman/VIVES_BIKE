/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import databag.Fiets;
import databag.Lid;
import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.controller.FietsenBeheerController;
import ui.controller.StartschermController;
import ui.controller.LedenBeheerController;
import ui.controller.RitBeheerController;
import ui.controller.RitControleController;
import ui.controller.SelectFietsController;
import ui.controller.SelectLidController;

/**
 *
 * @author Katrien.Deleu
 * 
 */
public class VIVESbike extends Application {

    private final Stage stage = new Stage();

    @Override
    public void start(Stage primaryStage) throws Exception {
        showstartscherm();
        stage.show();
    }

    public void showstartscherm() {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("view/Startscherm.fxml"));
            Parent root = loader.load();
            StartschermController startscherm = (StartschermController) loader.getController();
            startscherm.setParent(this);

            Scene scene = new Scene(root);
            stage.setTitle("VIVESBike - Administratie");
            stage.setScene(scene);

        } catch (IOException e) {
            System.out.println("SYSTEEMFOUT bij laden startscherm: " + e.getMessage());

        }
    }
    
    public void laadLedenbeheer() {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(
                    "view/LedenBeheer.fxml"));

            // controller instellen
            Parent root = loader.load();
            LedenBeheerController controller = (LedenBeheerController) loader.getController();

            // referentie naar hier bewaren in de controller
            controller.setParent(this);

            Scene scene = new Scene(root);
            stage.setTitle("Leden beheren");
            stage.setScene(scene);

        } catch (IOException e) {
            System.out.println("SYSTEEMFOUT bij laden ledenbeheer: " + e.getMessage());
        }

    }
    
    public void laadFietsenBeheer()
    {
         try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(
                    "view/FietsenBeheer.fxml"));

            // controller instellen
            Parent root = loader.load();
            FietsenBeheerController controller = (FietsenBeheerController) loader.getController();

            // referentie naar hier bewaren in de controller
            controller.setParent(this);

            Scene scene = new Scene(root);
            stage.setTitle("Fietsen beheren");
            stage.setScene(scene);

        } catch (IOException e) {
            System.out.println("SYSTEEMFOUT bij laden fietsenbeheer: " + e.getMessage());
        }
            
    }

    public void laadRittenBeheer() {
        try {

            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(
                    "view/RitBeheer.fxml"));

            // controller instellen
            Parent root = loader.load();
            RitBeheerController controller = (RitBeheerController) loader.getController();

            // referentie naar hier bewaren in de controller
            controller.setParent(this);

            Scene scene = new Scene(root);
            stage.setTitle("Ritten Beheren");
            stage.setScene(scene);

        } catch (IOException e) {
            System.out.println("SYSTEEMFOUT bij laden Ritten Beheren: " + e.getMessage());
        }
    }
    
    public void laadSelectLid()
    {
         try {

            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(
                    "view/SelectLid.fxml"));

            // controller instellen
            Parent root = loader.load();
            SelectLidController controller = (SelectLidController) loader.getController();

            // referentie naar hier bewaren in de controller
            controller.setParent(this);

            Scene scene = new Scene(root);
            stage.setTitle("Lid selecteren");
            stage.setScene(scene);

        } catch (IOException e) {
            System.out.println("SYSTEEMFOUT bij laden Lid Selecteren: " + e.getMessage());
        }
    }
    
    public void laadSelectFiets(Lid l)
    {
         try {

            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(
                    "view/SelectFiets.fxml"));

            // controller instellen
            Parent root = loader.load();
            SelectFietsController controller = (SelectFietsController) loader.getController();

            // referentie naar hier bewaren in de controller
            controller.setParent(this , l);

            Scene scene = new Scene(root);
            stage.setTitle("Ritten Beheren");
            stage.setScene(scene);

        } catch (IOException e) {
            System.out.println("SYSTEEMFOUT bij laden Ritten Beheren: " + e.getMessage());
        }
    }
    
    
    public void laadRitControle(Lid l , Fiets f)
    {
         try {

            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(
                    "view/RitControle.fxml"));

            // controller instellen
            Parent root = loader.load();
            RitControleController controller = (RitControleController) loader.getController();

            // referentie naar hier bewaren in de controller
            controller.setParent(this , l , f );

            Scene scene = new Scene(root);
            stage.setTitle("Ritten Beheren");
            stage.setScene(scene);

        } catch (IOException e) {
            System.out.println("SYSTEEMFOUT bij laden Rit Controle: " + e.getMessage());
        }
    }

    public Stage getPrimaryStage() {
        return stage;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
