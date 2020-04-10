/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicalappointmentregistrationsystemclient;

import ejb.session.singleton.ComputationSessionBeanRemote;
import ejb.session.singleton.ConsultationSessionBeanRemote;
import ejb.session.stateful.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.DoctorEntitySessionBeanRemote;
import ejb.session.stateless.PatientEntitySessionBeanRemote;
import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.PatientEntity;
import entity.StaffEntity;
import java.sql.Time;
import java.sql.Date;
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
import util.exception.PatientExistException;
import util.exception.PatientNotFoundException;
import util.security.EncryptionHelper;

/**
 *
 * @author p.tm
 */
public class RegistrationModule {

    private PatientEntitySessionBeanRemote patientEntitySessionBeanRemote;
    private DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote;
    private ComputationSessionBeanRemote computationSessionBeanRemote;
    private ConsultationSessionBeanRemote consultationSessionBeanRemote;
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private StaffEntity currStaffEntity;

    public RegistrationModule() {
    }

    public RegistrationModule(
            PatientEntitySessionBeanRemote patientEntitySessionBeanRemote,
            DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote,
            StaffEntity staffEntity,
            ComputationSessionBeanRemote computationSessionBeanRemote,
            ConsultationSessionBeanRemote consultationSessionBeanRemote,
            AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote) {

        this.patientEntitySessionBeanRemote = patientEntitySessionBeanRemote;
        this.doctorEntitySessionBeanRemote = doctorEntitySessionBeanRemote;
        this.currStaffEntity = staffEntity;
        this.computationSessionBeanRemote = computationSessionBeanRemote;
        this.consultationSessionBeanRemote = consultationSessionBeanRemote;
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
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
                    try {
                        registerWalkInConsult();
                    } catch (DoctorNotFoundException | InvalidInputException | PatientNotFoundException | AlreadyBookedAppointment ex) {
                        System.out.println(ex.getMessage());
                    } catch (InputMismatchException | IllegalArgumentException ex) {
                        System.out.println("Invalid Input!");
                    } catch (ClinicNotOpenException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if (response == 3) {
                    try {
                        registerConsultByAppointment();
                    } catch (PatientNotFoundException | AppointmentNotFoundException ex) {
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
                if (input.length() == 6) {
                    Long num = Long.parseLong(input);
                    String hashPassword = EncryptionHelper.getInstance().byteArrayToHexString(EncryptionHelper.getInstance().doMD5Hashing(input));
                    pe.setPassword(hashPassword);
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
                   if (input.equalsIgnoreCase("F") || input.equalsIgnoreCase("M")) {
                        pe.setGender(input);
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
                    Long num = Long.parseLong(input);
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

        } catch (IllegalArgumentException | InputMismatchException ex) {
            throw new InvalidInputException("Invalid input! Age, phone and password has to be a number and password has to be a 6-digit number. Please try again!");
        }

    }

    public void registerWalkInConsult() throws DoctorNotFoundException, InvalidInputException, InputMismatchException, PatientNotFoundException, ClinicNotOpenException, AlreadyBookedAppointment {

        Scanner sc = new Scanner(System.in);
        System.out.println("\n*** CARS :: Registration Operation :: Register Walk-In Consultation ***\n");

        // get current date
        Date currentDate = Date.valueOf(LocalDate.now());

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

        // read in patient id
        System.out.print("\nEnter Patient Identity Number> ");
        String pId = sc.nextLine().trim();
        if (pId.length() <= 0) {
            throw new InvalidInputException("Invalid Patient Id!");
        }
        PatientEntity patientEntity = patientEntitySessionBeanRemote.retrievePatientByIdNum(pId);
        
        if (patientEntitySessionBeanRemote.hasAppointmentOnDay(patientEntity, currentDate)) {
            throw new AlreadyBookedAppointment("Patient already has appointment on " + currentDate.toString());
        }

        for (Time time : nextSixTimeSlots) {
            if (doctorEntitySessionBeanRemote.isAvailableAtTimeDate(currentDoctorEntity, time, currentDate)) {
                apptTime = time;
                break;
            }
        }

        try {
            long queueNumber = consultationSessionBeanRemote.createNewConsultation(currentDoctorEntity, patientEntity, apptTime, currentDate);
            System.out.println(patientEntity.getFullName()
                    + " appointment is confirmed with Dr. "
                    + currentDoctorEntity.getFullName()
                    + " at "
                    + apptTime.toString().substring(0, 5));
            System.out.println("Queue Number is: " + queueNumber + ".\n");
        } catch (AppointmentInvalidException ex) {
            System.out.println("Something went wrong while creating appointment");
        }

    }

    public void registerConsultByAppointment() throws PatientNotFoundException, AppointmentNotFoundException, InputMismatchException, IllegalArgumentException {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n*** CARS :: Registration Operation :: Register Consultation By Appointment ***\n");

        System.out.print("Enter Patient Identity Number> ");
        String pId = sc.nextLine().trim();
        PatientEntity patientEntity = patientEntitySessionBeanRemote.retrievePatientByIdNum(pId);

        System.out.println("\nAppointments: ");
        System.out.printf("%-3s|%-11s|%-6s|%-64s\n", "Id", "Date", "Time", "Doctor");

        List<AppointmentEntity> appointments = appointmentEntitySessionBeanRemote.retrieveListOfAppointmentsByPatientId(patientEntity.getPatientId());
        appointments.forEach(appt -> {
            System.out.printf("%-3s|%-11s|%-6s|%-64s\n", appt.getAppointmentId(),
                    appt.getDate().toString(),
                    appt.getStartTime().toString().substring(0, 5),
                    appt.getDoctorEntity().getFullName());
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

        System.out.println(patientEntity.getFullName()
                + " appointment is confirmed with Dr. "
                + appointmentEntity.getDoctorEntity().getFullName()
                + " at "
                + appointmentEntity.getStartTime().toString().substring(0, 5));
        System.out.println("Queue Number is: " + consultationSessionBeanRemote.confirmConsultation() + ".");
    }

}
