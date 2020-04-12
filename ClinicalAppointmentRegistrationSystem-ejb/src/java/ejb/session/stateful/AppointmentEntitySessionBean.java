/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import ejb.session.stateless.PatientEntitySessionBeanLocal;
import entity.AppointmentEntity;
import entity.PatientEntity;
import java.sql.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AlreadyBookedAppointment;
import util.exception.AppointmentInvalidException;
import util.exception.AppointmentNotFoundException;

/**
 *
 * @author p.tm
 */
@Stateful
@Local (AppointmentEntitySessionBeanLocal.class)
@Remote (AppointmentEntitySessionBeanRemote.class)
public class AppointmentEntitySessionBean implements AppointmentEntitySessionBeanRemote, AppointmentEntitySessionBeanLocal {

    @PersistenceContext(unitName = "ClinicalAppointmentRegistrationSystem-ejbPU")
    EntityManager em;
    
    @EJB
    private PatientEntitySessionBeanLocal patientEntitySessionBeanLocal;
    
    public AppointmentEntitySessionBean() {
    }

    @Override
    public void createNewAppointment(AppointmentEntity appointmentEntity) throws AppointmentInvalidException, AlreadyBookedAppointment {
        PatientEntity patientEntity = appointmentEntity.getPatientEntity();
        Date date = appointmentEntity.getDate();
        
        if (patientEntitySessionBeanLocal.hasAppointmentOnDay(patientEntity.getPatientId(), date)) {
            throw new AlreadyBookedAppointment("Patient already has appointment on " + date.toString());
        }
        
        try {
            em.persist(appointmentEntity);
            em.flush();
        } catch (PersistenceException ex) {
            throw new AppointmentInvalidException("Something went wrong");
        }
    }

    @Override
    public List<AppointmentEntity> retrieveListOfAppointments() {
        Query query = em.createQuery("Select a from AppointmentEntity a");
        return query.getResultList();
    }
    
    @Override
    public AppointmentEntity retrieveAppointmentById(Long appointmentId) throws AppointmentNotFoundException  {
        Query query = em.createQuery("SELECT a FROM AppointmentEntity a WHERE a.appointmentId = :id");
        query.setParameter("id", appointmentId);
        
        try {
            return (AppointmentEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new AppointmentNotFoundException("Appoint does not exist!");
        }
    }
    
    @Override
    public void updateAppointmentEntity(AppointmentEntity appointmentEntity) throws AppointmentNotFoundException {
        AppointmentEntity curr = retrieveAppointmentById(appointmentEntity.getAppointmentId());
        curr.setDate(appointmentEntity.getDate());
        curr.setDoctorEntity(appointmentEntity.getDoctorEntity());
        curr.setPatientEntity(appointmentEntity.getPatientEntity());
        curr.setStartTime(appointmentEntity.getStartTime());
    }

    @Override
    public AppointmentEntity cancelAppointment(Long appointmentId) throws AppointmentNotFoundException {
        AppointmentEntity curr = retrieveAppointmentById(appointmentId);
        em.remove(curr);
        return curr;
    }

    @Override
    public List<AppointmentEntity> retrieveListOfAppointmentsByPatientId(long pId) {
        Query query = em.createQuery("SELECT a from AppointmentEntity a WHERE a.patientEntity.patientId = :id");
        query.setParameter("id", pId);
        
        return query.getResultList();
    }
}
