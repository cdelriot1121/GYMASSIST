package logic.main;

import logic.controller.*;
import pantallas.MainScreen;

public class Execution {
    public static void main(String[] args) {
        
        // Crear una instancia de DatosController
        GimnasioController gimnasiocontroller = new GimnasioController();
        DatosController datosController = new DatosController();
        
        
        // Cargar los clientes, administradores, y datos de gimnasios
        datosController.cargarClientes();
        datosController.cargarAdmins();
        
        
        // Mostrar la pantalla principal
        MainScreen main_screen = new MainScreen();
        main_screen.setVisible(true);
    }   
}
