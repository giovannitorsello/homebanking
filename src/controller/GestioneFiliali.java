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
import model.dao.FilialeDAO;
import model.entities.Filiale;
import homebanking.Session;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.dao.GalleryDAO;
import model.entities.Banca;
import model.entities.Gallery;

public class GestioneFiliali {

    //Modello
    private Filiale filiale=null;
    private FilialeDAO filialeDAO=new FilialeDAO();
    
    private ArrayList<Gallery> fotoFiliale=new ArrayList<Gallery>();

    private ObservableList<Filiale> tblData = FXCollections.observableArrayList();

    @FXML 
    private AnchorPane panel;

    // Controlli Grafici
    @FXML
    ImageView imgFiliale;
    
    @FXML
    Button btnLogout;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtIndirizzo;

    @FXML
    private TextField txtOrarioApertura;

    @FXML
    private TextField txtOrarioChiusura;

    @FXML
    private Button btnInsert;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnInsertImage;
    
    @FXML
    private Button btnInsertProdotto;
    
    @FXML
    private Button btnInsertUtente;
    
    @FXML
    private Button btnCassiere;
    
    @FXML
    private Label lblBanca;

    @FXML
    private Label lblUtente;
    
    @FXML
    private TableView tblFiliali;

    public GestioneFiliali()
    {
    }

    @FXML
    private void initialize()
    {
        initImgFiliale();
        initFilialeTable();
        lblUtente.setText(Session.getInstance().getAppUtente().getUsername());
        
        Banca banca=Session.getInstance().getSelectedBanca();
        if(banca!=null) lblBanca.setText(banca.getNome());
    }

    private void initFilialeTable() {

        tblFiliali.setItems(tblData);
        tblFiliali.getFocusModel().focusedCellProperty().addListener(new ChangeListener<TablePosition>() {
                    @Override
                    public void changed(ObservableValue<? extends TablePosition> observable,
                                        TablePosition oldPos, TablePosition pos) {
                        int row = pos.getRow();
                        int column = pos.getColumn();
                        if(row>=0 && row < tblData.size()) {
                            filiale = (Filiale) tblFiliali.getItems().get(row);
                            Session.getInstance().setSelectedFiliale(filiale);
                            refreshFields();
                            refreshStateButton();
                        }
                    }
                });
        refreshTable();
    }
    
    private void initImgFiliale() {
        //Loading image from URL
        try {
            filiale=Session.getInstance().getSelectedFiliale();
            if(filiale.getId()==-1) return;
            GalleryDAO gdao=new GalleryDAO();
            fotoFiliale=gdao.findByFiliale(filiale);        
            Gallery g=new Gallery(); 
            if(fotoFiliale.size()>0)g=fotoFiliale.get(0); 
            InputStream img=g.getImage();
            if(img!=null) imgFiliale.setImage(new Image(img));            
        }
        catch(Exception e) {e.printStackTrace();}
    }

    private void refreshFields() {
        txtNome.setText(filiale.getNome());
        txtIndirizzo.setText(filiale.getIndirizzo());
        txtOrarioApertura.setText(filiale.getOrarioApertura());
        txtOrarioChiusura.setText(filiale.getOrarioChiusura());
        initImgFiliale();
    }
    
    private void refreshStateButton() {
        if(filiale.getId()>0) {
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
            btnInsertUtente.setDisable(false);
            btnInsertImage.setDisable(false);
            btnCassiere.setDisable(false);
        }        
        if(filiale.getId()==-1)
        {
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
            btnInsertUtente.setDisable(true);
            btnInsertImage.setDisable(true);            
            btnCassiere.setDisable(true);
        }
    }
    
    public void insertProdotto(ActionEvent actionEvent) {
        Session.getInstance().openGestioneProdotti();
    }
    
    public void insertFiliale(ActionEvent actionEvent) {
        Filiale f=new Filiale();
        filiale=filialeFromForm();
        filialeDAO.insert(filiale);
        refreshTable();
        refreshStateButton();
    }

    public void updateFiliale(ActionEvent actionEvent) {
        Filiale f=new Filiale();
        filiale=filialeFromForm();
        filialeDAO.update(filiale);
        refreshTable();
    }

    public void deleteFiliale(ActionEvent actionEvent) {
        if(filiale!=null) filialeDAO.delete(filiale);
        filiale=new Filiale(); //Carica filiale vuota
        refreshTable();
        refreshStateButton();
    }

    private void refreshTable() {
        tblData.removeAll();
        tblFiliali.refresh();
        ArrayList<Filiale> listaFiliali= filialeDAO.findByBanca(Session.getInstance().getSelectedBanca());
        tblData.clear();
        if(listaFiliali.size()>0) tblData.addAll(listaFiliali);
        tblFiliali.refresh();
    }

    public void clickImgFilialeImage(MouseEvent mouseEvent) {
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
            g.setBanca(filiale.getBanca());
            g.setFiliale(filiale);
            g.setImage(image);
            
            GalleryDAO gdao=new GalleryDAO();
            if(gdao.insert(g)) {
                Session.getInstance().openInfoDialog("Caricamento immagine", "Successo", "Immagine correttamente inserita");
                initImgFiliale();
            }
            else
                Session.getInstance().openInfoDialog("Caricamento immagine", "Fallito", "Immagine non inserita");
                        
        } catch (Exception ex) {
            Logger.getLogger(GestioneBanche.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }


    public void asCassiere(ActionEvent actionEvent) {
        Session.getInstance().openAsCassiere();
    }

    
    public void insertUtente(ActionEvent actionEvent) {
        Session.getInstance().openGestioneAnagraficaUtenti();
    }
    
    public void logout(ActionEvent e) {
        Session.getInstance().resetSession();
    }
    
    public Filiale filialeFromForm() {
        Filiale f=new Filiale();
        f.setDirettore(Session.getInstance().getAppUtente());
        f.setBanca(Session.getInstance().getSelectedBanca());
        f.setNome(txtNome.getText());
        f.setIndirizzo(txtIndirizzo.getText());
        f.setOrarioApertura(txtOrarioApertura.getText());
        f.setOrarioChiusura(txtOrarioChiusura.getText());
        return f;
    }
}
