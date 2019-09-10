package model.entities;

public class Banca {

    private int id=-1;
    private String nome="";
    private String indirizzo="";
    private Utente amministratore=new Utente();
    private Utente direttore=new Utente();

    public Utente getAmministratore() {
        return amministratore;
    }

    public void setAmministratore(Utente utente) {
        this.amministratore = utente;
    }

    public Banca(){

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

    public Utente getDirettore() {
        return direttore;
    }

    public void setDirettore(Utente direttore) {
        this.direttore = direttore;
    }


}
