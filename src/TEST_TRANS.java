
import databag.Fiets;
import databag.Lid;
import databag.Rit;
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
import transactie.FietsTrans;
import transactie.LidTrans;
import transactie.RitTrans;

public class TEST_TRANS {

    public static void main(String args[]) throws ApplicationException, DBException, SQLException, Exception {

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

        Fiets fiets = new Fiets();
        fiets.setRegistratienummer(1);
        fiets.setStatus(Status.actief);
        fiets.setStandplaats(Standplaats.Tielt);
        fiets.setOpmerking("test");

        Rit rit = new Rit();
        rit.setRitID(1);
        rit.setStarttijd(LocalDateTime.now());
        int prijs = 10;
        rit.setPrijs(TEN);
        rit.setEindtijd(null);
        rit.setLidRijksregisternummer(r);
        rit.setFietsRegistratienummer(1);

        LidTrans lT = new LidTrans();
        FietsTrans fT = new FietsTrans();
        RitTrans rt = new RitTrans();

        lT.toevoegenLid(lid);
        fT.toevoegenFiets(fiets);
        rt.toevoegenRit(rit);
        lid.setOpmerkingen("test wijzigen Trans");
        lT.wijzigenLid(lid);
//        lT.uitschrijvenLid(lid.getRijksregisternummer());
        
        fT.wijzigenActiefNaarHerstel(fiets.getRegistratienummer());
        fT.wijzigenOpmerkingFiets(fiets.getRegistratienummer(), "aanpassen status en oprmerking Trans");
        fT.wijzigenHerstelNaarActief(fiets.getRegistratienummer());
        fT.wijzigenActiefNaarUitOmloop(fiets.getRegistratienummer());
        fT.wijzigenOpmerkingFiets(fiets.getRegistratienummer(), "alle methodes werken correct op TransFiets");
        rt.zoekEersteRitVanLid(lid.getRijksregisternummer());
        rt.zoekActieveRittenVanFiets(fiets.getRegistratienummer());
        rt.zoekActieveRittenVanLid(lid.getRijksregisternummer());
        rt.afsluitenRit(rit.getRitID());
        rt.zoekAlleRitten();
        

    }
}
