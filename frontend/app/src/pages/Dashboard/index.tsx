import { useState, useEffect, useCallback } from 'react'; // Importe o useCallback
import apiClient from '../../api/apiClient';
import type { PatientFormSummaryResponse } from '../../types/api';

const DashboardPage = () => {
    const [forms, setForms] = useState<PatientFormSummaryResponse[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [error, setError] = useState<string>('');

    const handleLogout = () => {
        localStorage.removeItem('authToken');
        window.location.href = '/login';
    };

    // 1. A lógica de busca agora está numa função nomeada e envolvida em useCallback
    //    para garantir que ela não seja recriada em cada renderização.
    const fetchForms = useCallback(async () => {
        try {
            setError('');
            setIsLoading(true);

            const response = await apiClient.get<PatientFormSummaryResponse[]>('/api/doctor/forms');

            setForms(response.data);
        } catch (err) {
            console.error("Erro ao buscar formulários:", err);
            setError('Não foi possível carregar os formulários. Por favor, tente fazer o login novamente.');
        } finally {
            setIsLoading(false);
        }
    }, []); // useCallback com array vazio, a função nunca muda.

    // 2. O useEffect agora simplesmente chama a nossa função de busca na primeira vez.
    useEffect(() => {
        fetchForms();
    }, [fetchForms]); // Adicionamos fetchForms como dependência.

    return (
        <div style={{ padding: '20px', maxWidth: '800px', margin: 'auto' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <h2>Dashboard do Médico</h2>
                <button onClick={handleLogout}>Sair</button>
            </div>
            <hr />
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
                <h3>Formulários Recebidos</h3>
                {/* 3. Adicionamos o botão de atualizar, que chama a mesma função fetchForms */}
                <button onClick={fetchForms} disabled={isLoading}>
                    {isLoading ? 'A recarregar...' : 'Atualizar Lista'}
                </button>
            </div>

            {/* O estado de loading agora só se aplica à lista */}
            {isLoading ? (
                <p>A carregar formulários...</p>
            ) : error ? (
                <p style={{ color: 'red' }}>{error}</p>
            ) : (
                <ul>
                    {forms.length > 0 ? (
                        forms.map(form => (
                            <li key={form.id} style={{ border: '1px solid #eee', padding: '10px', marginBottom: '10px', borderRadius: '4px' }}>
                                <strong>Paciente:</strong> {form.patientName} -
                                <strong> Enviado em:</strong> {new Date(form.submissionDate).toLocaleString('pt-BR')}
                            </li>
                        ))
                    ) : (
                        <p>Nenhum formulário recebido ainda.</p>
                    )}
                </ul>
            )}
        </div>
    );
};

export default DashboardPage;