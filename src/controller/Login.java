package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import model.dao.BancaDAO;
import model.dao.UtenteDAO;
import model.entities.Banca;
import model.entities.Utente;
import util.MD5;
import homebanking.Session;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;
import util.DbConnectionMysql;


public class Login {

    Utente selectedUtente=null;
    Banca selectedBanca=null;

    UtenteDAO utenteDAO=new UtenteDAO();
    BancaDAO bancaDAO=new BancaDAO();

    @FXML
    ImageView imgBackground;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private ComboBox<Banca> cmbBanca;

    @FXML
    private Label lblServizi;

    public Login()
    {
    }

    @FXML
    private void initialize()
    {
        checkIfFirstRun();
        initBackgroundImage();
        initCmbBanca();
        initLabelServizi();
    }


    @FXML
    public void btnLoginAction(){
        if(txtPassword.getText().isEmpty()) return;
        if(txtUsername.getText().isEmpty()) return;
        selectedUtente=utenteDAO.findUsername(txtUsername.getText());
        if(selectedUtente==null) return;

        String pass_hash= MD5.dumpBytes(txtPassword.getText().getBytes());
        //If password is correct
        if(selectedUtente.getPassword().equals(pass_hash)) {
            //Store User in session singleton
            Session.getInstance().setAppUtente(selectedUtente);
            Session.getInstance().setSelectedBanca(selectedBanca);

            if(selectedBanca!=null) Session.getInstance().getSelectedBanca().setAmministratore(selectedUtente);

            if(selectedUtente.getRuolo().equals("Amministratore"))  Session.getInstance().openAsAmministratore();
            if(selectedUtente.getRuolo().equals("Direttore"))       Session.getInstance().openAsDirettore();
            if(selectedUtente.getRuolo().equals("Cassiere"))        Session.getInstance().openAsCassiere();
            if(selectedUtente.getRuolo().equals("Cliente"))         Session.getInstance().openAsCliente();
            if(selectedUtente.getRuolo().equals(""))                return;

        }

    }

    private void initLabelServizi() {
        /*FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.1), lblServizi);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);        
        fadeTransition.setCycleCount(Animation.INDEFINITE);
        fadeTransition.play();
        */
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), evt -> lblServizi.setVisible(false)),
                                 new KeyFrame(Duration.seconds( 0.3), evt -> lblServizi.setVisible(true)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        
        
    }
    private void initBackgroundImage() {
        try {
            imgBackground.setImage(new Image(new FileInputStream("./images/background.jpeg")));
        }
        catch(Exception e) {e.printStackTrace();}
    }

    private void initCmbBanca() {

        Callback<ListView<Banca>, ListCell<Banca>> cellFactory = new Callback<ListView<Banca>, ListCell<Banca>>() {
            @Override
            public ListCell<Banca> call(ListView<Banca> l) {
                return new ListCell<Banca>() {
                    @Override
                    protected void updateItem(Banca item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getId() + "---" + item.getNome());
                        }
                    }
                };
            }
        };
        cmbBanca.setButtonCell(cellFactory.call(null));
        cmbBanca.setCellFactory(cellFactory);

        ArrayList<Banca> arrayListBanche=bancaDAO.findAll();
        ObservableList<Banca> options =FXCollections.observableArrayList(arrayListBanche);
        cmbBanca.setItems(options);

    }

    public void changeCmbBanca(ActionEvent e) {
        selectedBanca=cmbBanca.getValue();
        Session.getInstance().setSelectedBanca(selectedBanca);
    }

    public void changeUsername(ActionEvent e) {
        System.out.println(txtUsername.getText());
    }

    public void changePassword(ActionEvent e) {
        System.out.println(txtPassword.getText());
    }

    public void clickBackgroudImage(MouseEvent mouseEvent) {
        System.out.println("Click immagine");
    }
    
    public void showBankServices(MouseEvent mouseEvent) {
        Session.getInstance().openRegistrazioneUtente();
    }
    

    //Al priumo avvio crea un amministratore con credenziali di default
    private void checkIfFirstRun() {
       
        /*
        try {
            String sql_schema = Files.readString(Paths.get("./schema.sql"), StandardCharsets.US_ASCII);
            DbConnectionMysql.getInstance().execute(sql_schema);
        } catch (IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        
        
        int num_utenti = utenteDAO.getNumUtenti();
        if (num_utenti == 0) {
            //No users first run
            Utente firstAdmin = new Utente();
            firstAdmin.setNome("First");
            firstAdmin.setNome("Admin");
            firstAdmin.setUsername("admin");
            firstAdmin.setPassword(MD5.getStringHash("admin"));
            firstAdmin.setRuolo("Amministratore");
            firstAdmin.setCodice_fiscale("FRTAMM");
            firstAdmin.setData_nascita(new Date());
            firstAdmin.setEmail("first.admin@domain.com");
            firstAdmin.setPartitaiva("000000000000");
            firstAdmin.setPec("first.admin@domain.com");
            firstAdmin.setCodice_univoco("UUUUU");
            firstAdmin.setData_registrazione(new Date());
            utenteDAO.insert(firstAdmin);
        }
    }

    
}
