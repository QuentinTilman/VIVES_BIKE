/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactie;

import databag.Lid;
import java.util.ArrayList;

/**
 *
 * @author Katrien.Deleu
 */
public interface InterfaceLidTrans {

    void toevoegenLid(Lid l) throws Exception;

    void wijzigenLid(Lid teWijzigenLid) throws Exception;

    void uitschrijvenLid(String rr) throws Exception;

    Lid zoekLid(String rijksregisternummer) throws Exception;

    ArrayList<Lid> zoekAlleLeden() throws Exception;
}
