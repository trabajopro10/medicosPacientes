package medvoll.class1.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import medvoll.class1.direccion.DatosDireccion;


public record DatosRegistroMedico(
		// Con anotaciones hacemos las validaciones de los campos 
		// @NotNull no deja que se coloque datos no vacios sino nullos
		// la expresión @NotBlank, hace las dos validaciones que no 
		// coloque nada con nullo y tambien que no llegue en blanco
		// por consiguiente se deja solo @NotBlank que hace ambas
		@NotBlank
		String nombre, 
		// Le aplica el blanco y nullo y tambien las reglas del Email 
		@NotBlank
		@Email
		String email, 
		// Le aplica el blanco y nullo
		@NotBlank
		String telefono,
		// Le aplica igual el blanco y el nullo y además una expresión 
		// regular para que solo acepte digitos mínimo 4 y máximo 6
		@NotBlank
		@Pattern(regexp = "\\d{4,6}")
		String documento, 
		// Se coloca N@NotNull, porque Especialidad es un Enum 
		// que igual se declara similar a una clase para representar un conjunto de valores 
		// constantes que son mutuamente excluyentes
		@NotNull
		Especialidad especialidad,
		// Dirección no es un campo, es un objeto, por consiguiente
		// no puede llegar en blanco, su estado real es que puede llegar
		// Nullo, entonces se debe colocar @NotNull
		@NotNull
		@Valid
		DatosDireccion direccion) {

}
