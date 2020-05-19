package main;

import java.util.Vector;

public class Equation {
    private String name;
    private String expression;
    private String interval;

    private static Vector<Equation> equations = new Vector<Equation>();

    //Constructeur vide
    public Equation() {
        this.expression = "";
        this.name = "";
        this.interval = "";
    }

    //constructeur si on veut rentrer les paramètres à la mano
    public Equation(String expr, String name, String interval){
        this.expression = expr;
        this.name = name;
        this.interval = interval;
    }


    //constructeur utilisant une expression complete
    public Equation(String equation){

        try {
            String[] expr = parseExpr(expression);
            this.expression = expr[1];
            this.name = expr[0];
            this.interval = expr[2];

        } catch (SizeExprException e) {
            //System.out.println("mauvaise syntaxe dans l'expression");
            e.printStackTrace();
        }


    }

    //méthode au cas où on a construit un objet vide et qu'on veut le remplir, fonctionne comme le dernier constructeur
    public void addValue(String equation){
        try {
            String[] expr = parseExpr(expression);
            this.expression = expr[1];
            this.name = expr[0];
            this.interval = expr[2];

        } catch (SizeExprException e) {
            //System.out.println("mauvaise syntaxe dans l'expression");
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

    public static void setEquations(Vector<Equation> equations) {
        Equation.equations = equations;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public void setname(String name) {
        this.name = name;
    }
}