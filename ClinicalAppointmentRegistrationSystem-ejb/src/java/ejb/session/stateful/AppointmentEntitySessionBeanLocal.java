/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import entity.AppointmentEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.AlreadyBookedAppointment;
import util.exception.AppointmentInvalidException;
import util.exception.AppointmentNotFoundException;

/**
 *
 * @author p.tm
 */
@Local
public interface AppointmentEntitySessionBeanLocal {

    public void createNewAppointment(AppointmentEntity appointmentEntity) throws AppointmentInvalidException, AlreadyBookedAppointment;

    public List<AppointmentEntity> retrieveListOfAppointments();

    public void updateAppointmentEntity(AppointmentEntity appointmentEntity) throws AppointmentNotFoundException;

    public AppointmentEntity retrieveAppointmentById(Long appointmentId) throws AppointmentNotFoundException;

    public AppointmentEntity cancelAppointment(Long appointmentId) throws AppointmentNotFoundException;

    public List<AppointmentEntity> retrieveListOfAppointmentsByPatientId(long pId);
    
    
}
