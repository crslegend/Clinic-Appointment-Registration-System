/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import entity.LeaveEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.LeaveNotFoundException;

/**
 *
 * @author p.tm
 */
@Remote
public interface LeaveEntitySessionBeanRemote {

    public void createNewLeaveEntity(LeaveEntity leaveEntity);

    public void updateLeaveEntity(LeaveEntity leaveEntity) throws LeaveNotFoundException;

    public List<LeaveEntity> retrieveAllLeaveEntities();

    public LeaveEntity deleteLeaveEntity(LeaveEntity leaveEntity) throws LeaveNotFoundException;

    public LeaveEntity retrieveLeaveEntityById(Long leaveId) throws LeaveNotFoundException;
    
}
