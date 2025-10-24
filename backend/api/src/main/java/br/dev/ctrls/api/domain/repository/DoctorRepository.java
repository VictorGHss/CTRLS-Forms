package br.dev.ctrls.api.domain.repository;

import br.dev.ctrls.api.domain.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    Optional<Doctor> findByUserId(UUID userId);
    Optional<Doctor> findByProfessionalCouncilTypeAndProfessionalCouncilNumberAndProfessionalCouncilState(String type, String number, String state);
}