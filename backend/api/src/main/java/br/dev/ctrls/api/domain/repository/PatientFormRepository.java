package br.dev.ctrls.api.domain.repository;

import br.dev.ctrls.api.domain.entity.PatientForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PatientFormRepository extends JpaRepository<PatientForm, UUID> {
    List<PatientForm> findAllByDoctorIdOrderBySubmissionDateDesc(UUID doctorId);
}