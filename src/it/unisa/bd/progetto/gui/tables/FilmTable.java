package it.unisa.bd.progetto.gui.tables;

import it.unisa.bd.progetto.core.Database;
import it.unisa.bd.progetto.core.Film;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class FilmTable extends DatabaseTable {
    public void initialize() {
        LinkedHashMap<String, String> columnFields = new LinkedHashMap<>();
        columnFields.put("Codice", "Codice");
        columnFields.put("Titolo", "Titolo");
        columnFields.put("Anno", "Anno");
        columnFields.put("Durata", "Durata");
        columnFields.put("Età minima", "EtàMinima");
        columnFields.put("Nome regista", "NomeRegista");

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
    }

    public int insert(RowData film) throws SQLException {
        Database.insertFilm((Film) film);
        return 0;
    }

    public void update(RowData film) throws SQLException, InvalidParameterException {
        Database.updateFilm((Film) film);
    }

    public void delete(int primaryKey) throws SQLException {
        Database.deleteFilm(primaryKey);
    }
}
