/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

/**
 *
 * @author hadeerw
 */
import model.Appointment;
import model.Patient;
import model.SpecialistDoctor;
import model.Doctor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDatabaseManager {
    private static AppointmentDatabaseManager instance;
    private Connection connection;

    // Constructor to initialize database connection
    private AppointmentDatabaseManager() {
        try {
            String url = "jdbc:sqlite:clinic.db";
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Singleton pattern to get a single instance of the database manager
    public static synchronized AppointmentDatabaseManager getInstance() {
        if (instance == null) {
            instance = new AppointmentDatabaseManager();
        }
        return instance;
    }

    // Method to get a list of all appointments
    public List<Appointment> getAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT " +
                      "a.id, a.appointment_date, " +
                      "p.patient_id, p.name as patient_name, p.age as patient_age, p.disease, " +
                      "d.doctor_id, d.name as doctor_name, d.specialty " +
                      "FROM appointments a " +
                      "JOIN patients p ON a.patient_id = p.patient_id " +
                      "JOIN doctors d ON a.doctor_id = d.doctor_id";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                Patient patient = new Patient(
                    rs.getString("patient_id"),
                    rs.getString("patient_name"),
                    rs.getInt("patient_age"),
                    rs.getString("disease")
                );

                Doctor doctor = new Doctor(
                    rs.getString("doctor_id"),
                    rs.getString("doctor_name"),
                    rs.getString("specialty")
                );

                Appointment appointment = new Appointment(
                    rs.getInt("id"),
                    patient,
                    doctor,
                    rs.getString("appointment_date")
                );
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    // Method to add a new appointment to the database
    public void addAppointment(Appointment appointment) {
        String query = "INSERT INTO appointments (patient_id, doctor_id, appointment_date) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, appointment.getPatient().getPatientId());
            stmt.setString(2, appointment.getDoctor().getDoctorId());
            stmt.setString(3, appointment.getTimeSlot());
            stmt.executeUpdate();
            System.out.println("Appointment added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding appointment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to delete an appointment from the database
    public void deleteAppointment(int appointmentId) {
        String query = "DELETE FROM appointments WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, appointmentId);
            stmt.executeUpdate();
            System.out.println("Appointment deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting appointment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    void scheduleAppointment(Appointment appointment) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}


