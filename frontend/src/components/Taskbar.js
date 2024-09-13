import React, { useState } from 'react';
import { AppBar, Toolbar, Typography, Button, Menu, MenuItem, IconButton, Box, Grid } from '@mui/material';
import LanguageIcon from '@mui/icons-material/Language';
import CalendarTodayIcon from '@mui/icons-material/CalendarToday';
import AssessmentIcon from '@mui/icons-material/Assessment';
import PeopleIcon from '@mui/icons-material/People';
import WorkIcon from '@mui/icons-material/Work';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import LoginPage from './LoginPage'; // Import the login page
import Report from './Report'; // Import the Report component
import EventSummary from './EventSummary'; // Import EventSummary component
import './Taskbar.css'; // Import the CSS if needed

const Taskbar = () => {
    const [anchorEl, setAnchorEl] = useState(null);
    const [currentView, setCurrentView] = useState('calendar'); // Default view
    const [selectedLanguage, setSelectedLanguage] = useState('English'); // Default language

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = (language) => {
        if (language) {
            setSelectedLanguage(language);
        }
        setAnchorEl(null);
    };

    const handleNavigation = (view) => {
        setCurrentView(view);
    };

    // Modify renderView to display Report and EventSummary side by side
    const renderView = () => {
        switch (currentView) {
            case 'calendar':
                return <div>Calendar view (Placeholder)</div>; // Placeholder
            case 'report':
                return (
                    <Grid container spacing={2} sx={{ padding: '20px' }}>
                        <Grid item xs={12} md={4}> {/* Make Report smaller */}
                            <Report />
                        </Grid>
                        <Grid item xs={12} md={8}> {/* Make EventSummary larger */}
                            <EventSummary />
                        </Grid>
                    </Grid>
                );
            case 'clienti':
                return <div>Clienti view (Placeholder)</div>; // Placeholder
            case 'employee':
                return <div>Employee view (Placeholder)</div>; // Placeholder
            case 'login':
                return <LoginPage />; // Show login page when the login icon is clicked
            default:
                return <div>Calendar view (Placeholder)</div>; // Default view
        }
    };

    return (
        <>
            <AppBar position="static" sx={{ backgroundColor: '#045656' }}>
                <Toolbar>
                    <img src="/path-to-logo.png" alt="logo" style={{ width: '40px', marginRight: '10px' }} />
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                        Shift Scheduler
                    </Typography>

                    <Button
                        color="inherit"
                        startIcon={<CalendarTodayIcon />}
                        onClick={() => handleNavigation('calendar')}
                        className={currentView === 'calendar' ? 'active' : ''}  // Highlight active view
                    >
                        Calendar
                    </Button>
                    <Button
                        color="inherit"
                        startIcon={<AssessmentIcon />}
                        onClick={() => handleNavigation('report')}
                        className={currentView === 'report' ? 'active' : ''}  // Highlight active view
                    >
                        Report
                    </Button>
                    <Button
                        color="inherit"
                        startIcon={<PeopleIcon />}
                        onClick={() => handleNavigation('clienti')}
                        className={currentView === 'clienti' ? 'active' : ''}  // Highlight active view
                    >
                        Clienti
                    </Button>
                    <Button
                        color="inherit"
                        startIcon={<WorkIcon />}
                        onClick={() => handleNavigation('employee')}
                        className={currentView === 'employee' ? 'active' : ''}  // Highlight active view
                    >
                        Employee
                    </Button>

                    <Box sx={{ flexGrow: 1 }} /> {/* Adds space between buttons and right-aligned items */}

                    <IconButton color="inherit" onClick={handleClick}>
                        <LanguageIcon />
                    </IconButton>
                    <Menu
                        id="language-menu"
                        anchorEl={anchorEl}
                        open={Boolean(anchorEl)}
                        onClose={() => handleClose()}
                    >
                        <MenuItem onClick={() => handleClose('English')}>English</MenuItem>
                        <MenuItem onClick={() => handleClose('Italiano')}>Italiano</MenuItem>
                        <MenuItem onClick={() => handleClose('Español')}>Español</MenuItem>
                        <MenuItem onClick={() => handleClose('Français')}>Français</MenuItem>
                    </Menu>
                    <Typography variant="body1" color="inherit" sx={{ marginLeft: '10px' }}>
                        {selectedLanguage} {/* Display the selected language */}
                    </Typography>

                    {/* Login icon button */}
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
