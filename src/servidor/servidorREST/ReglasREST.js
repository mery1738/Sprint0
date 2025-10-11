module.exports.cargar = function (servidorExpress, laLogica) {
// -----------------------------------------------------------------------------
// Fichero: ReglasREST.js
// Autor: Meryame Ait Boumlik
// Descripción: Define las rutas REST del servidor para registrar y obtener
//              mediciones desde la base de datos.
// -----------------------------------------------------------------------------

    
    // -----------------------------------------------------------
    //GET /prueba
    // Diseño: -> /prueba -> texto
    // Descripción: Devuelve una respuesta simple para comprobar el funcionamiento del servidor.
    // -----------------------------------------------------------
    servidorExpress.get("/prueba", (req, res) => {
        console.log(" * GET /prueba ");
        res.send("¡Funciona!");
    });

    // -----------------------------------------------------------
    // POST /medicion
    // Diseño: objeto:{tipo, valor, instante} -> /medicion -> texto
    // Descripción: Guarda una medición en la base de datos usando laLogica.guardarMedicion().
    // -----------------------------------------------------------
    servidorExpress.post("/medicion", async (req, res) => {
    console.log(" * POST /medicion ");
    console.log(" Recibido:", req.body); 

    const datos = req.body; 

    if (!datos.tipo || !datos.valor || !datos.instante) {
        res.status(400).send("Faltan campos: tipo, valor o instante");
        return;
    }

    try {
        await laLogica.guardarMedicion(datos.tipo, datos.valor, datos.instante);
        res.status(200).send("Medición guardada");
    } catch (err) {
        console.error(" Error al guardar:", err);
        res.status(500).send("Error al guardar medición: " + err);
    }
});


    // -----------------------------------------------------------
    // GET /ultimaMedicion
    // Diseño: -> /ultimaMedicion -> objeto:{id, tipo, valor, instante}
    // Descripción: Obtiene la última medición registrada desde la base de datos.
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
