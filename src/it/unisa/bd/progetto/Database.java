package it.unisa.bd.progetto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private final static String uri = "jdbc:mysql://localhost:3306/progetto";
    private final static String user = "progetto";
    private final static String password = null;

    private Connection connection;

    public Database() {
        try {
            connection = DriverManager.getConnection(uri, user, password);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
