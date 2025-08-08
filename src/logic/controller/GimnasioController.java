package logic.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import logic.model.GimnasioModel;
import logic.model.PersonaModel;

public class GimnasioController {
    private static GimnasioController instance;
    private final List<GimnasioModel> registroGimnasios;
    private final List<PersonaModel> registroClientes;

    public GimnasioController() {
        registroGimnasios = new ArrayList<>();
        registroClientes = new ArrayList<>(); // Inicialización de la lista de clientes
    initCargarGimnasios();
    }

    public List<PersonaModel> getClientes_gimnasio() {
        return this.registroClientes;
    }

    public static synchronized GimnasioController getInstance() {
        if (instance == null) {
            instance = new GimnasioController();
        }
        return instance;
    }

    public boolean insertarGimnasio(GimnasioModel gimnasio) {
        // Inserta en BD si no existe
        if (autenticarGimnasio(gimnasio.getNom_gym())) {
            return false;
        }
        final String sql = "INSERT INTO gimnasios (nombre, val_mensual) VALUES (?,?)";
        try (Connection cn = ConexionBD.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, gimnasio.getNom_gym());
            ps.setString(2, gimnasio.getVal_mensual());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                registroGimnasios.add(gimnasio);
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    public boolean eliminarGimnasio(String nombreGimnasio) {
        final String sql = "DELETE FROM gimnasios WHERE nombre=?";
        try (Connection cn = ConexionBD.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nombreGimnasio);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                int index = buscarGimnasio(nombreGimnasio);
                if (index != -1) registroGimnasios.remove(index);
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    public boolean autenticarGimnasio(String nombreGimnasio) {
        final String sql = "SELECT id FROM gimnasios WHERE nombre=?";
        try (Connection cn = ConexionBD.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nombreGimnasio);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }

    private int buscarGimnasio(String nombreGimnasio) {
        for (int i = 0; i < registroGimnasios.size(); i++) {
            if (registroGimnasios.get(i).getNom_gym().equals(nombreGimnasio)) {
                return i;
            }
        }
        return -1; // Gimnasio no encontrado
    }

    public List<GimnasioModel> obtenerGimnasios() {
        return registroGimnasios;
    }

    // Compatibilidad con métodos existentes (ahora no escriben TXT)
    public void guardarGimnasios() {
        // No-op: los datos ya están en BD. Refrescamos cache desde BD.
        cargarGimnasios();
    }

    private void initCargarGimnasios() {
        registroGimnasios.clear();
        final String sql = "SELECT nombre, val_mensual FROM gimnasios";
        try (Connection cn = ConexionBD.getConnection(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                GimnasioModel g = new GimnasioModel(rs.getString("nombre"), rs.getString("val_mensual"));
                // Rellenar clientes nominales (nombres) para compatibilidad con UI
                g.setClientes_gimnasio(new ArrayList<>());
                final String sqlCli = "SELECT p.nombre FROM inscripciones i JOIN personas p ON p.id = i.persona_id JOIN gimnasios g2 ON g2.id = i.gimnasio_id WHERE g2.nombre=?";
                try (PreparedStatement ps2 = cn.prepareStatement(sqlCli)) {
                    ps2.setString(1, g.getNom_gym());
                    try (ResultSet rs2 = ps2.executeQuery()) {
                        while (rs2.next()) {
                            g.getClientes_gimnasio().add(rs2.getString(1));
                        }
                    }
                }
                registroGimnasios.add(g);
            }
        } catch (SQLException e) {
            // dejar lista vacía si falla
        }
    }

    public void cargarGimnasios() { initCargarGimnasios(); }

    public void guardarInscripcionGimnasio(String nombreGimnasio, String nombreCliente) {
        final String sql = "INSERT INTO inscripciones (persona_id, gimnasio_id) " +
                           "SELECT p.id, g.id FROM personas p, gimnasios g " +
                           "WHERE p.nombre=? AND p.is_admin=0 AND g.nombre=? " +
                           "AND NOT EXISTS (SELECT 1 FROM inscripciones i WHERE i.persona_id=p.id AND i.gimnasio_id=g.id)";
        try (Connection cn = ConexionBD.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nombreCliente);
            ps.setString(2, nombreGimnasio);
            ps.executeUpdate();
            // refrescar cache
            cargarGimnasios();
        } catch (SQLException e) {
            // ignorar duplicados
        }
    }

    //Metodos Cargar y obtener clientes por gimnasio
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
                    PersonaModel cliente = new PersonaModel(
                        rs.getString("nombre"), rs.getString("tp_id"), rs.getString("no_id"),
                        rs.getString("correo"), rs.getString("contrasena"), rs.getInt("asistencias")
                    );
                    registroClientes.add(cliente);
                }
            }
        } catch (SQLException e) {
            // silencio
        }
    }

    public List<String> buscarClientesPorNombreGimnasio(String nombreGimnasio) {
        List<String> clientesEnGimnasio = new ArrayList<>();
        final String sql = "SELECT p.nombre FROM personas p " +
                           "JOIN inscripciones i ON i.persona_id = p.id " +
                           "JOIN gimnasios g ON g.id = i.gimnasio_id WHERE g.nombre=?";
        try (Connection cn = ConexionBD.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nombreGimnasio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) clientesEnGimnasio.add(rs.getString(1));
            }
        } catch (SQLException e) {
            // silencio
        }
        return clientesEnGimnasio;
    }

    public List<PersonaModel> recogerClientesPorNombre(List<String> nombresClientes) {
        List<PersonaModel> clientesRecogidos = new ArrayList<>();
        if (nombresClientes.isEmpty()) return clientesRecogidos;
        final String sql = "SELECT nombre,tp_id,no_id,correo,contrasena,asistencias FROM personas WHERE is_admin=0 AND nombre IN (" +
                           String.join(",", java.util.Collections.nCopies(nombresClientes.size(), "?")) + ")";
        try (Connection cn = ConexionBD.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            for (int i = 0; i < nombresClientes.size(); i++) ps.setString(i + 1, nombresClientes.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clientesRecogidos.add(new PersonaModel(
                        rs.getString("nombre"), rs.getString("tp_id"), rs.getString("no_id"),
                        rs.getString("correo"), rs.getString("contrasena"), rs.getInt("asistencias")
                    ));
                }
            }
        } catch (SQLException e) {
            // silencio
        }
        return clientesRecogidos;
    }

    public List<PersonaModel> getRegistroClientes() {
        return registroClientes;
    }

    public void cargarClientesDesdeArchivo() {
        // Compat: ahora carga desde BD solo nombres
        registroClientes.clear();
        final String sql = "SELECT nombre FROM personas WHERE is_admin=0";
        try (Connection cn = ConexionBD.getConnection(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                PersonaModel cliente = new PersonaModel(rs.getString(1), "", "", "", "", 0);
                registroClientes.add(cliente);
            }
        } catch (SQLException e) {
            // silencio
        }
    }

    public List<PersonaModel> cargarDetallesClientes(List<String> nombresClientes) {
        return recogerClientesPorNombre(nombresClientes);
    }
}