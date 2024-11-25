import React from "react";
import { Link } from "react-router-dom";
import { AppBar, Toolbar, Box, Button, Typography, Grid, Divider } from "@mui/material";
import { Schedule, BarChart, Group, Monitor, AccessTime, TrendingUp, Phone, Email, LocationOn } from "@mui/icons-material";
import "./LandingPage.css";

const LandingPage = () => {
    return (
        <div className="landing-page">
            {/* Navbar */}
            <AppBar position="static" elevation={0} style={{ backgroundColor: "#fff", color: "#333" }}>
                <Toolbar style={{ display: "flex", justifyContent: "space-between", padding: "0 20px" }}>
                    <Typography variant="h6" style={{ fontWeight: "bold", color: "#045656" }}>
                        Workforce Shift Scheduling
                    </Typography>
                    <Box style={{ display: "flex", gap: "20px" }}>
                        <a href="#features" className="nav-link">Features</a>
                        <a href="#about" className="nav-link">About Us</a>
                        <a href="#contact" className="nav-link">Contact Us</a>
                    </Box>
                    <Box style={{ display: "flex", gap: "15px" }}>
                        <Link to="/login" style={{ textDecoration: "none" }}>
                            <Button variant="outlined" style={{ borderColor: "#045656", color: "#000000" }}>
                                Sign In
                            </Button>
                        </Link>
                        <Link to="/signup" style={{ textDecoration: "none" }}>
                            <Button variant="contained" style={{ backgroundColor: "#045656", color: "#ffffff" }}>
                                Sign Up Free
                            </Button>
                        </Link>
                    </Box>
                </Toolbar>
            </AppBar>

            {/* Main Heading Section */}
            <header
                style={{
                    textAlign: "center",
                    padding: "50px 20px",
                    backgroundColor: "#045656FF",
                    color: "#fff",
                }}
            >
                <Typography variant="h3" style={{ fontWeight: "bold", marginBottom: "20px" }}>
                    Bring Out the Best in Your Team.
                </Typography>
                <Typography variant="h6" style={{ marginBottom: "30px" }}>
                    Simplify workforce management and boost productivity with our cutting-edge scheduling platform.
                </Typography>
                <Link to="/signup" style={{ textDecoration: "none" }}>
                    <Button
                        variant="contained"
                        size="large"
                        style={{ backgroundColor: "#333", color: "#fff", borderRadius: "30px" }}
                    >
                        Get Started
                    </Button>
                </Link>
            </header>

            {/* Features Section */}
            <section id="features" style={{ padding: "60px 20px" }}>
                <Typography
                    variant="h4"
                    style={{
                        textAlign: "center",
                        fontWeight: "bold",
                        marginBottom: "40px",
                        color: "#333",
                    }}
                >
                    Our Features
                </Typography>
                <Grid container spacing={4} justifyContent="center">
                    {/* Individual Feature Cards */}
                    {[
                        {
                            icon: <Schedule style={{ fontSize: 60, color: "#045656" }} />,
                            title: "Automated Scheduling",
                            description: "Save time and reduce errors by automating shift schedules for your team.",
                        },
                        {
                            icon: <BarChart style={{ fontSize: 60, color: "#045656" }} />,
                            title: "Detailed Reports",
                            description: "Access insights on performance, shift history, and productivity at a glance.",
                        },
                        {
                            icon: <Group style={{ fontSize: 60, color: "#045656" }} />,
                            title: "Easy Collaboration",
                            description: "Effortlessly share schedules and updates, improving team communication.",
                        },
                        {
                            icon: <Monitor style={{ fontSize: 60, color: "#045656" }} />,
                            title: "Employee Monitoring",
                            description: "Track workforce activities to ensure maximum efficiency and accountability.",
                        },
                        {
                            icon: <AccessTime style={{ fontSize: 60, color: "#045656" }} />,
                            title: "Time and Attendance",
                            description: "Keep accurate records of employee attendance and manage time logs effortlessly.",
                        },
                        {
                            icon: <TrendingUp style={{ fontSize: 60, color: "#045656" }} />,
                            title: "Productivity Analytics",
                            description: "Measure employee productivity with data-driven insights and analytics.",
                        },
                    ].map((feature, index) => (
                        <Grid item xs={12} sm={6} md={4} key={index}>
                            <Box
                                style={{
                                    borderRadius: "12px",
                                    padding: "30px",
                                    backgroundColor: "#F9FFF1",
                                    textAlign: "center",
                                    boxShadow: "0 6px 12px rgba(0, 0, 0, 0.1)",
                                }}
                            >
                                {feature.icon}
                                <Typography variant="h6" style={{ fontWeight: "bold", margin: "20px 0" }}>
                                    {feature.title}
                                </Typography>
                                <Typography style={{ color: "#555" }}>{feature.description}</Typography>
                            </Box>
                        </Grid>
                    ))}
                </Grid>
            </section>

            <Divider />

            {/* About Us Section */}
            <section id="about" style={{ padding: "60px 20px", backgroundColor: "#FAFAFA" }}>
                <Typography
                    variant="h4"
                    style={{
                        textAlign: "center",
                        fontWeight: "bold",
                        marginBottom: "30px",
                        color: "#333",
                    }}
                >
                    About Us
                </Typography>
                <Typography
                    style={{
                        maxWidth: "800px",
                        margin: "0 auto",
                        textAlign: "center",
                        color: "#555",
                        fontSize: "18px",
                    }}
                >
                    Workforce Shift Scheduling helps organizations of all sizes streamline their team management
                    processes. From simplifying shift planning to enhancing productivity, we are committed to
                    helping you succeed.
                </Typography>
            </section>

            {/* Contact Us Section */}
            <section id="contact" style={{ padding: "60px 20px" }}>
                <Typography
                    variant="h4"
                    style={{
                        textAlign: "center",
                        fontWeight: "bold",
                        marginBottom: "30px",
                        color: "#333",
                    }}
                >
                    Contact Us
                </Typography>
                <Grid container spacing={4} justifyContent="center">
                    {/* Contact Info Cards */}
                    {[
                        {
                            icon: <Phone style={{ fontSize: 40, color: "#045656" }} />,
                            title: "Phone",
                            content: "+91-123-456-7890",
                        },
                        {
                            icon: <Email style={{ fontSize: 40, color: "#045656" }} />,
                            title: "Email",
                            content: "support@shiftmanager.com",
                        },
                        {
                            icon: <LocationOn style={{ fontSize: 40, color: "#045656" }} />,
                            title: "Address",
                            content: "123 Workforce Blvd, City, Country",
                        },
                    ].map((contact, index) => (
                        <Grid item xs={12} sm={6} md={4} key={index}>
                            <Box
                                style={{
                                    borderRadius: "12px",
                                    padding: "20px",
                                    backgroundColor: "#F9FFF1",
                                    textAlign: "center",
                                    boxShadow: "0 6px 12px rgba(0, 0, 0, 0.1)",
                                }}
                            >
                                {contact.icon}
                                <Typography variant="h6" style={{ fontWeight: "bold", margin: "20px 0" }}>
                                    {contact.title}
                                </Typography>
                                <Typography style={{ color: "#555" }}>{contact.content}</Typography>
                            </Box>
                        </Grid>
                    ))}
                </Grid>
            </section>

            {/* Footer */}
            <footer style={{ backgroundColor: "#333", color: "#fff", padding: "20px 0", textAlign: "center" }}>
                <Typography variant="body2">
                    © 2024 Workforce Shift Scheduling. All rights reserved.
                </Typography>
            </footer>
        </div>
    );
};

export default LandingPage;
