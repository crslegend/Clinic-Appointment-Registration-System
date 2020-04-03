/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicalappointmentregistrationsystemclient;

import ejb.session.singleton.ComputationSessionBeanRemote;
import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.DoctorEntitySessionBeanRemote;
import ejb.session.stateless.PatientEntitySessionBeanRemote;
import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.PatientEntity;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import util.exception.AppointmentInvalidException;
import util.exception.AppointmentNotFoundException;
import util.exception.ClinicNotOpenException;
import util.exception.DoctorNotFoundException;
import util.exception.InvalidInputException;
import util.exception.PatientNotFoundException;

/**
 *
 * @author p.tm
 */
public class AppointmentModule {

    private PatientEntitySessionBeanRemote patientEntitySessionBeanRemote;
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote;
    private ComputationSessionBeanRemote computationSessionBeanRemote;

    public AppointmentModule() {
    }

    public AppointmentModule(
            PatientEntitySessionBeanRemote patientEntitySessionBeanRemote,
            AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote,
            DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote,
            ComputationSessionBeanRemote computationSessionBeanRemote) {
        this.patientEntitySessionBeanRemote = patientEntitySessionBeanRemote;
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.doctorEntitySessionBeanRemote = doctorEntitySessionBeanRemote;
        this.computationSessionBeanRemote = computationSessionBeanRemote;
    }

    public void appointmentOperation() {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("\n*** CARS :: Appointment Operation ***\n");
            System.out.println("1: View Patient Appointments");
            System.out.println("2: Add Appointment");
            System.out.println("3: Cancel Appointment");
            System.out.println("4: Back\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        viewPatientAppointments();
                    } catch (PatientNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if (response == 2) {
                    try {
                        addNewAppointment();
                    } catch (DoctorNotFoundException | InvalidInputException | PatientNotFoundException | AppointmentInvalidException ex) {
                        System.out.println(ex.getMessage());
                    } catch (IllegalArgumentException ex) {
                        System.out.println("Invalid Input!");
                    } catch (InputMismatchException ex) {
                        System.out.println("Invalid Input!");
                    } catch (ClinicNotOpenException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if (response == 3) {
                    try {
                        cancelAppointment();
                    } catch (PatientNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    } catch (AppointmentNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    }
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

    public void viewPatientAppointments() throws PatientNotFoundException {

        Scanner sc = new Scanner(System.in);
        System.out.println("\n*** CARS :: Appointment Operation :: View Patient Appointments ***\n");

        System.out.print("Enter Patient Identity Number> ");
        String pId = "";
        while (true) {
            pId = sc.nextLine().trim();
            // input validation
            if (pId.length() > 0) {
                break;
            }
            System.out.println("Invalid input!\n");
            System.out.print("Re-enter Patient Identity Number> ");
        }

        PatientEntity patientEntity = patientEntitySessionBeanRemote.retrievePatientByIdNum(pId);
        List<AppointmentEntity> appointments = appointmentEntitySessionBeanRemote.retrieveListOfAppointmentsByPatientId(patientEntity.getPatientId());

        System.out.println("\nAppointments: ");
        System.out.printf("%-3s|%-11s|%-6s|%-64s\n", "Id", "Date", "Time", "Doctor");
        appointments.forEach(appt -> {
            System.out.printf("%-3s|%-11s|%-6s|%-64s\n", appt.getAppointmentId(),
                    appt.getDate().toString(),
                    appt.getStartTime().toString().substring(0, 5),
                    appt.getDoctorEntity().getFullName());
        });

    }

    public void addNewAppointment() throws DoctorNotFoundException, IllegalArgumentException, InvalidInputException, PatientNotFoundException, AppointmentInvalidException, InputMismatchException, ClinicNotOpenException {

        Scanner sc = new Scanner(System.in);
        System.out.println("\n*** CARS :: Appointment Operation :: Add Appointment ***\n");

        List<DoctorEntity> doctors = doctorEntitySessionBeanRemote.retrieveAllDoctors();

        System.out.println("Doctor:");
        System.out.printf("%-3s|%-64s\n", "Id", "Name");
        doctors.forEach(doc -> {
            System.out.printf("%-3s|%-64s\n", doc.getDoctorId(), doc.getFullName());
        });
        System.out.println();

        System.out.print("Enter Doctor Id> ");
        DoctorEntity doctorEntity = doctorEntitySessionBeanRemote.retrieveDoctorById(sc.nextLong());
        sc.nextLine();

        System.out.print("Enter Date> ");
        Date date = Date.valueOf(sc.nextLine().trim()); // "2020-05-20"
        if (!doctorEntitySessionBeanRemote.isAvailableAtDate(doctorEntity, date)) {
            throw new InvalidInputException("Doctor is not available!");
        }
        if (date.before(Date.valueOf(LocalDate.now().plusDays(2)))) {
            throw new InvalidInputException("Appointment should be booked 2 days in advance!");
        }
        System.out.println();

        System.out.println("Availability for " + doctorEntity.getFullName() + " on " + date.toString() + ":");
        List<Time> allTimeSlots = computationSessionBeanRemote.getAllTimeSlots(date);
        for (int i = 0; i < allTimeSlots.size(); i++) {
            Time time = allTimeSlots.get(i);
            if (!doctorEntitySessionBeanRemote.isAvailableAtTimeDate(doctorEntity, time, date)) {
                allTimeSlots.remove(i);
            }
        }
        allTimeSlots.forEach(time -> {
            System.out.print(time.toString().substring(0, 5) + " ");
        });
        System.out.println("\n");

        System.out.print("Enter Time> ");
        Time time = Time.valueOf(sc.nextLine().trim() + ":00");
        if (!allTimeSlots.contains(time)) {
            throw new InvalidInputException("Time is invalid!");
        }
        System.out.print("Enter Patient Identity Number> ");
        PatientEntity patientEntity = patientEntitySessionBeanRemote.retrievePatientByIdNum(sc.nextLine().trim());

        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setDate(date);
        appointmentEntity.setDoctorEntity(doctorEntity);
        appointmentEntity.setPatientEntity(patientEntity);
        appointmentEntity.setStartTime(time);

        appointmentEntitySessionBeanRemote.createNewAppointment(appointmentEntity);
        System.out.println(patientEntity.getFullName()
                + " appointment with "
                + doctorEntity.getFullName()
                + " at " + time.toString().substring(0, 5)
                + " on " + date.toString() + " has been added.");

    }

    public void cancelAppointment() throws PatientNotFoundException, AppointmentNotFoundException {

        Scanner sc = new Scanner(System.in);
        System.out.println("\n*** CARS :: Appointment Operation :: Add Appointment ***\n");

        System.out.print("Enter Patient Identity Number> ");
        String pId = sc.nextLine().trim();
        PatientEntity patientEntity = patientEntitySessionBeanRemote.retrievePatientByIdNum(pId);
        List<AppointmentEntity> appointments = appointmentEntitySessionBeanRemote.retrieveListOfAppointmentsByPatientId(patientEntity.getPatientId());

        System.out.println("\nAppointments: ");
        System.out.printf("%-3s|%-11s|%-6s|%-64s\n", "Id", "Date", "Time", "Doctor");
        appointments.forEach(appt -> {
            System.out.printf("%-3s|%-11s|%-6s|%-64s\n", appt.getAppointmentId(),
                    appt.getDate().toString(),
                    appt.getStartTime().toString().substring(0, 5),
                    appt.getDoctorEntity().getFullName());
        });
        System.out.println();

        System.out.print("Enter Appointment Id> ");
        AppointmentEntity appointmentEntity = appointmentEntitySessionBeanRemote.retrieveAppointmentById(sc.nextLong());
        sc.nextLine();
        if (!appointments.contains(appointmentEntity)) {
            throw new AppointmentNotFoundException("Oi! Don't delete other people appointment hor");
        }
        appointmentEntitySessionBeanRemote.cancelAppointment(appointmentEntity.getAppointmentId());
        System.out.println(appointmentEntity.getPatientEntity().getFullName()
                + " appointment with "
                + appointmentEntity.getDoctorEntity().getFullName()
                + " at " + appointmentEntity.getStartTime().toString().substring(0, 5)
                + " on " + appointmentEntity.getDate().toString() + " has been cancelled.");
    }

}
