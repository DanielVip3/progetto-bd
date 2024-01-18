package it.unisa.bd.progetto.gui;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import it.unisa.bd.progetto.core.Database;
import it.unisa.bd.progetto.core.Film;
import it.unisa.bd.progetto.core.Persona;
import it.unisa.bd.progetto.core.TipoPersona;
import it.unisa.bd.progetto.gui.errors.ErrorMessage;
import it.unisa.bd.progetto.gui.fields.ValidatedDateField;
import it.unisa.bd.progetto.gui.fields.ValidatedNumberField;
import it.unisa.bd.progetto.gui.fields.ValidatedNumberSpinner;
import it.unisa.bd.progetto.gui.fields.ValidatedTextField;
import it.unisa.bd.progetto.gui.tables.DatabaseTable;
import it.unisa.bd.progetto.gui.tables.FilmTable;
import it.unisa.bd.progetto.gui.tables.PersoneTable;
import it.unisa.bd.progetto.gui.tables.RowData;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;

public class HomeForm {
    private JTabbedPane tabbedPane;
    private JPanel root;
    private ValidatedNumberField codiceTextField;
    private ValidatedTextField titoloTextField;
    private ValidatedNumberSpinner annoTextField;
    private ValidatedNumberSpinner durataTextField;
    private ValidatedNumberSpinner etaMinimaTextField;
    private JComboBox<String> registaComboBox;
    private JComboBox<String> tipoComboBox;
    private ValidatedDateField dataDiNascitaTextField;
    private ValidatedTextField cognomeTextField;
    private ValidatedTextField nomeTextField;
    private JPanel additionalPersonaPanel;
    private JLabel additionalPersonaPanelLabel;
    private JTextField searchTextField;
    private FilmTable filmTable;
    private PersoneTable personeTable;
    private JButton deleteButton;
    private JButton addFilmButton;
    private JButton addPersonaButton;
    private ValidatedNumberField additionalPersonaPanelTextField;


    public static void main(String[] args) {
        FlatMacLightLaf.setup();
        UIManager.put("Table.alternateRowColor", new Color(215,225,238));

        JFrame frame = new JFrame("Cinema");
        frame.setContentPane(new HomeForm().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(new Dimension(600, 450));
    }

    private void deleteButtonPress(DatabaseTable<? extends RowData> currentTable) {

    }

    public HomeForm() {
        TablesUIManager tablesUIManager = new TablesUIManager(tabbedPane, new DatabaseTable[]{filmTable, personeTable}, deleteButton, registaComboBox);

        annoTextField.bounds(1900, Year.now().getValue() + 10, Year.now().getValue());
        durataTextField.bounds(1, 6000, 120);
        etaMinimaTextField.bounds(0, 18, 0);

        /* Event 1: change of type in combobox changes shown additional text panel */
        tipoComboBox.addActionListener(e -> {
            String selectedItem = (String) tipoComboBox.getSelectedItem();
            if (selectedItem == null || selectedItem.isBlank() || selectedItem.isEmpty()) return;

            TipoPersona type = TipoPersona.fromString((String) tipoComboBox.getSelectedItem());

            switch (type) {
                default -> additionalPersonaPanel.setVisible(false);
                case ARTISTA -> {
                    additionalPersonaPanelLabel.setText("# premi vinti");
                    additionalPersonaPanel.setVisible(true);
                }
                case IMPIEGATO -> {
                    additionalPersonaPanelLabel.setText("Matricola");
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
            if (!codiceTextField.canSubmit()) {
                new ErrorMessage("Il codice inserito non è valido!");
                return;
            }
            int codice = codiceTextField.getContent();

            if (!titoloTextField.canSubmit()) {
                new ErrorMessage("Il titolo inserito non è valido (stringa compresa tra 1 e 45 caratteri)!");
                return;
            }
            String titolo = titoloTextField.getContent();

            /* Always valid because spinner can never be invalid */
            int durata = durataTextField.getContent();
            int anno = annoTextField.getContent();
            int etaMinima = etaMinimaTextField.getContent();

            String regista = (String) registaComboBox.getSelectedItem();
            Film film = new Film(codice, titolo, durata, anno, etaMinima, regista);

            try {
                filmTable.insert(film);
                filmTable.addRow(film);
            } catch (SQLException ex) {
                new ErrorMessage(ex.getMessage());
            }
        });

        /* Event 5: on Persona add, validate input and insert in database and in table */
        addPersonaButton.addActionListener(e -> {
            String tipoString = (String) tipoComboBox.getSelectedItem();
            if (tipoString == null || tipoString.isBlank()) return;

            if (!nomeTextField.canSubmit()) {
                new ErrorMessage("Il nome inserito non è valido (stringa compresa tra 1 e 45 caratteri)!");
                return;
            }
            String nome = nomeTextField.getContent();

            if (!cognomeTextField.canSubmit()) {
                new ErrorMessage("Il cognome inserito non è valido (stringa compresa tra 1 e 45 caratteri)!");
                return;
            }
            String cognome = cognomeTextField.getContent();

            if (!dataDiNascitaTextField.canSubmit()) {
                new ErrorMessage("La data inserita non è valida!");
                return;
            }
            LocalDate dataDiNascita = dataDiNascitaTextField.getContent();
            TipoPersona tipo = TipoPersona.fromString((String) tipoComboBox.getSelectedItem());

            if (tipo != TipoPersona.CLIENTE && !additionalPersonaPanelTextField.canSubmit()) {
                if (tipo == TipoPersona.ARTISTA) new ErrorMessage("Il numero di premi inserito non è valido!");
                else new ErrorMessage("La matricola inserita non è valida!");
                return;
            }
            Integer additionalInfo = tipo != TipoPersona.CLIENTE ? additionalPersonaPanelTextField.getContent() : null;

            Persona persona = new Persona(-1, tipo, nome, cognome, dataDiNascita, additionalInfo);

            try {
                int codiceID = personeTable.insert(persona);
                persona.setCodiceID(codiceID);
                personeTable.addRow(persona);
            } catch (SQLException ex) {
                new ErrorMessage(ex.getMessage());
            }
        });

        /* Event 6: on delete button press, delete in database and in table */
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
