package medvoll.class1.direccion;

import jakarta.validation.constraints.NotBlank;

public record DatosDireccion(
		// Con anotaciones hacemos las validaciones de los campos 
		// @NotNull no deja que se coloque datos no vacios sino nullos
		// la expresión @NotBlank, hace las dos validaciones que no 
		// coloque nada con nullo y tambien que no llegue en blanco
		// por consiguiente se deja solo @NotBlank que hace ambas
		@NotBlank
		String calle, 
		@NotBlank
		String distrito, 
		@NotBlank                  
		String ciudad, 
		@NotBlank
		String numero, 
		@NotBlank
		String complemento  ) {
}
