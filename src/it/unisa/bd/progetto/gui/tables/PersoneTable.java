package it.unisa.bd.progetto.gui.tables;

import it.unisa.bd.progetto.core.Database;
import it.unisa.bd.progetto.core.Persona;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class PersoneTable extends DatabaseTable<Persona> {
    public void initialize() {
        LinkedHashMap<String, String> columnFields = new LinkedHashMap<>();
        columnFields.put("Codice ID", "CodiceID");
        columnFields.put("Nome", "Nome");
        columnFields.put("Cognome", "Cognome");
        columnFields.put("Data di nascita", "DataDiNascita");
        columnFields.put("Tipo", "Tipo");
        columnFields.put("# premi vinti", "NumeroPremiVinti");
        columnFields.put("Matricola", "Matricola");

        super.initialize(columnFields);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        TableColumnModel columnModel = getColumnModel();
        columnModel.getColumn(0).setMaxWidth(75);
        columnModel.getColumn(1).setPreferredWidth(200);
        columnModel.getColumn(2).setPreferredWidth(200);
        columnModel.getColumn(3).setPreferredWidth(125);
        columnModel.getColumn(3).setMaxWidth(125);
        columnModel.getColumn(3).setCellRenderer(centerRenderer);
        columnModel.getColumn(4).setMaxWidth(150);
        columnModel.getColumn(4).setCellRenderer(centerRenderer);
        columnModel.getColumn(5).setMaxWidth(125);
        columnModel.getColumn(5).setCellRenderer(centerRenderer);
        columnModel.getColumn(6).setMaxWidth(75);
        columnModel.getColumn(6).setCellRenderer(centerRenderer);
    }

    public int insert(Persona persona) throws SQLException {
        return Database.insertPersona(persona);
    }

    public int update(Persona persona) throws SQLException, InvalidParameterException {
        return Database.updatePersona(persona);
    }

    public int delete(int primaryKey) throws SQLException {
        return Database.deletePersona(primaryKey);
    }
}
