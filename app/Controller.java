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
    private LineChart<Double,Double> graph;

    private void drawGraph(){
        NumberAxis xAxis = new NumberAxis("Values for X-Axis", 0, 3, 1);
        NumberAxis yAxis = new NumberAxis("Values for Y-Axis", 0, 3, 1);
        ObservableList<XYChart.Series<Double,Double>> lineChartData = FXCollections.observableArrayList(
                new LineChart.Series<Double,Double>("Series 1", FXCollections.observableArrayList(
                        new XYChart.Data<Double,Double>(0.0, 1.0),
                        new XYChart.Data<Double,Double>(1.2, 1.4),
                        new XYChart.Data<Double,Double>(2.2, 1.9),
                        new XYChart.Data<Double,Double>(2.7, 2.3),
                        new XYChart.Data<Double,Double>(2.9, 0.5)
                )),
                new LineChart.Series<Double,Double>("Series 2", FXCollections.observableArrayList(
                        new XYChart.Data<Double,Double>(0.0, 1.6),
                        new XYChart.Data<Double,Double>(0.8, 0.4),
                        new XYChart.Data<Double,Double>(1.4, 2.9),
                        new XYChart.Data<Double,Double>(2.1, 1.3),
                        new XYChart.Data<Double,Double>(2.6, 0.9)
                ))
        );
        graph = new LineChart(xAxis, yAxis, lineChartData);
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
                System.out.println(input.getText());
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
                Equation.addEquation(new Equation(input.getText()));
                drawGraph();

            }
        });

    }
}