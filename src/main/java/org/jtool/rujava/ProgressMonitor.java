/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava;

public class ProgressMonitor {
    
    private int size;
    
    private int num;
    
    private int count;
    
    public ProgressMonitor() {
    }
    
    public void begin(int size) {
        this.size = size;
        this.count = 0;
        this.num = 0;
    }
    
    public void done() {
        System.out.println();
        System.out.flush();
    }
    
    public void work(int done) {
        count = count + done;
        if (size <= 100) {
            num++;
            display('.');
        } else {
            if (count * 100 >= size * num) {
                num++;
                display('.');
            }
        }
    }
    
    public void display(char ch) {
        System.out.print(ch);
        System.out.flush();
    }
    
    public void printWithCount(int done, String message) {
        count = count + done;
        System.out.println("(" + count + "/" + size + ") " + message);
        System.out.flush();
    }
    
    public void print(String message) {
        System.out.println(message);
        System.out.flush();
    }
}
