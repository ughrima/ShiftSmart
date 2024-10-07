import React, { useState, useEffect } from 'react';
import {
    Box, CircularProgress, Typography, Grid, Paper, TextField, Button, Divider
} from '@mui/material';
import mockShiftData from './mockShiftData'; // Import mock data

const EventSummary = () => {
    const [employeeData, setEmployeeData] = useState([]); // Initialize as an array
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [startDate, setStartDate] = useState(''); // Date selector
    const [endDate, setEndDate] = useState('');
    const [selectedEmployee, setSelectedEmployee] = useState('');

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

    // Handle form submission (for filtering data)
    const handleFilter = () => {
        // Logic for filtering data can go here based on startDate, endDate, and selectedEmployee
        console.log('Filtering data:', startDate, endDate, selectedEmployee);
        // Optionally filter employeeData if needed
    };

    if (loading) {
        return (
            <Box className="loading-container" sx={{ display: 'flex', justifyContent: 'center', padding: '50px' }}>
                <CircularProgress />
            </Box>
        );
    }

    if (error) {
        return (
            <Box className="error-container" sx={{ textAlign: 'center', padding: '20px' }}>
                <Typography variant="h6" color="error">
                    {error}
                </Typography>
            </Box>
        );
    }

    // Check if employeeData is an array before mapping
    if (!Array.isArray(employeeData) || employeeData.length === 0) {
        return (
            <Box className="error-container" sx={{ textAlign: 'center', padding: '20px' }}>
                <Typography variant="h6" color="error">
                    No data available
                </Typography>
            </Box>
        );
    }

    return (
        <Box className="event-summary-container" sx={{ padding: '30px' }}>
            <Typography variant="h4" gutterBottom>
                Employee Event Summary
            </Typography>

            {/* Date and Employee Selector */}
            <Box sx={{ marginBottom: '20px' }}>
                <Grid container spacing={2}>
                    <Grid item xs={12} sm={4}>
                        <TextField
                            label="Start Date"
                            type="date"
                            value={startDate}
                            onChange={(e) => setStartDate(e.target.value)}
                            fullWidth
                            InputLabelProps={{
                                shrink: true,
                            }}
                        />
                    </Grid>
                    <Grid item xs={12} sm={4}>
                        <TextField
                            label="End Date"
                            type="date"
                            value={endDate}
                            onChange={(e) => setEndDate(e.target.value)}
                            fullWidth
                            InputLabelProps={{
                                shrink: true,
                            }}
                        />
                    </Grid>
                    <Grid item xs={12} sm={4}>
                        <TextField
                            label="Employee"
                            value={selectedEmployee}
                            onChange={(e) => setSelectedEmployee(e.target.value)}
                            fullWidth
                        />
                    </Grid>
                    <Grid item xs={12}>
                        <Button
                            variant="contained"
                            onClick={handleFilter}
                            fullWidth
                        >
                            Filter Data
                        </Button>
                    </Grid>
                </Grid>
            </Box>

            <Divider sx={{ marginBottom: '20px' }} />

            {/* Display filtered employee data */}
            <Grid container spacing={2}>
                {employeeData.map((employee) => (
                    <Grid item xs={12} key={employee.id}>
                        <Paper elevation={3} sx={{ padding: '20px' }}>
                            <Typography variant="h6">{employee.name}</Typography>
                            <Typography variant="body1">
                                <strong>Jobs:</strong>
                            </Typography>
                            <ul>
                                {(employee.jobs || []).map((job, index) => (
                                    <li key={index}>{job}</li>
                                ))}
                            </ul>
                            <Typography variant="body1">
                                <strong>Permits:</strong>
                            </Typography>
                            <ul>
                                {(employee.permits || []).map((permit, index) => (
                                    <li key={index}>{permit}</li>
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
                            <Typography variant="body1" sx={{ marginTop: '10px' }}>
                                <strong>Total Hours Worked:</strong> {employee.totalHours || 0} hours
                            </Typography>
                        </Paper>
                    </Grid>
                ))}
            </Grid>
        </Box>
    );
};

export default EventSummary;
