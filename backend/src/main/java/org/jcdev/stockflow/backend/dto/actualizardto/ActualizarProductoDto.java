package org.jcdev.stockflow.backend.dto.actualizardto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ActualizarProductoDto {

    @Size(message = "El nombre no puede superar los 100 caracteres" , max = 100)
    private String nombre;
    @Size(message = "La descripcion debe tener entre 5 y 255 caracteres", min = 5, max = 255)
    private String descripcion;
    private LocalDate fechaProduccion;
    private LocalDate fechaCaducidad;
    private Boolean activo;
    private Long idCategoria;
    private Long idEmpresa;
    private Long idUbicacion;
}
