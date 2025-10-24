package br.dev.ctrls.api.service;

import br.dev.ctrls.api.domain.entity.Doctor;
import br.dev.ctrls.api.domain.entity.PatientForm;
import br.dev.ctrls.api.domain.entity.User;
import br.dev.ctrls.api.domain.repository.DoctorRepository;
import br.dev.ctrls.api.domain.repository.PatientFormRepository;
import br.dev.ctrls.api.domain.repository.specification.PatientFormSpecification;
import br.dev.ctrls.api.dto.response.PatientFormSummaryResponse;
import br.dev.ctrls.api.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorPortalService {

    private final PatientFormRepository patientFormRepository;
    private final DoctorRepository doctorRepository;

    @Transactional(readOnly = true)
    public List<PatientFormSummaryResponse> getFormsForDoctor(User loggedInUser, String search, LocalDate startDate, LocalDate endDate) {

        // 1. Encontrar o registo 'Doctor' associado ao 'User' logado.
        //    Lança exceção se, por algum motivo, um User com role DOCTOR não tiver um registo Doctor associado.
        Doctor doctor = doctorRepository.findByUserId(loggedInUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Dados profissionais do médico não encontrados para o utilizador: " + loggedInUser.getEmail()));

        // Imprime log de debug
        System.out.println("--- DEBUG SERVICE: Searching forms for Doctor ID [" + doctor.getId() + "] with search=[" + search + "], start=[" + startDate + "], end=[" + endDate + "]");

        // 2. Criar a Specification usando o ID do Doctor encontrado
        Specification<PatientForm> spec = PatientFormSpecification.filterBy(
                doctor.getId(),
                search,
                startDate,
                endDate
        );

        // 3. Definir a ordenação
        Sort sort = Sort.by(Sort.Direction.DESC, "submissionDate");

        // 4. Buscar e mapear os resultados usando findAll com Specification
        return patientFormRepository.findAll(spec, sort)
                .stream()
                .map(PatientFormSummaryResponse::fromEntity)
                .collect(Collectors.toList());
    }
}