package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas unitarias de los métodos ya implementados de GestorAlquileres.
 * Los métodos "para hacer" se probarán en el ejercicio 6.
 */
public class GestorAlquileresTest {

    private GestorAlquileres gestor;

    @Before
    public void setUp() {
        gestor = new GestorAlquileres();
    }

    // ---------------------------------------------------------------
    // obtenerVehiculos(tipo)

    @Test
    public void obtenerTurismosDevuelveSoloTurismos() {
        Vehiculo[] turismos = gestor.obtenerVehiculos("Turismo");

        assertEquals(3, turismos.length);
        for (Vehiculo v : turismos) {
            assertEquals("Turismo", v.getTipo());
        }
    }

    @Test
    public void obtenerMotocicletasDevuelveSoloMotocicletas() {
        Vehiculo[] motos = gestor.obtenerVehiculos("Motocicleta");

        assertEquals(3, motos.length);
        for (Vehiculo v : motos) {
            assertEquals("Motocicleta", v.getTipo());
        }
    }

    @Test
    public void obtenerVehiculosIndustrialesDevuelveSoloIndustriales() {
        Vehiculo[] industriales = gestor.obtenerVehiculos("VehiculoIndustrial");

        assertEquals(3, industriales.length);
        for (Vehiculo v : industriales) {
            assertEquals("VehiculoIndustrial", v.getTipo());
        }
    }

    @Test
    public void obtenerVehiculosDeTipoDesconocidoDevuelveArrayVacio() {
        assertEquals(0, gestor.obtenerVehiculos("Avion").length);
    }

    // ---------------------------------------------------------------
    // agregarAlquiler / obtenerAlquileres

    @Test
    public void agregarAlquilerLoGuardaEnOrden() {
        Vehiculo v = gestor.obtenerVehiculos("Turismo")[0];
        Cliente c = new Cliente("Clara Videncia", "001");
        LocalDate hoy = LocalDate.of(2026, 10, 9);

        Alquiler a1 = new Alquiler(v, c, hoy, hoy.plusDays(2));
        Alquiler a2 = new Alquiler(v, c, hoy.plusDays(5), hoy.plusDays(7));
        gestor.agregarAlquiler(a1);
        gestor.agregarAlquiler(a2);

        Alquiler[] alquileres = gestor.obtenerAlquileres();
        assertSame(a1, alquileres[0]);
        assertSame(a2, alquileres[1]);
    }
}
