package br.dev.ctrls.api.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientFormRequest {

    @NotBlank(message = "O nome do paciente não pode estar em branco.")
    @Size(min = 3, max = 255)
    private String patientName;

    @NotBlank(message = "O CPF do paciente não pode estar em branco.")
    @Pattern(regexp = "(^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$)|(^\\d{11}$)", message = "Formato de CPF inválido.")
    private String patientCpf;

    @NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message = "A data de nascimento deve ser no passado.")
    private LocalDate birthDate;

    @NotBlank(message = "O e-mail do paciente não pode estar em branco.")
    @Email(message = "Formato de e-mail inválido.")
    private String patientEmail;

    private String patientPhone;
    private String reasonForConsultation;
    private String medicalHistory;
    private String currentMedications;
    private String allergies;
}