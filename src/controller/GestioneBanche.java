package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.dao.BancaDAO;
import model.entities.Banca;
import homebanking.Session;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.dao.GalleryDAO;
import model.entities.Filiale;
import model.entities.Gallery;

public class GestioneBanche {

    //Modello
    private Banca banca=new Banca();
    private BancaDAO bancaDAO=new BancaDAO();

    private ObservableList<Banca> tblData = FXCollections.observableArrayList();
    private ArrayList<Gallery> fotoBanca=new ArrayList<Gallery>();

    @FXML 
    private AnchorPane panel;
    
    @FXML
    Button btnLogout;
    
    @FXML
    Label lblUtente;

    @FXML
    Label lblOrario;

    // Controlli Grafici
    @FXML
    ImageView imgBanca;

    @FXML
    private TextField txtDenominazione;

    @FXML
    private TextField txtIndirizzo;
    
    @FXML
    private TextField txtDescrizione;

    @FXML
    private Button btnInsert;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnInsertImage;
    
    @FXML
    private Button btnInsertProdotto;

    @FXML
    private Button btnInsertFiliale;
    
    
    @FXML
    private Button btnAsDirettore;
    
    @FXML
    private Button btnInsertUtente;
            

    @FXML
    private TableView tblBanche;

    
    public GestioneBanche()
    {
    }

    
    @FXML
    private void initialize()
    {
        initImgBanca();
        initBancaTable();
        lblUtente.setText(Session.getInstance().getAppUtente().getUsername());

    }

    private void initBancaTable() {

        tblBanche.setItems(tblData);
        tblBanche.getFocusModel().focusedCellProperty().addListener(
                new ChangeListener<TablePosition>() {
                    @Override
                    public void changed(ObservableValue<? extends TablePosition> observable,
                                        TablePosition oldPos, TablePosition pos) {
                        int row = pos.getRow();
                        int column = pos.getColumn();
                        if(row>=0 && row < tblData.size()) {
                            banca = (Banca) tblBanche.getItems().get(row);
                            Session.getInstance().setSelectedBanca(banca);
                            refreshFields();
                            refreshStateButton();
                        }
                        //label.setText(selectedValue);
                    }
                });
        refreshTable();
    }

    private void refreshFields() {
        txtDenominazione.setText(banca.getNome());
        txtIndirizzo.setText(banca.getIndirizzo());
        txtDescrizione.setText(banca.getDescrizione());      
        initImgBanca();
    }
    
    private void refreshStateButton() {
        if(banca.getId()>0) {
            btnAsDirettore.setDisable(false);
            btnInsertFiliale.setDisable(false);
            btnInsertUtente.setDisable(false);
            btnInsertProdotto.setDisable(false);
            btnInsertImage.setDisable(false);
        }        
        if(banca.getId()==-1)
        {
            btnAsDirettore.setDisable(true);
            btnInsertFiliale.setDisable(true);
            btnInsertUtente.setDisable(true);
            btnInsertProdotto.setDisable(true);
            btnInsertImage.setDisable(true);            
        }
    }
    
    public void insertBanca(ActionEvent actionEvent) {        
        banca.setNome(txtDenominazione.getText());
        banca.setIndirizzo(txtIndirizzo.getText());
        banca.setDescrizione(txtDescrizione.getText());
        banca.setAmministratore(Session.getInstance().getAppUtente());
        bancaDAO.insert(banca);
        refreshTable();
        refreshStateButton();
    }
    
    public void updateBanca(ActionEvent actionEvent) {        
        banca.setNome(txtDenominazione.getText());
        banca.setIndirizzo(txtIndirizzo.getText());
        banca.setDescrizione(txtDescrizione.getText());
        banca.setAmministratore(Session.getInstance().getAppUtente());
        bancaDAO.update(banca);
        refreshTable();
    }
    
    public void insertUtente (ActionEvent actionEvent) {
        Session.getInstance().openGestioneAnagraficaUtenti();
    }
    
    public void deleteBanca(ActionEvent actionEvent) {
        if(banca!=null) bancaDAO.delete(banca);
        banca=new Banca(); //Empty object
        refreshTable();
        refreshStateButton();
    }

    private void refreshTable() {
        tblData.removeAll();
        tblData.clear();
        tblBanche.refresh();
        ArrayList<Banca> listBanche=new ArrayList<Banca>();

        if(Session.getInstance().getAppUtente().getRuolo().equals("Amministratore"))
            listBanche=bancaDAO.findAll();
        if(Session.getInstance().getAppUtente().getRuolo().equals("Direttore"))
            bancaDAO.findByDirettore(Session.getInstance().getAppUtente());

        tblData.addAll(listBanche);
        tblBanche.refresh();        
    }

    

    private void initImgBanca() {
        //Loading image from URL
        try {
            banca=Session.getInstance().getSelectedBanca();
            if(banca==null) return;
            if(banca.getId()==-1) return;
            GalleryDAO gdao=new GalleryDAO();
            fotoBanca=gdao.findByBanca(banca);  
            Gallery g=new Gallery(); 
            if(fotoBanca.size()>0)g=fotoBanca.get(0); 
            InputStream img=g.getImage();
            if(img!=null) imgBanca.setImage(new Image(img));
        }
        catch(Exception e) {e.printStackTrace();}
    }


    public void clickImgBancaImage(MouseEvent mouseEvent) {
        System.out.println("Click immagine");
    }

    public void insertImage(ActionEvent actionEvent) {
        try {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog((Stage) panel.getScene().getWindow());        
            FileInputStream image=new FileInputStream(selectedFile);
            
            Gallery g=new Gallery();
            g.setData_inserimento(new Date());
            g.setDescrizione(selectedFile.getName());
            g.setBanca(banca);
            g.setFiliale(new Filiale());
            g.setImage(image);
            
            GalleryDAO gdao=new GalleryDAO();
            if(gdao.insert(g))  {
                Session.getInstance().openInfoDialog("Caricamento immagine", "Successo", "Immagine correttamente inserita");
                initImgBanca();
            }
            else
                Session.getInstance().openInfoDialog("Caricamento immagine", "Fallito", "Immagine non inserita");
                        
        } catch (Exception ex) {
            Logger.getLogger(GestioneBanche.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void asDirettore(ActionEvent actionEvent) {
        Session.getInstance().openAsDirettore();
    }

    public void insertFiliale(ActionEvent actionEvent) {
        Session.getInstance().openAsDirettore();
    }

    public void insertProdotto(ActionEvent actionEvent) {
        Session.getInstance().openGestioneProdotti();
    }

    public void logout(ActionEvent e) {
        Session.getInstance().resetSession();         
    }
}
