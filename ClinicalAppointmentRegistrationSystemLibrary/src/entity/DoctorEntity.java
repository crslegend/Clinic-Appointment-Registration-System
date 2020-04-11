/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author crs
 */
@Entity
@XmlRootElement
public class DoctorEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;
    @Column(nullable = false, length = 32)
    private String firstName;
    @Column(nullable = false, length = 32)
    private String lastName;
    @Column(nullable = false, unique = true, length = 32)
    private String registration;
    @Column(nullable = false, length = 32)
    private String qualifications;
    
    @OneToMany(mappedBy = "doctorEntity")
    private List<LeaveEntity> listOfLeaveEntities;
    
    @OneToMany(mappedBy = "doctorEntity")
    private List<AppointmentEntity> listOfAppointmentEntities;
    

    public DoctorEntity() {
    }

    public DoctorEntity(String firstName, String lastName, String registration, String qualifications) {
        this();
        
        this.firstName = firstName;
        this.lastName = lastName;
        this.registration = registration;
        this.qualifications = qualifications;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (doctorId != null ? doctorId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the doctorId fields are not set
        if (!(object instanceof DoctorEntity)) {
            return false;
        }
        DoctorEntity other = (DoctorEntity) object;
        if ((this.doctorId == null && other.doctorId != null) || (this.doctorId != null && !this.doctorId.equals(other.doctorId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.DoctorEntity[ id=" + doctorId + " ]";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public List<LeaveEntity> getListOfLeaveEntities() {
        return listOfLeaveEntities;
    }

    public void setListOfLeaveEntities(List<LeaveEntity> listOfLeaveEntities) {
        this.listOfLeaveEntities = listOfLeaveEntities;
    }

    @XmlTransient
    public List<AppointmentEntity> getListOfAppointmentEntities() {
        return listOfAppointmentEntities;
    }

    public void setListOfAppointmentEntities(List<AppointmentEntity> listOfAppointmentEntities) {
        this.listOfAppointmentEntities = listOfAppointmentEntities;
    }
    
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
    
}
