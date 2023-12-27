package medvoll.class1.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import medvoll.class1.paciente.DatosActualizacionPaciente;
import medvoll.class1.paciente.DatosRegistroPaciente;

//Datos de persistencia 
@Table (name = "pacientes")
@Entity(name = "Paciente")
@Getter
//Aca Lombok, trabaja para crear varias cosas, ejemplo los getters y setters de los campos 
//y los constructores 
@NoArgsConstructor // Un constructor sin argumentos 
@AllArgsConstructor // Un constructor con todos los argumentos
@EqualsAndHashCode(of = "id") // Comparar los Id entre Médicos ( Seguro luego lo entiendo) 
public class Paciente {
	
	// Para que genere de forma automática el Id 
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		private String nombre;
		private String email;
		private Boolean activo;
		private String telefono;
		private String documentoIdentidad;
		@Enumerated(EnumType.STRING)
		// Este dentro de la clase direccion se colcará 
		// @Embeddable, esto quiere decir que incluya la clase 
		// dirección cuando use la clase Medico, pero hay que crear un constructor sin 
		// argumentos dentro de Dirección para que funcione 
		@Embedded
		private Direccion direccion;
		
		// Se crea un constructor que reciba los datos de Record datosRegistroMedico y llene los campos 
		// de la clase Medico 
		public Paciente(DatosRegistroPaciente datosRegistroPaciente) {
			// TODO Auto-generated constructor stub
			this.nombre = datosRegistroPaciente.nombre();
			this.documentoIdentidad = datosRegistroPaciente.documentoIdentidad();
			this.email = datosRegistroPaciente.email();
			this.activo = true;
			this.telefono = datosRegistroPaciente.telefono();
			// Igual para que la clase Dirección, llene sus datos, se crea un constructor 
			// tenga como parámetro el Record DatosDirección 
			this.direccion = new Direccion(datosRegistroPaciente.direccion());				
		}

		public void eliminar() {
			// TODO Auto-generated method stub
			this.activo = false;
			
		}

		public void actualizarInformacion(@Valid DatosActualizacionPaciente datos) {
			// TODO Auto-generated method stub
			
		}
		
}



