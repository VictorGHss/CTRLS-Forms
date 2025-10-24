package br.dev.ctrls.api.service;

import br.dev.ctrls.api.domain.entity.*;
import br.dev.ctrls.api.domain.entity.enums.PatientFormStatus;
import br.dev.ctrls.api.domain.repository.DoctorClinicRepository; // IMPORTAR
import br.dev.ctrls.api.domain.repository.PatientFormRepository; // Renomeado
import br.dev.ctrls.api.dto.request.PatientFormRequest;
import br.dev.ctrls.api.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID; // IMPORTAR

@Service
@RequiredArgsConstructor
public class PatientFormService {

    private final DoctorClinicRepository doctorClinicRepository;
    private final PatientFormRepository patientFormRepository;

    @Transactional
    public void createPatientForm(UUID linkToken, PatientFormRequest request) {

        // 1. Encontrar a associação médico-clínica pelo token do link
        DoctorClinic doctorClinicAssociation = doctorClinicRepository.findByLinkToken(linkToken)
                .orElseThrow(() -> new ResourceNotFoundException("Formulário inválido ou não encontrado."));

        Clinic clinic = doctorClinicAssociation.getClinic();
        Doctor doctor = doctorClinicAssociation.getDoctor();

        // 2. Mapear o DTO para a Entidade PatientForm
        PatientForm newPatientForm = new PatientForm();

        // Define as chaves estrangeiras
        newPatientForm.setDoctorClinic(doctorClinicAssociation);
        newPatientForm.setDoctorId(doctor.getId());
        newPatientForm.setClinicId(clinic.getId());

        // Mapeia os dados do paciente
        newPatientForm.setPatientName(request.getPatientName());
        newPatientForm.setPatientCpf(request.getPatientCpf());
        newPatientForm.setBirthDate(request.getBirthDate());
        newPatientForm.setPatientEmail(request.getPatientEmail());
        newPatientForm.setPatientPhone(request.getPatientPhone());
        newPatientForm.setReasonForConsultation(request.getReasonForConsultation());
        newPatientForm.setMedicalHistory(request.getMedicalHistory());
        newPatientForm.setCurrentMedications(request.getCurrentMedications());
        newPatientForm.setAllergies(request.getAllergies());

        // Define as informações de consentimento
        if (request.getConsentGiven() != null && request.getConsentGiven()) {
            newPatientForm.setConsentGivenAt(OffsetDateTime.now());
            newPatientForm.setConsentVersion(clinic.getConsentTermVersion()); // Pega a versão da clínica
        } else {
            throw new IllegalArgumentException("Consentimento não fornecido.");
        }

        // Define o status e a data de submissão
        newPatientForm.setStatus(PatientFormStatus.SUBMITTED);
        newPatientForm.setSubmissionDate(OffsetDateTime.now());

        // 3. Salvar o novo formulário no nosso banco de dados
        patientFormRepository.save(newPatientForm);

        // --- Ponto para integração feegow ---
        // try {
        //    byte[] pdfBytes = pdfGeneratorService.generateFormPdf(newPatientForm);
        //    feegowIntegrationService.uploadPdfToPatient(newPatientForm.getPatientCpf(), pdfBytes);
        // } catch (Exception e) {
        //    log.error("Falha na integração com Feegow para formulário {}", newPatientForm.getId(), e);
        //    // Não lançar a exceção para não reverter a transação principal
        // }
    }
}