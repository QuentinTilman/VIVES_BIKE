package transactie;

import databag.Lid;
import database.LidDB;
import exception.ApplicationException;
import exception.DBException;
import java.sql.SQLException;
import java.util.ArrayList;

public class LidTrans implements InterfaceLidTrans {

    @Override
    public void toevoegenLid(Lid l) throws ApplicationException, DBException, SQLException {

        if (l == null) {
            throw new ApplicationException("Er werd geen lid meegegeven");  //controleren of megegeven lid niet null is
        }

        if (l.getRijksregisternummer() == null) { //controleren of de waarde lidRijksregisternummer van Rit niet null is
            throw new ApplicationException("er werd geen id meegegeven");
        }

        if ( zoekLid(l.getRijksregisternummer()) != null ) { //controleren of de waarde fietsRegistratienummer van rit niet null is
            throw new ApplicationException("lid bestaat al");
        }
        LidDB db = new LidDB(); //nieuwe Lid database object aanmaken
        db.toevoegenLid(l); //lid toevoegen aan database

    }

    @Override
    public void wijzigenLid(Lid teWijzigenLid) throws ApplicationException, DBException, SQLException {
        if (teWijzigenLid == null) {
            throw new ApplicationException("geen lid meegegeven"); //controleren of meegegeven Lid object niet null is
        }

        if (zoekLid(teWijzigenLid.getRijksregisternummer()) == null) { //controleren of meegegeven Lid object wel in database bestaat
            throw new ApplicationException("lid bestaat niet");
        }

        LidDB db = new LidDB(); //nieuwe Lid database object aanmaken
        db.wijzigenLid(teWijzigenLid); //lid toevoegen aan database

    }

    @Override
    public void uitschrijvenLid(String rr) throws ApplicationException, DBException, SQLException, Exception {
        RitTrans rt = new RitTrans(); //Rit transavtie object aanmaken
        
        if (rr == null) { //controleren of meegegeven string niet null is
            throw new ApplicationException("er werd geen ID meegegeven");
        }
        
        if(rr.trim().equals("")) //controleren of meegegeven string geen spaties zijn
        {
            throw new ApplicationException("er werd geen geldige ID meegegeven");
        }

        if (zoekLid(rr) == null) { //controleren of Lid wel in database bestaat
            throw new ApplicationException("lid bestaat niet");
        }
        
        if(!rt.zoekActieveRittenVanLid(rr).isEmpty()) //controleren of een rit geen actieve ritten heeft , error wordt gegooid als  "! isEmpty" geef aan dat opgenomen lijst niet leeg mag zijn 
        {
            throw new ApplicationException("lid heeft nog een actieve rit");
        }

        LidDB db = new LidDB(); //aanmaken Lid database object 

        db.uitschrijvenLid(rr); //lid gaan uitschrijven

    }

    @Override
    public Lid zoekLid(String rijksregisternummer) throws ApplicationException, DBException, SQLException {

        if (rijksregisternummer == null) { //controle of meegegeven string niet leeg is
            throw new ApplicationException("er werd geen rijksregisternummer mee gegeven");
        }
        LidDB ldb= new LidDB(); //aanmaken van Lid database object
        return ldb.zoekLid(rijksregisternummer); //retourneren van gevonden object
    }

    @Override
    public ArrayList<Lid> zoekAlleLeden() throws DBException, ApplicationException {
        
        LidDB ldb= new LidDB(); //aanmaken van lid database object
        return ldb.zoekAlleLeden(); //retourneren van alle gevonden leden
    }
    
    public ArrayList<Lid> zoekAlleBeschikbareLeden() throws DBException
    {
     LidDB ldb = new LidDB(); //lid database object aanmaken
     return ldb.zoekAlleBeschikbareLeden(); //alle beschikbare , dwz niet in actieve rit of uitgeschreven , retourneren
    }

}
