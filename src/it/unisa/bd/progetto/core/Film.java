package it.unisa.bd.progetto.core;

public class Film {
    private final int codice;
    private final String titolo;
    private final short anno;
    private final short durata;
    private final short etaMinima;

    public Film(int codice, String titolo, short anno, short durata, short etaMinima) {
        this.codice = codice;
        this.titolo = titolo;
        this.anno = anno;
        this.durata = durata;
        this.etaMinima = etaMinima;
    }

    public int getCodice() {
        return codice;
    }

    public String getTitolo() {
        return titolo;
    }

    public short getAnno() {
        return anno;
    }

    public short getDurata() {
        return durata;
    }

    public short getEtaMinima() {
        return etaMinima;
    }

    public String[] toRow() {
        return new String[]{String.valueOf(codice), titolo, String.valueOf(anno), String.valueOf(durata), String.valueOf(etaMinima)};
    }
}
