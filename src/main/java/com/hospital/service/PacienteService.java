package com.hospital.service;

import com.hospital.model.AtencionResumen;
import com.hospital.model.ContactoEmergencia;
import com.hospital.model.Paciente;
import com.hospital.repository.AtencionResumenRepository;
import com.hospital.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final AtencionResumenRepository atencionRepository;

    public PacienteService(PacienteRepository pacienteRepository, AtencionResumenRepository atencionRepository) {
        this.pacienteRepository = pacienteRepository;
        this.atencionRepository = atencionRepository;
    }

    @Transactional
    public Paciente registrar(Paciente paciente) {
        if (pacienteRepository.existsByNumeroDocumento(paciente.getNumeroDocumento())) {
            throw new IllegalArgumentException("Error: El documento " + paciente.getNumeroDocumento() + " ya se encuentra registrado.");
        }
        paciente.setEstado("ACTIVO");

        if (paciente.getContactos() != null) {
            for (ContactoEmergencia contacto : paciente.getContactos()) {
                contacto.setPaciente(paciente);
            }
        }
        return pacienteRepository.save(paciente);
    }

    @Transactional(readOnly = true)
    public Paciente obtenerPorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el paciente con ID: " + id));
    }

    @Transactional
    public Paciente actualizar(Long id, Paciente datos) {
        Paciente paciente = obtenerPorId(id);

        paciente.setNombres(datos.getNombres());
        paciente.setApellidoPaterno(datos.getApellidoPaterno());
        paciente.setApellidoMaterno(datos.getApellidoMaterno());
        paciente.setTelefono(datos.getTelefono());
        paciente.setCorreo(datos.getCorreo());
        paciente.setDireccion(datos.getDireccion());
        paciente.setFechaNacimiento(datos.getFechaNacimiento());

        if (datos.getContactos() != null) {
            paciente.getContactos().clear();
            for (ContactoEmergencia contacto : datos.getContactos()) {
                contacto.setPaciente(paciente);
                paciente.getContactos().add(contacto);
            }
        }
        return pacienteRepository.save(paciente);
    }

    @Transactional(readOnly = true)
    public List<AtencionResumen> obtenerAtenciones(Long pacienteId) {
        if (!pacienteRepository.existsById(pacienteId)) {
            throw new RuntimeException("No se encontró el paciente con ID: " + pacienteId);
        }
        return atencionRepository.findByPacienteId(pacienteId);
    }
}