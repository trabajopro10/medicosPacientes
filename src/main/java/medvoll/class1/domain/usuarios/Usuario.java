package medvoll.class1.domain.usuarios;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

//Datos de persistencia 
@Table (name = "usuarios")
@Entity(name = "Usuario")
@Getter
//Aca Lombok, trabaja para crear varias cosas, ejemplo los getters y setters de los campos 
//y los constructores 
@NoArgsConstructor // Un constructor sin argumentos 
@AllArgsConstructor // Un constructor con todos los argumentos
@EqualsAndHashCode(of = "id") // Comparar los Id entre Usuarios ( Seguro luego lo entiendo) 
public class Usuario implements UserDetails {

	// Para que genere de forma automática el Id 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String login;
	private String clave;
	
	// Con GrantedAuthority, le decimos a Spring que la cuenta tiene rol de usuario y se 
	// puede conectar 
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    return List.of(new SimpleGrantedAuthority("ROLE_USER"));
	}
	
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		//return null;
		return clave; // Aca retornamos la clave con el campo declarado 
		              // en la clase 
		// En la clave hace algo adicional y es utilizar de SecurityConfiguration el método 
		// BCryptPasswordEncoder passwordEncoder, para validar el tipo encriptación 
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		//return null;
		return login; // Aca retornamos el login que es el campo 
		              // declarado 
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		//return false;
		return true;// La cuenta no expiro 
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		//return false;
		return true; // La cuenta no esta bloqueda 
	}
	@Override 
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		//return false;
		return true; // Para que no diga que la credencial no está espirada, colocamos true 
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		//return false;
		return true; // El usuario está habilitado para ingresar 
	}
}
