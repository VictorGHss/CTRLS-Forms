import { Navigate, Outlet } from 'react-router-dom';

const ProtectedRoute = () => {
    // 1. O guarda verifica se existe um 'authToken' no armazenamento local.
    const token = localStorage.getItem('authToken');

    // 2. Se o token existir, ele renderiza o componente filho (a página protegida).
    //    O <Outlet /> é um placeholder do react-router para a rota filha.
    if (token) {
        return <Outlet />;
    }

    // 3. Se o token NÃO existir, ele redireciona o usuário para a página de login.
    return <Navigate to="/login" />;
};

export default ProtectedRoute;