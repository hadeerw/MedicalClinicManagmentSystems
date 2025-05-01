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
// File: MedicalRecord.java
// Pattern: Prototype Pattern
public interface MedicalRecord {
    void display();
    MedicalRecord clone();

    String getDetails();
}
