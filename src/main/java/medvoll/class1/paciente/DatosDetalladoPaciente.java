package medvoll.class1.paciente;

import medvoll.class1.domain.Direccion;
import medvoll.class1.domain.Paciente;

public record DatosDetalladoPaciente(String nombre, String email, String telefono, String documentoIdentidad, Direccion direccion) { 
    public DatosDetalladoPaciente(Paciente paciente) { 
        this(paciente.getNombre(), paciente.getEmail(), paciente.getTelefono(), paciente.getDocumentoIdentidad(), paciente.getDireccion()); 
    }
}
