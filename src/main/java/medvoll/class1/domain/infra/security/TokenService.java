package medvoll.class1.domain.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import medvoll.class1.domain.usuarios.Usuario;

@Service // Para que Spring lo tenga escaneado, lo anotamos con @Service 
         // esté srping pendiente de cualquier cambio 
public class TokenService {
	
	//Variable de ambiente para tener la clave de como validar nuestro Token 
	@Value("${api.security.secret}")
	private String apiSecret;

	// Debemos retornar un String entonces en el código dice String token = JWT.create
	// lo cambiamos por return JWT.create() 
	
	// Se agrega como parámetro la clase que tiene los datos del usuario. 
	public String generarToken(Usuario usuario) { 
		// Código traido desde el Github de Auth0, la empresa que nos está proveiendo el JWT 
		try {
		   // Algorithm algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
		   // La linea anterior, asi estaba declarado, pero el usa HMAC256, dice el tutor
		   // que esto no es una buena práctica, porque esto es visual en el código, se puede 
		   // ver, pero que para fines didacticos lo vamos hacer. 
			 Algorithm algorithm = Algorithm.HMAC256 (apiSecret); // Validamos la firma del token 
			//Retornamos un String, entonces quitamos el String token = y lo cambiamos por 
			 // retunr 
		    return JWT.create()
		   //String token = JWT.create()
		   //Como trabajaría el JWT, vemos aca que está emitiendo el Token auth0, pero 
		   //colocaremos que lo emite el nombre parecido al de nuestra base de datos  
		        //.withIssuer("auth0")
		    	.withIssuer("voll med")
		    	// Agregamos .WithSubject
		    	.withSubject(usuario.getLogin()) // En lugar de colocar el nombre del usuario
		    	                                 // vamos a dejarlo dinámico
		    	.withClaim("id", usuario.getId()) // Queremos retornar el id del usuario al cliente
		    	.withExpiresAt(generarFechaExpiracion()) // Se da una fecha de expiración del token 
		    	                                 // Se crea el método generarFechaExpiracion 
		        .sign(algorithm);
		} catch (JWTCreationException exception){
		    // Invalid Signing configuration / Couldn't convert Claims.
			// DICE EL TUTOR QUE EST CATCH NO SE DEBE DEJAR SIN RETORNAR NADA,
			// QUE ALMENOS LE VAMOS A COLOCAR 
			throw new RuntimeException();
		}
	}

	// Creamos un Método que llamará el subject, quien es el login que se conectará 
	// la aplicación, recordemos que es JWT (Json Web Token), tiene el mismo
	// algoritmo de cifrado y utiliza en lugar de clave publica y privada el 
	// apisecret que esta en nuestro archivo de propiedades y que se guarda 
	// en una variable del sistema. 
	
	public String getSubject(String token) {
		DecodedJWT verifier = null;
		try {
		    Algorithm algorithm = Algorithm.HMAC256(apiSecret); // Validamos la firma del Token 
		    verifier = JWT.require(algorithm)
		        // specify an specific claim validations
		        .withIssuer("voll med")// Valida que este sea quien emite el token 
		        // reusable verifier instance
		        .build().verify(token);	     
		    verifier.getSubject();
		} catch (JWTVerificationException exception){
		    // Invalid signature/claims
			System.out.println(exception.toString() + "Exception en la verficación, hubo problemas en la validación ");
		}
		// Se valida que el subject no venga null, de ser así se levanta una exception 
		// en tiempo de ejecución. 
		if(verifier.getSubject() ==null) {
			throw new RuntimeException("Verifier invalido .. no puede verificar el Subject ");
		}
		// Al retornar el Subject el usuario que se autentica, con un token válido
		return verifier.getSubject();
	}
	
	// El método generar una fecha de expiración de 2 horas y lo hace en una zona que dice el tutor 
	// la determina el ZoneOffset y al colocar -05, se está refiriendo a sudamerica 
	private Instant generarFechaExpiracion() {
		// TODO Auto-generated method stub
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));
	}
}
