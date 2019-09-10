package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import model.dao.OperazioneDAO;
import model.entities.Operazione;

public class GestioneOperazioniCassiere {

    Operazione selectedOperazione=null;
    OperazioneDAO operazioneDAO=new OperazioneDAO();


    @FXML
    private TableView tblOperazioni;

    public void btnConfirm(ActionEvent actionEvent) {
    }

    public void btnDelete(ActionEvent actionEvent) {
    }

    public void btnCancel(ActionEvent actionEvent) {
    }
}
