package homebanking;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    private Stage primaryStage=null;
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage=primaryStage;
        Stage loginStage=Session.getInstance().openGraphicInterface("Home Banking Login","fxml/login.fxml");
        Session.getInstance().setLoginStage(loginStage);
        Session.getInstance().setApplication(this);                        
    }

    public static void main(String[] args) {
        launch(args);
    }
}
