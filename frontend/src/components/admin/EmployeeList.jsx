import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { Link } from 'react-router-dom';
import '../../pages/PageStyles.css';

function EmployeeList() {
    const [employees, setEmployees] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [deleteError, setDeleteError] = useState('');
    const [deleteSuccess, setDeleteSuccess] = useState('');
    const { apiClient } = useAuth();

    const fetchEmployees = async () => {
        setLoading(true);
        setError('');
        setDeleteError('');
        setDeleteSuccess('');
        try {
            console.log("Fetching all employees...");
            const response = await apiClient.get('/admin/employees');
            console.log("Employees fetched:", response.data);
            setEmployees(response.data || []);
        } catch (err) {
            console.error("Failed to fetch employees:", err);
            let errorMessage = 'Failed to load employee data.';
            if (err.response) {
                errorMessage = err.response.data?.message || `Error: ${err.response.status}`;
            } else if (err.request) {
                errorMessage = 'No response from server.';
            }
            setError(errorMessage);
            setEmployees([]);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchEmployees();
    }, [apiClient]); // Dependency on apiClient ensures refetch if context changes

    const handleDelete = async (empIdToDelete) => {
        if (!window.confirm(`Are you sure you want to delete employee ID: ${empIdToDelete}? This action cannot be undone.`)) {
            return;
        }

        setDeleteError('');
        setDeleteSuccess('');
        
        try {
            console.log(`Attempting to delete employee ID: ${empIdToDelete}`);
            const response = await apiClient.delete(`/admin/employees/${empIdToDelete}`);
            console.log("Delete response:", response);
            
            setDeleteSuccess(`Employee ID: ${empIdToDelete} deleted successfully!`);

            setEmployees(prevEmployees => prevEmployees.filter(emp => emp.empId !== empIdToDelete));

        } catch (err) {
            console.error(`Failed to delete employee ${empIdToDelete}:`, err);
            let errorMessage = `Failed to delete employee ID: ${empIdToDelete}.`;
            if (err.response) {
                errorMessage += ` Error: ${err.response.data?.message || err.response.status}`; 
            } else if (err.request) {
                errorMessage += ' No response from server.';
            }
            setDeleteError(errorMessage);
        }
        setTimeout(() => {
            setDeleteError('');
            setDeleteSuccess('');
        }, 5000);
    };

    if (loading) {
        return <div>Loading employees...</div>;
    }

    if (error) {
        return <div className="error-message">Error loading employees: {error}</div>;
    }

    return (
        <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
                <h3>All Employees</h3>
                 {/* Add Create Employee Button/Link */}
                 <Link to="/admin/employees/new" className="create-button"> 
                    + Create New Employee
                 </Link>
             </div>

            {/* Display Delete Success/Error Messages */} 
            {deleteSuccess && <div className="success-message">{deleteSuccess}</div>}
            {deleteError && <div className="error-message">{deleteError}</div>}
            {/* Display general fetch error if it occurred but we still have some data */} 
            {error && employees.length > 0 && <div className="error-message">Error refreshing employee list: {error}</div>}

            {employees.length === 0 ? (
                <p>No employees found.</p>
            ) : (
                <table className="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>First Name</th>
                            <th>Last Name</th>
                            <th>Email</th>
                            <th>Gender</th>
                            <th>Race</th>
                            <th>Address</th>
                            <th>DOB</th>
                            <th>Mobile</th>
                            <th>Hire Date</th>
                            <th>Salary</th>
                            <th>SSN</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {employees.map((emp) => (
                            <tr key={emp.empId}>
                                <td>{emp.empId}</td>
                                <td>{emp.fName}</td>
                                <td>{emp.lName}</td>
                                <td>{emp.email}</td>
                                <td>{emp.gender || 'N/A'}</td>
                                <td>{emp.identifiedRace || 'N/A'}</td>
                                <td>
                                    {emp.street ? (
                                        <>
                                            {emp.street}, {emp.zip}<br/>
                                            {emp.city ? emp.city.cityName || emp.city.name : ''}
                                            {emp.city && emp.state ? ', ' : ''}
                                            {emp.state ? emp.state.stateName || emp.state.name : ''}
                                        </>
                                    ) : 'N/A'}
                                </td>
                                <td>{emp.dob || 'N/A'}</td>
                                <td>{emp.mobilePhone || 'N/A'}</td>
                                <td>{emp.hireDate}</td> 
                                <td>{emp.salary ? emp.salary.toFixed(2) : 'N/A'}</td>
                                <td>{emp.ssn}</td>
                                <td>
                                    <Link to={`/admin/employees/edit/${emp.empId}`} className="data-table-button edit-btn">Edit</Link> 
                                    <button 
                                        onClick={() => handleDelete(emp.empId)} 
                                        className="data-table-button delete-btn" 
                                        style={{marginLeft: '5px'}}
                                    >
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}

export default EmployeeList;