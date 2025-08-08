package logic.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import logic.model.AdministradorModel;
import logic.model.PersonaModel;

public class DatosController {
    private static DatosController instance; // Instancia única de DatosController

    private final List<PersonaModel> registroClientes;
    private final List<AdministradorModel> registroAdmins;

    public DatosController() {
        registroClientes = new ArrayList<>();
        registroAdmins = new ArrayList<>();
    initCargarClientes();
    initCargarAdmins();
    }

    // Método para obtener la instancia única de DatosController
    public static synchronized DatosController getInstance() {
        if (instance == null) {
            instance = new DatosController();
        }
        return instance;
    }

    // CRUD CLIENTE (tabla personas, is_admin = 0)
    public boolean insertarCliente(PersonaModel cliente) {
        final String sql = "INSERT INTO personas (nombre, tp_id, no_id, correo, contrasena, asistencias, is_admin) VALUES (?,?,?,?,?,?,0)";
        try (Connection cn = ConexionBD.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getTp_id());
            ps.setString(3, cliente.getNo_id());
            ps.setString(4, cliente.getCorreo());
            ps.setString(5, cliente.getContraseña());
            ps.setInt(6, cliente.getAsistencias());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                // cache ligera
                if (buscarCliente(cliente.getCorreo()) == -1) registroClientes.add(cliente);
                return true;
            }
        } catch (SQLException e) {
            // correo duplicado u otros errores
            return false;
        }
        return false;
    }

    public boolean insertarAdmin(AdministradorModel admin) {
        final String sql = "INSERT INTO personas (nombre, tp_id, no_id, correo, contrasena, asistencias, is_admin, nom_gym, val_mens) VALUES (?,?,?,?,?,?,1,?,?)";
        try (Connection cn = ConexionBD.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, admin.getNombre());
            ps.setString(2, admin.getTp_id());
            ps.setString(3, admin.getNo_id());
            ps.setString(4, admin.getCorreo());
            ps.setString(5, admin.getContraseña());
            ps.setInt(6, admin.getAsistencias());
            ps.setString(7, admin.getNom_gym());
            ps.setString(8, admin.getVal_mens());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                if (buscarAdmin(admin.getCorreo()) == -1) registroAdmins.add(admin);
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    public boolean modificarCliente(PersonaModel cliente) {
        final String sql = "UPDATE personas SET nombre=?, tp_id=?, no_id=?, contrasena=?, asistencias=? WHERE correo=? AND is_admin=0";
        try (Connection cn = ConexionBD.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getTp_id());
            ps.setString(3, cliente.getNo_id());
            ps.setString(4, cliente.getContraseña());
            ps.setInt(5, cliente.getAsistencias());
            ps.setString(6, cliente.getCorreo());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                int idx = buscarCliente(cliente.getCorreo());
                if (idx != -1) registroClientes.set(idx, cliente);
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    public boolean modificarAdmin(AdministradorModel admin) {
        final String sql = "UPDATE personas SET nombre=?, tp_id=?, no_id=?, contrasena=?, asistencias=?, nom_gym=?, val_mens=? WHERE correo=? AND is_admin=1";
        try (Connection cn = ConexionBD.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, admin.getNombre());
            ps.setString(2, admin.getTp_id());
            ps.setString(3, admin.getNo_id());
            ps.setString(4, admin.getContraseña());
            ps.setInt(5, admin.getAsistencias());
            ps.setString(6, admin.getNom_gym());
            ps.setString(7, admin.getVal_mens());
            ps.setString(8, admin.getCorreo());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                int idx = buscarAdmin(admin.getCorreo());
                if (idx != -1) registroAdmins.set(idx, admin);
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    public boolean eliminarCliente(String correo) {
        final String sql = "DELETE FROM personas WHERE correo=? AND is_admin=0";
        try (Connection cn = ConexionBD.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                int idx = buscarCliente(correo);
                if (idx != -1) registroClientes.remove(idx);
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    public boolean eliminarAdmin(String correo) {
        final String sql = "DELETE FROM personas WHERE correo=? AND is_admin=1";
        try (Connection cn = ConexionBD.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                int idx = buscarAdmin(correo);
                if (idx != -1) registroAdmins.remove(idx);
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    public PersonaModel obtenerCliente(String correo) {
        final String sql = "SELECT nombre,tp_id,no_id,correo,contrasena,asistencias FROM personas WHERE correo=? AND is_admin=0";
        try (Connection cn = ConexionBD.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new PersonaModel(
                        rs.getString("nombre"),
                        rs.getString("tp_id"),
                        rs.getString("no_id"),
                        rs.getString("correo"),
                        rs.getString("contrasena"),
                        rs.getInt("asistencias")
                    );
                }
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    public AdministradorModel obtenerAdmin(String correo) {
        final String sql = "SELECT nombre,tp_id,no_id,correo,contrasena,asistencias,nom_gym,val_mens FROM personas WHERE correo=? AND is_admin=1";
        try (Connection cn = ConexionBD.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AdministradorModel(
                        true,
                        rs.getString("nom_gym"),
                        rs.getString("val_mens"),
                        rs.getString("nombre"),
                        rs.getString("tp_id"),
                        rs.getString("no_id"),
                        rs.getString("correo"),
                        rs.getString("contrasena"),
                        rs.getInt("asistencias")
                    );
                }
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
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

    // Cargas desde BD (reemplazan lectura de TXT)
    private void initCargarClientes() {
        registroClientes.clear();
        final String sql = "SELECT nombre,tp_id,no_id,correo,contrasena,asistencias FROM personas WHERE is_admin=0";
        try (Connection cn = ConexionBD.getConnection(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                registroClientes.add(new PersonaModel(
                    rs.getString("nombre"), rs.getString("tp_id"), rs.getString("no_id"),
                    rs.getString("correo"), rs.getString("contrasena"), rs.getInt("asistencias")
                ));
            }
        } catch (SQLException e) {
            // silencio: dejamos lista vacía si falla
        }
    }

    private void initCargarAdmins() {
        registroAdmins.clear();
        final String sql = "SELECT nombre,tp_id,no_id,correo,contrasena,asistencias,nom_gym,val_mens FROM personas WHERE is_admin=1";
        try (Connection cn = ConexionBD.getConnection(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                registroAdmins.add(new AdministradorModel(
                    true,
                    rs.getString("nom_gym"),
                    rs.getString("val_mens"),
                    rs.getString("nombre"),
                    rs.getString("tp_id"),
                    rs.getString("no_id"),
                    rs.getString("correo"),
                    rs.getString("contrasena"),
                    rs.getInt("asistencias")
                ));
            }
        } catch (SQLException e) {
            // silencio
        }
    }

    // Métodos públicos opcionales si se desean recargas manuales desde la UI
    public void cargarClientes() { initCargarClientes(); }
    public void cargarAdmins() { initCargarAdmins(); }

    // Upserts compatibles con llamadas existentes desde la UI
    public void guardarAdmin(AdministradorModel admin) {
        if (!insertarAdmin(admin)) {
            modificarAdmin(admin);
        }
    }

    public void guardarCliente(PersonaModel cliente) {
        if (!insertarCliente(cliente)) {
            modificarCliente(cliente);
        }
    }

    // Cargar clientes por gimnasio desde BD usando tabla inscripciones + gimnasios
    public void cargarClientesPorGimnasio(String nombreGimnasio) {
        registroClientes.clear();
        final String sql = "SELECT p.nombre,p.tp_id,p.no_id,p.correo,p.contrasena,p.asistencias " +
                           "FROM personas p " +
                           "JOIN inscripciones i ON i.persona_id = p.id " +
                           "JOIN gimnasios g ON g.id = i.gimnasio_id " +
                           "WHERE g.nombre = ? AND p.is_admin = 0";
        try (Connection cn = ConexionBD.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nombreGimnasio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    registroClientes.add(new PersonaModel(
                        rs.getString("nombre"), rs.getString("tp_id"), rs.getString("no_id"),
                        rs.getString("correo"), rs.getString("contrasena"), rs.getInt("asistencias")
                    ));
                }
            }
        } catch (SQLException e) {
            // silencio
        }
    }
}