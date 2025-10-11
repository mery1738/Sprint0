package com.example.mery.beaconapp;

// ------------------------------------------------------------------
// MainActivity.java
// Descripción: Actividad principal que escanea, muestra y gestiona dispositivos BLE (iBeacons).
// ------------------------------------------------------------------

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

// ------------------------------------------------------------------
// Clase: MainActivity
// Descripción: Controla el escaneo BLE, interpreta tramas iBeacon y envía mediciones al servidor REST.
// ------------------------------------------------------------------
public class MainActivity extends AppCompatActivity {


    private static final String ETIQUETA_LOG = ">>>>";
    private static final int CODIGO_PETICION_PERMISOS = 11223344;
    private static final int REQUEST_ENABLE_BT = 99; // request code for enabling Bluetooth
    private BluetoothLeScanner elEscanner;
    private ScanCallback callbackDelEscaneo = null;
    private boolean escaneando = false;
    private long lastSentTime = 0;

// --------------------------------------------------------------
    // buscarTodosLosDispositivosBTLE()
    // Descripción: Inicia el escaneo de todos los dispositivos BLE.
    // Diseño: -> buscarTodosLosDispositivosBTLE() -> vacío
    // Parámetros: ninguno
// --------------------------------------------------------------
    private void buscarTodosLosDispositivosBTLE() {
        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empieza ");

        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): instalamos scan callback ");

        this.callbackDelEscaneo = new ScanCallback() {
            @Override
            public void onScanResult( int callbackType, ScanResult resultado ) {
                super.onScanResult(callbackType, resultado);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanResult() ");

                mostrarInformacionDispositivoBTLE( resultado );
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onBatchScanResults() ");

            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanFailed() ");

            }
        };

        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empezamos a escanear ");

        this.elEscanner.startScan( this.callbackDelEscaneo);
        this.escaneando = true;

    } // ()

// --------------------------------------------------------------
    // mostrarInformacionDispositivoBTLE()
    // Descripción: Muestra por log los datos de un dispositivo BLE y envía la medición al servidor.
    // Diseño: objeto:ScanResult -> mostrarInformacionDispositivoBTLE() -> vacío
    // Parámetros: resultado : ScanResult
// --------------------------------------------------------------
    private void mostrarInformacionDispositivoBTLE( ScanResult resultado ) {

        BluetoothDevice bluetoothDevice = resultado.getDevice();
        byte[] bytes = resultado.getScanRecord().getBytes();
        int rssi = resultado.getRssi();

        Log.d(ETIQUETA_LOG, " ****************************************************");
        Log.d(ETIQUETA_LOG, " ****** DISPOSITIVO DETECTADO BTLE ****************** ");
        Log.d(ETIQUETA_LOG, " ****************************************************");
        Log.d(ETIQUETA_LOG, " nombre = " + bluetoothDevice.getName());
        Log.d(ETIQUETA_LOG, " toString = " + bluetoothDevice.toString());

        /*
        ParcelUuid[] puuids = bluetoothDevice.getUuids();
        if ( puuids.length >= 1 ) {
            //Log.d(ETIQUETA_LOG, " uuid = " + puuids[0].getUuid());
           // Log.d(ETIQUETA_LOG, " uuid = " + puuids[0].toString());
        }*/

        Log.d(ETIQUETA_LOG, " dirección = " + bluetoothDevice.getAddress());
        Log.d(ETIQUETA_LOG, " rssi = " + rssi );

        Log.d(ETIQUETA_LOG, " bytes = " + new String(bytes));
        Log.d(ETIQUETA_LOG, " bytes (" + bytes.length + ") = " + Utilidades.bytesToHexString(bytes));

        TramaIBeacon tib = new TramaIBeacon(bytes);

        Log.d(ETIQUETA_LOG, " ----------------------------------------------------");
        Log.d(ETIQUETA_LOG, " prefijo  = " + Utilidades.bytesToHexString(tib.getPrefijo()));
        Log.d(ETIQUETA_LOG, "          advFlags = " + Utilidades.bytesToHexString(tib.getAdvFlags()));
        Log.d(ETIQUETA_LOG, "          advHeader = " + Utilidades.bytesToHexString(tib.getAdvHeader()));
        Log.d(ETIQUETA_LOG, "          companyID = " + Utilidades.bytesToHexString(tib.getCompanyID()));
        Log.d(ETIQUETA_LOG, "          iBeacon type = " + Integer.toHexString(tib.getiBeaconType()));
        Log.d(ETIQUETA_LOG, "          iBeacon length 0x = " + Integer.toHexString(tib.getiBeaconLength()) + " ( "
                + tib.getiBeaconLength() + " ) ");
        Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToHexString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " major  = " + Utilidades.bytesToHexString(tib.getMajor()) + "( "
                + Utilidades.bytesToInt(tib.getMajor()) + " ) ");

        int tipoMedicion = (Utilidades.bytesToInt(tib.getMajor()) >> 8) & 0xFF;
        Log.d(ETIQUETA_LOG, " tipo medicion  = " + tipoMedicion);

        int contador = Utilidades.bytesToInt(tib.getMajor()) & 0xFF;
        Log.d(ETIQUETA_LOG, " contador  = " + contador);

        Log.d(ETIQUETA_LOG, " minor  = " + Utilidades.bytesToHexString(tib.getMinor()) + "( "
                + Utilidades.bytesToInt(tib.getMinor()) + " ) ");

        int valor = Utilidades.bytesToInt(tib.getMinor());
        Log.d(ETIQUETA_LOG, " valor  = " + valor);
        String instante;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            instante = LocalDateTime.now().toString();
        } else {
            instante = new Date().toString();
        }


        String tipo = "";
        if (tipoMedicion == 11) tipo = "CO2";
        else if (tipoMedicion == 12) tipo = "TEMPERATURA";
        else if (tipoMedicion == 13) tipo = "RUIDO";
        else tipo = "DESCONOCIDO";

        Medicion m = new Medicion( tipo, valor, instante);

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSentTime > 10000) { // send only every 5 seconds
            lastSentTime = currentTime;
            LogicaFake.guardarMedicion(m);
            Log.d(ETIQUETA_LOG, " Medición enviada: " + m);
        } else {
            Log.d(ETIQUETA_LOG, " Esperando antes de enviar otra medición");
        }
        Log.d(ETIQUETA_LOG, ">>> Medición enviada: " + m);
        Log.d(ETIQUETA_LOG, " txPower  = " + Integer.toHexString(tib.getTxPower()) + " ( " + tib.getTxPower() + " )");
        Log.d(ETIQUETA_LOG, " ****************************************************");

    } // ()

// --------------------------------------------------------------
    // buscarEsteDispositivoBTLE()
    // Descripción: Escanea solo un dispositivo BLE específico.
    // Diseño: string:dispositivoBuscado -> buscarEsteDispositivoBTLE() -> vacío
    // Parámetros: dispositivoBuscado : nombre del dispositivo
// --------------------------------------------------------------
    private void buscarEsteDispositivoBTLE(final String dispositivoBuscado) {
        Log.d(ETIQUETA_LOG, " buscarEsteDispositivoBTLE(): empieza ");

        Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): instalamos scan callback ");


        // super.onScanResult(ScanSettings.SCAN_MODE_LOW_LATENCY, result); para ahorro de energía

        this.callbackDelEscaneo = new ScanCallback() {
            @Override
            public void onScanResult( int callbackType, ScanResult resultado ) {
                super.onScanResult(callbackType, resultado);
                Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onScanResult() ");

                mostrarInformacionDispositivoBTLE( resultado );
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onBatchScanResults() ");

            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onScanFailed() ");

            }
        };

        ScanFilter sf = new ScanFilter.Builder().setDeviceName( dispositivoBuscado ).build();


        List<ScanFilter> filtros = new java.util.ArrayList<>();
        filtros.add(sf);

        // 4. Configuración de escaneo (modo rápido, baja latencia)
        android.bluetooth.le.ScanSettings settings =
                new android.bluetooth.le.ScanSettings.Builder()
                        .setScanMode(android.bluetooth.le.ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .build();


        Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): empezamos a escanear buscando: " + dispositivoBuscado );
        // Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): empezamos a escanear buscando: " + dispositivoBuscado + " -> " + Utilidades.stringToUUID( dispositivoBuscado ) );

        this.elEscanner.startScan(filtros, settings, this.callbackDelEscaneo );
    } // ()

// --------------------------------------------------------------
    // detenerBusquedaDispositivosBTLE()
    // Descripción: Detiene el escaneo de dispositivos BLE activos.
    // Diseño: -> detenerBusquedaDispositivosBTLE() -> vacío
    // Parámetros: ninguno
// --------------------------------------------------------------
    private void detenerBusquedaDispositivosBTLE() {
        Log.d(ETIQUETA_LOG, " Intentando detener la búsqueda de dispositivos BTLE...");

        if (this.elEscanner == null) {
            Log.d(ETIQUETA_LOG, " No hay escáner inicializado.");
            return;
        }

        if (this.callbackDelEscaneo == null) {
            Log.d(ETIQUETA_LOG, " No hay escaneo activo para detener.");
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            Log.d(ETIQUETA_LOG, " Permiso BLUETOOTH_SCAN no concedido.");
            return;
        }

        try {
            this.elEscanner.stopScan(this.callbackDelEscaneo);
            Log.d(ETIQUETA_LOG, " Escaneo detenido correctamente.");
        } catch (Exception e) {
            Log.e(ETIQUETA_LOG, " Error al detener el escaneo", e);
        }

        this.callbackDelEscaneo = null;
        this.escaneando = false;

    }


// --------------------------------------------------------------
    // botonBuscarDispositivosBTLEPulsado()
    // Descripción: Acción del botón para iniciar búsqueda BLE.
    // Diseño: vista:View -> botonBuscarDispositivosBTLEPulsado() -> vacío
    // Parámetros: v : vista del botón
// --------------------------------------------------------------
    public void botonBuscarDispositivosBTLEPulsado(View v) {
        Log.d(ETIQUETA_LOG, " boton buscar dispositivos BTLE Pulsado");
        this.buscarTodosLosDispositivosBTLE();
    }
// --------------------------------------------------------------
    // botonBuscarNuestroDispositivoBTLEPulsado()
    // Descripción: Busca únicamente el dispositivo “GTI-Mery”.
    // Diseño: vista:View -> botonBuscarNuestroDispositivoBTLEPulsado() -> vacío
    // Parámetros: v : vista del botón
// --------------------------------------------------------------
    public void botonBuscarNuestroDispositivoBTLEPulsado( View v ) {
        Log.d(ETIQUETA_LOG, " boton nuestro dispositivo BTLE Pulsado" );
        this.buscarEsteDispositivoBTLE( "GTI-Mery");

    } // ()
// --------------------------------------------------------------
    // botonDetenerBusquedaDispositivosBTLEPulsado()
    // Descripción: Acción del botón para detener búsqueda BLE.
    // Diseño: vista:View -> botonDetenerBusquedaDispositivosBTLEPulsado() -> vacío
    // Parámetros: v : vista del botón
// --------------------------------------------------------------
    public void botonDetenerBusquedaDispositivosBTLEPulsado(View v) {
        Log.d(ETIQUETA_LOG, " Botón detener búsqueda pulsado");
        detenerBusquedaDispositivosBTLE();
    }


// --------------------------------------------------------------
    // inicializarBlueTooth()
    // Descripción: Inicializa el adaptador Bluetooth y permisos.
    // Diseño: -> inicializarBlueTooth() -> vacío
    // Parámetros: ninguno
// --------------------------------------------------------------
    private void inicializarBlueTooth() {
        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): obtenemos adaptador BT ");
        BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (!bta.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        this.elEscanner = bta.getBluetoothLeScanner();

        if (this.elEscanner == null) {
            Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): NO hemos obtenido escaner btle");
        }

        checkAndRequestPermissions();
    }
// --------------------------------------------------------------
    // checkAndRequestPermissions()
    // Descripción: Comprueba y solicita permisos BLE necesarios.
    // Diseño: -> checkAndRequestPermissions() -> booleano
    // Parámetros: ninguno
// --------------------------------------------------------------
    private boolean checkAndRequestPermissions() {
        String[] permisos = {
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        boolean todosConcedidos = true;
        for (String p : permisos) {
            if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                todosConcedidos = false;
            }
        }
        if (!todosConcedidos) {
            ActivityCompat.requestPermissions(this, permisos, CODIGO_PETICION_PERMISOS);
            return false;
        }
        return true;
    }
// --------------------------------------------------------------
    // onActivityResult()
    // Descripción: Gestiona el resultado al habilitar Bluetooth.
    // Diseño: entero:requestCode, entero:resultCode, Intent:data -> onActivityResult() -> vacío
    // Parámetros: requestCode, resultCode, data
// --------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                Log.d(ETIQUETA_LOG, "Bluetooth enabled by user");
                BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();
                elEscanner = bta.getBluetoothLeScanner();
            } else {
                Log.d(ETIQUETA_LOG, "Bluetooth NOT enabled by user");
            }
        }
    }

// --------------------------------------------------------------
    // onCreate()
    // Descripción: Configura la actividad e inicia Bluetooth.
    // Diseño: Bundle:savedInstanceState -> onCreate() -> vacío
    // Parámetros: savedInstanceState
// --------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializarBlueTooth();
    }

// --------------------------------------------------------------
    // onRequestPermissionsResult()
    // Descripción: Maneja el resultado de las solicitudes de permisos.
    // Diseño: entero:requestCode, lista[String]:permissions,lista[int]:grantResults -> onRequestPermissionsResult() -> vacío
    // Parámetros: requestCode, permissions, grantResults
// --------------------------------------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CODIGO_PETICION_PERMISOS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(ETIQUETA_LOG, " permisos concedidos");
                } else {
                    Log.d(ETIQUETA_LOG, " permisos NO concedidos");
                }
                return;
        }
    }
} // class
