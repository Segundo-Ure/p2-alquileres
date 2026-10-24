package telem;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.json.JSONObject;

/**
 * Modela un alquiler de un vehículo por parte de un cliente, con fechas de
 * inicio y fin.
 * Almacena los alquileres realizados en un array estático y proporciona métodos
 * para calcular el precio del alquiler.
 * Alquiler
 */
public class Alquiler {

    private Vehiculo vehiculo;
    private Cliente cliente;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private IcalculadorPrecio calculadorPrecio;

    private static Alquiler[] alquileres = new Alquiler[1000];

    public static final int PRECIO_BASE = 50;
    private static int contadorAlquileres = 0;

    public Alquiler(Vehiculo vehiculo, Cliente cliente, LocalDate fechaInicio, LocalDate fechaFin) {
        this.vehiculo = vehiculo;
        this.cliente = cliente;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public static Alquiler[] obtenerAlquileres() {
        return alquileres;
    }

    public static void agregarAlquiler(Alquiler alquiler) {
        if (contadorAlquileres < alquileres.length) {
            alquileres[contadorAlquileres] = alquiler;
            contadorAlquileres++;
        } else {
            System.out.println("No se pueden agregar más alquileres. Capacidad máxima alcanzada.");
        }
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public int calcularPrecioAlquiler(LocalDate fechaInicio, LocalDate fechaFin) {
        // +1 to include the start day
        int dias = (int) ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
        return dias * PRECIO_BASE;
    }

    /**
     * Calcula el precio del alquiler aplicando la política de precios indicada.
     * Selecciona el calculador (estrategia) que corresponde a la política
     * y le delega el cálculo.
     * 
     * @param fechaInicio primer día del alquiler.
     * @param fechaFin    último día del alquiler.
     * @param politica    política de precios a aplicar.
     * @param vehiculo    vehículo alquilado.
     * @return precio total del alquiler.
     */
    public int calcularPrecioAlquiler(LocalDate fechaInicio, LocalDate fechaFin,
            PoliticasDePrecios politica, Vehiculo vehiculo) {

        switch (politica) {
            case Normal:
                calculadorPrecio = new CalculadorPrecioNormal();
                break;
            case TemporadaAlta:
                calculadorPrecio = new CalculadorPrecioTemporadaAlta();
                break;
            case Oferton:
                calculadorPrecio = new CalculadorPrecioOferton();
                break;
            case ClienteFrecuente:
                calculadorPrecio = new CalculadorPrecioClienteFrecuente();
                break;
            default:
                throw new IllegalArgumentException("Política de precios desconocida: " + politica);
        }

        return calculadorPrecio.calcularPrecioAlquiler(fechaInicio, fechaFin, vehiculo);
    }

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }

    public static void main(String[] args) {
        // Crear instancias de Vehiculo y Cliente
        Vehiculo vehiculo1 = new Turismo("ABC123", Categoria.NORMAL, 2020, 5, 400.0);
        Cliente cliente1 = new Cliente("Juan Pérez", "123456789");
        LocalDate hoy = LocalDate.now();
        Alquiler alquiler1 = new Alquiler(vehiculo1, cliente1, hoy, hoy.plusDays(3)); // Alquiler de 3 días

        System.out.println(alquiler1.toString());
        System.out.println("--------------------------------------------------------");

        Vehiculo vehiculo2 = new VehiculoIndustrial("ABC123", Categoria.NORMAL, 2020, 1000, 3);
        Alquiler alquiler2 = new Alquiler(vehiculo2, cliente1, hoy, hoy.plusDays(3)); // Alquiler de 3 días

        System.out.println(alquiler2.toString());
        System.out.println("--------------------------------------------------------");

        // Ejemplo de uso del patrón Estrategia: mismo alquiler, distintas políticas de
        // precios
        LocalDate inicio = LocalDate.of(2026, 11, 1);
        LocalDate fin = LocalDate.of(2026, 11, 10); // 10 días (del 1 al 10, ambos incluidos)
        Alquiler alquiler3 = new Alquiler(vehiculo1, cliente1, inicio, fin);

        System.out.println("Precio de alquilar " + vehiculo1.getTipo() + " " + vehiculo1.getCategoria()
                + " (" + vehiculo1.getPrecioBase() + " euros/día) del " + inicio + " al " + fin + ":");

        for (PoliticasDePrecios politica : PoliticasDePrecios.values()) {
            int precio = alquiler3.calcularPrecioAlquiler(inicio, fin, politica, vehiculo1);
            System.out.println("  " + politica + ": " + precio + " euros");
        }
        System.out.println("--------------------------------------------------------");
    }
}
