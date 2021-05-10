/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.luokkafilet;

import java.util.Objects;

/**
 * Toimitila-luokka
 */
public class Toimitila {
    
    private int tilaId;
    private String lahiosoite;
    private String postinumero;
    private String postitoimipaikka;
    private int huonekoko;
    private int hintaPvm;
    private int huoneistonTila;
    private String kuvaus;
    private String tilanNimi;
    private int toimipisteId;
    
    /**
     * Parametriton konstruktori
     */
    public Toimitila() {
        
    }

    /**
     * Käytetään tätä konstruktoria kun luodaan Toimitila (ID:tä ei syötetä manuaalisesti koska se on tietokannassa AUTO_INCREMENT)
     * @param lahiosoite
     * @param postinumero
     * @param huonekoko
     * @param hintaPvm
     * @param huoneistonTila
     * @param kuvaus
     * @param tilanNimi
     * @param toimipisteId 
     */
    public Toimitila(String lahiosoite, String postinumero, String postitoimipaikka, int huonekoko, int hintaPvm, int huoneistonTila, String kuvaus, String tilanNimi, int toimipisteId) {
        this.lahiosoite = lahiosoite;
        this.postinumero = postinumero;
        this.postitoimipaikka = postitoimipaikka;
        this.huonekoko = huonekoko;
        this.hintaPvm = hintaPvm;
        this.huoneistonTila = huoneistonTila;
        this.kuvaus = kuvaus;
        this.tilanNimi = tilanNimi;
        this.toimipisteId = toimipisteId;
    }

    /**
     * Käytetään tätä konstruktoria kun haetaan tietokannasta Toimitila jolla on jo generoitu ID
     * @param tilaId
     * @param lahiosoite
     * @param postinumero
     * @param huonekoko
     * @param hintaPvm
     * @param huoneistonTila
     * @param kuvaus
     * @param tilanNimi
     * @param toimipisteId 
     */
    public Toimitila(int tilaId, String lahiosoite, String postinumero, String postitoimipaikka, int huonekoko, int hintaPvm, int huoneistonTila, String kuvaus, String tilanNimi, int toimipisteId) {
        this.tilaId = tilaId;
        this.lahiosoite = lahiosoite;
        this.postinumero = postinumero;
        this.postitoimipaikka = postitoimipaikka;
        this.huonekoko = huonekoko;
        this.hintaPvm = hintaPvm;
        this.huoneistonTila = huoneistonTila;
        this.kuvaus = kuvaus;
        this.tilanNimi = tilanNimi;
        this.toimipisteId = toimipisteId;
    }

    public int getTilaId() {
        return tilaId;
    }

    public void setTilaId(int tilaId) {
        this.tilaId = tilaId;
    }

    public String getLahiosoite() {
        return lahiosoite;
    }

    public void setLahiosoite(String lahiosoite) {
        this.lahiosoite = lahiosoite;
    }

    public String getPostinumero() {
        return postinumero;
    }

    public void setPostinumero(String postinumero) {
        this.postinumero = postinumero;
    }
    
    public String getPostitoimipaikka() {
        return postitoimipaikka;
    }

    public void setPostitoimipaikka(String postitoimipaikka) {
        this.postitoimipaikka = postitoimipaikka;
    }

    public int getHuonekoko() {
        return huonekoko;
    }

    public void setHuonekoko(int huonekoko) {
        this.huonekoko = huonekoko;
    }

    public int getHintaPvm() {
        return hintaPvm;
    }

    public void setHintaPvm(int hintaPvm) {
        this.hintaPvm = hintaPvm;
    }

    public int getHuoneistonTila() {
        return huoneistonTila;
    }

    public void setHuoneistonTila(int huoneistonTila) {
        this.huoneistonTila = huoneistonTila;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

    public String getTilanNimi() {
        return tilanNimi;
    }

    public void setTilanNimi(String tilanNimi) {
        this.tilanNimi = tilanNimi;
    }

    public int getToimipisteId() {
        return toimipisteId;
    }

    public void setToimipisteId(int toimipisteId) {
        this.toimipisteId = toimipisteId;
    }
    
    public String toString() {
        return this.getTilanNimi();
    }


    /**
     * Ylikirjoittaa Object-luokan hashCode() metodin
     * @return hashCode
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.tilaId;
        hash = 89 * hash + Objects.hashCode(this.lahiosoite);
        hash = 89 * hash + Objects.hashCode(this.postinumero);
        hash = 89 * hash + Objects.hashCode(this.postitoimipaikka);
        hash = 89 * hash + this.huonekoko;
        hash = 89 * hash + this.hintaPvm;
        hash = 89 * hash + Objects.hashCode(this.kuvaus);
        hash = 89 * hash + Objects.hashCode(this.tilanNimi);
        hash = 89 * hash + this.toimipisteId;
        return hash;
    }

    /**
     * Ylikirjoittaa Object-luokanequals() metodin
     * @return true / false
     */
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
        final Toimitila other = (Toimitila) obj;
        if (this.tilaId != other.tilaId) {
            return false;
        }
        if (this.huonekoko != other.huonekoko) {
            return false;
        }
        if (this.hintaPvm != other.hintaPvm) {
            return false;
        }
        if (this.toimipisteId != other.toimipisteId) {
            return false;
        }
        if (!Objects.equals(this.lahiosoite, other.lahiosoite)) {
            return false;
        }
        if (!Objects.equals(this.postinumero, other.postinumero)) {
            return false;
        }
        if (!Objects.equals(this.postitoimipaikka, other.postitoimipaikka)) {
            return false;
        }
        if (!Objects.equals(this.kuvaus, other.kuvaus)) {
            return false;
        }
        if (!Objects.equals(this.tilanNimi, other.tilanNimi)) {
            return false;
        }
        return true;
    }
    
    
    
    
}
