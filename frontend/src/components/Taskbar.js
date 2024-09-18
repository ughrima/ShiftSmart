import React, { useState } from 'react';
import { AppBar, Toolbar, Typography, Button, Box, Grid, IconButton } from '@mui/material';
import CalendarTodayIcon from '@mui/icons-material/CalendarToday';
import AssessmentIcon from '@mui/icons-material/Assessment';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import LoginPage from './LoginPage';
import Report from './Report';
import EventSummary from './EventSummary';
import ShiftCalendar from './ShiftCalendar'; // Import ShiftCalendar
import './Taskbar.css';

const Taskbar = () => {
    const [currentView, setCurrentView] = useState('calendar'); // Default view

    const handleNavigation = (view) => {
        setCurrentView(view);
    };

    const renderView = () => {
        switch (currentView) {
            case 'calendar':
                return <ShiftCalendar />; // Render ShiftCalendar component
            case 'report':
                return (
                    <Grid container spacing={2} sx={{ padding: '20px' }}>
                        <Grid item xs={12} md={4}>
                            <Report />
                        </Grid>
                        <Grid item xs={12} md={8}>
                            <EventSummary />
                        </Grid>
                    </Grid>
                );
            case 'login':
                return <LoginPage />;
            default:
                return <ShiftCalendar />; // Default view
        }
    };

    return (
        <>
            <AppBar position="static" sx={{ backgroundColor: '#045656' }}>
                <Toolbar>
                    <img src="/path-to-logo.png" alt="logo" style={{ width: '40px', marginRight: '10px' }} />
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                    ShiftSmart
                    </Typography>

                    <Button
                        color="inherit"
                        startIcon={<CalendarTodayIcon />}
                        onClick={() => handleNavigation('calendar')}
                        className={currentView === 'calendar' ? 'active' : ''}
                    >
                        Calendar
                    </Button>

                    <Button
                        color="inherit"
                        startIcon={<AssessmentIcon />}
                        onClick={() => handleNavigation('report')}
                        className={currentView === 'report' ? 'active' : ''}
                    >
                        Report
                    </Button>

                    <Box sx={{ flexGrow: 1 }} />

                    <IconButton color="inherit" onClick={() => handleNavigation('login')}>
                        <AccountCircleIcon />
                    </IconButton>
                </Toolbar>
            </AppBar>
            <main>
                {renderView()}
            </main>
        </>
    );
};

export default Taskbar;
