/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava.detector;

import org.jtool.rujava.ErrorMessage;
import org.jtool.rujava.MutabilityAnnotation;
import org.jtool.rujava.OwnershipAnnotation;
import org.jtool.rujava.collector.DataFlow;
import org.jtool.rujava.collector.RuMethod;
import org.jtool.rujava.collector.RuProject;
import org.jtool.rujava.collector.RuStatement;
import org.jtool.rujava.collector.RuVariable;

import org.jtool.srcmodel.JavaFile;
import org.jtool.cfg.CFGNode;

import org.eclipse.jdt.core.dom.ASTNode;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class MutableReferenceChecker extends Checker {
    
    public MutableReferenceChecker() {
    }
    
    public void check(RuProject project) {
        for (RuMethod method : project.getMethods()) {
            //ErrorDetector.print(method);
            checkAssignment(method);
        }
    }
    
    public void checkAssignment(RuMethod method) {
        for (RuStatement st : method.getStatements()) {
            if (st.isAssignment(OwnershipAnnotation.BORROW, MutabilityAnnotation.MUTABLE)) {
                List<RuStatement> sharedStatements = RuStatement.sort(method.getSharedStatements(st));
                
                List<RuStatement> mutableBorrowStatements = new ArrayList<>();
                mutableBorrowStatements.addAll(sharedStatements.stream()
                        .filter(s -> s.isAssignment(OwnershipAnnotation.BORROW, MutabilityAnnotation.MUTABLE)).toList());
                if (mutableBorrowStatements.size() > 1) {
                    mutableBorrowStatements.remove(0);
                    
                    JavaFile jfile = method.getJaveMethod().getFile();
                    
                    for (RuStatement s : mutableBorrowStatements) {
                        List<ASTNode> errorNodes = getErrorNodes(s, s.getDefFirst(), s.getUseFirst());
                        method.addErrorMessage(new ErrorMessage(3, "AssignMutableBorrowed", jfile, errorNodes));
                    }
                }
                
                List<RuStatement> ownedStatements = new ArrayList<>();
                ownedStatements.addAll(sharedStatements.stream()
                        .filter(s -> s.isAssignment(OwnershipAnnotation.OWNED, MutabilityAnnotation.MUTABLE)).toList());
                ownedStatements.addAll(sharedStatements.stream()
                        .filter(s -> s.isAssignment(OwnershipAnnotation.OWNED, MutabilityAnnotation.IMMUTABLE)).toList());
                if (ownedStatements.size() > 0) {
                    JavaFile jfile = method.getJaveMethod().getFile();
                    
                    for (RuStatement s : ownedStatements) {
                        List<ASTNode> errorNodes = getErrorNodes(s, s.getDefFirst(), s.getUseFirst());
                        method.addErrorMessage(new ErrorMessage(3, "AssignOwned", jfile, errorNodes));
                    }
                }
                
                RuVariable def = st.getDefFirst();
                List<RuStatement> dstStatements = method.getDstStatementsOfDataFlow(st, def, DataFlow.Kind.defuse);
                Set<CFGNode> survivalDurationForMutableBorrow =
                        method.getSurvivalDuration(st, new HashSet<>(dstStatements));
                
                for (RuStatement borrowStatement : getBorrowStatements(method, st)) {
                    Set<CFGNode> survivalDurationForImmutableBorrow = method.getSurvivalDuration(borrowStatement);
                    
                    for (CFGNode node : survivalDurationForImmutableBorrow) {
                        if (!survivalDurationForMutableBorrow.contains(node)) {
                            JavaFile jfile = method.getJaveMethod().getFile();
                            
                            List<ASTNode> errorNodes = getErrorNodes(borrowStatement,
                                    borrowStatement.getDefFirst(), borrowStatement.getUseFirst());
                            method.addErrorMessage(new ErrorMessage(2, "AssignBorrowed", jfile, errorNodes));
                            break;
                        }
                    }
                }
            }
        }
    }
    
    private Set<RuStatement> getBorrowStatements(RuMethod method, RuStatement st) {
        Set<RuStatement> sharedStatements = method.collectForward(st);
        
        Set<RuStatement> statements = new HashSet<>();
        for (RuStatement sharedSt : sharedStatements) {
            if (sharedSt.isAssignment(OwnershipAnnotation.BORROW, MutabilityAnnotation.MUTABLE) ||
                sharedSt.isAssignment(OwnershipAnnotation.BORROW, MutabilityAnnotation.IMMUTABLE)) {
                statements.add(sharedSt);
            }
        }
        return statements;
    }
}
