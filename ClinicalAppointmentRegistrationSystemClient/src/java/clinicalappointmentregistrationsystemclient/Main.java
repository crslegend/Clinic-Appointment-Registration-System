/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicalappointmentregistrationsystemclient;

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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        MainApp mainApp = new MainApp(staffEntitySessionBeanRemote, doctorEntitySessionBeanRemote, patientEntitySessionBeanRemote);
        mainApp.runApp();
    }
    
}
