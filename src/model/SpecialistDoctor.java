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
public class SpecialistDoctor extends Doctor {
    
    public SpecialistDoctor(String name, String specialty) {
        super(name, specialty);  // يجب التأكد أن constructor Doctor يقبل هذين المعاملين
    }

    @Override
    public void diagnose() {
        // هنا يمكنك إضافة كيفية تشخيص الطبيب المتخصص
        System.out.println("Diagnosing as a specialist doctor...");
    }
}