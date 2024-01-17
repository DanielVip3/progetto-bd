package it.unisa.bd.progetto.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Map;

public class DatabaseTable extends JTable {
    private Map<String, String> columnsFields; // pair <tableColumnName, dbField>

    protected void initialize(Map<String, String> columnNames) {
        columnsFields = columnNames;

        setAutoCreateRowSorter(true);
        getTableHeader().setReorderingAllowed(false);

        DefaultTableModel tableModel = new DefaultTableModel(columnsFields.keySet().toArray(), 0);
        setModel(tableModel);
    }

    public String getDatabaseFieldFromColumnName(String columnName) {
        return columnsFields.get(columnName);
    }

    public String getDatabaseFieldFromColumn(int index) {
        String key = (String) columnsFields.keySet().toArray()[index];
        return getDatabaseFieldFromColumnName(key);
    }

    public int getPrimaryKeyForRow(int rowIndex) {
        return Integer.parseInt((String) getValueAt(rowIndex, 0));
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col != 0;
    }
}
