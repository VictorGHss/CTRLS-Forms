-- === SUPER ADMIN ===
INSERT INTO users (id, email, password, full_name, role, is_active)
VALUES (
           'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a01',
           'superadmin@ctrls.dev.br',
           '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqR2e52Jei/qN/hiOS7wpXA9rSxG',
           'Super Admin',
           'SUPER_ADMIN',
           TRUE
       ) ON CONFLICT DO NOTHING;

-- === CLÍNICA DE TESTE ===
INSERT INTO clinics (id, name, slug, is_active)
VALUES (
           'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a02',
           'Clínica Modelo XYZ',
           'clinica-modelo-xyz',
           TRUE
       ) ON CONFLICT DO NOTHING;

-- === ADMIN DA CLÍNICA ===
INSERT INTO users (id, email, password, full_name, role, is_active)
VALUES (
           'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a03',
           'admin@clinicaxyz.com',
           '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqR2e52Jei/qN/hiOS7wpXA9rSxG',
           'Admin da Clínica XYZ',
           'CLINIC_ADMIN',
           TRUE
       ) ON CONFLICT DO NOTHING;

-- Associa o Clinic Admin à Clínica Modelo
INSERT INTO clinic_members (user_id, clinic_id, role_in_clinic)
VALUES (
           'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a03',
           'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a02',
           'CLINIC_ADMIN'
       ) ON CONFLICT DO NOTHING;

-- === MÉDICO DE TESTE ===
INSERT INTO users (id, email, password, full_name, role, is_active)
VALUES (
           'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a04',
           'house@clinicaxyz.com',
           '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqR2e52Jei/qN/hiOS7wpXA9rSxG',
           'Dr. Gregory House',
           'DOCTOR',
           TRUE
       ) ON CONFLICT DO NOTHING;

-- Cria o registo profissional do Doctor
INSERT INTO doctors (id, user_id, professional_council_type, professional_council_number, professional_council_state, specialty)
VALUES (
           'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a05',
           'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a04',
           'CRM',
           '12345',
           'NJ', -- ;)
           'Nefrologista e Infectologista'
       ) ON CONFLICT DO NOTHING;

-- Associa o Médico à Clínica Modelo (GERANDO O LINK TOKEN)
INSERT INTO doctor_clinics (doctor_id, clinic_id, is_primary)
VALUES (
           'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a05',
           'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a02',
           TRUE
       ) ON CONFLICT DO NOTHING;

-- Adiciona o Médico também à tabela de membros da clínica para consistência
INSERT INTO clinic_members (user_id, clinic_id, role_in_clinic)
VALUES (
           'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a04',
           'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a02',
           'DOCTOR'
       ) ON CONFLICT DO NOTHING;

-- === SECRETÁRIA DE TESTE ===
INSERT INTO users (id, email, password, full_name, role, is_active)
VALUES (
    'f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a06',
    'secretary@clinicaxyz.com',
    '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqR2e52Jei/qN/hiOS7wpXA9rSxG',
    'Secretária Eficaz',
    'SECRETARY',
    TRUE
) ON CONFLICT DO NOTHING;

-- Associa a Secretária à Clínica Modelo
INSERT INTO clinic_members (user_id, clinic_id, role_in_clinic)
VALUES (
    'f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a06',
    'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a02',
    'SECRETARY'
) ON CONFLICT DO NOTHING;

-- Associa a Secretária ao Dr. House na Clínica Modelo
INSERT INTO doctor_secretary_assignments (doctor_user_id, secretary_user_id, clinic_id)
VALUES (
    'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a04',
    'f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a06',
    'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a02'
) ON CONFLICT DO NOTHING;