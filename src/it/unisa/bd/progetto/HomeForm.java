package it.unisa.bd.progetto;

import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.LinkedHashMap;
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
    private DatabaseTable filmTable;
    private JTextField codiceIdTextField;
    private JComboBox<String> tipoComboBox;
    private JTextField dataDiNascitaTextField;
    private JTextField cognomeTextField;
    private JTextField nomeTextField;
    private DatabaseTable personeTable;
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

    private void initializeFilmTable() {
        LinkedHashMap<String, String> columnFields = new LinkedHashMap<>();
        columnFields.put("Codice", "Codice");
        columnFields.put("Titolo", "Titolo");
        columnFields.put("Anno", "Anno");
        columnFields.put("Durata", "Durata");
        columnFields.put("Età minima", "EtàMinima");

        filmTable.initialize(columnFields);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        TableColumnModel columnModel = filmTable.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(75);
        columnModel.getColumn(1).setPreferredWidth(300);
        columnModel.getColumn(2).setMaxWidth(75);
        columnModel.getColumn(2).setCellRenderer(centerRenderer);
        columnModel.getColumn(3).setMaxWidth(75);
        columnModel.getColumn(3).setCellRenderer(centerRenderer);
        columnModel.getColumn(4).setMaxWidth(75);
        columnModel.getColumn(4).setCellRenderer(centerRenderer);
    }

    private void initializePersoneTable() {
        LinkedHashMap<String, String> columnFields = new LinkedHashMap<>();
        columnFields.put("Codice ID", "CodiceID");
        columnFields.put("Tipo", "Tipo");
        columnFields.put("Nome", "Nome");
        columnFields.put("Cognome", "Cognome");
        columnFields.put("Data di nascita", "DataDiNascita");
        columnFields.put("# premi vinti", "NumeroPremiVinti");
        columnFields.put("Matricola", "Matricola");

        personeTable.initialize(columnFields);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        TableColumnModel columnModel = personeTable.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(75);
        columnModel.getColumn(1).setMaxWidth(150);
        columnModel.getColumn(2).setPreferredWidth(200);
        columnModel.getColumn(3).setPreferredWidth(200);
        columnModel.getColumn(4).setPreferredWidth(125);
        columnModel.getColumn(4).setMaxWidth(125);
        columnModel.getColumn(4).setCellRenderer(centerRenderer);
        columnModel.getColumn(5).setMaxWidth(125);
        columnModel.getColumn(5).setCellRenderer(centerRenderer);
        columnModel.getColumn(6).setMaxWidth(75);
        columnModel.getColumn(6).setCellRenderer(centerRenderer);
    }

    private void populateFilmTable(List<Film> films) {
        DefaultTableModel tableModel = (DefaultTableModel) filmTable.getModel();
        tableModel.setRowCount(0);
        films.forEach(p -> tableModel.addRow(p.toRow()));
    }

    private void populatePersoneTable(List<Persona> persone) {
        DefaultTableModel tableModel = (DefaultTableModel) personeTable.getModel();
        tableModel.setRowCount(0);
        persone.forEach(p -> tableModel.addRow(p.toRow()));
    }

    private void populateCurrentSelectedPane(String search) {
        int selectedPane = tabbedPane.getSelectedIndex();

        try {
            switch (selectedPane) {
                case 0 -> populateFilmTable(db.getFilms(search));
                case 1 -> populatePersoneTable(db.getPersone(search));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public HomeForm() throws SQLException {
        List<Persona> artisti = db.getPersone(null, TipoPersona.ARTISTA);
        artisti.forEach(r -> registaComboBox.addItem(r.toString()));

        initializeFilmTable();
        populateFilmTable(db.getFilms());

        initializePersoneTable();
        populatePersoneTable(db.getPersone());

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
