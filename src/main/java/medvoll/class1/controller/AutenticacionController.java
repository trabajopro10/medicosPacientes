package medvoll.class1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import medvoll.class1.domain.infra.security.DatosJWTtoken;
import medvoll.class1.domain.infra.security.TokenService;
import medvoll.class1.domain.usuarios.DatosAutenticacionUsuario;
import medvoll.class1.domain.usuarios.Usuario;

@RestController
@RequestMapping("/login") // Login donde estamos apuntando al momento de hacer la validación 
                          // de ingreso a la aplicación 
public class AutenticacionController {
	
	@Autowired // Inyectamos AuthenticationManager 
	private AuthenticationManager authenticationManager;
	
	//  Importamo el token local que se ha creado dentro de infra.security, 
	//  donde debe estar la llave que nos permitirá dar seguridad al login 
	//  del usuario. 
	@Autowired // Salía que el token era nullo con el @Autowire si funciona y no dice que sea nullo 
	private TokenService tokenService;
	
	@PostMapping // @PostMapping se utiliza para mapear solicitudes HTTP POST Esta anotación es parte del módulo 
    			 // spring-web de Spring MVC y se utiliza comúnmente en el desarrollo de aplicaciones web para 
    			 // gestionar la creación de recursos o el envío de datos al servidor
	
	            // Debemos colocar @RequestBody porque estamos recibiendo datos desde el cliente y 
	            // @Valid porque esos datos tienen que ser validados 
	public ResponseEntity autenticarUsuario(@RequestBody @Valid DatosAutenticacionUsuario datosAutenticacionUsuario) {
		// Recordemos que cuando vamos a recibir datos del cliente 
		                                    // necesitamos un DTO, osea una clase tipo Record, aca 
		                                    // DatosAutenticacionUsuario es nuestra clase Record DTO
		                                                                 // Data transfer Object 
		// Creamos una variable que es una interfaz dice el tutor del tipo Autehtication y se ha llamado token, 
		// que va a recibir un objeto con los datos de login y clave 
		// ASI ESTABA Authentication token = new UsernamePasswordAuthenticationToken(datosAutenticacionUsuario.login()
		// 		                                                       , datosAutenticacionUsuario.clave());
		Authentication authToken = new UsernamePasswordAuthenticationToken(datosAutenticacionUsuario.login()
                , datosAutenticacionUsuario.clave());
		
		// ASI ESTABA, authenticationManager.authenticate(token); se ha colocado authToken, así se está generando desde
		// datosAutenticacionUsuario, pero ahora para que tome el usuario de forma dinámica y al ya haber modificado
		// TokenService, debemos hacerlo aca tambien para que asuma el cambio asi estaba authenticationManager.authenticate(authToken);
		// Llevamos nuestro authToken a una variable usuarioAutenticado 
		var usuarioAutenticado = authenticationManager.authenticate(authToken);
		// Se agrega una variable para que reciba la generación del Token 
		//var JWTtoken :String = tokenService.generarToken(authToken);
		// En el JWTtoken llamamos el usuario que se ha autenticado y al decir getPrincipal, nos estamos refiriendo a el 
		// el usuario que se está autenticando y para que se comprensible se ha casteado con (Usuario) 
		var JWTtoken  = tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());
		// Dice el tutor que para ser didacticos, va retornar solo por eso un ResponseEntity  
		//return ResponseEntity.ok().build(); Con el token generandose, se retorna con el ResponseEntity 
		return ResponseEntity.ok(new DatosJWTtoken(JWTtoken)); // Retornamos el JWTtoken que se genera 
	}
}
