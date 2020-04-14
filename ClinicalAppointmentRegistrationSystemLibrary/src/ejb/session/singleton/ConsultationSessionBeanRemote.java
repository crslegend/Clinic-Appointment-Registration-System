/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import entity.DoctorEntity;
import entity.PatientEntity;
import java.sql.Date;
import java.sql.Time;
import javax.ejb.Remote;
import util.exception.AlreadyBookedAppointment;
import util.exception.AppointmentInvalidException;

/**
 *
 * @author p.tm
 */
@Remote
public interface ConsultationSessionBeanRemote {

    Long createNewConsultation(DoctorEntity doctorEntity, PatientEntity patientEntity, Time time, Date date) throws AppointmentInvalidException, AlreadyBookedAppointment;

    long confirmConsultation();
    
}
