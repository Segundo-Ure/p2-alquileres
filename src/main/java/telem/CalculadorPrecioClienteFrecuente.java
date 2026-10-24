package telem;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Política cliente frecuente: 20% de descuento sobre el precio base los tres
 * primeros días y 40% de descuento a partir del cuarto día.
 */
public class CalculadorPrecioClienteFrecuente implements IcalculadorPrecio {

    private static final double Descuento_PrimerosDias = 0.20;
    private static final double Descuento_RestoDias = 0.40;
    private static final int PrimerosDias = 3;

    @Override
    public int calcularPrecioAlquiler(LocalDate fechaInicio, LocalDate fechaFin, Vehiculo vehiculo) {
        // +1 para incluir el día de inicio, igual que en Alquiler
        int dias = (int) ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;

        // Se reparten los días en dos tramos: los 3 primeros y el resto
        int diasPrimerTramo = Math.min(dias, PrimerosDias);
        int diasSegundoTramo = dias - diasPrimerTramo;

        double precioPrimerTramo = vehiculo.getPrecioBase() * (1 - Descuento_PrimerosDias);
        double precioSegundoTramo = vehiculo.getPrecioBase() * (1 - Descuento_RestoDias);

        double total = diasPrimerTramo * precioPrimerTramo + diasSegundoTramo * precioSegundoTramo;

        // Se redondea al entero más cercano porque el precio se devuelve en int
        return (int) Math.round(total);
    }
}