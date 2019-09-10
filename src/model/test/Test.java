package model.test;
import model.dao.BancaDAO;
import model.dao.GalleryDAO;
import model.dao.UtenteDAO;
import model.entities.Banca;
import model.entities.Gallery;
import model.entities.Utente;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    public static void main(String[] args) {
        //ObjectDAO.createSchema("./homebanking.sql");
        //ObjectDAO.getInstance().loadExampleData("./example_data.sql");

        Test t=new Test(); t.testUtente();

    }


    public void testUtente() {
        //Init DAO Object
        UtenteDAO u_dao= new UtenteDAO();

        //Init Model Object
        Utente u=new Utente();
        u.setNome("Giovanni");
        u.setCognome("Torsello");
        u.setData_registrazione(new Date());
        u.setIndirizzo("Via Pertini 1");
        u.setEmail("giovanni.torsello@gmail.com");
        u.setUsername("giovanni.torsello@gmail.com");
        u.setPassword("qwerty09");
        u.setRuolo("Cliente");
        u.setCodice_fiscale("DLRMTN98M64D862E");
        u.setPartitaiva("null");
        u.setPec("null");
        u.setCodice_univoco("124ese");
        try {
            u.setData_nascita(sdf.parse("2003-01-01"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        //Test insert
        u_dao.insert(u);
        //Test delete
        //Test select

    }

    public void testBanca(){

        BancaDAO b_dao = new BancaDAO();

        Banca b = new Banca();
        b.setNome("Bravoos Bank");
        b.setIndirizzo("Lecce, Via Pertini, 1");


        b_dao.insert(b);
    }

    public void testGallery(){

        GalleryDAO g_dao = new GalleryDAO();

        Gallery g = new Gallery();
        g.setDescrizione("blah blah");
        g.setImage(null);
        try {
            g.setData_inserimento(sdf.parse("2003-04-20"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        g_dao.insert(g);

    }
}
