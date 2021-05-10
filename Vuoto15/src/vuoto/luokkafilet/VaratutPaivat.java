/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuoto.luokkafilet;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Luokka varauksien varattujen päivien tarkastelua varten. Sisältää päivämääräpareja (aloitusPvm ja lopetusPvm)
 * @author sauli
 */
public class VaratutPaivat {

    private LocalDate aloitusPvm; // aloitusPvm
    private LocalDate lopetusPvm; // lopetusPvm
    
    public VaratutPaivat(LocalDate aloitusPvm, LocalDate lopetusPvm) {
        this.aloitusPvm = aloitusPvm;
        this.lopetusPvm = lopetusPvm;
    }

    public LocalDate getAloitusPvm() {
        return aloitusPvm;
    }

    public void setAloitusPvm(LocalDate aloitusPvm) {
        this.aloitusPvm = aloitusPvm;
    }

    public LocalDate getLoppuPvm() {
        return lopetusPvm;
    }

    public void setLopetusPvm(LocalDate lopetusPvm) {
        this.lopetusPvm = lopetusPvm;
    }

    /**
     * Ylikirjoittaa Object-luokan hashCode() metodin
     * @return hashCode
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.aloitusPvm);
        hash = 73 * hash + Objects.hashCode(this.lopetusPvm);
        return hash;
    }

    /**
     * Ylikirjoittaa Object-luokan equals() metodin
     * @param obj Verrattava objekti
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
        final VaratutPaivat other = (VaratutPaivat) obj;
        if (!Objects.equals(this.aloitusPvm, other.aloitusPvm)) {
            return false;
        }
        if (!Objects.equals(this.lopetusPvm, other.lopetusPvm)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "VaratutPaivat{" + "d1=" + aloitusPvm + ", d2=" + lopetusPvm + '}';
    }
    
    
}
