import React, { useState } from 'react';
import { TextField, Button, Typography, Box } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import './LoginPage.css';

const LoginPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleLogin = async () => {
        if (!email.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) {
            setError('Please enter a valid email address.');
            return;
        }
        if (!password) {
            setError('Password cannot be empty.');
            return;
        }

        setError('');
        try {
            const response = await fetch('/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password }),
            });
            const data = await response.json();

            if (response.ok) {
                localStorage.setItem('token', data.token);
                navigate('/shift');
            } else {
                setError(data.message || 'Invalid email or password.');
            }
        } catch (err) {
            setError('Server error. Please try again later.');
        }
    };

    return (
        <Box className="login-container">
            {/* Background with overlay */}
            <Box className="login-background"></Box>

            {/* Login form in the bottom-right corner */}
            <Box className="login-form-container">
                <Typography variant="h4" className="login-header">
                    Login
                </Typography>
                {error && <Typography className="error-message">{error}</Typography>}
                <form noValidate autoComplete="off">
                    <TextField
                        label="Email"
                        variant="outlined"
                        fullWidth
                        margin="normal"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        className="login-textfield"
                    />
                    <TextField
                        label="Password"
                        variant="outlined"
                        fullWidth
                        margin="normal"
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        className="login-textfield"
                    />
                    <Button
                        className="login-button"
                        variant="contained"
                        fullWidth
                        onClick={handleLogin}
                        sx={{
                            backgroundColor: '#FF6600',
                            color: 'white',
                            '&:hover': { backgroundColor: '#E65C00' },
                        }}
                    >
                        Login
                    </Button>
                </form>
            </Box>
        </Box>
    );
};

export default LoginPage;
