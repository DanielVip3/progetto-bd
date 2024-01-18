package it.unisa.bd.progetto.gui.fields;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;

public class ValidatedNumberField extends JFormattedTextField implements ValidatedField<Integer> {
    private final NumberFormatter formatter = new NumberFormatter();

    public ValidatedNumberField() {
        NumberFormat intFormat = NumberFormat.getIntegerInstance();
        intFormat.setGroupingUsed(false);

        formatter.setFormat(intFormat);
        formatter.setAllowsInvalid(true);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setValueClass(Integer.class);
        setFormatterFactory(new DefaultFormatterFactory(formatter));
    }

    public void bounds(int minimum, int maximum) {
        formatter.setMinimum(minimum);
        formatter.setMaximum(maximum);
        setFormatterFactory(new DefaultFormatterFactory(formatter));
    }

    public boolean canSubmit() {
        return isEditValid() && !getText().isBlank();
    }

    public Integer getContent() {
        return Integer.parseInt(getText());
    }
}
