/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.luokkafilet;

/**
 * Laite -luokka
 * @author sauli
 */
public class Laite {
    
    private int laiteId;
    private String kuvaus;
    private int hintaPvm;

    /**
     * Tätä konstruktoria käytetään kun lisätään Laite tietokantaan ja tietokanta generoi sille ID:n
     * @param kuvaus
     * @param hintaPvm 
     */
    public Laite(String kuvaus, int hintaPvm) {
        this.kuvaus = kuvaus;
        this.hintaPvm = hintaPvm;
    }

    /**
     * Tätä konstruktoria käytetään kun haetaan Laite tietokannasta ja sillä on jo generoitu ID
     * @param laiteId
     * @param kuvaus
     * @param hintaPvm 
     */
    public Laite(int laiteId, String kuvaus, int hintaPvm) {
        this.laiteId = laiteId;
        this.kuvaus = kuvaus;
        this.hintaPvm = hintaPvm;
    }

    public int getLaiteId() {
        return laiteId;
    }

    public void setLaiteId(int laiteId) {
        this.laiteId = laiteId;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

    public int getHintaPvm() {
        return hintaPvm;
    }

    public void setHintaPvm(int hintaPvm) {
        this.hintaPvm = hintaPvm;
    }
    
    public String toString() {
        return getKuvaus();
    }
    
}
