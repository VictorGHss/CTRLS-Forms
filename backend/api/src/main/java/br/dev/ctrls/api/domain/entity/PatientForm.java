package br.dev.ctrls.api.domain.entity;

import br.dev.ctrls.api.domain.entity.enums.PatientFormStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "patient_forms")
@Data
@NoArgsConstructor
public class PatientForm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_clinic_token", nullable = false)
    private DoctorClinic doctorClinic;

    // Campos desnormalizados para facilitar queries
    @Column(name = "doctor_id", nullable = false)
    private UUID doctorId;

    @Column(name = "clinic_id", nullable = false)
    private UUID clinicId;

    // --- Dados do Paciente ---
    @Column(name = "patient_name", nullable = false)
    private String patientName;

    @Column(name = "patient_cpf", nullable = false)
    private String patientCpf;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "patient_email", nullable = false)
    private String patientEmail;

    @Column(name = "patient_phone")
    private String patientPhone;

    @Column(name = "reason_for_consultation", columnDefinition = "TEXT")
    private String reasonForConsultation;

    @Column(name = "medical_history", columnDefinition = "TEXT")
    private String medicalHistory;

    @Column(name = "current_medications", columnDefinition = "TEXT")
    private String currentMedications;

    @Column(columnDefinition = "TEXT")
    private String allergies;

    // --- Consentimento ---
    @Column(name = "consent_given_at")
    private OffsetDateTime consentGivenAt;

    @Column(name = "consent_version")
    private String consentVersion;

    // --- Status e Timestamps ---
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PatientFormStatus status = PatientFormStatus.SUBMITTED;

    @Column(name = "submission_date", nullable = false)
    private OffsetDateTime submissionDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}