/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import javax.ejb.Local;
import util.exception.ClinicNotOpenException;

/**
 *
 * @author p.tm
 */
@Local
public interface ComputationSessionBeanLocal {

    List<Time> getNextSixTimeSlots() throws ClinicNotOpenException;

    List<Time> getAllTimeSlots(Date date) throws ClinicNotOpenException;

    int getDayOfWeek(Date date) throws ClinicNotOpenException;
    
}
