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
public class StaffUsernameExistException extends Exception{

    public StaffUsernameExistException() {
    }

    public StaffUsernameExistException(String message) {
        super(message);
    }
    
}
