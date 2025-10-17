import {BrowserRouter as Router, Routes, Route, Navigate} from 'react-router-dom';
import LoginPage from './pages/Login';
import DashboardPage from './pages/Dashboard';
import PublicFormPage from './pages/PublicForm';
import ProtectedRoute from './router/ProtectedRoute';

function App() {
    return (
        <Router>
            <Routes>
                {/* Rotas Públicas */}
                <Route path="/login" element={<LoginPage/>}/>
                <Route path="/form/:linkToken" element={<PublicFormPage/>}/>

                {/* Rotas Protegidas */}
                <Route element={<ProtectedRoute/>}>
                    <Route path="/dashboard" element={<DashboardPage/>}/>
                </Route>

                {/* Redirecionamento padrão para a página de login */}
                <Route path="*" element={<Navigate to="/login"/>}/>
            </Routes>
        </Router>
    );
}

export default App;