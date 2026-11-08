package telem;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Política normal: precio base diario x número de días de alquiler.
 */
// [Ej. 2] estrategia concreta añadida: política normal
public class CalculadorPrecioNormal implements IcalculadorPrecio {

    @Override
    public int calcularPrecioAlquiler(LocalDate fechaInicio, LocalDate fechaFin, Vehiculo vehiculo) {
        // +1 para incluir el día de inicio, igual que en Alquiler
        int dias = (int) ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
        return vehiculo.getPrecioBase() * dias;
    }
}