/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.DoctorEntitySessionBeanLocal;
import ejb.session.stateless.PatientEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import entity.DoctorEntity;
import entity.PatientEntity;
import entity.StaffEntity;
import java.sql.Date;
import java.sql.Time;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.exception.DoctorExistException;
import util.exception.PatientExistException;
import util.exception.StaffNotFoundException;
import util.exception.StaffUsernameExistException;
import util.security.EncryptionHelper;

/**
 *
 * @author crs
 */
@Singleton
@LocalBean
@Startup
public class InitialiseDataSessionBean {

    @EJB
    private StaffEntitySessionBeanLocal staffEntitySessionBeanLocal;
    
    @EJB
    private DoctorEntitySessionBeanLocal doctorEntitySessionBeanLocal;
    
    @EJB
    private PatientEntitySessionBeanLocal patientEntitySessionBeanLocal;
    
    
    public InitialiseDataSessionBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
        try {
            staffEntitySessionBeanLocal.retrieveStaffByUsername("manager");
        } catch (StaffNotFoundException ex) {
            initialiseData();
        }
    }
    
    public void initialiseData() {
        String hashPassword = EncryptionHelper.getInstance().byteArrayToHexString(EncryptionHelper.getInstance().doMD5Hashing("password"));
        StaffEntity staffEntity1 = new StaffEntity("Eric", "Some", "manager", hashPassword);
        StaffEntity staffEntity2 = new StaffEntity("Victoria", "Newton", "nurse", hashPassword);
        DoctorEntity doctorEntity1 = new DoctorEntity("Tan", "Ming", "S10011", "BMBS");
        DoctorEntity doctorEntity2 = new DoctorEntity("Clair", "Hahn", "S41221", "MBBCh");
        DoctorEntity doctorEntity3 = new DoctorEntity("Robert", "Blake", "S58201", "MBBS");
        
        hashPassword = EncryptionHelper.getInstance().byteArrayToHexString(EncryptionHelper.getInstance().doMD5Hashing("001001"));
        PatientEntity patientEntity1 = new PatientEntity("Sarah", "Yi", "S9867027A", "F", 22, "93718799", "13, Clementi Road", hashPassword);
        
        hashPassword = EncryptionHelper.getInstance().byteArrayToHexString(EncryptionHelper.getInstance().doMD5Hashing("123123"));
        PatientEntity patientEntity2 = new PatientEntity("Rajesh", "Singh", "G1314207T", "M", 36, "93506839", "15, Mountbatten Road", hashPassword);
        
        try {
            staffEntitySessionBeanLocal.addNewStaff(staffEntity1);
            staffEntitySessionBeanLocal.addNewStaff(staffEntity2);
            doctorEntitySessionBeanLocal.addNewDoctor(doctorEntity1);
            doctorEntitySessionBeanLocal.addNewDoctor(doctorEntity2);
            doctorEntitySessionBeanLocal.addNewDoctor(doctorEntity3);
            patientEntitySessionBeanLocal.addNewPatient(patientEntity1);
            patientEntitySessionBeanLocal.addNewPatient(patientEntity2);
            
        } catch (StaffUsernameExistException | DoctorExistException | PatientExistException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }
}
