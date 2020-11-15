
import databag.Fiets;
import databag.Lid;
import databag.Rit;
import database.FietsDB;
import database.LidDB;
import database.RitDB;
import datatype.Geslacht;
import datatype.Rijksregisternummer;
import datatype.Standplaats;
import datatype.Status;
import exception.ApplicationException;
import exception.DBException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import extra.Removals;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import extra.Removals;
import java.time.LocalDateTime;
import org.junit.After;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Quent
 */
public class Test_RitDB {

    //Verschillende ritten
    Rit rit;
    Rit ritZonderLid;
    Rit ritZonderFiets;

    //verschillende Leden
    Lid lid;
    Lid lidNietInDB;

    //verschillende fietsen
    Fiets fiets;
    Fiets fietsNietInDB;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @After
    public void removalAfter() throws DBException, ApplicationException {
        Removals.removeAll();
    }

    @Before
    public void setUpTest() throws ApplicationException, DBException, SQLException {

        lid = new Lid();
        fiets = new Fiets();
        rit = new Rit();
        ritZonderLid = new Rit();
        ritZonderFiets = new Rit();

        //correct gemaakte lid
        Rijksregisternummer rr = new Rijksregisternummer("96061446905");
        lid.setRijksregisternummer(rr);
        lid.setNaam("test");
        lid.setVoornaam("test");
        lid.setEmailadres("test@test.com");
        lid.setTelnr("047455000");
        lid.setGeslacht(Geslacht.M);
        lid.setStart_lidmaatschap(LocalDate.now());

        rit.setLidRijksregisternummer(rr);//toevoegen van lid aan rit om Rijksregisternummer niet meerdere keren te defineren

        //Fout rit 
        ritZonderFiets.setLidRijksregisternummer(rr); //toevoegen van lid

        //correct gemaakte fiets
        fiets.setRegistratienummer(1);
        fiets.setStandplaats(Standplaats.Brugge);
        fiets.setStatus(Status.actief);

    }

    //POSITIEF TESTEN
    //testen op lid
    //testen op fiets
    //testen op rit
    @Test
    public void testRitToevoegen() throws Exception {

        try {
            RitDB ritDB = new RitDB();
            LidDB lidDB = new LidDB();
            FietsDB fietsDB = new FietsDB();

            lidDB.toevoegenLid(lid);
            fiets.setRegistratienummer(fietsDB.toevoegenFiets(fiets));
            rit.setFietsRegistratienummer(fiets.getRegistratienummer());
            int id = ritDB.toevoegenRit(rit);
            rit.setRitID(id);
            Rit opgehaalderit = ritDB.zoekRit(rit.getRitID());
            
            int regnr = fiets.getRegistratienummer();
            
            assertEquals(regnr, opgehaalderit.getFietsRegistratienummer());
            assertEquals(lid.getRijksregisternummer(), opgehaalderit.getLidRijksregisternummer());
            assertEquals(rit.getRitID(), opgehaalderit.getRitID());
            

        } finally {
            Removals.removeAll();
        }

    }

    //rekening toevoegen
    @Test
    public void testRitToevoegenMetID() throws Exception {
        try {
            RitDB rdb = new RitDB();
            LidDB lidDB = new LidDB();
            FietsDB fietsDB = new FietsDB();

            lidDB.toevoegenLid(lid);
            fietsDB.toevoegenFiets(fiets);

            rit.setRitID(2);
            rit.setFietsRegistratienummer(fiets.getRegistratienummer());
            int juisteId = rdb.toevoegenRit(rit);
            Rit opgehaaldeRit = rdb.zoekRit(juisteId);

            assertNotEquals("Id niet verandert", rit.getRitID(), opgehaaldeRit.getRitID()); //testen of meegegeven id wel verandert wordt door database
            assertEquals("Database heeft geen correcte id gegeven aan rit", juisteId, opgehaaldeRit.getRitID().intValue());//testen of gekozen id van database goed geattribueert wordt

        } finally {
            Removals.removeAll();
        }
    }

    //rit afsluiten
    @Test
    public void testAfsluitenRit() throws Exception {
        try {
            
            RitDB ritDB = new RitDB();
            LidDB lidDB = new LidDB();
            FietsDB fietsDB = new FietsDB();
            lidDB.toevoegenLid(lid);
            fiets.setRegistratienummer(fietsDB.toevoegenFiets(fiets));
            
            rit.setFietsRegistratienummer(fiets.getRegistratienummer());
            int id = ritDB.toevoegenRit(rit);
            rit.setRitID(id);
            ritDB.afsluitenRit(rit);
            rit.setEindtijd(LocalDateTime.now());

            assertNotEquals("afsluit tijd is niet correct",null, ritDB.zoekRit(id).getEindtijd().toString());
           
        } finally {
            Removals.removeAll();
        }

    }

    //NEGATIEF TESTEN
    //testen op lid
    @Test
    public void TestRitToevoegenZonderLid() throws Exception {

        thrown.expect(DBException.class);

        RitDB ritDB = new RitDB();
        ritZonderLid.setFietsRegistratienummer(fiets.getRegistratienummer());
        ritZonderLid.setLidRijksregisternummer(null);
        ritDB.toevoegenRit(ritZonderLid);

    }

    @Test
    public void testLidNietInDB() throws Exception {
        try {
            thrown.expect(DBException.class);

            RitDB rdb = new RitDB();
            FietsDB fdb = new FietsDB();
            fdb.toevoegenFiets(fiets);

            rit.setFietsRegistratienummer(fiets.getRegistratienummer());
            rdb.toevoegenRit(rit);
        } finally {
            Removals.removeFiets(fiets.getRegistratienummer());
        }

    }

    // testen op Rit
    @Test
    public void ToevoegenVanZelfdeRit() throws Exception {
        try {
            RitDB ritDB = new RitDB();
            FietsDB fdb = new FietsDB();
            LidDB ldb = new LidDB();

            ldb.toevoegenLid(lid);
            rit.setFietsRegistratienummer(fdb.toevoegenFiets(fiets));

            int rit1 = ritDB.toevoegenRit(rit);
            int rit2 = ritDB.toevoegenRit(rit);
            assertNotEquals(rit1, rit2);
        } finally {
            Removals.removeAll();
        }

    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testAfsluitenOnbestaandeRit() throws Exception {
        thrown.expect(NullPointerException.class);
        RitDB rdb = new RitDB();
        rit.setFietsRegistratienummer(fiets.getRegistratienummer());
        rit.setRitID(1);
        rdb.afsluitenRit(rit);
        rdb.zoekRit(1).getEindtijd();
    }

    //testen op fiets
    //toevoegen van een Rit zonder fiets
    @Test
    public void testRitGeenFiets() throws Exception {
        thrown.expect(DBException.class);

        RitDB ritDB = new RitDB();
        ritDB.toevoegenRit(ritZonderFiets);
    }

    //negatief : toevoegen van een rit met ongeldige fiets id
    @Test
    public void testOngeldigeFietsId() throws Exception {
        thrown.expect(DBException.class);
        try {
            RitDB ritDB = new RitDB();
            rit.setFietsRegistratienummer(-1);
            assertNull(ritDB.toevoegenRit(rit));
        } finally {
            Removals.removeAll();
        }

    }

    //Testen op Rit
    
    @Test
    public void testZoekOnbestaandeRit() throws Exception {
        try {

            RitDB rdb = new RitDB();
            assertNull(rdb.zoekRit(1));
        }
        finally
        {
            Removals.removeAll();
        }

    }

    @Test
    public void testZoekRitNull() throws Exception {
        try {
            RitDB rdb = new RitDB();
            assertNull("Fout: rit moet niet gevonden zijn", rdb.zoekRit(null));
        } finally {
            Removals.removeAll();
        }

    }
    
    @Test
    public void testZoekActieveRittenFietsNull() throws Exception
    {
        RitDB rdb = new RitDB();
        assertEquals(null , rdb.zoekActieveRittenVanFiets(null));
    }
    
}
