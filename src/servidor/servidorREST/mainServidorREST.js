// -----------------------------------------------------------------------------
// Fichero: mainServidorREST.js
// Autor: Meryame Ait Boumlik
// Descripción: Servidor REST principal que conecta la base de datos con las
//              reglas REST y escucha peticiones en el puerto 8080.
// -----------------------------------------------------------------------------

const express = require("express");
const bodyParser = require("body-parser");
const Logica = require("../logica/Logica.js");
// -----------------------------------------------------------------------------
// cargarLogica()
// Diseño: texto:ficheroBD -> cargarLogica() -> objeto Logica
// Descripción: Crea un objeto Logica conectado a la base de datos indicada.
// -----------------------------------------------------------------------------
function cargarLogica(ficheroBD) {
    return new Promise((resolver, rechazar) => {
        const laLogica = new Logica(ficheroBD, (err) => {
            if (err) {
                rechazar(err);
            } else {
                resolver(laLogica);
            }
        });
    });
}
// -----------------------------------------------------------------------------
// main()
//   Diseño: -> main() -> vacío
//  Descripción: 
//  - Espera a que se cargue la lógica.
//  - Crea el servidor Express.
//  - Carga las reglas REST (ReglasREST.js).
//  - Inicia el servidor en el puerto 8080.
// -----------------------------------------------------------------------------
async function main() {
    const path = require("path");
    const laLogica = await cargarLogica(path.join(__dirname, "../bd/datos.db"));



    const servidorExpress = express();

    const cors = require("cors");
    servidorExpress.use(cors());


    // Para leer JSON
    servidorExpress.use(express.json({ type: ['application/json', 'application/json; charset=utf-8'] }));


    // Cargar reglas
    const reglas = require("./ReglasREST.js");
    reglas.cargar(servidorExpress, laLogica);

    servidorExpress.use(express.static(__dirname + "/../cliente"));


    // Arrancar servidor
    const servicio = servidorExpress.listen(8080, () => {
        console.log("Servidor REST escuchando en puerto 8080");
    });

    // Cerrar con Ctrl+C
    process.on("SIGINT", () => {
        console.log("Terminando servidor...");
        servicio.close();
    });
}

main();
