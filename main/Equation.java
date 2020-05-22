package main;

import javafx.scene.control.CheckBox;

import java.util.Stack;
import java.util.Vector;

import javafx.util.Pair;
import org.mariuszgromada.math.mxparser.*;

public class Equation {
    private String name;
    private String expression;
    private String interval;
    private Double bornInf;
    private Double bornSup;
    private Double pas;
    private String nomVariable;
    private CheckBox select;

    private static Vector<Equation> equations = new Vector<Equation>();

    //Constructeur vide
    public Equation() {
        this.expression = "";
        this.name = "";
        this.interval = "";
        this.select = new CheckBox();
        this.nomVariable = "x";
        this.bornInf = 0.00;
        this.bornSup = 10.00;
        this.pas = 1.00 ;
    }

    //constructeur si on veut rentrer les paramètres à la mano
    public Equation(String expr, String name, String interval, String var, Double sup, Double inf, Double pas){
        this.expression = expr;
        this.name = name;
        this.interval = interval;
        this.select = new CheckBox();
        this.nomVariable = var;
        this.bornInf = inf;
        this.bornSup = sup;
        this.pas = pas ;

    }


    //constructeur utilisant une expression complete
    public Equation(String equation){
        try {
            String[] expr = parseExpr(equation);


            this.expression = expr[1];
            this.name = expr[0];
            this.interval = expr[2];
            this.nomVariable = expr[3];
            this.select = new CheckBox();

        } catch (SizeExprException e) {
            e.printStackTrace();
        }


        //récupération de l'intervalle et du pas
        this.bornInf = 0.00;
        this.bornSup = 10.00;
        this.pas = 1.00 ;
        try{
            String tmp = this.interval.replaceAll(" ","");
            String[] tmp2 = tmp.split(";");
            if(tmp2.length == 2 || tmp2.length == 3){
                this.bornInf = Double.parseDouble(tmp2[0]);
                this.bornSup = Double.parseDouble(tmp2[1]);
                if(tmp2.length == 3){
                    this.pas = Double.parseDouble(tmp2[2]);

                }

            }
            if(bornInf > bornSup){
                throw new IntervalException();
            }
            else if(pas > bornSup - bornInf){
                throw new IntervalException();
            }
        } catch (IntervalException e) {
            e.printStackTrace();
        }
        //Le pas et l'intervalle sont set

        //voir si le nom de la variable est entrée ou non
        //ATTENTION : A TESTER
        //try{
        //boolean varIsIn = false;
        //String name2 = this.name;

        //for(char elt : name2.toCharArray()){
        //    if(elt == "("){
        //        varIsIn = true;
        //    }
        //}
        //System.out.println(varIsIn);
        //    if (test == true){
        //        String[] split = this.name.split("[\\(]", 1);
        //        split[1].replaceAll("[\\(]", "");
        //        split[1].replaceAll("[\\)]", "");
        //        this.nomVariable = split[1];
        //    }
        //    else {
        //        throw new PasDeVariableException();
        //    }

        //} catch (PasDeVariableException e) {
        //    e.printStackTrace();
        //}


    }

    //méthode au cas où on a construit un objet vide et qu'on veut le remplir, fonctionne comme le dernier constructeur
    public void addValue(String equation){
        try {
            String[] expr = parseExpr(equation);
            this.expression = expr[1];
            this.name = expr[0];
            this.interval = expr[2];
            this.select = new CheckBox();

        } catch (SizeExprException e) {
            //System.out.println("mauvaise syntaxe dans l'expression");
            e.printStackTrace();
        }

        //récupération de l'intervalle et du pas
        this.bornInf = 0.00;
        this.bornSup = 10.00;
        this.pas = 1.00 ;
        try{
            String tmp = this.interval.replaceAll(" ","");
            String[] tmp2 = tmp.split(";");
            if(tmp2.length == 2 || tmp2.length == 3){
                this.bornInf = Double.parseDouble(tmp2[0]);
                this.bornSup = Double.parseDouble(tmp2[1]);
                if(tmp2.length == 3){
                    this.pas = Double.parseDouble(tmp2[2]);

                }

            }
            if(bornInf > bornSup){
                throw new IntervalException();
            }
            else if(pas > bornSup - bornInf){
                throw new IntervalException();
            }
        } catch (IntervalException e) {
            e.printStackTrace();
        }

        //Le pas et l'intervalle sont set
        //try{
        //    boolean test = this.name.matches("[\\(][A-Za-z]*[\\)]");
        //    if (test == true){
        //        String[] split = this.name.split("[\\(]", 1);
        //        split[1].replaceAll("[\\(]", "");
        //        split[1].replaceAll("[\\)]", "");
        //        this.nomVariable = split[1];
        //    }
        //    else {
        //        throw new PasDeVariableException();
        //    }

        //} catch (PasDeVariableException e) {
        //    e.printStackTrace();
        //}
    }

    //fonction pour parser l'expression de l'équation
    //l'expression doit être de la forme "name(variable) = expression = born inf;borne sup
    private String[] parseExpr (String expr) throws SizeExprException{
        String tmp = expr.replaceAll(" ","");
        String[] tmp2 = tmp.split("=");
        if(tmp2.length !=4){
            throw new SizeExprException();
        }
        else{
            return tmp2;
        }

    }

    public String getNomVariable() {
        return this.nomVariable;
    }

    public void setNomVariable(String var) {
        this.nomVariable = var;
    }

    public static void addEquation(Equation e){
        equations.add(e);
    }

    public static Vector<Equation> getEquations() {
        return equations;
    }

    public String getExpression() {
        return expression;
    }

    public String getInterval() {
        return interval;
    }

    public String getName() {
        return name;
    }

    public CheckBox getSelect() {
        return select;
    }

    public static void setEquations(Vector<Equation> equations) {
        Equation.equations = equations;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

    public static void deleteEquation(int i){equations.remove(i);}

    //calcul des valeurs de l'expression de fonction
    public Pair<Vector<Double>,Vector<Double>> calcFunc() throws IntervalException {

        //calcul des valeurs de la fonction
        Function f = new Function(this.name, this.expression, this.nomVariable);
        Vector<Double> X = new Vector<Double>();
        Vector<Double> Y = new Vector<Double>();
        for (Double i = bornInf; i <= bornSup; i = i + pas){
            Y.add(f.calculate(i));
            X.add(i);
        }

        Pair<Vector<Double>,Vector<Double>> valFunc = new Pair<>(X,Y);
        return valFunc;

    }




}