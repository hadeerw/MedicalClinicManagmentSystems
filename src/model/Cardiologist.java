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

public class Cardiologist extends Doctor {

    private int id;

    
    public Cardiologist(String name) {
        super(name, "Cardiologist");
    }

    
    public Cardiologist(int id, String name) {
        super(name, "Cardiologist");
        this.id = id;
    }

    
    public int getId() {
        return id;
    }

    @Override
    public void diagnose() {
        System.out.println("Diagnosing heart-related issues.");
    }
}


