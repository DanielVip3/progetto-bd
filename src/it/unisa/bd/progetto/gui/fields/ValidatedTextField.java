package it.unisa.bd.progetto.gui.fields;

import javax.swing.*;

public class ValidatedTextField extends JFormattedTextField {
    private int minLength;
    private int maxLength;

    public ValidatedTextField() {
        minLength = 0;
        maxLength = 45;
    }

    public int getMinLength() { return minLength; }

    public int getMaxLength() { return maxLength; }

    public void bounds(int min, int max) {
        minLength = min;
        maxLength = max;
    }
}
