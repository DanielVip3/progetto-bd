package it.unisa.bd.progetto.gui;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import it.unisa.bd.progetto.core.Database;
import it.unisa.bd.progetto.core.Persona;
import it.unisa.bd.progetto.core.TipoPersona;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class HomeForm {
    private static final Database db = new Database();

    private JTabbedPane tabbedPane;
    private JPanel root;
    private JTextField codiceTextField;
    private JTextField titoloTextField;
    private JTextField annoTextField;
    private JTextField durataTextField;
    private JTextField etaMinimaTextField;
    private JComboBox<String> registaComboBox;
    private FilmTable filmTable;
    private JTextField codiceIdTextField;
    private JComboBox<String> tipoComboBox;
    private JTextField dataDiNascitaTextField;
    private JTextField cognomeTextField;
    private JTextField nomeTextField;
    private PersoneTable personeTable;
    private JPanel additionalPersonaPanel;
    private JLabel additionalPersonaPanelTextField;
    private JTextField searchTextField;
    private JLabel searchLabel;

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

    private void populateCurrentSelectedPane(String search) {
        int selectedPane = tabbedPane.getSelectedIndex();

        try {
            switch (selectedPane) {
                case 0 -> filmTable.populate(db.getFilms(search));
                case 1 -> personeTable.populate(db.getPersone(search));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public HomeForm() throws SQLException {
        List<Persona> artisti = db.getPersone(null, TipoPersona.ARTISTA);
        artisti.forEach(r -> registaComboBox.addItem(r.toString()));

        filmTable.initialize();
        filmTable.populate(db.getFilms());

        personeTable.initialize();
        personeTable.populate(db.getPersone());

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

        CellEditorListener filmsChangeNotification = new CellEditorListener() {
            public void editingCanceled(ChangeEvent e) {}
            public void editingStopped(ChangeEvent e) {
                TableCellEditor editor = (TableCellEditor) e.getSource();

                String newValue = (String) editor.getCellEditorValue();
                String databaseField = filmTable.getDatabaseFieldFromColumn(filmTable.getSelectedColumn());
                int codice = filmTable.getPrimaryKeyForRow(filmTable.getSelectedRow());

                try {
                    db.updateFilm(codice, databaseField, newValue);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
        };

        filmTable.getDefaultEditor(String.class).addCellEditorListener(filmsChangeNotification);

        CellEditorListener personeChangeNotification = new CellEditorListener() {
            public void editingCanceled(ChangeEvent e) {}
            public void editingStopped(ChangeEvent e) {
                TableCellEditor editor = (TableCellEditor) e.getSource();

                String newValue = (String) editor.getCellEditorValue();
                String databaseField = personeTable.getDatabaseFieldFromColumn(personeTable.getSelectedColumn());
                int codiceID = personeTable.getPrimaryKeyForRow(personeTable.getSelectedRow());

                try {
                    db.updatePersona(codiceID, databaseField, newValue);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
        };

        personeTable.getDefaultEditor(String.class).addCellEditorListener(personeChangeNotification);
    }
}
