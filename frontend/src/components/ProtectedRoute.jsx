import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';

// This component checks authentication and authorization (role)
// allowedRoles is an array of roles that are permitted to access the route
const ProtectedRoute = ({ children, allowedRoles }) => {
    const { user, loading } = useAuth();
    const location = useLocation();

    // Show loading state while auth status is being determined (e.g., checking localStorage)
    if (loading) {
        // You might want to return a proper loading spinner component here
        return <div>Loading...</div>;
    }

    // If user is not logged in, redirect to login page
    // Store the intended destination in location state to redirect back after login
    if (!user) {
        console.log("ProtectedRoute: User not logged in, redirecting to login.");
        return <Navigate to="/login" state={{ from: location }} replace />;
    }

    // If user is logged in but their role is not in the allowedRoles array, redirect
    // You might want to redirect to an unauthorized page or back to a default dashboard
    if (!allowedRoles.includes(user.role)) {
        console.log(`ProtectedRoute: User role '${user.role}' not authorized. Allowed: ${allowedRoles}. Redirecting.`);
        // Redirecting to login might be confusing, perhaps redirect to a default route or an unauthorized page
        // For now, redirecting based on role seems reasonable
         return user.role === 'ADMIN' ? <Navigate to="/admin" replace /> : <Navigate to="/employee" replace />;
         // Or just redirect to login: return <Navigate to="/login" replace />;
    }

    // If user is logged in and has the correct role, render the child components
    console.log(`ProtectedRoute: User role '${user.role}' authorized. Access granted.`);
    return children;
};

export default ProtectedRoute; 