package main;

/**
 * Exception qui signale que l'intervalle entré n'est pas valide
 */
public class IntervalException extends Exception {
    public IntervalException() { System.out.println("borne de l'intervalle non valide. Ou le pas est trop grand.");}
}
