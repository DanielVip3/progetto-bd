package it.unisa.bd.progetto.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.util.LinkedHashMap;
import java.util.Map;

public class FilmTable extends DatabaseTable {
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
    }
}
