package logic.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Helper JDBC centralizado. Ajusta DRIVER, URL, USER y PASS según tu BD.
 */
public class ConexionBD {
    // Ajusta estos valores a tu entorno. Ejemplo para MySQL.
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver"; // MySQL 8+
    // Puerto por defecto 3306; cambia si tu MySQL usa otro
    private static final String URL = "jdbc:mysql://localhost:3306/gymassist?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "rootcar";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se encontró el driver JDBC: " + DRIVER, e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
