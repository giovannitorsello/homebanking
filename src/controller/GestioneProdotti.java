package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.dao.ProdottoDAO;
import model.entities.Prodotto;
import util.TimeUtil;
import homebanking.Session;
import java.util.ArrayList;

public class GestioneProdotti {

    private Prodotto selectedProdotto=null;
    private ProdottoDAO prodottoDAO =new ProdottoDAO();

    private ObservableList<Prodotto> tblData = FXCollections.observableArrayList();


    @FXML
    private TextField txtDenominazione;

    @FXML
    private DatePicker dtAttivazione;

    @FXML
    private DatePicker dtScadenza;

    @FXML
    private TextField txtDescrizione;


    @FXML
    private TextField txtUrlCondizioni;

    @FXML
    private TextField txtInteressiPassivi;

    @FXML
    private TextField txtInteressiAttivi;

    @FXML
    private Button btnFind;

    @FXML
    private Button btnInsert;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnInsertService;

    @FXML
    private TableView tblProdotti;

    
    @FXML
    private Label lblUtente;
    
    @FXML
    private Label lblBanca;
    
    @FXML
    private Label lblFiliale;
    
    
    public GestioneProdotti()
    {
    }

    @FXML
    private void initialize()
    {
        initProdottoTable();
        refreshTable();
        
        lblUtente.setText(Session.getInstance().getAppUtente().getUsername());
        lblBanca.setText(Session.getInstance().getSelectedBanca().getNome());
        lblFiliale.setText(Session.getInstance().getSelectedFiliale().getNome());
    }

    private void initProdottoTable() {

        tblProdotti.setItems(tblData);
        tblProdotti.getFocusModel().focusedCellProperty().addListener(
                new ChangeListener<TablePosition>() {
                    @Override
                    public void changed(ObservableValue<? extends TablePosition> observable,
                                        TablePosition oldPos, TablePosition pos) {
                        int row = pos.getRow();
                        int column = pos.getColumn();
                        if(row>=0 && row < tblData.size()) {
                            selectedProdotto = (Prodotto) tblProdotti.getItems().get(row);
                            Session.getInstance().setSelectedProdotto(selectedProdotto);
                            refreshFields();
                        }                        
                    }
                });
        refreshTable();
    }

    private void refreshFields() {
        txtDenominazione.setText(selectedProdotto.getDenominazione());
        dtAttivazione.setValue(TimeUtil.convertDateToLocalDate(selectedProdotto.getData_attivazione()));
        dtScadenza.setValue(TimeUtil.convertDateToLocalDate(selectedProdotto.getData_scadenza()));
        txtDescrizione.setText(selectedProdotto.getDescrizione());
        txtUrlCondizioni.setText(selectedProdotto.getUrl_condizioni_generali());
        txtInteressiPassivi.setText(Float.valueOf(selectedProdotto.getInteressi_passivi()).toString());
        txtInteressiAttivi.setText(Float.valueOf(selectedProdotto.getInteressi_attivi()).toString());
        
        if(selectedProdotto!=null) {
            btnDelete.setDisable(false);
            btnInsertService.setDisable(false);
        }
    }

   
    private void refreshTable() {
        tblData.removeAll();
        tblData.clear();
        tblProdotti.refresh();        
        ArrayList<Prodotto> listProdotti= prodottoDAO.findByBanca(Session.getInstance().getSelectedBanca());
        tblData.addAll(listProdotti);
        tblProdotti.setItems(tblData);
        tblProdotti.refresh();
    }

    public void insert(ActionEvent actionEvent) {
        Prodotto p=new Prodotto();
        p.setDenominazione(txtDenominazione.getText());
        p.setData_attivazione(TimeUtil.convertLocalDateToDate(dtAttivazione.getValue()));
        p.setData_scadenza(TimeUtil.convertLocalDateToDate(dtScadenza.getValue()));
        p.setDescrizione(txtDescrizione.getText());
        p.setUrl_condizioni_generali(txtUrlCondizioni.getText());
        p.setInteressi_passivi(Float.valueOf(txtInteressiPassivi.getText()));
        p.setInteressi_attivi(Float.valueOf(txtInteressiAttivi.getText()));
        p.setBanca(Session.getInstance().getSelectedBanca());
        
        prodottoDAO.insert(p);

        refreshTable();
    }
    
    public void delete(ActionEvent actionEvent) {
        if(selectedProdotto!=null) prodottoDAO.delete(selectedProdotto);
        refreshTable();
    }
    
    public void find(ActionEvent actionEvent) {
        //if(selectedProdotto!=null) prodottoDAO.delete(selectedProdotto);
        //refreshTable();
    }
    
    public void insertService(ActionEvent actionEvent) {
        Session.getInstance().openGestioneServizi();
    }
}
