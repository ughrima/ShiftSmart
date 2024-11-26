import React, { useState } from "react";
import axios from "axios";
import "./SignUpPage.css";

const SignUpPage = () => {
  const [formData, setFormData] = useState({
    companyName: "",
    email: "",
    password: "",
    workdaysPerWeek: "1",
    maxWorkHours: "",
    allowNightShifts: false,
    flexibleWorkHours: false,
    employeeCSV: null,
  });

  const [errors, setErrors] = useState({});
  const [errorMessage, setErrorMessage] = useState("");

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData({
      ...formData,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  /*const handleFileChange = (e) => {
    const file = e.target.files[0];
    setFormData({ ...formData, employeeCSV: file });
  };*/

  const validateForm = () => {
    const formErrors = {};
    if (!formData.companyName) formErrors.companyName = "Company Name is required.";
    if (!formData.email) formErrors.email = "Email is required.";
    if (!formData.password) formErrors.password = "Password is required.";
    if (!formData.maxWorkHours) formErrors.maxWorkHours = "Maximum work hours is required.";
    setErrors(formErrors);
    return Object.keys(formErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (validateForm()) {
      const formDataToSend = new FormData();
      Object.entries(formData).forEach(([key, value]) => {
        formDataToSend.append(key, value);
      });

      try {
        const response = await axios.post("/auth/register", formDataToSend, {
          headers: { "Content-Type": "multipart/form-data" },
        });
        alert("Registration successful!");
      } catch (err) {
        setErrorMessage(err.response?.data?.message || "An error occurred.");
      }
    }
  };

  return (
    <div className="signup-page">
      <div className="signup-left">
        <h1>ShiftSmart</h1>
      </div>
      <div className="signup-right">
        <div className="signup-container">
          <h2 className="signup-header">Sign Up</h2>
          {errorMessage && <div className="error-message">{errorMessage}</div>}
          <form onSubmit={handleSubmit}>
            <div className="signup-textfield">
              <label htmlFor="companyName">Company Name</label>
              <input
                type="text"
                id="companyName"
                name="companyName"
                value={formData.companyName}
                onChange={handleInputChange}
              />
              {errors.companyName && <span className="error-message">{errors.companyName}</span>}
            </div>
            <div className="signup-textfield">
              <label htmlFor="email">Email</label>
              <input
                type="email"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleInputChange}
              />
              {errors.email && <span className="error-message">{errors.email}</span>}
            </div>
            <div className="signup-textfield">
              <label htmlFor="password">Password</label>
              <input
                type="password"
                id="password"
                name="password"
                value={formData.password}
                onChange={handleInputChange}
              />
              {errors.password && <span className="error-message">{errors.password}</span>}
            </div>
            <div className="signup-textfield">
              <label htmlFor="workdaysPerWeek">Workdays per week</label>
              <select
                id="workdaysPerWeek"
                name="workdaysPerWeek"
                value={formData.workdaysPerWeek}
                onChange={handleInputChange}
              >
                {[...Array(7)].map((_, i) => (
                  <option key={i + 1} value={i + 1}>
                    {i + 1}
                  </option>
                ))}
              </select>
            </div>
            <div className="signup-textfield">
              <label htmlFor="maxWorkHours">Max Work Hours per Day</label>
              <input
                type="number"
                id="maxWorkHours"
                name="maxWorkHours"
                value={formData.maxWorkHours}
                onChange={handleInputChange}
              />
              {errors.maxWorkHours && <span className="error-message">{errors.maxWorkHours}</span>}
            </div>
            <div className="signup-checkbox">
              <input
                type="checkbox"
                name="allowNightShifts"
                checked={formData.allowNightShifts}
                onChange={handleInputChange}
              />
              <label>Allow Night Shifts</label>
            </div>
            <div className="signup-checkbox">
              <input
                type="checkbox"
                name="flexibleWorkHours"
                checked={formData.flexibleWorkHours}
                onChange={handleInputChange}
              />
              <label>Flexible Work Hours</label>
            </div>
            <div className="signup-button">
              <button type="submit">Register</button>
            </div>
          </form>
          <div className="signup-redirect">
            Already have an account? <a href="/login">Log in</a>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SignUpPage;
