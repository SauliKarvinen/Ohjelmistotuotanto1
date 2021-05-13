/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.luokkafilet;

/**
 *
 * @author marko
 */
public class LaskuOlio {
    private int laskuNro;
    private int hinta;
    private int varausId;
    private String laskuntyyppi;

    public LaskuOlio(int laskuNro, int hinta, int varausId, String laskuntyyppi) {
        this.laskuNro = laskuNro;
        this.hinta = hinta;
        this.varausId = varausId;
        this.laskuntyyppi = laskuntyyppi;
    }

    public int getLaskuNro() {
        return laskuNro;
    }

    public void setLaskuNro(int laskuNro) {
        this.laskuNro = laskuNro;
    }

    public int getHinta() {
        return hinta;
    }

    public void setHinta(int hinta) {
        this.hinta = hinta;
    }

    public int getVarausId() {
        return varausId;
    }

    public void setVarausId(int varausId) {
        this.varausId = varausId;
    }

    public String getLaskuntyyppi() {
        return laskuntyyppi;
    }

    public void setLaskuntyyppi(String laskuntyyppi) {
        this.laskuntyyppi = laskuntyyppi;
    }
    
    
    
}
