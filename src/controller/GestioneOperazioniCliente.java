package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.dao.OperazioneDAO;
import model.entities.Operazione;

public class GestioneOperazioniCliente {

    Operazione selectedOperazione=null;
    OperazioneDAO operazioneDAO=new OperazioneDAO();

    @FXML
    private ComboBox<Operazione> cmbTipologia;

    @FXML
    private TextField txtImporto;

    @FXML
    private DatePicker dtValuta;

    @FXML
    private TableView tblOperazioni;


    public void delete(ActionEvent actionEvent) {
    }

    public void insert(ActionEvent actionEvent) {
    }
}
