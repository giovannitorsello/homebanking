package controller;

import homebanking.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import model.dao.BancaDAO;
import model.dao.FilialeDAO;
import model.dao.ProdottoDAO;
import model.dao.UtenteDAO;
import model.entities.Banca;
import model.entities.Filiale;
import model.entities.Utente;

import java.util.ArrayList;
import java.util.Date;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import model.dao.ServizioClienteDAO;
import model.dao.ServizioDAO;
import model.entities.Prodotto;
import model.entities.Servizio;
import model.entities.ServizioCliente;
import util.*;

public class RegistrazioneUtente {

    private Banca selectedBanca=new Banca();
    private Filiale selectedFiliale = new Filiale();
    private Utente selectedUtente = new Utente();
    private Prodotto selectedProdotto = new Prodotto();
    private Servizio selectedServizio = new Servizio();
    private ServizioCliente selectedServizioCliente = new ServizioCliente();
    
    private BancaDAO bancaDAO=new BancaDAO();
    private FilialeDAO filialeDAO=new FilialeDAO();
    private UtenteDAO utenteDAO=new UtenteDAO();
    private ProdottoDAO prodottoDAO=new ProdottoDAO();
    private ServizioDAO servizioDAO=new ServizioDAO();
    private ServizioClienteDAO servizioClienteDAO=new ServizioClienteDAO();
    
    
    private ObservableList<Prodotto> tblDataProdotti = FXCollections.observableArrayList();
    private ObservableList<Servizio> tblDataServizi = FXCollections.observableArrayList();

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtCognome;

    @FXML
    private TextField txtIndirizzo;

    @FXML
    private TextField txtCodiceFiscale;

    @FXML
    private TextField txtEmail;

    @FXML
    private DatePicker dtDataNascita;

    @FXML
    private ComboBox cmbFilialeBanca;

    @FXML
    private TableView tblProdotti;
    
    @FXML
    private TableView tblServizi;

    @FXML
    private Button btnInsert;

    public RegistrazioneUtente(){}


    @FXML
    private void initialize()
    {
        //Dati di esempio commendare in produzione (versione da distribuire)
        txtNome.setText("Carlo");
        txtCognome.setText("Rossi");
        txtCodiceFiscale.setText("TRSBTTPOIDSI");
        txtIndirizzo.setText("Via Mazzini n.4, 73040, Galatina, LE");
        txtEmail.setText("carlo.rossi@domain.it");
        dtDataNascita.setValue(TimeUtil.convertDateToLocalDate(new Date()));
        //Dati di esempio commendare in produzione (versione da distribuire)s
        
        
        initCmbFiliale();
        initProdottoTable();
        initServizioTable();
    }

    public void insert(ActionEvent actionEvent) {
        Utente u=new Utente();
        u.setNome(txtNome.getText());
        u.setCognome(txtCognome.getText());
        u.setIndirizzo(txtIndirizzo.getText());
        u.setCodice_fiscale(txtCodiceFiscale.getText());
        u.setEmail(txtEmail.getText());
        u.setData_nascita(TimeUtil.convertLocalDateToDate(dtDataNascita.getValue()));
        u.setFiliale(selectedFiliale);
        
        //credenziali
        u.setUsername(txtEmail.getText());  //per default email e login coincidono        
        u.setPassword(PasswordGenerator.GeneraPassCasuale(12));        
        
        
        //impostazione predefinita "Non_registrato"
        //l'amministratore modifichera il parametro a Cliente, Direttore o Amministratore
        // durante il processo di conferma
        u.setRuolo("Non_registrato");
        
        utenteDAO.insert(u);        
        
        //selectedServizioCliente.se
        
        

        //sendEmailCliente(u);
        //sendEmailDirettore(u);

    }
    
    private void sendEmailCliente(Utente cliente) {
        String strMsgCliente="La tua richiesta è stata inviata al direttore della banca selezionata\n"+
                "prodotto "+selectedProdotto.getDenominazione()+"\n"+
                "servizio "+selectedServizio.getDenominazione()+ "\n"+
                "attendere conferma da parte della direzione";
        
        //cliente for confirmation
        EmailProcessorThread ept=new EmailProcessorThread();
        ept.getSender().setBody(strMsgCliente);
        ept.getSender().setTo(Session.getInstance().getSelectedFiliale().getDirettore().getEmail());
        ept.start();                
    }
    
    
    private void sendEmailDirettore(Utente cliente, Utente direttore) {
        String strMsgDirettore="L'utente "+
                cliente.getNome()+" "+
                cliente.getCognome()+"\n"+
                " avente email "+direttore.getEmail()+
                "ha inviato una richiesta in relazione "+
                "al prodotto "+selectedProdotto.getDenominazione()+"e "+
                "al servizio "+selectedServizio.getDenominazione()+ ".\n"+
                "Viene richiesta autorizzazione per l'accesso al servizio";
        
        //cliente for confirmation
        EmailProcessorThread ept=new EmailProcessorThread();
        ept.getSender().setBody(strMsgDirettore);
        ept.getSender().setTo(direttore.getEmail());
        ept.start();                
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
                            setText(item.getNome()+"--"+item.getBanca().getNome());
                        }
                    }
                };
            }
        };
        cmbFilialeBanca.setButtonCell(cellFactory.call(null));
        cmbFilialeBanca.setCellFactory(cellFactory);

        ArrayList<Filiale> arrayListFiliali=filialeDAO.findAll();
        ObservableList<Filiale> options = FXCollections.observableArrayList(arrayListFiliali);
        cmbFilialeBanca.setItems(options);

    }
    
    
    private void initProdottoTable() {

        tblProdotti.setItems(tblDataProdotti);
        tblProdotti.getFocusModel().focusedCellProperty().addListener(
                new ChangeListener<TablePosition>() {
                    @Override
                    public void changed(ObservableValue<? extends TablePosition> observable,
                                        TablePosition oldPos, TablePosition pos) {
                        int row = pos.getRow();
                        int column = pos.getColumn();
                        if(row>=0 && row < tblDataProdotti.size()) {
                            selectedProdotto = (Prodotto) tblProdotti.getItems().get(row);
                            Session.getInstance().setSelectedProdotto(selectedProdotto);    
                            if(selectedProdotto!=null) tblServizi.setDisable(false);
                            refreshTableServizi();
                        }                        
                    }
                });
        refreshTableProdotti();
    }

    private void initServizioTable() {

        tblServizi.setItems(tblDataServizi);
        tblServizi.getFocusModel().focusedCellProperty().addListener(
                new ChangeListener<TablePosition>() {
                    @Override
                    public void changed(ObservableValue<? extends TablePosition> observable,
                                        TablePosition oldPos, TablePosition pos) {
                        int row = pos.getRow();
                        int column = pos.getColumn();
                        if(row>=0 && row < tblDataServizi.size()) {
                            selectedServizio = (Servizio) tblServizi.getItems().get(row);
                            Session.getInstance().setSelectedServizio(selectedServizio);   
                            enableRegistrationForm();
                        }                        
                    }
                });
        refreshTableServizi();
    }
    
    public void changeCmbFilialeBanca(ActionEvent e) {        
        selectedFiliale=(Filiale) cmbFilialeBanca.getValue();
        selectedBanca=selectedFiliale.getBanca();
        Session.getInstance().setSelectedFiliale(selectedFiliale);
        Session.getInstance().setSelectedBanca(selectedBanca);
        tblProdotti.setDisable(false);
        refreshTableProdotti();
        
    }

    private void refreshTableProdotti() {
        tblDataProdotti.removeAll();
        tblDataProdotti.clear();
        tblProdotti.refresh();        
        
        if(selectedBanca==null) return;
        ArrayList<Prodotto> listProdotti= prodottoDAO.findByBanca(selectedBanca);
        tblDataProdotti.addAll(listProdotti);
        tblProdotti.setItems(tblDataProdotti);
        tblProdotti.refresh();
    }

    
    private void refreshTableServizi() {     
        
        tblDataServizi.removeAll();
        tblDataServizi.clear();
        tblServizi.refresh();        
        
        if(selectedProdotto==null) return;
        ArrayList<Servizio> listServizi= servizioDAO.findByProdotto(selectedProdotto);
        tblDataServizi.addAll(listServizi);
        tblServizi.setItems(tblDataServizi);
        tblServizi.refresh();
    }
    
    private void enableRegistrationForm() {
        txtNome.setDisable(false);
        txtCognome.setDisable(false);
        txtIndirizzo.setDisable(false);
        txtCodiceFiscale.setDisable(false);
        txtEmail.setDisable(false);
        dtDataNascita.setDisable(false);    
        btnInsert.setDisable(false);
    }


}
