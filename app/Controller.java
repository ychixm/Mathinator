package app;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML
    /**
     * il s'agit d'une zone de texte à remplir
     * **/
    private TextArea textArea;
    @FXML
    /**
     * en appuyant sur ce bouton on affiche en console le texte de la TextArea au dessus
     * **/
    private Button buttonOne;


    public void initialize(URL url, ResourceBundle resourceBundle){
        buttonOne.setOnAction(new EventHandler<ActionEvent>(){
            /**
             * la fonctionhandle va s'éxcuter à l'appui du bouton
             * **/
            public void handle (ActionEvent ae){
                System.out.println(textArea.getText());
            }
        });
    }
}