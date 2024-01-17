package it.unisa.bd.progetto.gui;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public abstract class DatabaseTable extends JTable {
    public CellEditorListener changeNotification = new CellEditorListener() {
        public void editingCanceled(ChangeEvent e) {}
        public void editingStopped(ChangeEvent e) {
            TableCellEditor editor = (TableCellEditor) e.getSource();

            String newValue = (String) editor.getCellEditorValue();
            String databaseField = getDatabaseFieldFromColumn(getSelectedColumn());
            int primaryKey = getPrimaryKeyForRow(getSelectedRow());

            try {
                update(primaryKey, databaseField, newValue);
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
    };

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

    public void removeRow(int rowIndex) {
        ((DefaultTableModel) getModel()).removeRow(rowIndex);
    }

    public void populate(List<? extends RowData> items) {
        DefaultTableModel tableModel = (DefaultTableModel) getModel();
        tableModel.setRowCount(0);
        items.forEach(p -> tableModel.addRow(p.toRow()));
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col != 0;
    }

    public abstract void update(int primaryKey, String field, String newValue) throws SQLException;
    public abstract void delete(int primaryKey) throws SQLException;
}
