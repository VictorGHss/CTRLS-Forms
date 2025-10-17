package br.dev.ctrls.api.controller;

import br.dev.ctrls.api.domain.entity.Doctor;
import br.dev.ctrls.api.dto.response.PatientFormSummaryResponse;
import br.dev.ctrls.api.service.DoctorPortalService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
@Tag(name = "Portal do Médico", description = "Endpoints protegidos para médicos autenticados")
@SecurityRequirement(name = "bearerAuth")
public class DoctorPortalController {

    private final DoctorPortalService doctorPortalService;

    @GetMapping("/forms")
    public ResponseEntity<List<PatientFormSummaryResponse>> getSubmittedForms(
            @AuthenticationPrincipal Doctor loggedInDoctor
    ) {
        List<PatientFormSummaryResponse> forms = doctorPortalService.getFormsForDoctor(loggedInDoctor);
        return ResponseEntity.ok(forms);
    }
}