package telem;

import java.time.LocalDate;

/**
 * Estrategia para calcular el precio de un alquiler.
 * Cada política de precios (normal, temporada alta, ofertón, cliente frecuente)
 * es una clase distinta que implementa esta interfaz.
 */
public interface IcalculadorPrecio {

    /**
     * Calcula el precio total de un alquiler.
     * @param fechaInicio primer día del alquiler (se cobra).
     * @param fechaFin último día del alquiler (se cobra).
     * @param vehiculo vehículo alquilado; de él se obtiene el precio base diario.
     * @return precio total del alquiler.
     */
    int calcularPrecioAlquiler(LocalDate fechaInicio, LocalDate fechaFin, Vehiculo vehiculo);
}