package medvoll.class1.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import medvoll.class1.domain.Paciente;

//Extendemos de Jpa para hacer este repositorio, que nos va a permitir crear el CRUD que 
//administrará los datos. Para que este pueda trabajar, requiere dos parámetros uno es Medico la entidad 
//segundo es necesario el tipo de objeto del Id, para el caso es de tipo Long 
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

	// Este método no existe en Jpa,que es para validar que el estado del Médico sea activo 
		// entonces se coloca en el controller findByActivoTrue, así Jpa puede crear dinámicamente el Sql que
		// valide que el activo en Médico esté como debe ser y nos lo presenta como una Pagina de Médico como lo dice 
		// el generico Page<Medico> 
		Page<Paciente> findAllByActivoTrue(Pageable paginacion);
}

