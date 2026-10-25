package telem;

import java.time.LocalDate;

import org.json.JSONObject;

/**
 * Método Factoría para la deserialización de vehículos.
 * A partir de la representación JSON de un vehículo decide, mediante el
 * campo "tipo", qué subclase hay que crear y llama a su constructor.
 * El código que usa la factoría no necesita conocer las subclases.
 */
public class FactoriaVehiculos {

    /**
     * Crea el vehículo representado por un JSON.
     * @param jsonString representación JSON del vehículo (la que genera toString).
     * @return objeto de la subclase de Vehiculo indicada por el campo "tipo".
     * @throws IllegalArgumentException si el tipo de vehículo no se conoce.
     */
    public static Vehiculo getVehiculo(String jsonString) {
        JSONObject json = new JSONObject(jsonString);
        String clase = json.getString("tipo");
        switch (clase) {
            case "Turismo":
                return new Turismo(jsonString);
            case "Motocicleta":
                return new Motocicleta(jsonString);
            case "VehiculoIndustrial":
                return new VehiculoIndustrial(jsonString);
            default:
                throw new IllegalArgumentException("Tipo de vehículo desconocido: " + clase);
        }
    }

    public static void main(String[] args) {
        // Serializamos vehículos de los tres tipos y los reconstruimos con la factoría
        Vehiculo[] originales = {
            new Turismo("1234ABC", Categoria.NORMAL, 2020, 5, 450.0),
            new Motocicleta("5678DEF", Categoria.SUPERIOR, 2021, 500),
            new VehiculoIndustrial("9012GHI", Categoria.ESPECIAL, 2025, 4400, 5)
        };

        for (Vehiculo v1 : originales) {
            String serializado = v1.toString();
            Vehiculo v2 = FactoriaVehiculos.getVehiculo(serializado);

            System.out.println("Serializado:    " + serializado);
            System.out.println("Clase creada:   " + v2.getClass().getSimpleName());
            System.out.println("JSON idéntico:  " + serializado.equals(v2.toString()));
            System.out.println("--------------------------------------------------------");
        }

        // Un alquiler se reconstruye completo: vehículo, cliente y fechas
        Cliente cliente = new Cliente("Clara Videncia", "001");
        LocalDate hoy = LocalDate.of(2026, 11, 1);
        Alquiler a1 = new Alquiler(originales[1], cliente, hoy, hoy.plusDays(3));
        Alquiler a2 = new Alquiler(a1.toString());

        System.out.println("Alquiler serializado:  " + a1);
        System.out.println("Vehículo reconstruido: " + a2.getVehiculo().getClass().getSimpleName());
        System.out.println("JSON idéntico:         " + a1.toString().equals(a2.toString()));
    }
}
