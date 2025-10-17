-- Tabela para os administradores do sistema.
CREATE TABLE admins (
                        id UUID PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL
);

-- Tabela para os médicos, que são os donos dos formulários.
CREATE TABLE doctors (
                         id UUID PRIMARY KEY,
                         full_name VARCHAR(255) NOT NULL,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         password VARCHAR(255) NOT NULL,
                         specialty VARCHAR(100),
                         link_token VARCHAR(255) NOT NULL UNIQUE -- O token único para o link do formulário.
);

-- Índice para otimizar a busca de médicos pelo token do link.
CREATE INDEX idx_doctors_link_token ON doctors(link_token);

-- Tabela principal que armazena as respostas dos formulários dos pacientes.
CREATE TABLE patient_forms (
                               id UUID PRIMARY KEY,
                               doctor_id UUID NOT NULL REFERENCES doctors(id),
                               patient_name VARCHAR(255) NOT NULL,
                               patient_cpf VARCHAR(14) NOT NULL, -- Formato com máscara.
                               birth_date DATE NOT NULL,
                               patient_email VARCHAR(255) NOT NULL,
                               patient_phone VARCHAR(20),
                               reason_for_consultation TEXT,
                               medical_history TEXT,
                               current_medications TEXT,
                               allergies TEXT,
                               submission_date TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Tabela para a personalização visual dos formulários por médico.
CREATE TABLE form_customizations (
                                     id UUID PRIMARY KEY,
                                     doctor_id UUID NOT NULL UNIQUE REFERENCES doctors(id),
                                     logo_url VARCHAR(255),
                                     banner_url VARCHAR(255),
                                     primary_color VARCHAR(7), -- Formato #RRGGBB.
                                     additional_instructions TEXT,
                                     clinic_address VARCHAR(255),
                                     clinic_phone VARCHAR(20)
);