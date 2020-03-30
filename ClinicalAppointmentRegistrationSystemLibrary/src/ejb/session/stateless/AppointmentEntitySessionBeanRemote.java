/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AppointmentEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.AppointmentInvalidException;
import util.exception.AppointmentNotFoundException;

/**
 *
 * @author p.tm
 */
@Remote
public interface AppointmentEntitySessionBeanRemote {

    void createNewAppointment(AppointmentEntity appointmentEntity) throws AppointmentInvalidException;

    List<AppointmentEntity> retrieveListOfAppointments();

    void updateAppointmentEntity(AppointmentEntity appointmentEntity) throws AppointmentNotFoundException;

    AppointmentEntity retrieveAppointmentById(Long appointmentId) throws AppointmentNotFoundException;

    AppointmentEntity cancelAppointment(Long appointmentId) throws AppointmentNotFoundException;
    
}
