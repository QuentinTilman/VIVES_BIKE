
package databag;

import datatype.Geslacht;
import datatype.Rijksregisternummer;
import java.time.LocalDate;

public class Lid {

    private Rijksregisternummer rijksregisternummer;
    private String naam;
    private String voornaam;
    private Geslacht geslacht;
    private String telnr;
    private String emailadres;
    private LocalDate start_lidmaatschap;
    private LocalDate einde_lidmaatschap;
    private String opmerkingen;

    public Lid() {
       
    }

    public String getRijksregisternummer() {
        if (rijksregisternummer == null) {
            return null;
        } else {
            return rijksregisternummer.getRijksregisternummer();
        }
    }

    public void setRijksregisternummer(Rijksregisternummer rijksregisternummer) {
        this.rijksregisternummer = rijksregisternummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    public Geslacht getGeslacht() {
        return geslacht;
    }

    public void setGeslacht(Geslacht geslacht) {
        this.geslacht = geslacht;
    }

    public String getTelnr() {
        return telnr;
    }

    public void setTelnr(String telnr) {
        this.telnr = telnr;
    }

    public String getEmailadres() {
        return emailadres;
    }

    public void setEmailadres(String email) {
        this.emailadres = email;
    }

    public LocalDate getStart_lidmaatschap() {
        return start_lidmaatschap;
    }

    public void setStart_lidmaatschap(LocalDate start_lidmaatschap) {
        this.start_lidmaatschap = start_lidmaatschap;
    }

    public LocalDate getEinde_lidmaatschap() {
        return einde_lidmaatschap;
    }

    public void setEinde_lidmaatschap(LocalDate einde_lidmaatschap) {
        this.einde_lidmaatschap = einde_lidmaatschap;
    }

    public String getOpmerkingen() {
        return opmerkingen;
    }

    public void setOpmerkingen(String opmerkingen) {
        this.opmerkingen = opmerkingen;
    }

    @Override
    public String toString() {
        return "Lid rijksregisternummer=" + rijksregisternummer.getRijksregisternummer() + System.lineSeparator()+ "naam=" + naam + ", voornaam=" + voornaam +System.lineSeparator()+
                " geslacht=" + geslacht +System.lineSeparator()+ " telnr=" + telnr +System.lineSeparator()+
                " email=" + emailadres +System.lineSeparator()+ " start_lidmaatschap=" + start_lidmaatschap +System.lineSeparator()+ " einde_lidmaatschap=" +
                einde_lidmaatschap +System.lineSeparator()+ " opmerkingen="+System.lineSeparator() + opmerkingen;
    }

}
