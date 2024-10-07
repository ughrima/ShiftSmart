import React from 'react';
import { AppBar, Toolbar, Typography, Button, Box, IconButton } from '@mui/material';
import { Link } from 'react-router-dom'; // Import Link for routing
import CalendarTodayIcon from '@mui/icons-material/CalendarToday';
import AssessmentIcon from '@mui/icons-material/Assessment';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import './Taskbar.css'; 

const Taskbar = () => {
    return (
        <>
            {/* AppBar for the taskbar navigation */}
            <AppBar position="static" sx={{ backgroundColor: '#045656' }}>
                <Toolbar>
                    <img src="/path-to-logo.png" alt="logo" style={{ width: '40px', marginRight: '10px' }} />
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                        ShiftSmart
                    </Typography>

                    {/* Navigation Buttons */}
                    <Button
                        color="inherit"
                        startIcon={<CalendarTodayIcon />}
                        component={Link} to="/" // Use Link for navigation
                    >
                        Calendar
                    </Button>

                    <Button
                        color="inherit"
                        startIcon={<AssessmentIcon />}
                        component={Link} to="/report" // Use Link for navigation
                    >
                        Report
                    </Button>

                    {/* Spacer for Alignment */}
                    <Box sx={{ flexGrow: 1 }} />

                    {/* Account Icon */}
                    <IconButton color="inherit" component={Link} to="/login">
                        <AccountCircleIcon />
                    </IconButton>
                </Toolbar>
            </AppBar>

            {/* Main content area where views are rendered */}
            <main style={{ padding: '20px', overflow: 'hidden' }}>
                {/* No need for manual view rendering, handled by routing in App.js */}
                <div style={{ overflowY: 'auto', maxHeight: 'calc(100vh - 120px)' }}>
                    {/* The Routes in App.js will handle rendering */}
                </div>
            </main>
        </>
    );
};

export default Taskbar;
