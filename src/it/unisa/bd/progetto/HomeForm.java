package it.unisa.bd.progetto;

import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HomeForm {
    private static final Database db = new Database();
    private static final List<Film> films = new ArrayList<>();

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

    public static void main(String[] args) {
        FlatMacLightLaf.setup();

        JFrame frame = new JFrame("Cinema");
        frame.setContentPane(new HomeForm().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void queryFilms() {
        try {
            ResultSet rs = db.getConnection().createStatement().executeQuery("SELECT * FROM film;");

            while (rs.next()) {
                films.add(new Film(
                        rs.getString("Codice"), rs.getString("Titolo"),
                        rs.getShort("Anno"), rs.getShort("Durata"),
                        rs.getShort("EtàMinima")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private List<String> queryRegistiPossibili() {
        List<String> registi = new ArrayList<>();

        try {
            ResultSet rs = db.getConnection().createStatement().executeQuery("SELECT Nome, Cognome FROM Persona WHERE Tipo = 'Artista';");

            while (rs.next()) registi.add(rs.getString("nome") + " " + rs.getString("cognome"));
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        return registi;
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

    private void initializeRegisti() {
        List<String> registiPossibili = queryRegistiPossibili();
        for (String regista : registiPossibili) registaComboBox.addItem(regista);
    }

    public HomeForm() {
        initializeRegisti();

        queryFilms();
        initializeFilmTable();

        films.forEach(f -> ((DefaultTableModel) filmTable.getModel()).addRow(f.toRow()));

        tipoComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) tipoComboBox.getSelectedItem();
                if (selectedItem == null || selectedItem.isBlank() || selectedItem.isEmpty()) return;

                TipoEnum type = TipoEnum.fromString((String) tipoComboBox.getSelectedItem());

                switch (type) {
                    default -> additionalPersonaPanel.setVisible(false);
                    case ARTISTA -> {
                        additionalPersonaPanelTextField.setText("Numero premi vinti");
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
