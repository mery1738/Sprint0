module.exports.cargar = function (servidorExpress, laLogica) {
    // -----------------------------------------------------------
    // GET /prueba
    // -----------------------------------------------------------
    servidorExpress.get("/prueba", (req, res) => {
        console.log(" * GET /prueba ");
        res.send("¡Funciona!");
    });

    // -----------------------------------------------------------
    // POST /medicion
    // Cuerpo esperado: { "tipo": "CO2", "valor": 400, "instante": "2025-09-25T10:30:00" }
    // -----------------------------------------------------------
    servidorExpress.post("/medicion", async (req, res) => {
        console.log(" * POST /medicion ");

        let datos = null;

        try {
            datos = JSON.parse(req.body);
        } catch (err) {
            res.status(400).send("Cuerpo no es JSON válido");
            return;
        }

        if (!datos.tipo || !datos.valor || !datos.instante) {
            res.status(400).send("Faltan campos: tipo, valor o instante");
            return;
        }

        try {
            await laLogica.guardarMedicion(datos.tipo, datos.valor, datos.instante);
            res.status(200).send("Medición guardada");
        } catch (err) {
            res.status(500).send("Error al guardar medición: " + err);
        }
    });

    // -----------------------------------------------------------
    // GET /ultimaMedicion
    // Devuelve la última medición registrada
    // -----------------------------------------------------------
    servidorExpress.get("/ultimaMedicion", async (req, res) => {
        console.log(" * GET /ultimaMedicion ");

        try {
            const resultado = await laLogica.getMedicion();

            if (!resultado) {
                res.status(404).send("No hay mediciones registradas");
                return;
            }

            res.send(JSON.stringify(resultado));
        } catch (err) {
            res.status(500).send("Error al obtener medición: " + err);
        }
    });
};
