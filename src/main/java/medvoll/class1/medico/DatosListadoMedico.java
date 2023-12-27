package medvoll.class1.medico;

import medvoll.class1.domain.Medico;

// Queremos que los datos que se muestren sean Nombre, Especialidad, Documento, Email, y
// para poder actualizar y saber que registro estamos tocando, se debe retornar el id del Médico 
public record DatosListadoMedico(Long id, String nombre, String especialidad, String documento, String email) {


// Constructor que con un parámetro de un objetos Médico retorna sus atributos 
		public DatosListadoMedico(Medico medico) {
			this(medico.getId(),medico.getNombre(), 
				 medico.getEspecialidad().toString(), 
			     medico.getDocumento(), medico.getEmail());
		}

}