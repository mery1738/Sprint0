// -----------------------------------------------------------------------------
// Fichero: LogicaFake.js
// Autor: Meryame Ait Boumlik
// Descripción: Lógica simulada del cliente ("LogicaFake") que obtiene la última
//              medición desde el servidor REST y la muestra en la página web.
// -----------------------------------------------------------------------------

// -----------------------------------------------------------------------------
// URL del servidor REST
//    - Para ver desde el navegador del PC:
//          http://localhost:8080/ultimaMedicion
//    - Para ver desde el teléfono (misma red Wi-Fi):
//          http://<IP_DEL_PC>:8080/ultimaMedicion
// -----------------------------------------------------------------------------
const API_URL = "http://localhost:8080/ultimaMedicion";


// -----------------------------------------------------------------------------
// Función: cargarUltimaMedicion()
// Descripción: Obtiene la última medición desde el servidor REST y actualiza
//              los elementos del DOM con los valores recibidos.
// Diseño: () -> fetch(API_URL) -> actualizar DOM
// Parámetros: ninguno
// -----------------------------------------------------------------------------
async function cargarUltimaMedicion() {
    try {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error("Error en la respuesta del servidor");

        const data = await response.json();

        // Actualiza los datos en la página web
        document.getElementById("tipo").textContent = data.tipo;
        document.getElementById("valor").textContent = data.valor;
        document.getElementById("instante").textContent = data.instante;

    } catch (error) {
        console.error("No se pudo obtener la medición:", error);
    }
}


// -----------------------------------------------------------------------------
// Inicialización de eventos
//    - Al cargar la página: se muestra la última medición.
//    - Al pulsar el botón “Actualizar”: se vuelve a obtener la medición.
// -----------------------------------------------------------------------------
window.onload = cargarUltimaMedicion;
document.getElementById("btnActualizar").addEventListener("click", cargarUltimaMedicion);
