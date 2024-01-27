package it.unisa.bd.progetto.gui.errors;

import javax.swing.*;

public class ErrorMessage {
    public ErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    public ErrorMessage(String message, boolean fatal) {
        JOptionPane.showMessageDialog(null, message, "Errore", JOptionPane.ERROR_MESSAGE);

        if (fatal) System.exit(1);
    }
}
