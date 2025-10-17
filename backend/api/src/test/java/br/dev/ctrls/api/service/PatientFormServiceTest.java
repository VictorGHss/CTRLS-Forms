package br.dev.ctrls.api.service;

import br.dev.ctrls.api.domain.entity.Doctor;
import br.dev.ctrls.api.domain.entity.PatientForm;
import br.dev.ctrls.api.domain.repository.DoctorRepository;
import br.dev.ctrls.api.domain.repository.PatientFormRepository;
import br.dev.ctrls.api.dto.request.PatientFormRequest;
import br.dev.ctrls.api.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

// Habilita a integração do Mockito com o JUnit 5
@ExtendWith(MockitoExtension.class)
class PatientFormServiceTest {

    // Cria um "mock" para o DoctorRepository.
    // Ele não vai ao banco de dados
    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientFormRepository patientFormRepository;

    // Injeta os mocks criados acima na nossa classe de serviço.
    @InjectMocks
    private PatientFormService patientFormService;

    private Doctor testDoctor;
    private PatientFormRequest validRequest;

    // Este metodo é executado antes de cada teste para preparar os dados.
    @BeforeEach
    void setUp() {
        // Prepara um médico de teste
        testDoctor = new Doctor();
        testDoctor.setId(UUID.randomUUID());
        testDoctor.setLinkToken("valid-token");

        // Prepara uma requisição de formulário válida
        validRequest = new PatientFormRequest();
        validRequest.setPatientName("John Doe");
        validRequest.setPatientCpf("123.456.789-00");
        validRequest.setBirthDate(LocalDate.of(1990, 1, 1));
        validRequest.setPatientEmail("john.doe@example.com");
    }

    @Test
    @DisplayName("Deve submeter o formulário com sucesso quando o token do link for válido")
    void createPatientForm_WithValidLinkToken_ShouldSucceed() {
        // Arrange (Organizar/Preparar)
        String validLinkToken = "valid-token";

        // Dizemos ao nosso mock: "Quando o metodo findByLinkToken for chamado com 'valid-token',
        // retorne o nosso médico de teste num Optional."
        when(doctorRepository.findByLinkToken(validLinkToken)).thenReturn(Optional.of(testDoctor));

        // Act (Agir)
        // Executamos o metodo que queremos testar.
        patientFormService.createPatientForm(validLinkToken, validRequest);

        // Assert (Verificar)
        // Verificamos se o metodo save do patientFormRepository foi chamado exatamente 1 vez.
        // Isso prova que a nossa lógica tentou salvar os dados.
        verify(patientFormRepository, times(1)).save(any(PatientForm.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando o token do link for inválido")
    void createPatientForm_WithInvalidLinkToken_ShouldThrowResourceNotFoundException() {
        // Arrange (Organizar/Preparar)
        String invalidLinkToken = "invalid-token";

        // Dizemos ao nosso mock: "Quando o metodo findByLinkToken for chamado com 'invalid-token',
        // retorne um Optional vazio, simulando que o médico não foi encontrado."
        when(doctorRepository.findByLinkToken(invalidLinkToken)).thenReturn(Optional.empty());

        // Act & Assert (Agir e Verificar)
        // Verificamos que, ao executar o metodo com o token inválido,
        // uma exceção do tipo ResourceNotFoundException é lançada.
        assertThrows(ResourceNotFoundException.class, () -> patientFormService.createPatientForm(invalidLinkToken, validRequest));

        // Verificamos também que, neste caso de erro, o método save NUNCA foi chamado.
        verify(patientFormRepository, never()).save(any(PatientForm.class));
    }
}