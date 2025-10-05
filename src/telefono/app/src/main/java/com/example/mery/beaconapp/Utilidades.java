package com.example.mery.beaconapp;
// ------------------------------------------------------------------
// Utilidades.java
// Descripción: Funciones auxiliares para conversión entre bytes, strings y UUIDs.
// ------------------------------------------------------------------

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

// -----------------------------------------------------------------------------------
// @author: Jordi Bataller i Mascarell
// -----------------------------------------------------------------------------------
public class Utilidades {

// --------------------------------------------------------------
    // stringToBytes()
    // Descripción: Convierte un texto a array de bytes.
    // Diseño: String -> stringToBytes() -> byte[]
// --------------------------------------------------------------
    public static byte[] stringToBytes ( String texto ) {
        return texto.getBytes();
        // byte[] b = string.getBytes(StandardCharsets.UTF_8); // Ja
    } // ()

// --------------------------------------------------------------
    // stringToUUID()
    // Descripción: Convierte un texto de 16 caracteres en un UUID.
    // Diseño: String -> stringToUUID() -> UUID
// --------------------------------------------------------------
    public static UUID stringToUUID( String uuid ) {
        if ( uuid.length() != 16 ) {
            throw new Error( "stringUUID: string no tiene 16 caracteres ");
        }
        byte[] comoBytes = uuid.getBytes();

        String masSignificativo = uuid.substring(0, 8);
        String menosSignificativo = uuid.substring(8, 16);
        UUID res = new UUID( Utilidades.bytesToLong( masSignificativo.getBytes() ), Utilidades.bytesToLong( menosSignificativo.getBytes() ) );

        // Log.d( MainActivity.ETIQUETA_LOG, " \n\n***** stringToUUID *** " + uuid  + "=?=" + Utilidades.uuidToString( res ) );

        // UUID res = UUID.nameUUIDFromBytes( comoBytes ); no va como quiero

        return res;
    } // ()

// --------------------------------------------------------------
    // uuidToString()
    // Descripción: Convierte un UUID a texto normal.
    // Diseño: UUID -> uuidToString() -> String
// --------------------------------------------------------------
    public static String uuidToString ( UUID uuid ) {
        return bytesToString( dosLongToBytes( uuid.getMostSignificantBits(), uuid.getLeastSignificantBits() ) );
    } // ()

// --------------------------------------------------------------
    // uuidToHexString()
    // Descripción: Convierte un UUID a texto hexadecimal.
    // Diseño: UUID -> uuidToHexString() -> String
// --------------------------------------------------------------
    public static String uuidToHexString ( UUID uuid ) {
        return bytesToHexString( dosLongToBytes( uuid.getMostSignificantBits(), uuid.getLeastSignificantBits() ) );
    } // ()


// --------------------------------------------------------------
    // bytesToString()
    // Descripción: Convierte bytes en texto legible.
    // Diseño: byte[] -> bytesToString() -> String
// --------------------------------------------------------------
    public static String bytesToString( byte[] bytes ) {
        if (bytes == null ) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append( (char) b );
        }
        return sb.toString();
    }

// --------------------------------------------------------------
    // dosLongToBytes()
    // Descripción: Combina dos long en un array de bytes.
    // Diseño: long,long -> dosLongToBytes() -> byte[]
// --------------------------------------------------------------
    public static byte[] dosLongToBytes( long masSignificativos, long menosSignificativos ) {
        ByteBuffer buffer = ByteBuffer.allocate( 2 * Long.BYTES );
        buffer.putLong( masSignificativos );
        buffer.putLong( menosSignificativos );
        return buffer.array();
    }


// --------------------------------------------------------------
    // bytesToInt()
    // Descripción: Convierte bytes en un entero.
    // Diseño: byte[] -> bytesToInt() -> int
// --------------------------------------------------------------
    public static int bytesToInt( byte[] bytes ) {
        return new BigInteger(bytes).intValue();
    }


// --------------------------------------------------------------
    // bytesToLong()
    // Descripción: Convierte bytes en un número largo.
    // Diseño: byte[] -> bytesToLong() -> long
// --------------------------------------------------------------
    public static long bytesToLong( byte[] bytes ) {
        return new BigInteger(bytes).longValue();
    }

// --------------------------------------------------------------
    // bytesToIntOK()
    // Descripción: Convierte bytes en int, validando longitud y signo.
    // Diseño: byte[] -> bytesToIntOK() -> int
// --------------------------------------------------------------
    public static int bytesToIntOK( byte[] bytes ) {
        if (bytes == null ) {
            return 0;
        }

        if ( bytes.length > 4 ) {
            throw new Error( "demasiados bytes para pasar a int ");
        }
        int res = 0;



        for( byte b : bytes ) {
           /*
           Log.d( MainActivity.ETIQUETA_LOG, "bytesToInt(): byte: hex=" + Integer.toHexString( b )
                   + " dec=" + b + " bin=" + Integer.toBinaryString( b ) +
                   " hex=" + Byte.toString( b )
           );
           */
            res =  (res << 8) // * 16
                    + (b & 0xFF); // para quedarse con 1 byte (2 cuartetos) de lo que haya en b
        } // for

        if ( (bytes[ 0 ] & 0x8) != 0 ) {
            // si tiene signo negativo (un 1 a la izquierda del primer byte
            res = -(~(byte)res)-1; // complemento a 2 (~) de res pero como byte, -1
        }
       /*
        Log.d( MainActivity.ETIQUETA_LOG, "bytesToInt(): res = " + res + " ~res=" + (res ^ 0xffff)
                + "~res=" + ~((byte) res)
        );
        */

        return res;
    } // ()

// --------------------------------------------------------------
    // bytesToHexString()
    // Descripción: Convierte bytes en formato hexadecimal.
    // Diseño: byte[] -> bytesToHexString() -> String
// --------------------------------------------------------------
    public static String bytesToHexString( byte[] bytes ) {

        if (bytes == null ) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
            sb.append(':');
        }
        return sb.toString();
    } // ()
} // class


