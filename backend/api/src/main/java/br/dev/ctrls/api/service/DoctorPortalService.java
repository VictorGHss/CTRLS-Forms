package br.dev.ctrls.api.service;

import br.dev.ctrls.api.domain.entity.Doctor;
import br.dev.ctrls.api.domain.repository.PatientFormRepository;
import br.dev.ctrls.api.dto.response.PatientFormSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorPortalService {

    private final PatientFormRepository patientFormRepository;

    public List<PatientFormSummaryResponse> getFormsForDoctor(Doctor doctor) {
        // Usa o metodo do repositório para buscar os formulários do médico logado.
        return patientFormRepository.findAllByDoctorIdOrderBySubmissionDateDesc(doctor.getId())
                .stream()
                // Converte cada entidade PatientForm para o DTO de resumo.
                .map(PatientFormSummaryResponse::fromEntity)
                .collect(Collectors.toList());
    }
}