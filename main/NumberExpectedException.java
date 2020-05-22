package main;

/**
 * Exception qui signale que ce le paramètre attendu est censé être un nombre mais ne l'est pas
 */
public class NumberExpectedException extends Exception{
    public NumberExpectedException(){
        System.out.println("value must be a number");
    }
}
