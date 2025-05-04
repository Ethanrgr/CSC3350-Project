import React from 'react';
import { useAuth } from '../context/AuthContext';
import { Routes, Route, Link, Navigate } from 'react-router-dom';
import ProfileView from '../components/employee/ProfileView';
import PayStatementsView from '../components/employee/PayStatementsView';
import './PageStyles.css';

function EmployeeDashboard() {
    const { user, logout } = useAuth(); // Get user and logout function

     const handleLogout = () => {
        logout();
         console.log("Employee logout triggered");
    };

    return (
        <div className="dashboard-container">
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <h2>Employee Dashboard - Welcome, {user?.email || 'Employee'}!</h2> {/* Changed to email */}
                <button onClick={handleLogout} className="logout-button">Logout</button>
            </div>

            {/* Navigation for Nested Routes */}
             <nav className="dashboard-nav">
                 <ul>
                    <li><Link to="/employee/profile">View Profile</Link></li>
                    <li><Link to="/employee/paystatements">View Pay Statements</Link></li>
                </ul>
            </nav>

            <div className="dashboard-content">
                {/* Define Nested Routes */}
                 <Routes>
                    <Route path="profile" element={<ProfileView />} />
                    <Route path="paystatements" element={<PayStatementsView />} />

                    {/* Default route within /employee */} 
                    <Route index element={<Navigate to="profile" replace />} />
                </Routes>
            </div>
        </div>
    );
}

export default EmployeeDashboard; 