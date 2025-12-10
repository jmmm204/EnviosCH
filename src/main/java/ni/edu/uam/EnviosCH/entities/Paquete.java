package ni.edu.uam.EnviosCH.entities;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
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

    @Enumerated(EnumType.STRING)
    @Required
    private TipoEnvio tipoEnvio;

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

        if (tipoEnvio != null) {
            switch (tipoEnvio) {
                case EXPRESS: multiplicador = 2.5; break;
                case ESTANDAR: multiplicador = 1.5; break;
                case ECONOMICO: multiplicador = 1.0; break;
            }
        }

        double total = tarifaBase + (peso * 20 * multiplicador);
        return new BigDecimal(total);
    }

    @AssertTrue(message = "Los envíos Express no pueden pesar más de 50kg por regulación aduanera")
    private boolean isPesoValidoParaExpress() {
        if (this.tipoEnvio == TipoEnvio.EXPRESS && this.peso > 50) {
            return false;
        }
        return true;
    }

    @PrePersist
    @PreUpdate
    private void normalizarDatos() {
        if (this.descripcion != null) {
            // Regla de negocio: Todo se guarda en mayúsculas para estandarización aduanal
            this.descripcion = this.descripcion.toUpperCase();
        }
    }
}