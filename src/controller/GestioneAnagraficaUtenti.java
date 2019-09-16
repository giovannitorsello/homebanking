package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.dao.UtenteDAO;
import model.entities.Utente;
import util.TimeUtil;
import homebanking.Session;

import java.util.ArrayList;
import java.util.Date;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TablePosition;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import model.dao.BancaDAO;
import model.dao.FilialeDAO;
import model.dao.ProdottoClienteDAO;
import model.dao.ServizioClienteDAO;
import model.entities.Banca;
import model.entities.Filiale;
import model.entities.Servizio;
import model.entities.ServizioCliente;
import util.MD5;

public class GestioneAnagraficaUtenti {

    private Utente utenteLogin=new Utente();    
    private Utente utente=new Utente();
    
    private Banca  banca= new Banca();
    private Filiale filiale= new Filiale();
    private ServizioCliente servizioCliente=new ServizioCliente();
    private Servizio servizio=new Servizio();
    
    
    private UtenteDAO utenteDAO=new UtenteDAO();
    private FilialeDAO filialeDAO=new FilialeDAO();    
    private ServizioClienteDAO servizioClienteDAO=new ServizioClienteDAO();
    private ProdottoClienteDAO prodottoClienteDAO=new ProdottoClienteDAO();
    
    private ObservableList<Utente> tblData = FXCollections.observableArrayList();
    private ObservableList<Servizio> tblDataServizioCliente = FXCollections.observableArrayList();

    
    @FXML 
    private AnchorPane panel;
    
    @FXML
    Button btnLogout;
    
    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtCognome;

    @FXML
    private TextField txtCodiceFiscale;

    @FXML
    private TextField txtIndirizzo;

    @FXML
    private TextField txtEmail;

    @FXML
    private DatePicker dtDataNascita;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtPartitaIva;
    
    @FXML
    private TextField txtCodiceUnivoco;
    
    @FXML
    private TextField txtPec;
    
    @FXML
    private ComboBox cmbRuolo;    
    
    @FXML
    private ComboBox<Filiale> cmbFiliale;    

    @FXML
    private TableView tblUtenti;

    @FXML
    private TableView tblServiziCliente;
    
    @FXML
    private Button btnInsert;

    @FXML
    private Button btnFind;
    
    @FXML
    private Button btnDelete;
    
    @FXML
    private Button btnConfirmServizioCliente;
    
    @FXML
    private Label lblUtente;
    
    @FXML
    private Label lblBanca;
    
    @FXML
    private Label lblFiliale;
    
    BancaDAO bancaDAO=new BancaDAO();
    Utente direttore=new Utente();
    
    
    public GestioneAnagraficaUtenti(){

    }
    
    @FXML
    private void initialize()
    {
        //Get utente login
        utenteLogin=Session.getInstance().getAppUtente();
        filiale=Session.getInstance().getSelectedFiliale();
        banca=Session.getInstance().getSelectedBanca();
        
        if(utenteLogin.getRuolo().equals("Amministratore")) 
        {            
            //Corregge una iptetica NON selezione della filiale (es. accesso a Utenti da schermata Banca);
            if(filiale.getId()==-1 && banca.getId()!=-1) {
                ArrayList<Filiale> arrayListFiliali=new ArrayList<Filiale>();                
                arrayListFiliali=filialeDAO.findByBanca(banca);
                if(arrayListFiliali.size()>0) {
                    filiale=arrayListFiliali.get(0);
                    Session.getInstance().setSelectedFiliale(new Filiale());
                }
            }
            
        }
        
        if(utenteLogin.getRuolo().equals("Direttore")) {
            //Selezione banca da direttore
            direttore=Session.getInstance().getAppUtente();
            ArrayList<Banca> arrayListBanca=new ArrayList<Banca>();
            if(arrayListBanca.size()>0)
            {
                banca=arrayListBanca.get(0);
                Session.getInstance().setSelectedBanca(banca);            
            }
        }

        /*
        if(utenteLogin.getRuolo().equals("Cassiere")) {
            
        }
        */
        
        //Init date to avoid problems
        dtDataNascita.setValue(TimeUtil.convertDateToLocalDate(new Date()));
        
        initCmbRuolo();
        initCmbFiliale();
        initUtentiTable();
        initServiziClienteTable();
        
        lblUtente.setText(utenteLogin.getNome() +" "+utenteLogin.getCognome()+"("+utenteLogin.getRuolo()+")");
        lblFiliale.setText(filiale.getNome());            
            
        
        if(utenteLogin.getFiliale()!=null)
            if(utenteLogin.getFiliale().getBanca()!=null)
                lblBanca.setText(utente.getFiliale().getBanca().getNome());
        
    }
    
    private void initUtentiTable() {

        tblUtenti.setItems(tblData);
        tblUtenti.getFocusModel().focusedCellProperty().addListener(new ChangeListener<TablePosition>() {
                    @Override
                    public void changed(ObservableValue<? extends TablePosition> observable,
                                        TablePosition oldPos, TablePosition pos) {
                        int row = pos.getRow();
                        int column = pos.getColumn();
                        if(row>=0 && row < tblData.size()) {
                            utente = (Utente) tblUtenti.getItems().get(row); 
                            Session.getInstance().setSelectedCliente(utente);
                            refreshFields();                            
                        }
                    }
                });
        refreshTable();
    }
    
    private void initServiziClienteTable(){
       tblServiziCliente.setItems(tblDataServizioCliente);
       tblServiziCliente.getFocusModel().focusedCellProperty().addListener(new ChangeListener<TablePosition>(){
                @Override
                public void changed(ObservableValue<? extends TablePosition> observable,
                                    TablePosition oldPos, TablePosition pos){
                    int row=pos.getRow();
                    int column=pos.getColumn();
                    if(row >= 0 && row< tblData.size()){
                        servizio = (Servizio) tblServiziCliente.getItems().get(row);
                        
                        //ricerca servizioCliente
                        ArrayList<ServizioCliente> alsc=servizioClienteDAO.findByClienteServizio(utente, servizio);
                        if(alsc.size()>0){
                            String statoServizioCliente=alsc.get(0).getStato();
                            if(statoServizioCliente.equals("non_confermato"))   btnConfirmServizioCliente.setDisable(false);
                            if(statoServizioCliente.equals("attivato"))         btnConfirmServizioCliente.setDisable(true);
                        }
                    }
                }
            });      
   }
   
    
    private void initCmbRuolo() {

        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> l) {
                return new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item);
                        }
                    }
                };
            }
        };
        cmbRuolo.setButtonCell(cellFactory.call(null));
        cmbRuolo.setCellFactory(cellFactory);

        ArrayList<String> arrayListRuolo=new ArrayList<String>();
        arrayListRuolo.add("Amministratore");
        arrayListRuolo.add("Direttore");
        arrayListRuolo.add("Cassiere");
        arrayListRuolo.add("Cliente");
        ObservableList<String> options =FXCollections.observableArrayList(arrayListRuolo);
        cmbRuolo.setItems(options);

    }
    
    private void initCmbFiliale() {

        Callback<ListView<Filiale>, ListCell<Filiale>> cellFactory = new Callback<ListView<Filiale>, ListCell<Filiale>>() {
            @Override
            public ListCell<Filiale> call(ListView<Filiale> l) {
                return new ListCell<Filiale>() {
                    @Override
                    protected void updateItem(Filiale item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getNome()+"---"+item.getBanca().getNome());
                            filiale=item;                                               
                        }
                    }
                };
            }
        };
        cmbFiliale.setButtonCell(cellFactory.call(null));
        cmbFiliale.setCellFactory(cellFactory);

        banca=Session.getInstance().getSelectedBanca();
        ArrayList<Filiale>  arrayListFiliali=filialeDAO.findByBanca(banca);
        ObservableList<Filiale> options =FXCollections.observableArrayList(arrayListFiliali);
        cmbFiliale.setItems(options);

    }
    
    private void refreshTableServiziCliente() {
        tblDataServizioCliente.removeAll();
        tblDataServizioCliente.clear();
        
        ArrayList<ServizioCliente> listaServiziCliente= servizioClienteDAO.findByCliente(utente);
        for(int i=0; i<listaServiziCliente.size();i++) {
            ServizioCliente sc=listaServiziCliente.get(i);
            
            Servizio ser=sc.getServizio();
            String stato=sc.getStato(); stato=stato.replace("_", " ");
            ser.setDenominazione(ser.getDenominazione()+" ("+stato+")");
            tblDataServizioCliente.add(sc.getServizio());
            
        }
        tblServiziCliente.setItems(tblDataServizioCliente);
        tblServiziCliente.refresh();
    }

    private void refreshTable() {
        tblData.removeAll();
        tblData.clear();
        ArrayList<Utente> listaUtenti= utenteDAO.findByFiliale(filiale);
        tblData.addAll(listaUtenti);
        tblUtenti.refresh();
    }

    private void refreshFields() {
        txtNome.setText(utente.getNome());
        txtCognome.setText(utente.getCognome());
        dtDataNascita.setValue(TimeUtil.convertDateToLocalDate(utente.getData_nascita()));
        txtCodiceFiscale.setText(utente.getCodice_fiscale());
        txtIndirizzo.setText(utente.getIndirizzo());
        txtEmail.setText(utente.getEmail());
        txtUsername.setText(utente.getUsername());
        txtPartitaIva.setText(utente.getPartitaiva());
        txtPec.setText(utente.getPec());
        txtCodiceUnivoco.setText(utente.getCodice_univoco());
        cmbRuolo.setValue(utente.getRuolo());
        filiale=utente.getFiliale();
        cmbFiliale.setValue(utente.getFiliale());
        
        refreshTableServiziCliente();
    }

    public void findUtente(ActionEvent actionEvent) {


    }
    
    public void deleteUtente(ActionEvent actionEvent) {
        //Solo un amministratore può cancellarne un'altro
        if(utente.getRuolo().equals("Amministratore") &&
           !utenteLogin.getRuolo().equals("Amministratore")) return;
        
        //Solo un Amministratore può cancellare un Direttore
        if(utente.getRuolo().equals("Direttore") &&
           !utenteLogin.getRuolo().equals("Amministratore")) {
                Session.getInstance().openInfoDialog("Errore eliminazione utente", "Non puoi eliminare questo direttore", "Non sei un un amministratore");
            return;
        }
        
        //Solo un Direttore o un amministratore può cancellare un Cassiere
        if(utente.getRuolo().equals("Cassiere") &&
           !(utenteLogin.getRuolo().equals("Direttore") || utenteLogin.getRuolo().equals("Amministratore"))) {
                Session.getInstance().openInfoDialog("Errore eliminazione utente", "Non puoi eliminare questo cassiere", "Non sei un direttore o un amministratore");
            return;
        }
        
        //Controllo servizi
        ArrayList<ServizioCliente> listaSerCli=servizioClienteDAO.findByCliente(utente);
        if(listaSerCli.size()>0) Session.getInstance().openInfoDialog("Errore eliminazione utente", "Non puoi eliminare questo utente", "L'utente è un cliente con servizi inseriti.");
        
        
        boolean b_delete=utenteDAO.delete(utente);
        if(b_delete==true)
            Session.getInstance().openInfoDialog("Cancellazione", "Cancellazione corretta", "Cancellazione avvenuta correttamente");
        else
            Session.getInstance().openErrorDialog("Errore", "Errore cancellazione", "Errore di cancellazione");
        refreshTable();
    }

    public void insertUtente(ActionEvent actionEvent) {
        fillClienteFromForm(); 
        
        
        if (utenteLogin.getRuolo().equals("Amministratore")) {
            if (utente.getRuolo().equals("Direttore")) {
                utente.setBanca(Session.getInstance().getSelectedBanca());
                utente.setFiliale(new Filiale());
            }
            
            if (utente.getRuolo().equals("Cassiere") ||
                utente.getRuolo().equals("Cliente")) {
                
                utente.setBanca(Session.getInstance().getSelectedBanca());
                utente.setFiliale(cmbFiliale.getValue());
            }                        
            boolean b_insert = utenteDAO.insert(utente);
        }
        
        if (utenteLogin.getRuolo().equals("Direttore")) {
            if(!utente.getRuolo().equals("Cassiere")) Session.getInstance().openInfoDialog("Errore privilegi", "Ruolo errato", "Il direttore può inserire solo Cassieri");
            
            utente.setRuolo("Cassiere");
            utente.setBanca(Session.getInstance().getSelectedBanca());
            utente.setFiliale(Session.getInstance().getSelectedFiliale());            
            boolean b_insert = utenteDAO.insert(utente);
        }
        
        
        refreshTable();

    }
    
    public void updateUtente(ActionEvent actionEvent) {
        fillClienteFromForm();        
        
        if (utenteLogin.getRuolo().equals("Amministratore")) {
            if (utente.getRuolo().equals("Direttore")) {
                utente.setBanca(Session.getInstance().getSelectedBanca());
                utente.setFiliale(new Filiale());
            }
            
            if (utente.getRuolo().equals("Cassiere") ||
                utente.getRuolo().equals("Cliente")) {
                
                utente.setBanca(Session.getInstance().getSelectedBanca());
                utente.setFiliale(cmbFiliale.getValue());
            }                        
            boolean b_update=utenteDAO.update(utente);        
        }
        
        if (utenteLogin.getRuolo().equals("Direttore")) {
            utente.setRuolo("Cassiere");
            utente.setBanca(Session.getInstance().getSelectedBanca());
            filiale=cmbFiliale.getValue();
            utente.setFiliale(filiale);  
            utente.setBanca(filiale.getBanca());
            boolean b_update=utenteDAO.update(utente);        
        }
        
        
        refreshTable();        
    }
    
    public void confermaServizioCliente(){        
        servizioClienteDAO.attivaServizioCliente(utente,servizio);        
        prodottoClienteDAO.attivaProdottoCliente(utente,servizio.getProdotto());        
    }
    
    public void confermaCliente(ActionEvent actionEvent) {
        utente.setRuolo("Cliente");
        boolean b_ok=Session.getInstance().openConfirmationDialog("Conferma", 
                                                     "Conferma la registrazione utente", 
                                                     "Se prosegui l'utenza verrà registrata come effettivo cliente della filiale.");
        if(b_ok) {
        boolean b_confirmed=utenteDAO.update(utente);
        servizioClienteDAO.attivaServizioCliente(utente,servizio);        
        prodottoClienteDAO.attivaProdottoCliente(utente,servizio.getProdotto());    
        
        if(b_confirmed)
            Session.getInstance().openInfoDialog("Operazione conclusa", 
                                                         "Cliente registrato", 
                                                         "Cliente registrato e servizio attivato");
            refreshTable();
        }        
    }
    
    public void fillClienteFromForm() {
        utente.setFiliale(Session.getInstance().getSelectedFiliale());
        utente.setNome(txtNome.getText());
        utente.setCognome(txtCognome.getText());
        utente.setCodice_fiscale(txtCodiceFiscale.getText());
        utente.setIndirizzo(txtIndirizzo.getText());
        if(dtDataNascita.getValue()!=null) utente.setData_nascita(TimeUtil.convertLocalDateToDate(dtDataNascita.getValue()));
        utente.setData_registrazione(new Date());
        utente.setEmail(txtEmail.getText());
        utente.setUsername(txtUsername.getText());
        utente.setPassword(MD5.getStringHash(txtPassword.getText()));
        utente.setPartitaiva(txtPartitaIva.getText());
        utente.setPec(txtPec.getText());
        utente.setCodice_univoco(txtCodiceUnivoco.getText());
        utente.setRuolo(cmbRuolo.getValue().toString());
        Filiale filiale=cmbFiliale.getValue();
        utente.setFiliale(filiale);    
        //utente.setBanca(filiale.getBanca());
    }
    
    public void logout(ActionEvent e) {
        Session.getInstance().resetSession();           
    }

}
