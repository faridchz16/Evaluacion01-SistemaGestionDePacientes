package com.hospital.repository;

import com.hospital.model.AtencionResumen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AtencionResumenRepository extends JpaRepository<AtencionResumen, Long> {
    List<AtencionResumen> findByPacienteId(Long pacienteId);
}