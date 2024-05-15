
package logic.model;
/**
 *
 * @author CarlosDelrio
 */
public class AdministradorModel extends PersonaModel{
    private boolean key_admin;
    private String nom_gym;
    private String val_mens;

    public AdministradorModel(boolean key_admin, String nom_gym, String val_mens, String nombre, String tp_id, String no_id, String correo, String contraseña, int asistencias) {
        super(nombre, tp_id, no_id, correo, contraseña, asistencias);
        this.key_admin = true;
        this.nom_gym = nom_gym;
        this.val_mens = val_mens;
    } 

    public boolean isKey_admin() {
        return key_admin;
    }

    public void setKey_admin(boolean key_admin) {
        this.key_admin = key_admin;
    }

    public String getNom_gym() {
        return nom_gym;
    }

    public void setNom_gym(String nom_gym) {
        this.nom_gym = nom_gym;
    }

    public String getVal_mens() {
        return val_mens;
    }

    public void setVal_mens(String val_mens) {
        this.val_mens = val_mens;
    }

    @Override
    public String toString() {
    return super.toString()+ ", " + key_admin + ", " + nom_gym + ", " + val_mens;
    }
}
