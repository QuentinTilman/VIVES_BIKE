/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import ui.VIVESbike;

/**
 * FXML Controller class
 *
 * @author Martijn
 */
public class StartschermController implements Initializable {

    // referentie naar VIVESbike (main)
    private VIVESbike parent;

    @FXML
    Button bu;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
    }

    /**
     * Referentie naar parent (start) instellen
     *
     * @param parent referentie naar de runnable class die alle oproepen naar de
     * schermen bestuurt
     */
    public void setParent(VIVESbike p) {
        parent = p;
    }

    @FXML
    public void Leden(ActionEvent e) {
        parent.laadLedenbeheer();
    }
    
    public void Fietsen(ActionEvent e)
    {
        parent.laadFietsenBeheer();
    }

    public void Ritten(ActionEvent e)
    {
        parent.laadRittenBeheer();
    }
}
