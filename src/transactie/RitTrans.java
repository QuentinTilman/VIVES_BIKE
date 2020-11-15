/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactie;

import databag.Fiets;
import databag.Lid;
import databag.Rit;
import database.FietsDB;
import database.LidDB;
import database.RitDB;
import datatype.Status;
import exception.ApplicationException;
import exception.DBException;
import java.sql.SQLException;
import java.util.ArrayList;

public class RitTrans implements InterfaceRitTrans {

    @Override
    public Integer toevoegenRit(Rit rit) throws ApplicationException, DBException, SQLException, Exception {
        FietsDB fdb = new FietsDB();
        LidDB db = new LidDB();

        if (rit == null) { //controleren of rit object leeg is
            throw new ApplicationException("er werd geen rit meegegeven");
        } 
       
        
        //controle op fiets
        if (fdb.zoekFiets(rit.getFietsRegistratienummer()) == null) { //controleren of de meegegeven fiets wel in database zit
            throw new ApplicationException("meegegeven fiets bestaat niet ");
        }
        
        if (fdb.zoekFiets(rit.getFietsRegistratienummer()).getStatus() != Status.actief) { //controleren of de fiets wel actief is
            throw new ApplicationException("meegegeven fiets is niet beschikbaar");
        }
        
        if(!zoekActieveRittenVanFiets(rit.getFietsRegistratienummer()).isEmpty()) //zoeken of meegegeven fiets niet in actieve rit zit
        { 
             throw new ApplicationException("meegegeven fiets heeft al een actieve rit");
        }
        
        
        //controle op Lid
        
         if(!zoekActieveRittenVanLid(rit.getLidRijksregisternummer()).isEmpty()) //controleren of Lid geen actieve rit heeft
         {
             throw new ApplicationException("meegegeven lid heeft al een actieve rit");
         }
        
        if (db.zoekLid(rit.getLidRijksregisternummer()) == null) {  //controleren of Lid wel in database zit
            throw new ApplicationException("meegegeven Rijksregisternummer is niet in gebruik");
        }
        
        if(db.zoekLid(rit.getLidRijksregisternummer()).getEinde_lidmaatschap() != null ) //controleren of lid niet al uitgeschreven is
        {
            throw new ApplicationException("Lid is uitgeschreven");
        }
        

        RitDB rdb = new RitDB();  //aanmaken van RitDaatabase Object
        return rdb.toevoegenRit(rit); //toevoegen van rit in database
    }

    @Override
    public void afsluitenRit(Integer id) throws ApplicationException, DBException, Exception {

        if (id == null) { //controleren of meegegeven Integer id niet null is
            throw new ApplicationException("er werd geen ID meegegeven");
        }

        if (zoekRit(id) == null) { //controleren of Lid in database zit
            throw new ApplicationException("gezochte rid bestaat niet");
        }

        if (zoekRit(id).getEindtijd() != null) { //controleren of Rit niet al afgesloten is
            throw new ApplicationException("Aangesproken rit is al afgesloten");
        }

        RitDB rdb = new RitDB(); //aanmaken van RitDatabase Object 
        rdb.afsluitenRit(zoekRit(id));//Afsluiten van meegegeven rit, gevonden door zoekRit
        //aangezien afsluiten rit methode een object Rit nodig heeft gebruiken we de methode zoekRit die een Rit object weergeeft

    }

    @Override
    public ArrayList zoekAlleRitten() throws DBException, ApplicationException {
        RitDB rdb = new RitDB(); //aanmaken van RitDatabase object
        return rdb.zoekAlleRitten(); //retourneren van een lijst met alle opgenomen ritten in de database
    }

    @Override
    public Rit zoekRit(Integer ritID) throws ApplicationException, Exception {
        if (ritID == null) { //controleren of meegegeven integer ritID niet null is
            throw new ApplicationException("er werd geen ritID meegegeven");
        }
        RitDB rdb = new RitDB(); //aanmaken van RitDatabase object
        return rdb.zoekRit(ritID); //retourneren van gevonden database

    }

    @Override
    public int zoekEersteRitVanLid(String rr) throws DBException, SQLException, ApplicationException, Exception {

        LidDB db = new LidDB();
        if (rr == null) { //controleren of meegegeven Rijksregisternummer niet null is
            throw new ApplicationException("er werd geen rijksregisternummer meegegeven");
        }

        if (db.zoekLid(rr) == null) { //zoeken of lid wel in Database bestaat
            throw new ApplicationException("gezochte lid bestaat niet");
        }

        RitDB rdb = new RitDB(); //rit database object aanmaken
        return rdb.zoekEersteRitVanLid(rr); //retourneren van de eerste Rit id van een Lid

    }

    @Override
    public ArrayList zoekActieveRittenVanLid(String rr) throws ApplicationException, DBException, SQLException, Exception {

        LidDB db = new LidDB();
        if ( rr == null) { //controleren of meegegeven rijksregisternummer niet null is
            throw new ApplicationException("er werd geen rijksregisternummer meegegeven");
        }

        if (db.zoekLid(rr) == null) { //controleren of lid wel in Database bestaat
            throw new ApplicationException("gezochte lid bestaat niet");
        }

        RitDB rdb = new RitDB(); //rit Database object aanmaken
        return rdb.zoekActieveRittenVanLid(rr); //weergeven van Actieve ritten van een lid

    }

    @Override
    public ArrayList zoekActieveRittenVanFiets(Integer regnr) throws ApplicationException, DBException, SQLException, Exception {
        FietsDB fdb = new FietsDB();
        if (regnr == null) { //cotnroleren of registernummer niet null is
            throw new ApplicationException("er werd geen registratienummer meegegeven");
        }

        if (fdb.zoekFiets(regnr) == null) { //controleren of fiets wel in database bestaat
            throw new ApplicationException("gezochte fiets bestaat niet");
        }
        RitDB rdb = new RitDB(); //Rit database object aanmaken
        return rdb.zoekActieveRittenVanFiets(regnr);//gevonden rit retourneren
    }

}
