package transactie;

import databag.Fiets;
import database.FietsDB;
import database.RitDB;
import datatype.Status;
import exception.ApplicationException;
import exception.DBException;
import java.sql.SQLException;
import java.util.ArrayList;

public class FietsTrans implements InterfaceFietsTrans {
    
    @Override
    public Integer toevoegenFiets(Fiets fiets) throws ApplicationException, DBException, SQLException {
        if (fiets == null) {
            throw new ApplicationException("er werd geen fiets meegegeven");
        }

        if (fiets.getRegistratienummer() == null) {
            throw new ApplicationException("er werd geen registratienummer meegegeven");
        }

        if (zoekFiets(fiets.getRegistratienummer()) != null) {
            throw new ApplicationException("fiets bestaat al");
        }
        FietsDB fdb = new FietsDB();
        return fdb.toevoegenFiets(fiets);
    }

    @Override
    public void wijzigenActiefNaarHerstel(Integer regnr) throws ApplicationException, DBException, SQLException, Exception {
        RitDB rdb = new RitDB();

        if (regnr == null) {
            throw new ApplicationException("er werd geen registratienummer meegegeven");
        }

        if (zoekFiets(regnr) == null) {
            throw new ApplicationException("fiets bestaat niet");
        }

        if (zoekFiets(regnr).getStatus().equals(Status.herstel)) {
            throw new ApplicationException(" fiets al in herstelling");
        }

        if (zoekFiets(regnr).getStatus() == Status.uit_omloop) {
            throw new ApplicationException(" fiets is niet meer in gerbuik");
        }
        
        if(rdb.zoekActieveRittenVanFiets(regnr).iterator().hasNext())
        {
             throw new ApplicationException(" fiets is nog in een actieve Rit");
        }

        FietsDB fdb = new FietsDB();
        fdb.wijzigenToestandFiets(regnr, Status.herstel);
    }

    @Override
    public void wijzigenActiefNaarUitOmloop(Integer regnr) throws ApplicationException, DBException, SQLException, Exception {
        RitDB rdb = new RitDB();
        
        if (regnr == null) {
            throw new ApplicationException("er werd geen registratienummer meegegeven");
        }

        if (zoekFiets(regnr) == null) {
            throw new ApplicationException("fiets bestaat niet");
        }

        if (zoekFiets(regnr).getStatus() == Status.uit_omloop) {
            throw new ApplicationException(" fiets is al niet meer in gerbuik");
        }
        
        if(rdb.zoekActieveRittenVanFiets(regnr).iterator().hasNext())
        {
             throw new ApplicationException(" fiets is nog in een actieve Rit");
        }
        
        FietsDB fdb = new FietsDB();
        fdb.wijzigenToestandFiets(regnr, Status.uit_omloop);

    }

    @Override
    public void wijzigenHerstelNaarActief(Integer regnr) throws ApplicationException, DBException, SQLException {
        if (regnr == null) {
            throw new ApplicationException("er werd geen registratienummer meegegeven");
        }

        if (zoekFiets(regnr) == null) {
            throw new ApplicationException("fiets bestaat niet");
        }

        if (zoekFiets(regnr).getStatus() == Status.actief) {
            throw new ApplicationException(" fiets al actief");
        }

        if (zoekFiets(regnr).getStatus() == Status.uit_omloop) {
            throw new ApplicationException(" fiets is niet meer in gerbuik");
        }
        
        

        FietsDB fdb = new FietsDB();
        fdb.wijzigenToestandFiets(regnr, Status.actief);
    }

    @Override
    public void wijzigenHerstelNaarUitOmloop(Integer regnr) throws ApplicationException, DBException, SQLException {
        if (regnr == null) {
            throw new ApplicationException("er werd geen registratienummer meegegeven");
        }

        if (zoekFiets(regnr) == null) {
            throw new ApplicationException("fiets bestaat niet");
        }

        if (zoekFiets(regnr).getStatus() == Status.actief) {
            throw new ApplicationException(" fiets is actief");
        }

        if (zoekFiets(regnr).getStatus() == Status.uit_omloop) {
            throw new ApplicationException(" fiets is al niet meer in gerbuik");
        }

        FietsDB fdb = new FietsDB();
        fdb.wijzigenToestandFiets(regnr, Status.uit_omloop);
    }

    @Override
    public void wijzigenOpmerkingFiets(Integer regnr, String opmerking) throws ApplicationException, DBException, SQLException {
        if (regnr == null || opmerking == null) {
            throw new ApplicationException("regnr of opmerking werd niet meegegeven");
        }

        if (zoekFiets(regnr) == null) {
            throw new ApplicationException("fiets bestaat niet");
        }

        FietsDB fdb = new FietsDB();
        fdb.wijzigenOpmerkingFiets(regnr, opmerking);
    }

    
    @Override
    public Fiets zoekFiets(Integer registratienummer) throws ApplicationException, DBException, SQLException {
        if (registratienummer == null) {
            throw new ApplicationException("er werd geen registratienummer meegegeven");
        }
        FietsDB fdb = new FietsDB();
        
        return fdb.zoekFiets(registratienummer);
    }

    @Override
    public ArrayList<Fiets> zoekAlleFietsen() throws DBException {
        FietsDB fdb = new FietsDB();
        return fdb.zoekAlleFietsen();
    }
    
    public ArrayList<Fiets> zoekAlleBeschikbareFietsen() throws DBException , ApplicationException
    {
        FietsDB fdb = new FietsDB();
        return fdb.zoekAlleBeschikbareFietsen();
    }

}
