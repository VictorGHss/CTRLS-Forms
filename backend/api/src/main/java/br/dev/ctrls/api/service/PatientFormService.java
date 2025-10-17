package br.dev.ctrls.api.service;

import br.dev.ctrls.api.domain.entity.Doctor;
import br.dev.ctrls.api.domain.entity.PatientForm;
import br.dev.ctrls.api.domain.repository.DoctorRepository;
import br.dev.ctrls.api.domain.repository.PatientFormRepository;
import br.dev.ctrls.api.dto.request.PatientFormRequest;
import br.dev.ctrls.api.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class PatientFormService {

    private final DoctorRepository doctorRepository;
    private final PatientFormRepository patientFormRepository;

    @Transactional
    public void createPatientForm(String linkToken, PatientFormRequest request) {
        Doctor doctor = doctorRepository.findByLinkToken(linkToken)
                .orElseThrow(() -> new ResourceNotFoundException("Formulário não encontrado para o token fornecido."));

        PatientForm newPatientForm = new PatientForm();
        newPatientForm.setDoctor(doctor);
        newPatientForm.setPatientName(request.getPatientName());
        newPatientForm.setPatientCpf(request.getPatientCpf());
        newPatientForm.setBirthDate(request.getBirthDate());
        newPatientForm.setPatientEmail(request.getPatientEmail());
        newPatientForm.setPatientPhone(request.getPatientPhone());
        newPatientForm.setReasonForConsultation(request.getReasonForConsultation());
        newPatientForm.setMedicalHistory(request.getMedicalHistory());
        newPatientForm.setCurrentMedications(request.getCurrentMedications());
        newPatientForm.setAllergies(request.getAllergies());
        newPatientForm.setSubmissionDate(OffsetDateTime.now());

        patientFormRepository.save(newPatientForm);
    }
}