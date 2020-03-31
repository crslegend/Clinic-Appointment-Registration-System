/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AppointmentEntitySessionBeanLocal;
import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.PatientEntity;
import java.sql.Date;
import java.sql.Time;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import util.exception.AppointmentInvalidException;

/**
 *
 * @author p.tm
 */
@Singleton
@Local (ConsultationSessionBeanLocal.class)
@Remote (ConsultationSessionBeanRemote.class)
public class ConsultationSessionBean implements ConsultationSessionBeanRemote, ConsultationSessionBeanLocal {

    private long queueNumber;
    private Date currentDate;
    
    @EJB
    private AppointmentEntitySessionBeanLocal appointmentEntitySessionBeanLocal;
    
    @PostConstruct
    public void postConstruct() {
        System.out.println("************* Donald Trump's a walking dick with blonde hair");
        
        queueNumber = 0;
        currentDate = new Date(System.currentTimeMillis());
    }

    @Override
    public Long createNewConsultation(
            DoctorEntity doctorEntity, PatientEntity patientEntity, Time time, Date date) throws AppointmentInvalidException {

        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setDate(date);
        appointmentEntity.setDoctorEntity(doctorEntity);
        appointmentEntity.setPatientEntity(patientEntity);
        appointmentEntity.setStartTime(time);

        appointmentEntitySessionBeanLocal.createNewAppointment(appointmentEntity);
        
        if (!new Date(System.currentTimeMillis()).toString().equals(currentDate.toString())) {
            queueNumber = 0;
            currentDate = new Date(System.currentTimeMillis());
        }
        
        queueNumber++;
        return queueNumber;
    }

    @Override
    public long confirmConsultation() {
        if (!new Date(System.currentTimeMillis()).toString().equals(currentDate.toString())) {
            queueNumber = 0;
            currentDate = new Date(System.currentTimeMillis());
        }
        
        queueNumber++;
        return queueNumber;
    }

}
