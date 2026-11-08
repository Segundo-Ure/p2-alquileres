package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;

import org.junit.Test;

/**
 * Pruebas unitarias de FactoriaCalculadorPrecio (ejercicio 4).
 */
// [Ej. 4] clase de pruebas añadida
public class FactoriaCalculadorPrecioTest {

    // ---------------------------------------------------------------
    // Cada política crea el calculador de su clase

    @Test
    public void normalCreaCalculadorPrecioNormal() {
        assertEquals(CalculadorPrecioNormal.class,
            FactoriaCalculadorPrecio.getCalculador(PoliticasDePrecios.Normal).getClass());
    }

    @Test
    public void temporadaAltaCreaCalculadorPrecioTemporadaAlta() {
        assertEquals(CalculadorPrecioTemporadaAlta.class,
            FactoriaCalculadorPrecio.getCalculador(PoliticasDePrecios.TemporadaAlta).getClass());
    }

    @Test
    public void ofertonCreaCalculadorPrecioOferton() {
        assertEquals(CalculadorPrecioOferton.class,
            FactoriaCalculadorPrecio.getCalculador(PoliticasDePrecios.Oferton).getClass());
    }

    @Test
    public void clienteFrecuenteCreaCalculadorPrecioClienteFrecuente() {
        assertEquals(CalculadorPrecioClienteFrecuente.class,
            FactoriaCalculadorPrecio.getCalculador(PoliticasDePrecios.ClienteFrecuente).getClass());
    }

    // ---------------------------------------------------------------
    // Ninguna política del enum se ha quedado sin calculador

    @Test
    public void todasLasPoliticasTienenCalculador() {
        // Si se añade una política al enum y no a la factoría, se lanzaría
        // IllegalArgumentException y este test fallaría
        for (PoliticasDePrecios politica : PoliticasDePrecios.values()) {
            assertNotNull(FactoriaCalculadorPrecio.getCalculador(politica));
        }
    }

    // ---------------------------------------------------------------
    // El calculador creado funciona

    @Test
    public void calculadorCreadoCalculaElPrecio() {
        Vehiculo turismo = new Turismo("1234ABC", Categoria.NORMAL, 2020, 5, 450.0);
        LocalDate inicio = LocalDate.of(2026, 11, 1);
        LocalDate fin = LocalDate.of(2026, 11, 10); // 10 días

        IcalculadorPrecio calculador = FactoriaCalculadorPrecio.getCalculador(PoliticasDePrecios.Oferton);

        // 35 euros/día * 10 = 350; más de 7 días -> -10% = 315
        assertEquals(315, calculador.calcularPrecioAlquiler(inicio, fin, turismo));
    }
}