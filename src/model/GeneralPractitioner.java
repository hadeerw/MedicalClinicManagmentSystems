/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import model.Doctor;

/**
 *
 * @author hadeerw
 */

public class GeneralPractitioner extends Doctor {

    private int id;
    
    public GeneralPractitioner(String name) {
        super(name, "General Practitioner"); 
    }

    
    public GeneralPractitioner(int id, String name) {
        super(name, "General Practitioner"); 
        this.id = id; 
    }

    @Override
    public void diagnose() {
        System.out.println("Diagnosing general health issues.");
    }

    public int getId() {
        if (id == 0) {
            throw new IllegalStateException("ID not set for this doctor.");
        }
        return id;
    }

    public void performGeneralCheckup() {
        System.out.println("Performing general checkup...");
    }
}


