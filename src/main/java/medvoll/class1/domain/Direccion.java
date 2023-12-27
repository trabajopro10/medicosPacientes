package medvoll.class1.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import medvoll.class1.direccion.DatosDireccion;

//Aca Lombok, trabaja para crear varias cosas, ejemplo los getters y setters de los campos 
//y los constructores 
@Getter
@NoArgsConstructor // Un constructor sin argumentos 
@AllArgsConstructor // Un constructor con todos los argumentos
@Embeddable 
public class Direccion {
	
	private String calle;
	private String numero;
	private String complemento;
	private String distrito;
	private String ciudad;
	
	
	// Creamos un constructor para llenar los campos de la dirección 
	
	public Direccion(DatosDireccion direccion) {
		// TODO Auto-generated constructor stub
		this.calle = direccion.calle();
		this.ciudad = direccion.ciudad();
		this.complemento = direccion.complemento();
		this.distrito = direccion.distrito();
		this.numero = direccion.numero();
	}


	public Direccion actualizarDatos(DatosDireccion direccion) {
		// TODO Auto-generated method stub
		this.calle = direccion.calle();
		this.ciudad = direccion.ciudad();
		this.complemento = direccion.complemento();
		this.distrito = direccion.distrito();
		this.numero = direccion.numero();
		return this;
	}
	
}
