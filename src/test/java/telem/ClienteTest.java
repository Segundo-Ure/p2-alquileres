package telem;

import static org.junit.Assert.assertEquals;

import org.json.JSONObject;
import org.junit.Test;

/**
 * Pruebas unitarias de la clase Cliente.
 */
// [Ej. 1] clase de pruebas añadida
public class ClienteTest {

    @Test
    public void constructorGuardaNombreYDni() {
        Cliente cliente = new Cliente("Clara Videncia", "001");

        assertEquals("Clara Videncia", cliente.getNombre());
        assertEquals("001", cliente.getDni());
    }

    @Test
    public void toStringGeneraJsonConNombreYDni() {
        Cliente cliente = new Cliente("Aitor Menta", "002");

        JSONObject json = new JSONObject(cliente.toString());

        assertEquals("Aitor Menta", json.getString("nombre"));
        assertEquals("002", json.getString("dni"));
    }

    @Test
    public void toStringSoloContieneLosCamposConGetter() {
        Cliente cliente = new Cliente("Lola Mento", "003");

        JSONObject json = new JSONObject(cliente.toString());

        // Cliente no tiene getTipo, así que solo aparecen nombre y dni
        assertEquals(2, json.length());
    }
}
