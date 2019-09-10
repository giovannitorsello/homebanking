package model.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import model.entities.Gallery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.entities.Banca;
import model.entities.Filiale;

public class GalleryDAO extends ObjectDAO {

    public boolean insert(Gallery g) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String str_data_inserimento=sdf.format(g.getData_inserimento());
        PreparedStatement ps = super.getPreparedStatement("INSERT INTO gallery (descrizione, image, data_inserimento, banca_id, filiale_id) VALUES (?,?,?,?,?);");
        
        try {
            ps.setString(1,g.getDescrizione());
            ps.setBlob(2,g.getImage());
            ps.setDate(3, new java.sql.Date((new Date()).getTime()));
            ps.setInt(4,g.getBanca().getId());
            ps.setInt(5,g.getBanca().getId());
            ps.execute();
            return true;
        } catch (Exception ex) {
            Logger.getLogger(GalleryDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }        
    }

    public boolean update(Gallery g) {
        
        String sql="UPDATE gallery SET descrizione=?, image=?, data_inserimento=?, banca_id=?, filiale_id=? WHERE id='"+g.getId()+"'";
        PreparedStatement ps = super.getPreparedStatement(sql);        
        try {
            ps.setString(1,g.getDescrizione());
            ps.setBlob(2,g.getImage());
            ps.setDate(3, new java.sql.Date((new Date()).getTime()));
            ps.setInt(4,g.getBanca().getId());
            ps.setInt(5,g.getBanca().getId());
            ps.execute();
            return true;
        } catch (Exception ex) {
            Logger.getLogger(GalleryDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }                
    }
    
    
    public Gallery findById(int id) {
        Gallery gallery=new Gallery();
        ResultSet rs =super.findById("gallery", id);
        try {
            if(rs.next()) gallery=setGalleryFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gallery;
    }

    public boolean delete(Gallery gallery) {
        return super.delete("gallery", gallery.getId());
    }

    public ArrayList<Gallery> findAll() {

        ResultSet rs = super.findAll("prodotto");
        ArrayList<Gallery> listaProdotti = getArrayListFromResultSet(rs);        
        return listaProdotti;
    }
    
    public ArrayList<Gallery> findByBanca(Banca banca) {
        String sql="SELECT * FROM gallery WHERE (banca_id='"+banca.getId()+"');";
        ResultSet rs=super.query(sql);
        return getArrayListFromResultSet(rs);
    }
    
    public ArrayList<Gallery> findByFiliale(Filiale filiale) {
        String sql="SELECT * FROM gallery WHERE (banca_id='"+filiale.getId()+"');";
        ResultSet rs=super.query(sql);
        return getArrayListFromResultSet(rs);
    }

    private Gallery setGalleryFromResultSet(ResultSet rs) {
        Gallery g=new Gallery();
        
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");        
        try {
            String str_data_inserimento=sdf.format(rs.getDate("data_inserimento"));
        
            g.setId((Integer) rs.getInt("id"));
            g.setData_inserimento(rs.getDate("data_inserimento"));
            g.setDescrizione(rs.getString("descrizione"));
            
            g.setImage((InputStream) rs.getBinaryStream("image"));
            
            //Find banca
            int banca_id = rs.getInt("banca_id");
            if(banca_id!=-1){
                BancaDAO bdao=new BancaDAO();
                Banca banca=bdao.findById(banca_id);
                g.setBanca(banca);
            }
            
            //Find filiale
            int filiale_id = rs.getInt("filiale_id");
            if(filiale_id!=-1){
                FilialeDAO bdao=new FilialeDAO();
                Filiale filiale=bdao.findById(filiale_id);
                g.setFiliale(filiale);
                g.setBanca(filiale.getBanca());                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return g;
    }
    
    public String getFileImageFromDatabase(Gallery g) {
        // write binary stream into file
        String filename="temp"+(new Date()).toString();
        File file = new File(filename);
            
            System.out.println("Writing to file " + file.getAbsolutePath());
                FileOutputStream output;
        try {
            output = new FileOutputStream(file);
            InputStream input = g.getImage();
            byte[] buffer = new byte[1024];
            while (input.read(buffer) > 0) {output.write(buffer);}
            
        } catch (Exception ex) {
            Logger.getLogger(GalleryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return filename;
    }
    
    private ArrayList<Gallery> getArrayListFromResultSet(ResultSet rs) {
        ArrayList<Gallery> al=new ArrayList<Gallery>();
        try {
            while(rs.next()) {
                Gallery p=setGalleryFromResultSet(rs);
                al.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return al;
    }
        
}
