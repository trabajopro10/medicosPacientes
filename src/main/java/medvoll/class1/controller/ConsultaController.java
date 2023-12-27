package medvoll.class1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import medvoll.class1.domain.consulta.AgendaDeConsultaService;
import medvoll.class1.domain.consulta.DatosAgendarConsulta;
import medvoll.class1.domain.consulta.DatosDetalleConsulta;

// @RestController y RequestMapping, son componentes gerenciados por Spring, esto implica que ya quedan instanciados automáticamente, no hay 
// que utilizar new para llamar a ConsultaCotroller desde otro componente de la aplicación 
//@RestController
@Controller  // El tutor ha cambiado a RestController por Controller y dice que es lo mismo 
// Colocar la ruta de acceso Url 
@ResponseBody // Cuando usamos RestController o Controller, se debe asociar a un ResponseBody 
@RequestMapping("/consultas")
public class ConsultaController {
	
	// Agregamos la clase AgendaDeConsultaServicio 
	@Autowired
	private AgendaDeConsultaService service;

	// ResponseEntity es una clase en el marco de Spring que representa toda la respuesta HTTP,
	// incluido el cuerpo, las cabeceras y el código de estado, 
	// OJO *** Para que este método agendar sea encontrado por Spring al momento de ejecutar la consultan con Insomnia, deben tener 
	// el @PostMapping y el @Transactional, sino no muestran nada. 
	@PostMapping
	@Transactional
	public ResponseEntity agendar(@RequestBody @Valid DatosAgendarConsulta datos) {
		System.out.println(datos);
		// Pasamos los datos que estamos recibiendo en Agendar dentro del controlador al servicio
		// que hemos creado AgendaDeConsultaService 
		service.agendar(datos);
		return  ResponseEntity.ok(new DatosDetalleConsulta(null,null,null,null));
	}
}
