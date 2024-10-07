import React, { useState, useEffect } from 'react';
import Select from 'react-select';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { Box, CircularProgress, Typography, Grid, Paper, TextField, Button, Divider } from '@mui/material';
import mockShiftData from './mockShiftData'; // Import mock data
import './Report.css'; // Ensure this CSS file exists

const mockEmployees = [
    { id: 1, name: 'John Doe' },
    { id: 2, name: 'Jane Smith' },
    { id: 3, name: 'Michael Brown' }
];

const Report = () => {
    const [selectedEmployees, setSelectedEmployees] = useState([]);
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [error, setError] = useState('');
    const [message, setMessage] = useState('');
    const [employeeData, setEmployeeData] = useState([]); // Initialize as an array
    const [loading, setLoading] = useState(true);
    const [filterStartDate, setFilterStartDate] = useState(''); // Date selector for event summary
    const [filterEndDate, setFilterEndDate] = useState('');
    const [selectedEmployee, setSelectedEmployee] = useState('');

    const employees = mockEmployees.map(emp => ({
        value: emp.id,
        label: emp.name
    }));

    const handleSubmit = (e) => {
        e.preventDefault();

        if (selectedEmployees.length === 0 || !startDate || !endDate) {
            setError('Please select employees and specify the date range.');
            return;
        }

        setError('');
        setMessage('');

        const mockResponse = selectedEmployees.map(emp => ({
            employee: emp.label,
            shifts: `Shifts from ${startDate.toLocaleDateString()} to ${endDate.toLocaleDateString()}`
        }));

        console.log(mockResponse);
        setMessage('Shifts generated successfully! Check the console for details.');
    };

    // Fetch mock data for employee summary
    useEffect(() => {
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
        fetchEmployeeData();
    }, []);

    // Handle filtering for event summary
    const handleFilter = () => {
        console.log('Filtering data:', filterStartDate, filterEndDate, selectedEmployee);
        // Implement logic for filtering data based on the selected criteria
    };

    return (
        <div className="main-container" style={{ marginTop: '60px' }}>
            <div className="form-container">
                <h1 className="form-title">Employee Selection Form</h1>
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="employees">Select Employees:</label>
                        <Select
                            id="employees"
                            isMulti
                            options={employees}
                            value={selectedEmployees}
                            onChange={setSelectedEmployees}
                            className="select-dropdown"
                            placeholder="Choose employees"
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="startDate">Start Date:</label>
                        <DatePicker
                            id="startDate"
                            selected={startDate}
                            onChange={setStartDate}
                            dateFormat="yyyy/MM/dd"
                            placeholderText="Select start date"
                            className="date-picker"
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="endDate">End Date:</label>
                        <DatePicker
                            id="endDate"
                            selected={endDate}
                            onChange={setEndDate}
                            dateFormat="yyyy/MM/dd"
                            placeholderText="Select end date"
                            className="date-picker"
                        />
                    </div>

                    {error && <p className="error-message">{error}</p>}
                    {message && <p className="success-message">{message}</p>}

                    <button type="submit" className="submit-btn">
                        Generate Report
                    </button>
                </form>
            </div>

            {/* Event Summary Section */}
            <Box className="event-summary-container" sx={{ padding: '30px' }}>
                <Typography variant="h4" gutterBottom>
                    Employee Event Summary
                </Typography>

                {loading ? (
                    <Box className="loading-container" sx={{ display: 'flex', justifyContent: 'center', padding: '50px' }}>
                        <CircularProgress />
                    </Box>
                ) : (
                    <>
                        {error && (
                            <Box className="error-container" sx={{ textAlign: 'center', padding: '20px' }}>
                                <Typography variant="h6" color="error">{error}</Typography>
                            </Box>
                        )}

                        <Box sx={{ marginBottom: '20px' }}>
                            <Grid container spacing={2}>
                                <Grid item xs={12} sm={4}>
                                    <TextField
                                        label="Start Date"
                                        type="date"
                                        value={filterStartDate}
                                        onChange={(e) => setFilterStartDate(e.target.value)}
                                        fullWidth
                                        InputLabelProps={{ shrink: true }}
                                    />
                                </Grid>
                                <Grid item xs={12} sm={4}>
                                    <TextField
                                        label="End Date"
                                        type="date"
                                        value={filterEndDate}
                                        onChange={(e) => setFilterEndDate(e.target.value)}
                                        fullWidth
                                        InputLabelProps={{ shrink: true }}
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

                        <Grid container spacing={2}>
                            {employeeData.map((employee) => (
                                <Grid item xs={12} key={employee.id}>
                                    <Paper elevation={3} sx={{ padding: '20px' }}>
                                        <Typography variant="h6">{employee.name}</Typography>
                                        <Typography variant="body1"><strong>Jobs:</strong></Typography>
                                        <ul>
                                            {(employee.jobs || []).map((job, index) => (
                                                <li key={index}>{job}</li>
                                            ))}
                                        </ul>
                                        <Typography variant="body1"><strong>Permits:</strong></Typography>
                                        <ul>
                                            {(employee.permits || []).map((permit, index) => (
                                                <li key={index}>{permit}</li>
                                            ))}
                                        </ul>
                                        <Typography variant="body1"><strong>Holidays:</strong></Typography>
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
                    </>
                )}
            </Box>
        </div>
    );
};

export default Report;
