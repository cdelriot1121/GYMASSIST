package logic.controller;

import java.util.ArrayList;
import java.util.List;
import logic.model.AdministradorModel;
import logic.model.PersonaModel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;


public class DatosController {
    private static DatosController instance; // Instancia única de DatosController
    
    private List<PersonaModel> registroClientes;
    private List<AdministradorModel> registroAdmins;

    public DatosController() {
        registroClientes = new ArrayList<>();
        registroAdmins = new ArrayList<>();
        cargarClientes();
        cargarAdmins();
    }
    
    // Método para obtener la instancia única de DatosController
    public static DatosController getInstance() {
        if (instance == null) {
            instance = new DatosController();
        }
        return instance;
    }
    public boolean insertarCliente(PersonaModel cliente) {
        if (buscarCliente(cliente.getCorreo()) == -1) {
            registroClientes.add(cliente);
            return true;
        } else {
            return false; // El cliente ya existe
        }
    }

    public boolean insertarAdmin(AdministradorModel admin) {
        if (buscarAdmin(admin.getCorreo()) == -1) {
            registroAdmins.add(admin);
            return true;
        } else {
            return false; // El administrador ya existe
        }
    }

    public boolean modificarCliente(PersonaModel cliente) {
        int index = buscarCliente(cliente.getCorreo());
        if (index != -1) {
            registroClientes.set(index, cliente);
            return true;
        } else {
            return false; // Cliente no encontrado
        }
    }

    public boolean modificarAdmin(AdministradorModel admin) {
        int index = buscarAdmin(admin.getCorreo());
        if (index != -1) {
            registroAdmins.set(index, admin);
            return true;
        } else {
            return false; // Administrador no encontrado
        }
    }

    public boolean eliminarCliente(String correo) {
        int index = buscarCliente(correo);
        if (index != -1) {
            registroClientes.remove(index);
            return true; // Cliente eliminado
        } else {
            return false; // Cliente no encontrado
        }
    }

    public boolean eliminarAdmin(String correo) {
        int index = buscarAdmin(correo);
        if (index != -1) {
            registroAdmins.remove(index);
            return true; // Administrador eliminado
        } else {
            return false; // Administrador no encontrado
        }
    }

    public PersonaModel obtenerCliente(String correo) {
        int index = buscarCliente(correo);
        if (index != -1) {
            return registroClientes.get(buscarCliente(correo));
        } else {
            return null; // Cliente no encontrado
        }
    }

    public AdministradorModel obtenerAdmin(String correo) {
        int index = buscarAdmin(correo);
        if (index != -1) {
            return registroAdmins.get(buscarAdmin(correo));
        } else {
            return null; // Administrador no encontrado
        }
    }

    private int buscarCliente(String correo) {
        for (int i = 0; i < registroClientes.size(); i++) {
            if (registroClientes.get(i).getCorreo().equals(correo)) {
                return i;
            }
        }
        return -1; // Cliente no encontrado
    }

    private int buscarAdmin(String correo) {
        for (int i = 0; i < registroAdmins.size(); i++) {
            if (registroAdmins.get(i).getCorreo().equals(correo)) {
                return i;
            }
        }
        return -1; // Administrador no encontrado
    }
    
    
    //METODOS PARA CARGAR Y GUARDAR DATOS EN LOS DOCUMENTOS TXT desde DatosController

     public void cargarClientes() {
        try (BufferedReader lector = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/almacen/AlmacenClientes.txt")))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(linea, ",");
                String nombre = st.nextToken().trim();
                String tp_id = st.nextToken().trim();
                String no_id = st.nextToken().trim();
                String correo = st.nextToken().trim();
                String contraseña = st.nextToken().trim();
                int asistencias = Integer.parseInt(st.nextToken().trim());
                // Crear un objeto PersonaModel con las partes y agregarlo a la lista de clientes
                PersonaModel cliente = new PersonaModel(nombre, tp_id, no_id, correo, contraseña, asistencias);
                registroClientes.add(cliente);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void cargarAdmins() {
        try (BufferedReader lector = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/almacen/AlmacenAdmins.txt")))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(linea, ",");
                String nombre = st.nextToken().trim();
                String tp_id = st.nextToken().trim();
                String no_id = st.nextToken().trim();
                String correo = st.nextToken().trim();
                String contraseña = st.nextToken().trim();
                int asistencias = Integer.parseInt(st.nextToken().trim());
                boolean key_admin = Boolean.parseBoolean(st.nextToken().trim());
                String nom_gym = st.nextToken().trim();
                String val_mens = st.nextToken().trim();
                // Crear un objeto AdministradorModel con las partes y agregarlo a la lista de admins
                AdministradorModel admin = new AdministradorModel(key_admin, nom_gym, val_mens, nombre, tp_id, no_id, correo, contraseña, asistencias);
                registroAdmins.add(admin);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
    public void guardarAdmin(AdministradorModel admin) {
        String rutaArchivo = System.getProperty("user.dir") + "/src/almacen/AlmacenAdmins.txt";
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            escritor.write(admin.toString());
            escritor.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void guardarCliente(PersonaModel cliente) {
        String rutaArchivo = System.getProperty("user.dir") + "/src/almacen/AlmacenClientes.txt";
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            escritor.write(cliente.toString());
            escritor.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Aqui puedes crear el otro metodo
    public void cargarClientesPorGimnasio(String nombreGimnasio) {
    try (BufferedReader lector = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/almacen/AlmacenClientes.txt")))) {
        String linea;
        while ((linea = lector.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(linea, ",");
            String nombre = null;
            String nom_gym = null;

            if (st.hasMoreTokens()) {
                nombre = st.nextToken().trim();
            }
            if (st.hasMoreTokens()) {
                st.nextToken(); // Saltamos el tipo de identificación
            }
            if (st.hasMoreTokens()) {
                st.nextToken(); // Saltamos el número de identificación
            }
            if (st.hasMoreTokens()) {
                st.nextToken(); // Saltamos el correo
            }
            if (st.hasMoreTokens()) {
                st.nextToken(); // Saltamos la contraseña
            }
            if (st.hasMoreTokens()) {
                st.nextToken(); // Saltamos las asistencias
            }
            if (st.hasMoreTokens()) {
                nom_gym = st.nextToken().trim();
            }

            System.out.println("Nombre del cliente: " + nombre);
            System.out.println("Nombre del gimnasio: " + nom_gym);

            if (nom_gym != null && nom_gym.equals(nombreGimnasio)) {
                PersonaModel cliente = new PersonaModel(nombre, "", "", "", "", 0);
                registroClientes.add(cliente); // Agregar cliente al registro
                System.out.println("Cliente agregado al gimnasio: " + nombre);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}   
}