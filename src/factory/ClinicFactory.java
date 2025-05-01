/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import model.Doctor;
import model.MedicalRecord;

public class ClinicFactory implements AbstractFactory {
    @Override
    public Doctor createDoctor(String specialty, String name) {
        return DoctorFactory.createDoctor(specialty, name);
    }

    @Override
    public MedicalRecord createMedicalRecord(String type) {
        return MedicalRecordFactory.createMedicalRecord(type);
    }
}
