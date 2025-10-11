// -----------------------------------------------------------------------------
// Fichero: testAPI.js
// Autor: Meryame Ait Boumlik
// Descripción: Tests automáticos de la API REST para /medicion y /ultimaMedicion.
// -----------------------------------------------------------------------------

const request = require("supertest");
const express = require("express");
const { expect } = require("chai");

// Importamos la lógica y el archivo de rutas
const Logica = require("../../logica/Logica.js");
const reglas = require("../ReglasREST.js"); // ajusta si ruta difiere

// -----------------------------------------------------------------------------
// Mock de Logica para no tocar la BD real
// -----------------------------------------------------------------------------
class LogicaFake {
  constructor() {
    this.data = [];
  }

  async guardarMedicion(tipo, valor, instante) {
    const medicion = { tipo, valor, instante };
    this.data.push(medicion);
    return this.data.length; // simula id generado
  }

  async getMedicion() {
    return this.data[this.data.length - 1];
  }
}

// -----------------------------------------------------------------------------
// TESTS
// -----------------------------------------------------------------------------
describe("Tests API REST", () => {
  let app, logica;

  beforeEach(() => {
    app = express();
    app.use(express.json());
    logica = new LogicaFake();
    reglas.cargar(app, logica); // usar la lógica fake
  });

  // --------------------------------------------------------------
  // Test 1: POST /medicion
  // --------------------------------------------------------------
  it("POST /medicion guarda una medición correctamente", async () => {
    const res = await request(app)
      .post("/medicion")
      .send({ tipo: "CO2", valor: 333, instante: "2025-10-05T10:00:00" });

    expect(res.status).to.equal(200);
    expect(res.text).to.include("Medición guardada");
    expect(logica.data).to.have.lengthOf(1);
    expect(logica.data[0].tipo).to.equal("CO2");
  });

  // --------------------------------------------------------------
  // Test 2: GET /ultimaMedicion
  // --------------------------------------------------------------
  it("GET /ultimaMedicion devuelve la última medición registrada", async () => {
    // pre-cargar medición simulada
    logica.data.push({
      tipo: "TEMPERATURA",
      valor: 25,
      instante: "2025-10-06T12:00:00"
    });

    const res = await request(app).get("/ultimaMedicion");

    expect(res.status).to.equal(200);
    const body = JSON.parse(res.text);
    expect(body.tipo).to.equal("TEMPERATURA");
    expect(body.valor).to.equal(25);
  });
});