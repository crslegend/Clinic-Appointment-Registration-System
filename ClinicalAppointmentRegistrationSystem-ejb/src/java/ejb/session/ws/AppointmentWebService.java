/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.singleton.ComputationSessionBeanLocal;
import ejb.session.stateful.AppointmentEntitySessionBeanLocal;
import ejb.session.stateless.DoctorEntitySessionBeanLocal;
import ejb.session.stateless.PatientEntitySessionBeanLocal;
import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.PatientEntity;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.AlreadyBookedAppointment;
import util.exception.AppointmentInvalidException;
import util.exception.AppointmentNotFoundException;
import util.exception.ClinicNotOpenException;
import util.exception.DoctorNotFoundException;

/**
 *
 * @author p.tm
 */
@WebService(serviceName = "AppointmentWebService")
@Stateless()
public class AppointmentWebService {

    @EJB
    private AppointmentEntitySessionBeanLocal appointmentEntitySessionBeanLocal;

    @EJB
    private PatientEntitySessionBeanLocal patientEntitySessionBeanLocal;

    @EJB
    private DoctorEntitySessionBeanLocal doctorEntitySessionBeanLocal;
    
    @PersistenceContext(unitName = "ClinicalAppointmentRegistrationSystem-ejbPU")
    private EntityManager em;

    @EJB
    private ComputationSessionBeanLocal computationSessionBeanLocal;

    @WebMethod
    public List<AppointmentEntity> retrieveListOfAppointmentsByPatientId(@WebParam long pId) {
        return appointmentEntitySessionBeanLocal.retrieveListOfAppointmentsByPatientId(pId);
    }

    @WebMethod
    public List<DoctorEntity> retrieveAllDoctors() {
        return doctorEntitySessionBeanLocal.retrieveAllDoctors();
    }

    @WebMethod
    public DoctorEntity retrieveDoctorById(@WebParam long doctorId) throws DoctorNotFoundException {
        return doctorEntitySessionBeanLocal.retrieveDoctorById(doctorId);
    }

    @WebMethod
    public Boolean isAvailableAtDate(@WebParam long doctorId, @WebParam String date) throws DoctorNotFoundException {
        System.err.println("*********** web param date :" + date);
       
        
        return doctorEntitySessionBeanLocal.isAvailableAtDate(doctorId, Date.valueOf(date));
    }

    @WebMethod
    public Boolean hasAppointmentOnDay(@WebParam long patientId, @WebParam String date) {
        return patientEntitySessionBeanLocal.hasAppointmentOnDay(patientId, Date.valueOf(date));
    }

    @WebMethod
    public List<String> getAllTimeSlots(@WebParam String date) throws ClinicNotOpenException {
        List<Time> timings = computationSessionBeanLocal.getAllTimeSlots(Date.valueOf(date));
        List<String> timingStrings = new ArrayList<>();
        for (Time t : timings) {
            timingStrings.add(t.toString());
        }
        return timingStrings;
    }

    @WebMethod
    public Boolean isAvailableAtTimeDate(@WebParam long doctorId, @WebParam String time, @WebParam String date) throws DoctorNotFoundException {
        return doctorEntitySessionBeanLocal.isAvailableAtTimeDate(doctorId, Time.valueOf(time), Date.valueOf(date));
    }

    @WebMethod
    public void createNewAppointment(@WebParam String date, @WebParam long doctorId, @WebParam long patientId, @WebParam String startTime) throws AppointmentInvalidException, AlreadyBookedAppointment {
        
        DoctorEntity doctorEntity = em.find(DoctorEntity.class, doctorId);
        PatientEntity patientEntity = em.find(PatientEntity.class, patientId);
        AppointmentEntity appointmentEntity = new AppointmentEntity(); 
        appointmentEntity.setDate(Date.valueOf(date));
        appointmentEntity.setDoctorEntity(doctorEntity);
        appointmentEntity.setPatientEntity(patientEntity);
        appointmentEntity.setStartTime(Time.valueOf(startTime));
        
        appointmentEntitySessionBeanLocal.createNewAppointment(appointmentEntity);
    }
    
    @WebMethod
    public AppointmentEntity retrieveAppointmentById(@WebParam Long appointmentId) throws AppointmentNotFoundException {
        return appointmentEntitySessionBeanLocal.retrieveAppointmentById(appointmentId);
    }
    
    @WebMethod
    public AppointmentEntity cancelAppointment(@WebParam Long appointmentId) throws AppointmentNotFoundException {
        return appointmentEntitySessionBeanLocal.cancelAppointment(appointmentId);
    }

}
