package app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;
import main.Equation;

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
    private Button fex;
    @FXML
    private TableView<Equation> storedEquation;
    @FXML
    private LineChart<String,Double> graph;

    private void drawGraph(){
        XYChart.Series<String,Double> a = new XYChart.Series<String, Double>();
        a.setName("plop");
        a.getData().add(new XYChart.Data<String, Double>("1.0",1.0));
        a.getData().add(new XYChart.Data<String, Double>("1.0",2.0));
        a.getData().add(new XYChart.Data<String, Double>("2.0",3.0));
        a.getData().add(new XYChart.Data<String, Double>("2.0",2.0));
        graph.setCreateSymbols(false);
        graph.getData().add(a);
    }

    private void store(){
        storedEquation.getColumns().clear();
        TableColumn<Equation,String> name = new TableColumn<Equation,String>("name");
        TableColumn<Equation,String> expression = new TableColumn<Equation,String>("expression");
        TableColumn<Equation,String> interval = new TableColumn<Equation,String>("interval");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        expression.setCellValueFactory(new PropertyValueFactory<>("expression"));
        interval.setCellValueFactory(new PropertyValueFactory<>("interval"));

        storedEquation.getColumns().addAll(name,expression,interval);

        Equation[] temp = new Equation[Equation.getEquations().size()];

        ObservableList<Equation> list = FXCollections.observableArrayList(Equation.getEquations().toArray(temp));
        storedEquation.setItems(list);
    }

    public void initialize(URL url, ResourceBundle resourceBundle){


        fex.setOnAction(new EventHandler<ActionEvent>(){
            public void handle (ActionEvent ae){
                Equation.getEquations().add(new Equation("x^2","f","-2;2"));
                Equation.getEquations().add(new Equation("x^2","g","-2,2"));
            }
        });
        store.setOnAction(new EventHandler<ActionEvent>(){
            public void handle (ActionEvent ae){
                store();
            }
        });
        draw.setOnAction(new EventHandler<ActionEvent>(){
            public void handle (ActionEvent ae){
                //Equation.addEquation(new Equation(input.getText()));
                drawGraph();

            }
        });

    }
}