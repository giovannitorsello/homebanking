package homebanking;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import model.entities.*;

public final class Session {
    
    
    private Configuration config=new Configuration();
    
    private Utente appUtente=new Utente();
    private Utente selectedUtente=new Utente();
    private Banca selectedBanca=new Banca();
    private Filiale selectedFiliale=new Filiale();
    private Prodotto selectedProdotto=new Prodotto();
    private Servizio selectedServizio=new Servizio();
    private Operazione selectedOperazione=new Operazione();
    private Utente selectedCliente=new Utente();
    private String requestParam="";
    
    //Store main window for logout
    private Stage loginStage=null;
    //store Application object
    private Application application=null;
    //store all opened stage
    private ArrayList<Stage> openedStages=new ArrayList<Stage>();
    
    static private Session instance=null;

    public static Session getInstance() {
        if(instance==null) {
            instance=new Session();            
        }
        return instance;
    }
    

    public Utente getAppUtente() {
        return appUtente;
    }

    public void setAppUtente(Utente appUtente) {
        this.appUtente = appUtente;
    }

    public Banca getSelectedBanca() {
        return selectedBanca;
    }

    public void setSelectedBanca(Banca selectedBanca) {
        this.selectedBanca = selectedBanca;
    }

    public Filiale getSelectedFiliale() {
        return selectedFiliale;
    }

    public void setSelectedFiliale(Filiale selectedFiliale) {
        this.selectedFiliale = selectedFiliale;
    }

    public Prodotto getSelectedProdotto() {
        return selectedProdotto;
    }

    public void setSelectedProdotto(Prodotto selectedProdotto) {
        this.selectedProdotto = selectedProdotto;
    }

    public Servizio getSelectedServizio() {
        return selectedServizio;
    }

    public void setSelectedServizio(Servizio selectedServizio) {
        this.selectedServizio = selectedServizio;
    }

    public Operazione getSelectedOperazione() {
        return selectedOperazione;
    }

    public void setSelectedOperazione(Operazione selectedOperazione) {
        this.selectedOperazione = selectedOperazione;
    }
    
    public void setSelectedUtente(Utente selectedUtente) {
        this.selectedUtente = selectedUtente;
    }

    public Utente getSelectedUtente() {
        return selectedUtente;
    }

    public Stage openGraphicInterface(String windowsTitle, String fxmFile)
    {
        try {
            ClassLoader cl = getClass().getClassLoader();
            URL urlFile=cl.getResource(fxmFile);            
            FXMLLoader fxmlLoader = new FXMLLoader(urlFile);
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.NONE);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle(windowsTitle);
            stage.setScene(new Scene(root));
            stage.show();
            openedStages.add(stage);            
            return stage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void openModalDialog(String windowsTitle, String fxmFile)
    {
        try {
            ClassLoader cl = getClass().getClassLoader();
            URL urlFile=cl.getResource(fxmFile);            
            FXMLLoader fxmlLoader = new FXMLLoader(urlFile);
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle(windowsTitle);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public void openAsAmministratore() {
        openGestioneGestioneBanche();
    }

    public void openAsDirettore() {
        openGestioneGestioneFiliali();
    }

    public void openAsCassiere() {
        openGestioneOperazioniCassiere();
    }

    public void openAsCliente() {
        openGestioneOperazioniCliente();
    }
    
    public void openAsClienteNonRegistrato() {
        openRegistrazioneUtente();
    }
    
    
   
    public void openGestioneGestioneBanche() {
        openGraphicInterface("Area Amministratore", "fxml/gestioneBanche.fxml");
    }
    
    public void openGestioneGestioneFiliali() {
        openGraphicInterface("Area Direttore", "fxml/gestioneFiliali.fxml");
    }
    
    public void openGestioneAnagraficaUtenti() {
        openGraphicInterface("Area Direttore", "fxml/gestioneAnagraficaUtenti.fxml");
    }    

    public void openGestioneProdotti() {
        openGraphicInterface("Area Direttore", "fxml/gestioneProdotti.fxml");
    }
    
    public void openGestioneServizi() {
        openGraphicInterface("Area Direttore", "fxml/gestioneServizi.fxml");
    }
        
    public void openGestioneOperazioniCassiere() {
        openGraphicInterface("Area Cassiere", "fxml/gestioneOperazioniCassiere.fxml");
    }
    
    public void openGestioneOperazioniCliente() {
        openGraphicInterface("Area cliente", "fxml/gestioneOperazioniCliente.fxml");
    }
    
    public void openRegistrazioneUtente() {
        openGraphicInterface("Area cliente", "fxml/registrazioneUtente.fxml");
    }

    public boolean openConfirmationDialog(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        Optional opt=alert.showAndWait();
        if(((ButtonType)opt.get()).getButtonData().isDefaultButton()) return true;            
        else return false;        
    }
    
    public boolean openInfoDialog(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        Optional opt=alert.showAndWait();
        if(((ButtonType)opt.get()).getButtonData().isDefaultButton()) return true;            
        else return false;
    }
    
    public boolean openErrorDialog(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        Optional opt=alert.showAndWait();
        if(((ButtonType)opt.get()).getButtonData().isDefaultButton()) return true;            
        else return false;
    }

    /**
     * @return the selectedCliente
     */
    public Utente getSelectedCliente() {
        return selectedCliente;
    }

    /**
     * @param selectedCliente the selectedCliente to set
     */
    public void setSelectedCliente(Utente selectedCliente) {
        this.selectedCliente = selectedCliente;
    }

    /**
     * @param aInstance the instance to set
     */
    public static void setInstance(Session aInstance) {
        instance = aInstance;
    }

    /**
     * @return the config
     */
    public Configuration getConfig() {
        return config;
    }

    /**
     * @param config the config to set
     */
    public void setConfig(Configuration config) {
        this.config = config;
    }

    /**
     * @return the requestParam
     */
    public String getRequestParam() {
        return requestParam;
    }

    /**
     * @param requestParam the requestParam to set
     */
    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }
        
    /**
     * @return the loginWindow
     */
    public Stage getLoginStage() {
        return loginStage;
    }

    /**
     * @param loginWindow the loginWindow to set
     */
    public void setLoginStage(Stage loginStage) {
        this.loginStage = loginStage;
    }
    
    /**
     * @return the application
     */
    public Application getApplication() {
        return application;
    }

    /**
     * @param application the application to set
     */
    public void setApplication(Application application) {
        this.application = application;
    }
    
    /**
     * @return the openedStages
     */
    public ArrayList<Stage> getOpenedStages() {
        return openedStages;
    }

    /**
     * @param openedStages the openedStages to set
     */
    public void setOpenedStages(ArrayList<Stage> openedStages) {
        this.openedStages = openedStages;
    }

        
    public void resetSession() {
        Stage ls=getLoginStage();
        Application app=getApplication();
        
        
                
        //close all stages and windows, display login
        for(int i=0; i<openedStages.size(); i++) {
            Stage stg=openedStages.get(i);
            if(stg.getTitle().equals("Home Banking Login")) stg.show();                            
            else                                            stg.close();
        }
        //Clear stages list
        openedStages.clear();
        
        instance=new Session();
        //save stage login and application object
        instance.setLoginStage(ls);
        instance.setApplication(app);
        
        //reload configuration
        config=new Configuration();     
        instance.setConfig(config);
        //add to list login stage
        instance.getOpenedStages().add(ls);        
    }

    
    
}
