/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava.detector;

import org.jtool.rujava.ErrorMessage;
import org.jtool.rujava.MutabilityAnnotation;
import org.jtool.rujava.OwnershipAnnotation;
import org.jtool.rujava.collector.RuMethod;
import org.jtool.rujava.collector.RuProject;
import org.jtool.rujava.collector.RuStatement;

import org.jtool.srcmodel.JavaFile;
import org.jtool.cfg.CFGNode;

import org.eclipse.jdt.core.dom.ASTNode;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class SharedReferenceChecker extends Checker {
    
    public SharedReferenceChecker() {
    }
    
    public void check(RuProject project) {
        for (RuMethod method : project.getMethods()) {
            // ErrorDetector.print(method);
            checkAssignment(method);
        }
    }
    
    public void checkAssignment(RuMethod method) {
        for (RuStatement st : method.getStatements()) {
            if (st.isAssignment(OwnershipAnnotation.BORROW, MutabilityAnnotation.IMMUTABLE)) {
                Set<RuStatement> sharedStatements = method.getSharedStatements(st);
                
                Set<RuStatement> moveMutableStatements = getMoveOrMutableStatements(sharedStatements);
                Set<CFGNode> survivalDuration = method.getSurvivalDuration(sharedStatements);
                
                for (RuStatement moveMutableStatement : moveMutableStatements) {
                    CFGNode moveNode = moveMutableStatement.getCFGStatement();
                    
                    if (survivalDuration.contains(moveNode)) {
                        JavaFile jfile = method.getJaveMethod().getFile();
                        
                        List<ASTNode> errorNodes = getErrorNodes(moveMutableStatement,
                                moveMutableStatement.getDefFirst(), moveMutableStatement.getUseFirst());
                        method.addErrorMessage(new ErrorMessage(2, "AssignBorrowed", jfile, errorNodes));
                    }
                }
            }
        }
    }
    
    private Set<RuStatement> getMoveOrMutableStatements(Set<RuStatement> sharedStatements) {
        Set<RuStatement> statements = new HashSet<>();
        for (RuStatement sharedSt : sharedStatements) {
            if (sharedSt.isAssignment(OwnershipAnnotation.OWNED, MutabilityAnnotation.MUTABLE) ||
                sharedSt.isAssignment(OwnershipAnnotation.OWNED, MutabilityAnnotation.IMMUTABLE) ||
                sharedSt.isAssignment(OwnershipAnnotation.BORROW, MutabilityAnnotation.MUTABLE)) {
                statements.add(sharedSt);
            }
        }
        return statements;
    }
}
