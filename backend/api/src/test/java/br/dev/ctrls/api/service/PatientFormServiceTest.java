package br.dev.ctrls.api.service;

import br.dev.ctrls.api.domain.entity.*;
import br.dev.ctrls.api.domain.entity.enums.PatientFormStatus;
import br.dev.ctrls.api.domain.repository.DoctorClinicRepository;
import br.dev.ctrls.api.domain.repository.PatientFormRepository;
import br.dev.ctrls.api.dto.request.PatientFormRequest;
import br.dev.ctrls.api.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientFormServiceTest {

    // Mock dos repositórios CORRETOS que o serviço usa
    @Mock
    private DoctorClinicRepository doctorClinicRepository;

    @Mock
    private PatientFormRepository patientFormRepository;

    // Injeta os mocks no serviço que estamos a testar
    @InjectMocks
    private PatientFormService patientFormService;

    // Objetos de dados para os testes
    private DoctorClinic testDoctorClinic;
    private PatientFormRequest validRequest;
    private UUID validLinkToken;
    private UUID invalidLinkToken;
    private Clinic testClinic;
    private Doctor testDoctor;


    @BeforeEach // Roda antes de CADA teste
    void setUp() {
        validLinkToken = UUID.randomUUID();
        invalidLinkToken = UUID.randomUUID();

        // Cria as entidades relacionadas necessárias para montar o DoctorClinic
        testClinic = new Clinic();
        testClinic.setId(UUID.randomUUID());
        testClinic.setConsentTermVersion("v1.0"); // Adiciona versão para teste do consentimento

        User testUser = new User(); // Precisa do User para o Doctor
        testUser.setId(UUID.randomUUID());

        testDoctor = new Doctor();
        testDoctor.setId(UUID.randomUUID());
        testDoctor.setUser(testUser); // Liga o Doctor ao User

        // Cria a associação DoctorClinic de teste
        testDoctorClinic = new DoctorClinic();
        testDoctorClinic.setLinkToken(validLinkToken);
        testDoctorClinic.setClinic(testClinic);
        testDoctorClinic.setDoctor(testDoctor);

        // Prepara um DTO de requisição válido, incluindo o consentimento
        validRequest = new PatientFormRequest();
        validRequest.setPatientName("John Doe");
        validRequest.setPatientCpf("123.456.789-00");
        validRequest.setBirthDate(LocalDate.of(1990, 1, 1));
        validRequest.setPatientEmail("john.doe@example.com");
        validRequest.setConsentGiven(true); // Define o consentimento como true
    }

    @Test
    @DisplayName("Deve submeter o formulário com sucesso quando o token do link for válido")
    void createPatientForm_WithValidLinkToken_ShouldSucceed() {
        // Arrange: Configura o mock para retornar a associação quando o token UUID válido for usado
        when(doctorClinicRepository.findByLinkToken(validLinkToken)).thenReturn(Optional.of(testDoctorClinic));

        // Act: Chama o método do serviço com o token UUID
        patientFormService.createPatientForm(validLinkToken, validRequest);

        // Assert: Verifica se 'save' foi chamado no patientFormRepository
        // Usa ArgumentCaptor para capturar e verificar os detalhes da entidade salva
        ArgumentCaptor<PatientForm> formCaptor = ArgumentCaptor.forClass(PatientForm.class);
        verify(patientFormRepository, times(1)).save(formCaptor.capture());

        // Verifica se o formulário salvo tem as associações e detalhes de consentimento corretos
        PatientForm savedForm = formCaptor.getValue();
        assertEquals(testDoctorClinic.getLinkToken(), savedForm.getDoctorClinic().getLinkToken());
        assertEquals(testDoctor.getId(), savedForm.getDoctorId());
        assertEquals(testClinic.getId(), savedForm.getClinicId());
        assertEquals(PatientFormStatus.SUBMITTED, savedForm.getStatus()); // Verifica o status
        assertNotNull(savedForm.getConsentGivenAt()); // Verifica se a data do consentimento foi gravada
        assertEquals("v1.0", savedForm.getConsentVersion()); // Verifica se a versão do consentimento foi gravada
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando o token do link for inválido")
    void createPatientForm_WithInvalidLinkToken_ShouldThrowResourceNotFoundException() {
        // Arrange: Configura o mock para retornar vazio quando o token UUID inválido for usado
        when(doctorClinicRepository.findByLinkToken(invalidLinkToken)).thenReturn(Optional.empty());

        // Act & Assert: Verifica se a exceção correta é lançada
        assertThrows(ResourceNotFoundException.class, () -> patientFormService.createPatientForm(invalidLinkToken, validRequest));

        // Verifica se 'save' NUNCA foi chamado em caso de erro
        verify(patientFormRepository, never()).save(any(PatientForm.class));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando o consentimento não for dado")
    void createPatientForm_WithoutConsent_ShouldThrowIllegalArgumentException() {
        // Arrange: Define o consentimento como false na requisição
        validRequest.setConsentGiven(false);
        // Ainda falta o mock do doctorClinic para a verificação inicial do token
        when(doctorClinicRepository.findByLinkToken(validLinkToken)).thenReturn(Optional.of(testDoctorClinic));

        // Act & Assert: Verifica se IllegalArgumentException é lançada devido ao consentimento
        assertThrows(IllegalArgumentException.class, () -> patientFormService.createPatientForm(validLinkToken, validRequest));

        // Verifica se 'save' NUNCA foi chamado
        verify(patientFormRepository, never()).save(any(PatientForm.class));
    }
}