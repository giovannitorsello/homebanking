package controller;

import homebanking.Session;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.dao.OperazioneDAO;
import model.dao.ServizioClienteDAO;
import model.dao.UtenteDAO;
import model.entities.Banca;
import model.entities.Filiale;
import model.entities.Operazione;
import model.entities.Servizio;
import model.entities.ServizioCliente;
import model.entities.Utente;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.printing.PDFPrintable;
import util.EmailProcessorThread;
import util.TimeUtil;

public class GestioneOperazioniCassiere {

    private Operazione operazione=new Operazione();
    private OperazioneDAO operazioneDAO=new OperazioneDAO();
    private UtenteDAO clienteDAO=new UtenteDAO();
    private ServizioClienteDAO servizioClienteDAO=new ServizioClienteDAO();
    
    private Utente cliente=new Utente();    
    private Filiale filiale=new Filiale();
    private Utente cassiere=new Utente();
    private Banca banca=new Banca();
    private ServizioCliente servizioCliente=new ServizioCliente();
    private String tipologia="";
    private String stato="";
    
    private ObservableList<Operazione> tblData = FXCollections.observableArrayList();
    
    @FXML
    private ComboBox<Utente> cmbCliente;
    
    @FXML
    private ComboBox<String> cmbStato;
    
    @FXML
    private ComboBox<ServizioCliente> cmbServizioCliente;
    
    @FXML
    private ComboBox<String> cmbTipologiaOperazioneServizio;
    
    @FXML
    private ComboBox<String> cmbPeriodo;
    
    @FXML
    private DatePicker dtStart;
    
    @FXML
    private DatePicker dtEnd;

    @FXML
    private Button btnConfirm;
    
    @FXML
    private Button btnCancel;
    
    @FXML
    private Button btnDelete;

    @FXML
    private TableView tblOperazioni;
    
    @FXML
    private Label lblCassiere;
    
    @FXML
    private Label lblBanca;
    
    @FXML
    private Label lblFiliale;

    public GestioneOperazioniCassiere(){
        
    }
    
    @FXML
    private void initialize(){              
      
       cassiere=Session.getInstance().getAppUtente();       
       filiale=cassiere.getFiliale();
       banca=filiale.getBanca();
       
       //inizializza finestra di ricerca iniziale
       cmbPeriodo.setValue("settimanale");
       dtStart.setValue(TimeUtil.convertDateToLocalDate(new Date()));
       setPeriodo();
       
       initCmbCliente();
       initCmbPeriodo();
       initCmbStato();       
       initTableOperazioni();
       
       
       lblCassiere.setText(cassiere.getUsername());
       lblBanca.setText(banca.getNome());
       lblFiliale.setText(filiale.getNome());
       
   }
    
    public void changeCmbCliente(ActionEvent e) {
        cliente=cmbCliente.getValue();
        initCmbServizioCliente();
        refreshTable();
        
    }
    
    public void changeCmbServizioCliente(ActionEvent e) {
        initCmbTipologiaOperazioniServizio();
        refreshTable();
    }
    
    public void changeCmbTipologiaOperazioniServizio(ActionEvent e) {        
        refreshTable();
    }
    
    public void changeCmbStato(ActionEvent e) {       
        refreshTable();
    }
    
    public void changeCmbPeriodo(ActionEvent e) {
        setPeriodo();
        refreshTable();
    }
    
    public void dtStartChange(ActionEvent e) {
        setPeriodo();
        refreshTable();
    }
            
    private void setPeriodo() {
        String per=cmbPeriodo.getValue();
        Date start=TimeUtil.convertLocalDateToDate(dtStart.getValue());
        int num_days=0;
        //disabilita data finale perchè viene calcolata automaticamente
        dtEnd.setDisable(true);
        
        if(per.equals("giornaliero"))    num_days=0; 
        if(per.equals("settimanale")) num_days=7;
        if(per.equals("mensile"))      num_days=30;
        
        //Computa data finale
        long end_date_mills=start.getTime();
        end_date_mills+=num_days*24*3600*1000;
        Date end=new Date();
        end.setTime(end_date_mills);        
        
        //abilita il controllo nel caso di ricerche generiche
        if(per.equals("generico"))  dtEnd.setDisable(false);
        dtEnd.setValue(TimeUtil.convertDateToLocalDate(end));
        
    }
    
    private void initCmbCliente() {

        Callback<ListView<Utente>, ListCell<Utente>> cellFactory = new Callback<ListView<Utente>, ListCell<Utente>>() {
            @Override
            public ListCell<Utente> call(ListView<Utente> l) {
                return new ListCell<Utente>() {
                    @Override
                    protected void updateItem(Utente item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getNome()+" "+item.getCognome());
                        }
                    }
                };
            }
        };
        cmbCliente.setButtonCell(cellFactory.call(null));
        cmbCliente.setCellFactory(cellFactory);
        
        ArrayList<Utente> arrayListCliente=clienteDAO.findByFiliale(filiale);
        ObservableList<Utente> options =FXCollections.observableArrayList(arrayListCliente);
        cmbCliente.setItems(options);
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
                            setText(item.getServizio().getDenominazione()+"--"+item.getServizio().getProdotto().getDenominazione());
                            servizioCliente=item;
                            Session.getInstance().setSelectedServizio(servizioCliente.getServizio());
                        }
                    }
                };
            }
        };
        cmbServizioCliente.setButtonCell(cellFactory.call(null));
        cmbServizioCliente.setCellFactory(cellFactory);
        
        ArrayList<ServizioCliente> arrayListCliente=servizioClienteDAO.findByCliente(cliente);
        ObservableList<ServizioCliente> options =FXCollections.observableArrayList(arrayListCliente);
        cmbServizioCliente.setItems(options);
        cmbServizioCliente.setDisable(false);
    }
    
    private void initCmbTipologiaOperazioniServizio() {

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
                            tipologia=item;
                        }
                    }
                };
            }
        };
        cmbTipologiaOperazioneServizio.setButtonCell(cellFactory.call(null));
        cmbTipologiaOperazioneServizio.setCellFactory(cellFactory);
        
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
        cmbTipologiaOperazioneServizio.setDisable(false);
    }
    
    private void initCmbStato() {

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
                            stato=item;
                        }
                    }
                };
            }
        };
        cmbStato.setButtonCell(cellFactory.call(null));
        cmbStato.setCellFactory(cellFactory);
        
        ArrayList<String> al=new ArrayList<String>();
        al.add("non confermata");al.add("confermata");al.add("annullata");
        ObservableList<String> options =FXCollections.observableArrayList(al);
        cmbStato.setItems(options);     
        cmbStato.setDisable(false);
    }
    
    private void initCmbPeriodo() {

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
        cmbPeriodo.setButtonCell(cellFactory.call(null));
        cmbPeriodo.setCellFactory(cellFactory);
        
        ArrayList<String> al=new ArrayList<String>();
        al.add("giornaliero");al.add("settimanale");al.add("mensile");al.add("generico");
        ObservableList<String> options =FXCollections.observableArrayList(al);
        cmbPeriodo.setItems(options);     
        cmbPeriodo.setDisable(false);

    }
    
    private void initTableOperazioni(){
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
                        }
                }
            });
   }
    
   
   private void refreshTable(){
        tblData.removeAll();
        tblData.clear();
        tblOperazioni.refresh();
        tipologia=cmbTipologiaOperazioneServizio.getValue();
        stato=cmbStato.getValue();
        
        ArrayList<Operazione> listOperazioni= operazioneDAO.findByClienteServizioStatoTipologiaPeriodo(cliente, 
                                                                                                      servizioCliente, 
                                                                                                      tipologia, 
                                                                                                      stato,
                                                                                                      TimeUtil.convertLocalDateToDate(dtStart.getValue()), 
                                                                                                      TimeUtil.convertLocalDateToDate(dtEnd.getValue()));
        tblData.addAll(listOperazioni);
        tblOperazioni.setItems(tblData);
        tblOperazioni.refresh();
    }
   
    public void update(ActionEvent actionEvent) {
        refreshTable();
    }
    
    public void confirm(ActionEvent actionEvent) {
        OperazioneDAO odao=new OperazioneDAO();
        operazione.setCassiere(cassiere);
        operazione.setStato("confermata");
        boolean b_confirm=odao.update(operazione);
        if(b_confirm) sendConfirmedEmail(operazione);
        refreshTable();
    }

    public void delete(ActionEvent actionEvent) {
        if(operazione!=null)
        {
            boolean b_delete=operazioneDAO.delete(operazione);
            if(b_delete) sendDeletedEmail(operazione);
            refreshTable();
        }
    }

    public void cancel(ActionEvent actionEvent) {
        OperazioneDAO odao=new OperazioneDAO();
        operazione.setStato("annullata");
        boolean b_cancel=odao.update(operazione);
        if(b_cancel) sendCanceledEmail(operazione);
        
        refreshTable();
    }
    
    public void print(ActionEvent actionEvent) {
        
        String filename = "temp.pdf";
        try 
        {
            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();
            
            doc.addPage(page);
            
            //Proprietà documento            
            PDDocumentInformation pdd = doc.getDocumentInformation();
            pdd.setAuthor("Home banking report");
            pdd.setTitle("Report cassiere"); 
            pdd.setCreator("Homebanking"); 
            pdd.setSubject("Report cassiere"); 
            pdd.setCreationDate(new GregorianCalendar());
            
            //Contenuto
            PDPageContentStream contentStream = new PDPageContentStream(doc, page);
            contentStream.beginText();
            String text = "Stringa di prova";       
            contentStream.showText(text);     
            contentStream.endText();
            
            
            doc.save(filename);            
            doc.close();
    
        } catch (Exception ex) {
            Logger.getLogger(GestioneOperazioniCassiere.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    private void sendCanceledEmail(Operazione op) {
        String strMsg="L'operazione "+
                op.getTipologia()+" "+
                op.getImporto()+"\n"+
                op.getData()+"\n"+
                "è stata rifiutata dal cassiere"+op.getCassiere().getNome()+" "+op.getCassiere().getCognome()+"\n"+
                "in data "+op.getData_conferma_cassiere();
        
        EmailProcessorThread ept=new EmailProcessorThread();
        ept.getSender().setSubject("Cancellazione operazione "+op.getServizioCliente().getServizio().getDenominazione());
        ept.getSender().setBody(strMsg);
        ept.getSender().setTo(op.getCliente().getEmail());
        ept.start(); 
    }
    
    private void sendConfirmedEmail(Operazione op) {
        String strMsg="L'operazione "+
                op.getTipologia()+" "+
                op.getImporto()+"\n"+
                op.getData()+"\n"+
                "è stata confermata dal cassiere"+op.getCassiere().getNome()+" "+op.getCassiere().getCognome()+"\n"+
                "in data "+op.getData_conferma_cassiere();
        
        EmailProcessorThread ept=new EmailProcessorThread();
        ept.getSender().setSubject("Conferma operazione "+op.getServizioCliente().getServizio().getDenominazione());
        ept.getSender().setBody(strMsg);
        ept.getSender().setTo(op.getCliente().getEmail());
        ept.start(); 
    }

    private void sendDeletedEmail(Operazione op) {
        String strMsg="L'operazione "+
                op.getTipologia()+" "+
                op.getImporto()+"\n"+
                op.getData()+"\n"+
                "è stata cancellata dal cassiere"+op.getCassiere().getNome()+" "+op.getCassiere().getCognome()+"\n"+
                "in data "+op.getData_conferma_cassiere();
        
        EmailProcessorThread ept=new EmailProcessorThread();
        ept.getSender().setSubject("Cancellazione operazione "+op.getServizioCliente().getServizio().getDenominazione());
        ept.getSender().setBody(strMsg);
        ept.getSender().setTo(op.getCliente().getEmail());
        ept.start(); 
    }

}
