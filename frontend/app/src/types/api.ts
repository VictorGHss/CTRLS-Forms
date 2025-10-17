export interface AuthenticationResponse {
    token: string;
}

export interface PatientFormSummaryResponse {
    id: string;
    patientName: string;
    submissionDate: string;
}

export interface PatientFormRequest {
    patientName: string;
    patientCpf: string;
    birthDate: string; // Formato 'YYYY-MM-DD'
    patientEmail: string;
    patientPhone?: string; // O '?' torna o campo opcional
    reasonForConsultation?: string;
    medicalHistory?: string;
    currentMedications?: string;
    allergies?: string;
}