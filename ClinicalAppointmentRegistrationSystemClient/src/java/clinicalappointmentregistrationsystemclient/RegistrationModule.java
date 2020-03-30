/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicalappointmentregistrationsystemclient;

import ejb.session.stateless.PatientEntitySessionBeanRemote;
import entity.PatientEntity;
import entity.StaffEntity;
import java.util.InputMismatchException;
import java.util.Scanner;
import util.exception.InvalidInputException;
import util.exception.PatientExistException;

/**
 *
 * @author p.tm
 */
public class RegistrationModule {

    private PatientEntitySessionBeanRemote patientEntitySessionBeanRemote;
    private StaffEntity currStaffEntity;

    public RegistrationModule() {
    }

    public RegistrationModule(
            PatientEntitySessionBeanRemote patientEntitySessionBeanRemote,
            StaffEntity staffEntity) {

        this.patientEntitySessionBeanRemote = patientEntitySessionBeanRemote;
        this.currStaffEntity = staffEntity;

    }

    public void registrationOperation() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** CARS :: Registration Operation ***\n");
            System.out.println("1: Register New Patient");
            System.out.println("2: Register Walk-In Consultation");
            System.out.println("3: Register Consultation By Appointment");
            System.out.println("4: Back\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        registerNewPatient();
                        System.out.println("Patient has been registered successfully!");
                    } catch (InvalidInputException | PatientExistException ex) {
                        System.out.println("Error: " + ex);
                    }
                } else if (response == 2) {
                    break;
                } else if (response == 3) {
                    break;
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

    public void registerNewPatient() throws InvalidInputException, PatientExistException {

        PatientEntity pe = new PatientEntity();
        Scanner sc = new Scanner(System.in);

        System.out.println("*** CARS :: Registration Operation :: Register New Patient ***\n");

        try {

            System.out.print("Enter Identity Number> ");
            while (true) {
                String input = sc.nextLine().trim();
                if (input.length() > 0) {
                    pe.setIdentityNumber(input);
                    break;
                } else {
                    System.out.println("Error! Identity Number cannot be empty\n");
                    System.out.print("Re-enter Identity Number> ");
                }

            }

            System.out.print("Enter Password> ");
            while (true) {
                String input = sc.nextLine().trim();
                if (input.length() > 0) {
                    pe.setPassword(input);
                    break;
                } else {
                    System.out.println("Error! Password cannot be empty\n");
                    System.out.print("Re-enter Password> ");
                }

            }

            System.out.print("Enter First Name> ");
            while (true) {
                String input = sc.nextLine().trim();
                if (input.length() > 0) {
                    pe.setFirstName(input);
                    break;
                } else {
                    System.out.println("Error! First Name cannot be empty\n");
                    System.out.print("Re-enter First Name> ");
                }

            }

            System.out.print("Enter Last Name> ");
            while (true) {
                String input = sc.nextLine().trim();
                if (input.length() > 0) {
                    pe.setLastName(input);
                    break;
                } else {
                    System.out.println("Error! Last Name cannot be empty\n");
                    System.out.print("Re-enter Last Name> ");
                }

            }

            System.out.print("Gender> ");
            while (true) {
                String input = sc.nextLine().trim();
                if (input.length() > 0) {
                    pe.setGender(input);
                    break;
                } else {
                    System.out.println("Error! Gender cannot be empty\n");
                    System.out.print("Re-enter Gender> ");
                }

            }

            System.out.print("Enter Age> ");
            while (true) {
                int input = sc.nextInt();
                if (input > 0 && input < 120) {
                    pe.setAge(input);
                    break;
                } else {
                    System.out.println("Error! Invalid Age Entered\n");
                    System.out.print("Re-enter Age> ");
                }
                sc.nextLine();
            }

            System.out.print("Enter Phone> ");
            while (true) {
                String input = sc.nextLine().trim();
                if (input.length() > 0) {
                    pe.setPhone(input);
                    break;
                } else {
                    System.out.println("Error! Phone cannot be empty\n");
                    System.out.print("Re-enter Phone> ");
                }

            }

            System.out.print("Enter Address> ");
            while (true) {
                String input = sc.nextLine().trim();
                if (input.length() > 0) {
                    pe.setAddress(input);
                    break;
                } else {
                    System.out.println("Error! Address cannot be empty\n");
                    System.out.print("Re-enter Address> ");
                }

            }

            patientEntitySessionBeanRemote.addNewPatient(pe);

        } catch (InputMismatchException ex) {
            throw new InvalidInputException("Invalid Identity Number!");
        }

    }

}
