package it.unisa.bd.progetto;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Persona {
    private final int codiceID;
    private final TipoEnum tipo;
    private final String nome;
    private final String cognome;
    private final LocalDate dataDiNascita;
    private Integer numeroPremiVinti;
    private Integer matricola;

    public Persona(int codiceID, TipoEnum tipo, String nome, String cognome, LocalDate dataDiNascita) {
        this.codiceID = codiceID;
        this.tipo = tipo;
        this.nome = nome;
        this.cognome = cognome;
        this.dataDiNascita = dataDiNascita;
        this.numeroPremiVinti = null;
        this.matricola = null;
    }

    public Persona(int codiceID, TipoEnum tipo, String nome, String cognome, LocalDate dataDiNascita, Integer additionalField) {
        this.codiceID = codiceID;
        this.tipo = tipo;
        this.nome = nome;
        this.cognome = cognome;
        this.dataDiNascita = dataDiNascita;
        this.numeroPremiVinti = null;
        this.matricola = null;

        switch (this.tipo) {
            default -> {}
            case ARTISTA -> {
                this.numeroPremiVinti = additionalField;
                this.matricola = null;
            }
            case IMPIEGATO -> {
                this.matricola = additionalField;
                this.numeroPremiVinti = null;
            }
        }
    }

    public int getCodiceID() {
        return codiceID;
    }

    public TipoEnum getTipo() {
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

    public int getNumeroPremiVinti() {
        return numeroPremiVinti;
    }

    public int getMatricola() {
        return matricola;
    }

    public String[] toRow() {
        return new String[]{
            String.valueOf(codiceID), tipo.toString(), nome, cognome,
            dataDiNascita.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            numeroPremiVinti != null ? String.valueOf(numeroPremiVinti) : "",
            matricola != null ? String.valueOf(matricola) : ""
        };
    }
}
