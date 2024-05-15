package logic.model;

import java.util.ArrayList;
import java.util.List;

public class GimnasioModel {
    private String nom_gym;
    private String val_mensual;
    private List<String> clientes_gimnasio;

    public GimnasioModel(String nom_gym, String val_mensual) {
        this.nom_gym = nom_gym;
        this.val_mensual = val_mensual;
        clientes_gimnasio = new ArrayList<>();
    }

    
    public String getNom_gym() {
        return nom_gym;
    }

    public void setNom_gym(String nom_gym) {
        this.nom_gym = nom_gym;
    }

    public String getVal_mensual() {
        return val_mensual;
    }

    public void setVal_mensual(String val_mensual) {
        this.val_mensual = val_mensual;
    }

    public List<String> getClientes_gimnasio() {
        return clientes_gimnasio;
    }

    public void setClientes_gimnasio(List<String> clientes_gimnasio) {
        this.clientes_gimnasio = clientes_gimnasio;
    }

    @Override
    public String toString() {
        return   '['+ nom_gym + " ," + val_mensual + ']' + '['+ clientes_gimnasio + ']';
    }
    
    
    
}