import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaLock } from "react-icons/fa";
import { MdEmail } from 'react-icons/md';
import axios from 'axios';
import logo from '../assets/WBDS_Logo.svg';
import './Login.css';

const LoginPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        if (isSubmitting) return; // Prevent multiple submissions
        setError('');
        setIsSubmitting(true);

        try {
            const res = await axios.post('http://localhost:5000/api/login', {
                empId,
                password
            });
            console.log('Login success:', res.data);
            setError('');
            navigate('/dashboard'); // Redirect to dashboard
        } catch (err) {
            console.error('Login failed:', err.response?.data || err.message);
            setError('Invalid Employee ID or Password');
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="app-container">
            <img src={logo} alt="Logo" className="side-logo" />   
            <div className='login_box'>
                <form onSubmit={handleLogin}>
                    <h1>Login</h1>
                    <div className='login_info'>
                        <input type="email" placeholder="Email" required value={email} onChange={(e) => setEmail(e.target.value)} disabled={isSubmitting}/>
                        <MdEmail className="icon" />
                    </div>
                    <div className='login_info'>
                        <input 
                            type="password" 
                            placeholder='Password' 
                            required 
                            value={password} 
                            onChange={(e) => setPassword(e.target.value)} 
                            disabled={isSubmitting}
                        />
                        <FaLock className='icon' />
                    </div>
                    <div className='forgot_password'>
                        <label><input type="checkbox" />Remember me</label>
                        <a href="#">Forgot Password?</a>
                    </div>
                    <button type="submit" disabled={isSubmitting}>
                        {isSubmitting ? 'Logging in...' : 'Login'}
                    </button>
                    {error && <p className='error'>{error}</p>}
                    <div className='sign_up'>
                        <p>Don't have an account? <a href="#" onClick={(e) => {e.preventDefault(); navigate('/signup')}}>Sign Up!</a></p>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default LoginPage;
