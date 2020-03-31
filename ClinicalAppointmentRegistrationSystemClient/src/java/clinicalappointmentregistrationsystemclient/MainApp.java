/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicalappointmentregistrationsystemclient;

import ejb.session.singleton.ComputationSessionBeanRemote;
import ejb.session.singleton.ConsultationSessionBeanRemote;
import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.DoctorEntitySessionBeanRemote;
import ejb.session.stateless.PatientEntitySessionBeanRemote;
import ejb.session.stateless.StaffEntitySessionBeanRemote;
import entity.StaffEntity;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author crs
 */
public class MainApp {
    
    private StaffEntitySessionBeanRemote staffEntitySessionBeanRemote;
    private DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote;
    private PatientEntitySessionBeanRemote patientEntitySessionBeanRemote;
    private ComputationSessionBeanRemote computationSessionBeanRemote;
    private ConsultationSessionBeanRemote consultationSessionBeanRemote;
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    
    private AdministrationModule administrationModule;
    private RegistrationModule registrationModule;
    
    private StaffEntity currentStaffEntity;

    public MainApp() {
    }

    public MainApp(
            StaffEntitySessionBeanRemote staffEntitySessionBeanRemote, 
            DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote, 
            PatientEntitySessionBeanRemote patientEntitySessionBeanRemote,
            ComputationSessionBeanRemote computationSessionBeanRemote,
            ConsultationSessionBeanRemote consultationSessionBeanRemote,
            AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote) {
        this.staffEntitySessionBeanRemote = staffEntitySessionBeanRemote;
        this.doctorEntitySessionBeanRemote = doctorEntitySessionBeanRemote;
        this.patientEntitySessionBeanRemote = patientEntitySessionBeanRemote;
        this.computationSessionBeanRemote = computationSessionBeanRemote;
        this.consultationSessionBeanRemote = consultationSessionBeanRemote;
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
    }
    
    
    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("*** Welcome to Clinic Appointment Registration System (CARS) ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;
            
            while(response < 1 || response > 2) {
                System.out.print("> ");

                response = scanner.nextInt();
                
                if (response == 1) {
                    try {
                        doLogin();
                        System.out.println("Login successful!\n");
                        
                        administrationModule = new AdministrationModule(staffEntitySessionBeanRemote, doctorEntitySessionBeanRemote, patientEntitySessionBeanRemote, currentStaffEntity);
                        registrationModule = new RegistrationModule(
                                patientEntitySessionBeanRemote, 
                                doctorEntitySessionBeanRemote, 
                                currentStaffEntity, 
                                computationSessionBeanRemote,
                                consultationSessionBeanRemote,
                                appointmentEntitySessionBeanRemote
                        );
                        menuMain();
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    break;
                } else {
                   System.out.println("Invalid option, please try again!\n"); 
                }
            }
            
            if (response == 2) {
                break;
            }
        }
    }
    
    private void doLogin()throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("\n*** CARS :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0) {
            currentStaffEntity = staffEntitySessionBeanRemote.staffLogin(username, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credentials!");
        }
    }
    
    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("*** CARS :: Main ***\n");
            System.out.println("You are login as " + currentStaffEntity.getFirstName() + " " + currentStaffEntity.getLastName() + "\n");
            System.out.println("1: Registration Operation");
            System.out.println("2: Appointment Operation");
            System.out.println("3: Administration Operation");
            System.out.println("4: Logout\n");
            response = 0;
            
             while(response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();
                
                if (response == 1) {
                    registrationModule.registrationOperation();
                } else if (response == 2) {
                    System.out.println("to add");
                } else if (response == 3) {
                    administrationModule.administrationOperation();
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
}
