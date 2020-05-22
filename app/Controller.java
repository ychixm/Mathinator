package app;

import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;
import javafx.util.Pair;
import main.Equation;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

import main.IntervalException;
import org.mariuszgromada.math.mxparser.*;


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
    private Button delete;
    @FXML
    private Button refreshA;
    @FXML
    private Button refreshB;
    @FXML
    private Button derivative;
    @FXML
    private TableView<Equation> storedEquation;
    @FXML
    private TableView<Equation> storage;
    @FXML
    private LineChart<Double,Double> graph;

    private void drawGraph() throws IntervalException {
        Equation e = new Equation(input.getText());
        Pair<Vector<Double>,Vector<Double>> tmp = e.calcFunc();

        XYChart.Series<Double,Double> a = new XYChart.Series<Double, Double>();
        a.setName(e.getName());
        for(int i = 0 ; i < tmp.getKey().size(); i++){

            a.getData().add(new XYChart.Data<Double, Double>(tmp.getKey().elementAt(i),tmp.getValue().elementAt(i)));
        }
        graph.setCreateSymbols(false);
        graph.getData().add(a);
        /*a.setName("plop");
        a.getData().add(new XYChart.Data<String, Double>("1.0",1.0));
        a.getData().add(new XYChart.Data<String, Double>("1.0",2.0));
        a.getData().add(new XYChart.Data<String, Double>("2.0",3.0));
        a.getData().add(new XYChart.Data<String, Double>("2.0",2.0));
        graph.setCreateSymbols(false);
        graph.getData().add(a);*/
    }

    private void refresh(){
        displayTableView(storedEquation);
        displayTableView(storage);
    }

    private void displayTableView(TableView<Equation> list){
        list.getColumns().clear();
        TableColumn<Equation,String> name = new TableColumn<Equation,String>("name");
        TableColumn<Equation,String> expression = new TableColumn<Equation,String>("expression");
        TableColumn<Equation,String> interval = new TableColumn<Equation,String>("interval");
        TableColumn<Equation,Boolean> select = new TableColumn<Equation,Boolean>("select");

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        expression.setCellValueFactory(new PropertyValueFactory<>("expression"));
        interval.setCellValueFactory(new PropertyValueFactory<>("interval"));
        select.setCellValueFactory(new PropertyValueFactory<>("select"));

        list.getColumns().addAll(name,expression,interval);
        storage.getColumns().add(select);
        Equation[] temp = new Equation[Equation.getEquations().size()];

        ObservableList<Equation> data = FXCollections.observableArrayList(Equation.getEquations().toArray(temp));
        list.setItems(data);
    }

    private void store(){
    }

    public void initialize(URL url, ResourceBundle resourceBundle){
        //displayTableView(storedEquation);
        displayTableView(storage);

        //fex.setOnAction(new EventHandler<ActionEvent>(){
        //    public void handle (ActionEvent ae){
        //        Equation.getEquations().add(new Equation("x^2","f","-2;2","x"));
        //        Equation.getEquations().add(new Equation("x^2","g","-2,2","x"));
        //    }
        //});
        store.setOnAction(new EventHandler<ActionEvent>(){
            public void handle (ActionEvent ae){
                store();
            }
        });
        draw.setOnAction(new EventHandler<ActionEvent>(){
            public void handle (ActionEvent ae){
                //Equation.addEquation(new Equation(input.getText()));
                try {
                    drawGraph();
                } catch (IntervalException e) {
                    e.printStackTrace();
                }
            }
        });
        delete.setOnAction(new EventHandler<ActionEvent>(){
            public void handle (ActionEvent ae){
                Vector<Integer> tmp = new Vector<Integer>();
                for (int i = 0; i < Equation.getEquations().size(); i++)
                {
                    if(Equation.getEquations().elementAt(i).getSelect().isSelected()){
                        tmp.add(i);
                    }
                }
                for(int j = 0 ; j < tmp.size() ; j++){
                    Equation.deleteEquation(tmp.elementAt(j));
                }
                refresh();
            }
        });
        refreshA.setOnAction(new EventHandler<ActionEvent>(){
            public void handle (ActionEvent ae){
                refresh();
            }
        });
        refreshB.setOnAction(new EventHandler<ActionEvent>(){
            public void handle (ActionEvent ae){
                refresh();
            }
        });
        derivative.setOnAction(new EventHandler<ActionEvent>(){
            public void handle (ActionEvent ae){
            }
        });
    }
}