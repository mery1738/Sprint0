// -----------------------------------------------------------------------------
// Fichero: LogicaFake.js
// Autor: Meryame Ait Boumlik
// Descripción: Lógica simulada del cliente que obtiene la última medición del
//              servidor REST.
// -----------------------------------------------------------------------------
export default class LogicaFake {
  async obtenerUltimaMedicion() {
    try {
      const respuesta = await fetch("http://localhost:8080/ultimaMedicion");
      if (!respuesta.ok) throw new Error("Error al obtener medición");
      const datos = await respuesta.json();
      return datos;
    } catch (err) {
      console.error(" Error en LogicaFake:", err);
      return null;
    }
  }
}
