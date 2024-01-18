package it.unisa.bd.progetto.core;

import it.unisa.bd.progetto.gui.tables.RowData;

import java.security.InvalidParameterException;
import java.time.Year;

public class Film implements RowData {
    private final int codice;
    private final String titolo;
    private final int anno;
    private final int durata;
    private final int etaMinima;
    private final String regista;

    public Film(int codice, String titolo, int anno, int durata, int etaMinima, String regista) throws InvalidParameterException {
        if (codice < 0) throw new InvalidParameterException("Il codice inserito non è valido!");
        if (titolo.isBlank() || titolo.length() > 45) throw new InvalidParameterException("Il titolo inserito non è valido!");
        if (anno < 1900 || anno > Year.now().getValue() + 10) throw new InvalidParameterException("L'anno inserito non è valido!");
        if (durata < 1 || durata > 6000) throw new InvalidParameterException("La durata inserita non è valida!");
        if (etaMinima < 0 || etaMinima > 18) throw new InvalidParameterException("L'età minima inserita non è valida!");

        this.codice = codice;
        this.titolo = titolo;
        this.anno = anno;
        this.durata = durata;
        this.etaMinima = etaMinima;
        this.regista = regista != null && !regista.isBlank() ? regista : "Sconosciuto";
    }

    public int getCodice() {
        return codice;
    }

    public String getTitolo() {
        return titolo;
    }

    public int getAnno() {
        return anno;
    }

    public int getDurata() {
        return durata;
    }

    public int getEtaMinima() {
        return etaMinima;
    }

    public String getRegista() { return regista; }

    public String[] toRow() {
        return new String[]{String.valueOf(codice), titolo, String.valueOf(anno), String.valueOf(durata), String.valueOf(etaMinima), regista};
    }

    public static Film fromRow(String[] row) throws InvalidParameterException {
        int codice, anno, durata, etaMinima;

        try {
            codice = Integer.parseInt(row[0]);
        } catch (NumberFormatException ex) {
            throw new InvalidParameterException("Il codice inserito non è valido!");
        }

        try {
            anno = Integer.parseInt(row[2]);
        } catch (NumberFormatException ex) {
            throw new InvalidParameterException("L'anno inserito non è valido!");
        }

        try {
            durata = Integer.parseInt(row[3]);
        } catch (NumberFormatException ex) {
            throw new InvalidParameterException("La durata inserita non è valida!");
        }

        try {
            etaMinima = Integer.parseInt(row[4]);
        } catch (NumberFormatException ex) {
            throw new InvalidParameterException("L'età minima inserita non è valida!");
        }

        return new Film(codice, row[1], anno, durata, etaMinima, row[5]);
    }
}
