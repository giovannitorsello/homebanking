package model.dao;

import util.DbConnection;
import model.entities.Utente;

import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjectDAO {

    private static ObjectDAO instance;

    public static ObjectDAO getInstance() {
        if(instance == null)
            instance = new ObjectDAO();
        return instance;
    }

    public static void createSchema(String filename_sql) {
        ObjectDAO odao=getInstance();
        try {
            File file_sql=new File(filename_sql);
            FileInputStream fin=new FileInputStream(file_sql);
            byte [] buf=new byte[(int) file_sql.length()];
            fin.read(buf, 0, (int) file_sql.length());
            String command=new String(buf);
            DbConnection.getInstance().execute(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  ResultSet findById(String tblName, int id) {
        Utente u = new Utente();
        String sql = "SELECT * FROM " +tblName+ " WHERE id="+id+";";
        ResultSet result = DbConnection.getInstance().query(sql);
        return result;
    }

    public ResultSet findAll(String tblName) {
        String sql="SELECT * FROM " +tblName;
        ResultSet result = DbConnection.getInstance().query(sql);
        return result;
    }

    public boolean insert(String sql) {
        return DbConnection.getInstance().insert(sql);
    }
    
    public boolean update(String sql) {
        return DbConnection.getInstance().insert(sql);
    }

    public ResultSet query(String sql) {
        ResultSet result = DbConnection.getInstance().query(sql);
        return result;
    }

    public boolean delete(String tblName, int id) {
        String sql="DELETE FROM " +tblName + " WHERE id="+id;
        return DbConnection.getInstance().delete(sql);
    }

    public int getNumRecords(String tblName) {
        String sql="SELECT COUNT(*) FROM "+tblName;
        ResultSet result = DbConnection.getInstance().query(sql);
        try {
            result.next();
            return result.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void loadExampleData(String filename_sql) {
        ObjectDAO odao=getInstance();
        try {
            File file_sql=new File(filename_sql);
            FileInputStream fin=new FileInputStream(file_sql);
            byte [] buf=new byte[(int) file_sql.length()];
            fin.read(buf, 0, (int) file_sql.length());
            String command=new String(buf);
            DbConnection.getInstance().execute(command);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    PreparedStatement getPreparedStatement(String sql) {
        return DbConnection.getInstance().getPreparedStatement(sql);
    }
}
