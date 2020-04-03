/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.LeaveEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.LeaveNotFoundException;

/**
 *
 * @author p.tm
 */
@Stateless
@Local (LeaveEntitySessionBeanLocal.class)
@Remote (LeaveEntitySessionBeanRemote.class)
public class LeaveEntitySessionBean implements LeaveEntitySessionBeanRemote, LeaveEntitySessionBeanLocal {

    @PersistenceContext(unitName = "ClinicalAppointmentRegistrationSystem-ejbPU")
    EntityManager em;
    
    public LeaveEntitySessionBean() {
    }

    @Override
    public void createNewLeaveEntity(LeaveEntity leaveEntity) {
        em.persist(leaveEntity);
        em.flush();
    }

    @Override
    public void updateLeaveEntity(LeaveEntity leaveEntity) throws LeaveNotFoundException {
        LeaveEntity curr = retrieveLeaveEntityById(leaveEntity.getLeaveId());
        curr.setDoctorEntity(leaveEntity.getDoctorEntity());
        curr.setStartDate(leaveEntity.getStartDate());
    }

    @Override
    public List<LeaveEntity> retrieveAllLeaveEntities() {
        return em.createQuery("Select l from LeaveEntity l").getResultList();
    }

    @Override
    public LeaveEntity deleteLeaveEntity(LeaveEntity leaveEntity) throws LeaveNotFoundException {
        LeaveEntity curr = retrieveLeaveEntityById(leaveEntity.getLeaveId());
        em.remove(curr);
        return curr;
    }

    @Override
    public LeaveEntity retrieveLeaveEntityById(Long leaveId) throws LeaveNotFoundException {
        Query query = em.createQuery("Select l from LeaveEntity l where l.leaveId = :id");
        query.setParameter("id", leaveId);
        
        try {
            return (LeaveEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            throw new LeaveNotFoundException("Leave record does not exist!");
        }
    }
    
}
