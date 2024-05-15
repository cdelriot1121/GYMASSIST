package logic.controller;

import logic.model.GimnasioModel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import logic.model.PersonaModel;

public class GimnasioController {
    private static GimnasioController instance;
    private List<GimnasioModel> registroGimnasios;
    private List<PersonaModel> registroClientes;
    
    
    public GimnasioController() {
        registroGimnasios = new ArrayList<>();
        registroClientes = new ArrayList<>(); // Inicialización de la lista de clientes
        cargarGimnasios();
    }

    public List<PersonaModel> getClientes_gimnasio() {
        return this.registroClientes;
    }
    

    public static GimnasioController getInstance() {
        if (instance == null) {
            instance = new GimnasioController();
        }
        return instance;
    }

    public boolean insertarGimnasio(GimnasioModel gimnasio) {
    if (!autenticarGimnasio(gimnasio.getNom_gym())) {
        registroGimnasios.add(gimnasio);
        guardarGimnasios(); // Guardar la lista de gimnasios actualizada en el archivo de texto
        System.out.println("Gimnasio insertado correctamente: " + gimnasio);
        return true;
    } else {
        return false; // El gimnasio ya existe
    }
}

    public boolean eliminarGimnasio(String nombreGimnasio) {
        int index = buscarGimnasio(nombreGimnasio);
        if (index != -1) {
            registroGimnasios.remove(index);
            return true; // Gimnasio eliminado
        } else {
            return false; // Gimnasio no encontrado
        }
    }

    public boolean autenticarGimnasio(String nombreGimnasio) {
        return buscarGimnasio(nombreGimnasio) != -1;
    }

    private int buscarGimnasio(String nombreGimnasio) {
        for (int i = 0; i < registroGimnasios.size(); i++) {
            if (registroGimnasios.get(i).getNom_gym().equals(nombreGimnasio)) {
                return i;
            }
        }
        return -1; // Gimnasio no encontrado
    }
    
    public List<GimnasioModel> obtenerGimnasios() { //PARA MI TIENE QUE VER ALGO AQUI
        return registroGimnasios;
    }
    
    // METODOS PARA GUARDAR Y CARGAR GIMNASIOS EN DOCUMENTOS TXT
    
    
    public void guardarGimnasios() {
    String rutaArchivo = System.getProperty("user.dir") + "/src/almacen/DatosGimnasios.txt";
    try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo))) {
        for (GimnasioModel gimnasio : registroGimnasios) {
            // Escribir el nombre del gimnasio y su valor mensual
            escritor.write(gimnasio.getNom_gym() + "," + gimnasio.getVal_mensual());
            
            // Si hay clientes, agregarlos al texto
            for (String cliente : gimnasio.getClientes_gimnasio()) {
                escritor.write("," + cliente);
            }
            escritor.newLine();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    
    //Que se complemente este metodo con los dos de guardar si es necesario
    
    public void cargarGimnasios() {
    try (BufferedReader lector = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/almacen/DatosGimnasios.txt")))) {
        String linea;
        while ((linea = lector.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(linea, ",");
            String nombreGym = st.nextToken().trim();
            String valorMensual = st.nextToken().trim();
            
            // Crear un objeto GimnasioModel con el nombre y la mensualidad
            GimnasioModel gimnasio = new GimnasioModel(nombreGym, valorMensual);
            
            // Agregar clientes si existen
            while (st.hasMoreTokens()) {
                String cliente = st.nextToken().trim();
                gimnasio.getClientes_gimnasio().add(cliente);
            }
            
            // Agregar el gimnasio al registro de gimnasios
            registroGimnasios.add(gimnasio);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    /*
    Aqui va un metodo para poder guardar a los clientes cuando se registran aun gimnasio
    en el documento txt DatosGimnasio en el boton de incribir gimnasio de los clientes
    */
    public void guardarInscripcionGimnasio(String nombreGimnasio, String nombreCliente) {
    String rutaArchivo = System.getProperty("user.dir") + "/src/almacen/DatosGimnasios.txt";
    try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo))) {
        for (GimnasioModel gimnasio : registroGimnasios) {
            // Si el gimnasio coincide con el nombre proporcionado
            if (gimnasio.getNom_gym().equals(nombreGimnasio)) {
                // Agregar el nuevo cliente a la lista de clientes del gimnasio
                gimnasio.getClientes_gimnasio().add(nombreCliente);
                
                // Escribir el nombre del gimnasio y la mensualidad
                escritor.write(gimnasio.getNom_gym() + "," + gimnasio.getVal_mensual());
                
                // Escribir la lista de clientes del gimnasio separados por comas
                for (String cliente : gimnasio.getClientes_gimnasio()) {
                    escritor.write("," + cliente);
                }
                
                // Agregar un salto de línea al final
                escritor.newLine();
            } else {
                // Si el gimnasio no coincide, escribir los datos sin modificar
                escritor.write(gimnasio.getNom_gym() + "," + gimnasio.getVal_mensual());
                for (String cliente : gimnasio.getClientes_gimnasio()) {
                    escritor.write("," + cliente);
                }
                escritor.newLine();
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    
    //Metodos Cargar y obtener clientes por gimnasio
    public void cargarClientesPorGimnasio(String nombreGimnasio) {
    try (BufferedReader lector = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/almacen/AlmacenClientes.txt")))) {
        String linea;
        while ((linea = lector.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(linea, ",");
            String nombre = null;
            String tp_id = null;
            String no_id = null;
            String correo = null;
            String contraseña = null;
            int asistencias = 0;
            String nom_gym = null;

            if (st.hasMoreTokens()) {
                nombre = st.nextToken().trim();
            }
            if (st.hasMoreTokens()) {
                tp_id = st.nextToken().trim();
            }
            if (st.hasMoreTokens()) {
                no_id = st.nextToken().trim();
            }
            if (st.hasMoreTokens()) {
                correo = st.nextToken().trim();
            }
            if (st.hasMoreTokens()) {
                contraseña = st.nextToken().trim();
            }
            if (st.hasMoreTokens()) {
                asistencias = Integer.parseInt(st.nextToken().trim());
            }
            if (st.hasMoreTokens()) {
                nom_gym = st.nextToken().trim();
            }

            if (nom_gym != null && nom_gym.equals(nombreGimnasio)) {
                PersonaModel cliente = new PersonaModel(nombre, tp_id, no_id, correo, contraseña, asistencias);
                registroClientes.add(cliente); // Ahora debería funcionar correctamente
            }
        }
    } catch (IOException | NumberFormatException e) {
        e.printStackTrace();
    }
}
    
    
    
     // Método para buscar clientes por nombre de gimnasio en el registro de gimnasios
    public List<String> buscarClientesPorNombreGimnasio(String nombreGimnasio) {
        List<String> clientesEnGimnasio = new ArrayList<>();
        for (GimnasioModel gimnasio : registroGimnasios) {
            if (gimnasio.getNom_gym().equals(nombreGimnasio)) {
                clientesEnGimnasio.addAll(gimnasio.getClientes_gimnasio());
                break;
            }
        }
        return clientesEnGimnasio;
    }

    // Método para recoger clientes del registro de clientes
    public List<PersonaModel> recogerClientesPorNombre(List<String> nombresClientes) {
        List<PersonaModel> clientesRecogidos = new ArrayList<>();
        for (String nombreCliente : nombresClientes) {
            for (PersonaModel cliente : registroClientes) {
                if (cliente.getNombre().equals(nombreCliente)) {
                    clientesRecogidos.add(cliente);
                    break;
                }
            }
        }
        return clientesRecogidos;
    }
}