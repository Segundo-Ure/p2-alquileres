package telem;

// [Ej. 5] imports de java.io, java.nio.file y org.json añadidos (LocalDate ya estaba)
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// [Fase 1] clase añadida desde el Aula Virtual tras fusionar refactor_fechas
public class GestorAlquileres {

    // [Ej. 5] constante añadida
    /** Fichero que se usa si el usuario no indica otro. */
    public static final String FICHERO_POR_DEFECTO = "alquileres.json";

    private int contadorAlquileres = 0;
    private Alquiler[] alquileres = new Alquiler[1000];

    // Para simplificar, vamos a definir un array de clientes y 
    // otro de vehículos dentro del gestor.
    // En una aplicación real estarían en una base de datos.
    private Cliente[] clientes = new Cliente[10];
    private Vehiculo[] vehiculos = new Vehiculo[100];


    public GestorAlquileres() {
        inicializarClientes();
        inicializarVehiculos();
    }

    /////////////////////////////////////////////////////////////////////////////////////
    // INICIALIZACIÓN DE CLIENTES Y VEHÍCULOS DE EJEMPLO

    // Definimos métodos privados para inicializar clientes y vehículos de ejemplo
    private void inicializarClientes() {
        clientes[0] = new Cliente("Clara Videncia", "001");
        clientes[1] = new Cliente("Aitor Menta", "002");
        clientes[2] = new Cliente("Lola Mento", "003");
        clientes[3] = new Cliente("Armando Guerra", "004");
        clientes[4] = new Cliente("Ana Lítica", "005");
        clientes[5] = new Cliente("Carmelo Cotón", "006");
    }

    private void inicializarVehiculos() {
        vehiculos[0] = new Turismo("TNA2020", Categoria.NORMAL, 2020, 5, 450.0);
        vehiculos[1] = new Motocicleta("MNA2021", Categoria.NORMAL, 2021, 200);
        vehiculos[2] = new VehiculoIndustrial("CNA2019", Categoria.NORMAL, 2019, 1400, 2);
        vehiculos[3] = new Turismo("TSA2024", Categoria.SUPERIOR, 2024, 5, 550.0);
        vehiculos[4] = new Motocicleta("MSA2021", Categoria.SUPERIOR, 2021, 500);
        vehiculos[5] = new VehiculoIndustrial("CSA2025", Categoria.SUPERIOR, 2025, 2400, 3);
        vehiculos[6] = new Turismo("TEA2026", Categoria.ESPECIAL, 2026, 7, 650.0);
        vehiculos[7] = new Motocicleta("MEA2026", Categoria.ESPECIAL, 2026, 1000);
        vehiculos[8] = new VehiculoIndustrial("CEA2025", Categoria.ESPECIAL, 2025, 4400, 5);

        // [Ej. 6] bucle añadido: al empezar, todos los vehículos están disponibles para alquilar
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo != null) {
                vehiculo.setDisponible(true);
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////
    // MÉTODOS PARA GESTIONAR VEHÍCULOS

     public Vehiculo[] obtenerVehiculos(String tipo) {
        
        Vehiculo[] vehiculosFiltrados = new Vehiculo[vehiculos.length];
        int contador = 0;

        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo != null) {
                if (vehiculo.getTipo().equals(tipo)) {
                    vehiculosFiltrados[contador++] = vehiculo;
                }
            }
        }

        // Ajustar el tamaño del array para que solo contenga los vehículos encontrados
        Vehiculo[] resultado = new Vehiculo[contador];
        System.arraycopy(vehiculosFiltrados, 0, resultado, 0, contador);
        return resultado;
    }   

    /**
     * Devuelve un array con los vehículos del tipo y categoría especificados.
     * @param tipo
     * @param categoria
     * @return lista de vehículos del tipo y categoría especificados
     */
    public Vehiculo[] obtenerVehiculos(String tipo, Categoria categoria) {
        // [Ej. 6] implementado (antes devolvía null)

        Vehiculo[] vehiculosFiltrados = new Vehiculo[vehiculos.length];
        int contador = 0;

        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo != null) {
                if (vehiculo.getTipo().equals(tipo) && vehiculo.getCategoria() == categoria) {
                    vehiculosFiltrados[contador++] = vehiculo;
                }
            }
        }

        // Ajustar el tamaño del array para que solo contenga los vehículos encontrados
        Vehiculo[] resultado = new Vehiculo[contador];
        System.arraycopy(vehiculosFiltrados, 0, resultado, 0, contador);
        return resultado;
    }

    /////////////////////////////////////////////////////////////////////////////////////
    // MÉTODOS PARA GESTIONAR ALQUILERES

    /**
     * Agrega un nuevo alquiler al array de alquileres.
     * @param alquiler
     */
    public void agregarAlquiler(Alquiler alquiler) {
        if (contadorAlquileres < alquileres.length) {
            alquileres[contadorAlquileres] = alquiler;
            contadorAlquileres++;
        } else {
            System.out.println("No se pueden agregar más alquileres. Capacidad máxima alcanzada.");
        }
    }

    /**
     * Devuelve un array con todos los alquileres registrados en el gestor.
     * @return
     */
    public Alquiler[] obtenerAlquileres() {
        return alquileres; 
    }

    /**
     * Devuelve el histórico de alquileres de un cliente.
     * @param cliente cliente cuyos alquileres se buscan (se identifica por su DNI).
     * @return array con los alquileres del cliente, vacío si no tiene ninguno.
     */
    public Alquiler[] obtenerAlquileres(Cliente cliente) {
        // [Ej. 6] implementado: busca por DNI (antes devolvía null)

        Alquiler[] alquileresCliente = new Alquiler[contadorAlquileres];
        int contador = 0;

        for (int i = 0; i < contadorAlquileres; i++) {
            if (alquileres[i].getCliente().getDni().equals(cliente.getDni())) {
                alquileresCliente[contador++] = alquileres[i];
            }
        }

        // Ajustar el tamaño del array para que solo contenga los alquileres encontrados
        Alquiler[] resultado = new Alquiler[contador];
        System.arraycopy(alquileresCliente, 0, resultado, 0, contador);
        return resultado;
    }

    /**
     * Informa si un cliente es cliente frecuente en función del número de alquileres previos.
     * @param cliente cliente a evaluar
     * @param umbral número mínimo de alquileres previos que se consideran para determinar si es cliente frecuente
     * @return
     */
    private boolean esClienteFrecuente(Cliente cliente, int umbral) {

        // [Ej. 6] implementado: es frecuente si tiene al menos 'umbral' alquileres previos
        return obtenerAlquileres(cliente).length >= umbral;
    }


    /////////////////////////////////////////////////////////////////////////////////////
    // PERSISTENCIA: GUARDAR Y RECUPERAR LOS DATOS EN UN FICHERO DE TEXTO (JSON)
    // [Ej. 5] sección añadida completa, hasta INTERFAZ DE USUARIO

    // Formato del fichero: un único objeto JSON con tres arrays
    //   {"clientes": [...], "vehiculos": [...], "alquileres": [...]}
    // Cada elemento es la representación JSON que genera el toString del objeto,
    // y se recupera con los constructores JSON y con FactoriaVehiculos.

    /**
     * Devuelve los clientes registrados en el gestor (sin huecos vacíos).
     * @return array con los clientes.
     */
    public Cliente[] obtenerClientes() {
        int contador = 0;
        for (Cliente c : clientes) {
            if (c != null) {
                contador++;
            }
        }
        Cliente[] resultado = new Cliente[contador];
        int i = 0;
        for (Cliente c : clientes) {
            if (c != null) {
                resultado[i++] = c;
            }
        }
        return resultado;
    }

    /**
     * Guarda los clientes, vehículos y alquileres del gestor en un fichero de texto
     * en formato JSON. Si el fichero existe, se sobrescribe.
     * @param nombreFichero ruta del fichero.
     * @throws IOException si no se puede escribir el fichero.
     */
    public void guardarEnFichero(String nombreFichero) throws IOException {
        JSONObject datos = new JSONObject();
        datos.put("clientes", aJsonArray(clientes));
        datos.put("vehiculos", aJsonArray(vehiculos));
        datos.put("alquileres", aJsonArray(alquileres));

        // toString(2): JSON con sangría de 2 espacios, para que el fichero sea legible
        Files.write(Paths.get(nombreFichero), datos.toString(2).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Recupera los clientes, vehículos y alquileres guardados en un fichero y
     * sustituye con ellos los datos actuales del gestor.
     * Si el fichero no es válido, el gestor conserva los datos que tenía.
     * @param nombreFichero ruta del fichero.
     * @throws IOException si no se puede leer el fichero.
     * @throws JSONException si el contenido no tiene el formato esperado.
     * @throws IllegalArgumentException si hay más datos de los que caben en el gestor
     *         o un vehículo de tipo desconocido.
     */
    public void cargarDeFichero(String nombreFichero) throws IOException {
        String contenido = new String(Files.readAllBytes(Paths.get(nombreFichero)), StandardCharsets.UTF_8);
        JSONObject datos = new JSONObject(contenido);
        JSONArray jsonClientes = datos.getJSONArray("clientes");
        JSONArray jsonVehiculos = datos.getJSONArray("vehiculos");
        JSONArray jsonAlquileres = datos.getJSONArray("alquileres");

        if (jsonClientes.length() > clientes.length
                || jsonVehiculos.length() > vehiculos.length
                || jsonAlquileres.length() > alquileres.length) {
            throw new IllegalArgumentException("El fichero contiene más datos de los que caben en el gestor");
        }

        // Se reconstruye todo en arrays nuevos y solo al final se sustituyen los del
        // gestor: si algo falla a mitad, el gestor no se queda con datos a medias.
        Cliente[] nuevosClientes = new Cliente[clientes.length];
        for (int i = 0; i < jsonClientes.length(); i++) {
            nuevosClientes[i] = new Cliente(jsonClientes.getJSONObject(i).toString());
        }

        Vehiculo[] nuevosVehiculos = new Vehiculo[vehiculos.length];
        for (int i = 0; i < jsonVehiculos.length(); i++) {
            nuevosVehiculos[i] = FactoriaVehiculos.getVehiculo(jsonVehiculos.getJSONObject(i).toString());
        }

        Alquiler[] nuevosAlquileres = new Alquiler[alquileres.length];
        for (int i = 0; i < jsonAlquileres.length(); i++) {
            Alquiler leido = new Alquiler(jsonAlquileres.getJSONObject(i).toString());

            // El alquiler leído trae sus propias copias del vehículo y del cliente.
            // Lo enlazamos con los objetos del gestor (misma matrícula / mismo DNI) para
            // que, p.e., al devolver el vehículo se actualice el de la lista de vehículos.
            Vehiculo vehiculo = buscarVehiculo(nuevosVehiculos, leido.getVehiculo().getMatricula());
            Cliente cliente = buscarCliente(nuevosClientes, leido.getCliente().getDni());

            nuevosAlquileres[i] = new Alquiler(
                vehiculo != null ? vehiculo : leido.getVehiculo(),
                cliente != null ? cliente : leido.getCliente(),
                leido.getFechaInicio(), leido.getFechaFin());
        }

        clientes = nuevosClientes;
        vehiculos = nuevosVehiculos;
        alquileres = nuevosAlquileres;
        contadorAlquileres = jsonAlquileres.length();
    }

    /** Convierte los objetos no nulos de un array en un JSONArray usando su toString. */
    private JSONArray aJsonArray(Object[] objetos) {
        JSONArray array = new JSONArray();
        for (Object o : objetos) {
            if (o != null) {
                array.put(new JSONObject(o.toString()));
            }
        }
        return array;
    }

    private Vehiculo buscarVehiculo(Vehiculo[] lista, String matricula) {
        for (Vehiculo v : lista) {
            if (v != null && v.getMatricula().equals(matricula)) {
                return v;
            }
        }
        return null;
    }

    private Cliente buscarCliente(Cliente[] lista, String dni) {
        for (Cliente c : lista) {
            if (c != null && c.getDni().equals(dni)) {
                return c;
            }
        }
        return null;
    }


    /////////////////////////////////////////////////////////////////////////////////////
    // INTERFAZ DE USUARIO (CONSOLA) Y LÓGICA DE NEGOCIO

    // La interfaz de usuario y la lógica de negocio suelen implementarse por separado.
    // Aquí se integran ambas por simplicidad, pero en una aplicación real hay que separarlas.


    public void menuPrincipal(){
        
        boolean salir = false;
        
        while(!salir){

            // Se da oportunidad de salir inmediatamente de la app.
            System.out.println("Gestíon de Alquileres");
            System.out.println("¿Desea salir de la app (S/N)?");

            char s = KeyBoard.readChar();
            if (s == 's' || s == 'S'){
                System.out.println("Bye ...");
                return;
            }

            // Primero se identifica al cliente:
            Cliente cliente = identificarCliente();

            // Ahora el cliente puede elegir una opción o salir de la aplicación.
            if (cliente != null) {
                System.out.println("Sr " + cliente.getNombre() + " Introduzca una opción");
                System.out.println("(1) seleccionar coche para alquilar");
                System.out.println("(2) entregar un coche alquilado");
                System.out.println("(3) listar su histórico de alquileres");
                // [Ej. 5] opciones 4 y 5 añadidas al menú
                System.out.println("(4) guardar los datos en un fichero");
                System.out.println("(5) cargar los datos desde un fichero");

                int opcion = KeyBoard.readInt();
                System.out.println("opción escogida: " + opcion);

                switch (opcion) {
                    case 1:
                        Vehiculo v = seleccionarVehiculo(cliente);
                        if (v.isDisponible()){
                            // para simplificar la IU se supone que el alquiler comienza "ahora"
                            // y el usuario especifica duración en días.
                            System.out.println("¿cuántos días desea alquilar el vehículo?");
                            int dias = KeyBoard.readInt();
                            LocalDate inicio = LocalDate.now();
                            LocalDate fin = inicio.plusDays(dias - 1); // [Ej. 6] dias - 1: el día de inicio también cuenta
                            // [Ej. 6] comprobación movida antes de registrar el alquiler
                            //Comprobamos si el cliente es usuario frecuente para aplicarle
                            // la tarifa que le corresponde. Se comprueba antes de registrar
                            // el alquiler nuevo para que solo cuenten los alquileres previos.
                            boolean usuarioFrecuente = this.esClienteFrecuente(cliente, 3);

                            // Creamos y registramos el alquiler
                            Alquiler q = new Alquiler(v, cliente, inicio, fin);
                            this.agregarAlquiler(q);

                            // [Ej. 6] el vehículo deja de estar disponible hasta que se entregue (opción 2)
                            v.setDisponible(false);

                            // Calculamos precio.
                            // Aquí habría que determinar la política de precios aplicable según el tipo de cliente y la fecha.
                            // Para simplificar, se aplicará la política de cliente frecuente si el cliente tiene más de 3 alquileres previos, 
                            // y la política normal en caso contrario.

                            if (usuarioFrecuente){

                                // aplicamos tarifa de cliente frecuente.
                                System.out.println("Alquiler realizado. Tarifa Usuario frecuente.");
                                // [Ej. 6] precio con la política de cliente frecuente
                                int precio = q.calcularPrecioAlquiler(inicio, fin, PoliticasDePrecios.ClienteFrecuente, v);
                                System.out.println("Precio del alquiler: " + precio + " euros");
                            }
                            else{
                                // aplicamos tarifa normal.
                                System.out.println("Alquiler realizado. Tarifa Normal.");

                                // [Ej. 6] precio con la política normal
                                int precio = q.calcularPrecioAlquiler(inicio, fin, PoliticasDePrecios.Normal, v);
                                System.out.println("Precio del alquiler: " + precio + " euros");
                            }
                            
                        }
                        else{
                            System.out.println("Debe seleccionar un vehículo disponible");
                        }
                        break;
                    case 2: 
                        // [Ej. 6] opción implementada (antes «opción no implementada»)
                        // Se buscan los vehículos que el cliente tiene alquilados y aún no ha entregado
                        Alquiler[] historial = obtenerAlquileres(cliente);
                        Vehiculo[] pendientes = new Vehiculo[historial.length];
                        int numPendientes = 0;
                        for (Alquiler a : historial) {
                            if (!a.getVehiculo().isDisponible()) {
                                pendientes[numPendientes++] = a.getVehiculo();
                            }
                        }

                        if (numPendientes == 0) {
                            System.out.println("No tiene ningún vehículo pendiente de entregar");
                        }
                        else {
                            System.out.println("Vehículos pendientes de entregar:");
                            for (int i = 0; i < numPendientes; i++) {
                                System.out.println(i + " ---> " + pendientes[i]);
                            }
                            System.out.println("Introduzca el número del vehículo que entrega:");
                            int numero = KeyBoard.readInt();
                            if (numero >= 0 && numero < numPendientes) {
                                // El vehículo vuelve a estar disponible para otros alquileres
                                pendientes[numero].setDisponible(true);
                                System.out.println("Vehículo " + pendientes[numero].getMatricula() + " entregado. Gracias.");
                            }
                            else {
                                System.out.println("Número de vehículo inválido");
                            }
                        }

                        break;              
                    case 3:
                        Alquiler alqs [] = obtenerAlquileres(cliente);
                        for (Alquiler q: alqs){
                            System.out.println(q);  // [Ej. 6] println en vez de print
                        }
                        break;  // [Ej. 5] break añadido: antes la opción 3 seguía al siguiente case
                    // [Ej. 5] opciones de guardar (4) y cargar (5)
                    case 4:
                        String ficheroGuardar = pedirNombreFichero();
                        try {
                            guardarEnFichero(ficheroGuardar);
                            System.out.println("Datos guardados en " + ficheroGuardar);
                        } catch (IOException e) {
                            System.out.println("No se ha podido guardar el fichero: " + e);
                        }
                        break;
                    case 5:
                        String ficheroCargar = pedirNombreFichero();
                        try {
                            cargarDeFichero(ficheroCargar);
                            System.out.println("Datos cargados desde " + ficheroCargar);
                        } catch (IOException | JSONException | IllegalArgumentException e) {
                            // El gestor conserva los datos que tenía
                            System.out.println("No se ha podido cargar el fichero: " + e);
                        }
                        break;
                    default:
                        break;
                }
            }
            else{
                System.out.println("Debe identificarse correctamente o salir de la app");
            }
        }
    }

    // [Ej. 5] método añadido
    /**
     * Pide por teclado el nombre de un fichero.
     * @return el nombre introducido, o FICHERO_POR_DEFECTO si se deja en blanco.
     */
    private String pedirNombreFichero() {
        System.out.println("Nombre del fichero (Intro para usar " + FICHERO_POR_DEFECTO + "):");
        String nombre = KeyBoard.readString();
        if (nombre == null || nombre.trim().isEmpty()) {
            return FICHERO_POR_DEFECTO;
        }
        return nombre.trim();
    }

    public Cliente identificarCliente(){

        boolean seleccionado = false;
        Cliente cliente = null;

        while (!seleccionado) {
        
            System.out.println("Bienvenido al Gestor de Alquileres");
            for (int i = 0; i < clientes.length; i++) {
                if (clientes[i] != null) {
                    System.out.println(i + " ---> " + clientes[i].getNombre() + " - " + clientes[i].getDni());
                }
            }
            System.out.println("Identífiquese como cliente con su número de cliente (-1 para salir):");
            int numeroCliente = KeyBoard.readInt();
            if (numeroCliente == -1) {
                return null;
            }
            else if (numeroCliente < 0 || numeroCliente >= clientes.length || clientes[numeroCliente] == null) {
                System.out.println("identificarCliente:Número de cliente inválido.");
                return null;
            }
            else{
                seleccionado = true;
                cliente = clientes[numeroCliente];
            }
        }
        return cliente;
    }

    public Vehiculo seleccionarVehiculo(Cliente cliente){
        boolean seleccionado = false;
        Vehiculo vehiculo = null;

        while (!seleccionado) {
        
            System.out.println("Estimado Sr./Sra. " + cliente.getNombre());
            System.out.println("Introduzca el número correspondiente al tipo de vehículo que desea alquilar:");
            System.out.println("1. Turismo, 2. Motocicleta, 3. Vehículo Industrial: ");

            int tipoVehiculo = KeyBoard.readInt();
            String tipo = "";
            Vehiculo [] elegibles = new Vehiculo[100];

            switch (tipoVehiculo) {
                case 1:
                    System.out.println("Ha seleccionado Turismo.");
                    tipo = "Turismo";
                    break;
                case 2:
                    System.out.println("Ha seleccionado Motocicleta.");
                    tipo = "Motocicleta";
                    break;
                case 3:
                    System.out.println("Ha seleccionado Vehículo Industrial.");
                    tipo = "VehiculoIndustrial";  // [Ej. 6] sin tilde, igual que getTipo()
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, seleccione un tipo de vehículo válido.");
                    continue; // Volver a pedir la selección
            }

            System.out.println("Vehiculos disponibles");

            System.out.println("Estimado Sr./Sra. " + cliente.getNombre() + "\n seleccione categoría.");
            System.out.println("Introduzca el número correspondiente a la categoría que desea alquilar:");
            System.out.println("1. Normal, 2. Superior, 3. Especial, 4.Cualquiera: ");

            int categoriaVehiculo = KeyBoard.readInt();

            switch (categoriaVehiculo) {
                case 1:
                    System.out.println("Ha seleccionado Categoría Normal.");
                    elegibles = obtenerVehiculos(tipo, Categoria.NORMAL);
                    break;
                case 2:
                    System.out.println("Ha seleccionado Categoría Superior.");
                    elegibles = obtenerVehiculos(tipo, Categoria.SUPERIOR);
                    break;
                case 3:
                    System.out.println("Ha seleccionado Categoría Especial.");
                    elegibles = obtenerVehiculos(tipo, Categoria.ESPECIAL);
                    break;
                default:
                    // [Ej. 6] «Cualquiera» ya no filtra por categoría (antes daba solo ESPECIAL)
                    System.out.println("Ha seleccionado cualquier categoría.");
                    elegibles = obtenerVehiculos(tipo);
                    break; // Volver a pedir la selección
            }

            System.out.println("Coches elegibles");

            for(int i = 0; i < elegibles.length; i++){
                System.out.println(i + " ---> " + elegibles[i]);
            }

            System.out.println("Introduzca número coche (sólo si está disponible)");
            int seleccion = KeyBoard.readInt();
            // [Ej. 6] validación del número de coche elegido
            if (seleccion < 0 || seleccion >= elegibles.length) {
                System.out.println("Número de coche inválido. Vuelva a seleccionar.");
                continue; // Volver a pedir la selección
            }
            vehiculo = elegibles[seleccion];
            seleccionado = true;
        }
        return vehiculo;
    }

    public void listarAlquileres (){
        System.out.println("Listado de alquileres");
        for (Alquiler alq: alquileres){
            System.out.println(alq);
        }
        System.out.println();
    }

    public void listarVehiculos(){
        System.out.println("Listado de vehículos");
        for (Vehiculo veh: vehiculos){
            System.out.println(veh);
        }
        System.out.println();
    }


    public static void main(String [] args){
        GestorAlquileres app = new GestorAlquileres();
        app.menuPrincipal();
    }
}
