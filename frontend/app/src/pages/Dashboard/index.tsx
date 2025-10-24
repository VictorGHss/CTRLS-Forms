import { useState, useEffect, useCallback } from 'react';
import apiClient from '../../api/apiClient';
import type { PatientFormSummaryResponse } from '../../types/api';

const DashboardPage = () => {
    const [forms, setForms] = useState<PatientFormSummaryResponse[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [error, setError] = useState<string>('');
    const [searchTerm, setSearchTerm] = useState<string>('');

    // 1. NOVOS ESTADOS: Para as datas de início e fim. '' significa sem filtro.
    const [startDate, setStartDate] = useState<string>(''); // Guardamos como string 'AAAA-MM-DD'
    const [endDate, setEndDate] = useState<string>('');

    const handleLogout = () => {
        localStorage.removeItem('authToken');
        window.location.href = '/login';
    };

    // 2. ATUALIZADO: A função agora aceita as datas como parâmetros.
    const fetchForms = useCallback(async (
        currentSearchTerm: string = '',
        currentStartDate: string = '',
        currentEndDate: string = ''
    ) => {
        try {
            setError('');
            setIsLoading(true);

            // 3. Monta o objeto de parâmetros dinamicamente, incluindo apenas os que têm valor.
            const params: { search?: string; startDate?: string; endDate?: string } = {};
            if (currentSearchTerm) params.search = currentSearchTerm;
            if (currentStartDate) params.startDate = currentStartDate;
            if (currentEndDate) params.endDate = currentEndDate;

            const response = await apiClient.get<PatientFormSummaryResponse[]>('/api/doctor/forms', {
                params: params // Passa os parâmetros (se existirem) para o backend
            });

            setForms(response.data);
        } catch (err) {
            console.error("Erro ao buscar formulários:", err);
            setError('Não foi possível carregar os formulários. Por favor, tente fazer o login novamente.');
        } finally {
            setIsLoading(false);
        }
    }, []);

    // 4. ATUALIZADO: O useEffect inicial agora busca sem filtros.
    useEffect(() => {
        fetchForms(); // Busca inicial sem filtros
    }, [fetchForms]);

    // 5. ATUALIZADO: Chamada quando o utilizador clica no botão de buscar/filtrar.
    const handleFilter = () => {
        fetchForms(searchTerm, startDate, endDate); // Busca com todos os filtros atuais
    };

    // 6. ATUALIZADO: Chamada quando o utilizador pressiona Enter no input de texto.
    const handleKeyPress = (event: React.KeyboardEvent<HTMLInputElement>) => {
        if (event.key === 'Enter') {
            handleFilter(); // Reutiliza a função de filtro
        }
    };

    return (
        <div style={{ padding: '20px', maxWidth: '900px', margin: 'auto' }}>
            {/* Cabeçalho */}
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <h2>Dashboard do Médico</h2>
                <button onClick={handleLogout}>Sair</button>
            </div>
            <hr />

            {/* Barra de Filtros */}
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '10px', marginBottom: '20px', alignItems: 'center', border: '1px solid #eee', padding: '15px', borderRadius: '8px' }}>
                {/* Campo de Busca por Nome */}
                <div style={{ flexGrow: 1, minWidth: '200px' }}>
                    <label htmlFor="search" style={{ fontSize: '0.9em', display: 'block', marginBottom: '3px' }}>Buscar por Nome:</label>
                    <input
                        id="search"
                        type="text"
                        placeholder="Nome do paciente..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        onKeyPress={handleKeyPress}
                        style={{ width: '100%', padding: '8px' }}
                    />
                </div>

                {/* 7. NOVO INPUT: Campo de Data de Início */}
                <div style={{ minWidth: '150px' }}>
                    <label htmlFor="startDate" style={{ fontSize: '0.9em', display: 'block', marginBottom: '3px' }}>De:</label>
                    <input
                        id="startDate"
                        type="date"
                        value={startDate}
                        onChange={(e) => setStartDate(e.target.value)}
                        style={{ width: '100%', padding: '8px' }}
                    />
                </div>

                {/* 8. NOVO INPUT: Campo de Data de Fim */}
                <div style={{ minWidth: '150px' }}>
                    <label htmlFor="endDate" style={{ fontSize: '0.9em', display: 'block', marginBottom: '3px' }}>Até:</label>
                    <input
                        id="endDate"
                        type="date"
                        value={endDate}
                        onChange={(e) => setEndDate(e.target.value)}
                        style={{ width: '100%', padding: '8px' }}
                    />
                </div>

                {/* Botão Aplicar Filtros */}
                <button onClick={handleFilter} disabled={isLoading} style={{ padding: '8px 15px', alignSelf: 'flex-end' }}>
                    {isLoading ? 'Filtrando...' : 'Aplicar Filtros'}
                </button>
                {/* Botão Limpar Filtros (opcional, mas útil) */}
                <button onClick={() => { setSearchTerm(''); setStartDate(''); setEndDate(''); fetchForms(); }} disabled={isLoading} style={{ padding: '8px 15px', alignSelf: 'flex-end', backgroundColor: '#6c757d' }}>
                    Limpar
                </button>
            </div>

            <h3>Formulários Recebidos</h3>

            {/* Exibição da Lista */}
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
                        <p>{searchTerm || startDate || endDate ? 'Nenhum formulário encontrado para os filtros aplicados.' : 'Nenhum formulário recebido ainda.'}</p>
                    )}
                </ul>
            )}
        </div>
    );
};

export default DashboardPage;