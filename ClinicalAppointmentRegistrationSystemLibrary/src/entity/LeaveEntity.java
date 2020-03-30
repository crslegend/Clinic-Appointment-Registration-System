/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author crs
 */
@Entity
public class LeaveEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveId;
    @Column(nullable = false)
    private Date startDate;
    @Column(nullable = false)
    private Date endDate;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private DoctorEntity doctorEntity;

    
    public LeaveEntity() {
    }

    public LeaveEntity(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(Long leaveId) {
        this.leaveId = leaveId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (leaveId != null ? leaveId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the leaveId fields are not set
        if (!(object instanceof LeaveEntity)) {
            return false;
        }
        LeaveEntity other = (LeaveEntity) object;
        if ((this.leaveId == null && other.leaveId != null) || (this.leaveId != null && !this.leaveId.equals(other.leaveId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.LeaveEntity[ id=" + leaveId + " ]";
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public DoctorEntity getDoctorEntity() {
        return doctorEntity;
    }

    public void setDoctorEntity(DoctorEntity doctorEntity) {
        this.doctorEntity = doctorEntity;
    }
    
}