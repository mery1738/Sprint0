-- Tabla de Mediciones
DROP TABLE IF EXISTS Mediciones;

CREATE TABLE Mediciones (
    tipo TEXT,         -- tipo de medición: "CO2", "TEMPERATURA", "RUIDO"
    valor INTEGER,     -- valor de la medición
    instante TEXT      -- timestamp ISO-8601 en string (ej: "2025-02-21T15:30:00")
);

