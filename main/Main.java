package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    /**
     * fenêtre javafx
     * **/
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../app/Window.fxml"));
        primaryStage.setTitle("Mathinator");
        Scene window = new Scene(root);
        window.getStylesheets().add(getClass().getResource("../app/window.css").toExternalForm());
        primaryStage.setScene(window);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}