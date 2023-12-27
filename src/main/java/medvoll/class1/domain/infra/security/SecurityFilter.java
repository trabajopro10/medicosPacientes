package medvoll.class1.domain.infra.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import medvoll.class1.domain.usuarios.UsuarioRepository;

// Esta clase no es un repository, tampoco es un controller, no es un servicio, entonces la anotación 
// para que Spring la tenga referenciada es colocarle @Component, Component es el estereotipo más genérico 
// de Spring para escanear un componente y agregarlo a su contexto. Service, Controller y Repository son 
// estereotipos basados en Component, nuestro filtro simplemente no es ninguno de esos, solo es un Component
@Component
// El tutor había colocado un Implements primero, pero Spring lo corrigió y le dijo que esta era una clase 
// abstracta, recordemos que las clases abstractas, nos dan la cabecera del método y nosotros solo 
// creamos el cuerpo de la clase 
public class SecurityFilter extends OncePerRequestFilter {
	 // Al colocar extends OncePerRequestFilter, nos obligó a implementar el método doFilterInternal, que veamos
	 // tiene tres parámetros que son importantes, 1- la solicitud resquest, la Respuesta response y 
	 // el FilterChain (la cadena de filtros que debe pasar nuestro request para llegar a los datos y obtener un 
	 // response ) 
	
	// Declaramos el TokenService, porque alli estamos trayendo el subject (Login del usuario) 
	// el @Autowired para que Spring lo tenga en su almacen y lo encuentre cuando lo llamamos 
	@Autowired 
	private TokenService tokenService;
	
	// Declaramos el UsuarioRepository, porque usando el  subject (Login del usuario) 
	// usaremos el findByLogin(subject), que obliga al usuario que tiene un token válido 
	// a loguearse dentro de la aplicación 
	// el @Autowired para que Spring lo tenga en su almacen y lo encuentre cuando lo llamamos 
	@Autowired 
	UsuarioRepository usuarioRepository;
	
   
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		System.out.println("El filtro está trabajando  \n");
		// Para validar el token, primero debemos obtenerlo del header. Se ha agregado que remplace a Bearer (Portadora)
		// por nada, así saldrá solo el token sin el Bearer, este Bearer sale porque insomnia lo tiene dentro del 
		// encabezado al llamar el Authorization 
		System.out.println(" ANTES DEL TOKEN ");
		
			// CON TODA ESTE CÓDIGO QUE SIGUE, VEMOS QUE YA PERMITE GENERAR EL TOKEN SI NO HAY UN USUARIO VALIDO O SI EL USUARIO
			// NO SE HA LOGUEADO AÚN, ADEMÁS QUE SI YA TIENE TOKEN VÁLIDO QUE LE OBLIGUE A INICIAR SESION EN LA APLICACIÓN Y 
			// PERMITA HACER TRANSACCIONES COMO EJECUTAR EL LISTADO DE MÉDICOS DE LA BASE DE DATOS. 
		    // quito token y colocamos authHeader 
			var authHeader = request.getHeader("Authorization");
			// Es necesario que permita generar un token nuevo si este ya expiró, entonces solo trabajará con el token si este ya 
			// ha sido generado, de lo contratio el doFilter(request, response) nos generá uno nuevo si interrumpir 
			// el curso normal del código. 
			if (authHeader!=null) {
				// Se quita al token la palabra Bearer para que solo pase el dato encriptado que es el Token de autorización 
				var token = authHeader.replace("Bearer ","");
				System.out.println("Este es nuestro Token " +  token);
				// Aca le enviamos el token, para que nos muestre que si ha podido encontrar el Subject de ese token. 
				// Tambien hay que validar si ese usuario que estamos validando con getSubject si este logueado 
				System.out.println(" ES EL SUBJECT .." + tokenService.getSubject(token));
				// Obtenemos el subject, porque necesitamos que se loguee el usuario si el token es válido y se haga
				var subject = tokenService.getSubject(token);
				// Válidamos que el subject no llegue nullo y podamos forzar el login a la aplicación 
				if (subject != null) {
					// Esta variable de usuario por si sola no funciona, para tener la autorización de login, debemos 
					// colocar dos líneas mas luego de usuario que son autenthication y SecurityContextHolder 
					var usuario = usuarioRepository.findBylogin(subject); // Aca encontramos el usuario en la base de datos 
					// con estas dos líneas, le decimos a Spring que este login o este usuario es válido.
					var authentication = new UsernamePasswordAuthenticationToken(usuario,null,usuario.getAuthorities());
					// con authentication, validamos que el usuario existe y forzamos el inicio de sesion, porque 
					// SecurityContextHolder.getContext().setAuthentication(authentication); le dice a Spring que el 
					// usuario ya está autenticado que puede acceder a la aplicación sin problem, e inicia la sesión.
					SecurityContextHolder.getContext().setAuthentication(authentication);
					// Con este SecurityContextHolder, le decimos al método SecurityFilterChain  dentro de la clase 
					// SecurityConfigurations que este usuario ya está autenticado 
				}
			} 
			// Hacemos el filtro intercepta la solicitud request que hace el cliente y se obtiene la respuesta con response 
			filterChain.doFilter(request, response);
		}
}
