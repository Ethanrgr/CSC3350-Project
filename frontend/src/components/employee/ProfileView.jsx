import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import '../../pages/PageStyles.css'; // Corrected path

function ProfileView() {
    const [profile, setProfile] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const { apiClient, user } = useAuth(); // Get user info too if needed for display

    useEffect(() => {
        const fetchProfile = async () => {
            setLoading(true);
            setError('');
            try {
                console.log("Fetching employee profile...");
                // The backend endpoint /employee/profile should use the authenticated user context
                const response = await apiClient.get('/employee/profile');
                console.log("Profile data fetched:", response.data);
                setProfile(response.data);
            } catch (err) {
                console.error("Failed to fetch profile:", err);
                let errorMessage = 'Failed to load profile data.';
                if (err.response) {
                    errorMessage = err.response.data?.message || `Error: ${err.response.status}`;
                } else if (err.request) {
                    errorMessage = 'No response from server.';
                }
                setError(errorMessage);
                setProfile(null);
            } finally {
                setLoading(false);
            }
        };

        fetchProfile();
    }, [apiClient]);

    if (loading) {
        return (
            <div className="loading-container">
                <div className="loading-spinner"></div>
                <p>Loading your profile information...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div className="error-container">
                <div className="error-message">
                    <h3>Error Loading Profile</h3>
                    <p>{error}</p>
                </div>
            </div>
        );
    }

    if (!profile) {
        return (
            <div className="error-container">
                <div className="error-message">
                    <h3>Profile Not Found</h3>
                    <p>Could not load your profile information. Please contact support.</p>
                </div>
            </div>
        );
    }

    // Display the profile information - adjust fields based on your Employee model
    return (
        <div className="profile-container">
            <h3>Your Profile</h3>
            <div className="profile-details">
                <h4>Personal Information</h4>
                <div className="profile-section">
                    <p><strong>Employee ID:</strong> {profile.empId}</p>
                    <p><strong>Full Name:</strong> {profile.fName} {profile.lName}</p>
                    <p><strong>Email:</strong> {profile.email}</p>
                    <p><strong>Date of Birth:</strong> {profile.dob || 'Not available'}</p>
                    <p><strong>Gender:</strong> {profile.gender || 'Not available'}</p>
                    <p><strong>Race/Ethnicity:</strong> {profile.identifiedRace || 'Not available'}</p>
                    <p><strong>Mobile Phone:</strong> {profile.mobilePhone || 'Not available'}</p>
                    <p><strong>SSN:</strong> {profile.ssn ? `***-**-${profile.ssn.slice(-4)}` : 'Not available'}</p>
                </div>
                
                <h4>Employment Information</h4>
                <div className="profile-section">
                    <p><strong>Hire Date:</strong> {profile.hireDate || 'Not available'}</p>
                    <p><strong>Salary:</strong> {profile.salary ? `$${profile.salary.toFixed(2)}` : 'Not available'}</p>
                </div>
                
                <h4>Address Information</h4>
                <div className="profile-section">
                    <p><strong>Street:</strong> {profile.street || 'Not available'}</p>
                    <p><strong>City:</strong> {profile.city ? profile.city.cityName || profile.city.name : 'Not available'}</p>
                    <p><strong>State:</strong> {profile.state ? profile.state.stateName || profile.state.name : 'Not available'}</p>
                    <p><strong>ZIP Code:</strong> {profile.zip || 'Not available'}</p>
                </div>
            </div>
        </div>
    );
}

export default ProfileView;

// Add basic styles for .profile-details if needed
/*
.profile-details {
    background-color: #f8f9fa;
    padding: 1.5rem;
    border-radius: 8px;
    border: 1px solid #dee2e6;
    margin-top: 1rem;
}

.profile-details p {
    margin-bottom: 0.8rem;
    font-size: 1.05rem;
}

.profile-details strong {
    margin-right: 0.5rem;
    color: #495057;
}
*/ 