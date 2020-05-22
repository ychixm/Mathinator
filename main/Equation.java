package main;

import javafx.scene.control.CheckBox;

import java.util.Stack;
import java.util.Vector;
import org.mariuszgromada.math.mxparser.*;

public class Equation {
    private String name;
    private String expression;
    private String interval;
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
    }

    //constructeur si on veut rentrer les paramètres à la mano
    public Equation(String expr, String name, String interval, String var){
        this.expression = expr;
        this.name = name;
        this.interval = interval;
        this.select = new CheckBox();
        this.nomVariable = var;

    }


    //constructeur utilisant une expression complete
    public Equation(String equation){

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
        //voir si le nom de la variable est entrée ou non
        //ATTENTION : A TESTER
        try{
            boolean test = this.name.matches("[\\(][A-Za-z]*[\\)]");
            if (test == true){
                String[] split = this.name.split("[\\(]", 1);
                split[1].replaceAll("[\\(]", "");
                split[1].replaceAll("[\\)]", "");
                this.nomVariable = split[1];
            }
            else {
                throw new PasDeVariableException();
            }

        } catch (PasDeVariableException e) {
            e.printStackTrace();
        }


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
        try{
            boolean test = this.name.matches("[\\(][A-Za-z]*[\\)]");
            if (test == true){
                String[] split = this.name.split("[\\(]", 1);
                split[1].replaceAll("[\\(]", "");
                split[1].replaceAll("[\\)]", "");
                this.nomVariable = split[1];
            }
            else {
                throw new PasDeVariableException();
            }

        } catch (PasDeVariableException e) {
            e.printStackTrace();
        }
    }

    //fonction pour parser l'expression de l'équation
    //l'expression doit être de la forme "name(variable) = expression = born inf;borne sup
    private String[] parseExpr (String expr) throws SizeExprException{
        String tmp = expr.replaceAll(" ","");
        String[] tmp2 = tmp.split("=");
        if(tmp2.length !=3){
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
    public Vector<Double> calcFunc() throws IntervalException {
        //récupération de l'intervalle et du pas
        // ATTENTION : Tester String vide si rend un tableau vide
        String tmp = this.interval.replaceAll(" ","");
        String[] tmp2 = tmp.split(";");
        Double bornInf = 0.00;
        Double bornSup = 10.00;
        Double pas = 1.00 ;
        //valeurs de l'intervalle avec pas non reconnues, on prendra des valeurs par défaut.
        if(tmp2.length == 2 || tmp2.length == 3){
            bornInf = Double.parseDouble(tmp2[0]);
            bornSup = Double.parseDouble(tmp2[1]);
            if(tmp2.length == 3){
                pas = Double.parseDouble(tmp2[2]);
            }

        }
        if(bornInf > bornSup){
            throw new IntervalException();
        }
        else if(pas > bornSup - bornInf){
            throw new IntervalException();
        }
        //Le pas et l'intervalle sont set


        //Code pas bon en dessous


        //mise en place de la pile pour calculer dans l'ordre
        //Stack<String> pile = new Stack<String>();
        //fonction de pile :
        //pile = pileFonction();



        //else{

        //Vector<Double> valFunc = new Vector<Double>();
        //for (Double i = bornInf; i < bornSup; i = i + pas){
        //    valFunc.add(valeurCalc(pile, i));
        //}

        //return valFunc;

        //reprise du code bon

        Function f = new Function(this.name, this.expression, this.nomVariable);
        Vector<Double> valFunc = new Vector<Double>();
        for (Double i = bornInf; i < bornSup; i = i + pas){
            valFunc.add(f.calculate(i));
        }
        return valFunc;

    }

    //Fonction pour set la pile de calcul des valeurs de la fonction
    //public Stack<String> pileFonction(){
    //    Stack<String> pile = new Stack<String>();
    //
    //    return pile;
    //}

    //public Double valeurCalc(Stack<String> pile, Double val){
    //    Double valFinal = 0.00;
    //    while(!pile.empty()){
    //        //dépiler en remplaçant la variable par val
    //     }
    //    return valFinal;
    //}


}