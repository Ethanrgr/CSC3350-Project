import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import '../../pages/PageStyles.css';

function SalaryUpdate() {
    const { apiClient } = useAuth();
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        percentage: '',
        minSalary: '',
        maxSalary: ''
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        // Validate form data
        if (!formData.percentage || !formData.minSalary || !formData.maxSalary) {
            setError('All fields are required.');
            return;
        }

        // Convert to numbers and do additional validation
        const percentage = parseFloat(formData.percentage);
        const minSalary = parseFloat(formData.minSalary);
        const maxSalary = parseFloat(formData.maxSalary);

        if (isNaN(percentage) || isNaN(minSalary) || isNaN(maxSalary)) {
            setError('All fields must be valid numbers.');
            return;
        }

        if (percentage <= 0) {
            setError('Percentage must be greater than 0.');
            return;
        }

        if (minSalary >= maxSalary) {
            setError('Minimum salary must be less than maximum salary.');
            return;
        }

        setLoading(true);
        setError('');
        setSuccessMessage('');

        try {
            console.log('Updating salary range with data:', { percentage, minSalary, maxSalary });
            const response = await apiClient.put('/admin/employees/salary/range', {
                percentage,
                minSalary,
                maxSalary
            });
            
            console.log('Salary update response:', response.data);
            const affectedEmployees = response.data.affectedEmployees || 0;
            
            setSuccessMessage(
                `Salaries updated successfully! ${affectedEmployees} employee${affectedEmployees !== 1 ? 's' : ''} with salaries between $${minSalary.toFixed(2)} and $${maxSalary.toFixed(2)} received a ${percentage}% increase.`
            );
            
            // Reset form
            setFormData({
                percentage: '',
                minSalary: '',
                maxSalary: ''
            });
            
            // Redirect to employees list after short delay so user can see success message
            setTimeout(() => {
                navigate('/admin/employees');
            }, 2000);

        } catch (err) {
            console.error('Failed to update salaries:', err);
            setError(err.response?.data?.message || 'Failed to update salary range. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <h3>Update Salary by Range</h3>
            <p className="form-description">
                Use this form to apply a percentage increase to salaries within a specific range.
            </p>

            {successMessage && <div className="success-message">{successMessage}</div>}
            {error && <div className="error-message">{error}</div>}

            <form onSubmit={handleSubmit} className="edit-form">
                <div className="form-group">
                    <label htmlFor="percentage">Percentage Increase (%):</label>
                    <input 
                        type="number" 
                        id="percentage" 
                        name="percentage" 
                        value={formData.percentage}
                        onChange={handleInputChange}
                        step="0.01"
                        min="0.01"
                        placeholder="e.g., 3.5"
                        disabled={loading}
                        required
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="minSalary">Minimum Salary ($):</label>
                    <input 
                        type="number" 
                        id="minSalary" 
                        name="minSalary" 
                        value={formData.minSalary}
                        onChange={handleInputChange}
                        step="0.01"
                        min="0"
                        placeholder="e.g., 40000.00"
                        disabled={loading}
                        required
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="maxSalary">Maximum Salary ($):</label>
                    <input 
                        type="number" 
                        id="maxSalary" 
                        name="maxSalary" 
                        value={formData.maxSalary}
                        onChange={handleInputChange}
                        step="0.01"
                        min="0"
                        placeholder="e.g., 80000.00"
                        disabled={loading}
                        required
                    />
                </div>

                <button type="submit" disabled={loading}>
                    {loading ? 'Updating Salaries...' : 'Update Salaries'}
                </button>
            </form>
        </div>
    );
}

export default SalaryUpdate; 