-- -----------------------------------------------------
-- Schema progetto
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS progetto DEFAULT CHARACTER SET utf8;
USE progetto;

-- -----------------------------------------------------
-- Tabella Cinema
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Cinema (
  Codice INT UNSIGNED NOT NULL,
  Nome VARCHAR(45) NOT NULL,
  CAP INT UNSIGNED NOT NULL,
  Via VARCHAR(45) NOT NULL,
  Civico VARCHAR(10) NOT NULL,
  Telefono VARCHAR(12) NULL,
  PRIMARY KEY (Codice)
);


-- -----------------------------------------------------
-- Tabella Sala
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Sala (
  Cinema INT UNSIGNED NOT NULL,
  Nome VARCHAR(45) NOT NULL,
  NumeroPosti INT NOT NULL,
  PRIMARY KEY (Cinema, Nome),
  FOREIGN KEY (Cinema)
    REFERENCES Cinema (Codice)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


-- -----------------------------------------------------
-- Tabella Persona
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Persona (
  CodiceID INT UNSIGNED NOT NULL AUTO_INCREMENT,
  Tipo ENUM("Cliente", "Artista", "Impiegato") NOT NULL,
  Nome VARCHAR(45) NOT NULL,
  Cognome VARCHAR(45) NOT NULL,
  DataDiNascita DATETIME NOT NULL,
  NumeroPremiVinti INT UNSIGNED NULL,
  Matricola INT UNSIGNED NULL,
  PRIMARY KEY (CodiceID),
  UNIQUE INDEX Matricola_UNIQUE (Matricola ASC) VISIBLE
);


-- -----------------------------------------------------
-- Tabella Film
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Film (
  Codice INT UNSIGNED NOT NULL,
  Titolo VARCHAR(45) NOT NULL,
  Anno SMALLINT NOT NULL,
  Durata INT NOT NULL,
  EtàMinima TINYINT(18) UNSIGNED NOT NULL,
  Regista INT UNSIGNED NULL,
  PRIMARY KEY (Codice),
  FOREIGN KEY (Regista)
    REFERENCES Persona (CodiceID)
    ON DELETE SET NULL
    ON UPDATE CASCADE
);


-- -----------------------------------------------------
-- Tabella Genere
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Genere (
  Nome VARCHAR(45) NOT NULL,
  PRIMARY KEY (Nome)
);


-- -----------------------------------------------------
-- Tabella GeneriFilm
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS GeneriFilm (
  Film INT UNSIGNED NOT NULL,
  Genere VARCHAR(45) NOT NULL,
  PRIMARY KEY (Film, Genere),
  FOREIGN KEY (Film)
    REFERENCES Film (Codice)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (Genere)
    REFERENCES Genere (Nome)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


-- -----------------------------------------------------
-- Tabella Proiezione
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Proiezione (
  Cinema INT UNSIGNED NOT NULL,
  Sala VARCHAR(45) NOT NULL,
  Film INT UNSIGNED NOT NULL,
  Data DATETIME NOT NULL,
  PrezzoStandard DOUBLE UNSIGNED NOT NULL,
  PRIMARY KEY (Cinema, Sala, Film, Data),
  FOREIGN KEY (Cinema , Sala)
    REFERENCES Sala (Cinema , Nome)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (Film)
    REFERENCES Film (Codice)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


-- -----------------------------------------------------
-- Tabella Interpretazione
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Interpretazione (
  Film INT UNSIGNED NOT NULL,
  Attore INT UNSIGNED NOT NULL,
  Ruolo VARCHAR(45) NOT NULL,
  PRIMARY KEY (Film, Attore, Ruolo),
  FOREIGN KEY (Film)
    REFERENCES Film (Codice)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (Attore)
    REFERENCES Persona (CodiceID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


-- -----------------------------------------------------
-- Tabella Biglietto
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Biglietto (
  Cinema INT UNSIGNED NOT NULL,
  Sala VARCHAR(45) NOT NULL,
  Film INT UNSIGNED NOT NULL,
  Data DATETIME NOT NULL,
  Persona INT UNSIGNED NOT NULL,
  Posto VARCHAR(10) NOT NULL,
  Prezzo DOUBLE UNSIGNED NOT NULL,
  PRIMARY KEY (Cinema, Sala, Film, Persona, Data),
  FOREIGN KEY (Cinema , Sala , Film , Data)
    REFERENCES Proiezione (Cinema , Sala , Film , Data)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (Persona)
    REFERENCES Persona (CodiceID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


-- -----------------------------------------------------
-- Tabella Impieghi
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Impieghi (
  Persona INT UNSIGNED NOT NULL,
  Cinema INT UNSIGNED NOT NULL,
  Impiego VARCHAR(45) NOT NULL,
  PRIMARY KEY (Persona, Cinema),
  FOREIGN KEY (Persona)
    REFERENCES Persona (Matricola)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (Cinema)
    REFERENCES Cinema (Codice)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

START TRANSACTION;
USE progetto;

-- -----------------------------------------------------
-- Popolamento della tabella Cinema
-- -----------------------------------------------------
INSERT INTO Cinema (Codice, Nome, CAP, Via, Civico, Telefono) VALUES (362, "The Space Salerno", 84131, "Viale Antonio Bandiera", "S.N.C.", "0893051821");
INSERT INTO Cinema (Codice, Nome, CAP, Via, Civico, Telefono) VALUES (215, "The Space Roma Parco De' Medici", 00148, "Via Salvatore Rebecchini", "3", NULL);
INSERT INTO Cinema (Codice, Nome, CAP, Via, Civico, Telefono) VALUES (187, "The Space Moderno Roma", 00184 , "Piazza della Repubblica", "43/45", NULL);
INSERT INTO Cinema (Codice, Nome, CAP, Via, Civico, Telefono) VALUES (624, "Cinema Apollo", 84125, "Via Michele Vernieri", "26", "0892149539");


-- -----------------------------------------------------
-- Popolamento della tabella Sala
-- -----------------------------------------------------
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (362, "Sala 1", 100);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (362, "Sala 2", 100);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (362, "Sala 3", 100);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (362, "Sala 4", 100);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (362, "Sala 5", 100);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (362, "Sala 6", 100);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (362, "Sala 7", 100);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (362, "Sala 8", 100);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (362, "Sala 9", 100);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (362, "Sala 10", 100);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (362, "Sala 11", 100);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (187, "Sala A", 191);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (187, "Sala B", 191);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (187, "Sala C", 191);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (187, "Sala D", 191);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (187, "Sala A+", 382);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (215, "Sala 1", 500);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (215, "Sala 2", 400);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (215, "Sala 3", 400);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (215, "Sala 4", 300);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (215, "Sala 5", 300);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (215, "Sala 6", 200);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (215, "Sala 7", 200);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (215, "Sala 8", 100);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (215, "Sala 9", 100);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (215, "Sala 10", 50);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (624, "Sala Enea", 87);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (624, "Sala Achille", 63);
INSERT INTO Sala (Cinema, Nome, NumeroPosti) VALUES (624, "Sala Eventi", 50);


-- -----------------------------------------------------
-- Popolamento della tabella Persona
-- -----------------------------------------------------
INSERT INTO Persona (CodiceID, Tipo, Nome, Cognome, DataDiNascita, NumeroPremiVinti, Matricola) VALUES (1, "Artista", "Hayao", "Miyazaki", "1941-01-05", 82, NULL);
INSERT INTO Persona (CodiceID, Tipo, Nome, Cognome, DataDiNascita, NumeroPremiVinti, Matricola) VALUES (2, "Artista", "Federico", "Fellini", "1920-01-20", 68, NULL);
INSERT INTO Persona (CodiceID, Tipo, Nome, Cognome, DataDiNascita, NumeroPremiVinti, Matricola) VALUES (3, "Artista", "Quentin", "Tarantino", "1963-03-27", 171, NULL);
INSERT INTO Persona (CodiceID, Tipo, Nome, Cognome, DataDiNascita, NumeroPremiVinti, Matricola) VALUES (4, "Artista", "John", "Travolta", "1954-02-18", 52, NULL);
INSERT INTO Persona (CodiceID, Tipo, Nome, Cognome, DataDiNascita, NumeroPremiVinti, Matricola) VALUES (5, "Artista", "Uma", "Thurman", "1970-04-29", 29, NULL);
INSERT INTO Persona (CodiceID, Tipo, Nome, Cognome, DataDiNascita, NumeroPremiVinti, Matricola) VALUES (6, "Artista", "Samuel L.", "Jackson", "1948-12-21", 48, NULL);
INSERT INTO Persona (CodiceID, Tipo, Nome, Cognome, DataDiNascita, NumeroPremiVinti, Matricola) VALUES (7, "Artista", "Marcello", "Mastroianni", "1924-09-28", 46, NULL);
INSERT INTO Persona (CodiceID, Tipo, Nome, Cognome, DataDiNascita, NumeroPremiVinti, Matricola) VALUES (8, "Artista", "Robert Jr.", "Downey", "1965-04-04", 61, NULL);
INSERT INTO Persona (CodiceID, Tipo, Nome, Cognome, DataDiNascita, NumeroPremiVinti, Matricola) VALUES (9, "Cliente", "Daniele", "De Martino", "2004-07-07", NULL, NULL);
INSERT INTO Persona (CodiceID, Tipo, Nome, Cognome, DataDiNascita, NumeroPremiVinti, Matricola) VALUES (10, "Cliente", "Christian", "Esposito", "2004-02-25", NULL, NULL);
INSERT INTO Persona (CodiceID, Tipo, Nome, Cognome, DataDiNascita, NumeroPremiVinti, Matricola) VALUES (11, "Cliente", "Federico", "De Rosa", "2003-04-14", NULL, NULL);
INSERT INTO Persona (CodiceID, Tipo, Nome, Cognome, DataDiNascita, NumeroPremiVinti, Matricola) VALUES (12, "Cliente", "Mario", "Rossi", "1987-06-11", NULL, NULL);
INSERT INTO Persona (CodiceID, Tipo, Nome, Cognome, DataDiNascita, NumeroPremiVinti, Matricola) VALUES (13, "Impiegato", "Francesco", "Durante", "1999-07-12", NULL, 5523781);
INSERT INTO Persona (CodiceID, Tipo, Nome, Cognome, DataDiNascita, NumeroPremiVinti, Matricola) VALUES (14, "Impiegato", "Orazio", "Torre", "1992-10-14", NULL, 2145192);
INSERT INTO Persona (CodiceID, Tipo, Nome, Cognome, DataDiNascita, NumeroPremiVinti, Matricola) VALUES (15, "Impiegato", "Gianmarco", "Tocco", "1993-05-28", NULL, 1129571);
INSERT INTO Persona (CodiceID, Tipo, Nome, Cognome, DataDiNascita, NumeroPremiVinti, Matricola) VALUES (16, "Impiegato", "Giovanna", "Amato", "1970-07-21", NULL, 3458159);
INSERT INTO Persona (CodiceID, Tipo, Nome, Cognome, DataDiNascita, NumeroPremiVinti, Matricola) VALUES (17, "Artista", "Leonardo", "DiCaprio", "1974-11-11", 104, NULL);
INSERT INTO Persona (CodiceID, Tipo, Nome, Cognome, DataDiNascita, NumeroPremiVinti, Matricola) VALUES (18, "Artista", "Brad", "Pitt", "1963-12-18", 120, NULL);
INSERT INTO Persona (CodiceID, Tipo, Nome, Cognome, DataDiNascita, NumeroPremiVinti, Matricola) VALUES (19, "Artista", "Margot", "Robbie", "1990-07-02", 25, NULL);
INSERT INTO Persona (CodiceID, Tipo, Nome, Cognome, DataDiNascita, NumeroPremiVinti, Matricola) VALUES (20, "Artista", "Ryan", "Gosling", "1980-11-12", 52, NULL);
INSERT INTO Persona (CodiceID, Tipo, Nome, Cognome, DataDiNascita, NumeroPremiVinti, Matricola) VALUES (21, "Artista", "Wes", "Anderson", "1969-05-01", 80, NULL);
INSERT INTO Persona (CodiceID, Tipo, Nome, Cognome, DataDiNascita, NumeroPremiVinti, Matricola) VALUES (22, "Artista", "Benedict", "Cumberbatch", "1976-07-19", 72, NULL);
INSERT INTO Persona (CodiceID, Tipo, Nome, Cognome, DataDiNascita, NumeroPremiVinti, Matricola) VALUES (23, "Cliente", "Ettore", "Canu", "1988-02-19", NULL, NULL);
INSERT INTO Persona (CodiceID, Tipo, Nome, Cognome, DataDiNascita, NumeroPremiVinti, Matricola) VALUES (24, "Cliente", "Niccolò", "Contessa", "1986-07-14", NULL, NULL);


-- -----------------------------------------------------
-- Popolamento della tabella Film
-- -----------------------------------------------------
INSERT INTO Film (Codice, Titolo, Anno, Durata, EtàMinima, Regista) VALUES (6587046, "Il ragazzo e l'airone", 2023, 124, 0, 1);
INSERT INTO Film (Codice, Titolo, Anno, Durata, EtàMinima, Regista) VALUES (0110912, "Pulp Fiction", 1994, 154, 18, 3);
INSERT INTO Film (Codice, Titolo, Anno, Durata, EtàMinima, Regista) VALUES (0056801, "8½", 1963, 138, 0, 2);
INSERT INTO Film (Codice, Titolo, Anno, Durata, EtàMinima, Regista) VALUES (7131622, "C'era una volta... a Hollywood", 2019, 161, 0, 3);
INSERT INTO Film (Codice, Titolo, Anno, Durata, EtàMinima, Regista) VALUES (0331516, "Drive", 2011, 100, 0, NULL);
INSERT INTO Film (Codice, Titolo, Anno, Durata, EtàMinima, Regista) VALUES (0137523, "Fight Club", 1999, 139, 14, NULL);
INSERT INTO Film (Codice, Titolo, Anno, Durata, EtàMinima, Regista) VALUES (12593682, "Bullet Train", 2022, 127, 14, NULL);
INSERT INTO Film (Codice, Titolo, Anno, Durata, EtàMinima, Regista) VALUES (16968450, "La meravigliosa storia di Henry Sugar", 2023, 41, 12, 21);
INSERT INTO Film (Codice, Titolo, Anno, Durata, EtàMinima, Regista) VALUES (0096283, "Il mio vicino Totoro", 1988, 86, 0, 1);
INSERT INTO Film (Codice, Titolo, Anno, Durata, EtàMinima, Regista) VALUES (4154756, "Avengers: Infinity War", 2018, 149, 0, NULL);


-- -----------------------------------------------------
-- DPopolamento della tabella Genere
-- -----------------------------------------------------
INSERT INTO Genere (Nome) VALUES ("Commedia");
INSERT INTO Genere (Nome) VALUES ("Drammatico");
INSERT INTO Genere (Nome) VALUES ("Avventura");
INSERT INTO Genere (Nome) VALUES ("Azione");
INSERT INTO Genere (Nome) VALUES ("Fantascienza");
INSERT INTO Genere (Nome) VALUES ("Animazione");
INSERT INTO Genere (Nome) VALUES ("Thriller");
INSERT INTO Genere (Nome) VALUES ("Noir");
INSERT INTO Genere (Nome) VALUES ("Grottesco");
INSERT INTO Genere (Nome) VALUES ("Fantastico");

-- -----------------------------------------------------
-- Popolamento della tabella GeneriFilm
-- -----------------------------------------------------
INSERT INTO GeneriFilm (Film, Genere) VALUES (6587046, "Animazione");
INSERT INTO GeneriFilm (Film, Genere) VALUES (6587046, "Fantastico");
INSERT INTO GeneriFilm (Film, Genere) VALUES (0110912, "Commedia");
INSERT INTO GeneriFilm (Film, Genere) VALUES (0110912, "Grottesco");
INSERT INTO GeneriFilm (Film, Genere) VALUES (0110912, "Thriller");
INSERT INTO GeneriFilm (Film, Genere) VALUES (0110912, "Noir");
INSERT INTO GeneriFilm (Film, Genere) VALUES (0056801, "Commedia");
INSERT INTO GeneriFilm (Film, Genere) VALUES (0056801, "Drammatico");
INSERT INTO GeneriFilm (Film, Genere) VALUES (0056801, "Grottesco");
INSERT INTO GeneriFilm (Film, Genere) VALUES (0056801, "Fantastico");
INSERT INTO GeneriFilm (Film, Genere) VALUES (7131622, "Commedia");
INSERT INTO GeneriFilm (Film, Genere) VALUES (7131622, "Drammatico");
INSERT INTO GeneriFilm (Film, Genere) VALUES (0331516, "Thriller");
INSERT INTO GeneriFilm (Film, Genere) VALUES (0331516, "Drammatico");
INSERT INTO GeneriFilm (Film, Genere) VALUES (0331516, "Azione");
INSERT INTO GeneriFilm (Film, Genere) VALUES (0331516, "Noir");
INSERT INTO GeneriFilm (Film, Genere) VALUES (0137523, "Drammatico");
INSERT INTO GeneriFilm (Film, Genere) VALUES (0137523, "Thriller");
INSERT INTO GeneriFilm (Film, Genere) VALUES (0137523, "Grottesco");
INSERT INTO GeneriFilm (Film, Genere) VALUES (12593682, "Azione");
INSERT INTO GeneriFilm (Film, Genere) VALUES (12593682, "Thriller");
INSERT INTO GeneriFilm (Film, Genere) VALUES (12593682, "Commedia");
INSERT INTO GeneriFilm (Film, Genere) VALUES (16968450, "Commedia");
INSERT INTO GeneriFilm (Film, Genere) VALUES (16968450, "Drammatico");
INSERT INTO GeneriFilm (Film, Genere) VALUES (16968450, "Avventura");
INSERT INTO GeneriFilm (Film, Genere) VALUES (0096283, "Animazione");
INSERT INTO GeneriFilm (Film, Genere) VALUES (0096283, "Fantastico");
INSERT INTO GeneriFilm (Film, Genere) VALUES (4154756, "Azione");
INSERT INTO GeneriFilm (Film, Genere) VALUES (4154756, "Fantascienza");
INSERT INTO GeneriFilm (Film, Genere) VALUES (4154756, "Avventura");


-- -----------------------------------------------------
-- Popolamento della tabella Proiezione
-- -----------------------------------------------------
INSERT INTO Proiezione (Cinema, Sala, Film, Data, PrezzoStandard) VALUES (215, "Sala 3", 6587046, "2024-02-02 18:20:00", 9.80);
INSERT INTO Proiezione (Cinema, Sala, Film, Data, PrezzoStandard) VALUES (362, "Sala 7", 0110912, "2024-02-02 18:30:00", 8.40);
INSERT INTO Proiezione (Cinema, Sala, Film, Data, PrezzoStandard) VALUES (362, "Sala 10", 6587046, "2024-02-04 11:45:00", 9.80);
INSERT INTO Proiezione (Cinema, Sala, Film, Data, PrezzoStandard) VALUES (362, "Sala 10", 6587046, "2024-02-04 15:15:00", 9.80);
INSERT INTO Proiezione (Cinema, Sala, Film, Data, PrezzoStandard) VALUES (362, "Sala 10", 6587046, "2024-02-04 19:00:00", 9.80);
INSERT INTO Proiezione (Cinema, Sala, Film, Data, PrezzoStandard) VALUES (187, "Sala A", 0110912, "2024-02-01 12:00:00", 9.99);
INSERT INTO Proiezione (Cinema, Sala, Film, Data, PrezzoStandard) VALUES (187, "Sala C", 7131622, "2024-01-28 14:25:00", 9.99);
INSERT INTO Proiezione (Cinema, Sala, Film, Data, PrezzoStandard) VALUES (187, "Sala A+", 16968450, "2024-01-28 14:25:00", 9.99);
INSERT INTO Proiezione (Cinema, Sala, Film, Data, PrezzoStandard) VALUES (187, "Sala C", 7131622, "2024-02-03 17:50:00", 9.99);
INSERT INTO Proiezione (Cinema, Sala, Film, Data, PrezzoStandard) VALUES (215, "Sala 5", 7131622, "2024-02-03 14:20:00", 10.50);
INSERT INTO Proiezione (Cinema, Sala, Film, Data, PrezzoStandard) VALUES (215, "Sala 9", 4154756, "2024-02-03 10:00:00", 10.50);
INSERT INTO Proiezione (Cinema, Sala, Film, Data, PrezzoStandard) VALUES (624, "Sala Enea", 6587046, "2024-02-03 11:30:00", 6.70);
INSERT INTO Proiezione (Cinema, Sala, Film, Data, PrezzoStandard) VALUES (624, "Sala Eventi", 7131622, "2024-02-02 16:45:00", 7.10);
INSERT INTO Proiezione (Cinema, Sala, Film, Data, PrezzoStandard) VALUES (624, "Sala Enea", 16968450, "2024-02-02 09:30:00", 6.70);
INSERT INTO Proiezione (Cinema, Sala, Film, Data, PrezzoStandard) VALUES (624, "Sala Eventi", 7131622, "2024-02-02 20:30:00", 7.20);


-- -----------------------------------------------------
-- Popolamento della tabella Interpretazione
-- -----------------------------------------------------
INSERT INTO Interpretazione (Film, Attore, Ruolo) VALUES (0110912, 4, "Vincent Vega");
INSERT INTO Interpretazione (Film, Attore, Ruolo) VALUES (0110912, 5, "Jules Winnfield");
INSERT INTO Interpretazione (Film, Attore, Ruolo) VALUES (0110912, 6, "Mia Wallace");
INSERT INTO Interpretazione (Film, Attore, Ruolo) VALUES (0056801, 7, "Guido Anselmi");
INSERT INTO Interpretazione (Film, Attore, Ruolo) VALUES (7131622, 18, "Cliff Booth");
INSERT INTO Interpretazione (Film, Attore, Ruolo) VALUES (7131622, 17, "Rick Dalton");
INSERT INTO Interpretazione (Film, Attore, Ruolo) VALUES (7131622, 19, "Sharon Tate");
INSERT INTO Interpretazione (Film, Attore, Ruolo) VALUES (0331516, 20, "Il Pilota");
INSERT INTO Interpretazione (Film, Attore, Ruolo) VALUES (12593682, 18, "Ladybug");
INSERT INTO Interpretazione (Film, Attore, Ruolo) VALUES (0137523, 18, "Tyler Durden");
INSERT INTO Interpretazione (Film, Attore, Ruolo) VALUES (16968450, 22, "Henry Sugar");
INSERT INTO Interpretazione (Film, Attore, Ruolo) VALUES (4154756, 22, "Stephen Strange");
INSERT INTO Interpretazione (Film, Attore, Ruolo) VALUES (4154756, 8, "Tony Stark");
INSERT INTO Interpretazione (Film, Attore, Ruolo) VALUES (4154756, 6, "Nick Fury");
INSERT INTO Interpretazione (Film, Attore, Ruolo) VALUES (16968450, 22, "Max Engelman");


-- -----------------------------------------------------
-- Popolamento della tabella Biglietto
-- -----------------------------------------------------
INSERT INTO Biglietto (Cinema, Sala, Film, Data, Persona, Posto, Prezzo) VALUES (624, "Sala Enea", 6587046, "2024-02-03 11:30:00", 9, "D3", 9.99);
INSERT INTO Biglietto (Cinema, Sala, Film, Data, Persona, Posto, Prezzo) VALUES (624, "Sala Eventi", 7131622, "2024-02-02 16:45:00", 10, "A2", 9.99);
INSERT INTO Biglietto (Cinema, Sala, Film, Data, Persona, Posto, Prezzo) VALUES (187, "Sala C", 7131622, "2024-01-28 14:25:00", 9, "D12", 9.99);
INSERT INTO Biglietto (Cinema, Sala, Film, Data, Persona, Posto, Prezzo) VALUES (187, "Sala C", 7131622, "2024-02-03 17:50:00", 23, "C2", 9.99);
INSERT INTO Biglietto (Cinema, Sala, Film, Data, Persona, Posto, Prezzo) VALUES (215, "Sala 9", 4154756, "2024-02-03 10:00:00", 10, "35", 9.99);
INSERT INTO Biglietto (Cinema, Sala, Film, Data, Persona, Posto, Prezzo) VALUES (624, "Sala Eventi", 7131622, "2024-02-02 16:45:00", 12, "B7", 9.99);
INSERT INTO Biglietto (Cinema, Sala, Film, Data, Persona, Posto, Prezzo) VALUES (187, "Sala C", 7131622, "2024-01-28 14:25:00", 23, "E4", 10.5);
INSERT INTO Biglietto (Cinema, Sala, Film, Data, Persona, Posto, Prezzo) VALUES (187, "Sala A", 0110912, "2024-02-01 12:00:00", 24, "F10", 6.7);
INSERT INTO Biglietto (Cinema, Sala, Film, Data, Persona, Posto, Prezzo) VALUES (187, "Sala C", 7131622, "2024-02-03 17:50:00", 10, "A6", 7.1);
INSERT INTO Biglietto (Cinema, Sala, Film, Data, Persona, Posto, Prezzo) VALUES (187, "Sala C", 7131622, "2024-02-03 17:50:00", 11, "AA2", 7.1);


-- -----------------------------------------------------
-- Popolamento della tabella Impieghi
-- -----------------------------------------------------
INSERT INTO Impieghi (Persona, Cinema, Impiego) VALUES (5523781, 624, "Tecnico Manutentore");
INSERT INTO Impieghi (Persona, Cinema, Impiego) VALUES (2145192, 624, "Assistente Igienico");
INSERT INTO Impieghi (Persona, Cinema, Impiego) VALUES (1129571, 362, "Proiezionista");
INSERT INTO Impieghi (Persona, Cinema, Impiego) VALUES (3458159, 215, "Manager");
INSERT INTO Impieghi (Persona, Cinema, Impiego) VALUES (3458159, 187, "Manager");
INSERT INTO Impieghi (Persona, Cinema, Impiego) VALUES (3458159, 362, "Manager");
INSERT INTO Impieghi (Persona, Cinema, Impiego) VALUES (3458159, 624, "Manager");
INSERT INTO Impieghi (Persona, Cinema, Impiego) VALUES (5523781, 187, "Tecnico Manutentore");

COMMIT;
