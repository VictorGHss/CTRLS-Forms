package br.dev.ctrls.api.domain.repository;

import br.dev.ctrls.api.domain.entity.DoctorClinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorClinicRepository extends JpaRepository<DoctorClinic, UUID> {
    Optional<DoctorClinic> findByLinkToken(UUID linkToken);

    List<DoctorClinic> findByDoctorId(UUID doctorId);
    List<DoctorClinic> findByClinicId(UUID clinicId);
    Optional<DoctorClinic> findByDoctorIdAndClinicId(UUID doctorId, UUID clinicId);
}