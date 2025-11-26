package ni.edu.uam.EnviosCH.entities;

import org.openxava.annotations.Hidden;
import org.openxava.annotations.ListProperties;
import org.openxava.annotations.Required;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class AgenteAduanal {

    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    @Required
    private String nombre;

    @Column(length = 50)
    @Required
    private String especialidad;

    @OneToMany(mappedBy = "agenteAsignado", cascade = CascadeType.ALL)
    @ListProperties("descripcion, peso, almacen.nombre")
    private Collection<Paquete> paquetesACargo = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public Collection<Paquete> getPaquetesACargo() {
        return paquetesACargo;
    }

    public void setPaquetesACargo(Collection<Paquete> paquetesACargo) {
        this.paquetesACargo = paquetesACargo;
    }
}