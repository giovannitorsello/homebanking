package model.dao;

import model.entities.ServizioCliente;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.entities.Prodotto;
import model.entities.Servizio;
import model.entities.ServizioCliente;
import model.entities.Utente;

public class ServizioClienteDAO extends ObjectDAO {

    public boolean insert(ServizioCliente sc) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String strDataAttivazione=sdf.format(sc.getData_attivazione());

        String sql="INSERT INTO servizioCliente (saldo, stato, data_attivazione, cliente_id, servizio_id) VALUES ("+
                "'"+sc.getSaldo()+"',"+
                "'"+sc.getStato()+"',"+
                 "'"+strDataAttivazione+"',"+
                "'"+sc.getCliente().getId()+"',"+
                "'"+sc.getServizio().getId()+"')";
        return super.insert(sql);
    }
    
    public boolean update(ServizioCliente sc) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String strDataAttivazione=sdf.format(sc.getData_attivazione());

        String sql="UPDATE servizioCliente SET "+
                "saldo='"+sc.getSaldo()+"',"+
                "stato='"+sc.getStato()+"',"+
                "data_attivazione='"+strDataAttivazione+"',"+
                "cliente_id='"+sc.getCliente().getId()+"',"+
                "servizio_id='"+sc.getServizio().getId()+"'"+
                " WHERE id='"+sc.getId()+"'";
        
        return super.update(sql);
    }

    public boolean delete(ServizioCliente sc) {
        return super.delete("servizioCliente", sc.getId());
    }
    
    
    public ServizioCliente findById(int id) {
        ServizioCliente sc=new ServizioCliente();
        ResultSet rs =super.findById("servizioCliente", id);
        try {
            if(rs.next())
                sc=setServizioClienteFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sc;
    }
    
    private ServizioCliente setServizioClienteFromResultSet(ResultSet rs) {        
        ServizioCliente sc=new ServizioCliente();
        try {
                sc.setId(rs.getInt("id"));
                sc.setSaldo(rs.getFloat("saldo"));
                sc.setStato(rs.getString("stato"));
                sc.setData_attivazione(rs.getDate("data_attivazione"));
                
                //individua il servizio di riferimento
                int servizio_id=rs.getInt("servizio_id");
                ServizioDAO sdao=new ServizioDAO();
                Servizio srv=sdao.findById(servizio_id);
                sc.setServizio(srv);
                
                //individua il servizio di riferimento
                int cliente_id=rs.getInt("cliente_id");
                UtenteDAO cdao=new UtenteDAO();
                Utente cli=cdao.findById(cliente_id);
                sc.setCliente(cli);
                
                
        } catch (SQLException ex) {
            Logger.getLogger(ServizioClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return sc;
    }

    public ServizioCliente findServizioClienteByField(String fieldname, String fieldvalue) {
        ServizioCliente sc=new ServizioCliente();
        String sql = "SELECT * FROM servizioCliente WHERE ("+ fieldname+"='"+fieldvalue+"');";
        ResultSet result = super.query(sql);
        return setServizioClienteFromResultSet(result);
    }

    public ArrayList<ServizioCliente> findAll() {
        ResultSet rs = super.findAll("servizioCliente");
        ArrayList<ServizioCliente> listaServiziCliente = getArrayListFromResultSet(rs);        
        return listaServiziCliente;
    }
    
    private ArrayList<ServizioCliente> getArrayListFromResultSet(ResultSet rs) {
        ArrayList<ServizioCliente> al=new ArrayList<ServizioCliente>();
        try {
            while(rs.next()) {
                ServizioCliente sc=setServizioClienteFromResultSet(rs);
                al.add(sc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return al;
    }

    public ArrayList<ServizioCliente> findByCliente(Utente cliente) {
        String sql="SELECT * FROM servizioCliente WHERE cliente_id="+cliente.getId();
        ResultSet rs=super.query(sql);
        return getArrayListFromResultSet(rs);
    }

    public ArrayList<ServizioCliente> findByClienteServizio(Utente cliente, Servizio servizio) {
        String sql="SELECT * FROM servizioCliente WHERE cliente_id="+cliente.getId()+" AND servizio_id="+servizio.getId();
        ResultSet rs=super.query(sql);
        return getArrayListFromResultSet(rs);
    }
    
    public ArrayList<ServizioCliente> findByClienteProdotto(Utente cliente, Prodotto prodotto) {
        String sql="SELECT * FROM servizioCliente WHERE cliente_id="+cliente.getId()+" AND prodotto_id="+prodotto.getId();
        ResultSet rs=super.query(sql);
        return getArrayListFromResultSet(rs);
    }
    
    
    public void attivaServiziCliente(Utente cliente) {
        ArrayList<ServizioCliente> al=new ArrayList<ServizioCliente>();
        al=this.findByCliente(cliente);
        for(int i=0;i<al.size();i++) {
            ServizioCliente sc=al.get(i);
            sc.setStato("attivato");
            sc.setData_attivazione(new Date());
            update(sc);
        }
    }
    
    public void attivaServizioCliente(Utente cliente, Servizio servizio) {
        ArrayList<ServizioCliente> al=new ArrayList<ServizioCliente>();
        String sql="SELECT * FROM servizioCliente WHERE cliente_id="+cliente.getId()+" AND servizio_id="+servizio.getId();
        ResultSet rs=super.query(sql);
        try {
            rs.next();
            ServizioCliente sc=setServizioClienteFromResultSet(rs);
            sc.setStato("attivato");
            sc.setData_attivazione(new Date());
            update(sc);
        } catch (SQLException ex) {
            Logger.getLogger(ServizioClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
    }

    
}
