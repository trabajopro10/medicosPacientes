package medvoll.class1.domain.infra.errores;

public class ValidacionDeIntegridad extends RuntimeException {
	// Por defecto esta clase al ser creada extendía de Exception, el tutor lo tiene 
	// extendiendo desde RuntimeException y tambien podría ser desde Throwable, el nos dice que 
	// Throwable responde ante errores y excepciones RuntimException solo responde ante
	// exceptions 
	
	public ValidacionDeIntegridad(String stMensaje) {
		
	}

}
