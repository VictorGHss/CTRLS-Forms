package br.dev.ctrls.api.service;

import br.dev.ctrls.api.domain.repository.UserRepository;
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

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse login(AuthenticationRequest request) {
        // 1. Autentica usando email/senha (o AuthenticationManager usa o userDetailsService)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Busca o User no repositório e passa o UserDetails para generateToken.
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Utilizador não encontrado após autenticação bem-sucedida. Email: " + request.getEmail()));

        // 3. Gera o token JWT para o utilizador encontrado.
        var jwtToken = jwtService.generateToken(user);

        // 4. Retorna o token.
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

}