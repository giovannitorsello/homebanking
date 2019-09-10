package model.dao;

import model.entities.ServizioCliente;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.entities.Servizio;
import model.entities.Utente;

public class ServizioClienteDAO extends ObjectDAO {

    public boolean insert(ServizioCliente sc) {


        String sql="INSERT INTO servizioCliente (saldo, stato, cliente_id, servizio_id) VALUES ("+
                "'"+sc.getSaldo()+"',"+
                "'"+sc.getStato()+"',"+
                "'"+sc.getCliente().getId()+"',"+
                "'"+sc.getServizio().getId()+"')";
        return super.insert(sql);
    }
    
    public boolean update(ServizioCliente sc) {
        String sql="UPDATE servizioCliente SET "+
                "saldo='"+sc.getSaldo()+"',"+
                "stato='"+sc.getStato()+"',"+
                "cliente_id='"+sc.getCliente().getId()+"',"+
                "servizio_id='"+sc.getServizio().getId()+"'"+
                " WHERE id='"+sc.getId()+"'";
        
        return super.update(sql);
    }

    public ServizioCliente findById(int id) {
        ServizioCliente u=new ServizioCliente();
        ResultSet rs =super.findById("servizioCliente", id);
        try {
            if(rs.next())
                u=setServizioClienteFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }
    
    private ServizioCliente setServizioClienteFromResultSet(ResultSet rs) {        
        ServizioCliente sc=new ServizioCliente();
        try {
                sc.setId(rs.getInt("id"));
                sc.setSaldo(rs.getFloat("saldo"));
                sc.setStato(rs.getString("stato"));
                
                //individua il servizio di riferimento
                int servizio_id=rs.getInt("servizio_id");
                ServizioDAO sdao=new ServizioDAO();
                Servizio srv=sdao.findById(servizio_id);
                sc.setServizio(srv);
                
                //individua il servizio di riferimento
                int cliente_id=rs.getInt("cliente_id");
                UtenteDAO cdao=new UtenteDAO();
                Utente cli=cdao.findById(servizio_id);
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
}
