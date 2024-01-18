package it.unisa.bd.progetto.core;

import it.unisa.bd.progetto.gui.tables.RowData;

public class Film implements RowData {
    private final int codice;
    private final String titolo;
    private final int anno;
    private final int durata;
    private final int etaMinima;
    private final String regista;


    public Film(int codice, String titolo, int anno, int durata, int etaMinima, String regista) {
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
}
