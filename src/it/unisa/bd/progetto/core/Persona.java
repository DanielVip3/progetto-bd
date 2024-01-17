package it.unisa.bd.progetto.core;

import it.unisa.bd.progetto.gui.RowData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Persona implements RowData {
    private int codiceID;
    private final TipoPersona tipo;
    private final String nome;
    private final String cognome;
    private final LocalDate dataDiNascita;
    private Integer numeroPremiVinti;
    private Integer matricola;

    public Persona(int codiceID, TipoPersona tipo, String nome, String cognome, LocalDate dataDiNascita) {
        this.codiceID = codiceID;
        this.tipo = tipo;
        this.nome = nome;
        this.cognome = cognome;
        this.dataDiNascita = dataDiNascita;
        this.numeroPremiVinti = null;
        this.matricola = null;
    }

    public Persona(int codiceID, TipoPersona tipo, String nome, String cognome, LocalDate dataDiNascita, Integer additionalField) {
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
            numeroPremiVinti != null ? String.valueOf(numeroPremiVinti) : "-",
            matricola != null ? String.valueOf(matricola) : "-"
        };
    }

    @Override
    public String toString() {
        return getNome() + " " + getCognome();
    }
}
