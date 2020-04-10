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
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
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
    public Boolean isAvailableAtDate(@WebParam DoctorEntity doctorEntity, @WebParam Date date) throws DoctorNotFoundException {
        return doctorEntitySessionBeanLocal.isAvailableAtDate(doctorEntity, date);
    }

    @WebMethod
    public Boolean hasAppointmentOnDay(@WebParam PatientEntity patientEntity, @WebParam Date date) {
        return patientEntitySessionBeanLocal.hasAppointmentOnDay(patientEntity, date);
    }

    @WebMethod
    public List<Time> getAllTimeSlots(@WebParam Date date) throws ClinicNotOpenException {
        return computationSessionBeanLocal.getAllTimeSlots(date);
    }

    @WebMethod
    public Boolean isAvailableAtTimeDate(@WebParam DoctorEntity doctorEntity, @WebParam Time time, @WebParam Date date) throws DoctorNotFoundException {
        return doctorEntitySessionBeanLocal.isAvailableAtDate(doctorEntity, date);
    }

    @WebMethod
    public void createNewAppointment(@WebParam AppointmentEntity appointmentEntity) throws AppointmentInvalidException {
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
