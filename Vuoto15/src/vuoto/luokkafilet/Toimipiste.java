
package vuoto.luokkafilet;

/**
 * Toimipiste -luokka
 * @author sauli
 */
public class Toimipiste {
    
    /** Toimipisteen ID*/
    private int toimipisteID;
    /** Toimipisteen nimi*/
    private String lahiosoite;
    
    private String postinumero;
    
    private String toimipistenimi;
    
    private String kuvaus;

    /**
     * Tyhjä Toimipiste konstruktori, uuden toimipisteen luontia varten.
     * Käytetään kun UusiToimipiste-paneeli avataan.
     */
    public Toimipiste() {
        this.lahiosoite = "";
        this.postinumero = "";
        this.toimipistenimi = "";
        this.kuvaus = "";
       
    }
    
    /**
     * Toimipiste konstruktori, uuden toimipisteen luontia varten. ToimipisteId -luodaan vasta kannassa.
     * Käytetään kun UusiToimipiste luodaan.
     * @param lahiosoite
     * @param postinumero
     * @param toimipistenimi
     * @param kuvaus 
     */
    public Toimipiste(String lahiosoite, String postinumero, String toimipistenimi, String kuvaus) {
        
        this.lahiosoite = lahiosoite;
        this.postinumero = postinumero;
        this.toimipistenimi = toimipistenimi;
        this.kuvaus = kuvaus;
        
    }
    
    /**
     * Toimipiste konstruktori, käytetään ohjelmassa. Olemassa olevaan toimipisteen käyttöön.
     * @param toimipisteID
     * @param lahiosoite
     * @param postinumero
     * @param toimipistenimi
     * @param kuvaus 
     */
    public Toimipiste(int toimipisteID, String lahiosoite, String postinumero, String toimipistenimi, String kuvaus) {
        
        this.toimipisteID = toimipisteID;
        this.lahiosoite = lahiosoite;
        this.postinumero = postinumero;
        this.toimipistenimi = toimipistenimi;
        this.kuvaus = kuvaus;
        
    }

    public int getToimipisteID() {
        return toimipisteID;
    }

    public void setToimipisteID(int toimipisteID) {
        this.toimipisteID = toimipisteID;
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

    public String getToimipistenimi() {
        return toimipistenimi;
    }

    public void setToimipistenimi(String toimipistenimi) {
        this.toimipistenimi = toimipistenimi;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }
    
    public String toString() {
        return getToimipistenimi();
    }

}
