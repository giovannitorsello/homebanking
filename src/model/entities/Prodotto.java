package model.entities;

import java.util.Date;

public class Prodotto {

    private int id=-1;
    private String denominazione="";
    private Date data_attivazione=new Date();
    private Date data_scadenza=new Date();
    private String descrizione="";
    private String url_condizioni_generali="";
    private float interessi_passivi=0F;
    private float interessi_attivi=0F;
    private Banca banca=new Banca();
   

    public Prodotto(){

    }


    public Banca getBanca() {
        return banca;
    }

    public void setBanca(Banca banca) {
        this.banca = banca;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDenominazione() {
        return denominazione;
    }

    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    public Date getData_attivazione() {
        return data_attivazione;
    }

    public void setData_attivazione(Date data_attivazione) {
        this.data_attivazione = data_attivazione;
    }

    public Date getData_scadenza() {
        return data_scadenza;
    }

    public void setData_scadenza(Date data_scadenza) {
        this.data_scadenza = data_scadenza;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getUrl_condizioni_generali() {
        return url_condizioni_generali;
    }

    public void setUrl_condizioni_generali(String url_condizioni_generali) {
        this.url_condizioni_generali = url_condizioni_generali;
    }

    public float getInteressi_passivi() {
        return interessi_passivi;
    }

    public void setInteressi_passivi(float interessi_passivi) {
        this.interessi_passivi = interessi_passivi;
    }

    public float getInteressi_attivi() {
        return interessi_attivi;
    }

    public void setInteressi_attivi(float interessi_attivi) {
        this.interessi_attivi = interessi_attivi;
    }
}
