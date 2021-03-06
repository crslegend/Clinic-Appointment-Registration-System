/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import ejb.session.stateful.LeaveEntitySessionBeanLocal;
import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.LeaveEntity;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.DeleteDoctorException;
import util.exception.DoctorExistException;
import util.exception.DoctorNotFoundException;
import util.exception.LeaveNotFoundException;
import util.exception.LeaveRejectedException;

/**
 *
 * @author crs
 */
@Stateless
@Local(DoctorEntitySessionBeanLocal.class)
@Remote(DoctorEntitySessionBeanRemote.class)
public class DoctorEntitySessionBean implements DoctorEntitySessionBeanRemote, DoctorEntitySessionBeanLocal {

    @PersistenceContext(unitName = "ClinicalAppointmentRegistrationSystem-ejbPU")
    private EntityManager em;
    
    @EJB
    private LeaveEntitySessionBeanLocal leaveEntitySessionBeanLocal;
    
    public DoctorEntitySessionBean() {
    }

    @Override
    public void addNewDoctor(DoctorEntity newDoctorEntity) throws DoctorExistException {
        try {
            em.persist(newDoctorEntity);
            em.flush();
        } catch (PersistenceException ex) {
            throw new DoctorExistException("New doctor cannot be created!: The registration already exist");
        }
    }

    @Override
    public void updateDoctor(DoctorEntity newDoctorEntity) throws DoctorNotFoundException {
        DoctorEntity doctorEntity = retrieveDoctorByRegistration(newDoctorEntity.getRegistration());
        doctorEntity.setFirstName(newDoctorEntity.getFirstName());
        doctorEntity.setLastName(newDoctorEntity.getLastName());
        doctorEntity.setQualifications(newDoctorEntity.getQualifications());
    }

    @Override
    public void deleteDoctor(String registration) throws DoctorNotFoundException, DeleteDoctorException {
        DoctorEntity doctorEntity = retrieveDoctorByRegistration(registration);
        List<AppointmentEntity> listOfAppointmentEntities = doctorEntity.getListOfAppointmentEntities();
        listOfAppointmentEntities.size();
        
        if (listOfAppointmentEntities.isEmpty()) {
            List<LeaveEntity> listOfLeaveEntities = doctorEntity.getListOfLeaveEntities();
            listOfLeaveEntities.size();
            
            if (!listOfLeaveEntities.isEmpty()) {
                for (LeaveEntity leave : listOfLeaveEntities) {
                    try {
                         leaveEntitySessionBeanLocal.deleteLeaveEntity(leave);
                    } catch (LeaveNotFoundException ex) {
                      // do nothing
                    }
                }
            }
            
            em.remove(doctorEntity);
        } else {
            throw new DeleteDoctorException("Doctor cannot be deleted as doctor has associating appointments!");
        }
    }

    @Override
    public DoctorEntity retrieveDoctorByRegistration(String registration) throws DoctorNotFoundException {
        Query query = em.createQuery("SELECT d FROM DoctorEntity d WHERE d.registration = :inRegistration");
        query.setParameter("inRegistration", registration);

        try {
            return (DoctorEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new DoctorNotFoundException("Doctor with registration " + registration + " does not exist!");
        }
    }

    @Override
    public List<DoctorEntity> retrieveAllDoctors() {
        Query query = em.createQuery("SELECT d FROM DoctorEntity d");
        return query.getResultList();
    }

    @Override
    public List<DoctorEntity> retrieveDoctorsOnDuty()  {

        // get today's date
        Date currentDate = new Date(System.currentTimeMillis());
        List<DoctorEntity> doctors = retrieveAllDoctors();
        for (DoctorEntity de : doctors) {
            try {
                em.refresh(de);
                if (!isAvailableAtDate(de.getDoctorId(), currentDate)) {
                    doctors.remove(de);
                }
            } catch (DoctorNotFoundException ex) {
                // do nothing at all bro
            }
        } // end outer
        return doctors;
    }

    @Override
    public Boolean isAvailableAtTimeDate(long doctorId, Time time, Date date) throws DoctorNotFoundException {

        DoctorEntity newDoctorEntity = retrieveDoctorById(doctorId);
        em.refresh(newDoctorEntity);
        
        List<AppointmentEntity> appointments = newDoctorEntity.getListOfAppointmentEntities();
        appointments.size();
        
//        System.out.println(newDoctorEntity.getDoctorId() + " " + appointments.size());
        
        for (AppointmentEntity ae : appointments) {
//            System.out.println("Time: " + ae.getStartTime().equals(time));
//            System.out.println("Date: " + ae.getDate().equals(date));
            if (ae.getStartTime().equals(time) && ae.getDate().toString().equals(date.toString())) {
                return Boolean.FALSE;
            }
        }

        return Boolean.TRUE;
    }

    @Override
    public Boolean isAvailableAtDate(long doctorId, Date currentDate) throws DoctorNotFoundException {
        
        DoctorEntity newDoctorEntity = retrieveDoctorById(doctorId);
        em.refresh(newDoctorEntity);
        
        List<LeaveEntity> leaveRecords = newDoctorEntity.getListOfLeaveEntities();
        leaveRecords.size();
        
        for (LeaveEntity le : leaveRecords) {
            if (le.getStartDate().equals(currentDate)) {
                return Boolean.FALSE;
            }
        } // end inner
        return Boolean.TRUE;
    }

    @Override
    public DoctorEntity retrieveDoctorById(long doctorId) throws DoctorNotFoundException {
        Query query = em.createQuery("SELECT d from DoctorEntity d WHERE d.doctorId = :id");
        query.setParameter("id", doctorId);
        
        try {
            return (DoctorEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new DoctorNotFoundException("Doctor with id " + doctorId + " does not exist!");
        }
    }

}
