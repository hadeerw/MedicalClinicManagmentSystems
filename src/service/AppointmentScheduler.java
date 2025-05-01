package service;

import model.Appointment;
import model.Patient;
import model.Doctor;
import java.sql.*;
import java.util.*;

public class AppointmentScheduler {
    private static AppointmentScheduler instance;
    private List<Appointment> appointments;

    private AppointmentScheduler() {
        appointments = new ArrayList<>();
    }

    public static synchronized AppointmentScheduler getInstance() {
        if (instance == null) {
            instance = new AppointmentScheduler();
        }
        return instance;
    }

    public boolean scheduleAppointment(Patient patient, Doctor doctor, String timeSlot) {
        System.out.println("Checking time slot: " + timeSlot);
        for (Appointment appointment : appointments) {
            System.out.println("Existing appointment time: " + appointment.getTimeSlot());
            if (appointment.getTimeSlot().equals(timeSlot)) {
                System.out.println("Time slot is already taken.");
                return false;
            }
        }
        
        appointments.add(new Appointment(patient, doctor, timeSlot));
        System.out.println("Appointment scheduled at " + timeSlot);
        return true;
    }
}

