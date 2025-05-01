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

public class Doctor {
    private String name;
    private String specialty;
    private String doctorId;

    // Constructor for new doctors (without ID)
    public Doctor(String name, String specialty) {
        this.name = name;
        this.specialty = specialty;
    }

    // Constructor for existing doctors (with ID)
    public Doctor(String doctorId, String name, String specialty) {
        this.doctorId = doctorId;
        this.name = name;
        this.specialty = specialty;
    }

    public String getName() {
        return name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        if (doctorId == null || doctorId.trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor ID cannot be null or empty");
        }
        this.doctorId = doctorId;
    }

    public void diagnose() {
        System.out.println("Diagnosing...");
    }

    public void displayInfo() {
        System.out.println("Doctor ID: " + (doctorId != null ? doctorId : "Not assigned"));
        System.out.println("Name: " + name);
        System.out.println("Specialty: " + specialty);
    }
}
