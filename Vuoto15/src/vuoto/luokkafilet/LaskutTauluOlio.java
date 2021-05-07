package vuoto.luokkafilet;

/**
 *
 * @author marko
 */
public class LaskutTauluOlio {
    
    /**
     * Luokka Laskun TblVu-näkymää varten:
     * int laskuNro
     * String laskuntyyppi  (email/lasku)
     * String yritysNimi (asiakas)
     * int toimitila    (varattu kohde)
     * int varausId
     * 
     */
    
   private int laskuNro;
   private String laskuntyyppi;
   private String yritysNimi;
   private String toimitila;
   private int varausId;

   /**
    * Olio Lasku -paneelin Laskut tblvu:n sarakkeille.
    * 
     * @param laskuNro
     * @param laskuntyyppi
     * @param yritysNimi
     * @param toimitila
     * @param varausId
    */
    public LaskutTauluOlio(int laskuNro, String laskuntyyppi, String yritysNimi, String toimitila, int varausId ) {
        this.laskuNro = laskuNro;
        this.laskuntyyppi = laskuntyyppi;
        this.yritysNimi = yritysNimi;
        this.toimitila = toimitila;
        this.varausId = varausId;
        
    }

    public int getLaskuNro() {
        return laskuNro;
    }

    public void setLaskuNro(int laskuNro) {
        this.laskuNro = laskuNro;
    }

    public String getLaskuntyyppi() {
        return laskuntyyppi;
    }

    public void setLaskuntyyppi(String laskuntyyppi) {
        this.laskuntyyppi = laskuntyyppi;
    }

    public String getYritysNimi() {
        return yritysNimi;
    }

    public void setYritysNimi(String yritysNimi) {
        this.yritysNimi = yritysNimi;
    }

    public String getToimitila() {
        return toimitila;
    }

    public void setToimitila(String toimitila) {
        this.toimitila = toimitila;
    }

    public int getVarausId() {
        return varausId;
    }

    public void setVarausId(int varausId) {
        this.varausId = varausId;
    }

   
   
   
      
   
}
