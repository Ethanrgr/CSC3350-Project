import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom'
import './App.css'

// Import actual page components
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import AdminDashboard from './pages/AdminDashboard'
import EmployeeDashboard from './pages/EmployeeDashboard'

// Import ProtectedRoute component
import ProtectedRoute from './components/ProtectedRoute'

// Removed placeholder routes

function App() {
    // Auth state will be managed in Context

    return (
        <Router>
            {/* Removed basic nav, we'll handle nav inside dashboards or a main layout component */}
            {/* <nav>
                <ul>
                    <li><Link to="/login">Login</Link></li>
                </ul>
            </nav> */}
            <Routes>
                {/* Public Routes */}
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />

                {/* Protected Admin Routes */}
                <Route 
                    path="/admin/*" 
                    element={
                        <ProtectedRoute allowedRoles={['ADMIN']}>
                            <AdminDashboard />
                        </ProtectedRoute>
                    }
                />

                {/* Protected Employee Routes */}
                <Route 
                    path="/employee/*" 
                    element={
                        <ProtectedRoute allowedRoles={['EMPLOYEE']}>
                            <EmployeeDashboard />
                        </ProtectedRoute>
                    }
                />

                
                <Route path="*" element={<Navigate to="/login" replace />} />
            </Routes>
        </Router>
    )
}

export default App
