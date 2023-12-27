package medvoll.class1.medico;

import jakarta.validation.constraints.NotNull;
import medvoll.class1.direccion.DatosDireccion;

// Nuestras reglas dicen que se puede actualizar, Nombre, Dirección y documento. 
// el id, debe ser obligatorio para poder consultar nuestro registro a actualizar, por consiguiente llevará 
// al inicio un @NotNull 
public record DatosActualizarMedico(@NotNull Long id, String nombre, DatosDireccion direccion, String documento) {

}
