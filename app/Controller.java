package app;

import Calc.Equation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML
    private TextField input;
    @FXML
    private Button draw;
    @FXML
    private Button store;
    @FXML
    private TableView<Equation> storedEquation;
    @FXML
    private LineChart<Double,Double> graph;

    private void store(){
        storedEquation.getColumns().clear();
        TableColumn<Equation,String> name = new TableColumn<Equation,String>("name");
        TableColumn<Equation,String> expression = new TableColumn<Equation,String>("expression");
        TableColumn<Equation,Pair> interval = new TableColumn<Equation,Pair>("interval");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        expression.setCellValueFactory(new PropertyValueFactory<>("expression"));
        interval.setCellValueFactory(new PropertyValueFactory<>("interval"));

        storedEquation.getColumns().addAll(name,expression,interval);

        Equation[] temp = new Equation[Equation.getEquations().size()];

        ObservableList<Equation> list = FXCollections.observableArrayList(Equation.getEquations().toArray(temp));
        storedEquation.setItems(list);
    }
    public void initialize(URL url, ResourceBundle resourceBundle){


        draw.setOnAction(new EventHandler<ActionEvent>(){
            public void handle (ActionEvent ae){
                System.out.println(input.getText());
                Equation.getEquations().add(new Equation("f","x^2",new Pair<Double, Double>(-2.0,2.0)));
                Equation.getEquations().add(new Equation("g","x^2",new Pair<Double, Double>(-2.0,2.0)));
            }
        });
        store.setOnAction(new EventHandler<ActionEvent>(){
            public void handle (ActionEvent ae){
                store();
            }
        });

    }
}