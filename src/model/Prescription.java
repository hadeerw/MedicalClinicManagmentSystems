/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import model.MedicalRecord;

/**
 *
 * @author hadeerw
 */
// File: Prescription.java
// Pattern: Prototype Pattern
public class Prescription implements MedicalRecord {
    private String prescriptionDetails;

    public Prescription() {
        this.prescriptionDetails = "Default prescription details.";
    }

    public Prescription(String prescriptionDetails) {
        this.prescriptionDetails = prescriptionDetails;
    }

    @Override
    public void display() {
        System.out.println("Prescription Record: " + prescriptionDetails);
    }

    @Override
    public String getDetails() {
        return prescriptionDetails;
    }
}
