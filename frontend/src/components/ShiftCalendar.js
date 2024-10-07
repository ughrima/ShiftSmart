import React, { useEffect, useState } from 'react';
import { Calendar, momentLocalizer } from 'react-big-calendar';
import { DndProvider } from 'react-dnd';
import { HTML5Backend } from 'react-dnd-html5-backend';
import moment from 'moment';
import axios from 'axios';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import 'react-calendar/dist/Calendar.css'; // Import Calendar CSS
import CalendarComponent from 'react-calendar'; // Import the mini calendar component
import './ShiftCalendar.css'; // Import custom CSS for layout and colors

const localizer = momentLocalizer(moment);

const ShiftCalendar = () => {
    const [events, setEvents] = useState([]);
    const [date, setDate] = useState(new Date()); // Add state to manage date

    useEffect(() => {
        const fetchShifts = async () => {
            try {
                const response = await axios.get('/api/get-shifts');
                const eventsData = response.data.map(shift => ({
                    title: `${shift.employeeName}: ${shift.company}`,
                    start: new Date(shift.startTime),
                    end: new Date(shift.endTime),
                    id: shift.id,
                    company: shift.company
                }));
                setEvents(eventsData);
            } catch (err) {
                console.error('Failed to fetch shifts:', err);
            }
        };
        fetchShifts();
    }, []);

    const handleEventDrop = ({ event, start, end }) => {
        const updatedEvent = { ...event, start, end };

        // Update the event locally
        const updatedEvents = events.map(ev => (ev.id === event.id ? updatedEvent : ev));
        setEvents(updatedEvents);

        // Optionally, update the event in the backend
        axios.post('/api/update-shift', { id: event.id, start, end })
            .then(() => {
                console.log('Shift updated successfully');
            })
            .catch(err => {
                console.error('Error updating shift:', err);
            });
    };

    const getEventColor = (company) => {
        switch (company) {
            case 'Company 1':
                return 'orange';
            case 'Company 2':
                return 'green';
            case 'PERMIT':
                return 'red';
            default:
                return 'blue';
        }
    };

    return (
        <DndProvider backend={HTML5Backend}>
            <div className="shift-scheduler-container">
                {/* Left Sidebar with Calendar and Employee List */}
                <aside className="sidebar">
                    <div className="mini-calendar">
                        <h3>{moment(date).format('MMMM YYYY')}</h3> {/* Display the selected month and year */}
                        <CalendarComponent 
                            onChange={setDate} 
                            value={date} 
                            // Other props can be added for customization
                        />
                    </div>
                    <div className="employee-list">
                        <h4>Calendars</h4>
                        <ul>
                            <li><input type="checkbox" checked /> Sandra Bullock</li>
                            <li><input type="checkbox" checked /> Tom Hanks</li>
                            <li><input type="checkbox" checked /> Morgan Freeman</li>
                            <li><input type="checkbox" checked /> George Clooney</li>
                            <li><input type="checkbox" checked /> Chris Evans</li>
                        </ul>
                    </div>
                </aside>

                {/* Main Content Area with Shift Calendar */}
                <main className="main-content">
                    <h2>Shift Scheduler</h2> {/* You can keep the title here if needed */}

                    <div className="calendar-container">
                        <Calendar
                            localizer={localizer}
                            events={events}
                            onEventDrop={handleEventDrop}
                            startAccessor="start"
                            endAccessor="end"
                            style={{ height: 600 }}
                            eventPropGetter={(event) => ({
                                style: { backgroundColor: getEventColor(event.company) }
                            })}
                        />
                    </div>
                </main>

                {/* Floating Action Button */}
                <div className="floating-button">
                    <button>+</button>
                </div>
            </div>
        </DndProvider>
    );
};

export default ShiftCalendar;
