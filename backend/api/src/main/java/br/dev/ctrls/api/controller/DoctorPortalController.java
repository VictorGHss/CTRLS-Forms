package br.dev.ctrls.api.controller;

import br.dev.ctrls.api.domain.entity.User;
import br.dev.ctrls.api.dto.response.PatientFormSummaryResponse;
import br.dev.ctrls.api.service.DoctorPortalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
@Tag(name = "Portal do Médico", description = "Endpoints protegidos para médicos autenticados")
@SecurityRequirement(name = "bearerAuth")
public class DoctorPortalController {

    private final DoctorPortalService doctorPortalService;

    @GetMapping("/forms")
    @Operation(summary = "Lista os formulários submetidos para o médico logado",
            description = "Retorna uma lista resumida dos formulários, opcionalmente filtrada por nome do paciente e/ou período.")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<PatientFormSummaryResponse>> getSubmittedForms(
            @AuthenticationPrincipal User loggedInUser,

            @Parameter(description = "Termo para buscar no início nome do paciente (case-insensitive)")
            @RequestParam(required = false, defaultValue = "") String search,

            @Parameter(description = "Data de início do filtro (formato AAAA-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(description = "Data de fim do filtro (formato AAAA-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<PatientFormSummaryResponse> forms = doctorPortalService.getFormsForDoctor(loggedInUser, search, startDate, endDate);
        return ResponseEntity.ok(forms);
    }

}