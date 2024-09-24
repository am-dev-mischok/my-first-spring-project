--Die SQL-Queries in dieser Datei werden von Spring beim Start des Programms automatisch ausgeführt, wenn wir H2 im In-Memory-Mode laufen lassen. Oder auch wenn wir im File-Mode sind, aber dann in den application.properties setzen:   spring.sql.init.mode=always
--Das machen wir aber lieber nicht und erstellen unsere Tabellen stattdessen mit Flyway.

CREATE TABLE IF NOT EXISTS person (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    age INT,
    married BOOLEAN
);
