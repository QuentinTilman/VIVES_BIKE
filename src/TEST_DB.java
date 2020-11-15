
import databag.Fiets;
import databag.Lid;
import databag.Rit;
import database.FietsDB;
import database.LidDB;
import database.RitDB;
import database.connect.ConnectionManager;
import datatype.Geslacht;
import datatype.Rijksregisternummer;
import datatype.Standplaats;
import datatype.Status;
import exception.ApplicationException;
import exception.DBException;
import static java.math.BigDecimal.TEN;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TEST_DB {

    public static void main(String args[]) throws ApplicationException, DBException, SQLException, Exception {
//
        Lid lid = new Lid();
        Rijksregisternummer r = new Rijksregisternummer("96061446905");
        lid.setNaam("test");
        lid.setVoornaam("test");
        lid.setGeslacht(Geslacht.M);
        lid.setTelnr("0004746456165");
        lid.setEmailadres("test@test.com");
        lid.setRijksregisternummer(r);
        lid.setStart_lidmaatschap(LocalDate.now());
        lid.setOpmerkingen("test");
        System.out.println(lid.getNaam());
        LidDB db = new LidDB();
        db.toevoegenLid(lid);
        
        
        lid.setNaam("testwijzigen");
        
        db.wijzigenLid(lid);
        System.out.println(db.zoekAlleLeden());
        db.uitschrijvenLid("96061446905");
        
        System.out.println(db.zoekLid("96061446905"));
      
        Fiets fiets = new Fiets();
        fiets.setRegistratienummer(1);
        fiets.setStatus(Status.actief);
        fiets.setStandplaats(Standplaats.Tielt);
        fiets.setOpmerking("test");
        
        FietsDB fdb = new FietsDB();
        fdb.toevoegenFiets(fiets);
        System.out.println(fdb.zoekFiets(1));
      
        fdb.wijzigenOpmerkingFiets(1, "testwijzigen opmerking werkt");
        fdb.wijzigenToestandFiets(1, Status.herstel);
        System.out.println( fdb.zoekAlleFietsen());
        
        Rit rit = new Rit();
        rit.setRitID(1);
        rit.setStarttijd(LocalDateTime.now());
        int prijs=10;
        rit.setPrijs(TEN);
        rit.setEindtijd(null);
        rit.setLidRijksregisternummer(r);
        rit.setFietsRegistratienummer(1);
//        
//        System.out.println(rit.toString());
//        
        RitDB rdb= new RitDB();
       rdb.toevoegenRit(rit);
        System.out.println( rdb.zoekActieveRittenVanFiets(1));
        System.out.println(rdb.zoekActieveRittenVanLid("96061446905"));
       System.out.println(rdb.zoekEersteRitVanLid("96061446905"));
       rdb.afsluitenRit(rit);
        
        
        
        
        
        

    }
}
