package medvoll.class1.domain.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Al colocar esta anotación @Configuration, Spring estará escaneando u
// observando permanentemente esta clase, al igual que lo haría si fuera 
// @Service, pero en su orden se estan escaneando siempre en primer lugar 
// los objetos @Configuration, porque se sobre entiende que son prerrequisitos 
// para que otros objetos se creen dentro de la aplicación 
@Configuration
@EnableWebSecurity // Con esta anotación, le estamos diciendo a Spring que sobre escriba 
                   // con SecurityFilterChain el comportamiento de autenticación de 
                   // seguridad que nosotros queremos implementar y dejarnos acceder a los recursos 
				   // y no muestre nuevamente el error 401 sin autorización (Unauthorize) 
public class SecurityConfigurations {
	
	// Necesitamos que SecurityConfigurations, vea nuestro SecurityFilter primer que el default de Spring 
	// para eso declaramos una variable del tipo SecurityFilter que nos permita utlizarlo dentro del 
	// securityFilterChain, para eso debemos inyectar la variable con @Autowired 
	
	@Autowired
	private SecurityFilter securityFilter;
	
	@SuppressWarnings("removal") // Quitó los llamados de atención para .and().authorizeHttpRequests()
	                             // que lo vemos abajo antes del .requestMatchers(HttpMethod.POST, "/login").permitAll()
	@Bean // @Bean lo que hace es cargar esta clase en el contenedor de Spring, lo inyecta, por que no es propia de el 
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				// Con las siguientes líneas, estamos diciendole a Spring que no cierre el login cuando se necesita 
				// generar una nuevo token de autorización y que si es un nuevo usuario igual le permite acceder a 
				// un token que le autorice validar el usuario y la clave y el acceso a la aplicación con un 
				// token válido
				.and().authorizeHttpRequests()
	            .requestMatchers(HttpMethod.POST, "/login")
	            .permitAll()   // Permite que se pueda acceder a autenticar sin problema        
	            .anyRequest() // Atiende cualquier solicitud 
	            .authenticated() // Aca se hace la autenticación como tal 
	            .and()
	            // Agregamos un tipo de filtro que hemos creado, un securityFilter antes de cualquier filtro que 
	            // tenga Spring, de esta manera le decimos a Spring que tenga encuenta nuestro filtro primero. 
	            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
	            .build(); // Construya el objeto
		// Cómo leer todas las líneas anteriores desde el STATELESS, se diría que: La política de creación es STATELESS 
		// y cada requestMatchers o sea cada solicitud que se del tipo POST y con la ruta login, permitale a todos 
		// permitAll(). Posteriormente le agrega que todos las solicitudes cualquiera de ellas anyRequest, deben 
		// ser autenticados authenticated, al final construya el objeto and().build()
		
		// CSRF (Cross-Site Request Forgery), es un tipo de protección que impide que un sitio web pudiera hacer 
		// peticiones maliciosas a otro sitio web que fuese seguro
    }
	
	// Se ha inyectado dentro del contenedor de Spring, porque al crear la clase AuthenticationController
	// dentro de ella se ha llamado private AuthenticationManager authenticationManager y un error se genera
	// porque dice que el contenedor de Spring lo desconoce y debe ser inyectado con @Bean para poder trabajar 
	// entonces se hace aca en SecurityConfigurations, esto porque el error dice que debe ser agregado en 
	// nuestra configuración 
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
		
	}
	
	//Para que Spring se conecte a la base de datos y pueda validar la clave tal cual como está dentro de la 
	//tabla login, es necesario que retornemos dentro de SecurityConfiguration un PasswordEncoder 
	//PasswordEncoder lo que hace es encriptar nuestro password para compararlo con el que está en la base 
	//de datos del usuario que está tratando de ingresar. 
	@Bean // Recordemos que Bean es para que sea inyectado a Spring y lo tengamos disponible para usarlo 
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		
	}
}
