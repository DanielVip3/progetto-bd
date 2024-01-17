package it.unisa.bd.progetto;

import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class HomeForm {
    private static final Database db = new Database();

    private JTabbedPane tabbedPane1;
    private JPanel root;
    private JTextField codiceTextField;
    private JTextField titoloTextField;
    private JTextField annoTextField;
    private JTextField durataTextField;
    private JTextField etaMinimaTextField;
    private JComboBox<String> registaComboBox;
    private JTable filmTable;
    private JTextField codiceIdTextField;
    private JComboBox<String> tipoComboBox;
    private JTextField dataDiNascitaTextField;
    private JTextField cognomeTextField;
    private JTextField nomeTextField;
    private JTable personeTable;
    private JPanel additionalPersonaPanel;
    private JLabel additionalPersonaPanelTextField;

    public static void main(String[] args) throws SQLException {
        FlatMacLightLaf.setup();

        JFrame frame = new JFrame("Cinema");
        frame.setContentPane(new HomeForm().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void initializeFilmTable() {
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Codice", "Titolo", "Anno", "Durata", "Età minima"}, 0);
        filmTable.setModel(tableModel);

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
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Codice ID", "Tipo", "Nome", "Cognome", "Data di nascita", "# premi vinti", "Matricola"}, 0);
        personeTable.setModel(tableModel);

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
        for (int i = 0; i < tableModel.getRowCount(); i++) tableModel.removeRow(i);
        films.forEach(p -> tableModel.addRow(p.toRow()));
    }

    private void populatePersoneTable(List<Persona> persone) {
        DefaultTableModel tableModel = (DefaultTableModel) personeTable.getModel();
        for (int i = 0; i < tableModel.getRowCount(); i++) tableModel.removeRow(i);
        persone.forEach(p -> tableModel.addRow(p.toRow()));
    }

    public HomeForm() throws SQLException {
        List<Persona> artisti = db.getPersone(TipoPersona.ARTISTA);
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
    }
}
