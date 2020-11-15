/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import databag.Fiets;
import datatype.Standplaats;
import datatype.Status;
import exception.ApplicationException;
import exception.DBException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import transactie.FietsTrans;
import datatype.Standplaats;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import transactie.LidTrans;

import ui.VIVESbike;

/**
 * FXML Controller class
 *
 * @author vdkrobby
 */
public class FietsenBeheerController implements Initializable {

    private VIVESbike parent;

    private ObservableList<Fiets> Fietsen = FXCollections.observableArrayList();

    @FXML
    TableView<Fiets> taFietsen;

    @FXML
    TextArea tfOpmerking;

    @FXML
    TextField tfRegNr;

    @FXML
    ComboBox<Standplaats> cbPlaats;

    @FXML
    Label ErrorMessage;

    @FXML
    RadioButton rbActief;
    @FXML
    RadioButton rbHerstelling;

    @FXML
    RadioButton rbUitomloop;

    @FXML
    Button btnTvFiets;

    ToggleGroup group = new ToggleGroup();

    public void setParent(VIVESbike p) {
        parent = p;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //blokeren van velden  & tabel initialiseren
        BlokerenVelden();
        initialiseerTabel();
        cbPlaats.setItems((FXCollections.observableArrayList(Standplaats.values())));
        //rb groep aanmaken om maar 1 keuze te maken
        rbActief.setToggleGroup(group);
        rbHerstelling.setToggleGroup(group);
        rbUitomloop.setToggleGroup(group);

    }

    @FXML
    private ObservableList<Fiets> initialiseerTabel() { //methode om alle leden in de tabel te plaatsen of updaten van tabel na toevoegen/wijzigen
        try {
            FietsTrans ft = new FietsTrans();
            taFietsen.getItems().clear(); //Legen van tabel indien toevoegen / wijzigen , zodat het correct upgedated wordt.
            ArrayList<Fiets> fietsLijst = new ArrayList();
            fietsLijst = ft.zoekAlleFietsen(); //toevoegen van alle fietsen in ArrayList
            Fietsen.addAll(fietsLijst);
            taFietsen.setItems(Fietsen); //Fietsen toevoegen aan tabel

        } catch (DBException ex) {
            ErrorMessage.setText("Fout opgetreden bij initialiseertabel" + ex.getMessage());
        }
        return Fietsen;
    }

    private void BlokerenVelden() {
        //tekstboxen leegmaken
        tfRegNr.clear();

        //velden blokeren
        tfRegNr.setDisable(true);
        tfOpmerking.setDisable(true);
        cbPlaats.setDisable(true);

        rbActief.setDisable(true);
        rbHerstelling.setDisable(true);
        rbUitomloop.setDisable(true);
        btnTvFiets.setDisable(true);
        tfRegNr.clear();

    }

    @FXML
    private void Toevoegen(ActionEvent e) {
        try {
            //nieuwe fiets trans aanamaken
            FietsTrans ft = new FietsTrans();
            //genereren van een fiets registratienummer
            Integer id = ft.zoekAlleFietsen().size() + 1;
            tfRegNr.setText(id.toString());
        } catch (DBException ex) {
            ErrorMessage.setText(ex.getMessage());
        }
        vrijMakenVelden();
        cbPlaats.getSelectionModel().select(-1);
    }

    private void vrijMakenVelden() {
        //velden vrijmaken

        tfRegNr.setDisable(true);
        tfOpmerking.setDisable(false);
        cbPlaats.setDisable(false);
        rbActief.setDisable(true);
        rbHerstelling.setDisable(true);
        rbUitomloop.setDisable(true);
        btnTvFiets.setDisable(false);
        tfOpmerking.clear();

    }

    @FXML
    private void ToevoegenFiets(ActionEvent e) {
        try {
//object aanmaken voor fiets waarna alle gegevens worden toegevoegd
            Fiets f = new Fiets();
            String sregnr = tfRegNr.getText();
            int iregnr = Integer.parseInt(sregnr);
            f.setRegistratienummer(iregnr);

            f.setStandplaats(cbPlaats.getSelectionModel().getSelectedItem());
            f.setStatus(Status.actief);
            f.setOpmerking(tfOpmerking.getText());
//trans aanmaken
            FietsTrans ft = new FietsTrans();
//object fiets toevoegen aan fiets trans
            ft.toevoegenFiets(f);
            clearVelden();
            initialiseerTabel();

        } catch (DBException ex) {
            ErrorMessage.setText(ex.getMessage());
        } catch (ApplicationException ex) {
            ErrorMessage.setText(ex.getMessage());
        } catch (SQLException ex) {
            ErrorMessage.setText(ex.getMessage());
        }
    }

    @FXML
    public void wijzigenStatus(ActionEvent e) {
        //object maken van geselecteerde fiets & nieuwe fiets trans
        Fiets f = taFietsen.getSelectionModel().getSelectedItem();
        FietsTrans ft = new FietsTrans();
//waarden tonen in velden 
        tfRegNr.setText(f.getRegistratienummer().toString());
        tfOpmerking.setText(f.getOpmerking());
        tfOpmerking.setDisable(false);
        cbPlaats.setValue(f.getStandplaats());

        tfRegNr.setDisable(true);
        cbPlaats.setDisable(true);
        btnTvFiets.setDisable(true);

        rbActief.setDisable(false);

        rbHerstelling.setDisable(false);

        rbUitomloop.setDisable(false);

    }

    @FXML
    public void naarActief(ActionEvent e) throws Exception {
        try {
            //object aanmaken voor huidige fiets
            Fiets f = taFietsen.getSelectionModel().getSelectedItem();
            FietsTrans ft = new FietsTrans();
            ft.wijzigenOpmerkingFiets(f.getRegistratienummer(), tfOpmerking.getText());
//kijken of de fiets uit een herstelling komt en niet reeds actief is
            if (f.getStatus().equals(Status.herstel)) {
                ft.wijzigenHerstelNaarActief(f.getRegistratienummer());
            }
            //opmerking toevoegen 
            ft.wijzigenOpmerkingFiets(f.getRegistratienummer(), tfOpmerking.getText());
            clearVelden();
            initialiseerTabel();
        } catch (DBException ex) {
            ErrorMessage.setText(ex.getMessage());
        } catch (ApplicationException ex) {
            ErrorMessage.setText(ex.getMessage());
        } catch (SQLException ex) {
            ErrorMessage.setText(ex.getMessage());
        }
    }

    @FXML
    public void naarHerstelling(ActionEvent e) throws Exception {
        try {
             //object aanmaken voor huidige fiets
            Fiets f = taFietsen.getSelectionModel().getSelectedItem();
            FietsTrans ft = new FietsTrans();
            //kijken naar vorige status indien actief, aanpassen naar herstelling
            if (f.getStatus().equals(Status.actief)) {
                ft.wijzigenActiefNaarHerstel(f.getRegistratienummer());
            }
            //opmerking toevoegen
            ft.wijzigenOpmerkingFiets(f.getRegistratienummer(), tfOpmerking.getText());

            clearVelden();
            initialiseerTabel();
        } catch (DBException ex) {
            ErrorMessage.setText(ex.getMessage());
        } catch (ApplicationException ex) {
            ErrorMessage.setText(ex.getMessage());
        } catch (SQLException ex) {
            ErrorMessage.setText(ex.getMessage());
        }
    }

    @FXML
    public void naarUitomloop(ActionEvent e) {
        try {
            Fiets f = taFietsen.getSelectionModel().getSelectedItem();
            FietsTrans ft = new FietsTrans();
            ft.wijzigenOpmerkingFiets(f.getRegistratienummer(), tfOpmerking.getText());

            if (f.getStatus().equals(Status.actief)) {
                ft.wijzigenActiefNaarUitOmloop(f.getRegistratienummer());
            } else if (f.getStatus().equals(Status.herstel)) {
                ft.wijzigenHerstelNaarUitOmloop(f.getRegistratienummer());
            }
            //opmerking toevoegen
            ft.wijzigenOpmerkingFiets(f.getRegistratienummer(), tfOpmerking.getText());
            clearVelden();
            initialiseerTabel();
        } catch (DBException ex) {
            ErrorMessage.setText(ex.getMessage());
        } catch (ApplicationException ex) {
            ErrorMessage.setText(ex.getMessage());
        } catch (SQLException ex) {
            ErrorMessage.setText(ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(FietsenBeheerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clearVelden() {
        //velden clearen
        rbActief.setDisable(true);
        rbHerstelling.setDisable(true);
        rbUitomloop.setDisable(true);
        rbActief.setSelected(false);
        rbHerstelling.setSelected(false);
        rbUitomloop.setSelected(false);
        tfOpmerking.clear();
        tfOpmerking.setDisable(true);
        cbPlaats.setDisable(true);
        //combobox plaats naar lege waarde zetten door hem op index -1 te plaatsen
        cbPlaats.getSelectionModel().select(-1);

    }
//bij laad gegevens maken we gebruik van een MouseEvent, bij het aanklikken van 1 fiets zal de waarde tevoorschijn komen in de velden
    @FXML
    public void laadGegevens(MouseEvent e) {
        //object aanmaken voor fiets van huidig geselecteerde fiets
        Fiets f = taFietsen.getSelectionModel().getSelectedItem();
        BlokerenVelden();
        tfRegNr.setText(f.getRegistratienummer().toString());
        tfOpmerking.setText(f.getOpmerking());
        cbPlaats.setValue(f.getStandplaats());

    }

    public void Main() {
        parent.showstartscherm();
    }
}
