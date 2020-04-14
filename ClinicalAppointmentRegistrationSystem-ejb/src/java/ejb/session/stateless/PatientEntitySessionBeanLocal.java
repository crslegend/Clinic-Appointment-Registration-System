/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PatientEntity;
import java.sql.Date;
import java.util.List;
import util.exception.InvalidLoginCredentialException;
import util.exception.PatientExistException;
import util.exception.PatientNotFoundException;

/**
 *
 * @author crs
 */
public interface PatientEntitySessionBeanLocal {

    public void addNewPatient(PatientEntity newPatientEntity) throws PatientExistException;

    public PatientEntity retrievePatientByIdNum(String identityNum) throws PatientNotFoundException;

    public List<PatientEntity> retrieveAllPatients();

    public void updatePatient(PatientEntity patientEntity) throws PatientNotFoundException;

    public PatientEntity patientLogin(String pId, String password) throws PatientNotFoundException, InvalidLoginCredentialException;
    
    public void deletePatient(String identityNum) throws PatientNotFoundException;

    public Boolean hasAppointmentOnDay(long patientId, Date date);

}
