package it.unisa.bd.progetto.gui.fields;

import javax.swing.*;

public class ValidatedNumberSpinner extends JSpinner implements ValidatedField<Integer> {
    public ValidatedNumberSpinner() {
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(this);
        editor.getFormat().setGroupingUsed(false);
        setEditor(editor);
    }

    public void bounds(int minimum, int maximum, int defaultValue) {
        SpinnerNumberModel model = (SpinnerNumberModel) getModel();
        model.setValue(defaultValue);
        model.setMinimum(minimum);
        model.setMaximum(maximum);
        model.setStepSize(1);
        setModel(model);
    }

    public boolean canSubmit() { return true; }

    public Integer getContent() {
        return (Integer) getValue();
    }
}
