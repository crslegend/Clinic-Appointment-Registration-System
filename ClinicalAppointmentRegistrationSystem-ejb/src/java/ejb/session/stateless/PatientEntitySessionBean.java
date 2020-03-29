/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PatientEntity;
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
import util.exception.PatientExistException;
import util.exception.PatientNotFoundException;

/**
 *
 * @author crs
 */
@Stateless
@Local (PatientEntitySessionBeanLocal.class)
@Remote (PatientEntitySessionBeanRemote.class)
public class PatientEntitySessionBean implements PatientEntitySessionBeanRemote, PatientEntitySessionBeanLocal {

    @PersistenceContext(unitName = "ClinicalAppointmentRegistrationSystem-ejbPU")
    private EntityManager em;

    public PatientEntitySessionBean() {
    }

    @Override
    public void addNewPatient(PatientEntity newPatientEntity) throws PatientExistException {
        try {
            em.persist(newPatientEntity);
            em.flush();
        } catch (PersistenceException ex) {
            throw new PatientExistException("New Patient cannot be created: Patient's identity number already exists in database");
        }
    }
    
    @Override
    public void updatePatient(PatientEntity patientEntity) throws PatientNotFoundException {
        PatientEntity pe = retrievePatientByIdNum(patientEntity.getIdentityNumber());
        pe.setFirstName(patientEntity.getFirstName());
        pe.setLastName(patientEntity.getLastName());
        pe.setAge(patientEntity.getAge());
        pe.setPhone(patientEntity.getPhone());
        pe.setAddress(patientEntity.getAddress());
    }
    
    public void deletePatient(String identityNum) throws PatientNotFoundException {
        PatientEntity pe = retrievePatientByIdNum(identityNum);
        em.remove(pe);
    }

    @Override
    public PatientEntity retrievePatientByIdNum(String identityNum) throws PatientNotFoundException {
        Query query = em.createQuery("SELECT p FROM PatientEntity p WHERE p.identityNumber = :inIdentityNum");
        query.setParameter("inIdentityNum", identityNum);
        
        try {
            return (PatientEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new PatientNotFoundException("Patient with identity number " + identityNum + " does not exists!");
        }
    }
    
    @Override
    public List<PatientEntity> retrieveAllPatients() {
        Query query = em.createQuery("SELECT p FROM PatientEntity p");
        return query.getResultList();
    }
    
}
