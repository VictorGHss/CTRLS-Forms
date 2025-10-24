package br.dev.ctrls.api.controller;

import br.dev.ctrls.api.dto.request.PatientFormRequest;
import br.dev.ctrls.api.service.PatientFormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID; // IMPORTAR

@RestController
@RequestMapping("/public/forms")
@RequiredArgsConstructor
@Tag(name = "Formulário Público", description = "Endpoint para pacientes submeterem formulários")
public class PatientFormController {

    private final PatientFormService patientFormService;

    @PostMapping("/{linkToken}/submit")
    @Operation(summary = "Submete um formulário pré-consulta preenchido pelo paciente")
    public ResponseEntity<Void> submitPatientForm(
            @PathVariable UUID linkToken, // MUDA PARA UUID
            @Valid @RequestBody PatientFormRequest request) {

        patientFormService.createPatientForm(linkToken, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}