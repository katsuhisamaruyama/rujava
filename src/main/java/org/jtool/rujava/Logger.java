/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava;

public class Logger {
    
    private static boolean verbose = false;
    
    private Logger() {
    }
    
    public static void setVerbose(boolean verb) {
        verbose = verb;
    }
    
    public static boolean isVerbose() {
        return verbose;
    }
    
    public static void print(String message) {
        if (verbose) {
            System.out.println(message);
            System.out.flush();
        }
    }
    
    public static void printError(String message) {
        System.err.println(message);
        System.err.flush();
    }
}
