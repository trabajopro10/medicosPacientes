package medvoll.class1.paciente;

import medvoll.class1.domain.Paciente;

public record DatosListadoPaciente(Long id, String nombre, String especialidad, String documento, String email) {


	// Constructor que con un parámetro de un objetos Médico retorna sus atributos 
			public DatosListadoPaciente(Paciente paciente) {
				this(paciente.getId(),paciente.getNombre(), 
					 paciente.getTelefono(), paciente.getDocumentoIdentidad(), 
					 paciente.getEmail());
			}
}
