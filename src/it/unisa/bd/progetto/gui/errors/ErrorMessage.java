package it.unisa.bd.progetto.gui.errors;

import javax.swing.*;

public class ErrorMessage {
    public ErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }
}
