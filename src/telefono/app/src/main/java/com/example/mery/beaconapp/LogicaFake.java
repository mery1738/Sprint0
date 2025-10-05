package com.example.mery.beaconapp;
// ------------------------------------------------------------------
// LogicaFake.java
// Descripción: Simula la lógica de almacenamiento local de mediciones.
// ------------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

public class LogicaFake {
    private List<Medicion> mediciones = new ArrayList<>();
// --------------------------------------------------------------
    // guardarMedicion()
    // Descripción: Añade una medición a la lista en memoria.
    // Diseño: objeto:Medicion -> guardarMedicion() -> vacío
    // Parámetros: m : medición a guardar
// --------------------------------------------------------------
    public void guardarMedicion(Medicion m) {
        mediciones.add(m);
    }

/* No se necesita por ahora
// --------------------------------------------------------------
    // getMediciones()
    // Descripción: Devuelve la lista de mediciones almacenadas.
    // Diseño: -> getMediciones() -> lista[Medicion]
    // Parámetros: ninguno
// --------------------------------------------------------------
    public List<Medicion> getMediciones() {
        return mediciones;
    }
*/
}
