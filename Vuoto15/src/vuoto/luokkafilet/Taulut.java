package vuoto.luokkafilet;

import vuoto.luokkafilet.Toimipiste;
import vuoto.luokkafilet.Asiakas;
import vuoto.luokkafilet.Laite;
import vuoto.luokkafilet.Lasku;
import vuoto.luokkafilet.Palvelu;
import vuoto.luokkafilet.Toimitila;
import vuoto.luokkafilet.Varaus;

/**
 *
 * @author marko
 */
public class Taulut {
    
    /**
     * Luokka Laskun TblVu-näkymää varten:
     * int laskuNro
     * String laskuntyyppi  (email/lasku)
     * String yritysNimi (asiakas)
     * int toimitila    (varattu kohde)
     * int varausId
     * 
     */
    
   int laskuNro;
   String laskuntyyppi;
   String yritysNimi;
   String toimitila;
   int varausId;

   /**
    * Olio Lasku -paneelin Laskut tblvu:n sarakkeille.
    * 
    * @param l Lasku -luokka
    * @param a Asiakas -luokka
    * @param v Varaus  -luokka
    * @param t Toimitila  -luokka
    */
    public void LaskutTauluOlio(Lasku l, Asiakas a, Varaus v, Toimitila t) {
        this.laskuNro = l.getLaskunNro();
        this.laskuntyyppi = l.getLaskunTyyppi();
        this.yritysNimi = a.getYrityksenNimi();
        this.toimitila = t.getTilanNimi();
        this.varausId = v.getVarausId();
        
    }

   
   
   
      
   
}
