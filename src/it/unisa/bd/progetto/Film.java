package it.unisa.bd.progetto;

public class Film {
    private final String codice;
    private final String titolo;
    private final short anno;
    private final short durata;
    private final short etaMinima;

    public Film(String codice, String titolo, short anno, short durata, short etaMinima) {
        this.codice = codice;
        this.titolo = titolo;
        this.anno = anno;
        this.durata = durata;
        this.etaMinima = etaMinima;
    }

    public String getCodice() {
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
        return new String[]{codice, titolo, String.valueOf(anno), String.valueOf(durata), String.valueOf(etaMinima)};
    }
}
