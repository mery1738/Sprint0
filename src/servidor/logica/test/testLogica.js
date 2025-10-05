// -----------------------------------------------------------------------------
// Fichero: testLogica.js
// Autor: Meryame Ait Boumlik
// Descripción: Pruebas automáticas para comprobar que guardarMedicion() y
//              getMedicion() funcionan correctamente.
// -----------------------------------------------------------------------------

const Logica = require("../logica/Logica.js");
const assert = require("assert");

describe("Pruebas automáticas de Logica: Mediciones", function () {
    let laLogica = null;

    it("conectar a la base de datos", function (hecho) {
        laLogica = new Logica("../bd/datos.db", function (err) {
            if (err) throw new Error("No se pudo conectar con datos.db");
            hecho();
        });
    });

    it("borrar todas las filas de Mediciones", async function () {
        await laLogica.borrarFilasDe("Mediciones");
    });

    // ------------------------------------------------------
    // Descripción: Guarda una medición fija (CO2, 333) y comprueba que se recupera igual.
    // ------------------------------------------------------
    it("guardar y recuperar medición CO2 = 333", async function () {
        const tipoEsperado = "CO2";
        const valorEsperado = 333;
        const instanteEsperado = new Date().toISOString();

        // Guardar medición
        await laLogica.guardarMedicion(tipoEsperado, valorEsperado, instanteEsperado);

        // Obtener la última medición
        const ultima = await laLogica.getMedicion();

        // Verificaciones automáticas
        assert(ultima, "No se devolvió ninguna medición");
        assert.strictEqual(ultima.tipo, tipoEsperado, "Tipo incorrecto");
        assert.strictEqual(ultima.valor, valorEsperado, "Valor incorrecto");
        assert.strictEqual(ultima.instante, instanteEsperado, "Instante incorrecto");
    });

    it("cerrar conexión a la base de datos", async function () {
        try {
            await laLogica.cerrar();
        } catch (err) {
            throw new Error("Fallo al cerrar la base de datos: " + err);
        }
    });
});
