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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertData {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:medical.db";

        try (Connection conn = DriverManager.getConnection(url)) {

            // إدخال مريض جديد
            String insertPatient = "INSERT INTO patients (name, age) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertPatient)) {
                pstmt.setString(1, "Ali");
                pstmt.setInt(2, 25);
                pstmt.executeUpdate();
                System.out.println("Patient added successfully.");
            }

            // إدخال طبيب جديد
            String insertDoctor = "INSERT INTO doctors (name, specialty) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertDoctor)) {
                pstmt.setString(1, "Dr. Ahmed");
                pstmt.setString(2, "Cardiology");
                pstmt.executeUpdate();
                System.out.println("Doctor added successfully.");
            }

            // إدخال موعد جديد
            String insertAppointment = "INSERT INTO appointments (patient_id, doctor_id, date) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertAppointment)) {
                pstmt.setInt(1, 1);  // ID المريض (تأكد من أن هذا ID المريض موجود في قاعدة البيانات)
                pstmt.setInt(2, 1);  // ID الطبيب (تأكد من أن هذا ID الطبيب موجود في قاعدة البيانات)
                pstmt.setString(3, "2024-12-15 10:00:00");
                pstmt.executeUpdate();
                System.out.println("Appointment added successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();  // سيتم طباعة أي خطأ يحدث في عملية الإدخال
        }
    }
}

