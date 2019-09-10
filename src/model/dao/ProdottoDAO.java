package model.dao;

import model.entities.Banca;
import model.entities.Prodotto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ProdottoDAO extends ObjectDAO {


    public boolean insert(Prodotto p) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String str_data_attivazione=sdf.format(p.getData_attivazione());
        String str_data_scadenza=sdf.format(p.getData_scadenza());
        String sql="INSERT INTO prodotto (denominazione, data_attivazione, data_scadenza, descrizione, " +
                "url_condizioni_generali,interessi_passivi, interessi_attivi, banca_id) VALUES ("+
                "'"+p.getDenominazione()+"',"+
                "'"+str_data_attivazione+"',"+
                "'"+str_data_scadenza+"',"+
                "'"+p.getDescrizione()+"',"+
                "'"+p.getUrl_condizioni_generali()+"',"+
                "'"+p.getInteressi_passivi()+"',"+
                "'"+p.getInteressi_attivi()+"',"+
                "'"+p.getBanca().getId()+"')";
        return super.insert(sql);
    }
    
    public boolean update(Prodotto p) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String str_data_attivazione=sdf.format(p.getData_attivazione());
        String str_data_scadenza=sdf.format(p.getData_scadenza());
        
        String sql="UPDATE servizio SET "+
                "denominazione='"+p.getDenominazione()+"',"+
                "data_attivazione='"+str_data_attivazione+"',"+
                "data_scadenza='"+str_data_scadenza+"',"+                
                "descrizione='"+p.getDescrizione()+"',"+
                "url_condizioni_generali='"+p.getUrl_condizioni_generali()+"',"+
                "interessi_passivi='"+p.getInteressi_attivi()+"',"+
                "interessi_attivi='"+p.getInteressi_passivi()+"',"+
                "banca_id='"+p.getBanca().getId()+"'"+
                " WHERE id='"+p.getId()+"'";
        
        return super.update(sql);
    }

    public Prodotto findById(int id) {
        Prodotto u=new Prodotto();
        ResultSet rs =super.findById("prodotto", id);
        try {
            if(rs.next())
                u=setProdottoFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }

    public boolean delete(Prodotto prodotto) {
        return super.delete("prodotto", prodotto.getId());
    }

    public ArrayList<Prodotto> findAll() {

        ResultSet rs = super.findAll("prodotto");
        ArrayList<Prodotto> listaProdotti = getArrayListFromResultSet(rs);        
        return listaProdotti;
    }

    public Prodotto findProdottoByField(String fieldname, String fieldvalue) {
        Prodotto prodotto=new Prodotto();
        String sql = "SELECT * FROM prodotto WHERE ("+ fieldname+"='"+fieldvalue+"');";
        ResultSet result = super.query(sql);
        try {
            if(result.next())
                return setProdottoFromResultSet(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prodotto;
    }

    private Prodotto setProdottoFromResultSet(ResultSet result) {
        Prodotto p=new Prodotto();
        try {
            p.setId(result.getInt("id"));
            p.setDenominazione(result.getString("denominazione"));
            p.setDescrizione(result.getString("descrizione"));
            p.setData_attivazione(result.getDate("data_attivazione"));
            p.setData_scadenza(result.getDate("data_scadenza"));
            p.setUrl_condizioni_generali(result.getString("url_condizioni_generali"));
            p.setInteressi_passivi(result.getFloat("interessi_passivi"));
            p.setInteressi_attivi(result.getFloat("interessi_attivi"));

            //Determina la banca di appartenenza
            BancaDAO bancaDAO=new BancaDAO();
            Banca b=bancaDAO.findById(result.getInt("banca_id"));
            p.setBanca(b);
          
            return p;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
    }

    

    public ArrayList<Prodotto> findByBanca(Banca banca) {
        String sql="SELECT * FROM prodotto WHERE (banca_id='"+banca.getId()+"');";
        ResultSet rs=super.query(sql);
        return getArrayListFromResultSet(rs);
    }

    private ArrayList<Prodotto> getArrayListFromResultSet(ResultSet rs) {
        ArrayList<Prodotto> al=new ArrayList<Prodotto>();
        try {
            while(rs.next()) {
                Prodotto p=setProdottoFromResultSet(rs);
                al.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return al;
    }
    
}
