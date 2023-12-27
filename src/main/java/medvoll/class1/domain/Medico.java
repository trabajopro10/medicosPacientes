package medvoll.class1.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import medvoll.class1.medico.DatosActualizarMedico;
import medvoll.class1.medico.DatosRegistroMedico;
import medvoll.class1.medico.Especialidad;

// Datos de persistencia 
@Table (name = "medicos")
@Entity(name = "Medico")
@Getter
//Aca Lombok, trabaja para crear varias cosas, ejemplo los getters y setters de los campos 
//y los constructores 
@NoArgsConstructor // Un constructor sin argumentos 
@AllArgsConstructor // Un constructor con todos los argumentos
@EqualsAndHashCode(of = "id") // Comparar los Id entre Médicos ( Seguro luego lo entiendo) 
public class Medico {
	
	// Para que genere de forma automática el Id 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nombre;
	private String email;
	private Boolean activo;
	private String telefono;
	private String documento;
	@Enumerated(EnumType.STRING)
	private Especialidad especialidad;
	// Este dentro de la clase direccion se colcará 
	// @Embeddable, esto quiere decir que incluya la clase 
	// dirección cuando use la clase Medico, pero hay que crear un constructor sin 
	// argumentos dentro de Dirección para que funcione 
	@Embedded
	private Direccion direccion;
	
	// Se crea un constructor que reciba los datos de Record datosRegistroMedico y llene los campos 
	// de la clase Medico 
	public Medico(DatosRegistroMedico datosRegistroMedico) {
		// TODO Auto-generated constructor stub
		this.nombre = datosRegistroMedico.nombre();
		this.documento = datosRegistroMedico.documento();
		this.email = datosRegistroMedico.email();
		this.activo = true;
		this.telefono = datosRegistroMedico.telefono();
		this.especialidad = datosRegistroMedico.especialidad();
		// Igual para que la clase Dirección, llene sus datos, se crea un constructor 
		// tenga como parámetro el Record DatosDirección 
		this.direccion = new Direccion(datosRegistroMedico.direccion());				
	}
	
    // Médico actualiza los datos, pero poder o no actualizarlos todos a la vez, entonces puede o no actualizar 
	// la dirección, el nombre o el documento, por lo que se hace necesario que se valide el contenido de cada dato 
	// que se reciba desde DatosActualizarMedico quien recibe desde el cliente que se va a actualizar. 
	// Al colocar validar el null evitamos errores, recordemos que dentro de 
	public void actualizarDatos(DatosActualizarMedico datosActualizarMedico) {
		// TODO Auto-generated method stub
		// El médico acepta el blanco, pero no acepta el null desde la tabla tambien está validado 
		if (datosActualizarMedico.nombre() !=null) {
			this.nombre = datosActualizarMedico.nombre();
		}
		// El el documento al crear la tabla se ha colocado que no puede ser null, acepta el blanco
		if (datosActualizarMedico.documento() !=null) {
			this.documento = datosActualizarMedico.documento();
		}
		// En la dirección siempre se valida en Datos dirección que ningun valor venga en blanco o nullo, por consiguiente 
		// si no viene el distrito o la calle o la ciduda o el número este será rechazado porque llama a DatosDirección quien 
		// valida con @NotBlank, esto implica que no pueden ser valores Blancos ni nullos . 
		if (datosActualizarMedico.direccion() != null) {
			this.direccion = direccion.actualizarDatos(datosActualizarMedico.direccion());
		}	
	}
   
	// Se Inactiva el estado activo del médico, pasa de true a false 
	public void inactivoMedico() {
		// TODO Auto-generated method stub
		this.activo = false;
	}

}
