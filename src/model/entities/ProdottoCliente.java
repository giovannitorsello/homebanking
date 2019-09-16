package model.entities;

import java.util.ArrayList;
import java.util.Date;

public class ProdottoCliente {

    private int id=-1;
    private float saldo=0;
    private String stato="non_confermato"; //attivo, disattivo, non_confermato
    private Date data_attivazione=new Date();
    private Prodotto prodotto=new Prodotto();   
    private Utente cliente=new Utente();
    
    
    
    
  
  
    
    
    public ProdottoCliente() {

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
    public Prodotto getProdotto() {
        return prodotto;
    }

    /**
     * @param servizio the servizio to set
     */
    public void setProdotto(Prodotto prodotto) {
        this.prodotto = prodotto;
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
    
    public float calcolaInteressiPeriodo(ArrayList<Operazione> ops, Date dsStart, Date dsEnd) {
        if(ops.size()==0) return 0;
        
        float interesse_periodo=0;
        float saldo_periodo=calcolaSaldoPeriodo(ops, dsStart, dsEnd);        
        ServizioCliente sc=ops.get(0).getServizioCliente();
        Servizio ser=sc.getServizio();
        Prodotto pro=ser.getProdotto();
        
        float interessi_attivi=pro.getInteressi_attivi();
        float interessi_passivi=pro.getInteressi_attivi();
        if(saldo_periodo>=0) interesse_periodo=saldo_periodo*interessi_attivi/100;
        if(saldo_periodo<=0) interesse_periodo=saldo_periodo*interessi_passivi/100;
        return interesse_periodo;
    }
    
    public float calcolaSaldoPeriodo(ArrayList<Operazione> ops, Date dsStart, Date dsEnd) {
    
        if(ops.size()==0) return 0;
        
        float saldo=0;
        for (int i = 0; i < ops.size(); i++) {
            Operazione op=ops.get(i);
            ServizioCliente sc=op.getServizioCliente();
            Servizio ser=sc.getServizio();
            Prodotto pro=ser.getProdotto();
            
            //conteggia solo le operazini confermate e che ricadono nel periodo di ricerca
            if(     op.getStato().equals("confermata") &&
                    op.getData().getTime()>=dsStart.getTime() && 
                    op.getData().getTime()<=dsEnd.getTime() ) {
                float importo=op.getImporto();
                saldo+=importo;
            }
        }
        return saldo;
    }

    /**
     * @return the data_attivazione
     */
    public Date getData_attivazione() {
        return data_attivazione;
    }

    /**
     * @param data_attivazione the data_attivazione to set
     */
    public void setData_attivazione(Date data_attivazione) {
        this.data_attivazione = data_attivazione;
    }

}
