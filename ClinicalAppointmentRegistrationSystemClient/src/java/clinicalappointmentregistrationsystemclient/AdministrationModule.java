/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicalappointmentregistrationsystemclient;

import ejb.session.stateless.DoctorEntitySessionBeanRemote;
import ejb.session.stateless.PatientEntitySessionBeanRemote;
import ejb.session.stateless.StaffEntitySessionBeanRemote;
import entity.DoctorEntity;
import entity.PatientEntity;
import entity.StaffEntity;
import java.sql.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import util.exception.DeleteDoctorException;
import util.exception.DoctorExistException;
import util.exception.DoctorNotFoundException;
import util.exception.LeaveRejectedException;
import util.exception.PatientExistException;
import util.exception.PatientNotFoundException;
import util.exception.StaffNotFoundException;
import util.exception.StaffUsernameExistException;

/**
 *
 * @author crs
 */
public class AdministrationModule {
    
    private StaffEntitySessionBeanRemote staffEntitySessionBeanRemote;
    private DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote;
    private PatientEntitySessionBeanRemote patientEntitySessionBeanRemote;
    
    private StaffEntity currentStaffEntity;

    public AdministrationModule() {
    }

    public AdministrationModule(StaffEntitySessionBeanRemote staffEntitySessionBeanRemote, DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote, PatientEntitySessionBeanRemote patientEntitySessionBeanRemote, StaffEntity currentStaffEntity) {
        this();
        this.staffEntitySessionBeanRemote = staffEntitySessionBeanRemote;
        this.doctorEntitySessionBeanRemote = doctorEntitySessionBeanRemote;
        this.patientEntitySessionBeanRemote = patientEntitySessionBeanRemote;
        this.currentStaffEntity = currentStaffEntity;
    }
    
    public void administrationOperation() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("*** CARS :: Administration Operation ***\n");
            System.out.println("1: Patient Management");
            System.out.println("2: Doctor Management");
            System.out.println("3: Staff Management");
            System.out.println("4: Back\n");
            response = 0;
            
            try {
               while(response < 1 || response > 4) {
                    System.out.print("> ");
                    response = scanner.nextInt();

                    if (response == 1) {
                        patientManagement();
                    } else if (response == 2) {
                        doctorManagement();
                    } else if (response == 3) {
                        staffManagement();
                    } else if (response == 4) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n"); 
                    }
                } 
            } catch (IllegalArgumentException | InputMismatchException ex) {
                System.out.println("Invalid input! Please try again!\n");
                scanner.nextLine();
            }
            
            
            if (response == 4) {
                break;
            }
        }
    }
    
    public void patientManagement() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("*** CARS :: Administration Operation :: Patient Management ***\n");
            System.out.println("1: Add Patient");
            System.out.println("2: View Patient Details");
            System.out.println("3: Update Patient");
            System.out.println("4: Delete Patient");
            System.out.println("5: View All Patients");
            System.out.println("6: Back\n");
            response = 0;
            
            try {
                while(response < 1 || response > 6) {
                    System.out.print("> ");
                    response = scanner.nextInt();

                    if (response == 1) {
                        doAddPatient();
                    } else if (response == 2) {
                        doViewPatientDetails();
                    } else if (response == 3) {
                        doUpdatePatient();
                    } else if (response == 4) {
                        doDeletePatient();
                    } else if (response == 5) {
                        doViewAllPatients();
                    } else if (response == 6) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n"); 
                    }
                }
            } catch (IllegalArgumentException | InputMismatchException ex) {
                System.out.println("Invalid input! Please try again!\n");
                scanner.nextLine();
            }
            
            
            if (response == 6) {
                break;
            }
        }
    }
    
    public void doAddPatient() {
        Scanner scanner = new Scanner(System.in);
        PatientEntity newPatientEntity = new PatientEntity();
        
        try {
            while (true) {
                System.out.println("*** CARS :: Administration Operation :: Patient Management :: Add New Patient ***\n");
                System.out.print("Enter First Name> ");
                String firstName = scanner.nextLine().trim();
                System.out.print("Enter Last Name> ");
                String lastName = scanner.nextLine().trim();
                System.out.print("Enter Identity Number> ");
                String identityNum = scanner.nextLine().trim();
                System.out.print("Enter Gender> ");
                String gender = scanner.nextLine().trim();
                System.out.print("Enter Phone> ");
                String phone = scanner.nextLine().trim();
                System.out.print("Enter Address> ");
                String address = scanner.nextLine().trim();
                System.out.print("Enter Password> ");
                String password = scanner.nextLine().trim();
                System.out.print("Enter Age> ");
                Integer age = scanner.nextInt();
                scanner.nextLine();

                if (firstName.length() > 0 && lastName.length() > 0 && identityNum.length() > 0 && 
                        gender.length() > 0 && phone.length() > 0 && address.length() > 0 &&
                        password.length() > 0 && age > 0) {
                    newPatientEntity.setFirstName(firstName);
                    newPatientEntity.setLastName(lastName);
                    newPatientEntity.setIdentityNumber(identityNum);
                    newPatientEntity.setGender(gender);
                    newPatientEntity.setPhone(phone);
                    newPatientEntity.setAddress(address);
                    newPatientEntity.setPassword(password);
                    newPatientEntity.setAge(age);
                    break;
                } else {
                    System.out.println("Input fields cannot be empty and password has to be 6 digits!\n");
                }
            }
            
            try {
                patientEntitySessionBeanRemote.addNewPatient(newPatientEntity);
                System.out.println("New staff created successfully!: \n");
            } catch (PatientExistException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        } catch (IllegalArgumentException | InputMismatchException ex) {
            System.out.println("Invalid input! Please try again!\n");
            scanner.nextLine();
        }
    }
    
    public void doViewPatientDetails() {
        Scanner scanner = new Scanner(System.in);
        String identityNum = "";
        
        while (true) {
            System.out.println("*** CARS :: Administration Operation :: Patient Management :: View Patient Details ***\n");
            System.out.print("Enter Patient Identity Number> ");
            identityNum = scanner.nextLine().trim();
            
            if (identityNum.length() > 0) {
                break;
            } else {
                System.out.println("Identity number cannot be empty!\n");
            }
        }
        
        try {
            PatientEntity pe = patientEntitySessionBeanRemote.retrievePatientByIdNum(identityNum);
            
            System.out.printf("%-10s|%-15s|%-15s|%-15s|%-10s|%-10s|%-15s|%-20s|%-20s\n", "Patient Id", "Identity Number", "First Name", "Last Name", "Gender", "Age", "Phone", "Address", "Password");
            System.out.printf("%-10s|%-15s|%-15s|%-15s|%-10s|%-10s|%-15s|%-20s|%-20s\n", pe.getPatientId(), pe.getIdentityNumber(), pe.getFirstName(), pe.getLastName(), pe.getGender(), pe.getAge(), pe.getPhone(), pe.getAddress(), pe.getPassword());
            System.out.print("Press any key to continue...> ");
            scanner.nextLine();
        } catch (PatientNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        }   
    }
    
    public void doUpdatePatient() {
        Scanner scanner = new Scanner(System.in);
        String identityNum = "";
        
        while (true) {
            System.out.println("*** CARS :: Administration Operation :: Patient Management :: Update Patient ***\n");
            System.out.print("Enter patient's identity number to update> ");
            identityNum = scanner.nextLine().trim();
            
            if (identityNum.length() > 0) {
                break;
            } else {
                System.out.println("Identity number cannot be empty!\n");
            }
        }
        
        try {
            PatientEntity pe = patientEntitySessionBeanRemote.retrievePatientByIdNum(identityNum);
            System.out.printf("%-10s|%-15s|%-15s|%-15s|%-10s|%-10s|%-15s|%-20s|%-20s\n", "Patient Id", "Identity Number", "First Name", "Last Name", "Gender", "Age", "Phone", "Address", "Password");
            System.out.printf("%-10s|%-15s|%-15s|%-15s|%-10s|%-10s|%-15s|%-20s|%-20s\n", pe.getPatientId(), pe.getIdentityNumber(), pe.getFirstName(), pe.getLastName(), pe.getGender(), pe.getAge(), pe.getPhone(), pe.getAddress(), pe.getPassword());
            PatientEntity patientEntity = new PatientEntity(pe.getFirstName(), pe.getLastName(), pe.getIdentityNumber(), pe.getGender(), pe.getAge(), pe.getPhone(), pe.getAddress(), pe.getPassword());
            
            System.out.print("Enter First Name (blank if no change)> ");
            String input = scanner.nextLine().trim();
            if (input.length() > 0) {
                patientEntity.setFirstName(input);
            }
            
            System.out.print("Enter Last Name (blank if no change)> ");
            input = scanner.nextLine().trim();
            if (input.length() > 0) {
                patientEntity.setLastName(input);
            }
            
            System.out.print("Enter Age (type -1 if no change)> ");
            Integer temp = scanner.nextInt();
            scanner.nextLine();
            if (temp > 0) {
                patientEntity.setAge(temp);
            }
            
            System.out.print("Enter Phone (blank if no change)> ");
            input = scanner.nextLine().trim();
            if (input.length() > 0) {
                patientEntity.setPhone(input);
            }
            
            System.out.print("Enter Address (blank if no change)> ");
            input = scanner.nextLine().trim();
            if (input.length() > 0) {
                patientEntity.setAddress(input);
            }
            
            patientEntitySessionBeanRemote.updatePatient(patientEntity);
            System.out.println("Patient updated successfully!\n");
            
        } catch (PatientNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }
    
    public void doDeletePatient() {
        Scanner scanner = new Scanner(System.in);
        String identityNum = "";
        
        while (true) {
            System.out.println("*** CARS :: Administration Operation :: Patient Management :: Delete Patient ***\n");
            System.out.print("Enter patient's identity number to delete> ");
            identityNum = scanner.nextLine().trim();
            
            if (identityNum.length() > 0) {
                break;
            } else {
                System.out.println("Identity number cannot be empty!\n");
            }
        }
        
        try {
            PatientEntity pe = patientEntitySessionBeanRemote.retrievePatientByIdNum(identityNum);
            System.out.printf("%-10s|%-15s|%-15s|%-15s|%-10s|%-10s|%-15s|%-20s|%-20s\n", "Patient Id", "Identity Number", "First Name", "Last Name", "Gender", "Age", "Phone", "Address", "Password");
            System.out.printf("%-10s|%-15s|%-15s|%-15s|%-10s|%-10s|%-15s|%-20s|%-20s\n", pe.getPatientId(), pe.getIdentityNumber(), pe.getFirstName(), pe.getLastName(), pe.getGender(), pe.getAge(), pe.getPhone(), pe.getAddress(), pe.getPassword());
            System.out.print("Confirm delete this staff? (Enter y if yes, n if no) > ");
            String input = scanner.nextLine().trim();

            if (input.equals("y")) {
                // need to check in future whether patient got existings appointments
                System.out.println("Patient is deleted successfully\n");
            } else {
                System.out.println("Patient is not deleted\n");
            }
            
        } catch (PatientNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }
    
    public void doViewAllPatients() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** CARS :: Administration Operation :: Patient Management :: View All Patients ***\n");
        List<PatientEntity> listOfPatients = patientEntitySessionBeanRemote.retrieveAllPatients();
        
        System.out.printf("%-10s|%-15s|%-15s|%-15s|%-10s|%-10s|%-15s|%-20s|%-20s\n", "Patient Id", "Identity Number", "First Name", "Last Name", "Gender", "Age", "Phone", "Address", "Password");
    
        for (PatientEntity pe : listOfPatients) {
            System.out.printf("%-10s|%-15s|%-15s|%-15s|%-10s|%-10s|%-15s|%-20s|%-20s\n", pe.getPatientId(), pe.getIdentityNumber(), pe.getFirstName(), pe.getLastName(), pe.getGender(), pe.getAge(), pe.getPhone(), pe.getAddress(), pe.getPassword());
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    public void doctorManagement() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("*** CARS :: Administration Operation :: Doctor Management ***\n");
            System.out.println("1: Add Doctor");
            System.out.println("2: View Doctor Details");
            System.out.println("3: Update Doctor");
            System.out.println("4: Delete Doctor");
            System.out.println("5: View All Doctors");
            System.out.println("6: Leave Management");
            System.out.println("7: Back\n");
            response = 0;
            
            try {
                while (response < 1 || response > 7) {
                    System.out.print("> ");
                    response = scanner.nextInt();

                    if (response == 1) {
                        doAddDoctor();
                    } else if (response == 2) {
                        doViewDoctorDetails();
                    } else if (response == 3) {
                        doUpdateDoctor();
                    } else if (response == 4) {
                        doDeleteDoctor();
                    } else if (response == 5) {
                        doViewAllDoctors();
                    } else if (response == 6) {
                        try {
                            doLeaveManage();
                        } catch (InputMismatchException | IllegalArgumentException ex) {
                            System.out.println("Invalid Input! Please try again!\n");
                        }
                    } else if (response == 7) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n"); 
                    }
                }
            } catch (IllegalArgumentException | InputMismatchException ex) {
                System.out.println("Invalid input! Please try again!\n");
                scanner.nextLine();
            }
            
            
            if (response == 7) {
                break;
            }
         }
    }
    
    public void doAddDoctor() {
        Scanner scanner = new Scanner(System.in);
        DoctorEntity doctorEntity = new DoctorEntity();
        
        while (true) {
            System.out.println("*** CARS :: Administration Operation :: Doctor Management :: Add New Doctor ***\n");
            System.out.print("Enter First Name> ");
            String firstName = scanner.nextLine().trim();
            System.out.print("Enter Last Name> ");
            String lastName = scanner.nextLine().trim();
            System.out.print("Enter Registration> ");
            String registration = scanner.nextLine().trim();
            System.out.print("Enter Qualifications> ");
            String qualifications = scanner.nextLine().trim();
            
            if (firstName.length() > 0 && lastName.length() > 0 && registration.length() > 0 && qualifications.length() > 0) {
                doctorEntity.setFirstName(firstName);
                doctorEntity.setLastName(lastName);
                doctorEntity.setRegistration(registration);
                doctorEntity.setQualifications(qualifications);
                break;
            } else {
                System.out.println("Input fields cannot be empty!\n");
            }
        }
        
        try {
            doctorEntitySessionBeanRemote.addNewDoctor(doctorEntity);
            System.out.println("Doctor created successfully!\n");
        } catch (DoctorExistException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }
    
    public void doViewDoctorDetails() {
        Scanner scanner = new Scanner(System.in);
        String registration = "";
        
        while (true) {
            System.out.println("*** CARS :: Administration Operation :: Doctor Management :: View Doctor Details ***\n");
            System.out.print("Enter Doctor Registration> ");
            registration = scanner.nextLine().trim();
            
            if (registration.length() > 0) {
                break;
            } else {
                System.out.println("Doctor registration cannot be empty!\n");
            }
        }
        
        try {
            DoctorEntity de = doctorEntitySessionBeanRemote.retrieveDoctorByRegistration(registration);
            System.out.printf("%-10s|%-15s|%-15s|%-13s|%-13s\n", "Doctor Id", "First Name", "Last Name", "Registration", "Qualifications");
            System.out.printf("%-10s|%-15s|%-15s|%-13s|%-13s\n", de.getDoctorId(), de.getFirstName(), de.getLastName(), de.getRegistration(), de.getQualifications());
            System.out.print("Press any key to continue...> ");
            scanner.nextLine();
        } catch (DoctorNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }
    
    public void doUpdateDoctor() {
        Scanner scanner = new Scanner(System.in);
        String registration = "";
        
        while (true) {
            System.out.println("*** CARS :: Administration Operation :: Doctor Management :: Update Doctor ***\n");
            System.out.print("Enter Doctor registration to update> ");
            registration = scanner.nextLine().trim();
            
            if (registration.length() > 0) {
                break;
            } else {
                System.out.println("Doctor registration cannot be empty!\n");
            }
        }
        
        try {
            DoctorEntity de = doctorEntitySessionBeanRemote.retrieveDoctorByRegistration(registration);
            System.out.printf("%-10s|%-15s|%-15s|%-13s|%-13s\n", "Doctor Id", "First Name", "Last Name", "Registration", "Qualifications");
            System.out.printf("%-10s|%-15s|%-15s|%-13s|%-13s\n", de.getDoctorId(), de.getFirstName(), de.getLastName(), de.getRegistration(), de.getQualifications());
            DoctorEntity doctorEntity = new DoctorEntity(de.getFirstName(), de.getLastName(), de.getRegistration(),de.getQualifications());
            
            System.out.print("Enter First Name (blank if no change)> ");
            String input = scanner.nextLine().trim();
            if (input.length() > 0) {
                doctorEntity.setFirstName(input);
            }
            
            System.out.print("Enter Last Name (blank if no change)> ");
            input = scanner.nextLine().trim();
            if (input.length() > 0) {
                doctorEntity.setLastName(input);
            }
            
            System.out.print("Enter Qualifications (blank if no change)> ");
            input = scanner.nextLine().trim();
             if (input.length() > 0) {
                doctorEntity.setQualifications(input);
            }
             
            doctorEntitySessionBeanRemote.updateDoctor(doctorEntity);
            System.out.println("Doctor updated successfully!\n");
        } catch (DoctorNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }
    
    public void doDeleteDoctor() {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        
        while (true) {
            System.out.println("*** CARS :: Administration Operation :: Doctor Management :: Delete Doctor ***\n");
            System.out.print("Enter Doctor's registration: ");
            input = scanner.nextLine().trim(); 
            
            if (input.length() > 0) {
                break;
            } else {
                System.out.println("Doctor registration cannot be empty!\n");
            }
        }
        
        try {
            DoctorEntity de = doctorEntitySessionBeanRemote.retrieveDoctorByRegistration(input);
            System.out.printf("%-10s|%-15s|%-15s|%-13s|%-13s\n", "Doctor Id", "First Name", "Last Name", "Registration", "Qualifications");
            System.out.printf("%-10s|%-15s|%-15s|%-13s|%-13s\n", de.getDoctorId(), de.getFirstName(), de.getLastName(), de.getRegistration(), de.getQualifications());
            System.out.print("Confirm Delete Doctor? (Type y if yes, n if no): ");
            input = scanner.nextLine().trim();
            
            if (input.equals("y")) {
                doctorEntitySessionBeanRemote.deleteDoctor(de.getRegistration());
                System.out.println("Doctor is deleted successfully!\n");
            } else {
                System.out.println("Doctor is not deleted!\n");
            }
        } catch (DoctorNotFoundException | DeleteDoctorException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }
    
    public void doViewAllDoctors() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** CARS :: Administration Operation :: Doctor Management :: View All Doctors ***\n");
        List<DoctorEntity> listOfDoctors = doctorEntitySessionBeanRemote.retrieveAllDoctors();
        System.out.printf("%-10s|%-15s|%-15s|%-13s|%-13s\n", "Doctor Id", "First Name", "Last Name", "Registration", "Qualifications");
        
        for (DoctorEntity de : listOfDoctors) {
            System.out.printf("%-10s|%-15s|%-15s|%-13s|%-13s\n", de.getDoctorId(), de.getFirstName(), de.getLastName(), de.getRegistration(), de.getQualifications());
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    public void doLeaveManage() throws InputMismatchException, IllegalArgumentException {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        
        while (true) {
            System.out.println("*** CARS :: Administration Operation :: Doctor Management :: Add Leave Request ***\n");
            System.out.print("Enter Doctor's Registration> ");
            input = scanner.nextLine().trim();
            
            if (input.length() > 0) {
                break;
            } else {
                System.out.println("Doctor registration cannot be empty!\n");
            }
        }
        
        System.out.print("Enter Date> ");
        Date date = Date.valueOf(scanner.nextLine().trim());
        
        try {
            doctorEntitySessionBeanRemote.applyLeave(input, date);
            System.out.println("Leave has been applied successfully!\n");
        } catch (LeaveRejectedException | DoctorNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }
     
    public void staffManagement() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("*** CARS :: Administration Operation :: Staff Management ***\n");
            System.out.println("1: Add Staff");
            System.out.println("2: View Staff Details");
            System.out.println("3: Update Staff");
            System.out.println("4: Delete Staff");
            System.out.println("5: View All Staffs");
            System.out.println("6: Back\n");
            response = 0;
            
            try {
                while(response < 1 || response > 6) {
                    System.out.print("> ");
                    response = scanner.nextInt();

                    if (response == 1) {
                        doAddStaff();
                    } else if (response == 2) {
                        doViewStaffDetails();
                    } else if (response == 3) {
                        doUpdateStaff();
                    } else if (response == 4) {
                        doDeleteStaff();
                    } else if (response == 5) {
                        doViewAllStaffs();
                    } else if (response == 6) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n"); 
                    }
                }
            } catch (IllegalArgumentException | InputMismatchException ex) {
                System.out.println("Invalid input! Please try again!\n");
                scanner.nextLine();
            }
            
            
            if (response == 6) {
                break;
            }
        }
    }
    
    public void doAddStaff() {
        Scanner scanner = new Scanner(System.in);
        StaffEntity newStaffEntity = new StaffEntity();
        
        while (true) {
            System.out.println("*** CARS :: Administration Operation :: Staff Management :: Add New Staff ***\n");
            System.out.print("Enter First Name> ");
            String firstName = scanner.nextLine().trim();
            System.out.print("Enter Last Name> ");
            String lastName = scanner.nextLine().trim();
            System.out.print("Enter Username> ");
            String username = scanner.nextLine().trim();
            System.out.print("Enter Password> ");
            String password = scanner.nextLine().trim();
            
            if (firstName.length() > 0 && lastName.length() > 0 && username.length() > 0 && password.length() > 0) {
                newStaffEntity.setFirstName(firstName);
                newStaffEntity.setLastName(lastName);
                newStaffEntity.setUserName(username);
                newStaffEntity.setPassword(password);
                break;
            } else {
                System.out.println("Input fields cannot be empty!\n");
            }
        }
        
        try {
            staffEntitySessionBeanRemote.addNewStaff(newStaffEntity);
            System.out.println("New staff created successfully!: \n");
        } catch (StaffUsernameExistException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
        
    }
    
    public void doViewStaffDetails() throws IllegalArgumentException, InputMismatchException {
        Scanner scanner = new Scanner(System.in);
        Long staffId = 0L;
        
        while (true) {
            System.out.println("*** CARS :: Administration Operation :: Staff Management :: View Staff Details ***\n");
            System.out.print("Enter Staff ID> ");
            staffId = scanner.nextLong();
            scanner.nextLine();
            
            if (staffId > 0) {
                break;
            } else {
                System.out.println("Staff ID cannot be negative!\n");
            }
        }
        
        try {
            StaffEntity staffEntity = staffEntitySessionBeanRemote.retrieveStaffById(staffId);
            
            System.out.printf("%-10s|%-15s|%-15s|%-13s|%-13s\n", "Staff Id", "First Name", "Last Name", "Username", "Password");
            System.out.printf("%-10s|%-15s|%-15s|%-13s|%-13s\n", staffEntity.getStaffId(), staffEntity.getFirstName(), staffEntity.getLastName(), staffEntity.getUserName(), staffEntity.getPassword());
            System.out.print("Press any key to continue...> ");
            scanner.nextLine();  
        } catch (StaffNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }
    
    public void doUpdateStaff() throws IllegalArgumentException, InputMismatchException {
        Scanner scanner = new Scanner(System.in);
        Long staffId = 0L;
        
        while (true) {
            System.out.println("*** CARS :: Administration Operation :: Staff Management :: Update Staff ***\n");
            System.out.print("Enter Staff ID to update> ");
            staffId = scanner.nextLong();
            
            if (staffId > 0) {
                break;
            } else {
                System.out.println("Staff ID cannot be negative!\n");
            }
        }
        
        try {
            StaffEntity staffEntity = staffEntitySessionBeanRemote.retrieveStaffById(staffId);
            System.out.printf("%-10s|%-15s|%-15s|%-13s|%-13s\n", "Staff Id", "First Name", "Last Name", "Username", "Password");
            System.out.printf("%-10s|%-15s|%-15s|%-13s|%-13s\n", staffEntity.getStaffId(), staffEntity.getFirstName(), staffEntity.getLastName(), staffEntity.getUserName(), staffEntity.getPassword());
            StaffEntity newStaffEntity = new StaffEntity(staffEntity.getFirstName(), staffEntity.getLastName(), staffEntity.getUserName(), staffEntity.getPassword());
            
            scanner.nextLine();
            System.out.print("Enter First Name (blank if no change)> ");
            String input = scanner.nextLine().trim();
            if (input.length() > 0) {
                newStaffEntity.setFirstName(input);
            }
        
            System.out.print("Enter Last Name (blank if no change)> ");
            input = scanner.nextLine().trim();
            if(input.length() > 0) {
                newStaffEntity.setLastName(input);
            }

            staffEntitySessionBeanRemote.updateStaff(newStaffEntity);
            System.out.println("Staff updated successfully!\n");
        } catch (StaffNotFoundException ex) {
           System.out.println(ex.getMessage() + "\n");
        }
    }
    
    public void doDeleteStaff() throws IllegalArgumentException, InputMismatchException {
        Scanner scanner = new Scanner(System.in);
        Long staffId = 0L;
        
        while (true) {
            System.out.println("*** CARS :: Administration Operation :: Staff Management :: Delete Staff ***\n");
            System.out.print("Enter Staff ID to delete> ");
            staffId = scanner.nextLong();
            
            if (staffId > 0) {
                break;
            } else {
                System.out.println("Staff ID cannot be negative!\n");
            }
        }
        
        if (currentStaffEntity.getStaffId().equals(staffId)) {
            System.out.println("Cannot delete the current logged in staff!\n");
        } else {
            try {
                StaffEntity staffEntity = staffEntitySessionBeanRemote.retrieveStaffById(staffId);

                System.out.printf("%-10s|%-15s|%-15s|%-13s|%-13s\n", "Staff Id", "First Name", "Last Name", "Username", "Password");
                System.out.printf("%-10s|%-15s|%-15s|%-13s|%-13s\n", staffEntity.getStaffId(), staffEntity.getFirstName(), staffEntity.getLastName(), staffEntity.getUserName(), staffEntity.getPassword());

                scanner.nextLine();
                System.out.print("Confirm delete this staff? (Enter y if yes, n if no) > ");
                String input = scanner.nextLine().trim();

                if (input.equals("y")) {
                    staffEntitySessionBeanRemote.deleteStaff(staffId);
                    System.out.println("Staff deleted successfully!");
                } else {
                    System.out.println("Staff is not deleted!");
                }
            } catch (StaffNotFoundException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        }
    }
    
    public void doViewAllStaffs() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** CARS :: Administration Operation :: Staff Management :: View All Staffs ***\n");
        List<StaffEntity> listOfStaffEntities = staffEntitySessionBeanRemote.retrieveAllStaffs();
        System.out.printf("%-10s|%-15s|%-15s|%-13s|%-13s\n", "Staff Id", "First Name", "Last Name", "Username", "Password");
        
        for (StaffEntity se : listOfStaffEntities) {
            System.out.printf("%-10s|%-15s|%-15s|%-13s|%-13s\n", se.getStaffId(), se.getFirstName(), se.getLastName(), se.getUserName(), se.getPassword());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
}
