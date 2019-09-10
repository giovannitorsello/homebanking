package model.entities;

public class ServizioCliente {

    private int id=-1;
    private float saldo=0;
    private String stato="non_confermato"; //attivo, disattivo, non_confermato
    private Servizio servizio=new Servizio();   
    private Utente cliente=new Utente();
    
    public ServizioCliente() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    /**
     * @return the servizio
     */
    public Servizio getServizio() {
        return servizio;
    }

    /**
     * @param servizio the servizio to set
     */
    public void setServizio(Servizio servizio) {
        this.servizio = servizio;
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

}
