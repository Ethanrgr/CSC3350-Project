import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './PageStyles.css';

function RegisterPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [role, setRole] = useState('EMPLOYEE');
    const [error, setError] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const { apiClient } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (isSubmitting) return;
        setError('');
        setSuccessMessage('');

        if (password !== confirmPassword) {
            setError('Passwords do not match.');
            return;
        }

        setIsSubmitting(true);
        console.log("Attempting registration with:", { email, role });

        try {
            const payload = { 
                email, 
                password, 
                role: role
            };

            const response = await apiClient.post('/register', payload);
            console.log("Register response:", response.data);

            setSuccessMessage('Registration successful! Redirecting to login...');
            console.log("Registration successful");
            setTimeout(() => {
                navigate('/login');
            }, 2000);

        } catch (err) {
            console.error("Registration error:", err);
            let errorMessage = 'An unexpected error occurred during registration.';
             if (err.response) {
                 errorMessage = err.response.data?.message || err.response.data || `Registration failed (status: ${err.response.status}). Email might be taken.`;
             } else if (err.request) {
                 errorMessage = 'Could not connect to the server. Please try again later.';
             } else {
                 errorMessage = `Registration failed: ${err.message}`;
             }
             setError(errorMessage);
             setSuccessMessage('');
        } finally {
             setIsSubmitting(false);
        }
    };

    return (
        <div className="page-container">
            <h2>Register</h2>
            <form onSubmit={handleSubmit} className="auth-form">
                {error && <p className="error-message">{error}</p>}
                {successMessage && <p className="success-message">{successMessage}</p>}
                <div className="form-group">
                    <label htmlFor="email">Email:</label>
                    <input
                        type="email"
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        disabled={isSubmitting}
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
                <div className="form-group">
                    <label htmlFor="confirmPassword">Confirm Password:</label>
                    <input
                        type="password"
                        id="confirmPassword"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        required
                        disabled={isSubmitting}
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="role">Role:</label>
                    <select 
                        id="role"
                        value={role}
                        onChange={(e) => setRole(e.target.value)} 
                        required
                        disabled={isSubmitting}
                        className="role-select"
                    >
                        <option value="EMPLOYEE">EMPLOYEE</option>
                        <option value="ADMIN">ADMIN</option>
                    </select>
                </div>
                <button type="submit" disabled={isSubmitting}>
                     {isSubmitting ? 'Registering...' : 'Register'}
                </button>
            </form>
            <p>
                Already have an account? <Link to="/login">Login here</Link>
            </p>
        </div>
    );
}

export default RegisterPage; 