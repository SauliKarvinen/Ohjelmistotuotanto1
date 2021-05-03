package vuoto.luokkafilet;

/**
 *  Lasku -luokka
 *  
 *  Laskujen käsittely, käytetyt muuttujat
 *  laskuNro        Int (db asettaa)
 *  laskuntyyppi    String(50) vai 
 *  hinta           int (double??)
 *  varausId        int (haku bd, Varaus class)
 * 
 * @author marko
 */
public class Lasku {
    
    /**
     * Muuttujien määrittely
     * 
     */
    
    private int     laskuNro;
    private String  laskuntyyppi;
    private int     hinta;
    private int     varausId;
        
    /**
     * Tyhjä Lasku konstruktori, uuden laskun luontia varten.
     * Käytetään, kun UusiLasku-paneeli avataan.
     */
    
    public Lasku() {
        // LaskunNro määräytyy vasta kun viedään DB:hen
        this.laskuntyyppi = "";
        this.hinta = 0;
        this.varausId = 0;
    }
    
    /**
     * Konstruktori laskulle, joka viedään kantaan.
     * (a)param laskuNro    Haetaan kannasta
     * @param laskuntyyppi  email tai paperi
     * @param hinta         Lasketaan xxx jostain. Alkuun manuaalisesti
     * (a)param varausNro   Haetaan kannasta
     */
    public Lasku(String laskuntyyppi, int hinta, int varausId){
        // this.laskuNro = getLaskunNro(); // Maaraytyy kannassa
        this.laskuntyyppi = laskuntyyppi;
        this.hinta = hinta;
        // lisätään kun UusiVaraus toiminto lisätty
        this.varausId = varausId;
    }
    
    /**
     * Valmiin laskun käsittelyyn.
     * Haetaan kannasta...
     * @param laskuNro
     * @param laskuntyyppi
     * @param hinta
     * @param  varausId
     */
    public Lasku(int laskuNro, String laskuntyyppi, int hinta, int varausId){
        this.laskuNro = laskuNro;
        this.laskuntyyppi = laskuntyyppi;
        this.hinta = hinta;
        this.varausId = varausId;
    }
    
    /** 
     *  GETTERS SETTERS
     */

     // LaskuNro käsittely
    public int getLaskunNro(){
        return laskuNro;
    }
    
    public void setLaskunNro(int laskuNro){
        this.laskuNro = laskuNro;
    }

    // Laskun tyyppi
    public String getLaskunTyyppi(){
        return laskuntyyppi;
    }
    
    public void setLaskunTyyppi(String laskuntyyppi){
        this.laskuntyyppi = laskuntyyppi;
    }
    
    // Laskun hinta
    public int getHinta(){
        return hinta;
    }
    
    public void setHinta(int hinta){
        this.hinta = hinta;
    }
    
    // Laskun varausId
    public int getVarausId(){
        return varausId;
    }
    
    public void setVarausId(int varausId){
        this.varausId = varausId;
    }
    
    public String toString() {
        return getLaskunTyyppi();
    }
}
