package controller;

import homebanking.Session;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.dao.ServizioDAO;
import model.entities.Servizio;
import util.TimeUtil;

import java.util.ArrayList;
import javafx.scene.layout.AnchorPane;

public class GestioneServizi {

    private Servizio selectedServizio=null;
    private ServizioDAO servizioDAO =new ServizioDAO();

    private ObservableList<Servizio> tblData = FXCollections.observableArrayList();

    @FXML 
    private AnchorPane panel;
    
    @FXML
    Button btnLogout;
    
    @FXML
    private TextField txtDenominazione;

    @FXML
    private DatePicker dtAttivazione;

    @FXML
    private DatePicker dtScadenza;

    @FXML
    private TextField txtDescrizione;


    @FXML
    private TextField txtNumeroMassimoOperazioni;
    
    @FXML
    private TextField txtTipologieOperazioneServizio;

    @FXML
    private Button btnFind;

    @FXML
    private Button btnInsert;
    
    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private TableView tblServizi;

    
    @FXML
    private Label lblUtente;
    
    @FXML
    private Label lblBanca;
    
    @FXML
    private Label lblFiliale;

    @FXML
    private Label lblProdotto;
    
    public GestioneServizi()
    {
    }

    @FXML
    private void initialize()
    {
        initServizioTable();
        
        lblUtente.setText(Session.getInstance().getAppUtente().getUsername());
        lblBanca.setText(Session.getInstance().getSelectedBanca().getNome());
        lblFiliale.setText(Session.getInstance().getSelectedFiliale().getNome());
        lblProdotto.setText(Session.getInstance().getSelectedProdotto().getDenominazione());
        
    }

    private void initServizioTable() {

        tblServizi.setItems(tblData);
        tblServizi.getFocusModel().focusedCellProperty().addListener(
                new ChangeListener<TablePosition>() {
                    @Override
                    public void changed(ObservableValue<? extends TablePosition> observable,
                                        TablePosition oldPos, TablePosition pos) {
                        int row = pos.getRow();
                        int column = pos.getColumn();
                        if(row>=0 && row < tblData.size()) {
                            selectedServizio = (Servizio) tblServizi.getItems().get(row);
                            Session.getInstance().setSelectedServizio(selectedServizio);
                            refreshFields();
                        }                       
                    }
                });
        
        refreshTable();
    }

    private void refreshFields() {
        txtDenominazione.setText(selectedServizio.getDenominazione());
        dtAttivazione.setValue(TimeUtil.convertDateToLocalDate(selectedServizio.getData_attivazione()));
        dtScadenza.setValue(TimeUtil.convertDateToLocalDate(selectedServizio.getData_scadenza()));
        txtDescrizione.setText(selectedServizio.getDescrizione());
        txtNumeroMassimoOperazioni.setText(Integer.valueOf(selectedServizio.getNumero_massimo_operazioni()).toString());
        txtTipologieOperazioneServizio.setText(selectedServizio.getTipologieOperazioneServizio());
         if(selectedServizio!=null) btnDelete.setDisable(false);

    }

    public void insert(ActionEvent actionEvent) {
        Servizio s=new Servizio();
        s.setDenominazione(txtDenominazione.getText());
        s.setData_attivazione(TimeUtil.convertLocalDateToDate(dtAttivazione.getValue()));
        s.setData_scadenza(TimeUtil.convertLocalDateToDate(dtScadenza.getValue()));
        s.setDescrizione(txtDescrizione.getText());
        s.setNumero_massimo_operazioni(Integer.valueOf(txtNumeroMassimoOperazioni.getText()).intValue());
        s.setProdotto(Session.getInstance().getSelectedProdotto());
        s.setTipologieOperazioneServizio(txtTipologieOperazioneServizio.getText());
        servizioDAO.insert(s);
        refreshTable();
    }
    
    public void update(ActionEvent actionEvent){
        if (selectedServizio != null) {
            selectedServizio.setDenominazione(txtDenominazione.getText());
            selectedServizio.setData_attivazione(TimeUtil.convertLocalDateToDate(dtAttivazione.getValue()));
            selectedServizio.setData_scadenza(TimeUtil.convertLocalDateToDate(dtScadenza.getValue()));
            selectedServizio.setDescrizione(txtDescrizione.getText());
            selectedServizio.setNumero_massimo_operazioni(Integer.valueOf(txtNumeroMassimoOperazioni.getText()).intValue());
            selectedServizio.setProdotto(Session.getInstance().getSelectedProdotto());
            selectedServizio.setTipologieOperazioneServizio(txtTipologieOperazioneServizio.getText());
            servizioDAO.update(selectedServizio);
        }
        refreshTable();
    }   

    public void delete(ActionEvent actionEvent){
        if(selectedServizio!=null) servizioDAO.delete(selectedServizio);
        refreshTable();
    }   

    public void find(ActionEvent actionEvent){
    
    }
    
    private void refreshTable() {
        tblData.removeAll();
        tblData.clear();
        tblServizi.refresh();        
        ArrayList<Servizio> listProdotti= servizioDAO.findByProdotto(Session.getInstance().getSelectedProdotto());
        tblData.addAll(listProdotti);
        tblServizi.setItems(tblData);
        tblServizi.refresh();
    }

    public void logout(ActionEvent e) {
       Session.getInstance().resetSession();
    }

}
