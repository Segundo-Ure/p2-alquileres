package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

/**
 * Pruebas de serialización/deserialización (ejercicio 3):
 * constructores a partir de JSON y FactoriaVehiculos.
 */
public class SerializacionTest {

    /** Dos JSON son equivalentes si tienen los mismos campos y valores, en cualquier orden. */
    private static void assertMismoJson(String esperado, String obtenido) {
        assertTrue("JSON distintos:\n" + esperado + "\n" + obtenido,
            new JSONObject(esperado).similar(new JSONObject(obtenido)));
    }

    // ---------------------------------------------------------------
    // Cliente

    @Test
    public void clienteIdaYVuelta() {
        Cliente original = new Cliente("Clara Videncia", "001");

        Cliente copia = new Cliente(original.toString());

        assertEquals("Clara Videncia", copia.getNombre());
        assertEquals("001", copia.getDni());
    }

    // ---------------------------------------------------------------
    // Constructores JSON de cada subclase

    @Test
    public void turismoIdaYVuelta() {
        Turismo original = new Turismo("TEA2026", Categoria.ESPECIAL, 2026, 7, 650.5);
        original.setDisponible(true);

        Turismo copia = new Turismo(original.toString());

        assertEquals("TEA2026", copia.getMatricula());
        assertEquals(Categoria.ESPECIAL, copia.getCategoria());
        assertEquals(2026, copia.getAnnio());
        assertTrue(copia.isDisponible());
        assertEquals(7, copia.getPlazas());
        assertEquals(650.5, copia.getCapacidadMaletero(), 0.0001);
    }

    @Test
    public void motocicletaIdaYVuelta() {
        Motocicleta original = new Motocicleta("MSA2021", Categoria.SUPERIOR, 2021, 500);

        Motocicleta copia = new Motocicleta(original.toString());

        assertMismoJson(original.toString(), copia.toString());
        assertEquals(500, copia.getCilindrada());
    }

    @Test
    public void vehiculoIndustrialIdaYVuelta() {
        VehiculoIndustrial original = new VehiculoIndustrial("CEA2025", Categoria.ESPECIAL, 2025, 4400, 5);

        VehiculoIndustrial copia = new VehiculoIndustrial(original.toString());

        assertMismoJson(original.toString(), copia.toString());
        assertEquals(4400, copia.getCapacidadCarga());
        assertEquals(5, copia.getNumeroEjes());
    }

    // ---------------------------------------------------------------
    // FactoriaVehiculos

    @Test
    public void factoriaCreaLaSubclaseCorrecta() {
        Vehiculo[] originales = {
            new Turismo("T", Categoria.NORMAL, 2020, 5, 400.0),
            new Motocicleta("M", Categoria.NORMAL, 2020, 125),
            new VehiculoIndustrial("C", Categoria.NORMAL, 2020, 1000, 2)
        };

        for (Vehiculo original : originales) {
            Vehiculo copia = FactoriaVehiculos.getVehiculo(original.toString());

            assertEquals(original.getClass(), copia.getClass());
            assertMismoJson(original.toString(), copia.toString());
        }
    }

    @Test
    public void factoriaConservaLaDisponibilidad() {
        Vehiculo original = new Motocicleta("M", Categoria.NORMAL, 2020, 125);
        original.setDisponible(true);

        assertTrue(FactoriaVehiculos.getVehiculo(original.toString()).isDisponible());
    }

    @Test(expected = IllegalArgumentException.class)
    public void factoriaRechazaTipoDesconocido() {
        String json = "{\"tipo\":\"Avion\",\"matricula\":\"X\",\"categoria\":\"NORMAL\","
            + "\"annio\":2020,\"disponible\":true}";

        FactoriaVehiculos.getVehiculo(json);
    }

    @Test(expected = JSONException.class)
    public void factoriaRechazaJsonSinTipo() {
        // Un Cliente no tiene campo "tipo": no es un vehículo
        FactoriaVehiculos.getVehiculo(new Cliente("Ana Lítica", "005").toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void categoriaInvalidaProvocaError() {
        String json = "{\"tipo\":\"Motocicleta\",\"matricula\":\"X\",\"categoria\":\"LUJO\","
            + "\"annio\":2020,\"disponible\":true,\"cilindrada\":125}";

        // Categoria.valueOf lanza IllegalArgumentException
        FactoriaVehiculos.getVehiculo(json);
    }

    // ---------------------------------------------------------------
    // Alquiler

    @Test
    public void alquilerIdaYVuelta() {
        Vehiculo vehiculo = new VehiculoIndustrial("CSA2025", Categoria.SUPERIOR, 2025, 2400, 3);
        Cliente cliente = new Cliente("Armando Guerra", "004");
        LocalDate inicio = LocalDate.of(2026, 11, 1);
        Alquiler original = new Alquiler(vehiculo, cliente, inicio, inicio.plusDays(6));

        Alquiler copia = new Alquiler(original.toString());

        assertMismoJson(original.toString(), copia.toString());
        assertTrue(copia.getVehiculo() instanceof VehiculoIndustrial);
        assertEquals("004", copia.getCliente().getDni());
        assertEquals(inicio, copia.getFechaInicio());
        assertEquals(LocalDate.of(2026, 11, 7), copia.getFechaFin());
    }

    @Test
    public void alquilerReconstruidoCalculaElMismoPrecio() {
        Vehiculo vehiculo = new Turismo("TNA2020", Categoria.NORMAL, 2020, 5, 450.0);
        LocalDate inicio = LocalDate.of(2026, 11, 1);
        LocalDate fin = inicio.plusDays(9);
        Alquiler original = new Alquiler(vehiculo, new Cliente("Clara Videncia", "001"), inicio, fin);

        Alquiler copia = new Alquiler(original.toString());

        for (PoliticasDePrecios p : PoliticasDePrecios.values()) {
            assertEquals(
                original.calcularPrecioAlquiler(inicio, fin, p, original.getVehiculo()),
                copia.calcularPrecioAlquiler(copia.getFechaInicio(), copia.getFechaFin(), p, copia.getVehiculo()));
        }
    }
}
