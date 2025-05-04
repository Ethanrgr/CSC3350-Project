import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import '../../pages/PageStyles.css';

function EmployeeEdit() {
    const { empId } = useParams();
    const navigate = useNavigate();
    const { apiClient } = useAuth();
    const [employeeData, setEmployeeData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [successMessage, setSuccessMessage] = useState('');

    useEffect(() => {
        const fetchEmployee = async () => {
            setLoading(true);
            setError('');
            try {
                console.log(`Fetching employee data for ID: ${empId}`);
                const response = await apiClient.get(`/admin/employees/${empId}`);
                setEmployeeData(response.data);
                console.log("Employee data fetched:", response.data);
            } catch (err) {
                console.error(`Failed to fetch employee ${empId}:`, err);
                 setError(err.response?.data?.message || 'Failed to load employee data.');
                setEmployeeData(null);
            } finally {
                setLoading(false);
            }
        };

        if (empId) {
            fetchEmployee();
        }
    }, [empId, apiClient]);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setEmployeeData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!employeeData || isSubmitting) return;

        if (!employeeData.fName || !employeeData.lName || !employeeData.email) {
            setError('First Name, Last Name, and Email are required.');
            return;
        }

        setIsSubmitting(true);
        setError('');
        setSuccessMessage('');
        console.log(`Updating employee ID: ${empId} with data:`, employeeData);

        try {
            const payload = {
                fName: employeeData.fName,
                lName: employeeData.lName,
                email: employeeData.email,
                hireDate: employeeData.hireDate,
                salary: parseFloat(employeeData.salary) || 0,
                ssn: employeeData.ssn,
                street: employeeData.street,
                zip: employeeData.zip,
                cityId: employeeData.city?.cityId || employeeData.city?.id,
                stateId: employeeData.state?.stateId || employeeData.state?.id,
                gender: employeeData.gender,
                identifiedRace: employeeData.identifiedRace,
                dob: employeeData.dob,
                mobilePhone: employeeData.mobilePhone
            };

            console.log("Sending update with payload:", payload);
            const response = await apiClient.put(`/admin/employees/${empId}`, payload);
            console.log("Update response:", response.data);
            setSuccessMessage('Employee updated successfully!');
             setTimeout(() => navigate('/admin/employees'), 500);
        } catch (err) {
            console.error(`Failed to update employee ${empId}:`, err);
             setError(err.response?.data?.message || 'Failed to update employee.');
        } finally {
            setIsSubmitting(false);
        }
    };

    if (loading) {
        return <div>Loading employee data...</div>;
    }

    if (error && !employeeData) {
        return <div className="error-message">{error}</div>;
    }

    if (!employeeData) {
        return <div>Employee not found.</div>;
    }

    return (
        <div>
            <h3>Edit Employee (ID: {empId})</h3>
            {successMessage && <div className="success-message">{successMessage}</div>}
             {/* Display submission error messages here */}
            {error && <div className="error-message" style={{marginBottom: '1rem'}}>{error}</div>}

            <form onSubmit={handleSubmit} className="edit-form">
                <h4>Basic Information</h4>
                <div className="form-group">
                    <label htmlFor="fName">First Name:</label>
                    <input type="text" id="fName" name="fName" value={employeeData.fName || ''} onChange={handleInputChange} required disabled={isSubmitting} />
                </div>
                <div className="form-group">
                    <label htmlFor="lName">Last Name:</label>
                    <input type="text" id="lName" name="lName" value={employeeData.lName || ''} onChange={handleInputChange} required disabled={isSubmitting} />
                </div>
                 <div className="form-group">
                    <label htmlFor="email">Email:</label>
                    <input type="email" id="email" name="email" value={employeeData.email || ''} onChange={handleInputChange} required disabled={isSubmitting} />
                </div>
                 <div className="form-group">
                    <label htmlFor="hireDate">Hire Date:</label>
                    {/* Assuming hireDate is string YYYY-MM-DD */}
                    <input type="date" id="hireDate" name="hireDate" value={employeeData.hireDate || ''} onChange={handleInputChange} disabled={isSubmitting} /> 
                </div>
                 <div className="form-group">
                    <label htmlFor="salary">Salary:</label>
                    <input type="number" step="0.01" id="salary" name="salary" value={employeeData.salary || ''} onChange={handleInputChange} disabled={isSubmitting} />
                </div>
                 <div className="form-group">
                    <label htmlFor="ssn">SSN:</label> 
                    <input type="text" id="ssn" name="ssn" value={employeeData.ssn || ''} onChange={handleInputChange} disabled={isSubmitting} />
                </div>

                <h4>Address Information</h4>
                <div className="form-group">
                    <label htmlFor="street">Street Address:</label>
                    <input type="text" id="street" name="street" value={employeeData.street || ''} onChange={handleInputChange} disabled={isSubmitting} />
                </div>
                <div className="form-group">
                    <label htmlFor="zip">ZIP/Postal Code:</label>
                    <input type="text" id="zip" name="zip" value={employeeData.zip || ''} onChange={handleInputChange} disabled={isSubmitting} />
                </div>

                <div className="form-row">
                    <div className="form-group" style={{width: '48%', marginRight: '4%'}}>
                        <label htmlFor="cityId">City:</label>
                        <input type="text" id="cityId" name="cityId" 
                            value={employeeData.city?.cityId || employeeData.city?.id || ''} 
                            onChange={handleInputChange} 
                            disabled={isSubmitting} 
                            placeholder="City ID" />
                    </div>
                    <div className="form-group" style={{width: '48%'}}>
                        <label htmlFor="stateId">State:</label>
                        <input type="text" id="stateId" name="stateId" 
                            value={employeeData.state?.stateId || employeeData.state?.id || ''} 
                            onChange={handleInputChange} 
                            disabled={isSubmitting} 
                            placeholder="State ID" />
                    </div>
                </div>

                <h4>Demographics</h4>
                <div className="form-group">
                    <label htmlFor="gender">Gender:</label>
                    <select id="gender" name="gender" value={employeeData.gender || ''} onChange={handleInputChange} disabled={isSubmitting}>
                        <option value="">Select Gender</option>
                        <option value="Male">Male</option>
                        <option value="Female">Female</option>
                        <option value="Non-Binary">Non-Binary</option>
                        <option value="Prefer not to say">Prefer not to say</option>
                    </select>
                </div>
                
                <div className="form-group">
                    <label htmlFor="identifiedRace">Identified Race:</label>
                    <input type="text" id="identifiedRace" name="identifiedRace" value={employeeData.identifiedRace || ''} onChange={handleInputChange} disabled={isSubmitting} />
                </div>
                
                <div className="form-group">
                    <label htmlFor="dob">Date of Birth:</label>
                    <input type="date" id="dob" name="dob" value={employeeData.dob || ''} onChange={handleInputChange} disabled={isSubmitting} />
                </div>
                
                <div className="form-group">
                    <label htmlFor="mobilePhone">Mobile Phone:</label>
                    <input type="tel" id="mobilePhone" name="mobilePhone" value={employeeData.mobilePhone || ''} onChange={handleInputChange} disabled={isSubmitting} />
                </div>

                <div style={{marginTop: '1rem'}}> 
                    <button type="submit" disabled={isSubmitting}>
                        {isSubmitting ? 'Updating...' : 'Save Changes'}
                    </button>
                    <button type="button" onClick={() => navigate('/admin/employees')} disabled={isSubmitting} style={{marginLeft: '10px', backgroundColor: '#6c757d'}}> 
                        Cancel
                    </button>
                </div>
            </form>
        </div>
    );
}

export default EmployeeEdit; 