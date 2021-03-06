
package vuoto.luokkafilet;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.sql.Date;
import java.util.Objects;



/**
 *
 * @author marko
 */
public class Varaus {
    
    /** 
     * Varauksen tiedot::
     *  int     varausId, 
     *  LocalDate    vuokraAlku, 
     *  LocalDate    vuokraLoppu,  Trying TimeStamp
     *  int   tilaId, 
     *  int   asiakasId,
     *  int   palveluvarausId,
     *  int   laitevarausId
     * 
     * Getters (ja setterit)
     *  getVarausId()
     *  getVuokraAlku()
     *  getVuokraLoppu()
     *  getTilaId()
     *  getAsiakasId()
     *  getPalveluvarausId()
     *  getLaitevarausId()
     * 
     */
    
    private int   varausId;
    private LocalDate  vuokraAlku;
    private LocalDate  vuokraLoppu;
    private int   tilaId;
    private int   asiakasId;
    private int   palveluvarausId;
    private int   laitevarausId;
    
    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    
    /**
     * Tyhjä Varaus konstruktori, uuden laskun luontia varten.
     * Käytetään kun Uusi Varaus-paneeli avataan.
     */
    
    public Varaus() {
        this.varausId = 0;
        this.tilaId = 0;
        this.asiakasId = 0;
        this.palveluvarausId = 0;
        this.laitevarausId = 0;
        this.vuokraAlku  = null;
        this.vuokraLoppu  = null;
    }

    /**
     * Konstruktori ilman varausId:tä, kun varaus luodaan.
     * varausId luodaan vasta db:ssä.
     *
     **/
    
     
    public Varaus(LocalDate vuokraAlku, LocalDate vuokraLoppu, int tilaId, int asiakasId, int palveluvarausId, int laitevarausId) {
        this.vuokraAlku = vuokraAlku;
        this.vuokraLoppu = vuokraLoppu;
        this.tilaId = tilaId;
        this.asiakasId = asiakasId;
        this.palveluvarausId = palveluvarausId;
        this.laitevarausId = laitevarausId;
    }
    
    /**
     * Konstruktori asiakas oliolle,
     * kun olemassa olevaa asiakasta käytetään ohjelmassa. 
     * 
     */
           
    public Varaus(int varausId, LocalDate vuokraAlku, LocalDate vuokraLoppu, int tilaId, int asiakasId, int palveluvarausId, int laitevarausId) {
        this.varausId = varausId;
        this.vuokraAlku = vuokraAlku;
        this.vuokraLoppu = vuokraLoppu;
        this.tilaId = tilaId;
        this.asiakasId = asiakasId;
        this.palveluvarausId = palveluvarausId;
        this.laitevarausId = laitevarausId;
    }

    /**
     * SETTERs & GETTERs
     * 
     */
    
    public int getVarausId() {
        return varausId;
    }

    public void setVarausId(int varausId) {
        this.varausId = varausId;
    }

    public LocalDate getVuokraAlku() {
        return vuokraAlku;
    }

    public void setVuokraAlku(LocalDate vuokraAlku) {
        this.vuokraAlku = vuokraAlku;
    }

    public LocalDate getVuokraLoppu() {
        return vuokraLoppu;
    }

    public void setVuokraLoppu(LocalDate vuokraLoppu) {
        this.vuokraLoppu = vuokraLoppu;
    }

    public int getTilaId() {
        return tilaId;
    }

    public void setTilaId(int tilaId) {
        this.tilaId = tilaId;
    }

    public int getAsiakasId() {
        return asiakasId;
    }

    public void setAsiakasId(int asiakasId) {
        this.asiakasId = asiakasId;
    }

    public int getPalveluvarausId() {
        return palveluvarausId;
    }

    public void setPalveluvarausId(int palveluvarausId) {
        this.palveluvarausId = palveluvarausId;
    }

    public int getLaitevarausId() {
        return laitevarausId;
    }

    public void setLaitevarausId(int laitevarausId) {
        this.laitevarausId = laitevarausId;
    }

    /**
     * Ylikirjoittaa Object luokan HashCode metodin
     * @return HashCode
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.vuokraAlku);
        hash = 89 * hash + Objects.hashCode(this.vuokraLoppu);
        hash = 89 * hash + this.tilaId;
        hash = 89 * hash + this.asiakasId;
        return hash;
    }

    /**
     * Ylikirjoittaa Object luokan equals metodin
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
        final Varaus other = (Varaus) obj;
        
        if (this.tilaId != other.tilaId) {
            return false;
        }
        if (this.asiakasId != other.asiakasId) {
            return false;
        }
        if (!Objects.equals(this.vuokraAlku, other.vuokraAlku)) {
            return false;
        }
        if (!Objects.equals(this.vuokraLoppu, other.vuokraLoppu)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Varaus{" + "vuokraAlku=" + vuokraAlku + ", vuokraLoppu=" + vuokraLoppu + ", tilaId=" + tilaId + ", asiakasId=" + asiakasId + '}';
    }

    
    
    
    
   
}