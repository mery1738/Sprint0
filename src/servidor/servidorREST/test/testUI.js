// -----------------------------------------------------------------------------
// Fichero: testUI.js
// Autor: Meryame Ait Boumlik
// Descripción: Test de la interfaz real (index.html + script.js).
//              Verifica que el cliente muestra correctamente la última medición.
// -----------------------------------------------------------------------------

const { JSDOM } = require("jsdom");
const { expect } = require("chai");
const fs = require("fs");
const path = require("path");

describe("Test de la Interfaz Web (UI)", function () {
  let dom;
  let document;
  let window;

  before(async function () {
    //  Cargar tu HTML real (index.html)
    const htmlPath = path.join(__dirname, "../../../cliente/index.html");
    const html = fs.readFileSync(htmlPath, "utf-8");

    //  Crear DOM simulado con JSDOM
    dom = new JSDOM(html, {
      runScripts: "dangerously",
      resources: "usable",
      url: "http://localhost:8080/"
    });

    window = dom.window;
    document = dom.window.document;

    // 3️⃣ Simular fetch() del servidor REST
    dom.window.fetch = async () => ({
      ok: true,
      json: async () => ({
        tipo: "CO2",
        valor: 333,
        instante: "2025-10-05T10:30:00"
      })
    });

    //  Cargar tu script.js dentro del DOM
    const scriptPath = path.join(__dirname, "../../../cliente/LogicaFake.js");
    const scriptContent = fs.readFileSync(scriptPath, "utf-8");
    const scriptElement = dom.window.document.createElement("script");
    scriptElement.textContent = scriptContent;
    dom.window.document.body.appendChild(scriptElement);

    // Esperar a que se ejecute el window.onload = cargarUltimaMedicion
    await new Promise(resolve => setTimeout(resolve, 200));
  });

  it("debería mostrar los valores correctos en el DOM", function () {
    const tipo = document.getElementById("tipo").textContent.trim();
    const valor = document.getElementById("valor").textContent.trim();
    const instante = document.getElementById("instante").textContent.trim();

    expect(tipo).to.equal("CO2");
    expect(valor).to.equal("333");
    expect(instante).to.equal("2025-10-05T10:30:00");
  });
});
