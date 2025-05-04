import React, { createContext, useState, useContext, useEffect } from 'react';
import axios from 'axios'; // Import axios to configure it

const AuthContext = createContext(null);

const apiClient = axios.create({
    baseURL: 'http://localhost:8010/api',
    withCredentials: true, // Important for CORS with credentials
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }
});

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null); // Stores { token, role, username (optional) }
    const [loading, setLoading] = useState(true);

    // Effect to check for existing token in localStorage on initial load
    useEffect(() => {
        const storedToken = localStorage.getItem('authToken');
        const storedRole = localStorage.getItem('authRole');
        const storedUsername = localStorage.getItem('authUsername');

        if (storedToken && storedRole) {
            console.log("Found token in storage, attempting to set user state.");
            setUser({ token: storedToken, role: storedRole, username: storedUsername });
            apiClient.defaults.headers.common['Authorization'] = `Bearer ${storedToken}`;
        } else {
            console.log("No token found in storage.");
            delete apiClient.defaults.headers.common['Authorization'];
        }
        setLoading(false);
    }, []);

    const login = (token, role, username = null) => {
        console.log(`Logging in user. Role: ${role}, Token: ${token}, Username: ${username}`);
        localStorage.setItem('authToken', token);
        localStorage.setItem('authRole', role);
        if (username) localStorage.setItem('authUsername', username);
        
        setUser({ token, role, username });
        apiClient.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    };

    const logout = () => {
        console.log("Logging out user.");
        localStorage.removeItem('authToken');
        localStorage.removeItem('authRole');
        localStorage.removeItem('authUsername');
        setUser(null);
        // Remove the authorization header
        delete apiClient.defaults.headers.common['Authorization'];
    };

    const value = {
        user,
        loading,
        login,
        logout,
        apiClient
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
}; 