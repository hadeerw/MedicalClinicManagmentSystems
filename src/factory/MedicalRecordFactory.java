/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import model.PatientHistory;
import model.Prescription;
import model.LabResult;
import model.MedicalRecord;

/**
 *
 * @author hadeerw
 */
// File: MedicalRecordFactory.java
// Pattern: Factory Pattern
public class MedicalRecordFactory {
    public static MedicalRecord createMedicalRecord(String type) {
        switch (type.toLowerCase()) {
            case "history":
                return new PatientHistory("New patient history");
            case "prescription":
                return new Prescription("New prescription");
            case "labresult":
                return new LabResult("New test", "0", "unit");
            default:
                throw new IllegalArgumentException("Invalid medical record type");
        }
    }
}

