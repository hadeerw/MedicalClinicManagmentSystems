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

public class Neurologist extends Doctor {

    private int id;
    
    public Neurologist(String name) {
        super(name, "Neurologist"); 
    }

    
    public Neurologist(int id, String name) {
        super(name, "Neurologist"); 
        this.id = id; 
    }

    @Override
    public void diagnose() {
        System.out.println("Diagnosing brain-related issues.");
    }

    public void conductBrainScan() {
        System.out.println("Conducting brain scan...");
    }

    public int getId() {
        return id;
    }
}

