/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carsautomatedmachineclient;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import ws.client.appointmentop.AppointmentEntity;
import ws.client.appointmentop.AppointmentInvalidException;
import ws.client.appointmentop.AppointmentNotFoundException;
import ws.client.appointmentop.ClinicNotOpenException;
import ws.client.appointmentop.DoctorNotFoundException;
import ws.client.userop.PatientEntity;

/**
 *
 * @author p.tm
 */
public class AppointmentServiceModule {

    private PatientEntity currentPatientEntity;
    
    public AppointmentServiceModule() {
    }
    
    public AppointmentServiceModule(PatientEntity patientEntity) {
        this.currentPatientEntity = patientEntity;
    }
    
    public void appointmentServiceOperation() {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("\n*** Self-Service Kiosk :: Main ***\n");
            System.out.println("You are login as " + currentPatientEntity.getFirstName() + " " + currentPatientEntity.getLastName() + "\n");
            System.out.println("1: View Appointments");
            System.out.println("2: Add Appointment");
            System.out.println("3: Cancel Appointment");
            System.out.println("4: Logout\n");
            response = 0;

            try {
                while (response < 1 || response > 6) {
                    System.out.print("> ");
                    response = scanner.nextInt();

                    if (response == 1) {
                         viewPatientAppointments();
                    } else if (response == 2) {
//                         try {
//                            addNewAppointment();
//                        } catch (DoctorNotFoundException | AlreadyBookedAppointment ex) {
//                            System.out.println(ex.getMessage());
//                        } catch (IllegalArgumentException ex) {
//                            System.out.println("Invalid Input!");
//                        } catch (InvalidInputException ex) {
//                            System.out.println(ex.getMessage());
//                        } catch (AppointmentInvalidException ex) {
//                            System.out.println(ex.getMessage());
//                        } catch (InputMismatchException ex) {
//                            System.out.println("Invalid Input!");
//                        } catch (ClinicNotOpenException ex) {
//                            System.out.println(ex.getMessage());
//                        }
//                        ;
                    } else if (response == 3) {
//                        try {
//                            cancelAppointment();
//                        } catch (AppointmentNotFoundException ex) {
//                            System.out.println(ex.getMessage());
//                        };
                    } else if (response == 4) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                }
            } catch (IllegalArgumentException | InputMismatchException ex) {
                System.out.println("Invalid Input! Please try again");
                scanner.nextLine();
            }

            if (response == 4) {
                break;
            }
        }
    }
    
    public void viewPatientAppointments() {

        System.out.println("\n*** Self-Service Kiosk :: View Patient Appointments ***\n");

        List<AppointmentEntity> appointments = retrieveListOfAppointmentsByPatientId(currentPatientEntity.getPatientId());

        System.out.println("\nAppointments: ");
        System.out.printf("%-3s|%-11s|%-6s|%-64s\n", "Id", "Date", "Time", "Doctor");
        appointments.forEach(appt -> {
            System.out.printf("%-3s|%-11s|%-6s|%-64s\n", appt.getAppointmentId(),
                    appt.getDate(),
                    appt.getStartTime().toString().substring(0, 5),
                    appt.getDoctorEntity().getFirstName() + " " + appt.getDoctorEntity().getLastName());
        });

    }

    private static java.util.List<ws.client.appointmentop.AppointmentEntity> retrieveListOfAppointmentsByPatientId(long arg0) {
        ws.client.appointmentop.AppointmentWebService_Service service = new ws.client.appointmentop.AppointmentWebService_Service();
        ws.client.appointmentop.AppointmentWebService port = service.getAppointmentWebServicePort();
        return port.retrieveListOfAppointmentsByPatientId(arg0);
    }
    
}
