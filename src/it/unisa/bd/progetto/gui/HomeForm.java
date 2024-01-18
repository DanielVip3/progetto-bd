package it.unisa.bd.progetto.gui;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import it.unisa.bd.progetto.core.Film;
import it.unisa.bd.progetto.core.Persona;
import it.unisa.bd.progetto.core.TipoPersona;
import it.unisa.bd.progetto.gui.errors.ErrorMessage;
import it.unisa.bd.progetto.gui.fields.ValidatedDateField;
import it.unisa.bd.progetto.gui.fields.ValidatedNumberField;
import it.unisa.bd.progetto.gui.fields.ValidatedNumberSpinner;
import it.unisa.bd.progetto.gui.fields.ValidatedTextField;
import it.unisa.bd.progetto.gui.tables.*;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.time.Year;

public class HomeForm {
    private JTabbedPane tabbedPane;
    private JPanel root;
    private ValidatedNumberField codiceTextField;
    private ValidatedTextField titoloTextField;
    private ValidatedNumberSpinner durataSpinner;
    private ValidatedNumberSpinner annoSpinner;
    private ValidatedNumberSpinner etaMinimaSpinner;
    private JComboBox<String> registaComboBox;
    private JComboBox<String> tipoComboBox;
    private ValidatedDateField dataDiNascitaTextField;
    private ValidatedTextField cognomeTextField;
    private ValidatedTextField nomeTextField;
    private JPanel additionalPersonaPanel;
    private JLabel additionalPersonaLabel;
    private JTextField searchTextField;
    private FilmTable filmTable;
    private PersoneTable personeTable;
    private JButton deleteButton;
    private JButton addFilmButton;
    private JButton addPersonaButton;
    private ValidatedNumberField additionalPersonaTextField;


    public static void main(String[] args) {
        FlatMacLightLaf.setup();
        UIManager.put("Table.alternateRowColor", new Color(215,225,238));

        JFrame frame = new JFrame("Cinema");
        frame.setContentPane(new HomeForm().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(new Dimension(700, 500));
    }

    public HomeForm() {
        TablesUIManager tablesUIManager = new TablesUIManager(tabbedPane, new DatabaseTable[]{filmTable, personeTable}, deleteButton, registaComboBox);

        durataSpinner.bounds(1, 6000, 120);
        annoSpinner.bounds(1900, Year.now().getValue() + 10, Year.now().getValue());
        etaMinimaSpinner.bounds(0, 18, 0);

        /* Event 1: change of type in combobox changes shown additional text panel */
        tipoComboBox.addActionListener(e -> {
            String selectedItem = (String) tipoComboBox.getSelectedItem();
            if (selectedItem == null || selectedItem.isBlank() || selectedItem.isEmpty()) return;

            TipoPersona type = TipoPersona.fromString((String) tipoComboBox.getSelectedItem());

            switch (type) {
                default -> additionalPersonaPanel.setVisible(false);
                case ARTISTA -> {
                    additionalPersonaLabel.setText("# premi vinti");
                    additionalPersonaPanel.setVisible(true);
                }
                case IMPIEGATO -> {
                    additionalPersonaLabel.setText("Matricola");
                    additionalPersonaPanel.setVisible(true);
                }
            }
        });

        /* Event 2: on searching, populates table with filtered results */
        searchTextField.addActionListener(e -> {
            String text = searchTextField.getText();
            tablesUIManager.populateTable(tablesUIManager.getCurrentTable(), text);
        });

        /* Event 3: on tab change, resets search field */
        tabbedPane.addChangeListener(e -> searchTextField.setText(""));

        /* Event 4: on Film add, validate input and insert in database and in table */
        addFilmButton.addActionListener(e -> {
            String codice = codiceTextField.getText();
            String titolo = titoloTextField.getText();
            String durata = annoSpinner.getValue().toString();
            String anno = durataSpinner.getValue().toString();
            String etaMinima = etaMinimaSpinner.getValue().toString();
            String regista = (String) registaComboBox.getSelectedItem();

            try {
                // Using fromRow and not normal constructor because in this way we also do validation
                Film film = Film.fromRow(new String[]{codice, titolo, durata, anno, etaMinima, regista});
                filmTable.insert(film);

                filmTable.addRow(film);
            } catch (SQLException | InvalidParameterException ex) {
                new ErrorMessage(ex.getMessage());
            }
        });

        /* Event 5: on Persona add, validate input and insert in database and in table */
        addPersonaButton.addActionListener(e -> {
            String nome = nomeTextField.getText();
            String cognome = cognomeTextField.getText();
            String dataDiNascita = dataDiNascitaTextField.getText();
            String additionalField = additionalPersonaTextField.getText();
            TipoPersona tipo = TipoPersona.fromString((String) tipoComboBox.getSelectedItem());

            try {
                // Using fromRow and not normal constructor because in this way we also do validation
                Persona persona = Persona.fromRow(new String[]{"0", nome, cognome, dataDiNascita, tipo.toString(), additionalField});
                int codiceID = personeTable.insert(persona);

                persona.setCodiceID(codiceID);

                personeTable.addRow(persona);
            } catch (SQLException | InvalidParameterException ex) {
                new ErrorMessage(ex.getMessage());
            }
        });

        /* Event 6: on Film cell update, update in database too; on error reset edit */
        filmTable.getDefaultEditor(String.class).addCellEditorListener(new CellEditorListener() {
            public void editingCanceled(ChangeEvent e) {}
            public void editingStopped(ChangeEvent e) {
                int row = filmTable.getSelectedRow();
                int column = filmTable.getSelectedColumn();

                String[] fields = new String[6];
                for (int i = 0; i < 6; i++) fields[i] = (String) filmTable.getValueAt(row, i);

                try {
                    filmTable.update(Film.fromRow(fields));
                } catch (SQLException | InvalidParameterException ex) {
                    new ErrorMessage(ex.getMessage());
                    filmTable.resetLastEdit(row, column);
                }
            }
        });

        /* Event 7: on Persona cell update, update in database too; on error reset edit */
        personeTable.getDefaultEditor(String.class).addCellEditorListener(new CellEditorListener() {
            public void editingCanceled(ChangeEvent e) {}
            public void editingStopped(ChangeEvent e) {
                int row = personeTable.getSelectedRow();
                int column = personeTable.getSelectedColumn();

                String[] fields = new String[6];
                for (int i = 0; i < 6; i++) fields[i] = (String) personeTable.getValueAt(row, i);

                try {
                    personeTable.update(Persona.fromRow(fields));

                    if (column == 4) { // if edited Tipo, we reset the additional fields (they will be set to null in database too)
                        personeTable.setValueAt("-", row, 5);
                        personeTable.setValueAt("-", row, 6);
                    }
                } catch (SQLException | InvalidParameterException ex) {
                    new ErrorMessage(ex.getMessage());
                    personeTable.resetLastEdit(row, column);
                }
            }
        });

        /* Event 8: on delete button press, delete in database and in table */
        deleteButton.addActionListener(e -> {
            DatabaseTable<? extends RowData> currentTable = tablesUIManager.getCurrentTable();
            int selectedRow = currentTable.getSelectedRow();
            if (selectedRow < 0) return;

            try {
                currentTable.delete(currentTable.getPrimaryKeyForRow(selectedRow));
                currentTable.removeRow(selectedRow);
            } catch (SQLException ex) {
                new ErrorMessage(ex.getMessage());
            }
        });
    }
}
