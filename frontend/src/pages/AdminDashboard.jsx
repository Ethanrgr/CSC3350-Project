import React from 'react';
import { useAuth } from '../context/AuthContext';
import { Routes, Route, Link, Navigate } from 'react-router-dom';
import EmployeeList from '../components/admin/EmployeeList';
import EmployeeSearch from '../components/admin/EmployeeSearch';
import EmployeeEdit from '../components/admin/EmployeeEdit';
import EmployeeCreate from '../components/admin/EmployeeCreate';
import SalaryUpdate from '../components/admin/SalaryUpdate';
import ReportsView from '../components/admin/ReportsView';
import './PageStyles.css';

function AdminDashboard() {
    const { user, logout } = useAuth();

    const handleLogout = () => {
        logout();
        console.log("Admin logout triggered");
    };

    return (
        <div className="dashboard-container">
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                 <h2>Admin Dashboard - Welcome, {user?.email || 'Admin'}!</h2>
                 <button onClick={handleLogout} className="logout-button">Logout</button>
             </div>

            <nav className="dashboard-nav">
                <ul>
                    <li><Link to="/admin/employees">View/Manage Employees</Link></li>
                    <li><Link to="/admin/search">Search Employees</Link></li>
                    <li><Link to="/admin/update-salary">Update Salary Range</Link></li>
                    <li><Link to="/admin/reports">View Reports</Link></li>
                 </ul>
            </nav>

            <div className="dashboard-content">
                <Routes>
                    <Route path="employees" element={<EmployeeList />} />
                    <Route path="employees/new" element={<EmployeeCreate />} />
                    <Route path="employees/edit/:empId" element={<EmployeeEdit />} /> 
                    <Route path="search" element={<EmployeeSearch />} />
                    <Route path="update-salary" element={<SalaryUpdate />} />
                    <Route path="reports" element={<ReportsView />} />
                    <Route index element={<Navigate to="employees" replace />} />
                </Routes>
            </div>
        </div>
    );
}

export default AdminDashboard;