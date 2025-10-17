package br.dev.ctrls.api.service;

import br.dev.ctrls.api.domain.repository.DoctorRepository;
import br.dev.ctrls.api.dto.auth.AuthenticationRequest;
import br.dev.ctrls.api.dto.auth.AuthenticationResponse;
import br.dev.ctrls.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final DoctorRepository doctorRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse login(AuthenticationRequest request) {
        // 1. O AuthenticationManager usa os Beans (UserDetailsService e PasswordEncoder)
        //    para validar se o e-mail e a senha correspondem a um utilizador no banco de dados.
        //    Se as credenciais estiverem incorretas, ele lança uma exceção.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Se a autenticação for bem-sucedida, busca o utilizador para gerar o token.
        var doctor = doctorRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado após a autenticação."));

        // 3. Gera o token JWT para o médico encontrado.
        var jwtToken = jwtService.generateToken(doctor);

        // 4. Retornamos o token encapsulado na classe de resposta.
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}