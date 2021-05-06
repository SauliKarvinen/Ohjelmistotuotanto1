/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.luokkafilet;

import java.time.LocalDate;

/**
 *
 * @author sauli
 */
public class VarausOlio {
    
    private int varausId;
    private LocalDate alkupaiva;
    private LocalDate loppupaiva;
    private String asiakas;
    private String toimitila;
    private String toimipiste;
    
    public VarausOlio(int varausId, LocalDate alkupaiva, LocalDate loppupaiva, String asiakas, String toimitila, String toimipiste) {
        this.varausId = varausId;
        this.alkupaiva = alkupaiva;
        this.loppupaiva = loppupaiva;
        this.asiakas = asiakas;
        this.toimitila = toimitila;
        this.toimipiste = toimipiste;
    }

    public int getVarausId() {
        return varausId;
    }

    public void setVarausId(int varausId) {
        this.varausId = varausId;
    }

    public LocalDate getAlkupaiva() {
        return alkupaiva;
    }

    public void setAlkupaiva(LocalDate alkupaiva) {
        this.alkupaiva = alkupaiva;
    }

    public LocalDate getLoppupaiva() {
        return loppupaiva;
    }

    public void setLoppupaiva(LocalDate loppupaiva) {
        this.loppupaiva = loppupaiva;
    }

    public String getAsiakas() {
        return asiakas;
    }

    public void setAsiakas(String asiakas) {
        this.asiakas = asiakas;
    }

    public String getToimitila() {
        return toimitila;
    }

    public void setToimitila(String toimitila) {
        this.toimitila = toimitila;
    }

    public String getToimipiste() {
        return toimipiste;
    }

    public void setToimipiste(String toimipiste) {
        this.toimipiste = toimipiste;
    }
    
    
}
