/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.luokkafilet;

import java.util.Objects;

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.laiteId;
        hash = 89 * hash + Objects.hashCode(this.kuvaus);
        hash = 89 * hash + this.hintaPvm;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Laite other = (Laite) obj;
        if (this.laiteId != other.laiteId) {
            return false;
        }
        if (this.hintaPvm != other.hintaPvm) {
            return false;
        }
        if (!Objects.equals(this.kuvaus, other.kuvaus)) {
            return false;
        }
        return true;
    }
    
    
    
}
