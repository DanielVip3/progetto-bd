package it.unisa.bd.progetto.gui.fields;

import javax.swing.*;

public interface ValidatedField<T> {
    boolean canSubmit();
    T getContent();
}
