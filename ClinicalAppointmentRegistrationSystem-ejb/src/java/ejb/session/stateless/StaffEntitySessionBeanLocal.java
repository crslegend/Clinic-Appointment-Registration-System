/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.StaffEntity;
import java.util.List;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;
import util.exception.StaffUsernameExistException;

/**
 *
 * @author crs
 */
public interface StaffEntitySessionBeanLocal {
    
    public StaffEntity staffLogin(String username, String password) throws InvalidLoginCredentialException;

    public StaffEntity retrieveStaffByUsername(String username) throws StaffNotFoundException;
    
    public void addNewStaff(StaffEntity newStaffEntity) throws StaffUsernameExistException;
    
    public List<StaffEntity> retrieveAllStaffs();
    
    public StaffEntity retrieveStaffById(Long id) throws StaffNotFoundException;
    
    public void updateStaff(StaffEntity updatedStaffEntity) throws StaffNotFoundException;
    
    public void deleteStaff(Long id) throws StaffNotFoundException;
}
