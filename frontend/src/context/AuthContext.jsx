import React, { createContext, useState, useContext, useEffect } from 'react';
import axios from 'axios'; // Import axios to configure it

const AuthContext = createContext(null);

// Create a dedicated axios instance for API calls
// This allows us to configure base URLs and interceptors centrally
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
    const [loading, setLoading] = useState(true); // Indicate initial loading state

    // Effect to check for existing token in localStorage on initial load
    useEffect(() => {
        const storedToken = localStorage.getItem('authToken');
        const storedRole = localStorage.getItem('authRole');
        const storedUsername = localStorage.getItem('authUsername');
        
        // You might want to add an API call here to validate the token
        if (storedToken && storedRole) {
            console.log("Found token in storage, attempting to set user state.");
            setUser({ token: storedToken, role: storedRole, username: storedUsername });
            // Set the token for all subsequent requests using the dedicated instance
            apiClient.defaults.headers.common['Authorization'] = `Bearer ${storedToken}`;
        } else {
            console.log("No token found in storage.");
            // Make sure to clear auth header if no token
            delete apiClient.defaults.headers.common['Authorization'];
        }
        setLoading(false); // Finished initial check
    }, []);

    const login = (token, role, username = null) => {
        console.log(`Logging in user. Role: ${role}, Token: ${token}, Username: ${username}`);
        localStorage.setItem('authToken', token);
        localStorage.setItem('authRole', role);
        if (username) localStorage.setItem('authUsername', username);
        
        setUser({ token, role, username });
        // Set the token for all subsequent requests using the dedicated instance
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
        // Optionally navigate to login page - often handled by ProtectedRoute
    };

    // Provide the user state, loading state, login/logout functions, and the configured axios instance
    const value = {
        user,
        loading,
        login,
        logout,
        apiClient // Provide the configured axios instance
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

// Custom hook to use the auth context
export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
}; 