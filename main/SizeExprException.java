package main;


/**
 * Exception qui signale que le tableau sur lequel on travail n'a pas la bonne taille
 */
public class SizeExprException extends Exception {
    public SizeExprException(){
        System.out.println("la syntaxe de l'expression n'est pas bonne");
    }
}
