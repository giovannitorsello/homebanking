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
import model.entities.Gallery;

public class GestioneBanche {

    //Modello
    private Banca selectedBanca=null;
    private BancaDAO bancaDAO=new BancaDAO();

    private ObservableList<Banca> tblData = FXCollections.observableArrayList();
    private ArrayList<Gallery> fotoBanca=new ArrayList<Gallery>();

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
    private Button btnInsert;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnInsertImage;

    @FXML
    private Button btnInsertFiliale;

    @FXML
    private TableView tblBanche;

    
    @FXML 
    private AnchorPane panel;
    
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
                            selectedBanca = (Banca) tblBanche.getItems().get(row);
                            Session.getInstance().setSelectedBanca(selectedBanca);
                            refreshFields();
                        }
                        //label.setText(selectedValue);
                    }
                });
        refreshTable();
    }

    private void refreshFields() {
        txtDenominazione.setText(selectedBanca.getNome());
        txtIndirizzo.setText(selectedBanca.getIndirizzo());
        if(selectedBanca!=null) btnInsertFiliale.setDisable(false);        
    }

    @FXML
    public void bntInsertBanca(ActionEvent actionEvent) {
        Banca b=new Banca();
        b.setNome(txtDenominazione.getText());
        b.setIndirizzo(txtIndirizzo.getText());
        b.setAmministratore(Session.getInstance().getAppUtente());
        bancaDAO.insert(b);

        refreshTable();
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

    public void bntDeleteBanca(ActionEvent actionEvent) {
        if(selectedBanca!=null) bancaDAO.delete(selectedBanca);
        refreshTable();
    }

    private void initImgBanca() {
        //Loading image from URL
        try {
            selectedBanca=Session.getInstance().getSelectedBanca();
            if(selectedBanca==null) return;
            if(selectedBanca.getId()==-1) return;
            GalleryDAO gdao=new GalleryDAO();
            fotoBanca=gdao.findByBanca(selectedBanca);  
            Gallery g=new Gallery(); 
            if(fotoBanca.size()>0)g=fotoBanca.get(0); 
            imgBanca.setImage(new Image(g.getImage()));
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
            g.setDescrizione(selectedFile.getPath());
            g.setBanca(selectedBanca);
            g.setImage(image);
            
            GalleryDAO gdao=new GalleryDAO();
            if(gdao.insert(g)) 
                Session.getInstance().openInfoDialog("Caricamento immagine", "Successo", "Immaggine correttamente inserita");
            else
                Session.getInstance().openInfoDialog("Caricamento immagine", "Fallito", "Immagine non inserita");
                        
        } catch (Exception ex) {
            Logger.getLogger(GestioneBanche.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void asCassiere(ActionEvent actionEvent) {
        Session.getInstance().openAsCassiere();
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
}
