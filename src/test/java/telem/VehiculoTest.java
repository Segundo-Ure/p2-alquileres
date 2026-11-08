package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.json.JSONObject;
import org.junit.Test;

/**
 * Pruebas unitarias de Vehiculo y de sus subclases
 * Turismo, Motocicleta y VehiculoIndustrial.
 */
// [Ej. 1] clase de pruebas añadida
public class VehiculoTest {

    // ---------------------------------------------------------------
    // Atributos comunes (Vehiculo)

    @Test
    public void constructorGuardaAtributosComunes() {
        Vehiculo v = new Turismo("TNA2020", Categoria.NORMAL, 2020, 5, 450.0);

        assertEquals("TNA2020", v.getMatricula());
        assertEquals(Categoria.NORMAL, v.getCategoria());
        assertEquals(2020, v.getAnnio());
    }

    @Test
    public void setDisponibleCambiaLaDisponibilidad() {
        Vehiculo v = new Motocicleta("MNA2021", Categoria.NORMAL, 2021, 200);

        v.setDisponible(true);
        assertTrue(v.isDisponible());

        v.setDisponible(false);
        assertFalse(v.isDisponible());
    }

    // ---------------------------------------------------------------
    // Atributos específicos de cada subclase

    @Test
    public void turismoGuardaPlazasYMaletero() {
        Turismo t = new Turismo("TSA2024", Categoria.SUPERIOR, 2024, 7, 550.5);

        assertEquals(7, t.getPlazas());
        assertEquals(550.5, t.getCapacidadMaletero(), 0.0001);
    }

    @Test
    public void motocicletaGuardaCilindrada() {
        Motocicleta m = new Motocicleta("MSA2021", Categoria.SUPERIOR, 2021, 500);

        assertEquals(500, m.getCilindrada());
    }

    @Test
    public void vehiculoIndustrialGuardaCargaYEjes() {
        VehiculoIndustrial vi = new VehiculoIndustrial("CNA2019", Categoria.NORMAL, 2019, 1400, 2);

        assertEquals(1400, vi.getCapacidadCarga());
        assertEquals(2, vi.getNumeroEjes());
    }

    // ---------------------------------------------------------------
    // getTipo

    @Test
    public void getTipoDevuelveElNombreDeLaSubclase() {
        assertEquals("Turismo",
            new Turismo("T", Categoria.NORMAL, 2020, 5, 400.0).getTipo());
        assertEquals("Motocicleta",
            new Motocicleta("M", Categoria.NORMAL, 2020, 125).getTipo());
        assertEquals("VehiculoIndustrial",
            new VehiculoIndustrial("C", Categoria.NORMAL, 2020, 1000, 2).getTipo());
    }

    @Test
    public void getTipoCoincideConElNombreDeLaClase() {
        Vehiculo[] vehiculos = {
            new Turismo("T", Categoria.NORMAL, 2020, 5, 400.0),
            new Motocicleta("M", Categoria.NORMAL, 2020, 125),
            new VehiculoIndustrial("C", Categoria.NORMAL, 2020, 1000, 2)
        };

        // La factoría de deserialización dependerá de esta coincidencia
        for (Vehiculo v : vehiculos) {
            assertEquals(v.getClass().getSimpleName(), v.getTipo());
        }
    }

    // ---------------------------------------------------------------
    // getPrecioBase (todas las combinaciones tipo x categoría)

    @Test
    public void precioBaseTurismoSegunCategoria() {
        assertEquals(50, new Turismo("T", Categoria.NORMAL, 2020, 5, 400.0).getPrecioBase());
        assertEquals(80, new Turismo("T", Categoria.ESPECIAL, 2020, 5, 400.0).getPrecioBase());
        assertEquals(100, new Turismo("T", Categoria.SUPERIOR, 2020, 5, 400.0).getPrecioBase());
    }

    @Test
    public void precioBaseMotocicletaSegunCategoria() {
        assertEquals(30, new Motocicleta("M", Categoria.NORMAL, 2020, 125).getPrecioBase());
        assertEquals(50, new Motocicleta("M", Categoria.ESPECIAL, 2020, 125).getPrecioBase());
        assertEquals(70, new Motocicleta("M", Categoria.SUPERIOR, 2020, 125).getPrecioBase());
    }

    @Test
    public void precioBaseVehiculoIndustrialSegunCategoria() {
        assertEquals(80, new VehiculoIndustrial("C", Categoria.NORMAL, 2020, 1000, 2).getPrecioBase());
        assertEquals(120, new VehiculoIndustrial("C", Categoria.ESPECIAL, 2020, 1000, 2).getPrecioBase());
        assertEquals(150, new VehiculoIndustrial("C", Categoria.SUPERIOR, 2020, 1000, 2).getPrecioBase());
    }

    @Test
    public void todosLosPreciosBaseSonPositivos() {
        for (Categoria c : Categoria.values()) {
            assertTrue(new Turismo("T", c, 2020, 5, 400.0).getPrecioBase() > 0);
            assertTrue(new Motocicleta("M", c, 2020, 125).getPrecioBase() > 0);
            assertTrue(new VehiculoIndustrial("C", c, 2020, 1000, 2).getPrecioBase() > 0);
        }
    }

    // ---------------------------------------------------------------
    // Serialización JSON (toString)

    @Test
    public void toStringTurismoIncluyeTipoYAtributos() {
        Turismo t = new Turismo("TEA2026", Categoria.ESPECIAL, 2026, 7, 650.0);

        JSONObject json = new JSONObject(t.toString());

        assertEquals("Turismo", json.getString("tipo"));
        assertEquals(80, json.getInt("precioBase"));
        assertEquals("ESPECIAL", json.getString("categoria"));
        assertEquals("TEA2026", json.getString("matricula"));
        assertEquals(2026, json.getInt("annio"));
        assertEquals(7, json.getInt("plazas"));
        assertEquals(650.0, json.getDouble("capacidadMaletero"), 0.0001);
        assertTrue(json.has("disponible"));
    }

    @Test
    public void toStringMotocicletaIncluyeTipoYCilindrada() {
        Motocicleta m = new Motocicleta("MEA2026", Categoria.ESPECIAL, 2026, 1000);

        JSONObject json = new JSONObject(m.toString());

        assertEquals("Motocicleta", json.getString("tipo"));
        assertEquals(1000, json.getInt("cilindrada"));
    }

    @Test
    public void toStringVehiculoIndustrialIncluyeTipoCargaYEjes() {
        VehiculoIndustrial vi = new VehiculoIndustrial("CEA2025", Categoria.ESPECIAL, 2025, 4400, 5);

        JSONObject json = new JSONObject(vi.toString());

        assertEquals("VehiculoIndustrial", json.getString("tipo"));
        assertEquals(4400, json.getInt("capacidadCarga"));
        assertEquals(5, json.getInt("numeroEjes"));
    }
}
