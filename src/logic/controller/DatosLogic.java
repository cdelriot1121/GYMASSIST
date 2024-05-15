package logic.controller;
import logic.model.*;


public class DatosLogic {
    private static DatosController cliente_ob = new DatosController();
    private static DatosController admin_ob = new DatosController();
    
    private static boolean esAdministrador = false;
    
    // Método para establecer si se está registrando un administrador
    public static void setEsAdministrador(boolean admin) {
        esAdministrador = admin;
    }
    
    public static boolean autentificarCliente(String correo, String contraseña){
        if (obtenerCliente(correo) != null){
            PersonaModel clienteconsulta = obtenerCliente(correo);
            return clienteconsulta.getCorreo().equals(correo) && clienteconsulta.getContraseña().equals(contraseña);
        } else {
            return false;
        }
    }
    
    public static boolean autentificarAdmin(String correo, String contraseña){
        if (obtenerAdmin(correo) != null){
            AdministradorModel admin_ob = obtenerAdmin(correo);
            return admin_ob.getCorreo().equals(correo) && admin_ob.getContraseña().equals(contraseña);
        } else {
            return false;
        }
    }
    
    public static boolean insertarCliente(PersonaModel cliente) {
        return cliente_ob.insertarCliente(cliente);
    }
    
    public static boolean insertarAdmin(AdministradorModel admin) {
        return admin_ob.insertarAdmin(admin);
    }
    
    public static boolean modificarCliente(PersonaModel cliente) {
        return cliente_ob.modificarCliente(cliente);
    }
    
    public static boolean modificarAdmin(AdministradorModel admin) {
        return admin_ob.modificarAdmin(admin);
    }
    
    public static boolean eliminarCliente(String correo) {
        return cliente_ob.eliminarCliente(correo);
    }
    
    public static boolean eliminarAdmin(String correo) {
        return admin_ob.eliminarAdmin(correo);
    }
    
    public static PersonaModel obtenerCliente(String correo) {
        return cliente_ob.obtenerCliente(correo);
    }
    
    public static AdministradorModel obtenerAdmin(String correo) {
        return admin_ob.obtenerAdmin(correo);
    }
    public static boolean registrarUsuario(String nombre, String tp_id, String no_id, String correo, String contraseña, boolean esAdmin, String nom_gym, String val_mens, int asistencias) {
    if (esAdmin) {
        // Si se está registrando un administrador
        AdministradorModel admin = new AdministradorModel(true, nom_gym, val_mens, nombre, tp_id, no_id, correo, contraseña, asistencias);
        return insertarAdmin(admin);
    } else {
        // Si se está registrando un cliente
        PersonaModel cliente = new PersonaModel(nombre, tp_id, no_id, correo, contraseña, asistencias);
        return insertarCliente(cliente);
    }
}

    
    

}
