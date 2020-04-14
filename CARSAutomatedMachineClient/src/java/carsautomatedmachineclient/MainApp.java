/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carsautomatedmachineclient;

import java.util.InputMismatchException;
import java.util.Scanner;
import ws.client.InvalidLoginCredentialException;
import ws.client.InvalidLoginCredentialException_Exception;
import ws.client.PatientEntity;
import ws.client.PatientExistException_Exception;
import ws.client.PatientNotFoundException_Exception;

/**
 *
 * @author p.tm
 */
public class MainApp {

    // current logged in patient
    private PatientEntity currentPatientEntity;
    private AppointmentServiceModule appointmentServiceModule;

    public MainApp() {
    }

    public void runApp() throws InputMismatchException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to AMS Client ***\n");
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
                        System.out.println("Registration Successful!\n");
                    } catch (PatientExistException_Exception ex) {
                        System.out.println(ex.getMessage());
                    }

                } else if (response == 2) {
                    try {
                        doLogin();
                        System.out.println("Login Successful!");
                        appointmentServiceModule = new AppointmentServiceModule(currentPatientEntity);
                        appointmentServiceModule.appointmentServiceOperation();
                    } catch (InvalidLoginCredentialException_Exception ex) {
                        System.out.println(ex.getMessage());
                    } catch (PatientNotFoundException_Exception ex) {
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

    public void doLogin() throws InvalidLoginCredentialException_Exception, PatientNotFoundException_Exception {
        Scanner scanner = new Scanner(System.in);
        String patientId = "";
        String password = "";

        System.out.println("\n*** AMS Client :: Login ***\n");
        System.out.print("Enter Identity Number> ");
        patientId = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (patientId.length() > 0 && password.length() > 0) {
            currentPatientEntity = patientLogin(patientId, password);
        } else {
            throw new InvalidLoginCredentialException_Exception("Missing login credentials!", new InvalidLoginCredentialException());
        }
    }

    public void doRegister() throws PatientExistException_Exception {

        Scanner sc = new Scanner(System.in);
        System.out.println("\n*** Welcome to AMS Client :: Register ***\n");

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
                if (input.length() == 6) {
                    Long num = Long.parseLong(input);
//                    String hashPassword = EncryptionHelper.getInstance().byteArrayToHexString(EncryptionHelper.getInstance().doMD5Hashing(input));
                    patientEntity.setPassword(input);
                    break;
                } else {
                    System.out.println("Error! Password cannot be empty. Password has to be a 6-digit number!\n");
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
                    if (input.equalsIgnoreCase("F") || input.equalsIgnoreCase("M")) {
                        patientEntity.setGender(input);
                        break;
                    } else {
                        System.out.println("Gender has to be either male (M) or female (F)!\n");
                        System.out.print("Re-enter Gender> ");
                    }
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
                    Long num = Long.parseLong(input);
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

            addNewPatient(patientEntity);

        } catch (IllegalArgumentException | InputMismatchException ex) {
            System.out.println("Invalid input! Age, phone and password has to be a number and password has to be a 6-digit number. Please try again!");
        }

    }

    private static void addNewPatient(ws.client.PatientEntity arg0) throws ws.client.PatientExistException_Exception {
        ws.client.CARSWebService service = new ws.client.CARSWebService();
        ws.client.CARSRemoteUserWebService port = service.getCARSRemoteUserWebServicePort();
        port.addNewPatient(arg0);
    }

    private static ws.client.PatientEntity patientLogin(java.lang.String arg0, java.lang.String arg1) throws ws.client.InvalidLoginCredentialException_Exception, ws.client.PatientNotFoundException_Exception {
        ws.client.CARSWebService service = new ws.client.CARSWebService();
        ws.client.CARSRemoteUserWebService port = service.getCARSRemoteUserWebServicePort();
        return port.patientLogin(arg0, arg1);
    }

}
