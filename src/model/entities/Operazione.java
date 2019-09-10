package model.entities;

import java.util.Date;

public class Operazione {

    private int id=-1;
    private Date data=new Date();
    private String hash="";
    private float importo=0;
    private String tipologia="non_valida";
    private String stato="non_confermato"; //confermato, annullato
    private Date data_conferma_cassiere=new Date();
    private Utente cliente;
    private Utente cassiere;
    private Filiale filiale;
            
    public Operazione(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public float getImporto() {
        return importo;
    }

    public void setImporto(float importo) {
        this.importo = importo;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    /**
     * @return the cliente
     */
    public Utente getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(Utente cliente) {
        this.cliente = cliente;
    }

    /**
     * @return the cassiere
     */
    public Utente getCassiere() {
        return cassiere;
    }

    /**
     * @param cassiere the cassiere to set
     */
    public void setCassiere(Utente cassiere) {
        this.cassiere = cassiere;
    }

    /**
     * @return the filiale
     */
    public Filiale getFiliale() {
        return filiale;
    }

    /**
     * @param filiale the filiale to set
     */
    public void setFiliale(Filiale filiale) {
        this.filiale = filiale;
    }

    /**
     * @return the data_conferma_cassiere
     */
    public Date getData_conferma_cassiere() {
        return data_conferma_cassiere;
    }

    /**
     * @param data_conferma_cassiere the data_conferma_cassiere to set
     */
    public void setData_conferma_cassiere(Date data_conferma_cassiere) {
        this.data_conferma_cassiere = data_conferma_cassiere;
    }
}
