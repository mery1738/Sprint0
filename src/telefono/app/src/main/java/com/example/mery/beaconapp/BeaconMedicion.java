package com.example.mery.beaconapp;


public class BeaconMedicion {
    public static final int SENSOR_CO2   = 11;
    public static final int SENSOR_TEMP  = 12;
    public static final int SENSOR_RUIDO = 13;

    private final String dispositivoMac;  // Identificador único del Arduino
    private final int sensorId;
    private final int contador;
    private final int valor;
    private final int rssi;
    private final long timestamp;

    public BeaconMedicion(TramaIBeacon tib, String mac, int rssi) {
        int major = Utilidades.bytesToInt(tib.getMajor());   // big endian
        int minor = Utilidades.bytesToInt(tib.getMinor());

        this.sensorId = (major >> 8) & 0xFF;   // byte alto = sensorId
        this.contador = major & 0xFF;          // byte bajo = contador
        this.valor = (short) minor;            // soporta negativos
        this.dispositivoMac = mac;
        this.rssi = rssi;
        this.timestamp = System.currentTimeMillis();
    }

    public String getDispositivoMac() { return dispositivoMac; }
    public int getSensorId() { return sensorId; }
    public int getContador() { return contador; }
    public int getValor() { return valor; }
    public int getRssi() { return rssi; }
    public long getTimestamp() { return timestamp; }

    public String descripcion() {
        switch (sensorId) {
            case SENSOR_CO2:
                return "CO2 = " + valor + " ppm (c=" + contador + ")";
            case SENSOR_TEMP:
                return "Temperatura = " + valor + " °C (c=" + contador + ")";
            case SENSOR_RUIDO:
                return "Ruido = " + valor + " dB (c=" + contador + ")";
            default:
                return "Sensor " + sensorId + " valor=" + valor + " (c=" + contador + ")";
        }
    }
}