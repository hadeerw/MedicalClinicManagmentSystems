/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

/**
 *
 * @author hadeerw
 */
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseSetup {

    public static void initializeDatabase() {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        if (conn != null) {
            createTables(conn);
        }
    }
    
    private static void createTables(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            // Create Patients table
            String createPatientsTable = "CREATE TABLE IF NOT EXISTS patients ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "patient_id TEXT UNIQUE, "
                + "name TEXT NOT NULL, "
                + "age INTEGER NOT NULL, "
                + "disease TEXT"
                + ")";
            stmt.execute(createPatientsTable);
            
            // Create Doctors table
            String createDoctorsTable = "CREATE TABLE IF NOT EXISTS doctors ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "doctor_id TEXT UNIQUE, "
                + "name TEXT NOT NULL, "
                + "specialty TEXT NOT NULL"
                + ")";
            stmt.execute(createDoctorsTable);
            
            // Create Appointments table
            String createAppointmentsTable = "CREATE TABLE IF NOT EXISTS appointments ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "patient_id TEXT NOT NULL, "
                + "doctor_id TEXT NOT NULL, "
                + "appointment_date TEXT NOT NULL, "
                + "status TEXT DEFAULT 'SCHEDULED', "
                + "FOREIGN KEY (patient_id) REFERENCES patients(patient_id), "
                + "FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id)"
                + ")";
            stmt.execute(createAppointmentsTable);
            
            // Create Lab Results table
            String createLabResultsTable = "CREATE TABLE IF NOT EXISTS lab_results ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "patient_id TEXT NOT NULL, "
                + "test_name TEXT NOT NULL, "
                + "result_value TEXT NOT NULL, "
                + "unit TEXT NOT NULL, "
                + "test_date TEXT DEFAULT CURRENT_TIMESTAMP, "
                + "FOREIGN KEY (patient_id) REFERENCES patients(patient_id)"
                + ")";
            stmt.execute(createLabResultsTable);
            
            System.out.println("All database tables have been created successfully!");
            
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        initializeDatabase();
    }
}
