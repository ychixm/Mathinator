package Calc;

import javafx.util.Pair;
import java.util.Vector;

public class Equation {
    private String name;
    private String expression;
    private Pair<Double,Double> interval;
    private static Vector<Equation> equations = new Vector<Equation>();

    public Equation(String name, String expression, Pair<Double,Double> interval){
        this.name = name;
        this.expression = expression;
        this.interval = interval;
    }

    public Pair<Double, Double> getInterval() {
        return interval;
    }

    public String getExpression() {
        return expression;
    }

    public String getName() {
        return name;
    }

    public static Vector<Equation> getEquations() {
        return equations;
    }

    public void setInterval(Pair<Double, Double> interval) {
        this.interval = interval;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void setEquations(Vector<Equation> equations) {
        Equation.equations = equations;
    }
}
