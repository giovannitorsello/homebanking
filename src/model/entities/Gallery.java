package model.entities;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Date;

public class Gallery {

    private int id=-1;
    private String descrizione="";
    private InputStream image=null;
    private Date data_inserimento=new Date();
    private Banca banca;
    private Filiale filiale;

    public Gallery(){

    }

    public String getDescrizione() {
        return descrizione;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public InputStream getImage() {
        return image;
    }

    public void setImage(InputStream image) {
        this.image = image;
    }

    public Date getData_inserimento() {
        return data_inserimento;
    }

    public void setData_inserimento(Date data_inserimento) {
        this.data_inserimento = data_inserimento;
    }

    /**
     * @return the banca
     */
    public Banca getBanca() {
        return banca;
    }

    /**
     * @param banca the banca to set
     */
    public void setBanca(Banca banca) {
        this.banca = banca;
    }

    /**
     * @return the filiale
     */
    public Filiale getFiliale() {
        return filiale;
    }

    /**
     * @param filiale the filiale to set
     */
    public void setFiliale(Filiale filiale) {
        this.filiale = filiale;
    }
}
