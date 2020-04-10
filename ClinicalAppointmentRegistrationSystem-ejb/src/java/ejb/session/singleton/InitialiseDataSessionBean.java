/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.DoctorEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import entity.DoctorEntity;
import entity.StaffEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.exception.DoctorExistException;
import util.exception.StaffNotFoundException;
import util.exception.StaffUsernameExistException;
import util.security.CryptographicHelper;

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
        String hashPassword = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing("password"));
        StaffEntity staffEntity = new StaffEntity("Eric", "Some", "manager", hashPassword);
        DoctorEntity doctorEntity1 = new DoctorEntity("Tan", "Ming", "S10011", "BMBS");
        DoctorEntity doctorEntity2 = new DoctorEntity("Clair", "Hahn", "S41221", "MBBCh");
        DoctorEntity doctorEntity3 = new DoctorEntity("Robert", "Blake", "S58201", "MBBS");
        
        try {
            staffEntitySessionBeanLocal.addNewStaff(staffEntity);
//            doctorEntitySessionBeanLocal.addNewDoctor(doctorEntity1);
//            doctorEntitySessionBeanLocal.addNewDoctor(doctorEntity2);
//            doctorEntitySessionBeanLocal.addNewDoctor(doctorEntity3);
            
        } catch (StaffUsernameExistException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }
}
