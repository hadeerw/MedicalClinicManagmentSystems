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
public class EmergencyMedicalRecord implements MedicalRecord {
    private String details;

    public EmergencyMedicalRecord(String details) {
        this.details = details;
    }

    @Override
    public void display() {
        System.out.println("Emergency Medical Record: " + details);
    }

    @Override
    public MedicalRecord clone() {
        return new EmergencyMedicalRecord(this.details);
    }

    @Override
    public String getDetails() {
        return details;
    }
}
