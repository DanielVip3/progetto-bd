package it.unisa.bd.progetto.gui.tables;

import javax.swing.table.DefaultTableModel;

public class ValidatedTableModel extends DefaultTableModel {
    private Object lastValue;

    public ValidatedTableModel(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        lastValue = getValueAt(row, column);

        super.setValueAt(aValue, row, column);
    }

    public Object getLastValue() {
        return lastValue;
    }
}
