package br.dev.ctrls.api.integration;

import br.dev.ctrls.api.domain.repository.PatientFormRepository;
import br.dev.ctrls.api.dto.request.PatientFormRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatientFormControllerIT extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PatientFormRepository patientFormRepository;

    private String requestUrl;

    @BeforeEach
    void setUp() {
        // Monta a URL completa para o nosso servidor de teste com a porta aleatória
        this.requestUrl = "http://localhost:" + serverPort + "/public/forms";

        // Limpa os dados entre os testes para garantir o isolamento
        patientFormRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve submeter um formulário com sucesso e salvá-lo no banco de dados")
    void submitForm_withValidData_shouldReturnCreatedAndPersistData() {
        // Arrange
        String linkToken = "dr-house-form"; // Este médico foi inserido pelo Flyway
        PatientFormRequest validRequest = new PatientFormRequest();
        validRequest.setPatientName("Jane Doe");
        validRequest.setPatientCpf("987.654.321-00");
        validRequest.setBirthDate(LocalDate.of(1985, 5, 15));
        validRequest.setPatientEmail("jane.doe@example.com");

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
                requestUrl + "/" + linkToken + "/submit",
                validRequest,
                Void.class
        );

        // Assert
        // 1. Verifica se a resposta HTTP foi "201 Created"
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // 2. Verifica se os dados foram realmente persistidos no banco de dados
        //    (Esta é a grande vantagem do teste de integração!)
        var formsInDb = patientFormRepository.findAll();
        assertEquals(1, formsInDb.size());
        assertEquals("Jane Doe", formsInDb.getFirst().getPatientName());
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found para um token de link inválido")
    void submitForm_withInvalidLinkToken_shouldReturnNotFound() {
        // Arrange
        String invalidLinkToken = "invalid-token";
        PatientFormRequest validRequest = new PatientFormRequest();
        validRequest.setPatientName("Test User");
        validRequest.setPatientCpf("111.222.333-44");
        validRequest.setBirthDate(LocalDate.of(2000, 1, 1));
        validRequest.setPatientEmail("test@example.com");


        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
                requestUrl + "/" + invalidLinkToken + "/submit",
                validRequest,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
