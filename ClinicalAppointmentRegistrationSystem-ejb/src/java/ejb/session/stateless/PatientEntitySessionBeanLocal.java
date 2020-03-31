/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PatientEntity;
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

    PatientEntity patientLogin(String pId, String password) throws PatientNotFoundException, InvalidLoginCredentialException;

}
