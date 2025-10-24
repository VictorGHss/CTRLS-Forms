CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       full_name VARCHAR(255) NOT NULL,
                       phone_number VARCHAR(20),
                       role VARCHAR(50) NOT NULL CHECK (role IN ('SUPER_ADMIN', 'CLINIC_ADMIN', 'DOCTOR', 'SECRETARY')),
                       is_active BOOLEAN NOT NULL DEFAULT TRUE,
                       created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);

CREATE TABLE clinics (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         name VARCHAR(255) NOT NULL,
                         slug VARCHAR(100) NOT NULL UNIQUE,
                         cnpj VARCHAR(18),
                         address TEXT,
                         phone VARCHAR(20),
                         consent_term_text TEXT,
                         consent_term_version VARCHAR(50),
                         is_active BOOLEAN NOT NULL DEFAULT TRUE,
                         created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_clinics_slug ON clinics(slug);

CREATE TABLE doctors (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         user_id UUID NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
                         birth_date DATE,
                         professional_council_type VARCHAR(10),
                         professional_council_number VARCHAR(50),
                         professional_council_state VARCHAR(2),
                         rqe VARCHAR(50),
                         specialty VARCHAR(100),
                         created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_doctors_user_id ON doctors(user_id);
CREATE UNIQUE INDEX idx_doctors_council ON doctors(professional_council_type, professional_council_number, professional_council_state);

CREATE TABLE doctor_clinics (
                                doctor_id UUID NOT NULL REFERENCES doctors(id) ON DELETE CASCADE,
                                clinic_id UUID NOT NULL REFERENCES clinics(id) ON DELETE CASCADE,
                                role_in_clinic VARCHAR(100),
                                is_primary BOOLEAN DEFAULT FALSE,
                                link_token UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                UNIQUE (doctor_id, clinic_id)
);
CREATE INDEX idx_doctor_clinics_doctor_id ON doctor_clinics(doctor_id);
CREATE INDEX idx_doctor_clinics_clinic_id ON doctor_clinics(clinic_id);

CREATE TABLE clinic_members (
                                user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                clinic_id UUID NOT NULL REFERENCES clinics(id) ON DELETE CASCADE,
                                role_in_clinic VARCHAR(50) NOT NULL CHECK (role_in_clinic IN ('CLINIC_ADMIN', 'SECRETARY', 'DOCTOR')),
                                added_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                PRIMARY KEY (user_id, clinic_id)
);
CREATE INDEX idx_clinic_members_user_id ON clinic_members(user_id);
CREATE INDEX idx_clinic_members_clinic_id ON clinic_members(clinic_id);

CREATE TABLE doctor_secretary_assignments (
                                              doctor_user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                              secretary_user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                              clinic_id UUID NOT NULL REFERENCES clinics(id) ON DELETE CASCADE,
                                              assigned_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                              PRIMARY KEY (doctor_user_id, secretary_user_id, clinic_id)

);

CREATE INDEX idx_doctor_secretary_assignments_doctor ON doctor_secretary_assignments(doctor_user_id, clinic_id);
CREATE INDEX idx_doctor_secretary_assignments_secretary ON doctor_secretary_assignments(secretary_user_id, clinic_id);

CREATE TABLE patient_forms (
                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                               doctor_clinic_token UUID NOT NULL REFERENCES doctor_clinics(link_token),
                               doctor_id UUID NOT NULL REFERENCES doctors(id),
                               clinic_id UUID NOT NULL REFERENCES clinics(id),

    -- Dados do Paciente
                               patient_name VARCHAR(255) NOT NULL,
                               patient_cpf VARCHAR(14) NOT NULL,
                               birth_date DATE NOT NULL,
                               patient_email VARCHAR(255) NOT NULL,
                               patient_phone VARCHAR(20),
                               reason_for_consultation TEXT,
                               medical_history TEXT,
                               current_medications TEXT,
                               allergies TEXT,

    -- Consentimento
                               consent_given_at TIMESTAMP WITH TIME ZONE,
                               consent_version VARCHAR(50),
                               status VARCHAR(20) NOT NULL DEFAULT 'SUBMITTED' CHECK (status IN ('DRAFT', 'SUBMITTED')),
    -- Datas
                               submission_date TIMESTAMP WITH TIME ZONE NOT NULL,
                               created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_patient_forms_doctor_clinic_token ON patient_forms(doctor_clinic_token);
CREATE INDEX idx_patient_forms_doctor_id ON patient_forms(doctor_id);
CREATE INDEX idx_patient_forms_clinic_id ON patient_forms(clinic_id);
CREATE INDEX idx_patient_forms_submission_date ON patient_forms(submission_date);
CREATE INDEX idx_patient_forms_patient_cpf ON patient_forms(patient_cpf);