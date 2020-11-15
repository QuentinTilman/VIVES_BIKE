/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import databag.Fiets;
import databag.Lid;
import exception.ApplicationException;
import exception.DBException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import transactie.FietsTrans;
import ui.VIVESbike;

/**
 * FXML Controller class
 *
 * @author Quent
 */
public class SelectFietsController implements Initializable {

    private VIVESbike app;
    
    private Lid lid;
    
    @FXML
    TableView<Fiets> taFietsen;
    
    @FXML
    Label ErrorMessage;
    
    private ObservableList<Fiets> Fietsen = FXCollections.
            observableArrayList();
    
    
    
    public void setParent(VIVESbike p , Lid l) {
        this.app=p; 
        lid=l; //lid opnemen en bijhouden om naar volgende UI view/controller door te sturen
        System.out.println(l); //lid afprinten voor controle
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initialiseerTabel(); //initialiseren van tabel bracht hier geen probleem in initialize methode
        
    }

    @FXML
    private ObservableList<Fiets> initialiseerTabel() {  //tabel initialiseren om alle beschilbare fietsen te kunnen zien

        try {
            FietsTrans ft = new FietsTrans(); //fiets transactie object aanmaken
            taFietsen.getItems().clear();//tabel legen van al zijn objecten
            ArrayList<Fiets> fietslijst = new ArrayList(); //Arraylist maken om alle opgehaalde fietsen bij te houden
            fietslijst = ft.zoekAlleBeschikbareFietsen(); //toevoegen van alle leden in ArrayList

            fietslijst.forEach((f) -> { //plaatsen van alle fietsen binnen Arraylist in ObservableList
                Fietsen.add(f);
            });//lambda expression
            taFietsen.setItems(Fietsen); //Observable list in tabel gaan plaatsen

        } catch (DBException ex) {
            ErrorMessage.setText("Fout opgetreden bij initialiseertabel" + ex.getMessage());
        } catch (ApplicationException ex) {
            ErrorMessage.setText("Fout opgetreden bij initialiseertabel" + ex.getMessage());
        }
        return Fietsen; //retourneren van alle fietsen
    }

    
    
    public void laadControle() //volgende UI view opladen 
    {
       if(taFietsen.getSelectionModel().getSelectedItem() == null ) //controleren of er wel een fiets geselecteerd is / geen controle op lid aangezien dit in vorige controller gedaan wordt
        {
            ErrorMessage.setText("Gelieve een Fiets te selecteren");
        }
        else
        {
            Fiets fiets = taFietsen.getSelectionModel().getSelectedItem(); //geselecteerde fiets in Fiets object plaatsen
            app.laadRitControle(lid , fiets); //zowel lid als fiets object doorsturen naar volgende View/Controller
        }
        
    }
    
    public void back()
    {
        app.laadSelectLid(); //terugkeren naar het selecteren van een lid
    }
    
    
}
