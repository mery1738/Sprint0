// -----------------------------------------------------------------------------
// Fichero: testLogica.js
// Autor: Meryame Ait Boumlik
// Descripción: Tests unitarios de la lógica de negocio sin usar una BD real.
// -----------------------------------------------------------------------------

// Simular la base de datos SQLite
const sqlite3Mock = {
  Database: class {
    constructor(name, cb) {
      this.data = [];
      const self = this;

      // simulate run() like sqlite3
      self.run = function (sql, params, cb2) {
        if (typeof params === "function") {
          cb2 = params;
          params = undefined;
        }

        if (sql.trim().toUpperCase().startsWith("INSERT")) {
          // store params (either object or array)
          self.data.push(params);
          self.lastID = self.data.length; // simulate incremental ID
        }

        if (cb2) cb2.call(self, null);
      };

      self.get = function (sql, params, cb2) {
        // return the last inserted row
        cb2 && cb2(null, self.data[self.data.length - 1]);
      };

      self.close = function (cb2) {
        cb2 && cb2(null);
      };

      if (cb) setTimeout(() => cb(null), 0);
    }
  }
};



// Sustituir el módulo sqlite3 real por el mock
require.cache[require.resolve("sqlite3")] = { exports: sqlite3Mock };

const { expect } = require("chai");
const Logica = require("../Logica.js");

// -----------------------------------------------------------------------------
// TESTS
// -----------------------------------------------------------------------------
describe("Tests de Lógica de Negocio", () => {
  let logica;

  beforeEach(() => {
    logica = new Logica("fake.db", () => {});
  });

  afterEach(() => {
    logica = null;
  });

  // ----------------------------------------------------------
  // Test 1: guardarMedicion()
  // ----------------------------------------------------------
  it("guardarMedicion() guarda una medición correctamente", async () => {
    await logica.guardarMedicion("CO2", 333, "2025-10-05T10:00:00");

    // Verificar que se insertó el registro correcto
    expect(logica.laConexion.data).to.have.lengthOf(1);
    expect(logica.laConexion.data[0].$tipo).to.equal("CO2");
    expect(logica.laConexion.data[0].$valor).to.equal(333);
  });

  // ----------------------------------------------------------
  // Test 2: getMedicion()
  // ----------------------------------------------------------
  it("getMedicion() devuelve la última medición guardada", async () => {
    // Insertar un registro simulado
    logica.laConexion.data.push({
      $tipo: "TEMP",
      $valor: 25,
      $instante: "2025-10-06T12:00:00"
    });

    const res = await logica.getMedicion();

    expect(res.$tipo).to.equal("TEMP");
    expect(res.$valor).to.equal(25);
    expect(res.$instante).to.equal("2025-10-06T12:00:00");
  });
});
