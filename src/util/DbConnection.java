package util;

/*
 * Classe dedicata alla gestione del Database.
 * Gestisce l'apertura e la chiusura della connessione col Database
 * Fornisce i metodi per l'esecuzione delle query sul Database
 */

import homebanking.Session;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DbConnection {

    private static Connection db=null;
    private static DbConnection instance=null;
    
    private Statement stmt;
    private ResultSet rs;
    private boolean b_connected=false;
        

    public static DbConnection getInstance() {
        try {
            if(instance == null)
                instance = new DbConnection();
            if((instance.db==null) || (!instance.db.isValid(1000))) instance.connect();
            return instance;
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // Apre la connessione con il Database
    private static boolean connect() {
        try {
            String engine=Session.getInstance().getConfig().getDb_engine();
            String username=Session.getInstance().getConfig().getDb_login();
            String password=Session.getInstance().getConfig().getDb_password();
            String server=Session.getInstance().getConfig().getDb_server();
            String port=Session.getInstance().getConfig().getDb_port();
            String dbname=Session.getInstance().getConfig().getDb_database();
            String url_db="";
            if(engine.equals("mysql")) {
                url_db="jdbc:mysql://localhost:3306/"+dbname+"?user="+username+"&password="+password+"&useLegacyDatetimeCode=false&serverTimezone=Europe/Rome";                     
                Class.forName("com.mysql.cj.jdbc.Driver");
            }
            if(engine.equals("derby")) {
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                url_db="jdbc:derby:://localhost:1527:homebanking";
                
            }
            
            db = DriverManager.getConnection(url_db);            
            DbConnection.getInstance().setB_connected(db.isValid(2000));
            return DbConnection.getInstance().isB_connected();            

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public Statement createStatement() {
        try {
            if(db.isValid(1000))
            return db.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public boolean createTables(String db_name, String filename) {
       
        try {
            if(db.isValid(1000)) db.close();
            
            String username=Session.getInstance().getConfig().getDb_login();
            String password=Session.getInstance().getConfig().getDb_password();
            String server=Session.getInstance().getConfig().getDb_server();
            String port=Session.getInstance().getConfig().getDb_port();
            String dbname=Session.getInstance().getConfig().getDb_database();
            
                
            String url_db="jdbc:mysql://localhost:3306/"+dbname+"?user="+username+"&password="+password+"&useLegacyDatetimeCode=false&serverTimezone=Europe/Rome";         
            Class.forName("com.mysql.cj.jdbc.Driver");
            db = DriverManager.getConnection(url_db);   
            String sql_schema;
            sql_schema = Files.readString(Paths.get(filename), StandardCharsets.US_ASCII);
            return execute(sql_schema);                
        } catch (Exception ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }                                
        return false;
    }

    public boolean createDatabase(String db_name) {
        try {
            
            String username=Session.getInstance().getConfig().getDb_login();
            String password=Session.getInstance().getConfig().getDb_password();
            String server=Session.getInstance().getConfig().getDb_server();
            String port=Session.getInstance().getConfig().getDb_port();
            String dbname=Session.getInstance().getConfig().getDb_database();
            
            String url_db="jdbc:mysql://localhost:3306/?user="+username+"&password="+password+"&useLegacyDatetimeCode=false&serverTimezone=Europe/Rome";         
            Class.forName("com.mysql.cj.jdbc.Driver");
            db = DriverManager.getConnection(url_db);            
            if(db.isValid(2000))
            {
                stmt=getPreparedStatement("CREATE DATABASE IF NOT EXISTS "+db_name);
                stmt.execute("CREATE DATABASE IF NOT EXISTS "+db_name);
                return true;
            }
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
            if(stmt==null || stmt.isClosed()) 
                stmt=getPreparedStatement(command);
            
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
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * @return the b_connected
     */
    public boolean isB_connected() {
        return b_connected;
    }

    /**
     * @param b_connected the b_connected to set
     */
    public void setB_connected(boolean b_connected) {
        this.b_connected = b_connected;
    }
}
