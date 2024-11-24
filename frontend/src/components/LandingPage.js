import React from 'react';
import { Link } from 'react-router-dom';
import { Button, Typography, Box, TextField, AppBar, Toolbar } from '@mui/material';
import './LandingPage.css';

const LandingPage = () => {
    return (
        <div className="landing-page">
            {/* Navbar */}
            <AppBar position="static" className="navbar" sx={{ backgroundColor: '#045656' }}>
                <Toolbar className="navbar-toolbar">
                    {/* Left Section: Title and Links */}
                    <Box className="navbar-left" sx={{ display: 'flex', alignItems: 'center' }}>
                        <Typography variant="h6" className="navbar-title" sx={{ marginRight: '20px' }}>
                            Workforce Shift Scheduling
                        </Typography>
                        <Box className="navbar-links" sx={{ display: 'flex', gap: '20px' }}>
                            <a href="#features" className="navbar-link">
                                Features
                            </a>
                            <a href="#about" className="navbar-link">
                                About Us
                            </a>
                            <a href="#contact" className="navbar-link">
                                Contact Us
                            </a>
                        </Box>
                    </Box>

                    {/* Right Section: Login and Sign Up Buttons */}
                    <Box className="navbar-auth" sx={{ display: 'flex', gap: '15px' }}>
                        <Link to="/login" className="navbar-auth-link">
                            <Button
                                variant="outlined"
                                className="auth-btn"
                                sx={{
                                    borderColor: '#fff',
                                    color: '#fff',
                                    '&:hover': {
                                        borderColor: '#ccc',
                                        color: '#ccc',
                                    },
                                }}
                            >
                                Login
                            </Button>
                        </Link>
                        <Link to="/signup" className="navbar-auth-link">
                            <Button
                                variant="contained"
                                className="auth-btn"
                                sx={{
                                    backgroundColor: '#fff',
                                    color: '#045656',
                                    '&:hover': {
                                        backgroundColor: '#ccc',
                                    },
                                }}
                            >
                                Sign Up
                            </Button>
                        </Link>
                    </Box>
                </Toolbar>
            </AppBar>

            {/* Header Section */}
            <header className="landing-header">
                <h1>Efficient Workforce Management</h1>
                <p>Streamline shift scheduling and team collaboration.</p>
                <div className="header-buttons">
                    <Link to="/login">
                        <Button
                            variant="contained"
                            className="landing-btn"
                            sx={{
                                backgroundColor: '#2A3D45',
                                '&:hover': { backgroundColor: '#1f2d33' },
                            }}
                        >
                            Get Started
                        </Button>
                    </Link>
                </div>
            </header>
            {/* Features Section */}
            <section id="features" className="features-section">
                <Typography variant="h4" className="section-title">
                    Features
                </Typography>
                <div className="features-container">
                    <div className="feature-card feature1">
                        <h3>Automated Scheduling</h3>
                        <p>Save time by automating shift schedules for your team.</p>
                        <div className="feature-details">
                            <p><strong>Minimize Errors:</strong> Automatically create accurate schedules, eliminating manual mistakes like double bookings or missing shifts.</p>
                            <p><strong>Employee Preferences:</strong> Employees can input their availability and preferred shifts, and the system will generate schedules that accommodate their needs.</p>
                            <p><strong>Real-Time Updates:</strong> Instant notifications for employees and managers about schedule changes, keeping everyone informed.</p>
                            <p><strong>Compliance & Fairness:</strong> Ensures schedules comply with labor laws and company policies, maintaining fairness in shift assignments.</p>
                            <p><strong>Cost-Effective:</strong> Reduces the need for manual intervention and optimizes staffing levels to prevent overstaffing or understaffing.</p>
                            <p>The system automatically adapts to last-minute changes, like shift swaps or sick days, and provides a customizable solution for your business.</p>
                        </div>
                    </div>
                    <div className="feature-card feature2">
                        <h3>Detailed Reports</h3>
                        <p>Access employee performance and shift history insights.</p>
                    </div>
                    <div className="feature-card feature3">
                        <h3>Easy Collaboration</h3>
                        <p>Share schedules and updates with employees seamlessly.</p>
                    </div>
                </div>
            </section>

            {/* About Us Section */}
            <section id="about" className="about-section">
                <Typography variant="h4" className="section-title">
                    About Us
                </Typography>
                <p className="about-text">
                    Workforce Shift Scheduling helps organizations simplify workforce management. Our mission is to
                    make shift planning, reporting, and collaboration effortless, so you can focus on growing your
                    business.
                </p>
            </section>

            {/* Contact Us Section */}
            <section id="contact" className="contact-section">
                <Typography variant="h4" className="section-title">
                    Contact Us
                </Typography>
                <form className="contact-form">
                    <TextField
                        label="Name"
                        variant="outlined"
                        fullWidth
                        margin="normal"
                        className="contact-input"
                    />
                    <TextField
                        label="Email"
                        variant="outlined"
                        fullWidth
                        margin="normal"
                        className="contact-input"
                    />
                    <TextField
                        label="Message"
                        variant="outlined"
                        fullWidth
                        multiline
                        rows={4}
                        margin="normal"
                        className="contact-input"
                    />
                    <Button
                        variant="contained"
                        className="contact-btn"
                        sx={{
                            backgroundColor: '#2A3D45',
                            '&:hover': { backgroundColor: '#1f2d33' },
                        }}
                    >
                        Submit
                    </Button>
                </form>
            </section>

            {/* Footer Section */}
            <footer className="landing-footer">
                <Typography variant="body2">
                    © 2024 Workforce Shift Scheduling. All rights reserved.
                </Typography>
            </footer>
        </div>
    );
};

export default LandingPage;
