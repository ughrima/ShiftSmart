import React, { useEffect, useState } from 'react';
import { Calendar, momentLocalizer } from 'react-big-calendar';
import { DndProvider } from 'react-dnd';
import { HTML5Backend } from 'react-dnd-html5-backend';
import moment from 'moment';
import axios from 'axios';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import './ShiftCalendar.css';


const localizer = momentLocalizer(moment);

const ShiftCalendar = () => {
    const [events, setEvents] = useState([]);

    useEffect(() => {
        const fetchShifts = async () => {
            try {
                const response = await axios.get('/api/get-shifts');
                const eventsData = response.data.map(shift => ({
                    title: `${shift.employeeName}: ${shift.company}`,
                    start: new Date(shift.startTime),
                    end: new Date(shift.endTime),
                    id: shift.id
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

    return (
        <DndProvider backend={HTML5Backend}>
            <div>
                <h2>Shift Calendar</h2>
                <Calendar
                    localizer={localizer}
                    events={events}
                    onEventDrop={handleEventDrop}
                    startAccessor="start"
                    endAccessor="end"
                    style={{ height: 600 }}
                    eventPropGetter={(event) => {
                        const backgroundColor = event.conflict ? 'red' : 'blue';
                        return { style: { backgroundColor } };
                    }}
                />
            </div>
        </DndProvider>
    );
};

export default ShiftCalendar;
