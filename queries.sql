-- #1 Una selezione ordinata su un attributo di una tabella con condizioni AND e OR:
-- Seleziona in ordine di durata tutti i film che non siano VM18 i quali sono anche o cortometraggi o usciti dopo il 2000
SELECT * FROM Film
    WHERE (ANNO > 2000 OR Durata < 100) AND EtàMinima <> 18
    ORDER BY Durata ASC;

-- #2 Una selezione su due o più tabelle con condizioni:
-- Seleziona tutti i film usciti prima del 2022 e per ciascuno di essi mostra il nome del regista se presente
SELECT F.Codice, F.Titolo, F.Anno, F.Durata, F.EtàMinima, CONCAT(P.Nome, " ", P.Cognome) AS Regista
    FROM Film F
    LEFT JOIN Persona P ON F.Regista = P.CodiceID
    WHERE Anno < 2022;

-- #3 Una selezione aggregata su tutti i valori (es. somma di tutti gli stipendi):
-- Mostra il numero medio di premi vinti calcolato su tutti gli artisti presenti
SELECT AVG(NumeroPremiVinti) FROM Persona;

-- #4 Una selezione aggregata su raggruppamenti (es. somma stipendi per dipartimenti):
-- Seleziona tutti i cinema e per ciascuno di essi mostra il numero totale di posti presenti
SELECT C.Nome, SUM(S.NumeroPosti) AS NumeroPosti
    FROM Cinema C
    INNER JOIN Sala S ON S.Cinema = C.Codice
    GROUP BY C.Nome;

-- #5 Una selezione aggregata su raggruppamenti con condizioni (es. dipartimenti la cui somma degli stipendi dei dipendenti è > 100k):
-- Seleziona tutte le proiezioni e per ciascuna di esse ne calcola l'incasso, poi considera solo quelle il cui incasso è > 10
-- Gli ultimi due join sono necessari solo a mostrare il corretto nome di persona, cinema e film ma non davvero utili ai fini del calcolo
SELECT C.Nome AS Cinema, Pr.Sala, F.Titolo AS Film, Pr.Data, SUM(B.Prezzo) AS Incasso
	FROM Biglietto B
	INNER JOIN Proiezione Pr ON B.Cinema = Pr.Cinema AND B.Sala = Pr.Sala AND B.Film = Pr.Film AND B.Data = Pr.Data
    INNER JOIN Cinema C ON Pr.Cinema = C.Codice
    INNER JOIN Film F ON Pr.Film = F.Codice
    GROUP BY Pr.Cinema, Pr.Sala, Pr.Film, Pr.Data
    HAVING Incasso > 10;

-- #6 Una selezione aggregata su raggruppamenti con condizioni che includano un’altra funzione di raggruppamento (es. dipartimenti la cui somma degli stipendi è la più alta):
-- Seleziona l'incasso medio di tutte le proiezioni
SELECT AVG(Incasso) as IncassoMedio
	FROM (
	    SELECT Pr.Cinema, Pr.Sala, Pr.Film, Pr.Data, SUM(B.Prezzo) AS Incasso
            FROM Biglietto B
            INNER JOIN Proiezione Pr ON B.Cinema = Pr.Cinema AND B.Sala = Pr.Sala AND B.Film = Pr.Film AND B.Data = Pr.Data
            GROUP BY Pr.Cinema, Pr.Sala, Pr.Film, Pr.Data
            HAVING Incasso > 10
    ) IncassiPerProiezione;

-- #7 Una selezione con operazioni insiemistiche:

-- #8 Una selezione con l’uso appropriato della divisione: