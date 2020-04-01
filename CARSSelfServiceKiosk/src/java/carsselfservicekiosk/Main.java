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
import ejb.session.stateless.StaffEntitySessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author p.tm
 */
public class Main {

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

    public static void main(String[] args) {
        MainApp mainApp = new MainApp(
                doctorEntitySessionBeanRemote,
                patientEntitySessionBeanRemote,
                computationSessionBeanRemote,
                consultationSessionBeanRemote,
                appointmentEntitySessionBeanRemote);
        mainApp.runApp();
    }
}
