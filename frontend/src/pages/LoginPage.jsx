import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './PageStyles.css';

function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const { login, apiClient } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (isSubmitting) return; // Prevent multiple submissions
        setError('');
        setIsSubmitting(true);
        console.log("Attempting login with:", email);

        try {
            const response = await apiClient.post('/login', { email, password });
            console.log("Login response:", response.data); // Debug log

            // Extract data from response
            const { token, role, email: userEmail } = response.data;

            if (token && role) {
                login(token, role, userEmail); // Update AuthContext
                console.log("Login successful, role:", role);
                // Redirect based on role
                if (role === 'ADMIN') {
                    navigate('/admin', { replace: true });
                } else if (role === 'EMPLOYEE') {
                    navigate('/employee', { replace: true });
                } else {
                     setError('Login successful, but received an unknown user role.');
                }
            } else {
                 setError('Login failed: Invalid response data received from server.');
                 console.error('Missing token or role in login response:', response.data);
            }
        } catch (err) {
            console.error("Login error:", err);
             let errorMessage = 'An unexpected error occurred during login.';
             if (err.response) {
                 errorMessage = err.response.data?.message || `Login failed (status: ${err.response.status}). Please check credentials.`;
             } else if (err.request) {
                 errorMessage = 'Could not connect to the server. Please try again later.';
             } else {
                 errorMessage = `Login failed: ${err.message}`;
             }
             setError(errorMessage);
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="page-container">
            <h2>Login</h2>
            <form onSubmit={handleSubmit} className="auth-form">
                {error && <p className="error-message">{error}</p>}
                <div className="form-group">
                    <label htmlFor="email">Email:</label>
                    <input
                        type="email"
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        disabled={isSubmitting} // Disable while submitting
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="password">Password:</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        disabled={isSubmitting}
                    />
                </div>
                <button type="submit" disabled={isSubmitting}>
                    {isSubmitting ? 'Logging in...' : 'Login'}
                </button>
            </form>
            <p>
                Don't have an account? <Link to="/register">Register here</Link>
            </p>
        </div>
    );
}

export default LoginPage; 