package telem;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Política ofertón: 30% de descuento sobre el precio base diario y,
 * si el alquiler dura más de siete días será un 10% adicional sobre el total.
 */
public class CalculadorPrecioOferton implements IcalculadorPrecio {

    private static final double Descuento_Diario = 0.30; // 30% de descuento diario
    private static final double Descuento_Adi = 0.10; // 10% de descuento adicional si el alquiler dura más de 7 días
    private static final int Dia_Adi = 7; // mas de 7 días se aplica el descuento adicional

    @Override
    public int calcularPrecioAlquiler(LocalDate fechaInicio, LocalDate fechaFin, Vehiculo vehiculo) {
        // +1 para incluir el día de inicio, igual que en Alquiler
        int dias = (int) ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;

        double precioDiario = vehiculo.getPrecioBase() * (1 - Descuento_Diario);
        double total = precioDiario * dias;

        if (dias > Dia_Adi) {
            total = total * (1 - Descuento_Adi);
        }

        // esto es algo adicional que se hace para que el precio final sea un número
        // entero, ya que el método devuelve un int.
        // Se utiliza Math.round() para redondear el total al entero más cercano antes
        // de convertirlo a int y devolverlo.
        return (int) Math.round(total);
    }
}