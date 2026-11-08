package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Pruebas de guardar y cargar los datos de GestorAlquileres en un fichero (ejercicio 5).
 */
// [Ej. 5] clase de pruebas añadida
public class GestorAlquileresFicheroTest {

    // Carpeta temporal que JUnit crea antes de cada test y borra al terminar
    @Rule
    public TemporaryFolder carpeta = new TemporaryFolder();

    private GestorAlquileres gestor;
    private String fichero;

    @Before
    public void setUp() throws IOException {
        gestor = new GestorAlquileres();
        fichero = new File(carpeta.getRoot(), "datos.json").getPath();
    }

    /** Añade al gestor dos alquileres de ejemplo y marca un vehículo como disponible. */
    private void anadirAlquileresDeEjemplo(GestorAlquileres g) {
        Cliente clara = g.obtenerClientes()[0];
        Cliente aitor = g.obtenerClientes()[1];
        Vehiculo turismo = g.obtenerVehiculos("Turismo")[0];
        Vehiculo camion = g.obtenerVehiculos("VehiculoIndustrial")[2];
        LocalDate inicio = LocalDate.of(2026, 11, 1);

        turismo.setDisponible(true);
        g.agregarAlquiler(new Alquiler(turismo, clara, inicio, inicio.plusDays(3)));
        g.agregarAlquiler(new Alquiler(camion, aitor, inicio.plusDays(5), inicio.plusDays(9)));
    }

    private static String leer(String ruta) throws IOException {
        return new String(Files.readAllBytes(new File(ruta).toPath()), StandardCharsets.UTF_8);
    }

    // ---------------------------------------------------------------
    // Guardar

    @Test
    public void guardarCreaUnFicheroJsonConTodosLosDatos() throws IOException {
        anadirAlquileresDeEjemplo(gestor);

        gestor.guardarEnFichero(fichero);

        JSONObject datos = new JSONObject(leer(fichero));
        assertEquals(6, datos.getJSONArray("clientes").length());
        assertEquals(9, datos.getJSONArray("vehiculos").length());
        assertEquals(2, datos.getJSONArray("alquileres").length());
    }

    @Test
    public void guardarSinAlquileresDejaElArrayVacio() throws IOException {
        gestor.guardarEnFichero(fichero);

        JSONObject datos = new JSONObject(leer(fichero));
        assertEquals(0, datos.getJSONArray("alquileres").length());
    }

    @Test
    public void guardarSobrescribeElFichero() throws IOException {
        anadirAlquileresDeEjemplo(gestor);
        gestor.guardarEnFichero(fichero);

        new GestorAlquileres().guardarEnFichero(fichero);

        JSONObject datos = new JSONObject(leer(fichero));
        assertEquals(0, datos.getJSONArray("alquileres").length());
    }

    // ---------------------------------------------------------------
    // Guardar y cargar (ida y vuelta)

    @Test
    public void cargarRecuperaLosMismosDatosQueSeGuardaron() throws IOException {
        anadirAlquileresDeEjemplo(gestor);
        gestor.guardarEnFichero(fichero);

        GestorAlquileres otro = new GestorAlquileres();
        otro.cargarDeFichero(fichero);

        // Si se vuelve a guardar, el contenido es equivalente al original
        String copia = new File(carpeta.getRoot(), "copia.json").getPath();
        otro.guardarEnFichero(copia);
        assertTrue(new JSONObject(leer(fichero)).similar(new JSONObject(leer(copia))));
    }

    @Test
    public void cargarRecuperaClientesVehiculosYAlquileres() throws IOException {
        anadirAlquileresDeEjemplo(gestor);
        gestor.guardarEnFichero(fichero);

        GestorAlquileres otro = new GestorAlquileres();
        otro.cargarDeFichero(fichero);

        assertEquals(6, otro.obtenerClientes().length);
        assertEquals("Clara Videncia", otro.obtenerClientes()[0].getNombre());
        assertEquals(3, otro.obtenerVehiculos("Turismo").length);
        assertEquals(3, otro.obtenerVehiculos("Motocicleta").length);
        assertEquals(3, otro.obtenerVehiculos("VehiculoIndustrial").length);

        Alquiler[] alquileres = otro.obtenerAlquileres();
        assertEquals("TNA2020", alquileres[0].getVehiculo().getMatricula());
        assertEquals(LocalDate.of(2026, 11, 1), alquileres[0].getFechaInicio());
        assertEquals("CEA2025", alquileres[1].getVehiculo().getMatricula());
        assertNull(alquileres[2]);
    }

    @Test
    public void cargarConservaLaDisponibilidadDeLosVehiculos() throws IOException {
        anadirAlquileresDeEjemplo(gestor);   // marca el turismo TNA2020 como disponible
        gestor.obtenerVehiculos("Turismo")[1].setDisponible(false); // [Ej. 6] línea añadida: y otro como no disponible
        gestor.guardarEnFichero(fichero);
        GestorAlquileres otro = new GestorAlquileres();
        otro.cargarDeFichero(fichero);

        assertTrue(otro.obtenerVehiculos("Turismo")[0].isDisponible());
        assertFalse(otro.obtenerVehiculos("Turismo")[1].isDisponible());
    }

    @Test
    public void losAlquileresCargadosApuntanALosObjetosDelGestor() throws IOException {
        anadirAlquileresDeEjemplo(gestor);
        gestor.guardarEnFichero(fichero);

        GestorAlquileres otro = new GestorAlquileres();
        otro.cargarDeFichero(fichero);

        // No son copias sueltas: son los mismos objetos que están en las listas del gestor
        Alquiler primero = otro.obtenerAlquileres()[0];
        assertSame(otro.obtenerVehiculos("Turismo")[0], primero.getVehiculo());
        assertSame(otro.obtenerClientes()[0], primero.getCliente());
    }

    @Test
    public void cargarSustituyeLosDatosAnteriores() throws IOException {
        gestor.guardarEnFichero(fichero);    // fichero sin alquileres

        GestorAlquileres otro = new GestorAlquileres();
        anadirAlquileresDeEjemplo(otro);
        otro.cargarDeFichero(fichero);

        assertNull(otro.obtenerAlquileres()[0]);
    }

    @Test
    public void despuesDeCargarSePuedenAnadirAlquileresAlFinal() throws IOException {
        anadirAlquileresDeEjemplo(gestor);
        gestor.guardarEnFichero(fichero);

        GestorAlquileres otro = new GestorAlquileres();
        otro.cargarDeFichero(fichero);
        Alquiler nuevo = new Alquiler(otro.obtenerVehiculos("Motocicleta")[0], otro.obtenerClientes()[2],
            LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 2));
        otro.agregarAlquiler(nuevo);

        // El contador de alquileres se ha actualizado: el nuevo va detrás de los cargados
        assertSame(nuevo, otro.obtenerAlquileres()[2]);
    }

    // ---------------------------------------------------------------
    // Errores

    @Test(expected = IOException.class)
    public void cargarFicheroInexistenteLanzaIOException() throws IOException {
        gestor.cargarDeFichero(new File(carpeta.getRoot(), "no_existe.json").getPath());
    }

    @Test
    public void cargarFicheroCorruptoNoModificaLosDatos() throws IOException {
        anadirAlquileresDeEjemplo(gestor);
        Files.write(new File(fichero).toPath(), "esto no es JSON".getBytes(StandardCharsets.UTF_8));

        try {
            gestor.cargarDeFichero(fichero);
            fail("Debería haber lanzado JSONException");
        } catch (JSONException e) {
            // El gestor conserva sus alquileres
            assertEquals("TNA2020", gestor.obtenerAlquileres()[0].getVehiculo().getMatricula());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void cargarVehiculoDeTipoDesconocidoLanzaExcepcion() throws IOException {
        String json = "{\"clientes\":[],\"alquileres\":[],\"vehiculos\":[{\"tipo\":\"Avion\","
            + "\"matricula\":\"X\",\"categoria\":\"NORMAL\",\"annio\":2020,\"disponible\":true}]}";
        Files.write(new File(fichero).toPath(), json.getBytes(StandardCharsets.UTF_8));

        gestor.cargarDeFichero(fichero);
    }
}
