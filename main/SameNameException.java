package main;
/**
 * Exception qui signale que le nom demandé est déjà utilisé
 */
public class SameNameException extends Exception {
    public SameNameException(){System.out.println("Function name already taken, please choose another one");}
}
