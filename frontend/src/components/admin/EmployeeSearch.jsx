import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { Link } from 'react-router-dom'; // Import Link
import '../../pages/PageStyles.css'; // Corrected path
// We can potentially reuse the EmployeeList component's table or create a dedicated results component

function EmployeeSearch() {
    const [searchParams, setSearchParams] = useState({
        name: '',
        dob: '', // Expects YYYY-MM-DD format from input type="date"
        ssn: '',
        empid: ''
    });
    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [searchAttempted, setSearchAttempted] = useState(false); // Track if a search has been run
    const { apiClient } = useAuth();

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setSearchParams(prev => ({ ...prev, [name]: value }));
    };

    const handleSearch = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');
        setSearchAttempted(true);
        setResults([]); // Clear previous results

        // Construct query parameters, only include non-empty values
        const queryParams = Object.entries(searchParams)
            .filter(([key, value]) => value !== '')
            .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
            .join('&');

        if (!queryParams) {
            setError('Please enter at least one search criterion.');
            setLoading(false);
            return;
        }

        console.log(`Searching employees with query: ${queryParams}`);

        try {
            // Use the correct endpoint as defined in Main.java
            const response = await apiClient.get(`/admin/search?${queryParams}`);
            console.log("Search results:", response.data);
            setResults(response.data || []); // Ensure results is an array
            if (!response.data || response.data.length === 0) {
                 // Optional: Set a message indicating no results found, distinct from an error
                 console.log("No employees found matching the criteria.");
            }
        } catch (err) {
            console.error("Failed to search employees:", err);
            let errorMessage = 'Failed to perform search.';
             if (err.response) {
                 errorMessage = err.response.data?.message || `Error: ${err.response.status}`;
             } else if (err.request) {
                 errorMessage = 'No response from server.';
             }
             setError(errorMessage);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <h3>Search Employees</h3>
            <form onSubmit={handleSearch} className="search-form"> {/* Style this form */} 
                <div className="form-group">
                    <label htmlFor="name">Name:</label>
                    <input type="text" id="name" name="name" value={searchParams.name} onChange={handleInputChange} />
                </div>
                <div className="form-group">
                    <label htmlFor="dob">Date of Birth:</label>
                    <input type="date" id="dob" name="dob" value={searchParams.dob} onChange={handleInputChange} />
                </div>
                <div className="form-group">
                    <label htmlFor="ssn">SSN:</label>
                    <input type="text" id="ssn" name="ssn" value={searchParams.ssn} onChange={handleInputChange} />
                </div>
                <div className="form-group">
                    <label htmlFor="empid">Employee ID:</label>
                    <input type="number" id="empid" name="empid" value={searchParams.empid} onChange={handleInputChange} />
                </div>
                <button type="submit" disabled={loading}>
                    {loading ? 'Searching...' : 'Search'}
                </button>
            </form>

            {error && <div className="error-message" style={{marginTop: '1rem'}}>{error}</div>}

            {/* Display Results */} 
            <div className="search-results" style={{marginTop: '2rem'}}> 
                <h4>Results</h4>
                 {loading && !searchAttempted && <div>Loading initial state...</div>} {/* Should not show if loading is for search */} 
                {loading && searchAttempted && <div>Searching...</div>}
                 {!loading && searchAttempted && results.length === 0 && !error && (
                     <p>No employees found matching your criteria.</p>
                )}
                {!loading && results.length > 0 && (
                     <table className="data-table"> 
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>First Name</th>
                                <th>Last Name</th>
                                <th>Email</th>
                                <th>Hire Date</th>
                                <th>Salary</th>
                                <th>SSN</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {results.map((emp) => (
                                <tr key={emp.empId}>
                                    <td>{emp.empId}</td>
                                    <td>{emp.fName}</td>
                                    <td>{emp.lName}</td>
                                    <td>{emp.email}</td>
                                    <td>{emp.hireDate}</td>
                                    <td>{emp.salary ? emp.salary.toFixed(2) : 'N/A'}</td>
                                    <td>{emp.ssn}</td>
                                    <td>
                                         {/* Update Edit button to be a Link */}
                                        <Link to={`/admin/employees/edit/${emp.empId}`} className="data-table-button edit-btn">Edit</Link> 
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                )}
            </div>
        </div>
    );
}

export default EmployeeSearch;

// Add styles for .search-form if needed in PageStyles.css 