/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicalappointmentregistrationsystemclient;

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
    
    private StaffEntity currentStaffEntity;

    public MainApp() {
    }

    public MainApp(StaffEntitySessionBeanRemote staffEntitySessionBeanRemote) {
        this.staffEntitySessionBeanRemote = staffEntitySessionBeanRemote;
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
        
        System.out.println("*** CARS :: Login ***\n");
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
        
    }
}
