package controller;

import homebanking.Session;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.dao.OperazioneDAO;
import model.dao.ServizioClienteDAO;
import model.entities.Operazione;
import model.entities.Servizio;
import model.entities.ServizioCliente;
import model.entities.Utente;
import util.EmailProcessorThread;
import util.TimeUtil;

public class GestioneOperazioniCliente {

    Utente cliente=new Utente();
    Operazione operazione=new Operazione();
    Servizio servizio=new Servizio();
    ServizioCliente servizioCliente=new ServizioCliente();
    
    ServizioClienteDAO servizioClienteDAO=new ServizioClienteDAO();
    OperazioneDAO operazioneDAO=new OperazioneDAO();

    private ObservableList<Operazione> tblData = FXCollections.observableArrayList();
    
    @FXML
    private ComboBox<ServizioCliente> cmbServizioCliente;
    
    @FXML
    private ComboBox<String> cmbTipologiaOperazioneServizio;

    
    @FXML
    private TextField txtImporto;

    @FXML
    private Label lblCliente;
    
    @FXML
    private Label lblBanca;
    
    @FXML
    private Label lblFiliale;
    
    @FXML
    private DatePicker dtValuta;

    @FXML
    private TableView tblOperazioni;
    
    @FXML
    private Button btnInsert;
    
    @FXML
    private Button btnDelete;

   public GestioneOperazioniCliente(){
       
   }

   @FXML
   private void initialize(){
       cliente=Session.getInstance().getSelectedCliente();
       
       initOperazioneTable();
       initCmbTipologia();
       initCmbServizioCliente();
       
       lblCliente.setText(cliente.getUsername());
       lblBanca.setText(cliente.getFiliale().getBanca().getNome());
       lblFiliale.setText(cliente.getFiliale().getNome());
       
   }
   
   private void initOperazioneTable(){
       tblOperazioni.setItems(tblData);
       tblOperazioni.getFocusModel().focusedCellProperty().addListener(
            new ChangeListener<TablePosition>(){
                @Override
                public void changed(ObservableValue<? extends TablePosition> observable,
                                    TablePosition oldPos, TablePosition pos){
                    int row=pos.getRow();
                    int column=pos.getColumn();
                    if(row >= 0 && row< tblData.size()){
                        operazione = (Operazione) tblOperazioni.getItems().get(row);
                        Session.getInstance().setSelectedOperazione(operazione);
                        refreshFields();
                                    }
                }
            });
       refreshTable();
   }
   
   private void refreshFields(){
       txtImporto.setText(Float.valueOf(operazione.getImporto()).toString());
       dtValuta.setValue(TimeUtil.convertDateToLocalDate(operazione.getData()));
       
       if(operazione!=null){
           btnDelete.setDisable(false);
          
       }
   }

    
    private void refreshTable(){
        tblData.removeAll();
        tblData.clear();
        tblOperazioni.refresh();
        ArrayList<Operazione> listOperazioni= operazioneDAO.findByCliente(cliente);
        tblData.addAll(listOperazioni);
        tblOperazioni.setItems(tblData);
        tblOperazioni.refresh();
    }
    
    public void insert(ActionEvent actionEvent) {
        servizioCliente=cmbServizioCliente.getValue();
        if(servizioCliente==null) return;
        Operazione op=new Operazione();
        op.setImporto(Float.valueOf(txtImporto.getText()).floatValue());
        op.setData(TimeUtil.convertLocalDateToDate(dtValuta.getValue()));
        op.setTipologia(cmbTipologiaOperazioneServizio.getValue());
        op.setStato("non confermata");
        op.setCliente(cliente);
        op.setFiliale(cliente.getFiliale());
        op.setCassiere(new Utente());
        op.setData_conferma_cassiere(new Date());
        op.setServizioCliente(servizioCliente);
        
        boolean b_insert=operazioneDAO.insert(op);
        if(b_insert) sendConfirmationEmail(op);
        refreshTable();
    }
    
    public void delete(ActionEvent actionEvent) {
        if(operazione!=null) operazioneDAO.delete(operazione);
        refreshTable();
    }
    
    private void initCmbTipologia() {

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
        cmbTipologiaOperazioneServizio.setButtonCell(cellFactory.call(null));
        cmbTipologiaOperazioneServizio.setCellFactory(cellFactory);

        //ArrayList<String> arrayListOperazioni=operazioneDAO.findAll();
        //ObservableList<String> options =FXCollections.observableArrayList(arrayListOperazioni);
        ///cmbTipologiaOperazioneServizio.setItems(options);

    }
    
    public void changeCmbServizioCliente(ActionEvent e) {
        servizioCliente=cmbServizioCliente.getValue();
        String strTipologieOperazioni=servizioCliente.getServizio().getTipologieOperazioneServizio();
        StringTokenizer st=new StringTokenizer(strTipologieOperazioni,",");
        ArrayList<String> al=new ArrayList<String>();
        while(st.hasMoreTokens()){
            String strElem=st.nextToken();
            al.add(strElem);
        }
        ObservableList<String> options =FXCollections.observableArrayList(al);
        cmbTipologiaOperazioneServizio.setItems(options);        
    }

    private void initCmbServizioCliente() {

        Callback<ListView<ServizioCliente>, ListCell<ServizioCliente>> cellFactory = new Callback<ListView<ServizioCliente>, ListCell<ServizioCliente>>() {
            @Override
            public ListCell<ServizioCliente> call(ListView<ServizioCliente> l) {
                return new ListCell<ServizioCliente>() {
                    @Override
                    protected void updateItem(ServizioCliente item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getServizio().getDenominazione());
                        }
                    }
                };
            }
        };
        
        cmbServizioCliente.setButtonCell(cellFactory.call(null));
        cmbServizioCliente.setCellFactory(cellFactory);

        ArrayList<ServizioCliente> arrayListServizio=servizioClienteDAO.findByCliente(cliente);
        ObservableList<ServizioCliente> options =FXCollections.observableArrayList(arrayListServizio);
        cmbServizioCliente.setItems(options);

    }

    
    /*public void changeCmbTipologia(ActionEvent e) {
        operazione=cmbTipologiaOperazioneServizio.getValue();
        Session.getInstance().setSelectedOperazione(operazione);
    }*/
    
        private void sendConfirmationEmail(Operazione op) {
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
        
        String strMsg="L'operazione "+
                op.getTipologia()+" "+
                op.getImporto()+"\n"+
                sdf.format(op.getData())+"\n"+
                "Viene richiesta conferma del cassiere";
        
        EmailProcessorThread ept=new EmailProcessorThread();
        ept.getSender().setBody(strMsg);
        ept.getSender().setTo(op.getCliente().getEmail());
        ept.start(); 
    }
    

}
