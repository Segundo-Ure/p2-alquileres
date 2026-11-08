package telem;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Política temporada alta: se duplica el precio base diario.
 */
// [Ej. 2] estrategia concreta añadida: temporada alta
public class CalculadorPrecioTemporadaAlta implements IcalculadorPrecio {

    @Override
    public int calcularPrecioAlquiler(LocalDate fechaInicio, LocalDate fechaFin, Vehiculo vehiculo) {
        // +1 para incluir el día de inicio, igual que en Alquiler
        int dias = (int) ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
        int precioDiario = vehiculo.getPrecioBase() * 2; //lo que haces es duplicar el precio base diario
        return precioDiario * dias;
    }
}