package main;

import javafx.scene.control.CheckBox;

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
    private CheckBox draw;

    private static Vector<Equation> equations = new Vector<Equation>();

    //Constructeur vide
    public Equation() {
        this.expression = "";
        this.name = "";
        this.interval = "";
        this.select = new CheckBox();
        this.draw = new CheckBox();
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
        this.draw = new CheckBox();
        this.nomVariable = var;
        this.bornInf = inf;
        this.bornSup = sup;
        this.pas = pas ;

    }


    /**
     * @param equation String contenant l'entrée utilisateur.
     * constructeur utilisant une expression complete*/
    public Equation(String equation){
        try {
            String[] expr = parseExpr(equation);


            this.expression = expr[1];
            this.name = expr[0];
            this.interval = expr[2];
            this.select = new CheckBox();
            this.draw = new CheckBox();

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
        if(this.name.matches("[A-Za-z]*[(][A-Za-z]*[)]")){
            String[] getNewName = this.name.split("\\(");
            this.name = getNewName[0];
            this.nomVariable = getNewName[1].replaceAll("\\)", "");
        }
        else{
            this.nomVariable = "x";
        }


    }

    /**
     * fonction pour parser l'expression de l'équation
     * l'expression doit être de la forme "name(variable) = expression = born inf;borne sup */
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
    /**
     * Getteur de la de l'Objet equation
     * */
    public String getNomVariable() {
        return this.nomVariable;
    }

    public void setNomVariable(String var) {
        this.nomVariable = var;
    }

    public Double getBornInf() {
        return bornInf;
    }

    public Double getBornSup() {
        return bornSup;
    }

    public Double getPas() {
        return pas;
    }

    /**
     * Setteur de l'Objet equation
     * */

    public void setBornInf(Double bornInf) {
        this.bornInf = bornInf;
    }

    public void setBornSup(Double bornSup) {
        this.bornSup = bornSup;
    }

    public void setPas(Double pas) {
        this.pas = pas;
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

    public CheckBox getDraw() {
        return draw;
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

    /**
     * Méthode supprimant l'équation à l'indice souhaité
     * */
    public static void deleteEquation(int i){equations.remove(i);}

    /**
     * calcul des valeurs de l'expression de fonction */
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

    /**
     * calcul des valeurs de l'expression de la fonction dérivée */
    public Pair<Vector<Double>,Vector<Double>> calcFuncDeriv() throws IntervalException {

        //calcul des valeurs de la fonction dérivée
        Vector<Double> X = new Vector<Double>();
        Vector<Double> Y = new Vector<Double>();
        for (Double i = bornInf; i <= bornSup; i = i + pas){
            Expression e = new Expression("der("+this.expression+","+this.nomVariable+","+i.toString()+")");
            Y.add(e.calculate());
            X.add(i);
        }

        Pair<Vector<Double>,Vector<Double>> valFunc = new Pair<>(X,Y);
        return valFunc;

    }

    /**
     * calcul des valeurs de l'expression de la fonction intégrée */
    public Pair<Vector<Double>,Vector<Double>> calcFuncInt() throws IntervalException {

        //calcul des valeurs de la fonction intégrée
        Vector<Double> X = new Vector<Double>();
        Vector<Double> Y = new Vector<Double>();
        for (Double i = bornInf; i <= bornSup; i = i + pas){
            Expression e = new Expression("int("+this.expression+","+this.nomVariable+",0,"+i.toString()+")");
            Y.add(e.calculate());
            X.add(i);
        }

        Pair<Vector<Double>,Vector<Double>> valFunc = new Pair<>(X,Y);
        return valFunc;

    }

    //fonction pour résoudre une équation avec une valeur
    public Double solveEqua(String[] param){
        String[] interval = param[1].split(";");
        Expression e = new Expression("solve("+this.expression+"-"+param[0]+","+this.nomVariable+","+interval[0]+","+interval[1]+")");
        return e.calculate();
    }
}