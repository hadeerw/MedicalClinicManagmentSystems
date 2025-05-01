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
// File: LabResult.java
// Pattern: Prototype Pattern
public class LabResult implements MedicalRecord {
    private String testName;
    private String resultValue;
    private String unit;

    // Constructor
    public LabResult(String testName, String resultValue, String unit) {
        setTestName(testName);
        setResultValue(resultValue);
        setUnit(unit);
    }

    @Override
    public void display() {
        System.out.println("Lab Result:");
        System.out.println("Test: " + testName);
        System.out.println("Value: " + resultValue + " " + unit);
    }

    @Override
    public String getDetails() {
        return String.format("Test: %s, Value: %s %s", testName, resultValue, unit);
    }

    // Getters and Setters with validation
    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        if (testName == null || testName.trim().isEmpty()) {
            throw new IllegalArgumentException("Test name cannot be null or empty.");
        }
        this.testName = testName;
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        if (resultValue == null || resultValue.trim().isEmpty()) {
            throw new IllegalArgumentException("Result value cannot be null or empty.");
        }
        this.resultValue = resultValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        if (unit == null || unit.trim().isEmpty()) {
            throw new IllegalArgumentException("Unit cannot be null or empty.");
        }
        this.unit = unit;
    }
}
