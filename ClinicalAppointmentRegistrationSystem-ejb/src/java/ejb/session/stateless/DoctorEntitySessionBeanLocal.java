/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DoctorEntity;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import util.exception.DeleteDoctorException;
import util.exception.DoctorExistException;
import util.exception.DoctorNotFoundException;

/**
 *
 * @author crs
 */
public interface DoctorEntitySessionBeanLocal {
    
    public void addNewDoctor(DoctorEntity newDoctorEntity) throws DoctorExistException;
    
    public DoctorEntity retrieveDoctorByRegistration(String registration) throws DoctorNotFoundException;
    
    public List<DoctorEntity> retrieveAllDoctors();
    
    public void updateDoctor(DoctorEntity newDoctorEntity) throws DoctorNotFoundException;

    public List<DoctorEntity> retrieveDoctorsOnDuty();

    public Boolean isAvailableAtTimeDate(long doctorId, Time time, Date date) throws DoctorNotFoundException;

    public Boolean isAvailableAtDate(long doctorEntity, Date date) throws DoctorNotFoundException;

    public DoctorEntity retrieveDoctorById(long doctorId) throws DoctorNotFoundException;
    
    public void deleteDoctor(String registration) throws DoctorNotFoundException, DeleteDoctorException;
    
}
