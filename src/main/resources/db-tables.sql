CREATE TABLE invoice_number(
    number INTEGER NOT NULL,
    year INTEGER NOT NULL
);

CREATE TABLE Artikellijst (
    artikelnummer TEXT NOT NULL,
    omschrijving TEXT NOT NULL,
    prijsPer NUMERIC NOT NULL,
    eenheid TEXT NOT NULL,
    verkoopprijs NUMERIC NOT NULL,
    PRIMARY KEY(artikelnummer)
);
CREATE INDEX ArtikelOmschrijvingIndex ON Artikellijst (omschrijving);
CREATE INDEX ArtikelnummerIndex ON Artikellijst (artikelnummer);

CREATE TABLE Debiteur (
    debiteurID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    naam TEXT NOT NULL,
    straat TEXT NOT NULL,
    nummer TEXT NOT NULL,
    postcode TEXT NOT NULL,
    plaats TEXT NOT NULL
);
CREATE TABLE BTWDebiteur (
    debiteurID INTEGER NOT NULL,
    btwNummer TEXT,
    PRIMARY KEY (debiteurID),
    FOREIGN KEY (debiteurID) REFERENCES Debiteur(debiteurID)
);

CREATE VIEW TotaalDebiteur AS
    SELECT d.debiteurID, d.naam, d.straat, d.nummer, d.postcode, d.plaats, b.btwNummer
    FROM Debiteur d NATURAL LEFT JOIN BTWDebiteur b;

CREATE TRIGGER DeleteDebiteurBtwDebiteur
INSTEAD OF DELETE ON TotaalDebiteur
BEGIN
    DELETE FROM BTWDebiteur
        WHERE BTWDebiteur.btwNummer = OLD.btwNummer
        AND BTWDebiteur.btwNummer = (SELECT btwNummer
                                     FROM BTWDebiteur
                                     WHERE debiteurID = (SELECT debiteurID
                                                         FROM Debiteur
                                                         WHERE naam = OLD.naam
                                                         AND straat = OLD.straat
                                                         AND nummer = OLD.nummer
                                                         AND postcode = OLD.postcode
                                                         AND plaats = OLD.plaats
                                                        )
                                    );

    DELETE FROM Debiteur
        WHERE naam = OLD.naam
        AND straat = OLD.straat
        AND nummer = OLD.nummer
        AND postcode = OLD.postcode
        AND plaats = OLD.plaats;
END;

CREATE TRIGGER InsertDebiteurBtwDebiteur
INSTEAD OF INSERT ON TotaalDebiteur
WHEN NOT EXISTS (SELECT 1 FROM Debiteur
                 WHERE naam=NEW.naam
                 AND straat=NEW.straat
                 AND nummer=NEW.nummer
                 AND postcode=NEW.postcode
                 AND plaats=NEW.plaats
                )
BEGIN
    INSERT INTO Debiteur(naam, straat, nummer, postcode, plaats)
        SELECT NEW.naam, NEW.straat, NEW.nummer, NEW.postcode, NEW.plaats;
    INSERT INTO BTWDebiteur (debiteurID, btwNummer)
        SELECT Debiteur.debiteurID, NEW.btwNummer
        FROM Debiteur
        WHERE NEW.btwNummer IS NOT NULL
        AND naam=NEW.naam
        AND straat=NEW.straat
        AND nummer=NEW.nummer
        AND postcode=NEW.postcode
        AND plaats=NEW.plaats
        AND NOT EXISTS (SELECT 1 FROM BTWDebiteur WHERE debiteurID=Debiteur.debiteurID AND btwNummer=NEW.btwNummer);
END;

CREATE TRIGGER UpdateComplexToComplexDebiteur
INSTEAD OF UPDATE ON TotaalDebiteur
WHEN EXISTS (SELECT 1 FROM BTWDebiteur WHERE BTWDebiteur.debiteurID = OLD.debiteurID)
AND NEW.btwNummer IS NOT NULL
BEGIN
    UPDATE BTWDebiteur
        SET btwNummer = NEW.btwNummer
        WHERE BTWDebiteur.debiteurID = OLD.debiteurID;
    UPDATE Debiteur
        SET naam = NEW.naam, straat = NEW.straat, nummer = NEW.nummer, postcode = NEW.postcode, plaats = NEW.plaats
        WHERE Debiteur.debiteurID = OLD.debiteurID;
END;

CREATE TRIGGER UpdateComplexToSimpleDebiteur
INSTEAD OF UPDATE ON TotaalDebiteur
WHEN EXISTS (SELECT 1 FROM BTWDebiteur WHERE BTWDebiteur.debiteurID = OLD.debiteurID)
AND NEW.btwNummer IS NULL
BEGIN
    DELETE FROM BTWDebiteur
        WHERE BTWDebiteur.debiteurID = OLD.debiteurID;
    UPDATE Debiteur
        SET naam = NEW.naam, straat = NEW.straat, nummer = NEW.nummer, postcode = NEW.postcode, plaats = NEW.plaats
        WHERE Debiteur.debiteurID = OLD.debiteurID;
END;

CREATE TRIGGER UpdateSimpleToComplexDebiteur
INSTEAD OF UPDATE ON TotaalDebiteur
WHEN NOT EXISTS (SELECT 1 FROM BTWDebiteur WHERE BTWDebiteur.debiteurID = OLD.debiteurID)
AND NEW.btwNummer IS NOT NULL
BEGIN
    INSERT INTO BTWDebiteur (debiteurID, btwNummer)
        VALUES (OLD.debiteurID, NEW.btwNummer);
    UPDATE Debiteur
        SET naam = NEW.naam, straat = NEW.straat, nummer = NEW.nummer, postcode = NEW.postcode, plaats = NEW.plaats
        WHERE Debiteur.debiteurID = OLD.debiteurID;
END;

CREATE TRIGGER UpdateSimpleToSimpleDebiteur
INSTEAD OF UPDATE ON TotaalDebiteur
WHEN NOT EXISTS (SELECT 1 FROM BTWDebiteur WHERE BTWDebiteur.debiteurID = OLD.debiteurID)
AND NEW.btwNummer IS NULL
BEGIN
    UPDATE Debiteur
        SET naam = NEW.naam, straat = NEW.straat, nummer = NEW.nummer, postcode = NEW.postcode, plaats = NEW.plaats
        WHERE Debiteur.debiteurID = OLD.debiteurID;
END;
