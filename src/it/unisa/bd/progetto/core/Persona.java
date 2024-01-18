package it.unisa.bd.progetto.core;

import it.unisa.bd.progetto.gui.tables.RowData;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Persona implements RowData {
    private int codiceID;
    private final TipoPersona tipo;
    private final String nome;
    private final String cognome;
    private final LocalDate dataDiNascita;
    private Integer numeroPremiVinti;
    private Integer matricola;

    public Persona(int codiceID, String nome, String cognome, LocalDate dataDiNascita, TipoPersona tipo, Integer additionalField) throws InvalidParameterException {
        if (codiceID < 0) throw new InvalidParameterException("Il codice inserito non è valido!");
        if (nome.isBlank() || nome.length() > 45) throw new InvalidParameterException("Il nome inserito non è valido!");
        if (cognome.isBlank() || cognome.length() > 45) throw new InvalidParameterException("Il cognome inserito non è valido!");

        this.codiceID = codiceID;
        this.tipo = tipo;
        this.nome = nome;
        this.cognome = cognome;
        this.dataDiNascita = dataDiNascita;
        this.numeroPremiVinti = null;
        this.matricola = null;

        switch (this.tipo) {
            default -> {
            }
            case ARTISTA -> {
                if (additionalField != null && additionalField < 0) throw new InvalidParameterException("Il numero di premi vinti inserito non è valido!");

                this.numeroPremiVinti = additionalField;
                this.matricola = null;
            }
            case IMPIEGATO -> {
                if (additionalField != null && additionalField < 0) throw new InvalidParameterException("La matricola inserita non è valida!");

                this.matricola = additionalField;
                this.numeroPremiVinti = null;
            }
        }
    }

    public int getCodiceID() {
        return codiceID;
    }

    public void setCodiceID(int codice) { codiceID = codice; }

    public TipoPersona getTipo() {
        return tipo;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public LocalDate getDataDiNascita() {
        return dataDiNascita;
    }

    public Integer getNumeroPremiVinti() {
        return numeroPremiVinti;
    }

    public Integer getMatricola() {
        return matricola;
    }

    public String[] toRow() {
        return new String[]{
            String.valueOf(codiceID), nome, cognome,
            dataDiNascita.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            tipo.toString(),
            numeroPremiVinti != null ? String.valueOf(numeroPremiVinti) : "-",
            matricola != null ? String.valueOf(matricola) : "-"
        };
    }

    public static Persona fromRow(String[] row) throws InvalidParameterException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        int codiceID;
        Integer additionalField = null;
        LocalDate dataDiNascita;

        try {
            codiceID = Integer.parseInt(row[0]);
        } catch (NumberFormatException ex) {
            throw new InvalidParameterException("Il codice inserito non è valido!");
        }

        TipoPersona tipo = TipoPersona.fromString(row[4]);
        switch (tipo) {
            default -> {}
            case ARTISTA -> {
                try {
                    if (!row[5].equals("-")) additionalField = Integer.parseInt(row[5]);
                } catch(NumberFormatException ex) {
                    throw new InvalidParameterException("Il numero di premi vinti inserito non è valido!");
                }
            }
            case IMPIEGATO -> {
                try {
                    if (!row[5].equals("-"))  additionalField = Integer.parseInt(row[5]);
                } catch(NumberFormatException ex) {
                    throw new InvalidParameterException("La matricola inserita non è valida!");
                }
            }
        }

        try {
            dataDiNascita = LocalDate.parse(row[3], formatter);
        } catch (DateTimeParseException ex) {
            throw new InvalidParameterException("La data di nascita inserita non è valida!");
        }

        return new Persona(codiceID, row[1], row[2], dataDiNascita, tipo, additionalField);
    }

    @Override
    public String toString() {
        return getNome() + " " + getCognome();
    }
}
