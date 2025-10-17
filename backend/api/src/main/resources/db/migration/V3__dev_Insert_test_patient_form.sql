-- Insere um formulário de teste para o médico Dr. House (usado em V2)
-- Utilize este arquivo para popular o banco de desenvolvimento e verificar o endpoint /api/doctor/forms

INSERT INTO patient_forms (
    id,
    doctor_id,
    patient_name,
    patient_cpf,
    birth_date,
    patient_email,
    patient_phone,
    reason_for_consultation,
    medical_history,
    current_medications,
    allergies,
    submission_date
)
VALUES (
    'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', -- ID fixo para teste
    'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', -- Referência ao médico de teste inserido em V2
    'John Doe',
    '000.000.000-00',
    '1980-01-01',
    'johndoe@example.com',
    '+5511999999999',
    'Consulta de rotina - queixa principal',
    'Sem histórico relevante',
    'Nenhuma medicação atual',
    'Nenhuma alergia conhecida',
    CURRENT_TIMESTAMP
);

