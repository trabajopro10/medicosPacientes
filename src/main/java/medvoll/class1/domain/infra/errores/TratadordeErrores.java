package medvoll.class1.domain.infra.errores;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

// Agregamos una anotación @RestControllerAdvice, que está relacionada con programación orientada a aspectos 
// posteriormente debemos buscar este concepto para entenderlo. Dice el tutor que todas formas, actua como un 
// proxi, para el caso se situa al frente de los controllers y toma mensajes que puede tratar o intersectar, 
// para el caso exceptions que sean lanzadas 
@RestControllerAdvice // con el @RestControllerAdvice, se pueden crear varios tipos de @ExceptionHandler, dependiendo
                      // del error que deseamos levantar 
public class TratadordeErrores {
	
// CUANDO HAY DATOS QUE SE ESTÁN CONSULTANDO Y NO EXISTEN 
	@ExceptionHandler(EntityNotFoundException.class) // Para que pueda ser llamada dentro de las clases cuando 
	                  // se presente este tipo de errores notDataFound, coloco dentro de los parentesis, el tipo de 
	                  // exception que quiero tratar, para el caso es EntityNotFoundException
	public ResponseEntity tratarError404() {
		// Este ResponseEntity recordemos nos facilita retornar un código de http en el caso que se presente, 
		// no se coloca nada dentro del generico de ResponseEntity, porque esta vez, atiende a todas las clases 
		return ResponseEntity.notFound().build();
	}
	
	// CUANDO EL CLIENTE ENVÍA ARGUMENTOS ERRONEOS O FALTAN ARGUMENTOS, PARA REGISTRAR UN CAMPO POR EJEMPLO 
	@ExceptionHandler(MethodArgumentNotValidException.class) // Para que pueda ser llamada dentro de las clases cuando 
    // se presente este tipo de errores badRequest, colocamos dentro de los parentesis, el tipo de 
    // exception que quiero tratar, para el caso es MethodArgumentNotValidException (hay argumentos no válidos o 
	// faltan argumentos desde la petición del cliente). 
	
	// Agregamos como en las excepciones la exception y se guarda en un campo llamado e, este tendra la información del error
	// que debemos buscar y formatearle al cliente. 
	public ResponseEntity tratarError400(MethodArgumentNotValidException e) {
			
		List errores = e.getFieldErrors().stream().map(DatosErrorValidacion::new).toList();
	 		
		// Este ResponseEntity recordemos nos facilita retornar un código de http en el caso que se presente, 
		// no se coloca nada dentro del generico de ResponseEntity, porque esta vez, atiende a todas las clases 
		return ResponseEntity.badRequest().body(errores);
	}
	
	// clase que se crea aca no mas, para ayudar con cualquier mensaje de error. 
	private record DatosErrorValidacion(String campo, String error) {
		public DatosErrorValidacion(FieldError error) {
			this(error.getField(), error.getDefaultMessage());
		}	
	}

}
