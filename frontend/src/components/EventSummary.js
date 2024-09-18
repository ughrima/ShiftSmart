import React, { useState, useEffect } from 'react';
import { Box, CircularProgress, Typography, Grid, Paper } from '@mui/material';
import mockShiftData from './mockShiftData'; // Import mock data

const EventSummary = () => {
    const [employeeData, setEmployeeData] = useState([]); // Initialize as an array
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Use mock data instead of API call
    const fetchEmployeeData = async () => {
        try {
            // Simulate data fetching delay
            setTimeout(() => {
                setEmployeeData(mockShiftData); // Ensure this is an array
                setLoading(false);
            }, 1000);
        } catch (error) {
            setError('Error fetching employee data');
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchEmployeeData();
    }, []);

    if (loading) {
        return (
            <Box className="loading-container">
                <CircularProgress />
            </Box>
        );
    }

    if (error) {
        return (
            <Box className="error-container">
                <Typography variant="h6" color="error">
                    {error}
                </Typography>
            </Box>
        );
    }

    // Check if employeeData is an array before mapping
    if (!Array.isArray(employeeData) || employeeData.length === 0) {
        return (
            <Box className="error-container">
                <Typography variant="h6" color="error">
                    No data available
                </Typography>
            </Box>
        );
    }

    return (
        <Box className="event-summary-container">
            <Typography variant="h4" gutterBottom>
                Employee Event Summary
            </Typography>
            <Grid container spacing={2}>
                {employeeData.map((employee) => (
                    <Grid item xs={12} key={employee.id}>
                        <Paper elevation={3} sx={{ padding: '20px' }}>
                            <Typography variant="h6">{employee.name}</Typography>
                            <Typography variant="body1">
                                <strong>Work Shifts:</strong>
                            </Typography>
                            <ul>
                                {(employee.workShifts || []).map((shift, index) => (
                                    <li key={index}>{shift}</li>
                                ))}
                            </ul>
                            <Typography variant="body1">
                                <strong>Holidays:</strong>
                            </Typography>
                            <ul>
                                {(employee.holidays || []).map((holiday, index) => (
                                    <li key={index}>{holiday}</li>
                                ))}
                            </ul>
                        </Paper>
                    </Grid>
                ))}
            </Grid>
        </Box>
    );
};

export default EventSummary;
