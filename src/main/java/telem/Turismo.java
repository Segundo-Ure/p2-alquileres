package telem;

import org.json.JSONObject;  // [Ej. 3] para el constructor JSON

public class Turismo extends Vehiculo {

    int plazas;
    double capacidadMaletero;

    public Turismo(String matricula, Categoria categoria, int annio,
        int plazas, double capacidadMaletero
    ) {
        super(matricula, categoria, annio);
        this.plazas = plazas;
        this.capacidadMaletero = capacidadMaletero;
    }

    // [Ej. 3] constructor de deserialización desde JSON añadido
    /**
     * Deserialización: reconstruye un Turismo a partir de su representación JSON.
     * @param jsonString representación JSON del turismo.
     */
    public Turismo(String jsonString) {
        super(jsonString);
        JSONObject json = new JSONObject(jsonString);
        this.plazas = json.getInt("plazas");
        this.capacidadMaletero = json.getDouble("capacidadMaletero");
    }

    public int getPlazas() {
        return plazas;
    }

    public double getCapacidadMaletero() {
        return capacidadMaletero;
    }

    // [Fase 1] implementación de getTipo
    @Override
    public String getTipo() {
        return "Turismo";
    }

    // [Fase 1] implementación de getPrecioBase (precios del guion)
    @Override
    public int getPrecioBase() {
        int precioBase = 0;
        switch (getCategoria()) {
            case NORMAL:
                precioBase = 50;
                break;
            case ESPECIAL:
                precioBase = 80;
                break;
            case SUPERIOR:
                precioBase = 100;
                break;
        }
        return precioBase;
    }
}
