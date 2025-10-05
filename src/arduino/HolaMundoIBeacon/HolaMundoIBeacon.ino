// -*-c++-*-

// --------------------------------------------------------------
//
// Jordi Bataller i Mascarell
// 2019-07-07
//
// --------------------------------------------------------------

// https://learn.sparkfun.com/tutorials/nrf52840-development-with-arduino-and-circuitpython

// https://stackoverflow.com/questions/29246805/can-an-ibeacon-have-a-data-payload

// --------------------------------------------------------------
// --------------------------------------------------------------
#include <bluefruit.h>

#undef min // eliminamos min/max definidos en bluefruit.h
#undef max // para evitar colisiones con la librería estándar

// --------------------------------------------------------------
// Incluimos clases auxiliares
// --------------------------------------------------------------
#include "LED.h"
#include "PuertoSerie.h"

// --------------------------------------------------------------
// Espacio de nombres Globales: objetos compartidos
// --------------------------------------------------------------
namespace Globales {
  
  LED elLED (7);                      // LED en pin 7
  PuertoSerie elPuerto ( 115200 );   // Puerto serie a 115200 baudios
  // Nota: Serial1 sería la conexión placa-sensor

};

// --------------------------------------------------------------
// Más clases necesarias
// --------------------------------------------------------------
#include "EmisoraBLE.h"
#include "Publicador.h"
#include "Medidor.h"


// --------------------------------------------------------------
// --------------------------------------------------------------
namespace Globales {
  Publicador elPublicador;   // Publicador BLE
  Medidor elMedidor;         // Medidor de sensores

}; // namespace

// --------------------------------------------------------------
// inicializarPlaquita() -> 
// Explicación: Inicializa la placa. De momento no hace nada.
// --------------------------------------------------------------
void inicializarPlaquita () {
  // de momento nada ;)
} // ()

// --------------------------------------------------------------
// setup() -> 
// Explicación: Se ejecuta al inicio. Configura puerto serie,emisora BLE, medidor, y escribe un mensaje final.
// --------------------------------------------------------------
void setup() {

  Globales::elPuerto.esperarDisponible();
  inicializarPlaquita();

 
  Globales::elPublicador.encenderEmisora();
  Globales::elMedidor.iniciarMedidor();

  esperar( 1000 );
  Globales::elPuerto.escribir( "---- setup(): fin ---- \n " );

} // setup ()

// --------------------------------------------------------------
// lucecitas() -> 
// Explicación: Hace parpadear el LED en varios intervalos (en ms) como secuencia de prueba visual.
// --------------------------------------------------------------
inline void lucecitas() {
  using namespace Globales;

  elLED.brillar( 100 );   esperar ( 400 ); 
  elLED.brillar( 100 );   esperar ( 400 ); 
  Globales::elLED.brillar( 100 );  esperar ( 400 ); 
  Globales::elLED.brillar( 1000 ); esperar ( 1000 ); 
} // ()

// --------------------------------------------------------------
// loop ()
// --------------------------------------------------------------
namespace Loop {
  uint8_t cont = 0;
};

// --------------------------------------------------------------
// --------------------------------------------------------------
void loop () {

  using namespace Loop;
  using namespace Globales;

  cont++;

  elPuerto.escribir( "\n---- loop(): empieza " );
  elPuerto.escribir( cont );
  elPuerto.escribir( "\n" );

  // LED parpadea
  lucecitas();

  // medir y publicar CO2
  int valorCO2 = elMedidor.medirCO2();
  elPublicador.publicarCO2( valorCO2,
							cont,
							1000 // intervalo de emisión
							);
  
  // medir y publicar Temperatura
  int valorTemperatura = elMedidor.medirTemperatura();
  
  elPublicador.publicarTemperatura( valorTemperatura, 
									cont,
									1000 // intervalo de emisión
									);

  // 
  // prueba para emitir un iBeacon y poner
  // en la carga (21 bytes = uuid 16 major 2 minor 2 txPower 1 )
  // lo que queramos (sin seguir dicho formato)
  // Al terminar la prueba hay que hacer Publicador::laEmisora privado
  // 
  char datos[21] = {
	'H', 'o', 'l', 'a',
	'H', 'o', 'l', 'a',
	'H', 'o', 'l', 'a',
	'H', 'o', 'l', 'a',
	'H', 'o', 'l', 'a',
	'H'
  };

  // elPublicador.laEmisora.emitirAnuncioIBeaconLibre ( &datos[0], 21 );
  // elPublicador.laEmisora.emitirAnuncioIBeaconLibre ( "MolaMolaMolaMolaMolaM", 21 );

  esperar(3000 );
  
  elPublicador.laEmisora.detenerAnuncio();
  
  
  elPuerto.escribir( "---- loop(): acaba **** " );
  elPuerto.escribir( cont );
  elPuerto.escribir( "\n" );
  
} // loop ()

