package logic.controller;

import java.sql.*;
import java.time.LocalDate;

public class AsistenciaController {
    // No necesitamos una instancia de ConexionBD ya que getConnection() es estático
    
    public AsistenciaController() {
        // Constructor vacío
    }
    
    /**
     * Registra la asistencia de un usuario utilizando su número de identificación
     * @param numeroId El número de identificación del usuario
     * @return true si la asistencia se registró correctamente, false en caso contrario
     */
    public boolean registrarAsistencia(String numeroId) {
        try {
            Connection conn = ConexionBD.getConnection();
            
            // Primero obtener el ID del usuario desde la tabla personas
            String getUserIdQuery = "SELECT id FROM personas WHERE no_id = ?";
            PreparedStatement getUserIdStmt = conn.prepareStatement(getUserIdQuery);
            getUserIdStmt.setString(1, numeroId);
            ResultSet userRs = getUserIdStmt.executeQuery();
            
            if (!userRs.next()) {
                return false; // Usuario no encontrado
            }
            
            int userId = userRs.getInt("id");
            
            // Verificar si ya registró asistencia hoy
            String checkQuery = "SELECT * FROM asistencias WHERE id_usuario = ? AND fecha = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, userId);
            checkStmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                return false; // Ya registró asistencia hoy
            }
            
            // Insertar nueva asistencia
            String insertQuery = "INSERT INTO asistencias (id_usuario, fecha) VALUES (?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setInt(1, userId);
            insertStmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            
            int result = insertStmt.executeUpdate();
            
            // Si fue exitoso, incrementar el contador de asistencias en la tabla personas
            if (result > 0) {
                String updateQuery = "UPDATE personas SET asistencias = asistencias + 1 WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setInt(1, userId);
                updateStmt.executeUpdate();
            }
            
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al registrar asistencia: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene el número de asistencias de un usuario en el mes actual
     * @param numeroId El número de identificación del usuario
     * @return El número de asistencias en el mes actual
     */
    public int obtenerAsistenciasMes(String numeroId) {
        try {
            Connection conn = ConexionBD.getConnection();
            
            // Primero obtener el ID del usuario desde la tabla personas
            String getUserIdQuery = "SELECT id FROM personas WHERE no_id = ?";
            PreparedStatement getUserIdStmt = conn.prepareStatement(getUserIdQuery);
            getUserIdStmt.setString(1, numeroId);
            ResultSet userRs = getUserIdStmt.executeQuery();
            
            if (!userRs.next()) {
                return 0; // Usuario no encontrado
            }
            
            int userId = userRs.getInt("id");
            
            LocalDate inicio = LocalDate.now().withDayOfMonth(1);
            LocalDate fin = LocalDate.now();
            
            String query = "SELECT COUNT(*) as total FROM asistencias WHERE id_usuario = ? AND fecha BETWEEN ? AND ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setDate(2, java.sql.Date.valueOf(inicio));
            stmt.setDate(3, java.sql.Date.valueOf(fin));
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
            
            return 0;
        } catch (SQLException e) {
            System.err.println("Error al obtener asistencias: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Obtiene el total de asistencias de un usuario desde la tabla personas
     * @param numeroId El número de identificación del usuario
     * @return El total de asistencias registradas
     */
    public int obtenerTotalAsistencias(String numeroId) {
        try {
            Connection conn = ConexionBD.getConnection();
            String query = "SELECT asistencias FROM personas WHERE no_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, numeroId);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("asistencias");
            }
            
            return 0;
        } catch (SQLException e) {
            System.err.println("Error al obtener total de asistencias: " + e.getMessage());
            return 0;
        }
    }

}
