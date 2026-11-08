package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas unitarias de GestorAlquileres: métodos implementados en el código
 * de partida y métodos completados en el ejercicio 6.
 */
// [Ej. 1] clase de pruebas añadida (las secciones del ejercicio 6 se añadieron después)
public class GestorAlquileresTest {

    private GestorAlquileres gestor;

    @Before
    public void setUp() {
        gestor = new GestorAlquileres();
    }

    // ---------------------------------------------------------------
    // [Ej. 1] obtenerVehiculos(tipo)

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
    // [Ej. 1] agregarAlquiler / obtenerAlquileres

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

    // ---------------------------------------------------------------
    // [Ej. 6] obtenerVehiculos(tipo, categoria)

    @Test
    public void obtenerVehiculosPorTipoYCategoria() {
        Vehiculo[] resultado = gestor.obtenerVehiculos("Turismo", Categoria.SUPERIOR);

        assertEquals(1, resultado.length);
        assertEquals("TSA2024", resultado[0].getMatricula());
    }

    @Test
    public void cadaTipoTieneUnVehiculoDeCadaCategoria() {
        String[] tipos = {"Turismo", "Motocicleta", "VehiculoIndustrial"};

        for (String tipo : tipos) {
            for (Categoria categoria : Categoria.values()) {
                Vehiculo[] resultado = gestor.obtenerVehiculos(tipo, categoria);

                assertEquals(1, resultado.length);
                assertEquals(tipo, resultado[0].getTipo());
                assertEquals(categoria, resultado[0].getCategoria());
            }
        }
    }

    @Test
    public void obtenerVehiculosPorCategoriaDeTipoDesconocidoDevuelveArrayVacio() {
        assertEquals(0, gestor.obtenerVehiculos("Avion", Categoria.NORMAL).length);
    }

    // ---------------------------------------------------------------
    // [Ej. 6] disponibilidad inicial de los vehículos

    @Test
    public void todosLosVehiculosEstanDisponiblesAlEmpezar() {
        String[] tipos = {"Turismo", "Motocicleta", "VehiculoIndustrial"};

        for (String tipo : tipos) {
            for (Vehiculo v : gestor.obtenerVehiculos(tipo)) {
                assertTrue(v.isDisponible());
            }
        }
    }

    // ---------------------------------------------------------------
    // [Ej. 6] obtenerAlquileres(cliente)

    @Test
    public void clienteSinAlquileresDevuelveArrayVacio() {
        Cliente clara = gestor.obtenerClientes()[0];

        // Nunca null: así el menú puede recorrerlo sin comprobaciones
        assertEquals(0, gestor.obtenerAlquileres(clara).length);
    }

    @Test
    public void obtenerAlquileresDevuelveSoloLosDelCliente() {
        Cliente clara = gestor.obtenerClientes()[0];
        Cliente aitor = gestor.obtenerClientes()[1];
        Vehiculo turismo = gestor.obtenerVehiculos("Turismo")[0];
        Vehiculo moto = gestor.obtenerVehiculos("Motocicleta")[0];
        LocalDate inicio = LocalDate.of(2026, 11, 1);

        Alquiler a1 = new Alquiler(turismo, clara, inicio, inicio.plusDays(2));
        Alquiler a2 = new Alquiler(moto, aitor, inicio, inicio.plusDays(4));
        Alquiler a3 = new Alquiler(moto, clara, inicio.plusDays(10), inicio.plusDays(12));
        gestor.agregarAlquiler(a1);
        gestor.agregarAlquiler(a2);
        gestor.agregarAlquiler(a3);

        Alquiler[] deClara = gestor.obtenerAlquileres(clara);
        assertEquals(2, deClara.length);
        assertSame(a1, deClara[0]);
        assertSame(a3, deClara[1]);

        Alquiler[] deAitor = gestor.obtenerAlquileres(aitor);
        assertEquals(1, deAitor.length);
        assertSame(a2, deAitor[0]);
    }

    @Test
    public void obtenerAlquileresIdentificaAlClientePorSuDni() {
        Cliente clara = gestor.obtenerClientes()[0];
        Vehiculo turismo = gestor.obtenerVehiculos("Turismo")[0];
        LocalDate inicio = LocalDate.of(2026, 11, 1);
        gestor.agregarAlquiler(new Alquiler(turismo, clara, inicio, inicio.plusDays(2)));

        // Otro objeto Cliente con el mismo DNI (como ocurre al cargar de un fichero)
        Cliente mismaClara = new Cliente("Clara Videncia", "001");

        assertEquals(1, gestor.obtenerAlquileres(mismaClara).length);
    }
}
