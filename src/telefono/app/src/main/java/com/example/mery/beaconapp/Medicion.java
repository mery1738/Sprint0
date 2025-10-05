package com.example.mery.beaconapp;
// ------------------------------------------------------------------
// Medicion.java
// Descripción: Representa una medición con tipo, valor e instante.
// ------------------------------------------------------------------
public class Medicion {

    private String tipo;
    private int valor;
    private String instante;

// --------------------------------------------------------------
    // Medicion()
    // Descripción: Constructor que inicializa tipo, valor e instante.
    // Diseño: string:tipo, entero:valor, string:instante -> Medicion() -> objeto
    // Parámetros: tipo, valor, instante
// --------------------------------------------------------------
    public Medicion( String tipo, int valor, String instante) {

        this.tipo = tipo;
        this.valor = valor;
        this.instante = instante;
    }

// --------------------------------------------------------------
    // getTipo()
    // Descripción: Devuelve el tipo de medición.
    // Diseño: -> getTipo() -> string
// --------------------------------------------------------------
    public String getTipo()
    {
        return tipo;
    }
// --------------------------------------------------------------
    // getValor()
    // Descripción: Devuelve el valor de la medición.
    // Diseño: -> getValor() -> entero
// --------------------------------------------------------------
    public int getValor()
    {
        return valor;
    }
// --------------------------------------------------------------
    // getInstante()
    // Descripción: Devuelve el instante de la medición.
    // Diseño: -> getInstante() -> string
// --------------------------------------------------------------
    public String getInstante()
    {
        return instante;
    }
// --------------------------------------------------------------
    // toString()
    // Descripción: Convierte la medición en texto legible.
    // Diseño: -> toString() -> string
// --------------------------------------------------------------
    @Override
    public String toString() {
        return "Medicion{" +
                "tipo='" + tipo + '\'' +
                ", valor=" + valor +
                ", instante='" + instante + '\'' +
                '}';
    }

}
