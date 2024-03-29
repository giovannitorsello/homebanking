package model.dao;

import model.entities.Filiale;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import model.entities.Banca;
import model.entities.Utente;

public class UtenteDAO extends ObjectDAO {


    public boolean insert(Utente u) {

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String str_data_nascita=sdf.format(u.getData_nascita());
        String str_data_registrazione=sdf.format(u.getData_registrazione());

        String sql="INSERT INTO utente (data_registrazione, nome, cognome, indirizzo, email, username, password, ruolo," +
                "codice_fiscale, partitaiva, pec, codice_univoco, filiale_id, banca_id, data_nascita) VALUES ('"
                +str_data_registrazione+"', '"
                +u.getNome()+"', '"
                +u.getCognome()+"', '"
                +u.getIndirizzo()+"', '"
                +u.getEmail()+"', '"
                +u.getUsername()+"', '"
                +u.getPassword()+"', '"
                +u.getRuolo()+"', '"
                +u.getCodice_fiscale()+"', '"
                +u.getPartitaiva()+"', '"
                +u.getPec()+"', '"
                +u.getCodice_univoco()+"', '"
                +u.getFiliale().getId()+"', '"
                +u.getBanca().getId()+"', '"
                +str_data_nascita+"')";
        return super.insert(sql);
    }
    
    public boolean update(Utente u) {
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String str_data_nascita=sdf.format(u.getData_nascita());
        String str_data_registrazione=sdf.format(u.getData_registrazione());

        String sql="UPDATE utente SET "+
                "data_registrazione='"+str_data_registrazione+"',"+
                "nome='"+u.getNome()+"',"+
                "cognome='"+u.getCognome()+"',"+
                "data_nascita='"+str_data_nascita+"',"+
                "indirizzo='"+u.getIndirizzo()+"',"+
                "email='"+u.getEmail()+"',"+
                "username='"+u.getUsername()+"',"+
                "password='"+u.getPassword()+"',"+
                "ruolo='"+u.getRuolo()+"',"+
                "codice_fiscale='"+u.getCodice_fiscale()+"',"+
                "partitaiva='"+u.getPartitaiva()+"',"+
                "pec='"+u.getPec()+"',"+
                "codice_univoco='"+u.getCodice_univoco()+"',"+
                "filiale_id='"+u.getFiliale().getId()+"',"+
                "banca_id='"+u.getBanca().getId()+"',"+
                "data_nascita='"+str_data_nascita+"'"+
                " WHERE id='"+u.getId()+"'";
        
        return super.update(sql);
    }
    
    public boolean delete(Utente u) {
        return super.delete("utente", u.getId());
    }

    public Utente findById(int id) {
        Utente u=new Utente();
        ResultSet rs =super.findById("utente", id);
        try {
            if(rs.next()) u=setUtenteFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }

    public Utente findUtenteByField(String fieldname, String fieldvalue) {
        Utente u=new Utente();
        String sql = "SELECT * FROM utente WHERE ("+ fieldname+"='"+fieldvalue+"');";
        ResultSet result = super.query(sql);
        try {
            if(result.next()) return setUtenteFromResultSet(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Utente setUtenteFromResultSet(ResultSet result) {
        Utente u=new Utente();
        try {
            u.setId(result.getInt("id"));
            u.setData_registrazione(result.getDate("data_registrazione"));
            u.setNome(result.getString("nome"));
            u.setCognome(result.getString("cognome"));
            u.setIndirizzo(result.getString("indirizzo"));
            u.setEmail(result.getString("email"));
            u.setUsername(result.getString("username"));
            u.setPassword(result.getString("password"));
            u.setRuolo(result.getString("ruolo"));
            u.setCodice_fiscale(result.getString("codice_fiscale"));
            u.setPartitaiva(result.getString("partitaiva"));
            u.setPec(result.getString("pec"));
            u.setCodice_univoco(result.getString("codice_univoco"));
            u.setData_nascita(result.getDate("data_nascita"));

            //caricamento banca di appartenenza
            try
            {
                BancaDAO bancaDAO=new BancaDAO();
                int banca_id=result.getInt("banca_id");
                Banca b=bancaDAO.findById(banca_id);
                u.setBanca(b);                
            }
            catch(Exception e){e.printStackTrace();}
            
            //caricamento filiale di appartenenza
            try
            {
                FilialeDAO filialeDAO=new FilialeDAO();
                int filiale_id=result.getInt("filiale_id");
                Filiale f=filialeDAO.findById(filiale_id);
                u.setFiliale(f);                
            }
            catch(Exception e){e.printStackTrace();}

            
            return u;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return u;
    }


    public Utente findUtenteByCodiceFiscale(String codfis) {
        return findUtenteByField("codice_fiscale", codfis);
    }
    
    public Utente findUtenteByEmail(String email) {
        return findUtenteByField("email", email);
    }

    public Utente findByUsername(String username) {
        return findUtenteByField("username", username);
    }

    public ArrayList<Utente> findAll() {

        ResultSet rs = super.findAll("utente");
        while(true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        ArrayList<Utente> listaUtenti = new ArrayList<>();

        return listaUtenti;
    }

    boolean cambiaSatoRegistrazione(Utente utente, int statoRegistrazione){
        String sql = "UPDATE utente "+"SET ruolo = '"+statoRegistrazione+"' "+"WHERE id = '"+utente.getId()+"'";
        //Rivedere command al posto di query
        return false;
    }

    public boolean checkCredential(Utente u) {
        //implementare check sugli hash della password.
        return true;
    }

    public int getNumUtenti() {
        return super.getNumRecords("utente");
    }
    
    public ArrayList<Utente> findDirettoreByBanca(Banca selectedBanca) {
        ArrayList<Utente> al=new ArrayList<Utente>();
        String sql="SELECT * FROM utente WHERE (banca_id='"+selectedBanca.getId()+"' AND ruolo='Direttore');";
        ResultSet rs=super.query(sql);
        return getArrayListFromResultSet(rs);
    }

    public ArrayList<Utente> findDirettoreByFiliale(Filiale selectedFiliale) {
        ArrayList<Utente> al=new ArrayList<Utente>();
        String sql="SELECT * FROM utente WHERE (filiale_id='"+selectedFiliale.getId()+"' AND ruolo='Direttore');";
        ResultSet rs=super.query(sql);
        return getArrayListFromResultSet(rs);
    }
    
    public ArrayList<Utente> findClienteByFiliale(Filiale selectedFiliale) {
        ArrayList<Utente> al=new ArrayList<Utente>();
        String sql="SELECT * FROM utente WHERE (filiale_id='"+selectedFiliale.getId()+"' AND ruolo='Cliente');";
        ResultSet rs=super.query(sql);
        return getArrayListFromResultSet(rs);
    }

    public ArrayList<Utente> findClienteByBanca(Banca selectedBanca) {
        ArrayList<Utente> al=new ArrayList<Utente>();
        String sql="SELECT * FROM utente WHERE (banca_id='"+selectedBanca.getId()+"' AND ruolo='Cliente');";
        ResultSet rs=super.query(sql);
        return getArrayListFromResultSet(rs);
    }
    
    public ArrayList<Utente> findCassiereByFiliale(Filiale selectedFiliale) {
        ArrayList<Utente> al=new ArrayList<Utente>();
        String sql="SELECT * FROM utente WHERE (filiale_id='"+selectedFiliale.getId()+"' AND ruolo='Cassiere');";
        ResultSet rs=super.query(sql);
        return getArrayListFromResultSet(rs);
    }

    public ArrayList<Utente> findByFiliale(Filiale selectedFiliale) {
        ArrayList<Filiale> al=new ArrayList<Filiale>();
        String sql="SELECT * FROM utente WHERE (filiale_id='"+selectedFiliale.getId()+"');";
        ResultSet rs=super.query(sql);
        return getArrayListFromResultSet(rs);
    }

    private ArrayList<Utente> getArrayListFromResultSet(ResultSet rs) {
        ArrayList<Utente> al=new ArrayList<Utente>();
        try {
            while(rs.next()) {
                Utente u=setUtenteFromResultSet(rs);
                al.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return al;
    }

   
    

}
