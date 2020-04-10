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
import util.security.EncryptionHelper;

/**
 *
 * @author crs
 */
@Entity
public class PatientEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long patientId;
    @Column(nullable = false, length = 32)
    private String firstName;
    @Column(nullable = false, length = 32)
    private String lastName;
    @Column(nullable = false, unique = true, length = 32)
    private String identityNumber;
    @Column(nullable = false, length = 2)
    private String gender;
    @Column(nullable = false)
    private Integer age;
    @Column(nullable = false, length = 32)
    private String phone;
    @Column(nullable = false, length = 32)
    private String address;
    @Column(nullable = false, length = 32)
    private String password;
    
    @OneToMany(mappedBy = "patientEntity")
     private List<AppointmentEntity> listOfAppointmentEntities;

    public PatientEntity() {
    }

    public PatientEntity(String firstName, String lastName, String identityNumber, String gender, Integer age, String phone, String address, String password) {
        this();
        
        this.firstName = firstName;
        this.lastName = lastName;
        this.identityNumber = identityNumber;
        this.gender = gender;
        this.age = age;
        this.phone = phone;
        this.address = address;
        this.password = password;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (patientId != null ? patientId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the patientId fields are not set
        if (!(object instanceof PatientEntity)) {
            return false;
        }
        PatientEntity other = (PatientEntity) object;
        if ((this.patientId == null && other.patientId != null) || (this.patientId != null && !this.patientId.equals(other.patientId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PatientEntity[ id=" + patientId + " ]";
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

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        String hashPassword = EncryptionHelper.getInstance().byteArrayToHexString(EncryptionHelper.getInstance().doMD5Hashing(password));
        this.password = hashPassword;
    }

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
