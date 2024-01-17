package it.unisa.bd.progetto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

    public List<Film> getFilms() throws SQLException {
        List<Film> films = new ArrayList<>();

        ResultSet rs = getConnection().createStatement().executeQuery("SELECT * FROM film;");

        while (rs.next()) {
            films.add(new Film(
                rs.getString("Codice"), rs.getString("Titolo"),
                rs.getShort("Anno"), rs.getShort("Durata"),
                rs.getShort("EtàMinima")
            ));
        }

        return films;
    }

    public List<Persona> getPersone() throws SQLException {
        return getPersone(null);
    }

    public List<Persona> getPersone(TipoPersona filterType) throws SQLException {
        List<Persona> persone = new ArrayList<>();

        ResultSet rs = getConnection().createStatement().executeQuery("SELECT * FROM persona;");

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
}
