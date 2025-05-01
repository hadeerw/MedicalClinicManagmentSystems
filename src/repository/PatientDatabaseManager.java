/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import model.Patient;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author hadeerw
 */
// Pattern: Singleton Pattern

public class PatientDatabaseManager {
    private static PatientDatabaseManager instance;
    private Connection connection;

    private PatientDatabaseManager() {
        connection = DatabaseConnection.getInstance().getConnection();
        if (connection == null) {
            throw new RuntimeException("Failed to establish database connection");
        }
    }

    public static synchronized PatientDatabaseManager getInstance() {
        if (instance == null) {
            instance = new PatientDatabaseManager();
        }
        return instance;
    }

    public void addPatient(Patient patient) {
        String sql = "INSERT INTO patients (patient_id, name, age, disease) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, patient.getPatientId());
            stmt.setString(2, patient.getName());
            stmt.setInt(3, patient.getAge());
            stmt.setString(4, patient.getDisease());
            stmt.executeUpdate();
            System.out.println("Patient added successfully: " + patient.getName());
        } catch (SQLException e) {
            System.out.println("Error adding patient: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to add patient", e);
        }
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Patient patient = new Patient(
                    rs.getString("patient_id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("disease")
                );
                patients.add(patient);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving patients: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve patients", e);
        }
        return patients;
    }

    public Patient getPatientByName(String name) {
        String sql = "SELECT * FROM patients WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Patient(
                    rs.getString("patient_id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("disease")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving patient: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve patient", e);
        }
        return null;
    }

    public boolean deletePatient(String patientId) {
        String sql = "DELETE FROM patients WHERE patient_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, patientId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting patient: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to delete patient", e);
        }
    }

    public void updatePatient(String patientId, String name, int age, String disease) {
        String sql = "UPDATE patients SET name = ?, age = ?, disease = ? WHERE patient_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, disease);
            stmt.setString(4, patientId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Patient not found with ID: " + patientId);
            }
            System.out.println("Patient updated successfully: " + name);
        } catch (SQLException e) {
            System.out.println("Error updating patient: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to update patient", e);
        }
    }
}

