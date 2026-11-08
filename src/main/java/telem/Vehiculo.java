package telem;

import org.json.JSONObject;

/**
 * Modela un vehículo con matrícula, categoría, año y disponibilidad.
 * La matrícula debe ser única para cada vehículo.
 * Vehiculo
 */
public abstract class Vehiculo {

    private String matricula;
    private Categoria categoria;
    private int annio;
    private boolean disponible;

    public Vehiculo(String matricula, Categoria categoria, int annio) {
        this.matricula = matricula;
        this.categoria = categoria;
        this.annio = annio;
    }

    // [Ej. 3] constructor JSON añadido (protected: solo lo llaman las subclases)
    /**
     * Deserialización: reconstruye los atributos comunes a partir de su
     * representación JSON. Las subclases lo invocan con super(jsonString).
     * @param jsonString representación JSON del vehículo (la que genera toString).
     */
    protected Vehiculo(String jsonString) {
        JSONObject json = new JSONObject(jsonString);
        this.matricula = json.getString("matricula");
        this.categoria = Categoria.valueOf(json.getString("categoria"));
        this.annio = json.getInt("annio");
        this.disponible = json.getBoolean("disponible");
    }

    public String getMatricula() {
        return matricula;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public int getAnnio() {
        return annio;
    }

    public boolean isDisponible() {
        return disponible;
    }
    
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    // [Fase 1] método abstracto añadido: cada subclase devuelve su tipo
    /**
     * En serialización/deserialización: para obtener el tipo de
     * vehículo (clase) del objeto.
     * @return El tipo de vehículo (sub-clase) del objeto.
     */
    public abstract String getTipo();

    // [Fase 1] método abstracto añadido: precio base diario de cada subclase
    /**
     * Calcula el precio base diario del vehículo según su categoría y tipo.
     * @return precio base diario del vehículo.
     */
    public abstract int getPrecioBase();

    @Override 
    public String toString() {
        return new JSONObject(this).toString();
    }

}
