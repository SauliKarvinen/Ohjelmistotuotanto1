
package vuoto.luokkafilet;

import javafx.collections.ObservableList;

/**
 *
 * @author marko
 */
public class Asiakas {
    
    /** 
     * Asiakkaan tiedot::
     *  asiakasId
     *  etunimi
     *  sukunimi
     *  lahiosoite
     *  postinumero
     *  puhelinnumero
     *  sahkoposti
     *  yrityksenNimi
     * 
     */
    
    private int asiakasId;
    private String etunimi;
    private String sukunimi;
    private String lahiosoite;
    private String postinumero;
    private String puhelinnumero;
    private String sahkoposti;
    private String yrityksenNimi;
    
     /**
     * Tyhjä Asiakas konstruktori, uuden toimipisteen luontia varten.
     * Käytetään kun UusiToimipiste-paneeli avataan.
     */
    
    public Asiakas() {
        this.etunimi = "";
        this.sukunimi = "";
        this.lahiosoite = "";
        this.postinumero = "";
        this.puhelinnumero = "";
        this.sahkoposti = "";
        this.yrityksenNimi = "";
    }
    
    /**
     * Konstruktori ilman asiakasId:tä, kun asiakas luodaan.
     * asiakasId luodaan vasta db:ssä.
     * 
     * @param etunimi
     * @param sukunimi
     * @param lahiosoite
     * @param postinumero
     * @param puhelinnumero
     * @param sahkoposti
     * @param yrityksenNimi 
     */
    public Asiakas(String etunimi, String sukunimi, String lahiosoite, String postinumero , String puhelinnumero , String sahkoposti , String yrityksenNimi) {
        this.etunimi = etunimi;
        this.sukunimi = sukunimi;
        this.lahiosoite = lahiosoite;
        this.postinumero = postinumero;
        this.puhelinnumero = puhelinnumero;
        this.sahkoposti = sahkoposti;
        this.yrityksenNimi = yrityksenNimi;
    }
    
    /**
     * Konstruktori asiakas oliolle,
     * kun olemassa olevaa asiakasta käytetään ohjelmassa. 
     * 
     * @param asiakasId
     * @param etunimi
     * @param sukunimi
     * @param lahiosoite
     * @param postinumero
     * @param puhelinnumero
     * @param sahkoposti
     * @param yrityksenNimi 
     */
    public Asiakas(int asiakasId, String etunimi, String sukunimi, String lahiosoite, String postinumero , String puhelinnumero , String sahkoposti , String yrityksenNimi) {
        
        this.asiakasId = asiakasId;
        this.etunimi = etunimi;
        this.sukunimi = sukunimi;
        this.lahiosoite = lahiosoite;
        this.postinumero = postinumero;
        this.puhelinnumero = puhelinnumero;
        this.sahkoposti = sahkoposti;
        this.yrityksenNimi = yrityksenNimi;
    }

     /**
     * SETTERs & GETTERs
     * 
     */
    
    
    public int getAsiakasId() {
        return asiakasId;
    }

    public void setAsiakasId(int asiakasId) {
        this.asiakasId = asiakasId;
    }

    public String getEtunimi() {
        return etunimi;
    }

    public void setEtunimi(String etunimi) {
        this.etunimi = etunimi;
    }

    public String getSukunimi() {
        return sukunimi;
    }

    public void setSukunimi(String sukunimi) {
        this.sukunimi = sukunimi;
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

    public String getPuhelinnumero() {
        return puhelinnumero;
    }

    public void setPuhelinnumero(String puhelinnumero) {
        this.puhelinnumero = puhelinnumero;
    }

    public String getSahkoposti() {
        return sahkoposti;
    }

    public void setSahkoposti(String sahkoposti) {
        this.sahkoposti = sahkoposti;
    }

    public String getYrityksenNimi() {
        return yrityksenNimi;
    }

    public void setYrityksenNimi(String yrityksenNimi) {
        this.yrityksenNimi = yrityksenNimi;
    }
   
}