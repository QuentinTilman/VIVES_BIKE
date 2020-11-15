/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactie;

import databag.Rit;
import java.util.ArrayList;

/**
 *
 * @author Katrien.Deleu
 */
public interface InterfaceRitTrans {

    Integer toevoegenRit(Rit rit) throws Exception;

    void afsluitenRit(Integer id) throws Exception;

    ArrayList zoekAlleRitten() throws Exception;

    Rit zoekRit(Integer ritID) throws Exception;

    int zoekEersteRitVanLid(String rr) throws Exception;

    ArrayList zoekActieveRittenVanLid(String rr) throws Exception;

    ArrayList zoekActieveRittenVanFiets(Integer regnr) throws Exception;

}
