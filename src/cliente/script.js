// -----------------------------------------------------------------------------
// Fichero: script.js
// Autor: Meryame Ait Boumlik
// Descripción: Carga y muestra la última medición desde el servidor REST en la página web.
// -----------------------------------------------------------------------------

// when viewing the page from the PC browser:
const API_URL = "http://localhost:8080/ultimaMedicion";
// when viewing the page from the phone (same Wi-Fi):
// const API_URL = "http://192.168.84.130:8080/ultimaMedicion";


// -----------------------------------------------------------------------------
// Diseño: () -> cargarUltimaMedicion() -> actualiza elementos del DOM
// Parámetros: ninguno
// -----------------------------------------------------------------------------
async function cargarUltimaMedicion() {
    try {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error("Error en la respuesta del servidor");

        const data = await response.json();

        // Show data in the page
        document.getElementById("tipo").textContent = data.tipo;
        document.getElementById("valor").textContent = data.valor;
        document.getElementById("instante").textContent = data.instante;

    } catch (error) {
        console.error(" No se pudo obtener la medición:", error);
    }
}

window.onload = cargarUltimaMedicion;
document.getElementById("btnActualizar").addEventListener("click", cargarUltimaMedicion);
