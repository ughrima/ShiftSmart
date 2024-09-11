import React, { useState, useEffect } from 'react';
import { Box, Typography, Paper, CircularProgress, Grid } from '@mui/material';
import './EventSummery.css';

const EventSummary = () => {
    const [employeeData, setEmployeeData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchEmployeeData = async () => {
        try {
            const response = await fetch('https://your-backend-api.com/employees'); // Replace with your backend URL
            if (!response.ok) {
                throw new Error('Error fetching employee data');
            }
            const data = await response.json();
            setEmployeeData(data);
            setLoading(false);
        } catch (error) {
            setError(error.message);
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
                                {employee.workShifts.map((shift, index) => (
                                    <li key={index}>{shift}</li>
                                ))}
                            </ul>
                            <Typography variant="body1">
                                <strong>Holidays:</strong>
                            </Typography>
                            <ul>
                                {employee.holidays.map((holiday, index) => (
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
