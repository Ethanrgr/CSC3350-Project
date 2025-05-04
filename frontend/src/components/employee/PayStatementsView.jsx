import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import '../../pages/PageStyles.css';

function PayStatementsView() {
    const { apiClient } = useAuth();
    const [payStatements, setPayStatements] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [filters, setFilters] = useState({
        startDate: '',
        endDate: ''
    });

    // Fetch pay statements on component mount
    useEffect(() => {
        fetchPayStatements();
    }, []);

    // Fetch pay statements with optional date filters
    const fetchPayStatements = async () => {
        setLoading(true);
        setError('');

        // Build query string with any filters that are set
        let queryParams = '';
        if (filters.startDate) {
            queryParams += `startDate=${filters.startDate}`;
        }
        if (filters.endDate) {
            queryParams += queryParams ? `&endDate=${filters.endDate}` : `endDate=${filters.endDate}`;
        }

        const endpoint = `/employee/paystatements${queryParams ? '?' + queryParams : ''}`;
        
        try {
            console.log('Fetching employee pay statements:', endpoint);
            const response = await apiClient.get(endpoint);
            console.log('Pay statements response:', response.data);
            setPayStatements(response.data || []);
        } catch (err) {
            console.error('Failed to fetch pay statements:', err);
            let errorMessage = 'Failed to load pay statement data.';
            if (err.response) {
                errorMessage = err.response.data?.message || `Error: ${err.response.status}`;
            } else if (err.request) {
                errorMessage = 'No response from server.';
            }
            setError(errorMessage);
            setPayStatements([]);
        } finally {
            setLoading(false);
        }
    };

    // Handle filter changes
    const handleFilterChange = (e) => {
        const { name, value } = e.target;
        setFilters(prev => ({
            ...prev,
            [name]: value
        }));
    };

    // Apply filters
    const handleFilterSubmit = (e) => {
        e.preventDefault();
        fetchPayStatements();
    };

    // Clear filters
    const handleClearFilters = () => {
        setFilters({
            startDate: '',
            endDate: ''
        });
        // Fetch all pay statements without filters
        fetchPayStatements();
    };

    return (
        <div className="pay-statements-container">
            <h3>Your Pay Statements</h3>
            
            {/* Filter form */}
            <form onSubmit={handleFilterSubmit} className="filter-form">
                <div className="form-row">
                    <div className="form-group">
                        <label htmlFor="startDate">Start Date:</label>
                        <input 
                            type="date" 
                            id="startDate" 
                            name="startDate"
                            value={filters.startDate}
                            onChange={handleFilterChange}
                            disabled={loading}
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="endDate">End Date:</label>
                        <input 
                            type="date" 
                            id="endDate" 
                            name="endDate"
                            value={filters.endDate}
                            onChange={handleFilterChange}
                            disabled={loading}
                        />
                    </div>
                    <div className="button-group">
                        <button type="submit" disabled={loading}>
                            {loading ? 'Loading...' : 'Apply Filters'}
                        </button>
                        <button 
                            type="button" 
                            onClick={handleClearFilters} 
                            disabled={loading || (!filters.startDate && !filters.endDate)}
                        >
                            Clear Filters
                        </button>
                    </div>
                </div>
            </form>
            
            {/* Error message display */}
            {error && <div className="error-message">{error}</div>}
            
            {/* Pay statements table */}
            {loading ? (
                <div className="loading-indicator">Loading pay statements...</div>
            ) : payStatements.length === 0 ? (
                <p>No pay statements found. {filters.startDate || filters.endDate ? 'Try adjusting your filters.' : ''}</p>
            ) : (
                <table className="data-table">
                    <thead>
                        <tr>
                            <th>Pay Date</th>
                            <th>Amount</th>
                        </tr>
                    </thead>
                    <tbody>
                        {payStatements.map((statement, index) => (
                            <tr key={index}>
                                <td>{statement.pay_date}</td>
                                <td>${statement.amount.toFixed(2)}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}

export default PayStatementsView; 