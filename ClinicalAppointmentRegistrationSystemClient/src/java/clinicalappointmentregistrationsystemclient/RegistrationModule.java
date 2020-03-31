/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicalappointmentregistrationsystemclient;

import ejb.session.stateless.ComputationSessionBeanRemote;
import ejb.session.stateless.DoctorEntitySessionBeanRemote;
import ejb.session.stateless.PatientEntitySessionBeanRemote;
import entity.DoctorEntity;
import entity.PatientEntity;
import entity.StaffEntity;
import java.sql.Time;
import java.sql.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import util.exception.InvalidInputException;
import util.exception.PatientExistException;

/**
 *
 * @author p.tm
 */
public class RegistrationModule {

    private PatientEntitySessionBeanRemote patientEntitySessionBeanRemote;
    private DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote;
    private ComputationSessionBeanRemote computationSessionBeanRemote;
    private StaffEntity currStaffEntity;

    public RegistrationModule() {
    }

    public RegistrationModule(
            PatientEntitySessionBeanRemote patientEntitySessionBeanRemote,
            DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote,
            StaffEntity staffEntity,
            ComputationSessionBeanRemote computationSessionBeanRemote) {

        this.patientEntitySessionBeanRemote = patientEntitySessionBeanRemote;
        this.doctorEntitySessionBeanRemote = doctorEntitySessionBeanRemote;
        this.currStaffEntity = staffEntity;
        this.computationSessionBeanRemote = computationSessionBeanRemote;
    }

    public void registrationOperation() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("\n*** CARS :: Registration Operation ***\n");
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
                        System.out.println("Error: " + ex.getMessage());
                    }
                } else if (response == 2) {
                    registerWalkInConsult();
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

        System.out.println("\n*** CARS :: Registration Operation :: Register New Patient ***\n");

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
                sc.nextLine();
                if (input > 0 && input < 120) {
                    pe.setAge(input);
                    break;
                } else {
                    System.out.println("Error! Invalid Age Entered\n");
                    System.out.print("Re-enter Age> ");
                }
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

    public void registerWalkInConsult() {

        Scanner sc = new Scanner(System.in);
        System.out.println("\n*** CARS :: Registration Operation :: Register New Patient ***\n");

        // get current date
        Date currentDate = new Date(System.currentTimeMillis());
        
        // get docs not on leave
        List<DoctorEntity> doctors = doctorEntitySessionBeanRemote.retrieveDoctorsOnDuty();

        System.out.println("Doctor:");
        System.out.printf("%-3s|%-64s\n", "Id", "Name");
        for (DoctorEntity de : doctors) {
            System.out.printf("%-3s|%-64s\n", de.getDoctorId(), de.getFullName());
        }
        System.out.println("\nAvailability: ");
        System.out.print("Time  |");
        doctors.forEach(doc -> {
            System.out.print(doc.getDoctorId() + "  |");
        });
        System.out.println();
        List<Time> nextSixTimeSlots = computationSessionBeanRemote.getNextSixTimeSlots();
        nextSixTimeSlots.forEach(time -> {
            System.out.print(time.toString().substring(0,5) + "|");
            doctors.forEach(doc -> {
                if (doctorEntitySessionBeanRemote.isAvailableAtTimeDate(doc, time, currentDate)) {
                    System.out.println("O  |");
                } else {
                    System.out.println("X  |");
                }
            });
            System.out.println();
        });

    }

}
