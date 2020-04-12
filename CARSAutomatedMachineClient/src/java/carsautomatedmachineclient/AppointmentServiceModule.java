/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carsautomatedmachineclient;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import ws.client.AlreadyBookedAppointment_Exception;
import ws.client.AppointmentEntity;
import ws.client.AppointmentInvalidException_Exception;
import ws.client.AppointmentNotFoundException_Exception;
import ws.client.ClinicNotOpenException_Exception;
import ws.client.DoctorEntity;
import ws.client.DoctorNotFoundException_Exception;
import ws.client.PatientEntity;

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
                         try {
                            addNewAppointment();
                        } catch (DoctorNotFoundException_Exception | AppointmentInvalidException_Exception | ClinicNotOpenException_Exception | AlreadyBookedAppointment_Exception ex) {
                            System.out.println(ex.getMessage());
                        } catch (IllegalArgumentException | InputMismatchException ex) {
                            System.out.println("Invalid Input!");
                        } 
                        ;
                    } else if (response == 3) {
                        try {
                            cancelAppointment();
                        } catch (AppointmentNotFoundException_Exception ex) {
                            System.out.println(ex.getMessage());
                        };
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
    
    public void addNewAppointment() throws DoctorNotFoundException_Exception, IllegalArgumentException, AppointmentInvalidException_Exception, InputMismatchException, ClinicNotOpenException_Exception, AlreadyBookedAppointment_Exception {

        Scanner sc = new Scanner(System.in);
        System.out.println("\n*** Self-Service Kiosk :: Add Appointment ***\n");

        List<DoctorEntity> doctors = retrieveAllDoctors();

        System.out.println("Doctor:");
        System.out.printf("%-3s|%-64s\n", "Id", "Name");
        doctors.forEach(doc -> {
            System.out.printf("%-3s|%-64s\n", doc.getDoctorId(), doc.getFirstName() + " " + doc.getLastName());
        });
        System.out.println();

        System.out.print("Enter Doctor Id> ");
        DoctorEntity doctorEntity = retrieveDoctorById(sc.nextLong());
        sc.nextLine();

        System.out.print("Enter Date> ");
        Date date = Date.valueOf(sc.nextLine().trim());
        if (!isAvailableAtDate(doctorEntity.getDoctorId(), date.toString())) {
            System.out.println("Doctor is not available!");
            return;
        }
        if (date.before(Date.valueOf(LocalDate.now().plusDays(2)))) {
            System.out.println("Appointment should be booked 2 days in advance!");
            return;
        }
        if (hasAppointmentOnDay(currentPatientEntity.getPatientId(), date.toString())) {
            System.out.println("Patient already has appointment on " + date.toString());
            return;
        }
        System.out.println();

        System.out.println("Availability for " + doctorEntity.getFirstName() + " " + doctorEntity.getLastName() + " on " + date.toString() + ":");
        List<String> allTimeSlots = getAllTimeSlots(date.toString());
        for (int i = 0; i < allTimeSlots.size(); i++) {
            String time = allTimeSlots.get(i);
            if (!isAvailableAtTimeDate(doctorEntity.getDoctorId(), time, date.toString())) {
                allTimeSlots.remove(i);
            }
        }
        allTimeSlots.forEach(time -> {
            System.out.print(time.toString().substring(0, 5) + " ");
        });
        System.out.println("\n");

        System.out.print("Enter Time> ");
        Time time = Time.valueOf(sc.nextLine().trim() + ":00");
        if (!allTimeSlots.contains(time.toString())) {
            System.out.println("Time is invalid!");
            return;
        }

        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setDate(date.toString());
        appointmentEntity.setDoctorEntity(doctorEntity);
        appointmentEntity.setPatientEntity(currentPatientEntity);
        appointmentEntity.setStartTime(time.toString());

        createNewAppointment(date.toString(), doctorEntity.getDoctorId(), currentPatientEntity.getPatientId(), time.toString());
        System.out.println(currentPatientEntity.getFirstName() + " "  + currentPatientEntity.getLastName()
                + " appointment with "
                + doctorEntity.getFirstName() + " " + doctorEntity.getLastName()
                + " at " + time.toString().substring(0, 5)
                + " on " + date.toString() + " has been added.");

    }
    
    public void cancelAppointment() throws AppointmentNotFoundException_Exception, IllegalArgumentException, InputMismatchException {

        Scanner sc = new Scanner(System.in);
        System.out.println("\n*** Self-Service Kiosk :: Cancel Appointment ***\n");

        List<AppointmentEntity> appointments = retrieveListOfAppointmentsByPatientId(currentPatientEntity.getPatientId());

        System.out.println("\nAppointments: ");
        System.out.printf("%-3s|%-11s|%-6s|%-64s\n", "Id", "Date", "Time", "Doctor");
        appointments.forEach(appt -> {
            System.out.printf("%-3s|%-11s|%-6s|%-64s\n", appt.getAppointmentId(),
                    appt.getDate().toString(),
                    appt.getStartTime().toString().substring(0, 5),
                    appt.getDoctorEntity().getFirstName() + " " + appt.getDoctorEntity().getLastName());
        });
        System.out.println();

        System.out.print("Enter Appointment Id> ");
        AppointmentEntity appointmentEntity = retrieveAppointmentById(sc.nextLong());
        sc.nextLine();
        if (appointments.contains(appointmentEntity)) {
            System.out.println("Oi! Don't delete other people appointment hor");
            return;
        }
        cancelAppointment(appointmentEntity.getAppointmentId());
        System.out.println(appointmentEntity.getPatientEntity().getFirstName() + " " + appointmentEntity.getPatientEntity().getLastName()
                + " appointment with "
                + appointmentEntity.getDoctorEntity().getFirstName() + " " + appointmentEntity.getDoctorEntity().getLastName()
                + " at " + appointmentEntity.getStartTime().toString().substring(0, 5)
                + " on " + appointmentEntity.getDate().toString() + " has been cancelled.");
    }

    private static java.util.List<ws.client.AppointmentEntity> retrieveListOfAppointmentsByPatientId(long arg0) {
        ws.client.AppointmentWebService_Service service = new ws.client.AppointmentWebService_Service();
        ws.client.AppointmentWebService port = service.getAppointmentWebServicePort();
        return port.retrieveListOfAppointmentsByPatientId(arg0);
    }

    private static java.util.List<ws.client.DoctorEntity> retrieveAllDoctors() {
        ws.client.AppointmentWebService_Service service = new ws.client.AppointmentWebService_Service();
        ws.client.AppointmentWebService port = service.getAppointmentWebServicePort();
        return port.retrieveAllDoctors();
    }

    private static DoctorEntity retrieveDoctorById(long arg0) throws DoctorNotFoundException_Exception {
        ws.client.AppointmentWebService_Service service = new ws.client.AppointmentWebService_Service();
        ws.client.AppointmentWebService port = service.getAppointmentWebServicePort();
        return port.retrieveDoctorById(arg0);
    }

    private static java.util.List<String> getAllTimeSlots(java.lang.String arg0) throws ClinicNotOpenException_Exception {
        ws.client.AppointmentWebService_Service service = new ws.client.AppointmentWebService_Service();
        ws.client.AppointmentWebService port = service.getAppointmentWebServicePort();
        return port.getAllTimeSlots(arg0);
    }

    private static AppointmentEntity retrieveAppointmentById(java.lang.Long arg0) throws AppointmentNotFoundException_Exception {
        ws.client.AppointmentWebService_Service service = new ws.client.AppointmentWebService_Service();
        ws.client.AppointmentWebService port = service.getAppointmentWebServicePort();
        return port.retrieveAppointmentById(arg0);
    }

    private static AppointmentEntity cancelAppointment(java.lang.Long arg0) throws AppointmentNotFoundException_Exception {
        ws.client.AppointmentWebService_Service service = new ws.client.AppointmentWebService_Service();
        ws.client.AppointmentWebService port = service.getAppointmentWebServicePort();
        return port.cancelAppointment(arg0);
    }

    private static Boolean isAvailableAtDate(long arg0, java.lang.String arg1) throws DoctorNotFoundException_Exception {
        ws.client.AppointmentWebService_Service service = new ws.client.AppointmentWebService_Service();
        ws.client.AppointmentWebService port = service.getAppointmentWebServicePort();
        return port.isAvailableAtDate(arg0, arg1);
    }

    private static Boolean isAvailableAtTimeDate(long arg0, java.lang.String arg1, java.lang.String arg2) throws DoctorNotFoundException_Exception {
        ws.client.AppointmentWebService_Service service = new ws.client.AppointmentWebService_Service();
        ws.client.AppointmentWebService port = service.getAppointmentWebServicePort();
        return port.isAvailableAtTimeDate(arg0, arg1, arg2);
    }

    private static Boolean hasAppointmentOnDay(long arg0, java.lang.String arg1) {
        ws.client.AppointmentWebService_Service service = new ws.client.AppointmentWebService_Service();
        ws.client.AppointmentWebService port = service.getAppointmentWebServicePort();
        return port.hasAppointmentOnDay(arg0, arg1);
    }

    private static void createNewAppointment(java.lang.String arg0, long arg1, long arg2, java.lang.String arg3) throws AppointmentInvalidException_Exception, AlreadyBookedAppointment_Exception {
        ws.client.AppointmentWebService_Service service = new ws.client.AppointmentWebService_Service();
        ws.client.AppointmentWebService port = service.getAppointmentWebServicePort();
        port.createNewAppointment(arg0, arg1, arg2, arg3);
    }
    
}
