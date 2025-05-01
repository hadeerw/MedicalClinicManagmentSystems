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

public class GeneralMedicalRecord implements MedicalRecord {
    private String description;

    public GeneralMedicalRecord(String description) {
        this.description = description;
    }

    @Override
    public void display() {
        System.out.println("General Medical Record: " + description);
    }

    @Override
    public String getDetails() {
        return description;
    }
}
