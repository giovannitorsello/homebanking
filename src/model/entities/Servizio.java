package model.entities;

import java.util.Date;

public class Servizio {

    private int id=-1;
    private String denominazione="";
    private Date data_attivazione=new Date();
    private Date data_scadenza=new Date();
    private String descrizione="";
    private int numero_massimo_operazioni=10;
    private Prodotto prodotto=new Prodotto();



    public Servizio(){

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

    public int getNumero_massimo_operazioni() {
        return numero_massimo_operazioni;
    }

    public void setNumero_massimo_operazioni(int numero_massimo_operazioni) {
        this.numero_massimo_operazioni = numero_massimo_operazioni;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public void setProdotto(Prodotto prodotto) {
        this.prodotto = prodotto;
    }
}
