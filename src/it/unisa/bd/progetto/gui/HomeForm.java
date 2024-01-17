package it.unisa.bd.progetto.gui;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import it.unisa.bd.progetto.core.Database;
import it.unisa.bd.progetto.core.Persona;
import it.unisa.bd.progetto.core.TipoPersona;

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
import java.util.List;

public class HomeForm {
    private JTabbedPane tabbedPane;
    private JPanel root;
    private JTextField codiceTextField;
    private JTextField titoloTextField;
    private JTextField annoTextField;
    private JTextField durataTextField;
    private JTextField etaMinimaTextField;
    private JComboBox<String> registaComboBox;
    private JTextField codiceIdTextField;
    private JComboBox<String> tipoComboBox;
    private JTextField dataDiNascitaTextField;
    private JTextField cognomeTextField;
    private JTextField nomeTextField;
    private JPanel additionalPersonaPanel;
    private JLabel additionalPersonaPanelTextField;
    private JTextField searchTextField;
    private JLabel searchLabel;
    private FilmTable filmTable;
    private PersoneTable personeTable;
    private JButton deleteButton;

    private ListSelectionListener disableDeleteButtonIfNoSelection = new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent e) {
            deleteButton.setEnabled(getCurrentTable().getSelectedRow() >= 0);
        }
    };


    public static void main(String[] args) throws SQLException {
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

    private void populateCurrentSelectedPane(String search) {
        DatabaseTable currentTable = getCurrentTable();

        try {
            if (currentTable instanceof FilmTable) currentTable.populate(Database.getFilms(search));
            else if (currentTable instanceof PersoneTable) currentTable.populate(Database.getPersone(search));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public HomeForm() throws SQLException {
        List<Persona> artisti = Database.getPersone(null, TipoPersona.ARTISTA);
        artisti.forEach(r -> registaComboBox.addItem(r.toString()));

        filmTable.initialize();
        filmTable.populate(Database.getFilms());

        personeTable.initialize();
        personeTable.populate(Database.getPersone());

        tipoComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) tipoComboBox.getSelectedItem();
                if (selectedItem == null || selectedItem.isBlank() || selectedItem.isEmpty()) return;

                TipoPersona type = TipoPersona.fromString((String) tipoComboBox.getSelectedItem());

                switch (type) {
                    default -> additionalPersonaPanel.setVisible(false);
                    case ARTISTA -> {
                        additionalPersonaPanelTextField.setText("# premi vinti");
                        additionalPersonaPanel.setVisible(true);
                    }
                    case IMPIEGATO -> {
                        additionalPersonaPanelTextField.setText("Matricola");
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
                    ex.printStackTrace();
                }
            }
        });

        filmTable.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                filmTable.clearSelection();
            }
        });

        personeTable.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                personeTable.clearSelection();
            }
        });

        filmTable.getSelectionModel().addListSelectionListener(disableDeleteButtonIfNoSelection);
        personeTable.getSelectionModel().addListSelectionListener(disableDeleteButtonIfNoSelection);
    }
}
