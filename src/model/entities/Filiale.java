package model.entities;

public class Filiale {

    private int id=-1;
    private String nome="";
    private String indirizzo="";
    private String orarioApertura="09:00";
    private String orarioChiusura="15:00";

    private Banca banca=null;
    private Utente direttore=null;


    public Filiale(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getOrarioApertura() {
        return orarioApertura;
    }

    public void setOrarioApertura(String orarioApertura) {
        this.orarioApertura = orarioApertura;
    }

    public String getOrarioChiusura() {
        return orarioChiusura;
    }

    public void setOrarioChiusura(String orarioChiusura) {
        this.orarioChiusura = orarioChiusura;
    }

    public Utente getDirettore() {
        return direttore;
    }

    public void setDirettore(Utente direttore) {
        this.direttore = direttore;
    }

    public Banca getBanca() {
        return banca;
    }

    public void setBanca(Banca banca) {
        this.banca = banca;
    }



}
