/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carsautomatedmachineclient;

import java.util.InputMismatchException;

/**
 *
 * @author p.tm
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainApp mainApp = new MainApp();
        try {
            mainApp.runApp();
        } catch (InputMismatchException ex) {
            System.out.println("Invalid input! Please try again");
        }
    }
    
}
