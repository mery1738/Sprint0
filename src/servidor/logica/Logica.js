// -----------------------------------------------------------------------------
// Fichero: Logica.js
// Autor: Meryame Ait Boumlik
// Descripción: Gestiona la conexión y operaciones con la base de datos SQLite
// -----------------------------------------------------------------------------

const sqlite3 = require("sqlite3");

module.exports = class Logica {
    
    // --------------------------------------------------------------
    // constructor()
    // Descripción: Abre la conexión con la base de datos SQLite.
    // Diseño: String -> constructor() -> 
    // Parámetros: nombreBD (string), cb (función callback)
    // --------------------------------------------------------------
    constructor(nombreBD, cb) {
        this.laConexion = new sqlite3.Database(
            nombreBD,
            (err) => {
                if (!err) {
                    this.laConexion.run("PRAGMA foreign_keys = ON");
                }
                cb(err);
            }
        );
    }

    // -------------------------------------------------------------------
    // guardarMedicion()
    // Descripción: Guarda una medición en la tabla Mediciones.
    // Diseño: string, real, string -> guardarMedicion() -> id: entero
    // Parámetros: tipo, valor, instante
    // -------------------------------------------------------------------
     guardarMedicion(tipo, valor, instante) {
        const textoSQL = `
            INSERT INTO Mediciones (tipo, valor, instante)
            VALUES ($tipo, $valor, $instante)
        `;
        const valoresParaSQL = {
            $tipo: tipo,
            $valor: valor,
            $instante: instante,
        };
        return new Promise((resolver, rechazar) => {
            this.laConexion.run(textoSQL, valoresParaSQL, function (err) {
                err ? rechazar(err) : resolver(this.lastID); // devolvemos id generado
            });
        });
    }

    // -------------------------------------------------------------------
    // getMedicion()
    // Descripción: Obtiene la última medición registrada en la base de datos.
    // Diseño: () -> getMedicion() -> { id: entero, tipo: Texto, valor: Real, instante: Texto}
    // Parámetros: ninguno
    // -------------------------------------------------------------------
   getMedicion() {
        const textoSQL = `
            SELECT * FROM Mediciones 
            ORDER BY id DESC 
            LIMIT 1
        `;
        return new Promise((resolver, rechazar) => {
            this.laConexion.get(textoSQL, [], (err, res) => {
                err ? rechazar(err) : resolver(res);
            });
        });
    }
};