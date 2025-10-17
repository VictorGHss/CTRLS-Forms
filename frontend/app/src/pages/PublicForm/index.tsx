import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import apiClient from '../../api/apiClient';
import type { PatientFormRequest } from '../../types/api';

const PublicFormPage = () => {
    // O hook `useParams` captura os parâmetros dinâmicos da URL (o :linkToken).
    const { linkToken } = useParams<{ linkToken: string }>();

    // Estado para gerir o estado da submissão (inicial, a enviar, sucesso, erro).
    const [submissionStatus, setSubmissionStatus] = useState<'idle' | 'submitting' | 'success' | 'error'>('idle');

    // Estado para cada campo do formulário.
    const [formData, setFormData] = useState<PatientFormRequest>({
        patientName: '',
        patientCpf: '',
        birthDate: '',
        patientEmail: '',
        patientPhone: '',
        reasonForConsultation: '',
        medicalHistory: '',
        currentMedications: '',
        allergies: '',
    });

    // Função para atualizar o estado do formulário de forma genérica.
    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setSubmissionStatus('submitting');

        try {
            await apiClient.post(`/public/forms/${linkToken}/submit`, formData);
            setSubmissionStatus('success');
        } catch (error) {
            console.error("Erro ao submeter o formulário:", error);
            setSubmissionStatus('error');
        }
    };

    // Se o formulário foi enviado com sucesso, mostra uma mensagem de confirmação.
    if (submissionStatus === 'success') {
        return (
            <div style={{ textAlign: 'center', margin: '50px' }}>
                <h2>Formulário Enviado com Sucesso!</h2>
                <p>Obrigado por preencher as suas informações.</p>
            </div>
        );
    }

    return (
        <div style={{ maxWidth: '600px', margin: '20px auto', padding: '20px', border: '1px solid #ddd' }}>
            <h2>Formulário de Pré-Consulta</h2>
            <p>Por favor, preencha os seus dados abaixo.</p>
            <hr />

            <form onSubmit={handleSubmit}>
                {/* Campos do formulário */}
                <input name="patientName" placeholder="Nome Completo" value={formData.patientName} onChange={handleChange} required />
                <input name="patientCpf" placeholder="CPF (ex: 123.456.789-00)" value={formData.patientCpf} onChange={handleChange} required />
                <input name="birthDate" type="date" placeholder="Data de Nascimento" value={formData.birthDate} onChange={handleChange} required />
                <input name="patientEmail" type="email" placeholder="E-mail" value={formData.patientEmail} onChange={handleChange} required />
                <input name="patientPhone" placeholder="Telefone (Opcional)" value={formData.patientPhone} onChange={handleChange} />
                <textarea name="reasonForConsultation" placeholder="Motivo da Consulta (Opcional)" value={formData.reasonForConsultation} onChange={handleChange} />
                <textarea name="medicalHistory" placeholder="Histórico Médico Relevante (Opcional)" value={formData.medicalHistory} onChange={handleChange} />
                <textarea name="currentMedications" placeholder="Medicamentos em Uso (Opcional)" value={formData.currentMedications} onChange={handleChange} />
                <textarea name="allergies" placeholder="Alergias (Opcional)" value={formData.allergies} onChange={handleChange} />

                {submissionStatus === 'error' && <p style={{ color: 'red' }}>Ocorreu um erro ao enviar. Por favor, tente novamente.</p>}

                <button type="submit" disabled={submissionStatus === 'submitting'}>
                    {submissionStatus === 'submitting' ? 'A enviar...' : 'Enviar Formulário'}
                </button>
            </form>

            {/* Estilos simples para o formulário */}
            <style>{`
        form { display: flex; flex-direction: column; gap: 15px; }
        input, textarea { padding: 10px; font-size: 16px; border: 1px solid #ccc; border-radius: 4px; }
        textarea { min-height: 80px; }
        button { padding: 12px; font-size: 16px; background-color: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer; }
        button:disabled { background-color: #ccc; }
      `}</style>
        </div>
    );
};

export default PublicFormPage;