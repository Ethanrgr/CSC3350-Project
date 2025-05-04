import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';

// This component checks authentication and authorization (role)
// allowedRoles is an array of roles that are permitted to access the route
const ProtectedRoute = ({ children, allowedRoles }) => {
    const { user, loading } = useAuth();
    const location = useLocation();
    if (loading) {
        return <div>Loading...</div>;
    }

    // If user is not logged in, redirect to login page
    if (!user) {
        console.log("ProtectedRoute: User not logged in, redirecting to login.");
        return <Navigate to="/login" state={{ from: location }} replace />;
    }

    // If user is logged in but their role is not in the allowedRoles array, redirect
    if (!allowedRoles.includes(user.role)) {
        console.log(`ProtectedRoute: User role '${user.role}' not authorized. Allowed: ${allowedRoles}. Redirecting.`);
         return user.role === 'ADMIN' ? <Navigate to="/admin" replace /> : <Navigate to="/employee" replace />;
    }
    console.log(`ProtectedRoute: User role '${user.role}' authorized. Access granted.`);
    return children;
};

export default ProtectedRoute; 