/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicalappointmentregistrationsystemclient;

import ejb.session.singleton.ComputationSessionBeanRemote;
import ejb.session.singleton.ConsultationSessionBeanRemote;
import ejb.session.stateless.DoctorEntitySessionBeanRemote;
import ejb.session.stateless.PatientEntitySessionBeanRemote;
import ejb.session.stateless.StaffEntitySessionBeanRemote;
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

    public static void main(String[] args) {
        MainApp mainApp = new MainApp(
                staffEntitySessionBeanRemote,
                doctorEntitySessionBeanRemote,
                patientEntitySessionBeanRemote,
                computationSessionBeanRemote,
                consultationSessionBeanRemote);
        mainApp.runApp();
    }

}
