/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicalappointmentregistrationsystemclient;

import ejb.session.singleton.ComputationSessionBeanRemote;
import ejb.session.singleton.ConsultationSessionBeanRemote;
import ejb.session.stateful.AppointmentEntitySessionBeanRemote;
import ejb.session.stateful.LeaveEntitySessionBeanRemote;
import ejb.session.stateless.DoctorEntitySessionBeanRemote;
import ejb.session.stateless.PatientEntitySessionBeanRemote;
import ejb.session.stateless.StaffEntitySessionBeanRemote;
import java.util.InputMismatchException;
import javax.ejb.EJB;

/**
 *
 * @author crs
 */
public class Main {

    @EJB
    private static StaffEntitySessionBeanRemote staffEntitySessionBeanRemote;
    @EJB
    private static DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote;
    @EJB
    private static PatientEntitySessionBeanRemote patientEntitySessionBeanRemote;
    @EJB
    private static ComputationSessionBeanRemote computationSessionBeanRemote;
    @EJB
    private static ConsultationSessionBeanRemote consultationSessionBeanRemote;
    @EJB
    private static AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    @EJB
    private static LeaveEntitySessionBeanRemote leaveEntitySessionBeanRemote;
    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(
                staffEntitySessionBeanRemote,
                doctorEntitySessionBeanRemote,
                patientEntitySessionBeanRemote,
                computationSessionBeanRemote,
                consultationSessionBeanRemote,
                appointmentEntitySessionBeanRemote,
                leaveEntitySessionBeanRemote);

        try {
            mainApp.runApp();
        } catch (IllegalArgumentException | InputMismatchException ex) {
            System.out.println("Input invalid! Please try again\n");
        }
        
    }

}
