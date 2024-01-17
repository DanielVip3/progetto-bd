package it.unisa.bd.progetto.core;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private final static Database instance = new Database();
    private final static String uri = "jdbc:mysql://localhost:3306/progetto";
    private final static String user = "progetto";
    private final static String password = null;
    private static Connection connection;

    private Database() {
        try {
            connection = DriverManager.getConnection(uri, user, password);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static List<Film> getFilms() throws SQLException {
        return getFilms(null);
    }

    public static List<Film> getFilms(String search) throws SQLException {
        List<Film> films = new ArrayList<>();

        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM Film WHERE titolo LIKE ?;");
        statement.setString(1, search == null || search.isEmpty() ? "%" : "%" + search + "%");
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            films.add(new Film(
                rs.getInt("Codice"), rs.getString("Titolo"),
                rs.getShort("Anno"), rs.getShort("Durata"),
                rs.getShort("EtàMinima")
            ));
        }

        return films;
    }

    public static List<Persona> getPersone() throws SQLException {
        return getPersone(null, null);
    }

    public static List<Persona> getPersone(String search) throws SQLException {
        return getPersone(search, null);
    }

    public static List<Persona> getPersone(String search, TipoPersona filterType) throws SQLException {
        List<Persona> persone = new ArrayList<>();

        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM Persona WHERE CONCAT(nome, ' ', cognome) LIKE ?;");
        statement.setString(1, search == null || search.isEmpty() ? "%" : "%" + search + "%");
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            TipoPersona type = TipoPersona.fromString(rs.getString("Tipo"));
            if (filterType != null && type != filterType) continue;

            persone.add(new Persona(
                rs.getInt("CodiceID"), type,
                rs.getString("Nome"), rs.getString("Cognome"),
                LocalDate.parse(rs.getString("DataDiNascita"), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")),
                switch (type) {
                    default -> null;
                    case ARTISTA -> rs.getInt("NumeroPremiVinti");
                    case IMPIEGATO -> rs.getInt("Matricola");
                }
            ));
        }

        return persone;
    }

    public static void updateFilm(int codice, String column, String value) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("UPDATE Film SET " + column + " = ? WHERE Codice = ?;");
        statement.setString(1, value);
        statement.setInt(2, codice);
        statement.execute();
    }

    public static void updatePersona(int codiceID, String column, String value) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("UPDATE Persona SET " + column + " = ? WHERE CodiceID = ?;");
        statement.setString(1, value);
        statement.setInt(2, codiceID);
        statement.execute();
    }
}
