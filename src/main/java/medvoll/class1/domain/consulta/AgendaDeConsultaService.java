package medvoll.class1.domain.consulta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import medvoll.class1.controller.MedicoRepository;
import medvoll.class1.controller.PacienteRepository;
import medvoll.class1.domain.Medico;
import medvoll.class1.domain.infra.errores.ValidacionDeIntegridad;

// Dentro de la clase AgendaConsultaService, debemos encontrar un médico que corresponda 
// a las necesidades que el paciente requiere, para ello hay que consultar en la base 
// de datos 

@Service
public class AgendaDeConsultaService {
	
	// Agregamos los respositorios de Medico y Paciente y se anotan con 
	// @Autowired, para que inyecten sus métodos y se puedan encontrar al 
	// momento de usarlos 
	@Autowired
	private PacienteRepository pacienteRepository;
		
	@Autowired
	private MedicoRepository medicoRepository;
	
	// Agregamos el ConsultaRepository que nos ayuda a guardar los datos en la 
	// base de datos 
	@Autowired // Si no colocamos @Autowired, el valor de consultaRepository será null, y no 
	           // nos permitiría utilizar los métodos que están dentro de consultaRepository
	           //@Autowired los inyecta y permite su uso. 
	private ConsultaRepository consultaRepository;

	// Agregamos el método agendar, que guardará la información que va a llegar desde 
	// el cliente(quien accede a la página) y a traves del Repository se va a guardar en la 
	// base de datos. Nos permitirá agregar nuevos registros a la base de datos,usando 
	// el id del paciente, el id del médico y la fecha de la consulta. 
	public void agendar(DatosAgendarConsulta datos) {
		
		// Aca consultando datos, estos datos deben ser validados 
		// con los repository, podemos acceder a la base de datos y saber según su 
		// respuesta si el dato si está 
		// Vamos con pacienteRepository y busquemos por el id del cliente. 
		// Veremos que toda la sentencia tiene varios métodos para comprobar, para el caso 
		// estamos diciendo con isPresent que si encontró por id el paciente buscado. 
		if(pacienteRepository.findById(datos.idPaciente()).isPresent()) {
			// Si no lo encontró vamos a crear nuestra propias exception a la cual le daremos 
			// un mensaje personalizado que nos mostrará al momento de presentarse el error.
			// Se crea esta clase ValidacionDeIntegridad, que va a extender de RutimeException el tutor
			// nos dice que si extendiera de Throwable, habría que colocar aca a la clase principal 
			// agendar Throw, al ser de RutimeException, no hay problema. 
			throw new ValidacionDeIntegridad("Este id para el Paciente no fue encontrado.... ");
		}
		
		// Validar que el médico exista y que este dísponible de acuerdo a las necesidades del 
		// paciente, para eso hay que agregar la validación, donde se verifica que el médico que llega
		// en datos no sea nullo y que exista dentro de la base de datos 
		if(datos.idMedico()!=null && medicoRepository.existsById(datos.idMedico())) {
		    throw new ValidacionDeIntegridad("El idMedico no existe en la base de datos o es nulo .... ");
		}
		
		
		// Consultamos los datos que nos son necesarios, el get() al final asegura 
		// dice el tutor que no sea una dato optional (Ayuda a mejorar el código evitando nullos)
		// sino que sea un dato con certeza del tipo paciente. 
		var paciente = pacienteRepository.findById(datos.idPaciente()).get();
		// Ahora para el médico, este va hacer, igual con medicoRepository, lo busca directamente
		// en la base de datos ubicandolo por medio del idMedico 
		//**  var medico = medicoRepository.findById(datos.idMedico()).get(); SE CUBRE DICE EL TUTOR 
		                                                                    // PARA HACER ALGO MEJOR
	    // Validar y traer el médico, colocamos datos como parámetro, recordemos que datos viene de 
		// DatosAgendarConsulta 
		var medico = seleccionarMedico(datos); // Este método retorna un objeto Medico 
		 
		
		var consulta = new Consulta(null,medico,paciente,datos.fecha());
		consultaRepository.save(consulta); // Podemos usar este save, gracias a que está anotado con 
		                              // @Autowired, que guardaría los datos en la base de datos 
		                              // Creo ? 
		
	}

	// Vamos de acuerdo al objeto datos, traer un médico de la base de datos 
	private Medico seleccionarMedico(DatosAgendarConsulta datos) {
		// Preguntar primero que el médico no sea null 
		if(datos.idMedico()!=null)
			return medicoRepository.getReferenceById(datos.idMedico());
		if(datos.especialidad()==null)
			throw new ValidacionDeIntegridad("El idMedico no tiene una especialidad disponible .... ");
		return medicoRepository.seleccionarMedicoEspecialidadEnFecha(datos.especialidad(), datos.fecha());
	}
}
