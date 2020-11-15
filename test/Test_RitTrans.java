
import databag.Fiets;
import databag.Lid;
import databag.Rit;
import database.FietsDB;
import database.LidDB;
import datatype.Geslacht;
import datatype.Rijksregisternummer;
import datatype.Standplaats;
import datatype.Status;
import exception.ApplicationException;
import org.junit.Before;
import transactie.RitTrans;
import extra.Removals;
import java.time.LocalDate;
import static org.junit.Assert.assertEquals;
import transactie.FietsTrans;
import transactie.LidTrans;
import transactie.RitTrans;
import datatype.Rijksregisternummer;
import exception.DBException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author vdkrobby
 */
public class Test_RitTrans {

    @Rule
    public ExpectedException thrown
            = ExpectedException.none();
    Rit rit;
    Rit rit2;

    //verschillende Leden
    //juist lid
    Lid lid;
    Lid lid2;

    //verschillende fietsen
    Fiets fiets;
    Fiets fiets2;
    Fiets fiets3;
    Fiets fiets4;

    Removals remove;
//toevoegen van bestaande leden en fietsen

    @Before
    public void setUpText() throws ApplicationException, DBException {

        lid = new Lid();
        lid2 = new Lid();
        fiets = new Fiets();
        fiets2 = new Fiets();
        fiets3 = new Fiets();
        fiets4 = new Fiets();

        //correct gemaakte lid
        Rijksregisternummer rr = new Rijksregisternummer("97050224323");
        lid.setRijksregisternummer(rr);
        lid.setNaam("VDK");
        lid.setVoornaam("Robby");
        lid.setEmailadres("robbytester@test.com");
        lid.setTelnr("047455000");
        lid.setGeslacht(Geslacht.M);
        lid.setStart_lidmaatschap(LocalDate.now());
//        rit.setLidRijksregisternummer(rr);//toevoegen van lid aan rit om Rijksregisternummer niet meerdere keren te defineren

        //2de lid 
        Rijksregisternummer rr2 = new Rijksregisternummer("96061446905");
        lid2.setRijksregisternummer(rr2);
        lid2.setNaam("Quentin");
        lid2.setVoornaam("Tester");
        lid2.setEmailadres("qtester@test.com");
        lid2.setTelnr("047455420");
        lid2.setGeslacht(Geslacht.M);
        lid2.setStart_lidmaatschap(LocalDate.now());

        //correct gemaakte fiets
        fiets.setRegistratienummer(1);
        fiets.setStandplaats(Standplaats.Brugge);
        fiets.setStatus(Status.actief);
        fiets2.setOpmerking("Fiets is actief");

        //2de fiets
        fiets4.setRegistratienummer(4);
        fiets4.setStandplaats(Standplaats.Kortrijk);
        fiets4.setStatus(Status.actief);
        fiets4.setOpmerking("Fiets is actief");

        //fiets herstelling
        fiets2.setRegistratienummer(2);
        fiets2.setStandplaats(Standplaats.Tielt);
        fiets2.setStatus(Status.herstel);
        fiets2.setOpmerking("Fiets in herstelling");

        //fiets uitomloop
        fiets3.setRegistratienummer(3);
        fiets3.setStandplaats(Standplaats.Oostende);
        fiets3.setStatus(Status.uit_omloop);
        fiets3.setOpmerking("Fiets uit omloop");

        //toevoegen van fiets en lid in de database
    }
    
    @After //wordt herhaald na elke test, bij deze is de finally clausule niet meer nodig, maar we hebben ze niet aangepast
    public void VerwijderAlles() throws Exception
    {
        Removals.removeAll();
    }
    
    //toevoegen RIT
    //Positief

    @Test
    public void testToevoegenRit() throws Exception {
        try {
            RitTrans rt = new RitTrans();
            FietsTrans ft = new FietsTrans();
            LidTrans lt = new LidTrans();
            rit = new Rit();

            ft.toevoegenFiets(fiets);
            lt.toevoegenLid(lid);

            Rijksregisternummer regnr = new Rijksregisternummer(lid.getRijksregisternummer());
            //rit aanamaken

            rit.setFietsRegistratienummer(fiets.getRegistratienummer());
            rit.setLidRijksregisternummer(regnr);
            rit.setStarttijd(LocalDateTime.MAX);
            //id opslaan van de toegevoegde rit & rit toevoegen
            int idrit = rt.toevoegenRit(rit);
            Rit opgehaalderit = rt.zoekRit(idrit);
            assertEquals("Rit aangemaakt", rt.zoekRit(idrit).getStarttijd(), opgehaalderit.getStarttijd());
        } finally {
            Removals.removeAll();
        }
    }

    @Test
    public void testToevoegenRitMetBestaandeFiets() throws Exception {
        try {
            RitTrans rt = new RitTrans();
            FietsTrans ft = new FietsTrans();
            LidTrans lt = new LidTrans();
            rit = new Rit();

            ft.toevoegenFiets(fiets);
            lt.toevoegenLid(lid);

            Rijksregisternummer regnr = new Rijksregisternummer(lid.getRijksregisternummer());
            //rit aanamaken

            rit.setFietsRegistratienummer(fiets.getRegistratienummer());
            rit.setLidRijksregisternummer(regnr);
            rit.setStarttijd(LocalDateTime.MAX);
            //id opslaan van de toegevoegde rit & rit toevoegen
            int idrit = rt.toevoegenRit(rit);
            Fiets opgehaaldefiets = ft.zoekFiets(fiets.getRegistratienummer());
            assertEquals("Fiets bestaat", rt.zoekRit(idrit).getFietsRegistratienummer(), opgehaaldefiets.getRegistratienummer().intValue());
        } finally {
            Removals.removeAll();
        }
    }

    @Test
    public void testToevoegenRitMetBeschikbareFiets() throws Exception {
        try {
            RitTrans rt = new RitTrans();
            FietsTrans ft = new FietsTrans();
            LidTrans lt = new LidTrans();
            rit = new Rit();

            ft.toevoegenFiets(fiets);
            lt.toevoegenLid(lid);

            Rijksregisternummer regnr = new Rijksregisternummer(lid.getRijksregisternummer());
            //rit aanamaken

            rit.setFietsRegistratienummer(fiets.getRegistratienummer());
            rit.setLidRijksregisternummer(regnr);
            rit.setStarttijd(LocalDateTime.MAX);
            //id opslaan van de toegevoegde rit & rit toevoegen
            int idrit = rt.toevoegenRit(rit);
            assertEquals(rt.zoekRit(idrit).getFietsRegistratienummer(), fiets.getRegistratienummer().intValue());
//            assertEquals("Fiets is beschikbaar",rt.zoekRit(idrit).getFietsRegistratienummer(), fiets.getStatus().actief);
        } finally {
            Removals.removeAll();
        }
    }

    @Test
    public void testToevoegenRitMetFietsDieGeenActieveRitHeeft() throws Exception {
        try {
            RitTrans rt = new RitTrans();
            FietsTrans ft = new FietsTrans();
            LidTrans lt = new LidTrans();
            rit = new Rit();

            ft.toevoegenFiets(fiets);
            lt.toevoegenLid(lid);

            Rijksregisternummer regnr = new Rijksregisternummer(lid.getRijksregisternummer());
            //rit aanamaken
            rit.setFietsRegistratienummer(fiets.getRegistratienummer());
            rit.setLidRijksregisternummer(regnr);
            rit.setStarttijd(LocalDateTime.MAX);
            //fiets ophalen en voor toevoegen aan een rit gaan kijken of de fiets al in een rit wordt opgenomen
            Fiets opgehaaldefiets = ft.zoekFiets(fiets.getRegistratienummer());
            assertEquals(rt.zoekActieveRittenVanFiets(opgehaaldefiets.getRegistratienummer()).size(), 0);
            //na te gaan testen of de fiets al opgenomen wordt, de rit gaan toevoegen met de gevraagde fiets.
            int idrit = rt.toevoegenRit(rit);
            Rit opgehaalderit = rt.zoekRit(idrit);
            assertEquals("Rit is toegevoegd", rt.zoekRit(idrit).getRitID(), opgehaalderit.getRitID());

        } finally {
            Removals.removeAll();
        }
    }

    @Test
    public void testToevoegenRitMetLidDieGeenActieveRitHeeft() throws Exception {
        try {
            RitTrans rt = new RitTrans();
            FietsTrans ft = new FietsTrans();
            LidTrans lt = new LidTrans();
            rit = new Rit();

            ft.toevoegenFiets(fiets);
            lt.toevoegenLid(lid);

            Rijksregisternummer regnr = new Rijksregisternummer(lid.getRijksregisternummer());
            //rit aanamaken
            rit.setFietsRegistratienummer(fiets.getRegistratienummer());
            rit.setLidRijksregisternummer(regnr);

            //lid ophalen en voor toevoegen aan een rit gaan kijken of de fiets al in een rit wordt opgenomen
            Lid opgehaaldelid = lt.zoekLid(lid.getRijksregisternummer());
            assertEquals(rt.zoekActieveRittenVanLid(opgehaaldelid.getRijksregisternummer()).size(), 0);
            //na te gaan testen of de fiets al opgenomen wordt, de rit gaan toevoegen met de gevraagde fiets.
            int idrit = rt.toevoegenRit(rit);
            Rit opgehaalderit = rt.zoekRit(idrit);
            assertEquals("Rit is toegevoegd", rt.zoekRit(idrit).getRitID(), opgehaalderit.getRitID());
        } finally {
            Removals.removeAll();
        }
    }

    public void testToevoegenRitMetIngeschrevenLid() throws Exception {
        try {
            RitTrans rt = new RitTrans();
            FietsTrans ft = new FietsTrans();
            LidTrans lt = new LidTrans();
            rit = new Rit();

            ft.toevoegenFiets(fiets);
            lt.toevoegenLid(lid);

            Rijksregisternummer regnr = new Rijksregisternummer(lid.getRijksregisternummer());
            //rit aanamaken

            rit.setFietsRegistratienummer(fiets.getRegistratienummer());
            rit.setLidRijksregisternummer(regnr);
            //id opslaan van de toegevoegde rit & rit toevoegen
            int idrit = rt.toevoegenRit(rit);

            Lid opgehaaldelid = lt.zoekLid(lid.getRijksregisternummer());
            // assertEquals("Lid is ingeschreven");
        } finally {
            Removals.removeAll();
        }
    }

    //Negatief
    @Test
    public void testToevoegenZonderRit() throws Exception {
        thrown.expect(ApplicationException.class);
        thrown.expectMessage("er werd geen rit meegegeven");
        try {
            RitTrans rt = new RitTrans();
            rit = new Rit();
            rt.toevoegenRit(null);
        } finally {
            Removals.removeAll();
        }
    }

    @Test
    public void testTeovoegenRitZonderBestaandeFiets() throws Exception {
        thrown.expect(ApplicationException.class);
        thrown.expectMessage("meegegeven fiets bestaat niet");
        try {
            RitTrans rt = new RitTrans();
            FietsTrans ft = new FietsTrans();
            LidTrans lt = new LidTrans();
            rit = new Rit();
//enkel een lid toevoegen 
            lt.toevoegenLid(lid);

            Rijksregisternummer regnr = new Rijksregisternummer(lid.getRijksregisternummer());
            //rit aanamaken

            rit.setFietsRegistratienummer(fiets.getRegistratienummer());
            rit.setLidRijksregisternummer(regnr);
            //id opslaan van de toegevoegde rit & rit toevoegen
            int idrit = rt.toevoegenRit(rit);
        } finally {
            Removals.removeAll();
        }
    }

    @Test
    public void testFietsUitOmloop() throws DBException, ApplicationException, Exception {
        thrown.expect(ApplicationException.class);
        thrown.expectMessage("meegegeven fiets is niet beschikbaar");
        try {
            rit = new Rit();
            RitTrans rt = new RitTrans();
            FietsTrans ft = new FietsTrans();
            LidTrans lt = new LidTrans();

            ft.toevoegenFiets(fiets3);
            lt.toevoegenLid(lid);

            Rijksregisternummer regnr = new Rijksregisternummer(lid.getRijksregisternummer());
            //rit aanamaken

            rit.setFietsRegistratienummer(fiets3.getRegistratienummer());
            rit.setLidRijksregisternummer(regnr);

            //id opslaan van de toegevoegde rit & rit toevoegen
            int idrit = rt.toevoegenRit(rit);

            assertEquals(rt.zoekRit(idrit).getFietsRegistratienummer(), fiets3.getRegistratienummer().intValue());
            assertEquals("Fiet is uit omloop ", rt.zoekRit(idrit).getFietsRegistratienummer(), fiets3.getStatus());
        } finally {
            Removals.removeAll();
        }

    }

    @Test
    public void testFietsInHerstel() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage("meegegeven fiets is niet beschikbaar");
        try {
            rit = new Rit();
            RitTrans rt = new RitTrans();
            FietsTrans ft = new FietsTrans();
            LidTrans lt = new LidTrans();

            ft.toevoegenFiets(fiets2);
            lt.toevoegenLid(lid);

            Rijksregisternummer regnr = new Rijksregisternummer(lid.getRijksregisternummer());
            //rit aanamaken

            rit.setFietsRegistratienummer(fiets2.getRegistratienummer());
            rit.setLidRijksregisternummer(regnr);
            //id opslaan van de toegevoegde rit & rit toevoegen
//            rt.toevoegenRit(rit);
            int idrit = rt.toevoegenRit(rit);

//            assertEquals(rt.zoekRit(idrit).getFietsRegistratienummer(), fiets2.getRegistratienummer().intValue());
//            assertEquals("Fiet in herstelling ", rt.zoekRit(idrit).getFietsRegistratienummer(), fiets2.getStatus());
        } finally {
            Removals.removeAll();
        }

    }

    @Test
    public void testToevoegenRitMetFietsDieActieveRitHeeft() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage("meegegeven fiets heeft al een actieve rit");
        try {
            rit = new Rit();
            rit2 = new Rit();
            RitTrans rt = new RitTrans();
            FietsTrans ft = new FietsTrans();
            LidTrans lt = new LidTrans();

            ft.toevoegenFiets(fiets);
            lt.toevoegenLid(lid);
            lt.toevoegenLid(lid2);

            Rijksregisternummer regnr = new Rijksregisternummer(lid.getRijksregisternummer());
            Rijksregisternummer regnr2 = new Rijksregisternummer(lid2.getRijksregisternummer());
            //rit 1 met fiets
            rit.setFietsRegistratienummer(fiets.getRegistratienummer());
            rit.setLidRijksregisternummer(regnr);
            //rit 2 met dezelfde fiets
            rit2.setFietsRegistratienummer(fiets.getRegistratienummer());
            rit2.setLidRijksregisternummer(regnr2);

            rt.toevoegenRit(rit);
            rt.toevoegenRit(rit2);

        } finally {
            Removals.removeAll();
        }
    }

    @Test
    public void testToevoegenRitMetLidDieActieveRitHeeft() throws Exception {
        thrown.expect(ApplicationException.class);
        thrown.expectMessage("meegegeven lid heeft al een actieve rit");
        try {
            rit = new Rit();
            rit2 = new Rit();
            RitTrans rt = new RitTrans();
            FietsTrans ft = new FietsTrans();
            LidTrans lt = new LidTrans();

            ft.toevoegenFiets(fiets);
            ft.toevoegenFiets(fiets4);
            lt.toevoegenLid(lid);
            Rijksregisternummer regnr = new Rijksregisternummer(lid.getRijksregisternummer());
            //rit 1 aanamaken
            rit.setFietsRegistratienummer(fiets.getRegistratienummer());
            rit.setLidRijksregisternummer(regnr);
            //rit 2 aanamaken met zelfde gebruiker die actief is in rit 1
            rit2.setFietsRegistratienummer(fiets4.getRegistratienummer());
            rit2.setLidRijksregisternummer(regnr);

            int idrit1 = rt.toevoegenRit(rit);
            int idrit2 = rt.toevoegenRit(rit2);
        } finally {
            Removals.removeAll();
        }
    }

    @Test
    public void testToevoegenRitMetUitgeschrevenLid() throws Exception {
        thrown.expect(ApplicationException.class);
        thrown.expectMessage("Lid is uitgeschreven");
        try {
            rit = new Rit();
            RitTrans rt = new RitTrans();
            FietsTrans ft = new FietsTrans();
            LidTrans lt = new LidTrans();

            ft.toevoegenFiets(fiets);
            lt.toevoegenLid(lid);
            Rijksregisternummer regnr = new Rijksregisternummer(lid.getRijksregisternummer());
            //uitschrijven van lid 
            lt.uitschrijvenLid("97050224323");
            //rit aanamaken
            rit.setFietsRegistratienummer(fiets.getRegistratienummer());
            rit.setLidRijksregisternummer(regnr);
            //id opslaan van de toegevoegde rit & rit toevoegen
            int idrit = rt.toevoegenRit(rit);

        } finally {
            Removals.removeAll();
        }
    }

    //afsluiten RIT
    //positief
    @Test
    public void testAfsluitenRitMetMeegevenVanRitID() throws Exception {
        try {
            rit = new Rit();
            RitTrans rt = new RitTrans();
            FietsTrans ft = new FietsTrans();
            LidTrans lt = new LidTrans();

            ft.toevoegenFiets(fiets);
            lt.toevoegenLid(lid);

            Rijksregisternummer regnr = new Rijksregisternummer(lid.getRijksregisternummer());

            rit.setFietsRegistratienummer(fiets.getRegistratienummer());
            rit.setLidRijksregisternummer(regnr);

            int idrit = rt.toevoegenRit(rit);
            rt.afsluitenRit(idrit);
            //assertEquals(rt.afsluitenRit(idrit),idrit);

        } finally {
            Removals.removeAll();
        }
    }

    @Test
    public void testAfsluitenRitVanBestaandeRit() throws Exception {
        try {
            rit = new Rit();
            rit2 = new Rit();
            RitTrans rt = new RitTrans();
            FietsTrans ft = new FietsTrans();
            LidTrans lt = new LidTrans();

            ft.toevoegenFiets(fiets);
            lt.toevoegenLid(lid);

            Rijksregisternummer regnr = new Rijksregisternummer(lid.getRijksregisternummer());

            rit.setFietsRegistratienummer(fiets.getRegistratienummer());
            rit.setLidRijksregisternummer(regnr);

            int idrit = rt.toevoegenRit(rit);
            Rit opgehaalderit = rt.zoekRit(idrit);
            assertEquals("rit bestaat", rt.zoekRit(idrit).getRitID(), opgehaalderit.getRitID());
            rt.afsluitenRit(idrit);
            Rit afgelostenrit = rt.zoekRit(idrit);
            assertEquals("rit afgesloten", rt.zoekRit(idrit).getEindtijd(), afgelostenrit.getEindtijd());
        } finally {
            Removals.removeAll();
        }
    }
    //negatief

    @Test
    public void testAfsluitenRitZonderMeegevenVanRitID() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage("er werd geen ID meegegeven");
        try {
            rit = new Rit();
            RitTrans rt = new RitTrans();
            FietsTrans ft = new FietsTrans();
            LidTrans lt = new LidTrans();

            ft.toevoegenFiets(fiets);
            lt.toevoegenLid(lid);

            Rijksregisternummer regnr = new Rijksregisternummer(lid.getRijksregisternummer());

            rit.setFietsRegistratienummer(fiets.getRegistratienummer());
            rit.setLidRijksregisternummer(regnr);

            rt.toevoegenRit(rit);
            rt.afsluitenRit(null);

        } finally {
            Removals.removeAll();
        }

    }

    @Test
    public void testAfsluitenRitVanOnbestaandeRit() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage("gezochte rid bestaat niet");
        try {
            rit = new Rit();
            RitTrans rt = new RitTrans();
            rt.afsluitenRit(1);
        } finally {
            Removals.removeAll();
        }
    }

    @Test
    public void testAfsluitenRitVanRitDieAlIsAfgesloten() throws Exception {
        //afgesloten rit kan niet meer afgesloten worden

        thrown.expect(Exception.class);
        thrown.expectMessage("Aangesproken rit is al afgesloten");
        try {
            rit = new Rit();
            RitTrans rt = new RitTrans();
            FietsTrans ft = new FietsTrans();
            LidTrans lt = new LidTrans();

            ft.toevoegenFiets(fiets);
            lt.toevoegenLid(lid);

            Rijksregisternummer regnr = new Rijksregisternummer(lid.getRijksregisternummer());
            //rit aanmaken
            rit.setFietsRegistratienummer(fiets.getRegistratienummer());
            rit.setLidRijksregisternummer(regnr);
            //id opslaan van de toegevoegde rit & rit toevoegen

            int idrit = rt.toevoegenRit(rit);
            rt.afsluitenRit(idrit);
            Rit opgehaalderit = rt.zoekRit(idrit);
            rt.afsluitenRit(idrit);

            assertEquals(rt.zoekRit(idrit).getFietsRegistratienummer(), fiets.getRegistratienummer().intValue());
            assertEquals("Rit afgesloten ", rt.zoekRit(idrit).getEindtijd(), opgehaalderit.getEindtijd());
        } finally {
            Removals.removeAll();
        }
    }
    //zoek rit
    //positief
    @Test 
    public void testZoekRitMetMeegevenVanRitID() throws Exception {
        try {
            rit = new Rit();
            RitTrans rt = new RitTrans();
            FietsTrans ft = new FietsTrans();
            LidTrans lt = new LidTrans();

            ft.toevoegenFiets(fiets);
            lt.toevoegenLid(lid);

            Rijksregisternummer regnr = new Rijksregisternummer(lid.getRijksregisternummer());

            rit.setFietsRegistratienummer(fiets.getRegistratienummer());
            rit.setLidRijksregisternummer(regnr);

            int idrit = rt.toevoegenRit(rit);
            rt.zoekRit(idrit);
            
            Rit opgevraagderit = rt.zoekRit(idrit);
            assertEquals(rt.zoekRit(idrit).getRitID(),opgevraagderit.getRitID());
        } finally {
                Removals.removeAll();
        }
    }
    //negatief
    @Test
    public void testZoekRitZonderMeegevenVanRitID() throws Exception {
       thrown.expect(Exception.class);
        thrown.expectMessage("er werd geen ritID meegegeven");
        try {
            rit = new Rit();
            RitTrans rt = new RitTrans();
            FietsTrans ft = new FietsTrans();
            LidTrans lt = new LidTrans();

            ft.toevoegenFiets(fiets);
            lt.toevoegenLid(lid);

            Rijksregisternummer regnr = new Rijksregisternummer(lid.getRijksregisternummer());

            rit.setFietsRegistratienummer(fiets.getRegistratienummer());
            rit.setLidRijksregisternummer(regnr);

            int idrit = rt.toevoegenRit(rit);
            rt.zoekRit(null);
        } finally {
                 Removals.removeAll();
        }
    }

    //zoek eerste rit van lid
    //positief
    //negatief
    //zoek atieve ritten van lid
    //positief
    public void testZoekActieveRitVanLidMetMeegevenVanRijksregisternummer() throws Exception {

    }

    public void testZoekActieveRitVanBestaandLid() throws Exception {

    }
    //negatief

    public void testZoekActieveRitVanLidZonderMeegevenVanRijksregisternummer() throws Exception {
    }

    public void testZoekActieveRitVanOnbestaandLid() throws Exception {

    }
    //zoek actieve ritten van fiets
    //positief

    public void testZoekActieveRitVanFietsMetMeegevenVanRegistratienummer() throws Exception {

    }

    public void testZoekActieveRitVanBestaandeFiets() throws Exception {

    }
    //negatief

    public void testZoekActieveRitVanFietsZonderMeegevenVanRegistratienummer() throws Exception {

    }

    public void testZoekActieveRitVanOnbestaandeFiets() throws Exception {

    }

}
