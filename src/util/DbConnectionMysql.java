package util;

/*
 * Classe dedicata alla gestione del Database.
 * Gestisce l'apertura e la chiusura della connessione col Database
 * Fornisce i metodi per l'esecuzione delle query sul Database
 */

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DbConnectionMysql {

    private static Connection db=null;
    private static DbConnectionMysql instance=null;
    private Statement stmt;
    private ResultSet rs;

    public static DbConnectionMysql getInstance() {
        try {
            if(instance == null)
                instance = new DbConnectionMysql();
            if((db==null) || (!db.isValid(1000)))
                connect("homebanking", "homebanking", "homebanking");
            return instance;
        } catch (SQLException ex) {
            Logger.getLogger(DbConnectionMysql.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // Apre la connessione con il Database
    private static boolean connect(String dbname, String username, String password) {

        try {
            String url="jdbc:mysql://localhost:3306/"+dbname+"?user="+username+"&password="+password+"&useLegacyDatetimeCode=false&serverTimezone=UTC";
            Class.forName("com.mysql.cj.jdbc.Driver");
            db = DriverManager.getConnection(url);
            return db.isValid(1000);            

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

        
    public boolean insert(String sql) {
        return execute(sql);
    }
    
    public boolean update(String sql) {
        return execute(sql);
    }
        
    public boolean delete(String sql){
        return execute(sql);
    }
        
    public boolean execute(String command) {
        try {            
            //if((stmt!=null) && (!stmt.isClosed())) stmt.close();
            stmt.execute(command);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }        
    }
    
    public ResultSet query(String query) {
        try {
            //chiusura preventiva per i precedenti comandi
            //if((rs!=null) && (!rs.isClosed())) stmt.close();        
            //if((stmt!=null) && (!stmt.isClosed())) stmt.close();                    
            stmt = db.createStatement();
            rs = stmt.executeQuery(query);
            return rs;
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public PreparedStatement getPreparedStatement(String sql) {
        try {
            return db.prepareStatement(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DbConnectionMysql.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
