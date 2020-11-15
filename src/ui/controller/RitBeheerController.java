/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import databag.Rit;
import databag.Lid;
import exception.ApplicationException;
import exception.DBException;
import transactie.RitTrans;
import java.util.ArrayList;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import transactie.RitTrans;
import ui.VIVESbike;

/**
 * FXML Controller class
 *
 * @author Quent
 */
public class RitBeheerController implements Initializable {

    private VIVESbike app;

    private ObservableList<Rit> Ritten = FXCollections.
            observableArrayList();

    @FXML
    TableView<Rit> taRitten;

    @FXML
    TextArea tfOpmerking;

    @FXML
    Label ErrorMessage;

    public void setParent(VIVESbike p) {
        this.app = p;
    }
//bij het opladen van ritbeheer zal de tabel worden geinitialiseerd
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initialiseerTabel();
    }

    @FXML
    private ObservableList<Rit> initialiseerTabel() {
        try {
            RitTrans rt = new RitTrans();
            taRitten.getItems().clear();
            //alle ritten toevoegen in een arraylist
            ArrayList<Rit> LijstRitten = new ArrayList();
            LijstRitten = rt.zoekAlleRitten();
            LijstRitten.forEach((r) -> {
                Ritten.add(r);
            });
            taRitten.setItems(Ritten);

        } catch (DBException ex) {
            ErrorMessage.setText("Fout opgetreden bij initialiseertabel" + ex.getMessage());
        } catch (ApplicationException ex) {
            ErrorMessage.setText("Fout opgetreden bij initialiseertabel" + ex.getMessage());
        }
        return Ritten;

    }
//toevoegen van een rit
    public void ToevoegenRit() {
        app.laadSelectLid();
    }
    //afsluiten van een rit
    public void afsluitenRit(ActionEvent e)
    {
        try {
            //rit trans wordt aangemaakt 
            RitTrans rt = new RitTrans();
            System.out.println(taRitten.getSelectionModel().getSelectedItem().getRitID());
            rt.afsluitenRit(taRitten.getSelectionModel().getSelectedItem().getRitID());
            initialiseerTabel();
        } catch (DBException ex) {
             ErrorMessage.setText("Fout opgetreden bij afsluitenRit" + ex.getMessage());
             System.out.println("test"+ex.getMessage());
        } catch (Exception ex) {
             ErrorMessage.setText("Fout opgetreden bij afsluitenRit" + ex.getMessage());
             System.out.println("test"+ex.getMessage());
        }
    }
            

    public void laadSelectLid() {
        app.laadSelectLid();
    }

    public void laadMenu() {
        app.showstartscherm();
    }

}
