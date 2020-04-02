/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carsselfservicekiosk;

import ejb.session.singleton.ComputationSessionBeanRemote;
import ejb.session.singleton.ConsultationSessionBeanRemote;
import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.DoctorEntitySessionBeanRemote;
import ejb.session.stateless.PatientEntitySessionBeanRemote;
import entity.PatientEntity;
import java.util.InputMismatchException;
import java.util.Scanner;
import util.exception.InvalidInputException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PatientExistException;
import util.exception.PatientNotFoundException;

/**
 *
 * @author p.tm
 */
public class MainApp {

    // session beans
    private DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote;
    private PatientEntitySessionBeanRemote patientEntitySessionBeanRemote;
    private ComputationSessionBeanRemote computationSessionBeanRemote;
    private ConsultationSessionBeanRemote consultationSessionBeanRemote;
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;

    // current logged in patient
    private PatientEntity currentPatientEntity;

    // modules
    private SelfServiceModule selfServiceModule;

    public MainApp() {
    }

    public MainApp(
            DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote,
            PatientEntitySessionBeanRemote patientEntitySessionBeanRemote,
            ComputationSessionBeanRemote computationSessionBeanRemote,
            ConsultationSessionBeanRemote consultationSessionBeanRemote,
            AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote) {
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
            System.out.println("*** Welcome to Self-Service Kiosk ***\n");
            System.out.println("1: Register");
            System.out.println("2: Login");
            System.out.println("3: Exit\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        doRegister();
                    } catch (InvalidInputException ex) {
                        System.out.println("Invalid input!");
                    } catch (PatientExistException ex) {
                        System.out.println(ex.getMessage());
                    }
                    System.out.println("Registration Successful!\n");
                } else if (response == 2) {
                    try {
                        doLogin();
                        System.out.println("Login Successful!");
                        selfServiceModule = new SelfServiceModule(
                                doctorEntitySessionBeanRemote,
                                computationSessionBeanRemote,
                                consultationSessionBeanRemote,
                                appointmentEntitySessionBeanRemote,
                                currentPatientEntity);
                        selfServiceModule.selfServiceOperation();
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println(ex.getMessage());
                    } catch (PatientNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 3) {
                break;
            }
        }
    }

    public void doLogin() throws InvalidLoginCredentialException, PatientNotFoundException {
        Scanner scanner = new Scanner(System.in);
        String patientId = "";
        String password = "";

        System.out.println("\n*** Self-Service Kiosk :: Login ***\n");
        System.out.print("Enter Identity Number> ");
        patientId = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (patientId.length() > 0 && password.length() > 0) {
            currentPatientEntity = patientEntitySessionBeanRemote.patientLogin(patientId, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credentials!");
        }
    }

    public void doRegister() throws InvalidInputException, PatientExistException {

        Scanner sc = new Scanner(System.in);
        System.out.println("\n*** Welcome to Self-Service Kiosk :: Register ***\n");

        PatientEntity patientEntity = new PatientEntity();

        try {

            System.out.print("Enter Identity Number> ");
            while (true) {
                String input = sc.nextLine().trim();
                if (input.length() > 0) {
                    patientEntity.setIdentityNumber(input);
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
                    patientEntity.setPassword(input);
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
                    patientEntity.setFirstName(input);
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
                    patientEntity.setLastName(input);
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
                    patientEntity.setGender(input);
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
                    patientEntity.setAge(input);
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
                    patientEntity.setPhone(input);
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
                    patientEntity.setAddress(input);
                    break;
                } else {
                    System.out.println("Error! Address cannot be empty\n");
                    System.out.print("Re-enter Address> ");
                }

            }

            patientEntitySessionBeanRemote.addNewPatient(patientEntity);

        } catch (InputMismatchException ex) {
            throw new InvalidInputException("Invalid Identity Number!");
        }

    }
}
