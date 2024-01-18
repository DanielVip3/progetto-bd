package it.unisa.bd.progetto.gui.fields;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ValidatedDateField extends JFormattedTextField {
    public ValidatedDateField() {
        DateFormatter formatter = new DateFormatter();
        formatter.setAllowsInvalid(true);
        formatter.setFormat(new SimpleDateFormat("dd/MM/yyyy"));
        setFormatterFactory(new DefaultFormatterFactory(formatter));
    }
}
