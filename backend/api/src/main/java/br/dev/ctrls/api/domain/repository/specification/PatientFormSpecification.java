package br.dev.ctrls.api.domain.repository.specification;

import br.dev.ctrls.api.domain.entity.PatientForm;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PatientFormSpecification {

    public static Specification<PatientForm> filterBy(
            UUID doctorId,
            String patientNameSearch,
            LocalDate startDate,
            LocalDate endDate
    ) {
        // A lambda expression implementa a interface Specification<PatientForm>
        return (root, query, criteriaBuilder) -> {
            // 'root' representa a entidade PatientForm
            // 'query' permite construir a consulta (ex: adicionar ordenação)
            // 'criteriaBuilder' é usado para criar as condições (predicates)

            List<Predicate> predicates = new ArrayList<>();

            // 1. Filtro OBRIGATÓRIO pelo ID do médico
            predicates.add(criteriaBuilder.equal(root.get("doctorId"), doctorId));

            // 2. Filtro OPCIONAL pelo nome do paciente
            if (StringUtils.hasText(patientNameSearch)) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("patientName")),
                        patientNameSearch.toLowerCase() + "%"
                ));
            }

            // 3. Filtro OPCIONAL pela data de início
            if (startDate != null) {
                OffsetDateTime startDateTime = startDate.atStartOfDay().atOffset(ZoneOffset.UTC);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("submissionDate"), startDateTime));
            }

            // 4. Filtro OPCIONAL pela data de fim
            if (endDate != null) {
                OffsetDateTime endDateTime = endDate.atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("submissionDate"), endDateTime));
            }

            // Combina todas as condições criadas com um 'AND' lógico
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}