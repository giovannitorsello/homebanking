package model.dao;

import model.entities.Banca;
import model.entities.Utente;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BancaDAO extends ObjectDAO {


    public boolean insert(Banca b) {
        String sql="INSERT INTO banca (nome, indirizzo, amministratore_id, direttore_id) VALUES ("+
                "'"+b.getNome()+ "',"+
                "'"+b.getIndirizzo()+ "',"+
                "'"+b.getAmministratore().getId()+ "',"+
                "'"+b.getDirettore().getId()+ "'"+
                ")";

        return super.insert(sql);
    }

    public Banca findById(int id) {
        Banca b=new Banca();
        
        try {
            ResultSet result =super.findById("banca", id);
            result.next();
            b=setBancaFromResultSet(result);
        } catch (SQLException ex) {
            Logger.getLogger(FilialeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return b;
    }

    public boolean delete(Banca banca) {
        return super.delete("banca", banca.getId());
    }

    public ArrayList<Banca> findAll() {
        ArrayList<Utente> al=new ArrayList<Utente>();
        String sql="SELECT * FROM banca;";
        ResultSet rs=super.query(sql);
        return getArrayListFromResultSet(rs);
    }

    public Banca setBancaFromResultSet(ResultSet rs) {
        Banca b=new Banca();
        try {
            b.setId(rs.getInt("id"));
            b.setNome(rs.getString("nome"));
            b.setIndirizzo(rs.getString("indirizzo"));
            //Trova l'amministratore della banca
            int amministratore_id=rs.getInt("amministratore_id");
            int direttore_id=rs.getInt("direttore_id");

            UtenteDAO udao = new UtenteDAO();
            Utente amministratore = udao.findById(amministratore_id);
            b.setAmministratore(amministratore);

            //Trova il direttore
            if(direttore_id!=0)
            {
                Utente direttore = udao.findById(direttore_id);
                b.setDirettore(direttore);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }
    
    public ArrayList<Banca> findByAmministratore(Utente amministratore) {
        ArrayList<Utente> al=new ArrayList<Utente>();
        String sql="SELECT * FROM banca WHERE (direttore_id='"+amministratore.getId()+"');";
        ResultSet rs=super.query(sql);
        return getArrayListFromResultSet(rs);
    }

    public ArrayList<Banca> findByDirettore(Utente direttore) {
        ArrayList<Utente> al=new ArrayList<Utente>();
        String sql="SELECT * FROM banca WHERE (direttore_id='"+direttore.getId()+"');";
        ResultSet rs=super.query(sql);
        return getArrayListFromResultSet(rs);
    }

    private ArrayList<Banca> getArrayListFromResultSet(ResultSet rs) {
        ArrayList<Banca> al=new ArrayList<Banca>();
        try {
            while(rs.next()) {
                Banca b=setBancaFromResultSet(rs);
                al.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return al;
    }
}
