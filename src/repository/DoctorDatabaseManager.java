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
import model.Doctor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Assuming Doctor is an abstract class or you have specific Doctor subclasses like Cardiologist, etc.
public class DoctorDatabaseManager {
    private static DoctorDatabaseManager instance;
    private Connection connection;

    // منع إنشاء نسخ متعددة
    private DoctorDatabaseManager() {
        connection = DatabaseManager.getInstance().getConnection();
    }

    // استخدام طريقة Singleton للحصول على الاتصال
    public static synchronized DoctorDatabaseManager getInstance() {
        if (instance == null) {
            instance = new DoctorDatabaseManager();
        }
        return instance;
    }

    // إضافة طبيب إلى قاعدة البيانات
    public void addDoctor(Doctor doctor) {
        String sql = "INSERT INTO doctors (doctor_id, name, specialty) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, doctor.getDoctorId());
            stmt.setString(2, doctor.getName());
            stmt.setString(3, doctor.getSpecialty());
            stmt.executeUpdate();
            System.out.println("Doctor added successfully with ID: " + doctor.getDoctorId());
        } catch (SQLException e) {
            System.out.println("Error adding doctor: " + e.getMessage());
            throw new RuntimeException("Failed to add doctor: " + e.getMessage());
        }
    }

    // الحصول على قائمة الأطباء
    public List<Doctor> getDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); 
             ResultSet rs = stmt.executeQuery("SELECT doctor_id, name, specialty FROM doctors")) {
            while (rs.next()) {
                Doctor doctor = new Doctor(
                    rs.getString("doctor_id"),
                    rs.getString("name"),
                    rs.getString("specialty")
                );
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving doctors: " + e.getMessage());
            e.printStackTrace();
        }
        return doctors;
    }

    // الحصول على طبيب بواسطة الاسم
    public Doctor getDoctorByName(String name) {
        String sql = "SELECT doctor_id, name, specialty FROM doctors WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Doctor(
                    rs.getString("doctor_id"),
                    rs.getString("name"),
                    rs.getString("specialty")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving doctor: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // حذف طبيب من قاعدة البيانات
    public boolean deleteDoctor(String doctorId) {
        String sql = "DELETE FROM doctors WHERE doctor_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, doctorId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Doctor deleted successfully with ID: " + doctorId);
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Error deleting doctor: " + e.getMessage());
            throw new RuntimeException("Failed to delete doctor: " + e.getMessage());
        }
    }

    // تحديث بيانات طبيب
    public void updateDoctor(String doctorId, String name, String specialty) {
        String sql = "UPDATE doctors SET name = ?, specialty = ? WHERE doctor_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, specialty);
            stmt.setString(3, doctorId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Doctor not found with ID: " + doctorId);
            }
            System.out.println("Doctor updated successfully with ID: " + doctorId);
        } catch (SQLException e) {
            System.out.println("Error updating doctor: " + e.getMessage());
            throw new RuntimeException("Failed to update doctor: " + e.getMessage());
        }
    }
}
