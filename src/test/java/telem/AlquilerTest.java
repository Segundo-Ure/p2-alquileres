package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas unitarias de la clase Alquiler.
 */
public class AlquilerTest {

    private Vehiculo turismo;
    private Cliente cliente;
    private LocalDate inicio;

    @Before
    public void setUp() {
        turismo = new Turismo("TNA2020", Categoria.NORMAL, 2020, 5, 450.0);
        cliente = new Cliente("Clara Videncia", "001");
        // Fecha fija para que las pruebas no dependan del día en que se ejecutan
        inicio = LocalDate.of(2026, 10, 9);
    }

    @Test
    public void constructorGuardaTodosLosDatos() {
        LocalDate fin = inicio.plusDays(3);
        Alquiler alquiler = new Alquiler(turismo, cliente, inicio, fin);

        assertSame(turismo, alquiler.getVehiculo());
        assertSame(cliente, alquiler.getCliente());
        assertEquals(inicio, alquiler.getFechaInicio());
        assertEquals(fin, alquiler.getFechaFin());
    }

    // ---------------------------------------------------------------
    // calcularPrecioAlquiler: se cuenta también el día de inicio (+1)

    @Test
    public void precioAlquilerDeUnSoloDia() {
        Alquiler alquiler = new Alquiler(turismo, cliente, inicio, inicio);

        assertEquals(1 * Alquiler.PRECIO_BASE,
            alquiler.calcularPrecioAlquiler(inicio, inicio));
    }

    @Test
    public void precioAlquilerIncluyeElDiaDeInicio() {
        LocalDate fin = inicio.plusDays(3);
        Alquiler alquiler = new Alquiler(turismo, cliente, inicio, fin);

        // Del 9 al 12 de octubre son 4 días: 9, 10, 11 y 12
        assertEquals(4 * Alquiler.PRECIO_BASE,
            alquiler.calcularPrecioAlquiler(inicio, fin));
    }

    @Test
    public void precioAlquilerQueCruzaDeMes() {
        LocalDate ini = LocalDate.of(2026, 10, 30);
        LocalDate fin = LocalDate.of(2026, 11, 2);
        Alquiler alquiler = new Alquiler(turismo, cliente, ini, fin);

        // 30, 31, 1 y 2: 4 días
        assertEquals(4 * Alquiler.PRECIO_BASE,
            alquiler.calcularPrecioAlquiler(ini, fin));
    }

    @Test
    public void precioAlquilerQueCruzaCambioDeHora() {
        // El 25/10/2026 se cambia la hora en España: con Date esto fallaba
        LocalDate ini = LocalDate.of(2026, 10, 24);
        LocalDate fin = LocalDate.of(2026, 10, 26);
        Alquiler alquiler = new Alquiler(turismo, cliente, ini, fin);

        assertEquals(3 * Alquiler.PRECIO_BASE,
            alquiler.calcularPrecioAlquiler(ini, fin));
    }

    // ---------------------------------------------------------------
    // Registro estático de alquileres

    @Test
    public void agregarAlquilerLoRegistra() {
        Alquiler alquiler = new Alquiler(turismo, cliente, inicio, inicio.plusDays(1));

        Alquiler.agregarAlquiler(alquiler);

        // El array es estático y compartido entre pruebas: buscamos el alquiler
        boolean encontrado = false;
        for (Alquiler a : Alquiler.obtenerAlquileres()) {
            if (a == alquiler) {
                encontrado = true;
            }
        }
        assertTrue(encontrado);
    }

    // ---------------------------------------------------------------
    // Serialización JSON (toString)

    @Test
    public void toStringGeneraFechasEnFormatoIso() {
        Alquiler alquiler = new Alquiler(turismo, cliente, inicio, inicio.plusDays(3));

        JSONObject json = new JSONObject(alquiler.toString());

        assertEquals("2026-10-09", json.getString("fechaInicio"));
        assertEquals("2026-10-12", json.getString("fechaFin"));
        // Se pueden reconstruir con LocalDate.parse (útil para el ejercicio 3)
        assertEquals(inicio, LocalDate.parse(json.getString("fechaInicio")));
    }

    @Test
    public void toStringIncluyeClienteYVehiculoAnidados() {
        Alquiler alquiler = new Alquiler(turismo, cliente, inicio, inicio.plusDays(3));

        JSONObject json = new JSONObject(alquiler.toString());

        JSONObject jsonCliente = json.getJSONObject("cliente");
        assertEquals("001", jsonCliente.getString("dni"));

        JSONObject jsonVehiculo = json.getJSONObject("vehiculo");
        assertEquals("Turismo", jsonVehiculo.getString("tipo"));
        assertEquals("TNA2020", jsonVehiculo.getString("matricula"));
    }
}
