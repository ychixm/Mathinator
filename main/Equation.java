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

    /**
     * Constructeur vide
     * */
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

    /**
     * constructeur si on veut rentrer les paramètres à la mano
     * */
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
     * constructeur utilisant une expression complete.
     * */
    public Equation(String equation){
        try {
            //parsing de l'expression.
            String[] expr = parseExpr(equation);

            //test pour savoir si la variable est dans le nom de la fonction : du type f(x).
            if(expr[0].matches("[A-Za-z]*[(][A-Za-z]*[)]")){

                String[] getNewName = expr[0].split("\\(");
                expr[0] = getNewName[0];

                this.nomVariable = getNewName[1].replaceAll("\\)", "");
            }
            //si la variable n'est pas présente, on donne x comme variable par défaut.
            else{
                this.nomVariable = "x";
            }

            //Test du nom, si le nom donné existe déjà, on envoie une exception.
            for(Equation elt : Equation.getEquations()){
                if(expr[0].equals(elt.getName())){
                    throw new SameNameException();
                }
            }

            this.expression = expr[1];
            this.name = expr[0];
            this.interval = expr[2];
            this.select = new CheckBox();
            this.draw = new CheckBox();

        } catch (SizeExprException e) {
            e.printStackTrace();
        } catch (SameNameException e) {
            e.printStackTrace();
        }


        //récupération de l'intervalle et du pas :
            //valeurs par défaut.
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
            //exception si les bornes ne sont pas compatibles.
            if(bornInf > bornSup){
                throw new IntervalException();
            }
            //exception si le pas n'est pas compatible.
            else if(pas > bornSup - bornInf){
                throw new IntervalException();
            }
        } catch (IntervalException e) {
            e.printStackTrace();
        }
    }

    /**
     * fonction pour parser l'expression de l'équation.
     * l'expression doit être de la forme "name(variable) = expression = born inf;borne sup.
     * */
    private String[] parseExpr (String expr) throws SizeExprException{
        String tmp = expr.replaceAll(" ","");
        String[] tmp2 = tmp.split("=");
        //si la syntaxe n'est pas bien suivie, on envoie une exception.
        if(tmp2.length !=3){
            throw new SizeExprException();
        }
        else{
            return tmp2;
        }

    }



    /**
     * Getter de la de l'Objet equation
     * */

    public String getNomVariable() {
        return this.nomVariable;
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

    public static void addEquation(Equation e){
        equations.add(e);
    }

    /**
     * Setter de l'Objet equation
     * */

    public void setBornInf(Double bornInf) {
        this.bornInf = bornInf;
    }

    public void setNomVariable(String var) {
        this.nomVariable = var;
    }

    public void setBornSup(Double bornSup) {
        this.bornSup = bornSup;
    }

    public void setPas(Double pas) {
        this.pas = pas;
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
     * Méthode supprimant l'équation à l'indice souhaité.
     * */
    public static void deleteEquation(int i){equations.remove(i);}

    /**
     * calcul des valeurs de l'expression de fonction.
     * */
    public Pair<Vector<Double>,Vector<Double>> calcFunc() throws IntervalException {

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
     * calcul des valeurs de l'expression de la fonction dérivée.
     * */
    public Pair<Vector<Double>,Vector<Double>> calcFuncDeriv() throws IntervalException {

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
     * calcul des valeurs de l'expression de la fonction intégrée.
     * */
    public Pair<Vector<Double>,Vector<Double>> calcFuncInt() throws IntervalException {

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

    /**
     * fonction pour résoudre une équation avec une valeur.
     * */
    public Double solveEquation(String[] param){
        try {
            //si la syntaxe n'est pas bien suivie, on envoie une exception
            if (param.length != 2) {
                throw new SizeExprException();

            }
        }catch (SizeExprException e) {e.printStackTrace(); }

        String[] subParam = param[1].split(";");
        try{
            //s'il n'y a pas le bon nombre de parametre entré on envoie une expection.
            if(subParam.length != 2){
                throw new SizeExprException();

            }
            //si un des paramètre n'est pas une nombre on envoie une exception.
            if(!param[0].matches("[0-9]*") || !subParam[0].matches("[0-9]*") || !subParam[1].matches("[0-9]*")){
                throw new NumberExpectedException();
            }

        }
        catch(NumberExpectedException e){
            e.printStackTrace();

        } catch (SizeExprException e) {
            e.printStackTrace();
        }
        //si on ne rencontre aucun problème, on résoud l'équation
        Expression e = new Expression("solve("+this.expression+"-"+param[0]+","+this.nomVariable+","+subParam[0]+","+subParam[1]+")");
        return e.calculate();
    }
}