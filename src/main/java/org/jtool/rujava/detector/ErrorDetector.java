/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava.detector;

import org.jtool.rujava.Logger;
import org.jtool.rujava.collector.DataFlow;
import org.jtool.rujava.collector.RuMethod;
import org.jtool.rujava.collector.RuProject;

import org.jtool.cfg.CFG;

public class ErrorDetector {
    
    private final RuProject project;
    
    private final ImmutabilityChecker immutabilityChecker;
    private final AssignmentChecker asignmentChecker;
    private final MoveChecker moveChecker;
    private final ReferenceChecker referenceChecker;
    private final SharedReferenceChecker sharedReferenceChecker;
    private final MutableReferenceChecker mutableReferenceChecker;
    
    public ErrorDetector(RuProject project) {
        this(project, true, true);
    }
    
    public ErrorDetector(RuProject project, boolean allowImmutableToMutable, boolean allowMutableBorrow) {
        this.project = project;
        
        this.immutabilityChecker = new ImmutabilityChecker();
        this.asignmentChecker = new AssignmentChecker(allowImmutableToMutable, allowMutableBorrow);
        this.moveChecker = new MoveChecker();
        this.referenceChecker = new ReferenceChecker();
        this.sharedReferenceChecker = new SharedReferenceChecker();
        this.mutableReferenceChecker = new MutableReferenceChecker();
    }
    
    public void detect() {
        Logger.print("-Checking the immutability for variables");
        immutabilityChecker.check(project);
        
        Logger.print("-Checking the assignment possibility for variables");
        asignmentChecker.check(project);
        
        Logger.print("-Checking the ownership movement for variables");
        moveChecker.check(project);
        
        Logger.print("-Checking the correctness of references for variables");
        referenceChecker.check(project);
        sharedReferenceChecker.check(project);
        mutableReferenceChecker.check(project);
    }
    
    public static void print(RuMethod method) {
        CFG cfg = method.getCFG();
        cfg.print();
        System.out.println();
        
        DataFlow.sort(method.getDataFlows()).forEach(f -> System.out.println(f.toString()));
        System.out.println();
    }
}
