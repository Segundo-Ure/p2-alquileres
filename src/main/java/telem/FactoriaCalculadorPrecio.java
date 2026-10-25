package telem;

import java.time.LocalDate;

/**
 * Método Factoría para las políticas de precios.
 * A partir de la política indicada decide qué calculador de precio
 * (estrategia) hay que crear y lo devuelve. El código que usa la factoría
 * solo conoce la interfaz IcalculadorPrecio, no las clases concretas.
 */
public class FactoriaCalculadorPrecio {

    /**
     * Crea el calculador de precio correspondiente a una política.
     * 
     * @param politica política de precios a aplicar.
     * @return calculador que implementa esa política.
     * @throws IllegalArgumentException si la política no se conoce.
     */
    public static IcalculadorPrecio getCalculador(PoliticasDePrecios politica) {
        switch (politica) {
            case Normal:
                return new CalculadorPrecioNormal();
            case TemporadaAlta:
                return new CalculadorPrecioTemporadaAlta();
            case Oferton:
                return new CalculadorPrecioOferton();
            case ClienteFrecuente:
                return new CalculadorPrecioClienteFrecuente();
            default:
                throw new IllegalArgumentException("Política de precios desconocida: " + politica);
        }
    }

    public static void main(String[] args) {
        // La factoría crea el calculador a partir de la política, sin que
        // quien la usa tenga que conocer las clases concretas
        Vehiculo turismo = new Turismo("1234ABC", Categoria.NORMAL, 2020, 5, 450.0);
        LocalDate inicio = LocalDate.of(2026, 11, 1);
        LocalDate fin = LocalDate.of(2026, 11, 10); // 10 días

        System.out.println("Alquiler de " + turismo.getTipo() + " " + turismo.getCategoria()
                + " del " + inicio + " al " + fin + ":");

        for (PoliticasDePrecios politica : PoliticasDePrecios.values()) {
            IcalculadorPrecio calculador = FactoriaCalculadorPrecio.getCalculador(politica);
            System.out.println("  " + politica + " -> " + calculador.getClass().getSimpleName()
                    + ": " + calculador.calcularPrecioAlquiler(inicio, fin, turismo) + " euros");
        }
    }

}