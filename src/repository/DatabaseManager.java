package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Appointment;
import model.Doctor;
import model.Patient;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    private static final String DATABASE_URL = "jdbc:sqlite:clinic.db";

    private DatabaseManager() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DATABASE_URL);
            System.out.println("Connection to the database has been established.");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Connection to the database has been closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing the connection: " + e.getMessage());
        }
    }

    // Add helper methods to check existence
    private boolean doesPatientExist(String patientId) {
        String query = "SELECT COUNT(*) FROM patients WHERE patient_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, patientId);
            ResultSet rs = stmt.executeQuery();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error checking patient existence: " + e.getMessage());
            return false;
        }
    }

    private boolean doesDoctorExist(String doctorId) {
        String query = "SELECT COUNT(*) FROM doctors WHERE doctor_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error checking doctor existence: " + e.getMessage());
            return false;
        }
    }

    public void addPatientToDatabase(String name, int age, String disease) {
        // Generate patient ID: PAT + timestamp + first 3 letters of name
        String patientId = "PAT" + System.currentTimeMillis() + 
                          name.substring(0, Math.min(3, name.length())).toUpperCase();
        
        String query = "INSERT INTO patients (patient_id, name, age, disease) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, patientId);
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, age);
            preparedStatement.setString(4, disease);

            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                System.out.println("Patient added successfully with ID: " + patientId);
            }
        } catch (SQLException e) {
            System.out.println("Error adding patient: " + e.getMessage());
            throw new RuntimeException("Failed to add patient: " + e.getMessage());
        }
    }

    public String addDoctorToDatabase(String name, String specialty) {
        // Generate doctor ID based on specialty
        String specialtyPrefix;
        switch (specialty.toUpperCase()) {
            case "CARDIOLOGIST":
                specialtyPrefix = "CAR";
                break;
            case "NEUROLOGIST":
                specialtyPrefix = "NEU";
                break;
            case "GENERALPRACTITIONER":
                specialtyPrefix = "GP";
                break;
            default:
                specialtyPrefix = "DOC";
        }
        
        // Generate ID: specialty prefix + timestamp + first 3 letters of name
        String doctorId = specialtyPrefix + System.currentTimeMillis() + 
                         name.substring(0, Math.min(3, name.length())).toUpperCase();
        
        String query = "INSERT INTO doctors (doctor_id, name, specialty) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, doctorId);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, specialty);

            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                System.out.println("Doctor added successfully with ID: " + doctorId);
                return doctorId; // Return the generated ID
            }
        } catch (SQLException e) {
            System.out.println("Error adding doctor: " + e.getMessage());
            throw new RuntimeException("Failed to add doctor: " + e.getMessage());
        }
        return null;
    }

    public void addAppointmentToDatabase(String patientId, String doctorId, String date) {
        // First verify that both patient and doctor exist
        if (!doesPatientExist(patientId)) {
            throw new RuntimeException("Patient with ID " + patientId + " does not exist!");
        }
        if (!doesDoctorExist(doctorId)) {
            throw new RuntimeException("Doctor with ID " + doctorId + " does not exist!");
        }

        String query = "INSERT INTO appointments (patient_id, doctor_id, appointment_date) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, patientId);
            preparedStatement.setString(2, doctorId);
            preparedStatement.setString(3, date);

            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                System.out.println("Appointment added successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error adding appointment: " + e.getMessage());
            throw new RuntimeException("Failed to add appointment: " + e.getMessage());
        }
    }

    public void addLabResultToDatabase(String patientId, String testName, String result, String unit) {
        String query = "INSERT INTO lab_results (patient_id, test_name, result, unit) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, patientId);
            preparedStatement.setString(2, testName);
            preparedStatement.setString(3, result);
            preparedStatement.setString(4, unit);

            int resultUpdate = preparedStatement.executeUpdate();
            if (resultUpdate > 0) {
                System.out.println("Lab result added successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error adding lab result: " + e.getMessage());
        }
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT " +
                    "a.id, a.appointment_date, " +
                    "p.patient_id, p.name as patient_name, p.age as patient_age, p.disease, " +
                    "d.doctor_id, d.name as doctor_name, d.specialty " +
                    "FROM appointments a " +
                    "JOIN patients p ON a.patient_id = p.patient_id " +
                    "JOIN doctors d ON a.doctor_id = d.doctor_id";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                // Create Patient object with full information
                Patient patient = new Patient(
                    rs.getString("patient_id"),
                    rs.getString("patient_name"),
                    rs.getInt("patient_age"),
                    rs.getString("disease")
                );
                
                // Create Doctor object with full information
                Doctor doctor = new Doctor(
                    rs.getString("doctor_id"),
                    rs.getString("doctor_name"),
                    rs.getString("specialty")
                );
                
                // Create Appointment object
                Appointment appointment = new Appointment(
                    rs.getInt("id"),
                    patient,
                    doctor,
                    rs.getString("appointment_date")
                );
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving appointments: " + e.getMessage());
            e.printStackTrace();
        }
        return appointments;
    }

    public void updateAppointment(String appointmentId, String patientId, String doctorId, String date) {
        // First verify that both patient and doctor exist
        if (!doesPatientExist(patientId)) {
            throw new RuntimeException("Patient with ID " + patientId + " does not exist!");
        }
        if (!doesDoctorExist(doctorId)) {
            throw new RuntimeException("Doctor with ID " + doctorId + " does not exist!");
        }

        String sql = "UPDATE appointments SET patient_id = ?, doctor_id = ?, appointment_date = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, patientId);
            stmt.setString(2, doctorId);
            stmt.setString(3, date);
            stmt.setString(4, appointmentId);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Appointment not found with ID: " + appointmentId);
            }
            System.out.println("Appointment updated successfully with ID: " + appointmentId);
        } catch (SQLException e) {
            System.out.println("Error updating appointment: " + e.getMessage());
            throw new RuntimeException("Failed to update appointment: " + e.getMessage());
        }
    }

    public void cancelAppointment(String appointmentId) {
        String sql = "DELETE FROM appointments WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, appointmentId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Appointment not found with ID: " + appointmentId);
            }
            System.out.println("Appointment cancelled successfully with ID: " + appointmentId);
        } catch (SQLException e) {
            System.out.println("Error cancelling appointment: " + e.getMessage());
            throw new RuntimeException("Failed to cancel appointment: " + e.getMessage());
        }
    }
}


