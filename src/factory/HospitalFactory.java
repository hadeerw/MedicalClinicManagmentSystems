/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import model.Doctor;
import model.MedicalRecord;
import model.PatientHistory;
import model.Prescription;
import model.EmergencyMedicalRecord;
import model.GeneralMedicalRecord;
import model.Cardiologist;
import model.Neurologist;
import model.GeneralPractitioner;

/**
 *
 * @author hadeerw
 */
public class HospitalFactory implements AbstractFactory {
    @Override
    public Doctor createDoctor(String specialty, String name) {
        switch (specialty.toLowerCase()) {
            case "cardiologist":
                return new Cardiologist(name);
            case "neurologist":
                return new Neurologist(name);
            case "generalpractitioner":
                return new GeneralPractitioner(name);
            default:
                throw new IllegalArgumentException("Invalid doctor specialization: " + specialty);
        }
    }

    @Override
    public MedicalRecord createMedicalRecord(String type) {
        switch (type.toLowerCase()) {
            case "emergency":
                return new EmergencyMedicalRecord("Emergency Case Details");
            case "general":
                return new GeneralMedicalRecord("General Checkup Details");
            case "history":
                return new PatientHistory("Patient History Details");
            case "prescription":
                return new Prescription("New Prescription");
            default:
                throw new IllegalArgumentException("Unknown medical record type: " + type);
        }
    }
}



