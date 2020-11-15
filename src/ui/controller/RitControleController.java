/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import databag.Fiets;
import databag.Lid;
import databag.Rit;
import datatype.Rijksregisternummer;
import exception.ApplicationException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import transactie.RitTrans;
import ui.VIVESbike;

/**
 * FXML Controller class
 *
 * @author Quent
 */
public class RitControleController implements Initializable {

    private VIVESbike app;

    @FXML
    ListView lvLid;

    @FXML
    ListView lvFiets;
    
    @FXML
    Label ErrorMessage;

    private Lid lid;
    private Fiets fiets;

    private ObservableList<Lid> lijstLid = FXCollections.
            observableArrayList();

    private ObservableList<Fiets> LijstFiets = FXCollections.
            observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setParent(VIVESbike p, Lid l, Fiets f) { //fiets en lid meegeven en bijhouden
        this.app = p;
        lid = l;
        fiets = f;
        initLvLid(); //initialiseren van Listview met meegegeven Lid
        initLvFiets();//initialiseren van Listview met meegegeven Fiets
        //aangezien initialize eerst wordt uitgevoerd moeten de init tabellen hier plaatsnemen want we geven die door
        //via onze setParent methode 

    }

    public ObservableList<Lid> initLvLid() {
        lijstLid.add(lid); //Lid in observable list plaatsen
        lvLid.setItems(lijstLid);                 //observable list gaan plaatsen in listview ( dit roept automatisch de toString() methode van een Lid.
        return lijstLid;//retourneren van lijst   //om dit ta laten passen in een listview gebruik ik "enter" in toString
    }

    public ObservableList<Fiets> initLvFiets() {
        LijstFiets.add(fiets);//Fiets in observable list plaatsen
        lvFiets.setItems(LijstFiets);               //observable list gaan plaatsen in listview ( dit roept automatisch de toString() methode van een Fiets.
        return LijstFiets;//retourneren van lijst   //om dit ta laten passen in een listview gebruik ik "enter" in toString
    }

    public void laadSelectLid() {
        app.laadSelectLid(); //terugkeren naar Lid selecteren
    }

    public void LaadSelectFiets() {
        app.laadSelectFiets(lid); //terugkeren naar fiets selecteren
    }

    public void laadRitBeheer() { //terugkeren naar Rit beheer + aanmaken van Rit
        try {
            RitTrans rt = new RitTrans(); //rit tranasactie laag aanmaken
            Rit rit = new Rit(); //Aanmaken van een Rit object
            Rijksregisternummer rs = new Rijksregisternummer(lid.getRijksregisternummer()); //aanmaken van een Rijksregisternummer voor meegegeven lid
            rit.setLidRijksregisternummer(rs); //plaatsen van rijksregisternummer meegegeven lid in rit
            rit.setFietsRegistratienummer(fiets.getRegistratienummer()); //registernummer van meegegeven fiets in Rit object plaatsen
            rit.setStarttijd(LocalDateTime.now()); //starttijd van rit in object plaatsen
            rt.toevoegenRit(rit);// //toevoegen Rit in database
            app.laadRittenBeheer(); //parent oproepen om naar Ritten beheer View te gaan
        } catch (SQLException ex) {
            ErrorMessage.setText(ex.getMessage());
        } catch (Exception ex) {
             ErrorMessage.setText(ex.getMessage());
        }
    }

}
