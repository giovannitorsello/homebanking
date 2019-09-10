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
import java.io.FileInputStream;
import java.io.InputStream;

import java.util.ArrayList;
import javafx.scene.image.Image;
import model.dao.GalleryDAO;
import model.entities.Gallery;

public class GestioneFiliali {

    //Modello
    private Filiale selectedFiliale=null;
    private FilialeDAO filialeDAO=new FilialeDAO();
    
    private ArrayList<Gallery> fotoFiliale=new ArrayList<Gallery>();

    private ObservableList<Filiale> tblData = FXCollections.observableArrayList();


    // Controlli Grafici
    @FXML
    ImageView imgFiliale;

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
    private Button btnDelete;

    @FXML
    private Button btnInsertImage;

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
        lblBanca.setText(Session.getInstance().getSelectedBanca().getNome());
    }

    private void initFilialeTable() {

        tblFiliali.setItems(tblData);
        tblFiliali.getFocusModel().focusedCellProperty().addListener(
                new ChangeListener<TablePosition>() {
                    @Override
                    public void changed(ObservableValue<? extends TablePosition> observable,
                                        TablePosition oldPos, TablePosition pos) {
                        int row = pos.getRow();
                        int column = pos.getColumn();
                        if(row>=0 && row < tblData.size()) {
                            selectedFiliale = (Filiale) tblFiliali.getItems().get(row);
                            Session.getInstance().setSelectedFiliale(selectedFiliale);
                            refreshFields();
                        }
                    }
                });
        refreshTable();
    }
    
    private void initImgFiliale() {
        //Loading image from URL
        try {
            selectedFiliale=Session.getInstance().getSelectedFiliale();
            if(selectedFiliale.getId()==-1) return;
            GalleryDAO gdao=new GalleryDAO();
            fotoFiliale=gdao.findByFiliale(selectedFiliale);        
            Gallery g=new Gallery(); 
            if(fotoFiliale.size()>0)g=fotoFiliale.get(0); 
            imgFiliale.setImage(new Image(g.getImage()));
        }
        catch(Exception e) {e.printStackTrace();}
    }

    private void refreshFields() {
        txtNome.setText(selectedFiliale.getNome());
        txtIndirizzo.setText(selectedFiliale.getIndirizzo());
    }


    public void deleteFiliale(ActionEvent actionEvent) {
        if(selectedFiliale!=null) filialeDAO.delete(selectedFiliale);
        refreshTable();
    }

    private void refreshTable() {
        tblData.removeAll();
        tblFiliali.refresh();
        ArrayList<Filiale> listaFiliali= filialeDAO.findByBanca(Session.getInstance().getSelectedBanca());
        tblData.clear();
        tblData.addAll(listaFiliali);
        tblFiliali.refresh();
    }


    public void clickImgFilialeImage(MouseEvent mouseEvent) {
        System.out.println("Click immagine");
    }


    public void asCassiere(ActionEvent actionEvent) {
        Session.getInstance().openAsCassiere();
    }

    public void insertFiliale(ActionEvent actionEvent) {
        Filiale f=new Filiale();
        f.setDirettore(Session.getInstance().getAppUtente());
        f.setBanca(Session.getInstance().getSelectedBanca());
        f.setNome(txtNome.getText());
        f.setIndirizzo(txtNome.getText());
        f.setOrarioApertura(txtOrarioApertura.getText());
        f.setOrarioChiusura(txtOrarioChiusura.getText());

        filialeDAO.insert(f);
        refreshTable();
    }

    public void insertUtente(ActionEvent actionEvent) {
        Session.getInstance().openGestioneAnagraficaUtenti();
    }
}
