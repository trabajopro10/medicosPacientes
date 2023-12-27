package medvoll.class1.domain.consulta;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import medvoll.class1.domain.Medico;
import medvoll.class1.domain.Paciente;


//Datos de persistencia 
@Table (name = "consultas")
@Entity(name = "Consulta")
@Getter
//Aca Lombok, trabaja para crear varias cosas, ejemplo los getters y setters de los campos 
//y los constructores 
@NoArgsConstructor // Un constructor sin argumentos 
@AllArgsConstructor // Un constructor con todos los argumentos
@EqualsAndHashCode(of = "id") // Comparar los Id entre Médicos ( Seguro luego lo entiendo) 
public class Consulta {
	

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// La tabla consulta hace el vínculo con medico y paciente mediante los campos 
	// medico_id y paciente_id 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medico_id")
	private Medico medico;
	
	// Igual aca hace el Joint entre la tabla paciente 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "paciente_id")
	private Paciente paciente;
	
	private LocalDateTime data;
	
	// Se crea un constructor que reciba los datos de Record datosRegistroMedico y llene los campos 
	// de la clase Medico 
		public Consulta(Medico medico, Paciente paciente, LocalDateTime fecha ) {
			// TODO Auto-generated constructor stub
			this.medico = medico;
			this.paciente = paciente;
			this.data = fecha;			
		}
	

}
