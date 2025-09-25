const Logica = require("../Logica.js");
const assert = require("assert");

describe("Pruebas de Logica: Mediciones", function () {
    let laLogica = null;

    // ------------------------------------------------------
    // Conectar a la base de datos
    // ------------------------------------------------------
    it("conectar a la base de datos", function (hecho) {
        laLogica = new Logica("../bd/datos.bd", function (err) {
            if (err) {
                throw new Error("No se pudo conectar con datos.bd");
            }
            hecho(); // avisamos que ya está
        });
    });

    // ------------------------------------------------------
    // Borrar todas las filas antes de empezar
    // ------------------------------------------------------
    it("borrar todas las filas de Mediciones", async function () {
        await laLogica.laConexion.run("DELETE FROM Mediciones");
    });

    // ------------------------------------------------------
    // Guardar una medición y recuperarla
    // ------------------------------------------------------
    it("puedo guardar y recuperar la última medición", async function () {
        const instante = new Date().toISOString();

        // Guardamos una medición
        await laLogica.guardarMedicion("CO2", 1234, instante);

        // Recuperamos la última
        const ultima = await laLogica.getMedicion();

        assert(ultima, "¿No devolvió nada?");
        assert.equal(ultima.tipo, "CO2", "¿El tipo no es CO2?");
        assert.equal(ultima.valor, 1234, "¿El valor no es 1234?");
        assert.equal(ultima.instante, instante, "¿El instante no coincide?");
    });

    // ------------------------------------------------------
    // Cerrar conexión
    // ------------------------------------------------------
    it("cerrar conexión a la base de datos", async function () {
        try {
            await laLogica.cerrar();
        } catch (err) {
            throw new Error("Fallo al cerrar la base de datos: " + err);
        }
    });
});
