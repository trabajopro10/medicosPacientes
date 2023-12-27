package medvoll.class1.domain.consulta;

import org.springframework.data.jpa.repository.JpaRepository;

// Extendemos de Jpa para hacer este repositorio, que nos va a permitir crear el CRUD que 
// administrará los datos. Para que este pueda trabajar, requiere dos parámetros uno es Medico la entidad 
// segundo es necesario el tipo de objeto del Id, para el caso es de tipo Long  
public interface ConsultaRepository extends JpaRepository<Consulta, Long>  {

	
}
