package ni.edu.uam.EnviosCH.entities;

import org.openxava.annotations.*;
import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Paquete {

    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    @Required
    private String descripcion;

    @Required
    @Min(value = 0, message = "El peso no puede ser negativo")
    private double peso;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "almacen_id")
    // @DescriptionsList(descriptionProperties = "nombre")
    @Required
    private Almacen almacen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agente_id")
    // @DescriptionsList(descriptionProperties = "nombre, especialidad")
    @Required
    private AgenteAduanal agenteAsignado;

    @Depends("peso, tipoEnvio")
    @Stereotype("MONEY")
    public BigDecimal getCostoEnvio() {
        if (peso == 0) return BigDecimal.ZERO;

        double tarifaBase = 150.0;
        double multiplicador = 1.0;


        double total = tarifaBase + (peso * 20 * multiplicador);
        return new BigDecimal(total);
    }

    @PrePersist
    @PreUpdate
    private void normalizarDatos() {
        if (this.descripcion != null) {
            // Regla de negocio: Todo se guarda en mayúsculas para estandarización aduanal
            this.descripcion = this.descripcion.toUpperCase();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public AgenteAduanal getAgenteAsignado() {
        return agenteAsignado;
    }

    public void setAgenteAsignado(AgenteAduanal agenteAsignado) {
        this.agenteAsignado = agenteAsignado;
    }
}