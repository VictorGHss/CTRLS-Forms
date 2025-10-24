package br.dev.ctrls.api.dto.response;

import br.dev.ctrls.api.domain.entity.PatientForm;
import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class PatientFormSummaryResponse {

    private UUID id;
    private String patientName;
    private OffsetDateTime submissionDate;

    // O método de fábrica continua válido, pois os campos existem na entidade PatientForm
    public static PatientFormSummaryResponse fromEntity(PatientForm patientForm) {
        return PatientFormSummaryResponse.builder()
                .id(patientForm.getId())
                .patientName(patientForm.getPatientName())
                .submissionDate(patientForm.getSubmissionDate())
                .build();
    }
}