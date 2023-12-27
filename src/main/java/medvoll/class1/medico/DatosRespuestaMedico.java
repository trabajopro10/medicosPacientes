package medvoll.class1.medico;

import medvoll.class1.direccion.DatosDireccion;

// Este es un DTO (Data Transfer Object) tipo Record, para dar respuesta al momento que se actualicen 
// los datos de un médico, por algún motivo no incluyeron documento. 
public record DatosRespuestaMedico (Long id, String nombre, String email,String telefono, String especialidad, 
		                             DatosDireccion direccion) {

}
