/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author p.tm
 */
@Remote
public interface ComputationSessionBeanRemote {

    List<Time> getNextSixTimeSlots();

    List<Time> getAllTimeSlots(Date date);
    
}
