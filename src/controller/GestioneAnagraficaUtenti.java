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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TablePosition;
import javafx.util.Callback;
import model.entities.Banca;
import model.entities.Filiale;
import util.MD5;

public class GestioneAnagraficaUtenti {

    private Utente cliente=new Utente();
    private Utente direttore=new Utente();
    private Banca   selectedBanca= new Banca();
    private Filiale selectedFiliale= new Filiale();
    private UtenteDAO utenteDAO=new UtenteDAO();
    private ObservableList<Utente> tblData = FXCollections.observableArrayList();

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
    private Button btnInsert;

    @FXML
    private Button btnFind;
    
    @FXML
    private Button btnDelete;

    @FXML
    private ComboBox cmbRuolo;
    
    @FXML
    private TextField txtPartitaIva;
    
    @FXML
    private TextField txtCodiceUnivoco;
    
    @FXML
    private TextField txtPec;
    
    @FXML
    private TableView tblUtenti;

    @FXML
    private Label lblUtente;
    
    @FXML
    private Label lblBanca;
    
    @FXML
    private Label lblFiliale;
    
    
    public GestioneAnagraficaUtenti(){

    }
    
    @FXML
    private void initialize()
    {
        initCmbRuolo();
        initUtentiTable();
        
        selectedFiliale=Session.getInstance().getSelectedFiliale();
        if(selectedFiliale!=null) lblFiliale.setText(selectedFiliale.getNome());            
            
        selectedBanca=selectedFiliale.getBanca();
        if(selectedBanca!=null) lblBanca.setText(cliente.getFiliale().getBanca().getNome());
            
        if(direttore.getId()!=-1)
            lblUtente.setText(direttore.getNome() +" "+direttore.getCognome());
    }
    
    private void initUtentiTable() {

        tblUtenti.setItems(tblData);
        tblUtenti.getFocusModel().focusedCellProperty().addListener(
                new ChangeListener<TablePosition>() {
                    @Override
                    public void changed(ObservableValue<? extends TablePosition> observable,
                                        TablePosition oldPos, TablePosition pos) {
                        int row = pos.getRow();
                        int column = pos.getColumn();
                        if(row>=0 && row < tblData.size()) {
                            cliente = (Utente) tblUtenti.getItems().get(row); 
                            Session.getInstance().setSelectedCliente(cliente);
                            refreshFields();
                        }
                    }
                });
        refreshTable();
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

    private void refreshTable() {
        tblData.removeAll();
        tblData.clear();
        ArrayList<Utente> listaUtenti= utenteDAO.findByFiliale(Session.getInstance().getSelectedFiliale());
        tblData.addAll(listaUtenti);
        tblUtenti.refresh();
    }

    private void refreshFields() {
        txtNome.setText(cliente.getNome());
        txtCognome.setText(cliente.getNome());
        txtCodiceFiscale.setText(cliente.getCodice_fiscale());
        txtIndirizzo.setText(cliente.getIndirizzo());
        txtEmail.setText(cliente.getEmail());
        txtUsername.setText(cliente.getUsername());
        cmbRuolo.setValue(cliente.getRuolo());
    }

    public void findUtente(ActionEvent actionEvent) {


    }
    
    public void deleteUtente(ActionEvent actionEvent) {
        //Che sia cliente
        //Che non abbia operazioni --- Richiesta cancellazione storico operazioni, servizi, prodotti, anagrafica
        
        boolean b_delete=utenteDAO.delete(cliente);
        if(b_delete==true)
            Session.getInstance().openInfoDialog("Cancellazione", "Cancellazione corretta", "Cancellazione avvenuta correttamente");
        else
            Session.getInstance().openErrorDialog("Errore", "Errore cancellazione", "Errore di cancellazione");
        refreshTable();
    }

    public void insertUtente(ActionEvent actionEvent) {
        Utente u=utenteFromForm();
        utenteDAO.insert(u);
        refreshTable();

    }
    
    public void updateUtente(ActionEvent actionEvent) {
        Utente u=utenteFromForm();
        utenteDAO.update(u);
        refreshTable();        
    }
    
    public void confermaCliente(ActionEvent actionEvent) {
        cliente.setRuolo("Cliente");
        boolean b_ok=Session.getInstance().openConfirmationDialog("Conferma", 
                                                     "Conferma la registrazione utente", 
                                                     "Se prosegui l'utenza verrà registrata come effettivo cliente della filiale.");
        if(b_ok) {
        boolean b_confirmed=utenteDAO.update(cliente);
        if(b_confirmed)
            Session.getInstance().openInfoDialog("Operazione conclusa", 
                                                         "Cliente registrato", 
                                                         "Cliente registrato e servizio attivato");
            refreshTable();
        }        
    }
    
    public Utente utenteFromForm() {
        Utente u=new Utente();
        u.setFiliale(Session.getInstance().getSelectedFiliale());
        u.setNome(txtNome.getText());
        u.setCognome(txtCognome.getText());
        u.setCodice_fiscale(txtCodiceFiscale.getText());
        u.setIndirizzo(txtIndirizzo.getText());
        if(dtDataNascita.getValue()!=null) u.setData_nascita(TimeUtil.convertLocalDateToDate(dtDataNascita.getValue()));
        u.setData_registrazione(new Date());
        u.setEmail(txtEmail.getText());
        u.setUsername(txtUsername.getText());
        u.setPassword(MD5.getStringHash(txtPassword.getText()));
        u.setPartitaiva(txtPartitaIva.getText());
        u.setPec(txtPec.getText());
        u.setCodice_univoco(txtCodiceUnivoco.getText());
        u.setRuolo(cmbRuolo.getValue().toString());
        u.setFiliale(selectedFiliale);
        return u;
    }
}
