package medvoll.class1.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import medvoll.class1.domain.Medico;
import medvoll.class1.medico.Especialidad;

// Extendemos de Jpa para hacer este repositorio, que nos va a permitir crear el CRUD que 
// administrará los datos. Para que este pueda trabajar, requiere dos parámetros uno es Medico la entidad 
// segundo es necesario el tipo de objeto del Id, para el caso es de tipo Long  
public interface MedicoRepository extends JpaRepository<Medico, Long>  {

	// Este método no existe en Jpa,que es para validar que el estado del Médico sea activo 
	// entonces se coloca en el controller findByActivoTrue, así Jpa puede crear dinámicamente el Sql que
	// valide que el activo en Médico esté como debe ser y nos lo presenta como una Pagina de Médico como lo dice 
	// el generico Page<Medico> 
	Page<Medico> findByActivoTrue(Pageable paginacion);

	// Debemos usar la anotación @Query, para buscar los Médicos activos y con esa especialidad, acá hay varias cosas 
	// el order by rand, ordena de forma aleatoria, el limit quiere decir que se limita a un único médico, solo me debe 
	// devolver un médico que cumpla con las condiciones, por ejemplo vemos que dice not in, dentro de una subconsulta
	// que selecciona los médicos que estén dentro de una fecha, osea que ya tengan una consulta programada para esa fecha
	// esos médicos no los tendrá en cuenta. 
	@Query("""   
			Select m from Medico m
			Where m.activo = 1 and 
			      m.especialidad=:especialidad and 
			      m.id not in ( 
			      	Select c.medico.id from Consulta c
			      		   c.data =:fecha
			      )    
			order by rand()
			limit 1
			""")
	Medico seleccionarMedicoEspecialidadEnFecha(Especialidad especialidad, LocalDateTime fecha);
     // Algo importante esque este método se llama de forma convencional, porque tiene la anotación
	// @Query y la consulta que va a ejecutar con los parámetros que vemos en en los parentesis, 
	// pero si no tuviera eso, el nombre debe ser acorde a JPA que indentifique lo que este quiere
	// hacer así como el findByActivoTrue de la parte superior. 
}
