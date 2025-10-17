import axios from 'axios';

// Cria uma instância do Axios com configurações padrão.
const apiClient = axios.create({
    baseURL: 'http://localhost:8080', // A URL base da API
    headers: {
        'Content-Type': 'application/json',
    },
});

// Este é o "interceptor". É uma função que é executada ANTES de cada requisição ser enviada.
apiClient.interceptors.request.use(
    (config) => {
        // Pega o token do armazenamento local.
        const token = localStorage.getItem('authToken');

        // Se o token existir, anexa-o ao cabeçalho de autorização.
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }

        return config; // Retorna a configuração modificada para a requisição prosseguir.
    },
    (error) => {
        // Em caso de erro na configuração da requisição, rejeita a promessa.
        return Promise.reject(error);
    }
);

export default apiClient;