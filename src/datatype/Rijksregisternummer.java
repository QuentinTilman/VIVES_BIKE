
package datatype;

import exception.ApplicationException;


public class Rijksregisternummer {
    private String rijksregisternummer;
    
    public Rijksregisternummer(String rr)throws ApplicationException{
        //controle correct rijksregisternummer
        try{            
            int deel1 = Integer.parseInt(rr.substring(0, 9));
            int controlegetal = Integer.parseInt(rr.substring(9, 11));
            if((97-(deel1%97)==controlegetal) ||(97-((2000000000+deel1)%97)==controlegetal)){
               rijksregisternummer = rr; 
            }
            else{
                throw new ApplicationException();
            }
        }
        catch(Exception ex){
            throw new ApplicationException("geen geldig rijksregisternummer");
        }
          
    }

    public String getRijksregisternummer() {
        return String.valueOf(rijksregisternummer);
    }
}
