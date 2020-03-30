/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author p.tm
 */
public class AppointmentInvalidException extends Exception {

    /**
     * Creates a new instance of <code>InvalidAppointmentException</code>
     * without detail message.
     */
    public AppointmentInvalidException() {
    }

    /**
     * Constructs an instance of <code>InvalidAppointmentException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public AppointmentInvalidException(String msg) {
        super(msg);
    }
}
