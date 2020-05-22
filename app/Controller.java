package app;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;
import javafx.util.Pair;
import main.Equation;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;

import main.IntervalException;


public class Controller implements Initializable{
    @FXML
    private TextField input;
    @FXML
    private TextField value;
    @FXML
    private TextField name;
    @FXML
    private Button draw;
    @FXML
    private Button store;
    @FXML
    private Button delete;
    @FXML
    private Button refreshA;
    @FXML
    private Button refreshB;
    @FXML
    private Button derivative;
    @FXML
    private Button integral;
    @FXML
    private Button clear;
    @FXML
    private Button solve;
    @FXML
    private Button combine;
    @FXML
    private Label solved;
    @FXML
    private TableView<Equation> storedEquation;
    @FXML
    private TableView<Equation> storage;
    @FXML
    private LineChart<Double,Double> graph;
    @FXML
    private ChoiceBox solver;
    @FXML
    private ChoiceBox expr1;
    @FXML
    private ChoiceBox expr2;
    @FXML
    private ChoiceBox operation;


    /**
     * @param c caractère permettant le choix du type de tracé de graph p(primitive) d(dérivé) sinon un tracé classique de l'expression
     * @param e equation que l'on souhaite tracer
     * */


    private void drawGraph(char c, Equation e) throws IntervalException {
        Pair<Vector<Double>,Vector<Double>> tmp;
        switch (c){
            case 'd':
                tmp = e.calcFuncDeriv();
                break;
            case 'p':
                tmp = e.calcFuncInt();
                break;
            default:
                tmp = e.calcFunc();
                break;
        }

        XYChart.Series<Double,Double> a = new XYChart.Series<Double, Double>();
        a.setName(e.getName());
        for(int i = 0 ; i < tmp.getKey().size(); i++){

            a.getData().add(new XYChart.Data<Double, Double>(tmp.getKey().elementAt(i),tmp.getValue().elementAt(i)));
        }
        graph.setCreateSymbols(false);
        graph.getData().add(a);
    }

    private void drawGraphD() throws IntervalException {
        Equation e = new Equation(input.getText());
        drawGraph('d',e);
    }

    private void drawGraphP() throws IntervalException {
        Equation e = new Equation(input.getText());
        drawGraph('p',e);
    }

    /**
     * fonction permettant de mettre à jour les différents visuels en cas d'ajout de donnés
     * */
    private void refresh(){
        displayTableView(storedEquation);
        displayTableView(storage);
        refreshChoice();
    }
    /**
     * @param list Tableview que l'on souhaite mette à jour.
     * cette fonction crée/recréer la TableView souhaité
     * */
    private void displayTableView(TableView<Equation> list){
        list.getColumns().clear();
        TableColumn<Equation,String> name = new TableColumn<Equation,String>("name");
        TableColumn<Equation,String> expression = new TableColumn<Equation,String>("expression");
        TableColumn<Equation,String> interval = new TableColumn<Equation,String>("interval");

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        expression.setCellValueFactory(new PropertyValueFactory<>("expression"));
        interval.setCellValueFactory(new PropertyValueFactory<>("interval"));

        list.getColumns().addAll(name,expression,interval);
        if(list == storage){
            TableColumn<Equation,Boolean> select = new TableColumn<Equation,Boolean>("select");
            select.setCellValueFactory(new PropertyValueFactory<>("select"));
            storage.getColumns().add(select);
        }
        if(list == storedEquation) {
            TableColumn<Equation, Boolean> draw = new TableColumn<Equation, Boolean>("draw");
            draw.setCellValueFactory(new PropertyValueFactory<>("draw"));
            storedEquation.getColumns().add(draw);
        }

        Equation[] temp = new Equation[Equation.getEquations().size()];

        ObservableList<Equation> data = FXCollections.observableArrayList(Equation.getEquations().toArray(temp));
        list.setItems(data);
    }

    /**
     * Fonction supprimant les données du LineChart
     * */
    private void clearGraph(){
        graph.getData().clear();
    }

    /**
     * @param input String contenant l'entrée utilisateur à parser et à enregistrer dans un tableau
     * */
    private void store(String input){
        Equation.getEquations().add(new Equation(input));
        Equation.getEquations().lastElement().getDraw().selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue ov, Boolean old_val, Boolean new_val) {
                clearGraph();
                for (Equation e:Equation.getEquations()) {
                    if(e.getDraw().isSelected()){
                        try {
                            drawGraph('a',e);
                        } catch (IntervalException intervalException) {
                            intervalException.printStackTrace();
                        }
                    }
                }
            }
        });
        refresh();
    }

    private void refreshChoice(){
        ArrayList<String> list = new ArrayList<String>();
        for (Equation elt : Equation.getEquations()){
            list.add(elt.getName());
        }
        solver.setItems(FXCollections.observableArrayList(list.toArray()));
        expr1.setItems(FXCollections.observableArrayList(list.toArray()));
        expr2.setItems(FXCollections.observableArrayList(list.toArray()));
    }

    /**
     * dans value, mettre "valeur > bornInf;BornSup"
     * */
    private void solveEquation(){
        String solveEquaName = (String) solver.getValue();
        String[] valueTextField = value.getText().replaceAll(" ","").split(">");
        Double solution = 0.00;
        for(Equation e : Equation.getEquations()){
            if(e.getName() == solveEquaName){
                solution = e.solveEquation(valueTextField);
            }
        }
        solved.setText(Double.toString(solution));
    }

    private void combineEquation(){
        String expr1Name = expr1.getValue().toString();
        String expr2Name = expr2.getValue().toString();
        String expr1e = "0";
        String expr2e = "0";
        for(Equation e : Equation.getEquations()){
            if(e.getName() == expr1Name){
                expr1e = e.getExpression();
                expr2e = e.getExpression();
            }
        }
        String solution = "0";
        switch (operation.getValue().toString().charAt(0)){
            case '+':
                solution = expr1e + "+(" + expr2e +")";
                break;
            case '-':
                solution = expr1e + "-(" + expr2e +")";
                break;
            case '*':
                solution = expr1e + "*(" + expr2e +")";
                break;
            case '/':
                solution = expr1e + "/(" + expr2e +")";
                break;
            case '^':
                solution = expr1e + "-^" + expr2e +")";
                break;
            default:
                break;
        }
        store(name.getText()+" = "+solution +" = -5;5;0.1");
    }

    public void initialize(URL url, ResourceBundle resourceBundle){
        operation.setItems(FXCollections.observableArrayList("+","-","*","/","^"));
        refresh();
        store.setOnAction(new EventHandler<ActionEvent>(){
            public void handle (ActionEvent ae){
                store(input.getText());
            }
        });

        draw.setOnAction(new EventHandler<ActionEvent>(){
            public void handle (ActionEvent ae){
                //Equation.addEquation(new Equation(input.getText()));
                try {
                    Equation e = new Equation(input.getText());
                    drawGraph('a',e);
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
                    Equation.deleteEquation(tmp.elementAt(j)-j);
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
                try {
                    drawGraphD();
                } catch (IntervalException e) {
                    e.printStackTrace();
                }
            }
        });
        integral.setOnAction(new EventHandler<ActionEvent>(){
            public void handle (ActionEvent ae){
                try {
                    drawGraphP();
                } catch (IntervalException e) {
                    e.printStackTrace();
                }
            }
        });
        clear.setOnAction(new EventHandler<ActionEvent>(){
            public void handle (ActionEvent ae){
                clearGraph();
            }
        });
        solve.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                solveEquation();
            }
        });
        combine.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                combineEquation();
            }
        });
    }
}