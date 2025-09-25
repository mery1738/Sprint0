const express = require("express");
const bodyParser = require("body-parser");
const Logica = require("../logica/Logica.js");

// Cargar lógica desde la BD
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

// Main del servidor
async function main() {
    const laLogica = await cargarLogica("../bd/datos.bd");

    const servidorExpress = express();

    // Para leer JSON
    servidorExpress.use(bodyParser.text({ type: "application/json" }));

    // Cargar reglas
    const reglas = require("./ReglasREST.js");
    reglas.cargar(servidorExpress, laLogica);

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
