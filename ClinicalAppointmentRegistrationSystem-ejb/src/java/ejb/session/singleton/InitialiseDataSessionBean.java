/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.StaffEntitySessionBeanLocal;
import entity.StaffEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.exception.StaffNotFoundException;
import util.exception.StaffUsernameExistException;

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
        StaffEntity staffEntity = new StaffEntity("Eric", "Some", "manager", "password");
        
        try {
            staffEntitySessionBeanLocal.addNewStaff(staffEntity);
        } catch (StaffUsernameExistException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }
}
