-- Tabla de Mediciones
DROP TABLE IF EXISTS Mediciones;

CREATE TABLE Mediciones (
    id INTEGER PRIMARY KEY AUTOINCREMENT, -- identificador único
    tipo TEXT,                            -- "CO2", "TEMPERATURA", "RUIDO"
    valor REAL,                           -- puede ser int o decimal
    instante TEXT                         -- timestamp ISO-8601
);

