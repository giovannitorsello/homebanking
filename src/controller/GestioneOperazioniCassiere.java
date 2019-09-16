package controller;

import homebanking.Session;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.dao.OperazioneDAO;
import model.dao.ServizioClienteDAO;
import model.dao.ServizioDAO;
import model.dao.UtenteDAO;
import model.entities.Banca;
import model.entities.Filiale;
import model.entities.Operazione;
import model.entities.Prodotto;
import model.entities.Servizio;
import model.entities.ServizioCliente;
import model.entities.Utente;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import util.EmailProcessorThread;
import util.TimeUtil;

public class GestioneOperazioniCassiere {

    private Operazione operazione=new Operazione();
    private OperazioneDAO operazioneDAO=new OperazioneDAO();
    private UtenteDAO clienteDAO=new UtenteDAO();
    private ServizioClienteDAO servizioClienteDAO=new ServizioClienteDAO();
    private ServizioDAO servizioDAO=new ServizioDAO();
    
    private Utente cliente=new Utente();    
    private Filiale filiale=new Filiale();
    private Utente cassiere=new Utente();
    private Banca banca=new Banca();
    private ServizioCliente servizioCliente=new ServizioCliente();
    private String tipologia="";
    private String stato="";
    private float saldoProdottoCliente = 0;   
    private float interessiProdottoCliente = 0;   
        
    
    
    private ObservableList<Operazione> tblData = FXCollections.observableArrayList();
    
    @FXML 
    private AnchorPane panel;
    
    @FXML
    Button btnLogout;
    
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
    
    @FXML
    private Label lblProdotto;
    
    @FXML
    private Label lblSaldo;
    
    @FXML
    private Label lblSaldoNettoInteressi;
        
    @FXML
    private Label lblInteresse;
    

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
       
       //Per l'utente  amministratpore
       Utente utenteApp=Session.getInstance().getAppUtente();
       if(utenteApp.getRuolo().equals("Amministratore") || utenteApp.getRuolo().equals("Direttore")) {
           banca=Session.getInstance().getSelectedBanca();
           filiale=Session.getInstance().getSelectedFiliale();
       }
       
       initCmbCliente();
       initCmbPeriodo();
       initCmbStato();       
       initTableOperazioni();
       
       
       
       
       if(cassiere!=null)   lblCassiere.setText(cassiere.getUsername());
       if(banca!=null)      lblBanca.setText(banca.getNome());
       if(filiale!=null)    lblFiliale.setText(filiale.getNome());
       
   }
    
    public void changeCmbCliente(ActionEvent e) {
        cliente=cmbCliente.getValue();
        initCmbServizioCliente();
        refreshTable();
        
    }
    
    public void changeCmbServizioCliente(ActionEvent e) {
        servizioCliente=cmbServizioCliente.getValue();
        initCmbTipologiaOperazioniServizio();
        refreshTable();
        //refreshSaldo();
    }
    
    public void changeCmbTipologiaOperazioneServizio(ActionEvent e) {  
        tipologia=cmbTipologiaOperazioneServizio.getValue();
        refreshTable();
    }
    
    public void changeCmbStato(ActionEvent e) {  
        stato=cmbStato.getValue();
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
    
    public void dtEndChange(ActionEvent e) {        
        refreshTable();
    }
            
    private void setPeriodo() {
        String per=cmbPeriodo.getValue();
        Date start=TimeUtil.convertLocalDateToDate(dtStart.getValue());
        int num_days=0;
        //disabilita data finale perchè viene calcolata automaticamente
        dtEnd.setDisable(true);
        
        if(per.equals("giornaliero"))    num_days=0; 
        if(per.equals("settimanale"))    num_days=7;
        if(per.equals("mensile"))        num_days=30;
        
        //Computa data finale
        long end_date_mills=start.getTime();
        end_date_mills+=(long)num_days*24L*3600L*1000L;
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
        
        ArrayList<Utente> arrayListCliente=clienteDAO.findClienteByFiliale(filiale);
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
        
        ArrayList<ServizioCliente> arrayListServizioCliente=servizioClienteDAO.findByCliente(cliente);
        ObservableList<ServizioCliente> options =FXCollections.observableArrayList(arrayListServizioCliente);
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
        al.add("");
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
        al.add("");al.add("non confermata");al.add("confermata");al.add("annullata");
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
        refreshSaldo();
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
    
    private void getReportIntestazione(PDPageContentStream stream) throws IOException {
        int hspace=100;
        int vspace=15;
        
        stream.showText("Cliente");
        stream.newLineAtOffset(hspace, 0);
        stream.showText(cliente.getNome()+" "+cliente.getCognome());
        
        stream.newLineAtOffset(-hspace, -vspace);
        stream.showText("Servizio");
        stream.newLineAtOffset(hspace, 0);        
        stream.showText(servizioCliente.getServizio().getDenominazione());
        
        calcolaSaldo();
        
        stream.newLineAtOffset(-hspace, -vspace);
        stream.showText("Saldo");
        stream.newLineAtOffset(hspace, 0);        
        stream.showText(Float.valueOf(servizioCliente.getSaldo()).toString());
        
        //Inserimnto spazio finale
        stream.newLineAtOffset(-hspace, -4*vspace);
               
    }
    
    private void calcolaSaldo() {        
        ArrayList<Operazione> ops=operazioneDAO.findByServizioCliente(servizioCliente);
        float saldo=servizioCliente.calcolaSaldoPeriodo(ops, servizioCliente.getData_attivazione(), new Date());
        servizioCliente.setSaldo(saldo);
    }
    
    private void getReportSaldo(PDPageContentStream stream) throws IOException {
        int hspace=150;
        int vspace=15;
        
        stream.showText("Saldo");
        stream.newLineAtOffset(hspace, 0);
        stream.showText(Float.valueOf(saldoProdottoCliente).toString());
        stream.newLineAtOffset(hspace, 0);
        
        stream.showText("Interessi");
        stream.newLineAtOffset(hspace, 0);
        stream.showText(Float.valueOf(interessiProdottoCliente).toString());
        
        stream.newLineAtOffset(-2*hspace, -2*vspace);        
    }
    
    
    private void getReportFirma(PDPageContentStream stream) throws IOException {
        int hspace=100;
        int vspace=15;
        
        stream.showText("Cassiere");
        stream.newLineAtOffset(hspace, 0);
        stream.showText(cassiere.getNome()+" "+cassiere.getCognome());
        
        stream.newLineAtOffset(-hspace, -vspace);
        stream.showText("Filiale");
        stream.newLineAtOffset(hspace, 0);        
        stream.showText(filiale.getNome());
    }
    
    private void getReportOperazioni(PDPageContentStream stream) throws IOException {
        String buffer="";
        ArrayList<Operazione> listOperazioni= operazioneDAO.findByClienteServizioStatoTipologiaPeriodo(cliente, 
                                                                                                      servizioCliente, 
                                                                                                      tipologia, 
                                                                                                      stato,
                                                                                                      TimeUtil.convertLocalDateToDate(dtStart.getValue()), 
                                                                                                      TimeUtil.convertLocalDateToDate(dtEnd.getValue()));
        
        int hspace=110;
        int vspace=15;
        
        stream.showText("Tipo");
        stream.newLineAtOffset(hspace, 0);
        stream.showText("Importo");
        stream.newLineAtOffset(hspace, 0);
        stream.showText("Stato");
        stream.newLineAtOffset(hspace, 0);
        stream.showText("Data");
        stream.newLineAtOffset(hspace, 0);
        stream.showText("Data conferma");
        
                                     
        
        for(int i=0; i<listOperazioni.size(); i++) {            
            Operazione op=listOperazioni.get(i);
            
            stream.newLineAtOffset(-4*hspace, -vspace); //crea nuova riga e va a capo
            stream.showText(op.getTipologia());
            stream.newLineAtOffset(hspace, 0);
            stream.showText(Float.valueOf(op.getImporto()).toString());
            stream.newLineAtOffset(hspace, 0);
            stream.showText(op.getStato());
            stream.newLineAtOffset(hspace, 0);
            stream.showText((new SimpleDateFormat("yyyy-MM-dd")).format(op.getData()));
            stream.newLineAtOffset(hspace, 0);
            stream.showText((new SimpleDateFormat("yyyy-MM-dd")).format(op.getData_conferma_cassiere()));
        }
        
        //Inserimnto spazio finale
        stream.newLineAtOffset(-4*hspace, -4*vspace);
        
    }
    
    public void print(ActionEvent actionEvent) {
        
        String filename = "./pdf_cassiere/report_"+(new Date()).getTime()+".pdf";
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
            
            //Font
            PDType0Font font=PDType0Font.load(doc, new File("./font/courier.ttf"));
            
            //Contenuto
            PDPageContentStream contentStream = new PDPageContentStream(doc, page);
            contentStream.beginText();
            //posizione in alto a sinistra
            contentStream.newLineAtOffset(20, page.getMediaBox().getHeight() - 20);
                        
            contentStream.setFont(font, 12);
            
            //Inserimento intestazione
            getReportIntestazione(contentStream);
            
            //Inserimento informazioni operazioni
            getReportOperazioni(contentStream);
            
            //Inserimento informazioni firma
            getReportSaldo(contentStream);
            
            //Inserimento informazioni firma
            getReportFirma(contentStream);
            
            
            contentStream.endText();
            contentStream.close();
            
            File pdfFile=new File(filename);
            doc.save(pdfFile);            
            doc.close();
            
            //Open pdf file througth system 
            
            String pdfFilePath=pdfFile.getAbsolutePath();
            String pdfProgram=Session.getInstance().getConfig().getPdf_program();
            if(pdfProgram.equals("SYSTEM")) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(new File(pdfFilePath));
                }                
            }    
            else {
                Process ps=Runtime.getRuntime().exec(new String[]{pdfProgram,pdfFilePath});                    
            }
                
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
    
    public void logout(ActionEvent e) {
        Session.getInstance().resetSession();
    }
    
    
    private void refreshSaldo() {
       
        ServizioCliente sc=cmbServizioCliente.getValue();
        if(sc==null) return;
        Servizio servizio=sc.getServizio();
        Prodotto prodotto=servizio.getProdotto();
        ArrayList<Servizio> listaServizio = servizioDAO.findByProdotto(prodotto);
        
        
        saldoProdottoCliente = 0;   
        interessiProdottoCliente = 0;   
        
        for (int i_ser=0; i_ser<listaServizio.size(); i_ser++) {
            Servizio s=listaServizio.get(i_ser);
            ArrayList<ServizioCliente> listaServizioCliente=servizioClienteDAO.findByClienteServizio(cliente, s);
            for (int j = 0; j < listaServizioCliente.size(); j++) {
                ServizioCliente scp = listaServizioCliente.get(j);
                //ArrayList<Operazione> listaOperazione = operazioneDAO.findByServizioCliente(scp);

                Date dsStart = scp.getData_attivazione();
                
                //only for tests
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");                
                Date dsEnd=new Date();
                try {                    
                    dsEnd = sdf.parse("2019-12-31");
                } catch (ParseException ex) {
                    Logger.getLogger(GestioneOperazioniCassiere.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                long diff=dsEnd.getTime()-dsStart.getTime();
                float days = (diff / (1000*60*60*24));
                int num_months=(int)(days/30);
                                
                for (int n_per=0;n_per<num_months;n_per++) {
                    
                    dsEnd.setTime(dsStart.getTime()+(long)(n_per+1)*30L*24L*3600L*1000L);                    
                    ArrayList<Operazione> ops=operazioneDAO.findByServizioClientePeriodo(scp,dsStart,dsEnd);
                    
                    
                    float saldoServizioCliente = sc.calcolaSaldoPeriodo(ops, dsStart, dsEnd);
                    float interessiServizioCliente=sc.calcolaInteressiPeriodo(ops, dsStart, dsEnd);
                    
                    saldoProdottoCliente += saldoServizioCliente;
                    interessiProdottoCliente+=interessiServizioCliente;
                    
                    dsStart=dsEnd;
                }
            }
        }
                
            
        
        ArrayList<Operazione> listOperazioni= operazioneDAO.findByServizioCliente(sc);
        //float saldo=sc.calcolaSaldoPeriodo(listOperazioni, sc.getData_attivazione(), new Date());
        //float interessi=sc.calcolaInteressiPeriodo(listOperazioni, sc.getData_attivazione(), new Date());
        
        lblProdotto.setText(prodotto.getDenominazione());
        lblSaldo.setText(Float.valueOf(saldoProdottoCliente+interessiProdottoCliente).toString());
        lblSaldoNettoInteressi.setText(Float.valueOf(saldoProdottoCliente).toString());
        lblInteresse.setText(Float.valueOf(interessiProdottoCliente).toString());
        

    }
}
