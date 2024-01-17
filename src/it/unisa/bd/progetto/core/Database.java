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

        PreparedStatement statement = getConnection().prepareStatement(
        "SELECT Film.*, CONCAT(Nome, ' ', Cognome) AS NomeRegista " +
            "FROM Film LEFT JOIN Persona ON Film.Regista = Persona.CodiceID " +
            "WHERE titolo LIKE ?;"
        );
        statement.setString(1, search == null || search.isEmpty() ? "%" : "%" + search + "%");
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            films.add(new Film(
                rs.getInt("Codice"), rs.getString("Titolo"),
                rs.getShort("Anno"), rs.getShort("Durata"),
                rs.getShort("EtàMinima"), rs.getString("NomeRegista")
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

    public static void insertFilm(Film film) throws SQLException {
        PreparedStatement statement;
        Integer codiceRegista = null;

        if (!film.getRegista().equals("Sconosciuto")) {
            statement = getConnection().prepareStatement("SELECT CodiceID From Persona WHERE CONCAT(Nome, ' ', Cognome) = ?");
            statement.setString(1, film.getRegista());
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                codiceRegista = rs.getInt("CodiceID");
            } else {
                throw new RuntimeException("Il regista cercato non esiste.");
            }
        }

        statement = getConnection().prepareStatement(
        "INSERT INTO Film (Codice, Titolo, Durata, Anno, EtàMinima, Regista) " +
            "VALUES (?, ?, ?, ?, ?, ?);"
        );

        statement.setInt(1, film.getCodice());
        statement.setString(2, film.getTitolo());
        statement.setShort(3, film.getDurata());
        statement.setShort(4, film.getAnno());
        statement.setShort(5, film.getEtaMinima());

        if (codiceRegista != null) statement.setInt(6, codiceRegista);
        else statement.setNull(6, java.sql.Types.NULL);

        statement.execute();
    }

    public static int insertPersona(Persona persona) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement(
        "INSERT INTO Persona (Tipo, Nome, Cognome, DataDiNascita, NumeroPremiVinti, Matricola) " +
            "VALUES (?, ?, ?, ?, ?, ?);",
            Statement.RETURN_GENERATED_KEYS
        );

        statement.setString(1, persona.getTipo().toString());
        statement.setString(2, persona.getNome());
        statement.setString(3, persona.getCognome());
        statement.setDate(4, Date.valueOf(persona.getDataDiNascita()));

        if (persona.getTipo() == TipoPersona.ARTISTA) statement.setInt(5, persona.getNumeroPremiVinti());
        else statement.setNull(5, java.sql.Types.NULL);

        if (persona.getTipo() == TipoPersona.IMPIEGATO) statement.setInt(6, persona.getMatricola());
        else statement.setNull(6, java.sql.Types.NULL);

        statement.execute();

        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) return rs.getInt(1);
        else return -1;
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

    public static void deleteFilm(int codice) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("DELETE FROM Film WHERE Codice = ?;");
        statement.setInt(1, codice);
        statement.execute();
    }

    public static void deletePersona(int codiceID) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("DELETE FROM Persona WHERE CodiceID = ?;");
        statement.setInt(1, codiceID);
        statement.execute();
    }
}
