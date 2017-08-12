CREATE TABLE invoice_number(
    number INTEGER NOT NULL,
    year INTEGER NOT NULL);

CREATE TABLE Artikellijst (
    artikelnummer TEXT NOT NULL,
    omschrijving TEXT NOT NULL,
    prijsPer NUMERIC NOT NULL,
    eenheid TEXT NOT NULL,
    verkoopprijs NUMERIC NOT NULL,
    PRIMARY KEY(artikelnummer));
CREATE INDEX ArtikelOmschrijvingIndex ON Artikellijst (omschrijving);
CREATE INDEX ArtikelnummerIndex ON Artikellijst (artikelnummer);
