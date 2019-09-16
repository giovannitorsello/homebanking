package model.dao;

import model.entities.Operazione;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import model.entities.Utente;
import model.entities.Filiale;
import model.entities.Servizio;
import model.entities.ServizioCliente;

public class OperazioneDAO extends ObjectDAO {

    public boolean insert(Operazione op) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String str_data=sdf.format(op.getData());
        String str_data_conferma_cassiere=sdf.format(op.getData_conferma_cassiere());
        
        String sql="INSERT INTO operazione (data, hash, importo, note, tipologia, stato, data_conferma_cassiere, servizio_id, cliente_id, filiale_id, cassiere_id) VALUES ("+
                "'"+str_data+"',"+
                "'"+op.getHash()+"',"+
                "'"+op.getImporto()+"',"+
                "'"+op.getNote()+"',"+
                "'"+op.getTipologia()+"',"+
                "'"+op.getStato()+"',"+
                "'"+str_data_conferma_cassiere+"',"+
                "'"+op.getServizioCliente().getId()+"',"+
                "'"+op.getCliente().getId()+"',"+
                "'"+op.getFiliale().getId()+"',"+
                "'"+op.getCassiere().getId()+"')";
        return super.insert(sql);
    }
    
    public boolean update(Operazione op) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String str_data=sdf.format(op.getData());
        String str_data_conferma_cassiere=sdf.format(op.getData_conferma_cassiere());
        
        String sql="UPDATE operazione SET "+
                "data='"+str_data+"',"+
                "hash='"+op.getHash()+"',"+
                "importo='"+op.getImporto()+"',"+   
                "note='"+op.getNote()+"',"+   
                "tipologia='"+op.getTipologia()+"',"+
                "stato='"+op.getStato()+"',"+
                "data_conferma_cassiere='"+str_data_conferma_cassiere+"',"+
                "servizio_id='"+op.getServizioCliente().getId()+"',"+
                "cliente_id='"+op.getCliente().getId()+"',"+
                "filiale_id='"+op.getFiliale().getId()+"',"+
                "cassiere_id='"+op.getCassiere().getId()+"'"+                
                " WHERE id='"+op.getId()+"'";
        
        return super.update(sql);
    }

    public boolean delete(Operazione op){        
        return super.delete("operazione", op.getId());
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
   
    
    public ArrayList<Operazione> findByClienteServizioStatoTipologiaPeriodo(Utente cli, ServizioCliente sercli, String tip, String sta, Date dtStart, Date dtEnd) {
        ArrayList<Operazione> al=new ArrayList<Operazione>();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        
        String sql="SELECT * FROM operazione WHERE (";
        if(cli!=null && cli.getId()!=-1)                sql+="cliente_id='"+cli.getId()+"'";
        if(sercli!=null && sercli.getId()!=-1)          sql+=" AND servizio_id='"+sercli.getId()+"'";
        if(sta!=null && !sta.isEmpty())                 sql+=" AND stato='"+sta+"'";
        if(tip!=null && !tip.isEmpty())                 sql+=" AND tipologia='"+tip+"'";
        if(dtStart!=null && dtEnd!=null)   {
            String strDateStart=sdf.format(dtStart);
            String strDateEnd=sdf.format(dtEnd);
                                                        sql+=" AND (data >='"+strDateStart+"' AND  data<='"+strDateEnd+"')";
        }
        sql+=");";
        
        ResultSet rs=super.query(sql);
        return getArrayListFromResultSet(rs);
    }
    
    public ArrayList<Operazione> findByCliente(Utente cliente) {
        ArrayList<Operazione> al=new ArrayList<Operazione>();
        String sql="SELECT * FROM operazione WHERE (cliente_id='"+cliente.getId()+"');";
        ResultSet rs=super.query(sql);
        return getArrayListFromResultSet(rs);
    }
    
    public ArrayList<Operazione> findByServizioCliente(ServizioCliente sercli) {
        ArrayList<Operazione> al=new ArrayList<Operazione>();
        String sql="SELECT * FROM operazione WHERE (servizio_id='"+sercli.getId()+"');";
        ResultSet rs=super.query(sql);
        return getArrayListFromResultSet(rs);
    }
    
    public ArrayList<Operazione> findByServizioClientePeriodo(ServizioCliente sercli, Date dtStart, Date dtEnd) {
        ArrayList<Operazione> al=new ArrayList<Operazione>();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String sql="SELECT * FROM operazione WHERE (servizio_id='"+sercli.getId()+"'";
        if(dtStart!=null && dtEnd!=null)   {
            String strDateStart=sdf.format(dtStart);
            String strDateEnd=sdf.format(dtEnd);
            sql+=" AND (data >='"+strDateStart+"' AND  data<='"+strDateEnd+"')";
        }        
        sql+=");";
        
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
        ResultSet rs = super.findAll("operazione");
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
                op.setStato(rs.getString("stato"));
                op.setNote(rs.getString("note"));
                op.setData_conferma_cassiere(rs.getDate("data_conferma_cassiere"));
                
                //determina il servizio cliente
                ServizioClienteDAO scdao=new ServizioClienteDAO();
                ServizioCliente sc=scdao.findById(rs.getInt("servizio_id"));
                op.setServizioCliente(sc);
                
                //determina il cliente
                UtenteDAO udao=new UtenteDAO();
                Utente cliente=udao.findById(rs.getInt("cliente_id"));
                op.setCliente(cliente);
                
                //determina il cassiere
                Utente cassiere=udao.findById(rs.getInt("cassiere_id"));
                op.setCassiere(cassiere);
                
                //determina la filiale
                FilialeDAO fdao=new FilialeDAO();
                Filiale filiale=fdao.findById(rs.getInt("filiale_id"));
                op.setFiliale(filiale);
                                    
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
