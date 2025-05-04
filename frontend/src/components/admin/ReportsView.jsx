import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import '../../pages/PageStyles.css';

function ReportsView() {
    const { apiClient } = useAuth();
    const [activeTab, setActiveTab] = useState('paystatements');
    const [payStatements, setPayStatements] = useState([]);
    const [jobTitleReport, setJobTitleReport] = useState([]);
    const [divisionReport, setDivisionReport] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [reportParams, setReportParams] = useState({
        month: new Date().getMonth() + 1,
        year: new Date().getFullYear()
    });

    useEffect(() => {
        if (activeTab === 'paystatements') {
            fetchPayStatements();
        }
    }, [activeTab]);

    const fetchPayStatements = async () => {
        setLoading(true);
        setError('');
        
        try {
            console.log('Fetching pay statements');
            const response = await apiClient.get('/admin/reports/paystatements');
            console.log('Pay statements response:', response.data);
            setPayStatements(response.data || []);
        } catch (err) {
            console.error('Failed to fetch pay statements:', err);
            setError('Failed to load pay statements. Please try again.');
            setPayStatements([]);
        } finally {
            setLoading(false);
        }
    };

    const fetchJobTitleReport = async () => {
        setLoading(true);
        setError('');
        
        try {
            console.log('Fetching job title pay report:', reportParams);
            const response = await apiClient.get(
                `/admin/reports/pay/jobtitle?month=${reportParams.month}&year=${reportParams.year}`
            );
            console.log('Job title pay report response:', response.data);
            setJobTitleReport(response.data || []);
        } catch (err) {
            console.error('Failed to fetch job title pay report:', err);
            setError('Failed to load job title pay report. Please try again.');
            setJobTitleReport([]);
        } finally {
            setLoading(false);
        }
    };

    const fetchDivisionReport = async () => {
        setLoading(true);
        setError('');
        
        try {
            console.log('Fetching division pay report:', reportParams);
            const response = await apiClient.get(
                `/admin/reports/pay/division?month=${reportParams.month}&year=${reportParams.year}`
            );
            console.log('Division pay report response:', response.data);
            setDivisionReport(response.data || []);
        } catch (err) {
            console.error('Failed to fetch division pay report:', err);
            setError('Failed to load division pay report. Please try again.');
            setDivisionReport([]);
        } finally {
            setLoading(false);
        }
    };

    const handleTabChange = (tab) => {
        setActiveTab(tab);
        setError('');

        if (tab === 'paystatements') {
            fetchPayStatements();
        } else if (tab === 'jobtitle') {
            fetchJobTitleReport();
        } else if (tab === 'division') {
            fetchDivisionReport();
        }
    };

    const handleParamChange = (e) => {
        const { name, value } = e.target;
        setReportParams(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleGenerateReport = (e) => {
        e.preventDefault();
        
        if (activeTab === 'jobtitle') {
            fetchJobTitleReport();
        } else if (activeTab === 'division') {
            fetchDivisionReport();
        }
    };

    return (
        <div className="reports-container">
            <h3>Reports</h3>
            
            {/* Tab navigation */}
            <div className="report-tabs">
                <button 
                    className={`tab-button ${activeTab === 'paystatements' ? 'active' : ''}`}
                    onClick={() => handleTabChange('paystatements')}
                >
                    Pay Statements
                </button>
                <button 
                    className={`tab-button ${activeTab === 'jobtitle' ? 'active' : ''}`}
                    onClick={() => handleTabChange('jobtitle')}
                >
                    Pay by Job Title
                </button>
                <button 
                    className={`tab-button ${activeTab === 'division' ? 'active' : ''}`}
                    onClick={() => handleTabChange('division')}
                >
                    Pay by Division
                </button>
            </div>
            
            {/* Error message display */}
            {error && <div className="error-message">{error}</div>}
            
            {/* Loading indicator */}
            {loading && <div className="loading-indicator">Loading report data...</div>}
            
            {/* Report content based on active tab */}
            <div className="report-content">
                {/* Pay Statements Report */}
                {activeTab === 'paystatements' && (
                    <div className="pay-statements-report">
                        <h4>All Employee Pay Statements</h4>
                        {payStatements.length === 0 ? (
                            <p>No pay statements found.</p>
                        ) : (
                            <table className="data-table">
                                <thead>
                                    <tr>
                                        <th>Employee ID</th>
                                        <th>First Name</th>
                                        <th>Last Name</th>
                                        <th>Pay Date</th>
                                        <th>Amount</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {payStatements.map((statement, index) => (
                                        <tr key={index}>
                                            <td>{statement.empid}</td>
                                            <td>{statement.fname}</td>
                                            <td>{statement.lname}</td>
                                            <td>{statement.pay_date}</td>
                                            <td>${statement.amount.toFixed(2)}</td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        )}
                    </div>
                )}
                
                {/* Job Title Report */}
                {activeTab === 'jobtitle' && (
                    <div className="job-title-report">
                        <h4>Total Pay by Job Title</h4>
                        
                        {/* Report parameters form */}
                        <form onSubmit={handleGenerateReport} className="report-params-form">
                            <div className="form-row">
                                <div className="form-group">
                                    <label htmlFor="month">Month:</label>
                                    <select 
                                        id="month" 
                                        name="month" 
                                        value={reportParams.month}
                                        onChange={handleParamChange}
                                        disabled={loading}
                                    >
                                        {Array.from({length: 12}, (_, i) => i + 1).map(month => (
                                            <option key={month} value={month}>{month}</option>
                                        ))}
                                    </select>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="year">Year:</label>
                                    <select 
                                        id="year" 
                                        name="year" 
                                        value={reportParams.year}
                                        onChange={handleParamChange}
                                        disabled={loading}
                                    >
                                        {Array.from({length: 5}, (_, i) => new Date().getFullYear() - 2 + i).map(year => (
                                            <option key={year} value={year}>{year}</option>
                                        ))}
                                    </select>
                                </div>
                                <button type="submit" disabled={loading}>
                                    Generate Report
                                </button>
                            </div>
                        </form>
                        
                        {/* Report data table */}
                        {jobTitleReport.length === 0 ? (
                            <p>No data available for the selected period.</p>
                        ) : (
                            <table className="data-table">
                                <thead>
                                    <tr>
                                        <th>Job Title</th>
                                        <th>Total Pay</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {jobTitleReport.map((item, index) => (
                                        <tr key={index}>
                                            <td>{item.job_title}</td>
                                            <td>${item.total_pay.toFixed(2)}</td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        )}
                    </div>
                )}
                
                {/* Division Report */}
                {activeTab === 'division' && (
                    <div className="division-report">
                        <h4>Total Pay by Division</h4>
                        
                        {/* Report parameters form (reusing the same structure) */}
                        <form onSubmit={handleGenerateReport} className="report-params-form">
                            <div className="form-row">
                                <div className="form-group">
                                    <label htmlFor="month">Month:</label>
                                    <select 
                                        id="month" 
                                        name="month" 
                                        value={reportParams.month}
                                        onChange={handleParamChange}
                                        disabled={loading}
                                    >
                                        {Array.from({length: 12}, (_, i) => i + 1).map(month => (
                                            <option key={month} value={month}>{month}</option>
                                        ))}
                                    </select>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="year">Year:</label>
                                    <select 
                                        id="year" 
                                        name="year" 
                                        value={reportParams.year}
                                        onChange={handleParamChange}
                                        disabled={loading}
                                    >
                                        {Array.from({length: 5}, (_, i) => new Date().getFullYear() - 2 + i).map(year => (
                                            <option key={year} value={year}>{year}</option>
                                        ))}
                                    </select>
                                </div>
                                <button type="submit" disabled={loading}>
                                    Generate Report
                                </button>
                            </div>
                        </form>
                        
                        {/* Report data table */}
                        {divisionReport.length === 0 ? (
                            <p>No data available for the selected period.</p>
                        ) : (
                            <table className="data-table">
                                <thead>
                                    <tr>
                                        <th>Division</th>
                                        <th>Total Pay</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {divisionReport.map((item, index) => (
                                        <tr key={index}>
                                            <td>{item.division_name}</td>
                                            <td>${item.total_pay.toFixed(2)}</td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
}

export default ReportsView; 