package model.dao;

import model.entities.Banca;
import model.entities.Filiale;
import model.entities.Utente;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FilialeDAO extends ObjectDAO {

    public boolean insert(Filiale f) {
        String sql="INSERT INTO filiale (nome, indirizzo, orario_apertura, orario_chiusura, banca_id, direttore_id) VALUES ('"
                +f.getNome()+"', '"
                +f.getIndirizzo()+"', '"
                +f.getOrarioApertura()+"', '"
                +f.getOrarioChiusura()+"', '"
                +f.getBanca().getId()+"', '"
                +f.getDirettore().getId()+"'"+
                ")";
        return super.insert(sql);
    }

    public Filiale findById(int id) {
        Filiale f=new Filiale();
        
        try {
            ResultSet rs =super.findById("filiale", id);
            if(rs.next())
                f=setFilialeFromResultSet(rs);
        } catch (SQLException ex) {
            Logger.getLogger(FilialeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return f;
    }


    public boolean delete(Filiale f){
        return super.delete("filiale", f.getId());
    }

    public ArrayList<Filiale> findByBanca(Banca selectedBanca) {
        ArrayList<Filiale> al=new ArrayList<Filiale>();
        String sql="SELECT * FROM filiale WHERE (banca_id='"+selectedBanca.getId()+"');";
        ResultSet rs=super.query(sql);
        return getArrayListFromResultSet(rs);
    }

    public ArrayList<Filiale> findAll() {
        ResultSet rs=super.findAll("filiale");
        return getArrayListFromResultSet(rs);
    }

    private ArrayList<Filiale> getArrayListFromResultSet(ResultSet rs) {
        ArrayList<Filiale> al=new ArrayList<Filiale>();
        try {
            while(rs.next()) {
                Filiale b=setFilialeFromResultSet(rs);
                al.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return al;
    }

    public Filiale setFilialeFromResultSet(ResultSet rs) {
        Filiale f=new Filiale();
        try { 
            f.setId(rs.getInt("id"));
            f.setNome(rs.getString("nome"));
            f.setIndirizzo(rs.getString("indirizzo"));
            f.setOrarioApertura(rs.getString("orario_apertura"));
            f.setOrarioChiusura(rs.getString("orario_chiusura"));
            //Popolai membri della gerarchia
            int banca_id= 0;
            
                //Trova la banca di appartenenza della filiale
                banca_id = rs.getInt("banca_id");
                BancaDAO bdao=new BancaDAO();
                Banca banca=bdao.findById(banca_id);
                f.setBanca(banca);

                //Trova il direttore della filiale
                int utente_id=rs.getInt("direttore_id");
                UtenteDAO udao=new UtenteDAO();
                Utente utente=udao.findById(utente_id);
                f.setDirettore(utente);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }


}
