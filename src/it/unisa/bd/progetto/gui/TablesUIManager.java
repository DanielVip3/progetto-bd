package it.unisa.bd.progetto.gui;

import it.unisa.bd.progetto.core.Database;
import it.unisa.bd.progetto.core.Persona;
import it.unisa.bd.progetto.core.TipoPersona;
import it.unisa.bd.progetto.gui.errors.ErrorMessage;
import it.unisa.bd.progetto.gui.tables.DatabaseTable;
import it.unisa.bd.progetto.gui.tables.FilmTable;
import it.unisa.bd.progetto.gui.tables.PersoneTable;
import it.unisa.bd.progetto.gui.tables.RowData;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;
import java.util.List;

public class TablesUIManager {
    private final JTabbedPane tabbedPane;
    private final DatabaseTable<? extends RowData>[] tables;
    private final JButton deleteButton;
    private final JComboBox<String> registiComboBox;

    public TablesUIManager(JTabbedPane tabbedPane, DatabaseTable<? extends RowData>[] tables, JButton deleteButton, JComboBox<String> registiComboBox) {
        this.tabbedPane = tabbedPane;
        this.tables = tables;
        this.deleteButton = deleteButton;
        this.registiComboBox = registiComboBox;



        /* Event 1: disable the delete button if there is no selected item in the table */
        ListSelectionListener disableDeleteButtonIfNoSelection = e -> deleteButton.setEnabled(getCurrentTable().getSelectedRow() >= 0);

        /* Event 2: when a table loses focus, we clear the selection, except if it's being given to table itself or delete button */
        FocusAdapter clearSelectionOnLostFocus = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                Component clicked = e.getOppositeComponent();
                if (clicked != null && (clicked.equals(deleteButton) || clicked.getParent() instanceof JTable)) return;

                ((JTable) e.getComponent()).clearSelection();
            }
        };

        /* Event 3: on tab change, populate the table with fresh data and combobox too (also resetting searches) */
        tabbedPane.addChangeListener(e -> {
            populateTable(getCurrentTable(), null);
            populateRegisti();
        });

        for (DatabaseTable<? extends RowData> table : tables) {
            /* Setting up events for all tables */
            table.addFocusListener(clearSelectionOnLostFocus);
            table.getSelectionModel().addListSelectionListener(disableDeleteButtonIfNoSelection);

            /* Initializing all tables and populating them initially */
            table.initialize();
            populateTable(getCurrentTable(), null);
        }

        /* Populating combobox initially */
        populateRegisti();
    }

    public void populateRegisti() {
        try {
            Database.getPersone(null, TipoPersona.ARTISTA).stream().map(Persona::toString).toList().forEach(registiComboBox::addItem);
        } catch (SQLException ex) {
            new ErrorMessage(ex.getMessage());
        }
    }

    public void populateTable(DatabaseTable<? extends RowData> table, String search) {
        try {
            if (table instanceof FilmTable) table.populate(Database.getFilms(search));
            else if (table instanceof PersoneTable) table.populate(Database.getPersone(search));
        } catch (SQLException ex) {
            new ErrorMessage(ex.getMessage());
        }
    }

    public DatabaseTable<? extends RowData> getCurrentTable() {
        int selectedPane = tabbedPane.getSelectedIndex();

        return tables[selectedPane];
    }
}
