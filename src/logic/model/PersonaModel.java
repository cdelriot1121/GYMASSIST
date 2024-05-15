
package logic.model;

/**
 *
 * @author CarlosDelrio
 */
public class PersonaModel {
    private String nombre;
    private String tp_id;
    private String no_id;
    private String correo;
    private String contraseña;
    private int asistencias;
    

    public PersonaModel(String nombre, String tp_id, String no_id, String correo, String contraseña, int asistencias) {
        this.nombre = nombre;
        this.tp_id = tp_id;
        this.no_id = no_id;
        this.correo = correo;
        this.contraseña = contraseña;
        this.asistencias = asistencias;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTp_id() {
        return tp_id;
    }

    public void setTp_id(String tp_id) {
        this.tp_id = tp_id;
    }

    public String getNo_id() {
        return no_id;
    }

    public void setNo_id(String no_id) {
        this.no_id = no_id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public int getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(int asistencias) {
        this.asistencias = asistencias;
    }
    @Override
    public String toString() {
        return  nombre + ", " + tp_id + ", " + no_id + ", " + correo + ", " + contraseña + ", " + asistencias;
    }
}
