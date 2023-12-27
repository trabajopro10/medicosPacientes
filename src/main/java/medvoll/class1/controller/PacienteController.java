package medvoll.class1.controller;

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
import medvoll.class1.domain.Paciente;
import medvoll.class1.paciente.DatosActualizacionPaciente;
import medvoll.class1.paciente.DatosDetalladoPaciente;
import medvoll.class1.paciente.DatosListadoPaciente;
import medvoll.class1.paciente.DatosRegistroPaciente;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {
	
	@Autowired 
	private PacienteRepository pacienteRepository; 

	
	@PostMapping
	@Transactional
	public ResponseEntity registrar(@RequestBody @Valid DatosRegistroPaciente datos, UriComponentsBuilder uriBuilder) {
	    var paciente = new Paciente(datos);
	    pacienteRepository.save(paciente);

	    var uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
	    return ResponseEntity.created(uri).body(new DatosDetalladoPaciente(paciente));
	}

	@GetMapping
	public ResponseEntity<Page<DatosListadoPaciente>> listar(@PageableDefault(size = 10, sort = {"nombre"}) Pageable paginacion) {
	    var page = pacienteRepository.findAllByActivoTrue(paginacion).map(DatosListadoPaciente::new);
	    return ResponseEntity.ok(page);
	}

	@PutMapping
	@Transactional
	public ResponseEntity<DatosDetalladoPaciente> actualizar(@RequestBody @Valid DatosActualizacionPaciente datos) {
	    var paciente = pacienteRepository.getReferenceById(datos.id());
	    paciente.actualizarInformacion(datos);

	    return ResponseEntity.ok(new DatosDetalladoPaciente(paciente));
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity eliminar(@PathVariable Long id) {
	    var paciente = pacienteRepository.getReferenceById(id);
	    paciente.eliminar();

	    return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DatosDetalladoPaciente> detallar(@PathVariable Long id) {
	    var paciente = pacienteRepository.getReferenceById(id);
	    return ResponseEntity.ok(new DatosDetalladoPaciente(paciente));
	}
	
	
}
