package it.unisa.bd.progetto.gui;

import it.unisa.bd.progetto.core.Database;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class FilmTable extends DatabaseTable {
    CellEditorListener changeNotification = new CellEditorListener() {
        public void editingCanceled(ChangeEvent e) {}
        public void editingStopped(ChangeEvent e) {
            TableCellEditor editor = (TableCellEditor) e.getSource();

            String newValue = (String) editor.getCellEditorValue();
            String databaseField = getDatabaseFieldFromColumn(getSelectedColumn());
            int codice = getPrimaryKeyForRow(getSelectedRow());

            try {
                Database.updateFilm(codice, databaseField, newValue);
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
    };

    public void initialize() {
        LinkedHashMap<String, String> columnFields = new LinkedHashMap<>();
        columnFields.put("Codice", "Codice");
        columnFields.put("Titolo", "Titolo");
        columnFields.put("Anno", "Anno");
        columnFields.put("Durata", "Durata");
        columnFields.put("Età minima", "EtàMinima");

        super.initialize(columnFields);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        TableColumnModel columnModel = getColumnModel();
        columnModel.getColumn(0).setMaxWidth(75);
        columnModel.getColumn(1).setPreferredWidth(300);
        columnModel.getColumn(2).setMaxWidth(75);
        columnModel.getColumn(2).setCellRenderer(centerRenderer);
        columnModel.getColumn(3).setMaxWidth(75);
        columnModel.getColumn(3).setCellRenderer(centerRenderer);
        columnModel.getColumn(4).setMaxWidth(75);
        columnModel.getColumn(4).setCellRenderer(centerRenderer);

        getDefaultEditor(String.class).addCellEditorListener(changeNotification);
    }
}
