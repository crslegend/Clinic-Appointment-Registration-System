/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.LeaveEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.LeaveNotFoundException;

/**
 *
 * @author p.tm
 */
@Local
public interface LeaveEntitySessionBeanLocal {

    void createNewLeaveEntity(LeaveEntity leaveEntity);

    void updateLeaveEntity(LeaveEntity leaveEntity) throws LeaveNotFoundException;

    List<LeaveEntity> retrieveAllLeaveEntities();

    LeaveEntity deleteLeaveEntity(LeaveEntity leaveEntity) throws LeaveNotFoundException;

    LeaveEntity retrieveLeaveEntityById(Long leaveId) throws LeaveNotFoundException;
    
}
