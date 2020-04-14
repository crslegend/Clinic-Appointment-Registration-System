/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import ejb.session.stateless.DoctorEntitySessionBeanLocal;
import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.LeaveEntity;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.LeaveNotFoundException;
import util.exception.LeaveRejectedException;
import util.exception.DoctorNotFoundException;

/**
 *
 * @author p.tm
 */
@Stateful
@Local (LeaveEntitySessionBeanLocal.class)
@Remote (LeaveEntitySessionBeanRemote.class)
public class LeaveEntitySessionBean implements LeaveEntitySessionBeanRemote, LeaveEntitySessionBeanLocal {

    @PersistenceContext(unitName = "ClinicalAppointmentRegistrationSystem-ejbPU")
    EntityManager em;
    
    @EJB
    private DoctorEntitySessionBeanLocal doctorEntitySessionBeanLocal; 
    
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
    
    public void applyLeave(String registration, Date dateOfLeave) throws LeaveRejectedException, DoctorNotFoundException {
        if (dateOfLeave.after(Date.valueOf(LocalDate.now().plusDays(7)))) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateOfLeave);
            int wkNum1 = calendar.get(Calendar.WEEK_OF_YEAR);
            LeaveEntity leaveEntity = new LeaveEntity(dateOfLeave);
            
            DoctorEntity doctorEntity = doctorEntitySessionBeanLocal.retrieveDoctorByRegistration(registration);
            em.refresh(doctorEntity);
            List<LeaveEntity> listOfLeaveEntities = doctorEntity.getListOfLeaveEntities();
            List<AppointmentEntity> listOfAppointmentEntities = doctorEntity.getListOfAppointmentEntities();
            listOfLeaveEntities.size();
            listOfAppointmentEntities.size();
            
            // to check whether date of leave clashes with any appointments
            for (AppointmentEntity ae : listOfAppointmentEntities) {
                if (ae.getDate().toString().equals(dateOfLeave.toString())) {
                    throw new LeaveRejectedException("Leave cannot be applied: Doctor has an appointment with patient on " + dateOfLeave.toString());
                }
            }
            
            // to check whether any applied leaves exist in the same week as the date of leave
            for (LeaveEntity le : listOfLeaveEntities) {
                Date date = le.getStartDate();
                calendar.setTime(date);
                int wkNum2 = calendar.get(Calendar.WEEK_OF_YEAR);
                System.out.println(wkNum1 == wkNum2);
                if (wkNum1 == wkNum2) {
                    throw new LeaveRejectedException("Leave cannot be applied: There is already a leave applied in the same week!");
                }
            }
            leaveEntity.setDoctorEntity(doctorEntity);
            createNewLeaveEntity(leaveEntity);
        } else {
            throw new LeaveRejectedException("Leave cannot be applied: Leave has to be applied 1 week in advance!");
        }
    }
    
}
