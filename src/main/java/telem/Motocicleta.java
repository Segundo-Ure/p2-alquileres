package telem;

import org.json.JSONObject;  // [Ej. 3] para el constructor JSON

public class Motocicleta extends Vehiculo {

    int cilindrada;

    public Motocicleta(String matricula, Categoria categoria, int annio,
        int cilindrada
    ) {
        super(matricula, categoria, annio);
        this.cilindrada = cilindrada;
    }

    // [Ej. 3] constructor de deserialización desde JSON añadido
    /**
     * Deserialización: reconstruye una Motocicleta a partir de su representación JSON.
     * @param jsonString representación JSON de la motocicleta.
     */
    public Motocicleta(String jsonString) {
        super(jsonString);
        JSONObject json = new JSONObject(jsonString);
        this.cilindrada = json.getInt("cilindrada");
    }

    public int getCilindrada() {
        return cilindrada;
    }

    // [Fase 1] implementación de getTipo
    @Override
    public String getTipo() {
        return "Motocicleta";
    }

    // [Fase 1] implementación de getPrecioBase (precios elegidos por nosotros)
    @Override
    public int getPrecioBase() {
        int precioBase = 0;
        switch (getCategoria()) {
            case NORMAL:
                precioBase = 30;
                break;
            case ESPECIAL:
                precioBase = 50;
                break;
            case SUPERIOR:
                precioBase = 70;
                break;
        }
        return precioBase;
    }

}
