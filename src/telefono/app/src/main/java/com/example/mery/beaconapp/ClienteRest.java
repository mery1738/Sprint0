package com.example.mery.beaconapp;

// ------------------------------------------------------------------
// ClienteRest.java
// Descripción: Envía objetos de tipo Medicion a un servidor REST mediante una petición HTTP POST en formato JSON.
// ------------------------------------------------------------------
import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClienteRest {

    private static final String ETIQUETA_LOG = ">>>>";
    private static final String DIRECCION_API = "http://192.168.84.130:8080/medicion"; // IP del PC ( 10.0.2.2 en emulador)

// --------------------------------------------------------------
    // enviarMedicion()
    // Descripción: Envía una medición al servidor REST en formato JSON.
    // Diseño: objeto:Medicion -> enviarMedicion() -> vacío
    // Parámetros: medicion : objeto con tipo, valor e instante
// --------------------------------------------------------------
    public static void enviarMedicion(Medicion medicion) {
        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                // Crear objeto JSON con los datos de la medición
                JSONObject json = new JSONObject();
                json.put("tipo", medicion.getTipo());
                json.put("valor", medicion.getValor());
                json.put("instante", medicion.getInstante());

                Log.d(ETIQUETA_LOG, " Enviando JSON: " + json.toString());

                // Configurar conexión HTTP
                URL url = new URL(DIRECCION_API);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                // Enviar JSON al servidor
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = json.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Leer respuesta del servidor
                int code = conn.getResponseCode();
                Log.d(ETIQUETA_LOG, " Código de respuesta del servidor: " + code);

                //  Read server response for debugging
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    Log.d(ETIQUETA_LOG, " Respuesta del servidor: " + response);
                }

            } catch (Exception e) {
                Log.e(ETIQUETA_LOG, " Error enviando medicion", e);
            } finally {
                if (conn != null) conn.disconnect();
            }
        }).start();
    }
}
