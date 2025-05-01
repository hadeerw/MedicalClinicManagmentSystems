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
// File: PatientHistory.java
// Pattern: Prototype Pattern
public class PatientHistory implements MedicalRecord {
    private String historyDetails;

    public PatientHistory() {
        this.historyDetails = "Default patient history details.";
    }

    public PatientHistory(String historyDetails) {
        this.historyDetails = historyDetails;
    }

    @Override
    public void display() {
        System.out.println("Patient History Record: " + historyDetails);
    }

    @Override
    public MedicalRecord clone() {
        return new PatientHistory(this.historyDetails);
    }

    @Override
    public String getDetails() {
        return historyDetails;
    }
}

