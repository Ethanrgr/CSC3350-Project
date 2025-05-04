import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaLock } from 'react-icons/fa';
import { MdEmail } from 'react-icons/md';
import axios from 'axios';
import logo from '../assets/WBDS_Logo.svg';
import './Login.css'; // Reuse same styles

const SignUpPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [role, setRole] = useState('EMPLOYEE');
    const [error, setError] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        if (isSubmitting) return;

        setError('');
        setSuccessMessage('');

        if (password !== confirmPassword) {
            setError('Passwords do not match.');
            return;
        }

        setIsSubmitting(true);

        try {
            const response = await axios.post('http://localhost:5000/api/register', {
                email,
                password,
                role
            });
            console.log("Registration success:", response.data);
            setSuccessMessage('Registration successful! Redirecting...');
            setTimeout(() => {
                navigate('/login');
            }, 2000);
        } catch (err) {
            console.error("Registration failed:", err.response?.data || err.message);
            setError(err.response?.data?.message || 'Registration failed. Email may already be taken.');
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="app-container">
            <img src={logo} alt="Logo" className="side-logo" />
            <div className="login_box">
                <form onSubmit={handleRegister}>
                    <h1>Sign Up</h1>
                    <div className="login_info">
                        <input type="email" placeholder="Email" required value={email} onChange={(e) => setEmail(e.target.value)} disabled={isSubmitting}/>
                        <MdEmail className="icon" />
                    </div>
                    <div className="login_info">
                        <input type="password" placeholder="Password" required value={password} onChange={(e) => setPassword(e.target.value)} disabled={isSubmitting}/>
                        <FaLock className="icon" />
                    </div>
                    <div className="login_info">
                        <input type="password" placeholder="Confirm Password" required value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} disabled={isSubmitting}/>
                        <FaLock className="icon" />
                    </div>
                    <div className="login_info">
                        <select 
                            value={role} 
                            onChange={(e) => setRole(e.target.value)} 
                            disabled={isSubmitting}
                            style={{
                                width: '100%',
                                height: '100%',
                                borderRadius: '40px',
                                backgroundColor: 'pink',
                                border: '2px solid rgba(255, 255, 255, .2)',
                                fontSize: '16px',
                                color: 'black',
                                padding: '0 15px'
                            }}
                        >
                            <option value="EMPLOYEE">EMPLOYEE</option>
                            <option value="ADMIN">ADMIN</option>
                        </select>
                    </div>

                    <button type="submit" disabled={isSubmitting}>
                        {isSubmitting ? 'Registering...' : 'Register'}
                    </button>
                    {error && <p className="error">{error}</p>}
                    {successMessage && <p className="success-message">{successMessage}</p>}
                    <div className="sign_up">
                        <p>Already have an account? <a href="#" onClick={(e) => { e.preventDefault(); navigate('/login'); }}>Login here!</a></p>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default SignUpPage;
