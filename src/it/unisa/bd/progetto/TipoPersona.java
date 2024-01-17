package it.unisa.bd.progetto;

public enum TipoPersona {
    CLIENTE,
    ARTISTA,
    IMPIEGATO;

    public static TipoPersona fromString(String s) {
        return switch (s.toLowerCase()) {
            default -> CLIENTE;
            case "artista" -> ARTISTA;
            case "impiegato" -> IMPIEGATO;
        };
    }

    public String toString() {
        return switch (this) {
            case CLIENTE -> "Cliente";
            case ARTISTA -> "Artista";
            case IMPIEGATO -> "Impiegato";
        };
    }
}
