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

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import javafx.util.Duration;
import model.entities.Filiale;
import util.DbConnection;


public class Login {

    Utente utente=null;
    Banca banca=null;
    Filiale filiale=null;

    UtenteDAO utenteDAO=new UtenteDAO();
    BancaDAO bancaDAO=new BancaDAO();
    
    @FXML 
    private AnchorPane panel;

    @FXML
    ImageView imgBackground;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnLogin;
    
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
        
        txtUsername.textProperty().addListener(new ChangeListener<String>() {
        @Override
            public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {txtPassword.setDisable(false);}
        });
        
        txtPassword.textProperty().addListener(new ChangeListener<String>() {
        @Override
            public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {btnLogin.setDisable(false);}
        });
    }


    @FXML
    public void btnLoginAction(ActionEvent e){
        if(txtPassword.getText().isEmpty()) return;
        if(txtUsername.getText().isEmpty()) return;
        utente=utenteDAO.findByUsername(txtUsername.getText());
        if(utente==null) return;

        String pass_hash= MD5.dumpBytes(txtPassword.getText().getBytes());
        //If password is correct
        if(utente.getPassword().equals(pass_hash)) {
            
            utente=utenteDAO.findById(utente.getId()); //Caricamento completo di banca e filiale
            //Init Session
            Session.getInstance().setAppUtente(utente);            
            
            //Filtraggio ruolo
            if(utente.getRuolo().equals("Amministratore"))  {
                close(e);
                Session.getInstance().getSelectedBanca().setAmministratore(utente);
                Session.getInstance().openAsAmministratore();                
            }            
            else if(utente.getRuolo().equals("Direttore"))   {
                close(e);                
                Session.getInstance().setSelectedBanca(utente.getBanca());
                Session.getInstance().openAsDirettore();
            }
            else if(utente.getRuolo().equals("Cassiere"))  {
                close(e);
                Session.getInstance().setSelectedBanca(utente.getBanca());
                Session.getInstance().setSelectedFiliale(utente.getFiliale());                
                Session.getInstance().openAsCassiere();
            }
            else if(utente.getRuolo().equals("Cliente")){         
                close(e);
                Session.getInstance().setSelectedCliente(utente);
                Session.getInstance().setSelectedBanca(utente.getBanca());
                Session.getInstance().setSelectedFiliale(utente.getFiliale());                  
                Session.getInstance().openAsCliente();
            }
            else if(utente.getRuolo().equals(""))                return;
        }
        else {
            Session.getInstance().openErrorDialog("Errore credenziali", "Controlla le credenziali", "Le credenziali inserite non sono corrette");
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
        banca=cmbBanca.getValue();
        Session.getInstance().setSelectedBanca(banca);
        txtUsername.setDisable(false);
    }

    public void clickBackgroudImage(MouseEvent mouseEvent) {
        Session.getInstance().openRegistrazioneUtente();
    }
    
    public void showBankServices(MouseEvent mouseEvent) {
        Session.getInstance().openRegistrazioneUtente();
    }
    

    //Al primo avvio crea un amministratore con credenziali di default
    private void checkIfFirstRun() {
       
        //Inizializzazione Database
        /*
        boolean b_database_create=DbConnectionMysql.getInstance().createDatabase("homebanking");
        if(b_database_create){
            boolean b_schema=DbConnectionMysql.getInstance().createTables("homebanking","./homebanking.sql");
            if(!b_schema) Session.getInstance().openInfoDialog("Database error", "Errore di creazione del database", "Controlla i parametri nel file configuration.ini");
        }
          */  
        
        
        //Creazione utente di default admin
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
            
            txtUsername.setDisable(false);
        }
    }

    private void close(Event e) {        
        Window loginWindow=((Node)(e.getSource())).getScene().getWindow();
        loginWindow.hide();                
    }
    
}
