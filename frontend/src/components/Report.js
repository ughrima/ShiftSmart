import React, { useState, useEffect } from 'react';
import Select from 'react-select';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import axios from 'axios';
import Spinner from './Spinner';
import './Report.css'; // Ensure this CSS file exists

const Report = () => {
    const [selectedEmployees, setSelectedEmployees] = useState([]);
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [employees, setEmployees] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [message, setMessage] = useState('');

    // Fetch employee data when component mounts
    useEffect(() => {
        const fetchEmployees = async () => {
            setLoading(true);
            try {
                const response = await axios.get('/api/employees');
                const formattedEmployees = response.data.map(emp => ({
                    value: emp.id,
                    label: emp.name
                }));
                setEmployees(formattedEmployees);
            } catch (error) {
                setError('Error fetching employee data. Please try again.');
            } finally {
                setLoading(false);
            }
        };

        fetchEmployees();
    }, []);

    // Form submit handler
    const handleSubmit = async (e) => {
        e.preventDefault();

        if (selectedEmployees.length === 0 || !startDate || !endDate) {
            setError('Please select employees and specify the date range.');
            return;
        }

        setError('');
        setMessage('');
        setLoading(true);

        try {
            const response = await axios.post('/api/generate-shifts', {
                employees: selectedEmployees.map(emp => emp.value),
                startDate,
                endDate
            });
            // Use the response, e.g., log it or display a message
            console.log(response.data);
            setMessage('Shifts generated successfully!');
            
        } catch (error) {
            setError('Error generating shifts. Please try again.');
        } finally {
            setLoading(false);
        }
        
    };

    if (loading && employees.length === 0) {
        return <Spinner />; // Show spinner if employee data is loading
    }

    return (
        <div className="main-container">
            <div className="form-container">
                <h1>Employee Selection Form</h1>
                <form onSubmit={handleSubmit}>
                    {/* Employee Select */}
                    <div className="form-group">
                        <label htmlFor="employees">Select Employees:</label>
                        <Select
                            id="employees"
                            isMulti
                            options={employees}
                            value={selectedEmployees}
                            onChange={setSelectedEmployees}
                            className="select-dropdown"
                        />
                    </div>

                    {/* Start Date Picker */}
                    <div className="form-group">
                        <label htmlFor="startDate">Start Date:</label>
                        <DatePicker
                            id="startDate"
                            selected={startDate}
                            onChange={setStartDate}
                            dateFormat="yyyy/MM/dd"
                            placeholderText="Select start date"
                        />
                    </div>

                    {/* End Date Picker */}
                    <div className="form-group">
                        <label htmlFor="endDate">End Date:</label>
                        <DatePicker
                            id="endDate"
                            selected={endDate}
                            onChange={setEndDate}
                            dateFormat="yyyy/MM/dd"
                            placeholderText="Select end date"
                        />
                    </div>

                    {/* Error and success messages */}
                    {error && <p className="error-message">{error}</p>}
                    {message && <p className="success-message">{message}</p>}

                    {/* Submit Button */}
                    <button type="submit" className="submit-btn" disabled={loading}>
                        {loading ? 'Generating...' : 'Generate'}
                    </button>
                </form>
            </div>
        </div>
    );
};

export default Report;
