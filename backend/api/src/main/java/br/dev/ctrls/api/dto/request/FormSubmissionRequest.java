package br.dev.ctrls.api.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class FormSubmissionRequest {

    @NotBlank(message = "O nome do paciente não pode estar em branco.")
    @Size(min = 3, max = 255)
    private String nomePaciente;

    @NotBlank(message = "O CPF do paciente não pode estar em branco.")

    @Pattern(regexp = "(^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$)|(^\\d{11}$)", message = "Formato de CPF inválido.")
    private String cpfPaciente;

    @NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message = "A data de nascimento deve ser no passado.")
    private LocalDate dataNascimento;

    @NotBlank(message = "O e-mail do paciente não pode estar em branco.")
    @Email(message = "Formato de e-mail inválido.")
    private String emailPaciente;

    private String telefonePaciente;
    private String motivoConsulta;
    private String historicoMedico;
    private String medicamentosEmUso;
    private String alergias;
}