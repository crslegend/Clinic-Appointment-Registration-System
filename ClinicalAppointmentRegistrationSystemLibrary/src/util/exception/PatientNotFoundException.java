/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author crs
 */
public class PatientNotFoundException extends Exception {

    public PatientNotFoundException() {
    }

    public PatientNotFoundException(String message) {
        super(message);
    }
    
}
