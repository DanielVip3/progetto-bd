package it.unisa.bd.progetto.gui.errors;

public class EmptyResultException extends RuntimeException {
    public EmptyResultException(String what) {
        super("Impossibile effettuare " + what + " .");
    }
}
