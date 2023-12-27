package medvoll.class1.domain.usuarios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

//Extendemos de Jpa para hacer este repositorio, que nos va a permitir crear el CRUD que 
//administrará los datos. 
//Para que este pueda trabajar, requiere dos parámetros uno es Usuario de la entidad 
//segundo es necesario el tipo de objeto del Id, para el caso es de tipo Long  
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	// Con un tipo de UserDetails, nos va a encontrar un usuario por nombre de usuario,
	// Jpa creará de forma interna el cuerpo de este método para que podamos utilizarlo 
	UserDetails findBylogin(String username);

}
