/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carsselfservicekiosk;

import ejb.session.singleton.ComputationSessionBeanRemote;
import ejb.session.singleton.ConsultationSessionBeanRemote;
import ejb.session.stateful.AppointmentEntitySessionBeanRemote;
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
import util.exception.AlreadyBookedAppointment;
import util.exception.AppointmentInvalidException;
import util.exception.AppointmentNotFoundException;
import util.exception.ClinicNotOpenException;
import util.exception.DoctorNotFoundException;
import util.exception.InvalidInputException;

/**
 *
 * @author p.tm
 */
public class SelfServiceModule {

    // session beans
    private DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote;
    private ComputationSessionBeanRemote computationSessionBeanRemote;
    private ConsultationSessionBeanRemote consultationSessionBeanRemote;
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private PatientEntitySessionBeanRemote patientEntitySessionBeanRemote;
    private PatientEntity currentPatientEntity;

    public SelfServiceModule() {
    }

    public SelfServiceModule(
            DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote,
            ComputationSessionBeanRemote computationSessionBeanRemote,
            ConsultationSessionBeanRemote consultationSessionBeanRemote,
            AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote,
            PatientEntitySessionBeanRemote patientEntitySessionBeanRemote,
            PatientEntity currentPatientEntity) {
        this.doctorEntitySessionBeanRemote = doctorEntitySessionBeanRemote;
        this.currentPatientEntity = currentPatientEntity;
        this.computationSessionBeanRemote = computationSessionBeanRemote;
        this.consultationSessionBeanRemote = consultationSessionBeanRemote;
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.patientEntitySessionBeanRemote = patientEntitySessionBeanRemote;
    }

    public void selfServiceOperation() {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("\n*** Self-Service Kiosk :: Main ***\n");
            System.out.println("You are login as " + currentPatientEntity.getFullName() + "\n");
            System.out.println("1: Register Walk-In Consultation");
            System.out.println("2: Register Consultation By Appointment");
            System.out.println("3: View Appointments");
            System.out.println("4: Add Appointment");
            System.out.println("5: Cancel Appointment");
            System.out.println("6: Logout\n");
            response = 0;

            try {
                while (response < 1 || response > 6) {
                    System.out.print("> ");
                    response = scanner.nextInt();

                    if (response == 1) {
                        try {
                            registerWalkInConsult();
                        } catch (DoctorNotFoundException | InvalidInputException | AlreadyBookedAppointment ex) {
                            System.out.println(ex.getMessage());
                        } catch (InputMismatchException | IllegalArgumentException ex) {
                            System.out.println("Invalid Input!");
                        } catch (ClinicNotOpenException ex) {
                            System.out.println(ex.getMessage());
                        }
                        ;
                    } else if (response == 2) {
                        try {
                            registerConsultByAppointment();
                        } catch (AppointmentNotFoundException ex) {
                            System.out.println(ex.getMessage());
                        };
                    } else if (response == 3) {
                        viewPatientAppointments();
                    } else if (response == 4) {
                        try {
                            addNewAppointment();
                        } catch (DoctorNotFoundException | AlreadyBookedAppointment ex) {
                            System.out.println(ex.getMessage());
                        } catch (IllegalArgumentException ex) {
                            System.out.println("Invalid Input!");
                        } catch (InvalidInputException ex) {
                            System.out.println(ex.getMessage());
                        } catch (AppointmentInvalidException ex) {
                            System.out.println(ex.getMessage());
                        } catch (InputMismatchException ex) {
                            System.out.println("Invalid Input!");
                        } catch (ClinicNotOpenException ex) {
                            System.out.println(ex.getMessage());
                        }
                        ;
                    } else if (response == 5) {
                        try {
                            cancelAppointment();
                        } catch (AppointmentNotFoundException ex) {
                            System.out.println(ex.getMessage());
                        };
                    } else if (response == 6) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                }
            } catch (IllegalArgumentException | InputMismatchException ex) {
                System.out.println("Invalid Input! Please try again");
                scanner.nextLine();
            }

            if (response == 6) {
                break;
            }
        }
    }

    public void registerWalkInConsult() throws DoctorNotFoundException, InvalidInputException, InputMismatchException, ClinicNotOpenException, AlreadyBookedAppointment {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n*** Self-Service Kiosk :: Register Walk-In Consultation ***\n");

        // get current date
        Date currentDate = new Date(System.currentTimeMillis());

        if (patientEntitySessionBeanRemote.hasAppointmentOnDay(currentPatientEntity, currentDate)) {
            throw new AlreadyBookedAppointment("Patient already has appointment on " + currentDate.toString());
        }

        // get docs not on leave
        List<DoctorEntity> doctors = doctorEntitySessionBeanRemote.retrieveDoctorsOnDuty();

        System.out.println("Doctor:");
        System.out.printf("%-3s|%-64s\n", "Id", "Name");
        doctors.forEach((de) -> {
            System.out.printf("%-3s|%-64s\n", de.getDoctorId(), de.getFullName());
        });
        System.out.println("\nAvailability: ");
        System.out.print("Time  |");
        doctors.forEach(doc -> {
            System.out.print(doc.getDoctorId() + "  |");
        });
        System.out.println();
        List<Time> nextSixTimeSlots = computationSessionBeanRemote.getNextSixTimeSlots();
        nextSixTimeSlots.forEach(time -> {
            System.out.print(time.toString().substring(0, 5) + " |");
            doctors.forEach(doc -> {
                try {
                    if (doctorEntitySessionBeanRemote.isAvailableAtTimeDate(doc, time, currentDate)) {
                        System.out.print("O  |");
                    } else {
                        System.out.print("X  |");
                    }
                } catch (DoctorNotFoundException ex) {
                    // do nothing
                }
            });
            System.out.println();
        });

        // instantiate
        boolean present = false;
        DoctorEntity currentDoctorEntity = null;
        Time apptTime = null;

        // read in doc id
        System.out.print("\nEnter Doctor Id> ");
        long docId = sc.nextLong();
        sc.nextLine();
        // check if doc id is present
        for (DoctorEntity de : doctors) {
            if (de.getDoctorId().equals(docId)) {
                present = true;
                currentDoctorEntity = de;
            }
        }
        if (!present) {
            throw new DoctorNotFoundException("Doctor is not available!");
        }

        for (Time time : nextSixTimeSlots) {
            if (doctorEntitySessionBeanRemote.isAvailableAtTimeDate(currentDoctorEntity, time, currentDate)) {
                apptTime = time;
                break;
            }
        }

        try {
            long queueNumber = consultationSessionBeanRemote.createNewConsultation(currentDoctorEntity, currentPatientEntity, apptTime, currentDate);
            System.out.println(currentPatientEntity.getFullName()
                    + " appointment is confirmed with Dr. "
                    + currentDoctorEntity.getFullName()
                    + " at "
                    + apptTime.toString().substring(0, 5) + ".");
            System.out.println("Queue Number is: " + queueNumber + ".");
        } catch (AppointmentInvalidException ex) {
            System.out.println("Something went wrong while creating appointment");
        }
    }

    public void registerConsultByAppointment() throws AppointmentNotFoundException, IllegalArgumentException, InputMismatchException {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n*** Self-Service Kiosk :: Register Consultation By Appointment ***\n");

        System.out.println("\nAppointments: ");
        System.out.printf("%-3s|%-11s|%-6s|%-64s\n", "Id", "Date", "Time", "Doctor");

        // get current date
        Date currentDate = Date.valueOf(LocalDate.now());
        
        List<AppointmentEntity> appointments = appointmentEntitySessionBeanRemote.retrieveListOfAppointmentsByPatientId(currentPatientEntity.getPatientId());
        appointments.forEach(appt -> {
            if (appt.getDate().toString().equals(currentDate.toString())) {
                System.out.printf("%-3s|%-11s|%-6s|%-64s\n", appt.getAppointmentId(),
                        appt.getDate().toString(),
                        appt.getStartTime().toString().substring(0, 5),
                        appt.getDoctorEntity().getFullName());
            }
        });

        System.out.print("\nEnter Appointment Id> ");
        long aId = sc.nextLong();
        sc.nextLine();
        AppointmentEntity appointmentEntity = null;

        for (AppointmentEntity appt : appointments) {
            if (appt.getAppointmentId() == aId) {
                appointmentEntity = appt;
            }
        }

        if (appointmentEntity == null) {
            throw new AppointmentNotFoundException("Appointment not found!");
        }

        System.out.println(currentPatientEntity.getFullName()
                + " appointment is confirmed with Dr. "
                + appointmentEntity.getDoctorEntity().getFullName()
                + " at "
                + appointmentEntity.getStartTime().toString().substring(0, 5) + ".");
        System.out.println("Queue Number is: " + consultationSessionBeanRemote.confirmConsultation() + ".");
    }

    public void viewPatientAppointments() {

        System.out.println("\n*** Self-Service Kiosk :: View Patient Appointments ***\n");

        List<AppointmentEntity> appointments = appointmentEntitySessionBeanRemote.retrieveListOfAppointmentsByPatientId(currentPatientEntity.getPatientId());

        System.out.println("\nAppointments: ");
        System.out.printf("%-3s|%-11s|%-6s|%-64s\n", "Id", "Date", "Time", "Doctor");
        appointments.forEach(appt -> {
            System.out.printf("%-3s|%-11s|%-6s|%-64s\n", appt.getAppointmentId(),
                    appt.getDate().toString(),
                    appt.getStartTime().toString().substring(0, 5),
                    appt.getDoctorEntity().getFullName());
        });

    }

    public void addNewAppointment() throws DoctorNotFoundException, IllegalArgumentException, InvalidInputException, AppointmentInvalidException, InputMismatchException, ClinicNotOpenException, AlreadyBookedAppointment {

        Scanner sc = new Scanner(System.in);
        System.out.println("\n*** Self-Service Kiosk :: Add Appointment ***\n");

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
        Date date = Date.valueOf(sc.nextLine().trim());
        if (!doctorEntitySessionBeanRemote.isAvailableAtDate(doctorEntity, date)) {
            throw new InvalidInputException("Doctor is not available!");
        }
        if (date.before(Date.valueOf(LocalDate.now().plusDays(2)))) {
            throw new InvalidInputException("Appointment should be booked 2 days in advance!");
        }
        if (patientEntitySessionBeanRemote.hasAppointmentOnDay(currentPatientEntity, date)) {
            throw new AlreadyBookedAppointment("Patient already has appointment on " + date.toString());
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

        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setDate(date);
        appointmentEntity.setDoctorEntity(doctorEntity);
        appointmentEntity.setPatientEntity(currentPatientEntity);
        appointmentEntity.setStartTime(time);

        appointmentEntitySessionBeanRemote.createNewAppointment(appointmentEntity);
        System.out.println(currentPatientEntity.getFullName()
                + " appointment with "
                + doctorEntity.getFullName()
                + " at " + time.toString().substring(0, 5)
                + " on " + date.toString() + " has been added.");

    }

    public void cancelAppointment() throws AppointmentNotFoundException, IllegalArgumentException, InputMismatchException {

        Scanner sc = new Scanner(System.in);
        System.out.println("\n*** Self-Service Kiosk :: Cancel Appointment ***\n");

        List<AppointmentEntity> appointments = appointmentEntitySessionBeanRemote.retrieveListOfAppointmentsByPatientId(currentPatientEntity.getPatientId());

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
