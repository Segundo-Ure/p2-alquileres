package telem;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas unitarias del patrón Estrategia (ejercicio 2):
 * las cuatro políticas de precios y su uso desde Alquiler.
 */
// [Ej. 2] clase de pruebas añadida
public class CalculadorPrecioTest {

    private Vehiculo turismoNormal;   // precio base 50
    private Vehiculo motoNormal;      // precio base 30
    private Cliente cliente;
    private LocalDate inicio;

    @Before
    public void setUp() {
        turismoNormal = new Turismo("TNA2020", Categoria.NORMAL, 2020, 5, 450.0);
        motoNormal = new Motocicleta("MNA2021", Categoria.NORMAL, 2021, 200);
        cliente = new Cliente("Clara Videncia", "001");
        // Fecha fija para que las pruebas no dependan del día en que se ejecutan
        inicio = LocalDate.of(2026, 11, 1);
    }

    /** Fecha de fin para un alquiler de n días (el día de inicio cuenta). */
    private LocalDate finTrasDias(int n) {
        return inicio.plusDays(n - 1);
    }

    // ---------------------------------------------------------------
    // Política normal: precio base x días

    @Test
    public void normalUnDia() {
        IcalculadorPrecio calc = new CalculadorPrecioNormal();
        assertEquals(50, calc.calcularPrecioAlquiler(inicio, inicio, turismoNormal));
    }

    @Test
    public void normalDiezDias() {
        IcalculadorPrecio calc = new CalculadorPrecioNormal();
        assertEquals(500, calc.calcularPrecioAlquiler(inicio, finTrasDias(10), turismoNormal));
    }

    @Test
    public void normalUsaElPrecioBaseDelVehiculo() {
        IcalculadorPrecio calc = new CalculadorPrecioNormal();
        // Moto normal: 30 euros/día
        assertEquals(300, calc.calcularPrecioAlquiler(inicio, finTrasDias(10), motoNormal));
    }

    // ---------------------------------------------------------------
    // Política temporada alta: se duplica el precio base diario

    @Test
    public void temporadaAltaDuplicaElPrecioDiario() {
        IcalculadorPrecio calc = new CalculadorPrecioTemporadaAlta();
        assertEquals(100, calc.calcularPrecioAlquiler(inicio, inicio, turismoNormal));
        assertEquals(1000, calc.calcularPrecioAlquiler(inicio, finTrasDias(10), turismoNormal));
    }

    // ---------------------------------------------------------------
    // Política ofertón: -30% diario y -10% extra si son más de 7 días

    @Test
    public void ofertonAplicaTreintaPorCientoDeDescuento() {
        IcalculadorPrecio calc = new CalculadorPrecioOferton();
        // 50 * 0,7 = 35 euros/día; 4 días = 140
        assertEquals(140, calc.calcularPrecioAlquiler(inicio, finTrasDias(4), turismoNormal));
    }

    @Test
    public void ofertonConSieteDiasNoTieneDescuentoExtra() {
        IcalculadorPrecio calc = new CalculadorPrecioOferton();
        // Caso límite: 7 días no es "más de siete" -> 35 * 7 = 245
        assertEquals(245, calc.calcularPrecioAlquiler(inicio, finTrasDias(7), turismoNormal));
    }

    @Test
    public void ofertonConOchoDiasTieneDescuentoExtra() {
        IcalculadorPrecio calc = new CalculadorPrecioOferton();
        // 35 * 8 = 280; -10% -> 252
        assertEquals(252, calc.calcularPrecioAlquiler(inicio, finTrasDias(8), turismoNormal));
    }

    @Test
    public void ofertonRedondeaAlEnteroMasCercano() {
        IcalculadorPrecio calc = new CalculadorPrecioOferton();
        // Moto: 30 * 0,7 = 21 euros/día; 21 * 8 = 168; -10% -> 151,2 -> 151
        assertEquals(151, calc.calcularPrecioAlquiler(inicio, finTrasDias(8), motoNormal));
    }

    // ---------------------------------------------------------------
    // Política cliente frecuente: -20% los 3 primeros días, -40% desde el 4º

    @Test
    public void clienteFrecuenteMenosDeTresDias() {
        IcalculadorPrecio calc = new CalculadorPrecioClienteFrecuente();
        // 2 días a 40 euros
        assertEquals(80, calc.calcularPrecioAlquiler(inicio, finTrasDias(2), turismoNormal));
    }

    @Test
    public void clienteFrecuenteTresDias() {
        IcalculadorPrecio calc = new CalculadorPrecioClienteFrecuente();
        // Caso límite: 3 días a 40 euros, todavía sin el -40%
        assertEquals(120, calc.calcularPrecioAlquiler(inicio, finTrasDias(3), turismoNormal));
    }

    @Test
    public void clienteFrecuenteCuatroDias() {
        IcalculadorPrecio calc = new CalculadorPrecioClienteFrecuente();
        // 3 días a 40 + 1 día a 30
        assertEquals(150, calc.calcularPrecioAlquiler(inicio, finTrasDias(4), turismoNormal));
    }

    @Test
    public void clienteFrecuenteDiezDias() {
        IcalculadorPrecio calc = new CalculadorPrecioClienteFrecuente();
        // 3 días a 40 + 7 días a 30
        assertEquals(330, calc.calcularPrecioAlquiler(inicio, finTrasDias(10), turismoNormal));
    }

    // ---------------------------------------------------------------
    // Alquiler (contexto): elige la estrategia según la política

    @Test
    public void alquilerAplicaCadaPolitica() {
        LocalDate fin = finTrasDias(10);
        Alquiler alquiler = new Alquiler(turismoNormal, cliente, inicio, fin);

        assertEquals(500, alquiler.calcularPrecioAlquiler(inicio, fin, PoliticasDePrecios.Normal, turismoNormal));
        assertEquals(1000, alquiler.calcularPrecioAlquiler(inicio, fin, PoliticasDePrecios.TemporadaAlta, turismoNormal));
        assertEquals(315, alquiler.calcularPrecioAlquiler(inicio, fin, PoliticasDePrecios.Oferton, turismoNormal));
        assertEquals(330, alquiler.calcularPrecioAlquiler(inicio, fin, PoliticasDePrecios.ClienteFrecuente, turismoNormal));
    }

}
