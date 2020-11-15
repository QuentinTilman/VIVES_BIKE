/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import databag.Lid;
import datatype.Geslacht;
import datatype.Rijksregisternummer;
import exception.ApplicationException;
import exception.DBException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import transactie.FietsTrans;
import transactie.LidTrans;
import transactie.RitTrans;
import ui.VIVESbike;

/**
 * FXML Controller class
 *
 * @author Katrien.Deleu
 */
public class LedenBeheerController implements Initializable {

    // referentie naar VIVESbike (main)
    private VIVESbike parent;

    private ObservableList<Lid> Leden = FXCollections.
            observableArrayList();
    private boolean toevoegen = false;

    @FXML
    TableView<Lid> taLeden;

    @FXML
    TextField tfRijksregisternummer;

    @FXML
    TextField tfVoornaam;

    @FXML
    TextField tfNaam;

    @FXML
    RadioButton rbMan;

    @FXML
    RadioButton rbVrouw;

    @FXML
    TextField tfTelNr;

    @FXML
    TextField tfEmailAdress;

    @FXML
    CheckBox Uitgeschreven;

    @FXML
    TextArea tfOpmerking;

    @FXML
    Label ErrorMessage;

    /**
     * Referentie naar parent (start) instellen
     *
     * @param p
     * @param parent referentie naar de runnable class die alle oproepen naar de
     * schermen bestuurt
     */
    public void setParent(VIVESbike p) {
        parent = p;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        BlokerenVelden(); //methode oproepen om alle velden te blokeren zodat er niet geschreven kan zijn
        initialiseerTabel(); //methode opgeroepen om alle Leden vanuit de database in het tabel te plaatsen

    }

    @FXML
    private ObservableList<Lid> initialiseerTabel() { //methode om alle leden in de tabel te plaatsen of updaten van tabel na toevoegen/wijzigen

        try {
            LidTrans ltr = new LidTrans();
            taLeden.getItems().clear(); //Legen van tabel indien toevoegen / wijzigen , zodat het correct upgedated wordt.
            ArrayList<Lid> lidLijst = new ArrayList();
            lidLijst = ltr.zoekAlleLeden(); //toevoegen van alle leden in ArrayList

            lidLijst.forEach((l) -> { //Leden toevoegen in observable list
                Leden.add(l);
            });//lambda expression
            taLeden.setItems(Leden); //Leden toevoegen aan tabel

        } catch (DBException ex) {
            ErrorMessage.setText("Fout opgetreden bij initialiseertabel" + ex.getMessage());
        } catch (ApplicationException ex) {
            ErrorMessage.setText("Fout opgetreden bij initialiseertabel" + ex.getMessage());
        }
        return Leden;
    }

    public void toevoegen() {
        toevoegen = true; //aangeven dat we een gebruiker toevoegen en niet wijzigen
        vrijMakenVelden(); // velden vrijmaken zodat gebruiker er in kan typen
    }

    public void wijzigen(ActionEvent e) {
        Lid l = taLeden.getSelectionModel().getSelectedItem(); //opnemen van geselecteerd Lid object in tabel
        LidTrans ltd = new LidTrans();

        vrijMakenVelden(); // velden vrijmaken zodat gebruiker er in kan typen

        //plaatsen van opgehaalde waarden in velden zodat er aangepast kan worden.
        tfRijksregisternummer.setText(l.getRijksregisternummer().toString());
        tfNaam.setText(l.getNaam().toString());
        tfVoornaam.setText(l.getVoornaam().toString());
        tfEmailAdress.setText(l.getEmailadres());
        tfOpmerking.setText(l.getOpmerkingen().toString());
        tfTelNr.setText(l.getTelnr());

        toevoegen = false; //aangeven dat we niet zullen toevoegen maar wijzigen

        tfRijksregisternummer.setDisable(true); //blokkeren van het veld Rijksregisternummer aangezien dat het een primaire sleutel is

    }

    public void vrijMakenVelden() { // het vrijmaken van velden zodat gebruiker kan typen

        tfRijksregisternummer.setDisable(false);
        tfNaam.setDisable(false);
        tfVoornaam.setDisable(false);
        tfTelNr.setDisable(false);
        tfEmailAdress.setDisable(false);
        rbMan.setDisable(false);
        rbVrouw.setDisable(false);
        tfOpmerking.setDisable(false);

        //leegmaken van velden 
        tfRijksregisternummer.clear();
        tfNaam.clear();
        tfVoornaam.clear();
        tfTelNr.clear();
        tfOpmerking.clear();

    }

    public void BlokerenVelden() { //blokkeren van velden

        //leegmaken van velden
        tfRijksregisternummer.clear();
        tfNaam.clear();
        tfVoornaam.clear();
        tfTelNr.clear();
        tfEmailAdress.clear();
        rbMan.contentDisplayProperty().isNull();
        rbVrouw.contentDisplayProperty().isNull();
        tfOpmerking.clear();

        tfRijksregisternummer.setDisable(true);
        tfNaam.setDisable(true);
        tfVoornaam.setDisable(true);
        tfTelNr.setDisable(true);
        tfEmailAdress.setDisable(true);
        rbMan.setDisable(true);
        rbVrouw.setDisable(true);
        tfOpmerking.setDisable(true);

    }

    public void Opslaan(ActionEvent e) { //methode opgeroepen voor zowel toevoegen als wijzigen
        try {
            chechAlleVeldenIngevuld(); //controle of alle velden correct ingevuld zijn
            ActionEvent ev = e;

            if (toevoegen) {
                ToevoegenLid(ev); //indien toevoegen (true) dan viegen we lid toe
            } else {
                WijzigenLid(ev); //indien false wijzigen we onze lid
            }
            BlokerenVelden(); //na wijzigen of opslaan blokeren we onze velden opnieuw
        } catch (ApplicationException ex) {
            ErrorMessage.setText(ex.getMessage());
        }
    }

    @FXML
    public void ToevoegenLid(ActionEvent e) { //toevoegen van lid in de database
        try {
            Lid l = new Lid();
            Rijksregisternummer rr = new Rijksregisternummer(tfRijksregisternummer.getText());
            l.setRijksregisternummer(rr);
            l.setNaam(tfNaam.getText());
            l.setVoornaam(tfVoornaam.getText());
            if (rbMan.isSelected()) { //if statement voor keuze tussen M/V
                l.setGeslacht(Geslacht.M);
            } else {
                l.setGeslacht(Geslacht.V);
            }

            l.setTelnr(tfTelNr.getText());
            l.setEmailadres(tfEmailAdress.getText());
            l.setStart_lidmaatschap(LocalDate.now());
            l.setEinde_lidmaatschap(null);
            l.setOpmerkingen(tfOpmerking.getText());
            LidTrans Lt = new LidTrans();

            Lt.toevoegenLid(l); //toevoegen van lid in database

            initialiseerTabel(); //updaten van Tabel

        } catch (DBException ex) {
            ErrorMessage.setText(ex.getMessage());
        } catch (ApplicationException ex) {
            ErrorMessage.setText(ex.getMessage());
        } catch (SQLException ex) {
            ErrorMessage.setText(ex.getMessage());
        }

    }

    @FXML
    public void WijzigenLid(ActionEvent e) { //wijzigen van Lid in database
        try {
            //aanmaken van nieuwe lid object om zo alle waarden erin te plaatsen en door te sturen naar database
            Lid l = new Lid();
            Rijksregisternummer rr = new Rijksregisternummer(tfRijksregisternummer.getText());
            l.setRijksregisternummer(rr);
            l.setNaam(tfNaam.getText());
            l.setVoornaam(tfVoornaam.getText());

            if (rbMan.getText().equals(Geslacht.M)) { //if statement voor keuze tussen M/V
                l.setGeslacht(Geslacht.M);
            } else {
                l.setGeslacht(Geslacht.V);
            }

            l.setTelnr(tfTelNr.getText());
            l.setEmailadres(tfEmailAdress.getText());
            l.setStart_lidmaatschap(LocalDate.now());
            l.setEinde_lidmaatschap(null);
            l.setOpmerkingen(tfOpmerking.getText());
            LidTrans Lt = new LidTrans();
            Lt.wijzigenLid(l); //wijzigen van Lid in database
            initialiseerTabel(); //updaten van Tabel

        } catch (ApplicationException ex) {
            ErrorMessage.setText(ex.getMessage());
        } catch (DBException ex) {
            ErrorMessage.setText(ex.getMessage());
        } catch (SQLException ex) {
            ErrorMessage.setText(ex.getMessage());
        }
    }

    @FXML
    public void uitschrijvenLid(ActionEvent e) { //uitschrijven van lid
        try {
            LidTrans Lt = new LidTrans(); //aanmaken van Lid transactie object
            Lt.uitschrijvenLid(taLeden.getSelectionModel().getSelectedItem().getRijksregisternummer()); // primaire sleutel opnemen van geselecteerde Lid in Tabel

        } catch (ApplicationException ex) {
            ErrorMessage.setText(ex.getMessage());
        } catch (DBException ex) {
            ErrorMessage.setText(ex.getMessage());
        } catch (SQLException ex) {
            ErrorMessage.setText(ex.getMessage());
        } catch (Exception ex) {
            ErrorMessage.setText(ex.getMessage());
        }
    }

    private void chechAlleVeldenIngevuld() throws ApplicationException { //controle van velden
        if (tfRijksregisternummer.getText().equals("")) {
            throw new ApplicationException("Gelieve Lid een RijksRegisternummer aan te geven");
        }

        if (tfNaam.getText().equals("")) {
            throw new ApplicationException("Gelieve het veld Naam in te vullen");
        }

        if (tfVoornaam.getText().equals("")) {
            throw new ApplicationException("Gelieve het veld Voornaam in te vullen");
        }

        if (rbMan.getText().equals("")) {
            throw new ApplicationException("Gelieve het veld Geslacht in te vullen");
        }

        if (tfEmailAdress.getText().equals("")) {
            throw new ApplicationException("gelieve het veld EmailAdress in te vullen");
        }

        if (tfTelNr.getText().equals("")) {
            throw new ApplicationException("Gelieve het veld Telefoon nummer in te vullen");
        }
    }

    @FXML
    public void annuleren(ActionEvent e) {
        tfNaam.undo();
        tfVoornaam.undo();
        tfRijksregisternummer.undo();
        tfEmailAdress.undo();
        tfTelNr.undo();
        tfOpmerking.undo();
        //undo methode op een textfiels plaats de oude waarde voor deze aangepast werd.

    }

    @FXML
    public void laadGegevens(MouseEvent e) throws Exception {
        Lid l = taLeden.getSelectionModel().getSelectedItem();
        tfRijksregisternummer.setText(l.getRijksregisternummer());
        tfVoornaam.setText(l.getVoornaam());
        tfNaam.setText(l.getNaam());
        tfEmailAdress.setText(l.getEmailadres());
        tfTelNr.setText(l.getTelnr());
        if (l.getGeslacht() == Geslacht.M) {
            rbMan.setSelected(true);
        } else {
            rbVrouw.setSelected(true);
        }
        tfOpmerking.setText(l.getOpmerkingen());

        //blokeren van velden
        tfRijksregisternummer.setDisable(true);
        tfNaam.setDisable(true);
        tfVoornaam.setDisable(true);
        tfTelNr.setDisable(true);
        tfEmailAdress.setDisable(true);
        rbMan.setDisable(true);
        rbVrouw.setDisable(true);
        tfOpmerking.setDisable(true);

    }

    public void laadMenu() {
        parent.showstartscherm();
    }

}
