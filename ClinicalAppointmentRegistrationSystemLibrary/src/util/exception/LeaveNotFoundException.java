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
public class LeaveNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>LeaveNotFoundException</code> without
     * detail message.
     */
    public LeaveNotFoundException() {
    }

    /**
     * Constructs an instance of <code>LeaveNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public LeaveNotFoundException(String msg) {
        super(msg);
    }
}
