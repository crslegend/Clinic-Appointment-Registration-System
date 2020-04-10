/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.PatientEntitySessionBeanLocal;
import entity.PatientEntity;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.InvalidLoginCredentialException;
import util.exception.PatientExistException;
import util.exception.PatientNotFoundException;
import util.exception.InvalidInputException;
import util.security.EncryptionHelper;

/**
 *
 * @author p.tm
 */
@WebService(serviceName = "CARSWebService")
@Stateless()
public class CARSRemoteUserWebService {

    @EJB
    private PatientEntitySessionBeanLocal patientEntitySessionBeanLocal;
    
    @WebMethod
    public PatientEntity patientLogin(@WebParam String pId, @WebParam String password) throws PatientNotFoundException, InvalidLoginCredentialException {
        return patientEntitySessionBeanLocal.patientLogin(pId, password);
    }
    
    @WebMethod
    public void addNewPatient(@WebParam PatientEntity newPatientEntity) throws PatientExistException {
        patientEntitySessionBeanLocal.addNewPatient(newPatientEntity);
    }
    
}
