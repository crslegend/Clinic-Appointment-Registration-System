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
public class ClinicNotOpenException extends Exception {

    /**
     * Creates a new instance of <code>ClinicNotOpenException</code> without
     * detail message.
     */
    public ClinicNotOpenException() {
    }

    /**
     * Constructs an instance of <code>ClinicNotOpenException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ClinicNotOpenException(String msg) {
        super(msg);
    }
}
