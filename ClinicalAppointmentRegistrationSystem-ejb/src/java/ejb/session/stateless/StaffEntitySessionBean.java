/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.StaffEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;
import util.exception.StaffUsernameExistException;
import util.security.CryptographicHelper;

/**
 *
 * @author crs
 */
@Stateless
@Local(StaffEntitySessionBeanLocal.class)
@Remote (StaffEntitySessionBeanRemote.class)
public class StaffEntitySessionBean implements StaffEntitySessionBeanRemote, StaffEntitySessionBeanLocal {

    @PersistenceContext(unitName = "ClinicalAppointmentRegistrationSystem-ejbPU")
    private EntityManager em;

    public StaffEntitySessionBean() {
    }
    
    @Override
    public void addNewStaff(StaffEntity newStaffEntity) throws StaffUsernameExistException {
        try {
            em.persist(newStaffEntity);
            em.flush();
        } catch (PersistenceException ex) {
            throw new StaffUsernameExistException("New staff cannot be created!: The user name already exist");
        }
    }
    
    @Override
    public void updateStaff(StaffEntity updatedStaffEntity) throws StaffNotFoundException {
        StaffEntity staffEntity = retrieveStaffByUsername(updatedStaffEntity.getUserName());
        staffEntity.setFirstName(updatedStaffEntity.getFirstName());
        staffEntity.setLastName(updatedStaffEntity.getLastName());
    }
    
    @Override
    public void deleteStaff(Long id) throws StaffNotFoundException {
        StaffEntity staffEntity = retrieveStaffById(id);
        em.remove(staffEntity);
    }
    
    @Override
    public List<StaffEntity> retrieveAllStaffs() {
        Query query = em.createQuery("SELECT s FROM StaffEntity s");
        return query.getResultList();
    }    
    
    @Override
    public StaffEntity retrieveStaffById(Long id) throws StaffNotFoundException {
        StaffEntity staffEntity = em.find(StaffEntity.class, id);
        
        if (staffEntity != null) {
            return staffEntity;
        } else {
            throw new StaffNotFoundException("Staff ID " + id + " does not exist!");
        }
    }
            
    @Override
    public StaffEntity retrieveStaffByUsername(String username) throws StaffNotFoundException {
        Query query = em.createQuery("SELECT s FROM StaffEntity s WHERE s.userName = :inUsername");
        query.setParameter("inUsername", username);
        
        try {
            return (StaffEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new StaffNotFoundException("Staff Username " + username + " does not exist!");
        }
     }

    @Override
    public StaffEntity staffLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            StaffEntity staffEntity = retrieveStaffByUsername(username);
            String hashPassword = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password));
            
            if(staffEntity.getPassword().equals(hashPassword)) {
                return staffEntity;
            } else {
                throw new InvalidLoginCredentialException("Invalid password!");
            }
        } catch (StaffNotFoundException ex) {
            throw new InvalidLoginCredentialException(ex.getMessage());
        }
    }
  
}
