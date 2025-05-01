/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author hadeerw
 */
// File: Appointment.java
// Represents an Appointment
public class Appointment {
    private int id;
    private Patient patient;
    private Doctor doctor;
    private String timeSlot;
    private String status;

    // Constructor with all fields
    public Appointment(int id, Patient patient, Doctor doctor, String timeSlot) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.timeSlot = timeSlot;
        this.status = "Scheduled";
    }

    // Constructor without id (for new appointments)
    public Appointment(Patient patient, Doctor doctor, String timeSlot) {
        this.patient = patient;
        this.doctor = doctor;
        this.timeSlot = timeSlot;
        this.status = "Scheduled";
    }

    // Getter for ID
    public int getId() {
        return id;
    }

    // Other getters
    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public String getStatus() {
        return status;
    }

    public Object get(int index) {
        switch(index) {
            case 0: return id;
            case 1: return patient;
            case 2: return doctor;
            case 3: return timeSlot;
            case 4: return status;
            default: throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
    }

    public boolean isEmpty() {
        return patient == null || doctor == null || timeSlot == null;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return timeSlot;
    }
}

