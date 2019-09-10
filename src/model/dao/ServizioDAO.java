package model.dao;

import model.entities.Servizio;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.entities.Prodotto;
import model.entities.Servizio;

public class ServizioDAO extends ObjectDAO {


    public boolean insert(Servizio s) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String str_data_attivazione=sdf.format(s.getData_attivazione());
        String str_data_scadenza=sdf.format(s.getData_scadenza());
        String sql="INSERT INTO servizio (denominazione, data_attivazione, data_scadenza, descrizione, numero_massimo_operazioni, prodotto_id) VALUES ("
                +"'"+s.getDenominazione()+"',"
                +"'"+str_data_attivazione+"',"
                +"'"+str_data_scadenza+"',"
                +"'"+s.getDescrizione()+"',"
                +"'"+s.getNumero_massimo_operazioni()+"',"
                +"'"+s.getProdotto().getId()+"')";
        return super.insert(sql);
    }
    
    public boolean update(Servizio s) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String str_data_attivazione=sdf.format(s.getData_attivazione());
        String str_data_scadenza=sdf.format(s.getData_scadenza());
        
        String sql="UPDATE servizio SET "+
                "denominazione='"+s.getDenominazione()+"',"+
                "data_attivazione='"+str_data_attivazione+"',"+
                "data_scadenza='"+str_data_scadenza+"',"+                
                "numero_massimo_operazioni='"+s.getNumero_massimo_operazioni()+"',"+
                "prodotto_id='"+s.getProdotto().getId()+"'"+
                " WHERE id='"+s.getId()+"'";
        
        return super.update(sql);
    }

    public Servizio findById(int id) {
        Servizio u=new Servizio();
        ResultSet rs =super.findById("servizio", id);
        try {
            if(rs.next())
                u=setServizioFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }

    public ArrayList<Servizio> findAll() {
        ResultSet rs = super.findAll("servizio");
        ArrayList<Servizio> listaProdotti = getArrayListFromResultSet(rs);        
        return listaProdotti;
    }


    private Servizio setServizioFromResultSet(ResultSet rs) {
        
        Servizio s=new Servizio();
        
        try {
            s.setId((Integer) rs.getInt("id"));
            s.setDenominazione((String) rs.getString("denominazione"));
            s.setDescrizione((String) rs.getString("descrizione"));
            s.setData_attivazione((Date) rs.getDate("data_attivazione"));
            s.setData_scadenza((Date) rs.getDate("data_scadenza"));
            s.setNumero_massimo_operazioni((Integer) rs.getInt("numero_massimo_operazioni"));
            
            //Determina il prodotto di appartenenza del servizio
            int prodotto_id = rs.getInt("prodotto_id");
            ProdottoDAO pdao=new ProdottoDAO();
            Prodotto prodotto=pdao.findById(prodotto_id);
            s.setProdotto(prodotto);
            
        } catch (Exception ex) {
            Logger.getLogger(ServizioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return s;
    }

    public boolean delete(Servizio servizio) {
        return super.delete("servizio", servizio.getId());
    }

    public ArrayList<Servizio> findByProdotto(Prodotto selectedProdotto) {
        ArrayList<Servizio> al=new ArrayList<Servizio>();
        String sql="SELECT * FROM servizio WHERE (prodotto_id='"+selectedProdotto.getId()+"');";
        ResultSet rs=super.query(sql);
        return getArrayListFromResultSet(rs);
    }
    
    private ArrayList<Servizio> getArrayListFromResultSet(ResultSet rs) {
        ArrayList<Servizio> al=new ArrayList<Servizio>();
        try {
            while(rs.next()) {
                Servizio p=setServizioFromResultSet(rs);
                al.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return al;
    }

}
