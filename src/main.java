package main.java;

// Java Swing & AWT imports
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;

// Java Util imports
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

// Model imports
import model.Doctor;
import model.Patient;
import model.Appointment;
import model.MedicalRecord;
import model.PatientHistory;
import model.Prescription;
import model.LabResult;
import model.EmergencyMedicalRecord;
import model.GeneralMedicalRecord;
import model.Cardiologist;
import model.Neurologist;
import model.GeneralPractitioner;

// Factory imports
import factory.AbstractFactory;
import factory.HospitalFactory;
import factory.DoctorFactory;
import factory.MedicalRecordFactory;

// Service imports
import service.AppointmentScheduler;

// Database imports
import repository.DatabaseManager;
import repository.PatientDatabaseManager;
import repository.DoctorDatabaseManager;

// Database imports
import database.DatabaseSetup;

public class main {
    private JFrame frame;
    private DefaultTableModel patientTableModel;
    private DefaultTableModel doctorTableModel;
    private DefaultTableModel appointmentTableModel;
    private DefaultTableModel labResultTableModel;
    private int patientIdCounter = 1;
    private int doctorIdCounter = 1;
    private DatabaseManager databaseManager;
    private PatientDatabaseManager patientDBManager;
    private DoctorDatabaseManager doctorDBManager;

    public main() {
        initializeDatabase();
        initializeFrame();
        createMainPanel();
        loadExistingData();
        frame.setVisible(true);
    }

    private void initializeDatabase() {
        try {
            DatabaseSetup.initializeDatabase();
            databaseManager = DatabaseManager.getInstance();
            patientDBManager = PatientDatabaseManager.getInstance();
            doctorDBManager = DoctorDatabaseManager.getInstance();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error initializing database: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void initializeFrame() {
        frame = new JFrame("Medical Clinic Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
    }

    private void createMainPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("Patients", createPatientsPanel());
        tabbedPane.addTab("Doctors", createDoctorsPanel());
        tabbedPane.addTab("Appointments", createAppointmentsPanel());
        
        frame.add(tabbedPane);
    }

    private JPanel createPatientsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        patientTableModel = new DefaultTableModel(
            new Object[]{"ID", "Name", "Age", "Disease"}, 0
        );
        
        JTable patientTable = new JTable(patientTableModel);
        JScrollPane scrollPane = new JScrollPane(patientTable);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = createStyledButton("Add New Patient");
        JButton editButton = createStyledButton("Edit Patient");
        JButton deleteButton = createStyledButton("Delete Patient");
        JButton refreshButton = createStyledButton("Refresh List");
        
        editButton.setBackground(new Color(60, 179, 113)); // Medium Sea Green
        deleteButton.setBackground(new Color(220, 20, 60)); // Crimson
        
        addButton.addActionListener(e -> openAddPatientDialog());
        refreshButton.addActionListener(e -> refreshPatientsList());
        
        editButton.addActionListener(e -> {
            int selectedRow = patientTable.getSelectedRow();
            if (selectedRow != -1) {
                String patientId = patientTableModel.getValueAt(selectedRow, 0).toString();
                String name = patientTableModel.getValueAt(selectedRow, 1).toString();
                int age = Integer.parseInt(patientTableModel.getValueAt(selectedRow, 2).toString());
                String disease = patientTableModel.getValueAt(selectedRow, 3).toString();
                openEditPatientDialog(patientId, name, age, disease);
            } else {
                JOptionPane.showMessageDialog(frame,
                    "Please select a patient to edit",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        deleteButton.addActionListener(e -> {
            int selectedRow = patientTable.getSelectedRow();
            if (selectedRow != -1) {
                String patientId = patientTableModel.getValueAt(selectedRow, 0).toString();
                int confirm = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to delete this patient?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
                    
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        patientDBManager.deletePatient(patientId);
                        refreshPatientsList();
                        JOptionPane.showMessageDialog(frame,
                            "Patient deleted successfully",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame,
                            "Error deleting patient: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame,
                    "Please select a patient to delete",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createDoctorsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        doctorTableModel = new DefaultTableModel(
            new Object[]{"ID", "Name", "Specialty"}, 0
        );
        
        JTable doctorTable = new JTable(doctorTableModel);
        JScrollPane scrollPane = new JScrollPane(doctorTable);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = createStyledButton("Add New Doctor");
        JButton editButton = createStyledButton("Edit Doctor");
        JButton deleteButton = createStyledButton("Delete Doctor");
        JButton refreshButton = createStyledButton("Refresh List");
        
        editButton.setBackground(new Color(60, 179, 113)); // Medium Sea Green
        deleteButton.setBackground(new Color(220, 20, 60)); // Crimson
        
        addButton.addActionListener(e -> openAddDoctorDialog());
        refreshButton.addActionListener(e -> refreshDoctorsList());
        
        editButton.addActionListener(e -> {
            int selectedRow = doctorTable.getSelectedRow();
            if (selectedRow != -1) {
                String doctorId = doctorTableModel.getValueAt(selectedRow, 0).toString();
                String name = doctorTableModel.getValueAt(selectedRow, 1).toString();
                String specialty = doctorTableModel.getValueAt(selectedRow, 2).toString();
                openEditDoctorDialog(doctorId, name, specialty);
            } else {
                JOptionPane.showMessageDialog(frame,
                    "Please select a doctor to edit",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        deleteButton.addActionListener(e -> {
            int selectedRow = doctorTable.getSelectedRow();
            if (selectedRow != -1) {
                String doctorId = doctorTableModel.getValueAt(selectedRow, 0).toString();
                int confirm = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to delete this doctor?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
                    
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        doctorDBManager.deleteDoctor(doctorId);
                        refreshDoctorsList();
                        JOptionPane.showMessageDialog(frame,
                            "Doctor deleted successfully",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame,
                            "Error deleting doctor: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame,
                    "Please select a doctor to delete",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createAppointmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        appointmentTableModel = new DefaultTableModel(
            new Object[]{"ID", "Patient", "Doctor", "Date", "Status"}, 0
        );
        
        JTable appointmentTable = new JTable(appointmentTableModel);
        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = createStyledButton("Schedule New Appointment");
        JButton editButton = createStyledButton("Edit Appointment");
        JButton deleteButton = createStyledButton("Cancel Appointment");
        JButton refreshButton = createStyledButton("Refresh List");
        
        editButton.setBackground(new Color(60, 179, 113)); // Medium Sea Green
        deleteButton.setBackground(new Color(220, 20, 60)); // Crimson
        
        addButton.addActionListener(e -> openAddAppointmentDialog());
        refreshButton.addActionListener(e -> refreshAppointmentsList());
        
        editButton.addActionListener(e -> {
            int selectedRow = appointmentTable.getSelectedRow();
            if (selectedRow != -1) {
                String appointmentId = appointmentTableModel.getValueAt(selectedRow, 0).toString();
                String patient = appointmentTableModel.getValueAt(selectedRow, 1).toString();
                String doctor = appointmentTableModel.getValueAt(selectedRow, 2).toString();
                String date = appointmentTableModel.getValueAt(selectedRow, 3).toString();
                String status = appointmentTableModel.getValueAt(selectedRow, 4).toString();
                openEditAppointmentDialog(appointmentId, patient, doctor, date, status);
            } else {
                JOptionPane.showMessageDialog(frame,
                    "Please select an appointment to edit",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        deleteButton.addActionListener(e -> {
            int selectedRow = appointmentTable.getSelectedRow();
            if (selectedRow != -1) {
                String appointmentId = appointmentTableModel.getValueAt(selectedRow, 0).toString();
                int confirm = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to cancel this appointment?",
                    "Confirm Cancellation",
                    JOptionPane.YES_NO_OPTION);
                    
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        databaseManager.cancelAppointment(appointmentId);
                        refreshAppointmentsList();
                        JOptionPane.showMessageDialog(frame,
                            "Appointment cancelled successfully",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame,
                            "Error cancelling appointment: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame,
                    "Please select an appointment to cancel",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void loadExistingData() {
        refreshPatientsList();
        refreshDoctorsList();
        refreshAppointmentsList();
    }

    private void refreshPatientsList() {
        patientTableModel.setRowCount(0);
        try {
            List<Patient> patients = patientDBManager.getAllPatients();
            for (Patient patient : patients) {
                patientTableModel.addRow(new Object[]{
                    patient.getPatientId(),
                    patient.getName(),
                    patient.getAge(),
                    patient.getDisease()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                "Error loading patients: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshDoctorsList() {
        doctorTableModel.setRowCount(0);
        try {
            List<Doctor> doctors = doctorDBManager.getDoctors();
            for (Doctor doctor : doctors) {
                doctorTableModel.addRow(new Object[]{
                    doctor.getDoctorId(),
                    doctor.getName(),
                    doctor.getSpecialty()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                "Error loading doctors: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshAppointmentsList() {
        appointmentTableModel.setRowCount(0);
        try {
            List<Appointment> appointments = databaseManager.getAllAppointments();
            for (Appointment appointment : appointments) {
                appointmentTableModel.addRow(new Object[]{
                    appointment.getId(),
                    appointment.getPatient().getName(),
                    appointment.getDoctor().getName(),
                    appointment.getTimeSlot(),
                    "Scheduled"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                "Error loading appointments: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAddPatientDialog() {
        JDialog dialog = new JDialog(frame, "Add New Patient", true);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField nameField = new JTextField(20);
        JSpinner ageSpinner = new JSpinner(new SpinnerNumberModel(18, 0, 150, 1));
        JTextField diseaseField = new JTextField(20);
        
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Age:"));
        formPanel.add(ageSpinner);
        formPanel.add(new JLabel("Disease:"));
        formPanel.add(diseaseField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = createStyledButton("Save");
        JButton cancelButton = createStyledButton("Cancel");
        
        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                int age = (Integer) ageSpinner.getValue();
                String disease = diseaseField.getText().trim();
                
                if (name.isEmpty()) {
                    throw new IllegalArgumentException("Name cannot be empty");
                }
                
                databaseManager.addPatientToDatabase(name, age, disease);
                
                dialog.dispose();
                refreshPatientsList();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Error adding patient: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private void openEditPatientDialog(String patientId, String currentName, int currentAge, String currentDisease) {
        JDialog dialog = new JDialog(frame, "Edit Patient", true);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField nameField = new JTextField(currentName, 20);
        JSpinner ageSpinner = new JSpinner(new SpinnerNumberModel(currentAge, 0, 150, 1));
        JTextField diseaseField = new JTextField(currentDisease, 20);
        
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Age:"));
        formPanel.add(ageSpinner);
        formPanel.add(new JLabel("Disease:"));
        formPanel.add(diseaseField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = createStyledButton("Save Changes");
        JButton cancelButton = createStyledButton("Cancel");
        
        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                int age = (Integer) ageSpinner.getValue();
                String disease = diseaseField.getText().trim();
                
                if (name.isEmpty()) {
                    throw new IllegalArgumentException("Name cannot be empty");
                }
                
                patientDBManager.updatePatient(patientId, name, age, disease);
                
                dialog.dispose();
                refreshPatientsList();
                
                JOptionPane.showMessageDialog(frame,
                    "Patient updated successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Error updating patient: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private void openAddDoctorDialog() {
        JDialog dialog = new JDialog(frame, "Add New Doctor", true);
        dialog.setSize(500, 400);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Doctor Name:");
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        JTextField nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        JLabel specialtyLabel = new JLabel("Specialty:");
        formPanel.add(specialtyLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        String[] specialties = {"Cardiologist", "Neurologist", "GeneralPractitioner"};
        JComboBox<String> specialtyCombo = new JComboBox<>(specialties);
        formPanel.add(specialtyCombo, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton saveButton = createStyledButton("Save");
        JButton cancelButton = createStyledButton("Cancel");
        
        saveButton.setBackground(new Color(70, 130, 180));
        cancelButton.setBackground(new Color(190, 190, 190));

        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String specialty = specialtyCombo.getSelectedItem().toString();

                if (name.isEmpty()) {
                    throw new IllegalArgumentException("Doctor name cannot be empty");
                }

                if (name.length() < 3) {
                    throw new IllegalArgumentException("Doctor name must be at least 3 characters long");
                }

                if (!name.matches("^[a-zA-Z\\s.]+$")) {
                    throw new IllegalArgumentException("Doctor name can only contain letters, spaces, and dots");
                }

                String doctorId = databaseManager.addDoctorToDatabase(name, specialty);
                
                dialog.dispose();
                refreshDoctorsList();

                JOptionPane.showMessageDialog(dialog,
                    String.format("Doctor Added Successfully!\n\nID: %s\nName: %s\nSpecialty: %s",
                        doctorId, name, specialty),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Error adding doctor: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel);

        dialog.getRootPane().setDefaultButton(saveButton);
        dialog.setResizable(false);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void openEditDoctorDialog(String doctorId, String currentName, String currentSpecialty) {
        JDialog dialog = new JDialog(frame, "Edit Doctor", true);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField nameField = new JTextField(currentName, 20);
        String[] specialties = {"Cardiologist", "Neurologist", "GeneralPractitioner"};
        JComboBox<String> specialtyCombo = new JComboBox<>(specialties);
        specialtyCombo.setSelectedItem(currentSpecialty);
        
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Specialty:"));
        formPanel.add(specialtyCombo);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = createStyledButton("Save Changes");
        JButton cancelButton = createStyledButton("Cancel");
        
        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String specialty = specialtyCombo.getSelectedItem().toString();
                
                if (name.isEmpty()) {
                    throw new IllegalArgumentException("Name cannot be empty");
                }
                
                doctorDBManager.updateDoctor(doctorId, name, specialty);
                
                dialog.dispose();
                refreshDoctorsList();
                
                JOptionPane.showMessageDialog(frame,
                    "Doctor updated successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Error updating doctor: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private void openAddAppointmentDialog() {
        JDialog dialog = new JDialog(frame, "Schedule New Appointment", true);
        dialog.setSize(500, 400);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setLocationRelativeTo(frame);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        List<Patient> patients = patientDBManager.getAllPatients();
        List<Doctor> doctors = doctorDBManager.getDoctors();

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel patientLabel = new JLabel("Select Patient:");
        formPanel.add(patientLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        DefaultComboBoxModel<String> patientModel = new DefaultComboBoxModel<>();
        for (Patient patient : patients) {
            patientModel.addElement(patient.getName() + " (ID: " + patient.getPatientId() + ")");
        }
        JComboBox<String> patientCombo = new JComboBox<>(patientModel);
        formPanel.add(patientCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        JLabel doctorLabel = new JLabel("Select Doctor:");
        formPanel.add(doctorLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        DefaultComboBoxModel<String> doctorModel = new DefaultComboBoxModel<>();
        for (Doctor doctor : doctors) {
            doctorModel.addElement(doctor.getName() + " (" + doctor.getSpecialty() + ") - ID: " + doctor.getDoctorId());
        }
        JComboBox<String> doctorCombo = new JComboBox<>(doctorModel);
        formPanel.add(doctorCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        JLabel dateLabel = new JLabel("Appointment Date:");
        formPanel.add(dateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd HH:mm");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setValue(new Date());
        formPanel.add(dateSpinner, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton saveButton = createStyledButton("Schedule Appointment");
        JButton cancelButton = createStyledButton("Cancel");
        
        saveButton.setBackground(new Color(70, 130, 180));
        cancelButton.setBackground(new Color(190, 190, 190));

        saveButton.addActionListener(e -> {
            try {
                if (patientCombo.getSelectedIndex() == -1) {
                    throw new IllegalArgumentException("Please select a patient");
                }
                if (doctorCombo.getSelectedIndex() == -1) {
                    throw new IllegalArgumentException("Please select a doctor");
                }

                String selectedPatient = patientCombo.getSelectedItem().toString();
                String selectedDoctor = doctorCombo.getSelectedItem().toString();
                
                String patientId = selectedPatient.substring(selectedPatient.lastIndexOf("ID: ") + 4, 
                                                          selectedPatient.lastIndexOf(")"));
                String doctorId = selectedDoctor.substring(selectedDoctor.lastIndexOf("ID: ") + 4);
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String appointmentDate = sdf.format((Date) dateSpinner.getValue());
                
                databaseManager.addAppointmentToDatabase(patientId, doctorId, appointmentDate);

                dialog.dispose();
                refreshAppointmentsList();

                JOptionPane.showMessageDialog(dialog,
                    String.format("Appointment Scheduled Successfully!\n\nPatient: %s\nDoctor: %s\nDate: %s",
                        selectedPatient.substring(0, selectedPatient.indexOf(" (ID:")),
                        selectedDoctor.substring(0, selectedDoctor.indexOf(" (")),
                        appointmentDate),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog,
                    ex.getMessage(),
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Error scheduling appointment: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel);

        dialog.getRootPane().setDefaultButton(saveButton);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private void openEditAppointmentDialog(String appointmentId, String currentPatient, String currentDoctor, 
                                         String currentDate, String currentStatus) {
        JDialog dialog = new JDialog(frame, "Edit Appointment", true);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        List<Patient> patients = patientDBManager.getAllPatients();
        List<Doctor> doctors = doctorDBManager.getDoctors();
        
        DefaultComboBoxModel<String> patientModel = new DefaultComboBoxModel<>();
        for (Patient patient : patients) {
            patientModel.addElement(patient.getName() + " (ID: " + patient.getPatientId() + ")");
        }
        JComboBox<String> patientCombo = new JComboBox<>(patientModel);
        
        DefaultComboBoxModel<String> doctorModel = new DefaultComboBoxModel<>();
        for (Doctor doctor : doctors) {
            doctorModel.addElement(doctor.getName() + " (" + doctor.getSpecialty() + ") - ID: " + doctor.getDoctorId());
        }
        JComboBox<String> doctorCombo = new JComboBox<>(doctorModel);
        
        for (int i = 0; i < patientModel.getSize(); i++) {
            if (patientModel.getElementAt(i).contains(currentPatient)) {
                patientCombo.setSelectedIndex(i);
                break;
            }
        }
        
        for (int i = 0; i < doctorModel.getSize(); i++) {
            if (doctorModel.getElementAt(i).contains(currentDoctor)) {
                doctorCombo.setSelectedIndex(i);
                break;
            }
        }
        
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd HH:mm");
        dateSpinner.setEditor(dateEditor);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            dateSpinner.setValue(sdf.parse(currentDate));
        } catch (Exception ex) {
            dateSpinner.setValue(new Date());
        }
        
        formPanel.add(new JLabel("Patient:"));
        formPanel.add(patientCombo);
        formPanel.add(new JLabel("Doctor:"));
        formPanel.add(doctorCombo);
        formPanel.add(new JLabel("Date:"));
        formPanel.add(dateSpinner);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = createStyledButton("Save Changes");
        JButton cancelButton = createStyledButton("Cancel");
        
        saveButton.addActionListener(e -> {
            try {
                String selectedPatient = patientCombo.getSelectedItem().toString();
                String selectedDoctor = doctorCombo.getSelectedItem().toString();
                
                String patientId = selectedPatient.substring(selectedPatient.lastIndexOf("ID: ") + 4, 
                                                          selectedPatient.lastIndexOf(")"));
                String doctorId = selectedDoctor.substring(selectedDoctor.lastIndexOf("ID: ") + 4);
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String appointmentDate = sdf.format((Date) dateSpinner.getValue());
                
                databaseManager.updateAppointment(appointmentId, patientId, doctorId, appointmentDate);
                
                dialog.dispose();
                refreshAppointmentsList();
                
                JOptionPane.showMessageDialog(frame,
                    "Appointment updated successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Error updating appointment: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        return button;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new main());
    }
} 