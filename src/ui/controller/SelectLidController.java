/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import databag.Lid;
import exception.ApplicationException;
import exception.DBException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import transactie.LidTrans;
import ui.VIVESbike;

/**
 * FXML Controller class
 *
 * @author Quent
 */
public class SelectLidController implements Initializable {

    private VIVESbike app;

    @FXML
    TableView<Lid> taLeden;

    private ObservableList<Lid> Leden = FXCollections.
            observableArrayList();
    @FXML
    Label ErrorMessage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
    }

    public void setParent(VIVESbike p) {
        this.app = p;
        initialiseerTabel(); //tabel in Setparent initialiseren want dit gebeurt na initialize, zorgde anders voor problemen bij ophalen
    }

    public void laadFietsSelectie() { //laden van UI view voor het selecteren van beschikbare fietsen
        if (taLeden.getSelectionModel().getSelectedItem() == null) { //controleren of er een lid geselecteerd is
            ErrorMessage.setText("selecteer lid om verder te gaan");
        } else {
            Lid l = taLeden.getSelectionModel().getSelectedItem(); //lid meegeven naar volgende UI view/controller
            app.laadSelectFiets(l);
        }

    }

    public void back() {
        app.laadRittenBeheer(); //terugkeren naar vorige UI scherm hier Ritten Beheer
    }

    @FXML
    private ObservableList<Lid> initialiseerTabel() { //initialiseren van alle beschikbare Leden

        try {
            LidTrans ltr = new LidTrans(); //lid transactie object aanmaken
            taLeden.getItems().clear(); //Tabel legen van alle objecten
            ArrayList<Lid> lidLijst = new ArrayList(); //arraylist maken om alle leden bij te houden
            lidLijst = ltr.zoekAlleBeschikbareLeden(); //toevoegen van alle leden in ArrayList
            lidLijst.forEach((l) -> { //alle leden één per één toevoegen in Observable List
                Leden.add(l);
            });//lambda expression
            taLeden.setItems(Leden); //alle objecten binnen Observable list in tabel plaatsen
            return Leden; //alle leden retourneren
        } catch (DBException ex) {
            ErrorMessage.setText("fouten bij ophalen van Leden"+ex.getMessage());
        }
        return null;
    }
 
   

}
