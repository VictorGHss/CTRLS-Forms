-- Insere um médico de teste, Dr. House, para fins de desenvolvimento.
-- A senha 'password' foi hasheada usando BCrypt.
-- O token do link é um valor simples para facilitar os testes da API.
INSERT INTO doctors (id, full_name, email, password, specialty, link_token)
VALUES (
           'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', -- ID fixo para consistência.
           'Dr. Gregory House',
           'house@example.com',
           '$2a$10$teCffem/lVduMSVzOKcHIeynRyV/mQrx3LkMwg0XGmriBywHak0ze', -- Hash para "password".
           'Nefrologista e Infectologista',
           'dr-house-form'
       );