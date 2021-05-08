/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.luokkafilet;

import java.util.Objects;

/**
 * Palvelu -luokka
 * @author sauli
 */
public class Palvelu {
    
    private int PalveluId;
    private int hintaPvm;
    private String kuvaus;

    /**
     * Tätä konstruktoria käytetään kun lisätään Palvelu tietokantaan ja tietokanta generoi sille ID:n
     * @param hintapvm
     * @param kuvaus 
     */
    public Palvelu(int hintapvm, String kuvaus) {
        this.hintaPvm = hintapvm;
        this.kuvaus = kuvaus;
    }

    /**
     * Tätä konstruktoria käytetään kun haetaan Palvelu tietokannasta ja sillä on jo generoitu ID
     * @param PalveluId
     * @param hintapvm
     * @param kuvaus 
     */
    public Palvelu(int PalveluId, int hintapvm, String kuvaus) {
        this.PalveluId = PalveluId;
        this.hintaPvm = hintapvm;
        this.kuvaus = kuvaus;
    }

    public int getPalveluId() {
        return PalveluId;
    }

    public void setPalveluId(int PalveluId) {
        this.PalveluId = PalveluId;
    }

    public int getHintaPvm() {
        return hintaPvm;
    }

    public void setHintapPvm(int hintapvm) {
        this.hintaPvm = hintapvm;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }
    
    public String toString() {
        return getKuvaus();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.PalveluId;
        hash = 53 * hash + this.hintaPvm;
        hash = 53 * hash + Objects.hashCode(this.kuvaus);
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
        final Palvelu other = (Palvelu) obj;
        if (this.PalveluId != other.PalveluId) {
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
