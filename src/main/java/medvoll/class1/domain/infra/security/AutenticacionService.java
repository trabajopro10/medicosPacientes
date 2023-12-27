package medvoll.class1.domain.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import medvoll.class1.domain.usuarios.UsuarioRepository;

@Service // Con esta anotación Spring Scanea esta clase porque es un servicio 
         // de nuestra apliación 
public class AutenticacionService implements UserDetailsService {
	                              // La interface UserDetailsService, la usa Spring para administrar
	                              // el login de los usuarios de manera interna, es propia de Spring
	
	// Para poder cargar por nombre de usuario, necesitamos el UsuarioRepository, así que los declaramos
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	// Implementa este método loadUserByUsername que nos dice: cargar usuario por nombre de usuario
	@Override  
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		// Retornamos la busqueda de usuario por el username, este findByUsername, lo debemos colocar 
		// dentro de UsuarioRepository, para que sea creado su cuerpo con JPA y podamos utilizarlo 
		return usuarioRepository.findBylogin(username);
	}
}
