/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.LeaveEntity;
import java.sql.Date;
import java.sql.Time;
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
import util.exception.DoctorExistException;
import util.exception.DoctorNotFoundException;
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

    // WIP
    public void deleteDoctor(String registration) throws DoctorNotFoundException {
        DoctorEntity doctorEntity = retrieveDoctorByRegistration(registration);
        em.remove(doctorEntity);
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

    // WIP
    public void applyLeave(String registration) throws LeaveRejectedException {

    }

    @Override
    public List<DoctorEntity> retrieveDoctorsOnDuty() {

        // get today's date
        Date currentDate = new Date(System.currentTimeMillis());
        List<DoctorEntity> doctors = retrieveAllDoctors();
        for (DoctorEntity de : doctors) {
            if (!isAvailableAtDate(de, currentDate)) {
                doctors.remove(de);
            }
        } // end outer
        return doctors;
    }

    @Override
    public Boolean isAvailableAtTimeDate(DoctorEntity doctorEntity, Time time, Date date) {

        List<AppointmentEntity> appointments = doctorEntity.getListOfAppointmentEntities();

        for (AppointmentEntity ae : appointments) {
            if (ae.getStartTime().equals(time) && ae.getDate().equals(date)) {
                return Boolean.FALSE;
            }
        }

        return Boolean.TRUE;
    }

    @Override
    public Boolean isAvailableAtDate(DoctorEntity doctorEntity, Date currentDate) {
        List<LeaveEntity> leaveRecords = doctorEntity.getListOfLeaveEntities();
        for (LeaveEntity le : leaveRecords) {
            if (le.getStartDate().equals(currentDate)) {
                return Boolean.FALSE;
            }
        } // end inner
        return Boolean.TRUE;
    }

}
