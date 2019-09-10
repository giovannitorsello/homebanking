package model.dao;

import model.entities.Operazione;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import model.entities.Utente;

public class OperazioneDAO extends ObjectDAO {

    public boolean insert(Operazione op) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String str_data=sdf.format(op.getData());
        String str_data_conferma_cassiere=sdf.format(op.getData_conferma_cassiere());
        
        String sql="INSERT INTO operazione (data, hash, importo, tipologia, stato, data_conferma_cassiere, cliente_id, filiale_id, cassiere_id) VALUES ('"
                +str_data+"', '"
                +op.getHash()+"', '"
                +op.getImporto()+"', '"
                +op.getTipologia()+"', '"
                +op.getStato()+"', '"
                +str_data_conferma_cassiere+"', '"
                +op.getCliente().getId()+"', '"
                +op.getFiliale().getId()+"', '"
                +op.getCassiere().getId()+")";
        return super.insert(sql);
    }
    
    public boolean update(Operazione op) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String str_data=sdf.format(op.getData());
        String str_data_conferma_cassiere=sdf.format(op.getData_conferma_cassiere());
        
        String sql="UPDATE servizio SET "+
                "data='"+str_data+"',"+
                "hash='"+op.getHash()+"',"+
                "importo='"+op.getImporto()+"',"+                
                "tipologia='"+op.getTipologia()+"',"+
                "stato='"+op.getStato()+"',"+
                "data_conferma_cassiere='"+str_data_conferma_cassiere+"',"+
                "cliente_id='"+op.getCliente().getId()+"',"+
                "cassiere_id='"+op.getCassiere().getId()+"'"+                
                " WHERE id='"+op.getId()+"'";
        
        return super.update(sql);
    }

    public boolean delete(Operazione op){
        String sql="DELETE FROM operazione WHERE id="+op.getId();
        return super.delete(sql, op.getId());
    }
    
    public Operazione findById(int id) {
        Operazione op=new Operazione();
        ResultSet rs =super.findById("operazione", id);
        try {
            if(rs.next()) op=setOperazioneFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return op;
    }
   
    public ArrayList<Operazione> findByCliente(Utente cliente) {
        ArrayList<Operazione> al=new ArrayList<Operazione>();
        String sql="SELECT * FROM operazione WHERE (cliente_id='"+cliente.getId()+"');";
        ResultSet rs=super.query(sql);
        return getArrayListFromResultSet(rs);
    }
    
    public ArrayList<Operazione> findByCassiere(Utente cassiere) {
        ArrayList<Operazione> al=new ArrayList<Operazione>();
        String sql="SELECT * FROM operazione WHERE (cassiere_id='"+cassiere.getId()+"');";
        ResultSet rs=super.query(sql);
        return getArrayListFromResultSet(rs);
    }
    
    
    public Operazione findProdottoByField(String fieldname, String fieldvalue) {
        Operazione prodotto=new Operazione();
        String sql = "SELECT * FROM prodotto WHERE ("+ fieldname+"='"+fieldvalue+"');";
        ResultSet result = super.query(sql);
        try {
            if(result.next())
                return setOperazioneFromResultSet(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prodotto;
    }

    public ArrayList<Operazione> findAll() {
        ResultSet rs = super.findAll("prodotto");
        ArrayList<Operazione> listaOperazioni = getArrayListFromResultSet(rs);        
        return listaOperazioni;
    }
    
    private Operazione setOperazioneFromResultSet(ResultSet rs) {
        Operazione op=new Operazione();        
        try {            
                op.setId(rs.getInt("id"));
                op.setData(rs.getDate("data"));
                op.setHash(rs.getString("hash"));
                op.setImporto(rs.getFloat("importo"));
                op.setTipologia(rs.getString("tipologia"));
                op.setStato(rs.getString("Stato"));
                op.setData_conferma_cassiere(rs.getDate("data_conferma_cassiere"));
                
                //determina il cliente
                UtenteDAO udao=new UtenteDAO();
                Utente cliente=udao.findById(rs.getInt("cliente_id"));
                op.setCliente(cliente);
                
                //determina il cassiere
                Utente cassiere=udao.findById(rs.getInt("cassiere_id"));
                op.setCliente(cassiere);
                                    
        } catch (SQLException e) {
            e.printStackTrace();
        }

        
        return op;
    }


    private ArrayList<Operazione> getArrayListFromResultSet(ResultSet rs) {
        ArrayList<Operazione> al=new ArrayList<Operazione>();
        try {
            while(rs.next()) {
                Operazione op=setOperazioneFromResultSet(rs);
                al.add(op);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return al;
    }
}
