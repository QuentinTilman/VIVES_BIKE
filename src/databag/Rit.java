
package databag;

import datatype.Rijksregisternummer;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Rit {

    private Integer ritID;
    private LocalDateTime starttijd;
    private LocalDateTime eindtijd;
    private BigDecimal prijs;
    private Rijksregisternummer lidRijksregisternummer;
    private int fietsRegistratienummer;

    public Rit() {
    }

    public Integer getRitID() {
        return ritID;
    }

    public void setRitID(Integer RitID) {
        this.ritID = RitID;
    }

    public LocalDateTime getStarttijd() {
        return starttijd;
    }

    public void setStarttijd(LocalDateTime starttijd) {
        this.starttijd = starttijd;
    }

    public LocalDateTime getEindtijd() {
        return eindtijd;
    }

    public void setEindtijd(LocalDateTime eindtijd) {
        this.eindtijd = eindtijd;
    }

    public BigDecimal getPrijs() {
        return prijs;
    }

    public void setPrijs(BigDecimal prijs) {
        this.prijs = prijs;
    }

    public String getLidRijksregisternummer() {
        if (lidRijksregisternummer == null) {
            return null;
        } else {
            return lidRijksregisternummer.getRijksregisternummer();
        }
    }

    public void setLidRijksregisternummer(Rijksregisternummer lidRijksregisternummer) {
        this.lidRijksregisternummer = lidRijksregisternummer;
    }

    public int getFietsRegistratienummer() {
        return fietsRegistratienummer;
    }

    public void setFietsRegistratienummer(int fietsRegistratienummer) {
        this.fietsRegistratienummer = fietsRegistratienummer;
    }



}
