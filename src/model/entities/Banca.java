package model.entities;

public class Banca {

    private int id=-1;
    private String nome="";
    private String indirizzo="";
    private String descrizione="";
    private Utente amministratore=null;
    private Utente direttore=null;

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

    /**
     * @return the descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @param descrizione the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }


}
