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

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private ListSelectionListener disableDeleteButtonIfNoSelection = new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent e) {
            deleteButton.setEnabled(getCurrentTable().getSelectedRow() >= 0);
        }
    };


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

    private DatabaseTable getCurrentTable() {
        int selectedPane = tabbedPane.getSelectedIndex();

        return switch (selectedPane) {
            default -> filmTable;
            case 1 -> personeTable;
        };
    }

    private void populateRegisti() {
        try {
            List<Persona> artisti = Database.getPersone(null, TipoPersona.ARTISTA);
            artisti.forEach(r -> registaComboBox.addItem(r.toString()));
        } catch(SQLException ex) {
            new ErrorMessage(ex.getMessage());
        }
    }

    private void populateCurrentSelectedPane(String search) {
        DatabaseTable currentTable = getCurrentTable();

        try {
            if (currentTable instanceof FilmTable) {
                currentTable.populate(Database.getFilms(search));
                populateRegisti();
            } else if (currentTable instanceof PersoneTable) currentTable.populate(Database.getPersone(search));
        } catch (SQLException ex) {
            new ErrorMessage(ex.getMessage());
        }
    }

    public HomeForm() {
        annoTextField.bounds(1900, Year.now().getValue() + 10, Year.now().getValue());
        durataTextField.bounds(1, 6000, 120);
        etaMinimaTextField.bounds(0, 18, 0);

        populateRegisti();

        try {
            filmTable.initialize();
            filmTable.populate(Database.getFilms());

            personeTable.initialize();
            personeTable.populate(Database.getPersone());
        } catch (SQLException ex) {
            new ErrorMessage(ex.getMessage());
        }

        tipoComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });

        searchTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = searchTextField.getText();
                populateCurrentSelectedPane(text);
            }
        });

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                searchTextField.setText("");
                populateCurrentSelectedPane(null);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatabaseTable currentTable = getCurrentTable();
                int selectedRow = currentTable.getSelectedRow();
                if (selectedRow < 0) return;

                try {
                    currentTable.delete(currentTable.getPrimaryKeyForRow(selectedRow));
                    currentTable.removeRow(selectedRow);
                } catch (SQLException ex) {
                    new ErrorMessage(ex.getMessage());
                }
            }
        });

        filmTable.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (e.getOppositeComponent() == null || !e.getOppositeComponent().equals(deleteButton)) filmTable.clearSelection();
            }
        });

        personeTable.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (e.getOppositeComponent() == null || !e.getOppositeComponent().equals(deleteButton)) personeTable.clearSelection();
            }
        });

        filmTable.getSelectionModel().addListSelectionListener(disableDeleteButtonIfNoSelection);
        personeTable.getSelectionModel().addListSelectionListener(disableDeleteButtonIfNoSelection);

        addFilmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });

        addPersonaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });
    }
}
