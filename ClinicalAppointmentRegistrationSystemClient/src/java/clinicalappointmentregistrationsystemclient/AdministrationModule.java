/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicalappointmentregistrationsystemclient;

import ejb.session.stateless.DoctorEntitySessionBeanRemote;
import ejb.session.stateless.PatientEntitySessionBeanRemote;
import ejb.session.stateless.StaffEntitySessionBeanRemote;
import entity.StaffEntity;
import java.util.List;
import java.util.Scanner;
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
        }
    }
    
    public void doctorManagement() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
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
            
            if (response == 6) {
                break;
            }
        }
    }
    
    public void doAddStaff() {
        Scanner scanner = new Scanner(System.in);
        StaffEntity newStaffEntity = new StaffEntity();
        
        while (true) {
            System.out.println("*** CARS :: Administration Operation :: Staff Management :: Create New Staff ***\n");
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
    
    public void doViewStaffDetails() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** CARS :: Administration Operation :: Staff Management :: View Staff Details ***\n");
        System.out.print("Enter Staff ID> ");
        Long staffId = scanner.nextLong();
        scanner.nextLine();
        
        try {
            StaffEntity staffEntity = staffEntitySessionBeanRemote.retrieveStaffById(staffId);
            
            System.out.printf("%-10s|%-15s|%-15s|%-13s|%-13s\n", "Staff Id", "First Name", "Last Name", "Username", "Password");
            System.out.printf("%-10s|%-15s|%-15s|%-13s|%-13s\n", staffEntity.getStaffId(), staffEntity.getFirstName(), staffEntity.getLastName(), staffEntity.getUserName(), staffEntity.getPassword());
            System.out.print("Press any key to continue...> ");
            scanner.nextLine();  
        } catch (StaffNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void doUpdateStaff() {
        Scanner scanner = new Scanner(System.in);
        Long staffId = 0L;
        
        while (true) {
            System.out.println("*** CARS :: Administration Operation :: Staff Management :: Update Staff ***\n");
            System.out.print("Enter Staff ID to update> ");
            staffId = scanner.nextLong();
            
            if (staffId > 0) {
                break;
            } else {
                System.out.println("Staff ID cannot be empty!\n");
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

            try {
                staffEntitySessionBeanRemote.updateStaff(newStaffEntity);
                System.out.println("Staff updated successfully!\n");
            } catch (StaffNotFoundException ex) {
                System.out.println(ex.getMessage());
            }

        } catch (StaffNotFoundException ex) {
           System.out.println(ex.getMessage());
        }
    }
    
    public void doDeleteStaff() {
        Scanner scanner = new Scanner(System.in);
        Long staffId = 0L;
        
        while (true) {
            System.out.println("*** CARS :: Administration Operation :: Staff Management :: Delete Staff ***\n");
            System.out.print("Enter Staff ID to delete> ");
            staffId = scanner.nextLong();
            
            if (staffId > 0) {
                break;
            } else {
                System.out.println("Staff ID cannot be empty!\n");
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
                System.out.println(ex.getMessage());
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
