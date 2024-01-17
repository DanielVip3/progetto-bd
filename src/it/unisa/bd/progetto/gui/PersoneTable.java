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

public class PersoneTable extends DatabaseTable {
    public void initialize() {
        LinkedHashMap<String, String> columnFields = new LinkedHashMap<>();
        columnFields.put("Codice ID", "CodiceID");
        columnFields.put("Tipo", "Tipo");
        columnFields.put("Nome", "Nome");
        columnFields.put("Cognome", "Cognome");
        columnFields.put("Data di nascita", "DataDiNascita");
        columnFields.put("# premi vinti", "NumeroPremiVinti");
        columnFields.put("Matricola", "Matricola");

        super.initialize(columnFields);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        TableColumnModel columnModel = getColumnModel();
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

        getDefaultEditor(String.class).addCellEditorListener(changeNotification);
    }

    public void update(int primaryKey, String field, String newValue) throws SQLException {
        Database.updatePersona(primaryKey, field, newValue);
    }

    public void delete(int primaryKey) throws SQLException {
        Database.deletePersona(primaryKey);
    }
}
