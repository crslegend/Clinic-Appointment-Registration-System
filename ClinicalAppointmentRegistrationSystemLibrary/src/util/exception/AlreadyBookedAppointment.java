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
public class AlreadyBookedAppointment extends Exception {

    /**
     * Creates a new instance of <code>AlreadyBookedAppointment</code> without
     * detail message.
     */
    public AlreadyBookedAppointment() {
    }

    /**
     * Constructs an instance of <code>AlreadyBookedAppointment</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AlreadyBookedAppointment(String msg) {
        super(msg);
    }
}
