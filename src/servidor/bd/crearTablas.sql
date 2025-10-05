-- Tabla de Mediciones
DROP TABLE IF EXISTS Mediciones;

CREATE TABLE Mediciones (
    id INTEGER PRIMARY KEY AUTOINCREMENT, -- identificador único
    tipo TEXT,                            -- "CO2", "TEMPERATURA", 
    valor REAL,                           -- 1234
    instante TEXT                         -- timestamp 
);

