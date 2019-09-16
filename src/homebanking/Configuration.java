/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homebanking;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ini4j.Ini;

/**
 *
 * @author torsello
 */
public class Configuration {
    
    //Database
   private String db_server="";
   private String db_port="";
   private String db_login="";
   private String db_password="";
   private String db_url="";    
   private String db_database="";
   private String db_engine="";
   
//Pdf
   private String pdf_program="";
            
   //Mail      
   private String mail_server="";
   private String mail_port="";
   private String mail_login="";
   private String mail_password="";
   private String auth_type="";

   public Configuration() {
       readFromIniFile("configuration.ini");
   }
   
   private void readFromIniFile(String fileini) {
       try {
           Ini ini = new Ini(new File(fileini));
           db_server=ini.get("database_server", "server");
           db_port=ini.get("database_server", "port");
           db_login=ini.get("database_server", "login");
           db_password=ini.get("database_server", "password");
           db_database=ini.get("database_server", "database");
           db_url=ini.get("database_server", "url");
           db_engine=ini.get("database_server", "engine");
           
           pdf_program=ini.get("pdf_reader", "program");
           
           mail_server=ini.get("mail_server", "server");
           mail_port=ini.get("mail_server", "port");
           mail_login=ini.get("mail_server", "login");
           mail_password=ini.get("mail_server", "password");
           auth_type=ini.get("mail_server", "auth_type");
           
       } catch (IOException ex) {
           Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
       }
       
   }
   
    /**
     * @return the db_server
     */
    public String getDb_server() {
        return db_server;
    }

    /**
     * @param db_server the db_server to set
     */
    public void setDb_server(String db_server) {
        this.db_server = db_server;
    }

    /**
     * @return the db_port
     */
    public String getDb_port() {
        return db_port;
    }

    /**
     * @param db_port the db_port to set
     */
    public void setDb_port(String db_port) {
        this.db_port = db_port;
    }

    /**
     * @return the db_login
     */
    public String getDb_login() {
        return db_login;
    }

    /**
     * @param db_login the db_login to set
     */
    public void setDb_login(String db_login) {
        this.db_login = db_login;
    }

    /**
     * @return the db_password
     */
    public String getDb_password() {
        return db_password;
    }

    /**
     * @param db_password the db_password to set
     */
    public void setDb_password(String db_password) {
        this.db_password = db_password;
    }

    /**
     * @return the pdf_program
     */
    public String getPdf_program() {
        return pdf_program;
    }

    /**
     * @param pdf_program the pdf_program to set
     */
    public void setPdf_program(String pdf_program) {
        this.pdf_program = pdf_program;
    }

    /**
     * @return the mail_server
     */
    public String getMail_server() {
        return mail_server;
    }

    /**
     * @param mail_server the mail_server to set
     */
    public void setMail_server(String mail_server) {
        this.mail_server = mail_server;
    }

    /**
     * @return the mail_port
     */
    public String getMail_port() {
        return mail_port;
    }

    /**
     * @param mail_port the mail_port to set
     */
    public void setMail_port(String mail_port) {
        this.mail_port = mail_port;
    }

    /**
     * @return the mail_login
     */
    public String getMail_login() {
        return mail_login;
    }

    /**
     * @param mail_login the mail_login to set
     */
    public void setMail_login(String mail_login) {
        this.mail_login = mail_login;
    }

    /**
     * @return the mail_password
     */
    public String getMail_password() {
        return mail_password;
    }

    /**
     * @param mail_password the mail_password to set
     */
    public void setMail_password(String mail_password) {
        this.mail_password = mail_password;
    }

    /**
     * @return the db_url
     */
    public String getDb_url() {
        return db_url;
    }

    /**
     * @param db_url the db_url to set
     */
    public void setDb_url(String db_url) {
        this.db_url = db_url;
    }

    /**
     * @return the auth_type
     */
    public String getAuth_type() {
        return auth_type;
    }

    /**
     * @param auth_type the auth_type to set
     */
    public void setAuth_type(String auth_type) {
        this.auth_type = auth_type;
    }

    /**
     * @return the db_database
     */
    public String getDb_database() {
        return db_database;
    }

    /**
     * @param db_database the db_database to set
     */
    public void setDb_database(String db_database) {
        this.db_database = db_database;
    }

    /**
     * @return the db_engine
     */
    public String getDb_engine() {
        return db_engine;
    }

    /**
     * @param db_engine the db_engine to set
     */
    public void setDb_engine(String db_engine) {
        this.db_engine = db_engine;
    }
    
}
