package medvoll.class1.controller;

import java.net.URI;

//import java.awt.print.Pageable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import medvoll.class1.direccion.DatosDireccion;
import medvoll.class1.domain.Medico;
import medvoll.class1.medico.DatosActualizarMedico;
import medvoll.class1.medico.DatosBorrarMedico;
import medvoll.class1.medico.DatosListadoMedico;
import medvoll.class1.medico.DatosRegistroMedico;
import medvoll.class1.medico.DatosRespuestaMedico;

@RestController
@RequestMapping("/medicos")
public class MedicoController {
	
	// Para que Spring use el repository, debemos identificarlo y le colocamos una anotación 
	// que es @Autowired, esto para que Spring lo busque y lo use, de otro lado habría que 
	// colocar un New y eso hace más largo de implementar. ACA EL TUTOR DICE ALGO QUE COMPLICADO
	// QUE EL @Autowiored,LO HA COLOCADO DE MANERA DIDACTICA, PERO QUE NO ES RECOMEDABLE EN LA, 
	// PRÁCTICA, HAGAME ESA 
	
	@Autowired 
	private MedicoRepository medicoRepositorio; 

	/* Que va hacer al momento de mostralo en el Tomca, Post para registrar  	*/
	// El @Valid, recordemos que es para que Spring haga todas las validaciones que se hacen dentro 
	// de DatosRegistroMedico como @NotBlank, @NotNull, los Pathers que hayan puesto y luego de hacerlo
	// si continuar con el proceso de registrar el Medico.
	// UriComponentsBuilder, es un parámetro que me guarda la ruta donde se va a lojar el nuevo registro 
	// y que nos servirá para retornar la ubicación del nuevo registro 
	@PostMapping // @PostMapping se utiliza para mapear solicitudes HTTP POST Esta anotación es parte del módulo 
	             // spring-web de Spring MVC y se utiliza comúnmente en el desarrollo de aplicaciones web para 
	             // gestionar la creación de recursos o el envío de datos al servidor
	
	       // ResponseEntity recibe un dato genérico y para el caso le vamos a decir que nuestro método 
	       // registrarMedico va a responder con un tipo DatosRespuestaMedico 
	public ResponseEntity<DatosRespuestaMedico> registraMedico(@RequestBody @Valid DatosRegistroMedico datosRegistroMedico,
															 UriComponentsBuilder uriComponentsBuilder) {
		//System.out.println(datosRegistroMedico);
		// Guardamos en una variable de tipo Medico los datos dell registro del medico al momento de crear el nuevo 
		// médico 
		Medico medico = medicoRepositorio.save(new Medico(datosRegistroMedico));
		DatosRespuestaMedico datosRespuestaMedico = new DatosRespuestaMedico(medico.getId(), medico.getNombre(), 
                															 medico.getEmail(), medico.getTelefono(), 
                															 medico.getEspecialidad().toString(),
                															 	new DatosDireccion(medico.getDireccion().getCalle(),
                															 				       medico.getDireccion().getDistrito(), 
                															 				       medico.getDireccion().getCiudad(),
                															 				       medico.getDireccion().getNumero(),
                															 				       medico.getDireccion().getComplemento()));
		// URI guarda la url que se crea dinámicamente con con uriComponentsBuilder y buildAndExpand
		URI url = uriComponentsBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
		// Retornamos la url y en el body los datos del nuevo médico 
		return ResponseEntity.created(url).body(datosRespuestaMedico);
	}
	
	// Nos traemos el listado de la tabla Médicos, pero ahora va a listar solo los campos que se requieren, 
	// para hacer esto se mapea una clase tipo Record que se ha llamado DatosListadoMedico, al retornar vemos 
	// que crea un objeto DatosListadoMedico y dentro de DatosListadoMedico, tiene un constructor que recibe como 
	// parámetro un Medico y de allí con un constructor toma los campos que necesita para listarlos. Vemos que al final
	// todo llega aún .toList para ser listado. 
	// es enredado pero el tutor así lo explica. 
	//@GetMapping 
	//public List<DatosListadoMedico> listadoMedicos(){
	//	return medicoRepositorio.findAll().stream().map(DatosListadoMedico::new).toList();
	//}
	
	// Ahora no se hace un listado, se utiliza para Page para paginar los registros, Get para obtener 
	@GetMapping
	// Para que retorne ResponseEntity, se coloca luego de public y en el return, ResponseEntity.ok( y aca lo que ya tenia ) 
	public ResponseEntity<Page<DatosListadoMedico>> listadoMedicos(@PageableDefault(size = 2) Pageable paginacion){ // Paginanción llega desde el cliente
		//return medicoRepositorio.findAll(paginacion).map(DatosListadoMedico::new); // Hace la búsqueda y crea un objeto de 
		                                                                           // DatosListadoMedico por cada registro encontrado
		
		// Al tener que hacer una validación de si un médico tiene activo en 1 o 0, se hace necesario colocar un nombre de procedimiento 
		// que esta vez si hay que crear en  MedicoRepositorio, pero solo el encabezado que Jpa se encarga de colocar el cuerpo de validar 
		// a activo de manera interna, y ahora cuando se inactive un registro no lo mostrará en pantalla 
		return ResponseEntity.ok(medicoRepositorio.findByActivoTrue(paginacion).map(DatosListadoMedico::new)); // Hace la búsqueda y crea un objeto de 
        																		   // DatosListadoMedico por cada registro encontrado
	}	
		
	
	// Para actualizar registros usamos Put, queremos actualizar ya sea el nombre, la dirección o el documento 
	@PutMapping  // Mapeamos el método, debemos crear un nuevo DTO, esta vez DatosActualizarMedico
	             // Como estamos recibiendo datos desde Insomnia, estos vienen desde el body de la aplicación por 
	             // consiguiente debemos colocar la anotación @RequestBody así tomará los datos desde el cliente. 
	             // de lo contrario DatosActualizarMedico tendría valores null 
	@Transactional // @Transactional es como aplicar el commit, para que aplique el cambio en la base de datos
	               // Cuando se haga la actualización la veamos en la tabla con el nuevo dato 
	       // ResponseEntity, en esta método nos va a retornar los datos del resgistro ya actualizado, nos dice 
	       // el tutor se puede retornar la entidad en este caso Médico, pero que no es recomentable por buenas 
	       // prácticas, que se debe retornar un DTO (record) con estos datos actualizados 
	public ResponseEntity actualizaMedico(@RequestBody DatosActualizarMedico datosActualizarMedico) {
		// Como debemos saber que cliente vamos a actualizar, entonces debemos obtener con el id, el médico
		// nuestro DTO nos arroja el id del médico y medicoRepositorio nos trae los datos y los guarda en la variable
		// tipo Medico medico 
		Medico medico = medicoRepositorio.getReferenceById(datosActualizarMedico.id());
		medico.actualizarDatos(datosActualizarMedico);
		// Retorna los datos actualizados con DTO DatosRespuestaMedico, trae los datos desde la entida Médico, pero no 
		// la retorna, se vale de ella para traer los datos ya actualizados 
		return ResponseEntity.ok(new DatosRespuestaMedico(medico.getId(), medico.getNombre(), 
				                                          medico.getEmail(), medico.getTelefono(), medico.getEspecialidad().toString(),
				                                          new DatosDireccion(medico.getDireccion().getCalle(),
				                                                             medico.getDireccion().getDistrito(), 
				                                                             medico.getDireccion().getCiudad(),
				                                                             medico.getDireccion().getNumero(),
				                                                             medico.getDireccion().getComplemento())));
		
	}
	
	// Para borrar el registro usamos Delete, queremos eliminar trayendo el id para saber que médico borrar 
		@DeleteMapping  // Mapeamos el método, para borrar e involucrar DatosBorrarMedico
		             // Como estamos recibiendo datos desde Insomnia, estos vienen desde el body de la aplicación por 
		             // consiguiente debemos colocar la anotación @RequestBody así tomará los datos desde el cliente. 
		             // de lo contrario DatosActualizarMedico tendría valores null 
		@Transactional // @Transactional es como aplicar el commit, para que aplique el cambio en la base de datos
		               // Cuando se haga el borrado veamos en la tabla que ya no está el registro
		public void borrarMedico(@RequestBody DatosBorrarMedico datosBorrarMedico) {
			// Como debemos saber que cliente vamos a borrar, entonces debemos obtener con el id, el médico
			// nuestro DTO nos arroja el id del médico y medicoRepositorio nos trae los datos y los guarda en la variable
			// tipo Medico medico 
			Medico medico = medicoRepositorio.getReferenceById(datosBorrarMedico.id());
			if (medico !=null) {
				medicoRepositorio.deleteById(datosBorrarMedico.id());
			}
		}
		
		//** CUBRO ESTE MÉTODO PORQUE ERA DE MUESTRA DEL COMO PODEMOS ELIMINAR FÍSICAMENTE EL REGISTRO, AHORA 
		//** VEMOS QUE LO INACTIVAMOS Y LISTO, ADEMAS PORQUE FUNCIONA IGUAL QUE EL QUE INACTIVA, LLEVA EL ID DEL 
		//** REGISTRO EN LA URL 
		// Borrar el registro con el método que el tutor nos muestra 
		//@DeleteMapping("/{id}") // El id viene en la Url que se invoca con el Delete desde frontEnd 
		//@Transactional // @Transactional es como aplicar el commit, para que aplique el cambio en la base de datos
                       // Cuando se haga el borrado veamos en la tabla que ya no está el registro
		//public void eliminarMedico(@PathVariable Long id) {  // @PathVariable, nos permite capturar el valor de la Url  y 
			                                                 // guardarlo en el parámetro del método eliminarMedico
		//	Medico medico = medicoRepositorio.getReferenceById(id);
		//	medicoRepositorio.delete(medico); // Hace directamente el borrado usando la entidad médico, no hace falta 
			                                  // un DTO record que retorne el id desde la clase Medico. 	
		//}
		
		// Inactivar el registro, pasar el estado de true a false 
				@DeleteMapping("/{id}") // El id viene en la Url que se invoca con el Delete desde frontEnd 
				@Transactional // @Transactional es como aplicar el commit, para que aplique el cambio en la base de datos
		                       // Cuando se haga el borrado veamos en la tabla que ya no está el registro
				       // Para que retornemos algo al cliente, hemos quitado el void y lo hemos reemplazado por 
				       // ResponseEntity que responde con argumentos http y para que sean ResponseEntity le agregamos .built() y así lo hace 
				       // Para el ejemplo le estamos respondiendo al cliente cuando se inactiva un registro del médico un 204 noContent, 
					   // no retornamos directamente el objeto, al hacerlo con ResponseEntity hacemos un Wrapper de lo que se desea
					   // retornar. 
				public ResponseEntity inactivarMedico(@PathVariable Long id) {  // @PathVariable, nos permite capturar el valor de la Url  y 
					                                                 // guardarlo en el parámetro del método eliminarMedico
					Medico medico = medicoRepositorio.getReferenceById(id);
					medico.inactivoMedico(); // Se crea un método en Médico que solo dice medico.activo = false 
					return ResponseEntity.noContent().build();
				}
				
	  // Consultar los datos de un registro medico 
				@GetMapping("/{id}") // El id viene en la Url que se invoca la consulta desde frontEnd 
				       // Para que retornemos algo al cliente, hemos quitado el void y lo hemos reemplazado por 
				       // ResponseEntity que responde con argumentos http y para que sean ResponseEntity
				       // Hemos colocado en el espacio <> genérico DatoRespuestaMedico para hacer mas específico nuestro método 
				public ResponseEntity<DatosRespuestaMedico> consultaDatosMedico(@PathVariable Long id) {  // @PathVariable, nos permite capturar 
					                                                                                      // el valor de la Url  y guardarlo en el 
					                                                                                      // parámetro del método consultaDatosMedico
					// Obtenemos los datos desde medicoRepositorio del médico con el id para luego llenar los datos del nuevo objeto de cada id que se consulte 
					Medico medico = medicoRepositorio.getReferenceById(id);
					return ResponseEntity.ok(new DatosRespuestaMedico(medico.getId(), medico.getNombre(), 
                            medico.getEmail(), medico.getTelefono(), medico.getEspecialidad().toString(),
                            new DatosDireccion(medico.getDireccion().getCalle(),
                                               medico.getDireccion().getDistrito(), 
                                               medico.getDireccion().getCiudad(),
                                               medico.getDireccion().getNumero(),
                                               medico.getDireccion().getComplemento())));
				}
		
}