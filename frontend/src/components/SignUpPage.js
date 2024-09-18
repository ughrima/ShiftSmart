import React, { useState } from 'react';
import { TextField, Button, Paper, Typography, Box } from '@mui/material';
import './SignUpPage.css';

const SignUpPage = () => {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [csvFile, setCsvFile] = useState(null);

    const handleFileChange = (e) => {
        setCsvFile(e.target.files[0]);
    };

    const handleSignUp = () => {
        console.log('Username:', username);
        console.log('Email:', email);
        console.log('Password:', password);
        console.log('CSV File:', csvFile);
        // Add your signup logic here (e.g., API call to send the data to backend)
    };

    return (
        <Box className="signup-container">
            <Paper elevation={3} className="signup-paper">
                <Typography variant="h5" className="signup-header">
                    Sign Up
                </Typography>
                <form noValidate autoComplete="off">
                    <TextField
                        label="Username"
                        variant="outlined"
                        fullWidth
                        margin="normal"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        className="signup-textfield"
                    />
                    <TextField
                        label="Email"
                        variant="outlined"
                        fullWidth
                        margin="normal"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        className="signup-textfield"
                    />
                    <TextField
                        label="Password"
                        variant="outlined"
                        fullWidth
                        margin="normal"
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        className="signup-textfield"
                    />
                    <Typography variant="body2" style={{ marginTop: '20px' }}>
                        Upload CSV of Employee Details:
                    </Typography>
                    <input
                        type="file"
                        accept=".csv"
                        onChange={handleFileChange}
                        className="signup-csv-upload"
                    />
                    <Button
                        className="signup-button"
                        variant="contained"
                        fullWidth
                        onClick={handleSignUp}
                        sx={{
                            backgroundColor: '#045656',
                            color: 'white',
                            marginTop: '20px',
                            '&:hover': {
                                backgroundColor: '#034f4f',
                            },
                        }}
                    >
                        Sign Up
                    </Button>
                </form>
            </Paper>
        </Box>
    );
};

export default SignUpPage;
