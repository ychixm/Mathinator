package main;

public class SameNameException extends Exception {
    public SameNameException(){System.out.println("Function name already taken, please choose another one");}
}
