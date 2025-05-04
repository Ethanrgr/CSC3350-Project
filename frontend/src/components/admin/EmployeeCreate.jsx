import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import '../../pages/PageStyles.css';

function EmployeeCreate() {
    const navigate = useNavigate();
    const { apiClient } = useAuth();
    // Initialize state for all fields required by the POST endpoint
    const [newEmployee, setNewEmployee] = useState({
        empid: '', 
        fName: '',
        lName: '',
        email: '',
        hireDate: '', 
        salary: '',   
        ssn: '',
        street: '',
        zip: '',
        cityId: '',
        stateId: '',
        gender: '',
        identifiedRace: '',
        dob: '',
        mobilePhone: '',
    });
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setNewEmployee(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (isSubmitting) return;

        if (!newEmployee.empid || !newEmployee.fName || !newEmployee.lName || !newEmployee.email) {
            setError('Employee ID, First Name, Last Name, and Email are required.');
            return;
        }

        setIsSubmitting(true);
        setError('');
        setSuccessMessage('');
        console.log("Attempting to create employee with data:", newEmployee);

        try {
            const payload = {
                ...newEmployee,
                empid: parseInt(newEmployee.empid) || 0,
                salary: parseFloat(newEmployee.salary) || 0,
                cityId: newEmployee.cityId ? parseInt(newEmployee.cityId) : null,
                stateId: newEmployee.stateId ? parseInt(newEmployee.stateId) : null,
            };

            const response = await apiClient.post('/admin/employees', payload);
            console.log("Create response:", response.data);
            setSuccessMessage('Employee created successfully!');
            // Clear form
            setNewEmployee({
                empid: '', fName: '', lName: '', email: '', hireDate: '', salary: '', ssn: '',
                street: '', zip: '', cityId: '', stateId: '', gender: '', identifiedRace: '', dob: '', mobilePhone: ''
            });

             setTimeout(() => navigate('/admin/employees'), 500);

        } catch (err) {
            console.error("Failed to create employee:", err);
             setError(err.response?.data?.message || 'Failed to create employee. Check input data.');
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div>
            <h3>Create New Employee</h3>
            {successMessage && <div className="success-message">{successMessage}</div>}
            {error && <div className="error-message" style={{marginBottom: '1rem'}}>{error}</div>}

            <form onSubmit={handleSubmit} className="edit-form">
                <h4>Basic Information</h4>
                <div className="form-group">
                    <label htmlFor="empid">Employee ID:</label>
                    <input type="number" id="empid" name="empid" value={newEmployee.empid} onChange={handleInputChange} required disabled={isSubmitting} />
                </div>
                <div className="form-group">
                    <label htmlFor="fName">First Name:</label>
                    <input type="text" id="fName" name="fName" value={newEmployee.fName} onChange={handleInputChange} required disabled={isSubmitting} />
                </div>
                <div className="form-group">
                    <label htmlFor="lName">Last Name:</label>
                    <input type="text" id="lName" name="lName" value={newEmployee.lName} onChange={handleInputChange} required disabled={isSubmitting} />
                </div>
                <div className="form-group">
                    <label htmlFor="email">Email:</label>
                    <input type="email" id="email" name="email" value={newEmployee.email} onChange={handleInputChange} required disabled={isSubmitting} />
                </div>
                <div className="form-group">
                    <label htmlFor="hireDate">Hire Date:</label>
                    <input type="date" id="hireDate" name="hireDate" value={newEmployee.hireDate} onChange={handleInputChange} required disabled={isSubmitting} />
                </div>
                <div className="form-group">
                    <label htmlFor="salary">Salary:</label>
                    <input type="number" step="0.01" id="salary" name="salary" value={newEmployee.salary} onChange={handleInputChange} required disabled={isSubmitting} />
                </div>
                <div className="form-group">
                    <label htmlFor="ssn">SSN:</label>
                    <input type="text" id="ssn" name="ssn" value={newEmployee.ssn} onChange={handleInputChange} required disabled={isSubmitting} />
                </div>

                <h4>Address Information</h4>
                <div className="form-group">
                    <label htmlFor="street">Street Address:</label>
                    <input type="text" id="street" name="street" value={newEmployee.street} onChange={handleInputChange} disabled={isSubmitting} />
                </div>
                <div className="form-group">
                    <label htmlFor="zip">ZIP/Postal Code:</label>
                    <input type="text" id="zip" name="zip" value={newEmployee.zip} onChange={handleInputChange} disabled={isSubmitting} />
                </div>

                <div className="form-row">
                    <div className="form-group" style={{width: '48%', marginRight: '4%'}}>
                        <label htmlFor="cityId">City ID:</label>
                        <input type="number" id="cityId" name="cityId" value={newEmployee.cityId} onChange={handleInputChange} disabled={isSubmitting} placeholder="City ID" />
                    </div>
                    <div className="form-group" style={{width: '48%'}}>
                        <label htmlFor="stateId">State ID:</label>
                        <input type="number" id="stateId" name="stateId" value={newEmployee.stateId} onChange={handleInputChange} disabled={isSubmitting} placeholder="State ID" />
                    </div>
                </div>

                <h4>Demographics</h4>
                <div className="form-group">
                    <label htmlFor="gender">Gender:</label>
                    <select id="gender" name="gender" value={newEmployee.gender} onChange={handleInputChange} disabled={isSubmitting}>
                        <option value="">Select Gender</option>
                        <option value="Male">Male</option>
                        <option value="Female">Female</option>
                        <option value="Non-Binary">Non-Binary</option>
                        <option value="Prefer not to say">Prefer not to say</option>
                    </select>
                </div>
                
                <div className="form-group">
                    <label htmlFor="identifiedRace">Identified Race:</label>
                    <input 
                        type="text" 
                        id="identifiedRace" 
                        name="identifiedRace" 
                        value={newEmployee.identifiedRace} 
                        onChange={handleInputChange} 
                        disabled={isSubmitting}
                        placeholder="Race/Ethnicity as identified by employee" 
                    />
                </div>
                
                <div className="form-group">
                    <label htmlFor="dob">Date of Birth:</label>
                    <input type="date" id="dob" name="dob" value={newEmployee.dob} onChange={handleInputChange} disabled={isSubmitting} />
                </div>
                
                <div className="form-group">
                    <label htmlFor="mobilePhone">Mobile Phone:</label>
                    <input 
                        type="tel" 
                        id="mobilePhone" 
                        name="mobilePhone" 
                        value={newEmployee.mobilePhone} 
                        onChange={handleInputChange} 
                        disabled={isSubmitting}
                        placeholder="XXX-XXX-XXXX"
                    />
                </div>

                <div style={{marginTop: '1rem'}}> 
                    <button type="submit" disabled={isSubmitting}>
                        {isSubmitting ? 'Creating...' : 'Create Employee'}
                    </button>
                    <button type="button" onClick={() => navigate('/admin/employees')} disabled={isSubmitting} style={{marginLeft: '10px', backgroundColor: '#6c757d'}}> 
                        Cancel
                    </button>
                </div>
            </form>
        </div>
    );
}

export default EmployeeCreate; 