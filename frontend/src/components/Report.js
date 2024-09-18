import React, { useState } from 'react';
import Select from 'react-select';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
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

    const employees = mockEmployees.map(emp => ({
        value: emp.id,
        label: emp.name
    }));

    // Form submit handler
    const handleSubmit = (e) => {
        e.preventDefault();

        if (selectedEmployees.length === 0 || !startDate || !endDate) {
            setError('Please select employees and specify the date range.');
            return;
        }

        setError('');
        setMessage('');

        // Mock shift generation logic
        const mockResponse = selectedEmployees.map(emp => ({
            employee: emp.label,
            shifts: `Shifts from ${startDate.toLocaleDateString()} to ${endDate.toLocaleDateString()}`
        }));

        console.log(mockResponse);
        setMessage('Shifts generated successfully! Check the console for details.');
    };

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
                    <button type="submit" className="submit-btn">
                        Generate Report
                    </button>
                </form>
            </div>
        </div>
    );
};

export default Report;
