const sqlite3 = require("sqlite3");

module.exports = class Logica {
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
    // Guardar una medición
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
    // Obtener la última medición registrada (ordenada por id)
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