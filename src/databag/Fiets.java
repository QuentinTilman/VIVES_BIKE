
package databag;

import datatype.Standplaats;
import datatype.Status;

public class Fiets {
    
    private Integer registratienummer;
    private Standplaats standplaats;
    private Status status;    
    private String opmerking;
            
    public Fiets() {
    }

    public Standplaats getStandplaats() {
        return standplaats;
    }

    public void setStandplaats(Standplaats standplaats) {
        this.standplaats = standplaats;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status Status) {
        this.status = Status;
    }

    public Integer getRegistratienummer() {
        return registratienummer;
    }

    public void setRegistratienummer(Integer registratienummer) {
        this.registratienummer = registratienummer;
    }

    public String getOpmerking() {
        return opmerking;
    }

    public void setOpmerking(String opmerking) {
        this.opmerking = opmerking;
    }

    @Override
    public String toString() {
        return "registratienummer=" + registratienummer.toString()+ System.lineSeparator()+ "standplaats=" + standplaats.toString() 
                +System.lineSeparator()+ " status=" + status.toString() + System.lineSeparator()+"opmerking="+System.lineSeparator()+ opmerking.toString() ;
    }
    
    
    
}
