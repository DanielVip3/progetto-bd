package it.unisa.bd.progetto.gui.tables;

import javax.swing.*;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public abstract class DatabaseTable<T extends RowData> extends JTable {
    private Map<String, String> columnsFields; // pair <tableColumnName, dbField>

    protected void initialize(Map<String, String> columnNames) {
        columnsFields = columnNames;

        putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        setAutoCreateRowSorter(true);
        getTableHeader().setReorderingAllowed(false);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ValidatedTableModel tableModel = new ValidatedTableModel(columnsFields.keySet().toArray(), 0);
        setModel(tableModel);
    }

    public abstract void initialize();

    public String getDatabaseFieldFromColumnName(String columnName) {
        return columnsFields.get(columnName);
    }

    public String getDatabaseFieldFromColumn(int index) {
        String key = (String) columnsFields.keySet().toArray()[index];
        return getDatabaseFieldFromColumnName(key);
    }

    /* This works only if there is a single primary key and it's the first column; we can safely assume it for our uses */
    public int getPrimaryKeyForRow(int rowIndex) {
        return Integer.parseInt((String) getValueAt(rowIndex, 0));
    }

    public void addRow(RowData item) {
        ((ValidatedTableModel) getModel()).addRow(item.toRow());
    }

    public void removeRow(int rowIndex) {
        ((ValidatedTableModel) getModel()).removeRow(rowIndex);
    }

    public void resetLastEdit(int row, int column) {
        setValueAt(((ValidatedTableModel) getModel()).getLastValue(), row, column);
    }

    public void populate(List<? extends RowData> items) {
        ValidatedTableModel tableModel = (ValidatedTableModel) getModel();
        tableModel.setRowCount(0);
        items.forEach(this::addRow);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col != 0;
    }

    public abstract int insert(T data) throws SQLException;
    public abstract void update(T data) throws SQLException, InvalidParameterException;
    public abstract void delete(int primaryKey) throws SQLException;
}
