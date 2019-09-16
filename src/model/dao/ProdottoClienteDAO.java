package model.dao;

import model.entities.ProdottoCliente;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.entities.Prodotto;
import model.entities.Utente;

public class ProdottoClienteDAO extends ObjectDAO {

    public boolean insert(ProdottoCliente pc) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String strDataAttivazione=sdf.format(pc.getData_attivazione());

        String sql="INSERT INTO prodottoCliente (saldo, stato, data_attivazione, cliente_id, prodotto_id) VALUES ("+
                "'"+pc.getSaldo()+"',"+
                "'"+pc.getStato()+"',"+
                 "'"+strDataAttivazione+"',"+
                "'"+pc.getCliente().getId()+"',"+
                "'"+pc.getProdotto().getId()+"')";
        return super.insert(sql);
    }
    
    public boolean update(ProdottoCliente sc) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String strDataAttivazione=sdf.format(sc.getData_attivazione());

        String sql="UPDATE prodottoCliente SET "+
                "saldo='"+sc.getSaldo()+"',"+
                "stato='"+sc.getStato()+"',"+
                "data_attivazione='"+strDataAttivazione+"',"+
                "cliente_id='"+sc.getCliente().getId()+"',"+
                "prodotto_id='"+sc.getProdotto().getId()+"'"+
                " WHERE id='"+sc.getId()+"'";
        
        return super.update(sql);
    }

    public boolean delete(ProdottoCliente sc) {
        return super.delete("prodottoCliente", sc.getId());
    }
    
    
    public void attivaProdottoCliente(Utente cliente) {
        ArrayList<ProdottoCliente> al=new ArrayList<ProdottoCliente>();
        al=this.findByCliente(cliente);
        for(int i=0;i<al.size();i++) {
            ProdottoCliente pc=al.get(i);
            pc.setStato("attivato");
            pc.setData_attivazione(new Date());
            update(pc);
        }
    }
    
    
    public ProdottoCliente findById(int id) {
        ProdottoCliente pc=new ProdottoCliente();
        ResultSet rs =super.findById("prodottoCliente", id);
        try {
            if(rs.next())
                pc=setProdottoClienteFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pc;
    }
    
    private ProdottoCliente setProdottoClienteFromResultSet(ResultSet rs) {        
        ProdottoCliente sc=new ProdottoCliente();
        try {
                sc.setId(rs.getInt("id"));
                sc.setSaldo(rs.getFloat("saldo"));
                sc.setStato(rs.getString("stato"));
                sc.setData_attivazione(rs.getDate("data_attivazione"));
                
                //individua il prodotto di riferimento
                int prodotto_id=rs.getInt("prodotto_id");
                ProdottoDAO sdao=new ProdottoDAO();
                Prodotto pro=sdao.findById(prodotto_id);
                sc.setProdotto(pro);
                
                //individua il prodotto di riferimento
                int cliente_id=rs.getInt("cliente_id");
                UtenteDAO cdao=new UtenteDAO();
                Utente cli=cdao.findById(cliente_id);
                sc.setCliente(cli);
                                
        } catch (SQLException ex) {
            Logger.getLogger(ProdottoClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return sc;
    }

    public ProdottoCliente findProdottoClienteByField(String fieldname, String fieldvalue) {
        ProdottoCliente sc=new ProdottoCliente();
        String sql = "SELECT * FROM prodottoCliente WHERE ("+ fieldname+"='"+fieldvalue+"');";
        ResultSet result = super.query(sql);
        return setProdottoClienteFromResultSet(result);
    }

    public ArrayList<ProdottoCliente> findAll() {
        ResultSet rs = super.findAll("prodottoCliente");
        ArrayList<ProdottoCliente> listaServiziCliente = getArrayListFromResultSet(rs);        
        return listaServiziCliente;
    }
    
    private ArrayList<ProdottoCliente> getArrayListFromResultSet(ResultSet rs) {
        ArrayList<ProdottoCliente> al=new ArrayList<ProdottoCliente>();
        try {
            while(rs.next()) {
                ProdottoCliente pc=setProdottoClienteFromResultSet(rs);
                al.add(pc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return al;
    }

    public ArrayList<ProdottoCliente> findByCliente(Utente cliente) {
        String sql="SELECT * FROM prodottoCLiente WHERE cliente_id="+cliente.getId();
        ResultSet rs=super.query(sql);
        return getArrayListFromResultSet(rs);
    }

    public ArrayList<ProdottoCliente> findByClienteProdotto(Utente cliente, Prodotto prodotto) {
        String sql="SELECT * FROM prodottoCLiente WHERE cliente_id="+cliente.getId()+" AND prodotto_id="+prodotto.getId();
        ResultSet rs=super.query(sql);
        return getArrayListFromResultSet(rs);
    }
    
    public void attivaServiziCliente(Utente cliente) {
        ArrayList<ProdottoCliente> al=new ArrayList<ProdottoCliente>();
        al=this.findByCliente(cliente);
        for(int i=0;i<al.size();i++) {
            ProdottoCliente sc=al.get(i);
            sc.setStato("attivato");
            sc.setData_attivazione(new Date());
            update(sc);
        }
    }
    
    public void attivaProdottoCliente(Utente cliente, Prodotto prodotto) {
        ArrayList<ProdottoCliente> al=new ArrayList<ProdottoCliente>();
        String sql="SELECT * FROM prodottoCliente WHERE cliente_id="+cliente.getId()+" AND prodotto_id="+prodotto.getId();
        ResultSet rs=super.query(sql);
        try {
            rs.next();
            ProdottoCliente pc=setProdottoClienteFromResultSet(rs);
            pc.setStato("attivato");
            pc.setData_attivazione(new Date());
            update(pc);
        } catch (SQLException ex) {
            Logger.getLogger(ProdottoClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
    }
}
