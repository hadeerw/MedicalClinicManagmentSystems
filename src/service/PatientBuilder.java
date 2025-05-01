/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import model.Patient;

/**
 *
 * @author hadeerw
 */
// File: PatientBuilder.java
// Pattern: Builder Pattern
public class PatientBuilder {
    private String name;
    private int age;
    private String disease;
    private String patientId;

    // setter methods
    public PatientBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PatientBuilder setAge(int age) {
        this.age = age;
        return this;
    }

    public PatientBuilder setDisease(String disease) {
        this.disease = disease;
        return this;
    }

    public PatientBuilder setPatientId(String patientId) {
        this.patientId = patientId;
        return this;
    }

    public Patient build() {
        if (patientId != null && !patientId.isEmpty()) {
            return new Patient(patientId, name, age, disease);
        }
        return new Patient(name, age, disease);
    }
}

